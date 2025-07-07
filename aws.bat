@echo off
setlocal

rem Ajusta estas variables antes de usar
set AWS_REGION=us-east-2
set AWS_ACCOUNT_ID=445444484464
set ECS_CLUSTER=candidate-management-cluster
set SUBNETS=subnet-xyz
set SEC_GROUPS=sg-xyz

rem 1) Login en ECR
aws ecr get-login-password --region %AWS_REGION% ^
  | docker login --username AWS --password-stdin %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com
if errorlevel 1 exit /b 1

rem 2) Build & push de imágenes
for %%SVC in (common auth-service customer-service) do (
  echo === Construyendo y push de %%SVC ===
  docker build -f %%SVC/Dockerfile -t %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com/%%SVC:latest .
  if errorlevel 1 exit /b 1
  docker push %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com/%%SVC:latest
  if errorlevel 1 exit /b 1
)

rem 3) Crear cluster (si no existe)
aws ecs create-cluster --cluster-name %ECS_CLUSTER% --region %AWS_REGION% >nul 2>&1

rem 4) Registrar task-defs
aws ecs register-task-definition --cli-input-json file://auth-task-def.json --region %AWS_REGION% || exit /b 1
aws ecs register-task-definition --cli-input-json file://customer-task-def.json --region %AWS_REGION% || exit /b 1

rem 5) Crear o actualizar servicios Fargate
aws ecs create-service --cluster %ECS_CLUSTER% --service-name auth-service --task-definition auth-service --desired-count 1 --launch-type FARGATE --network-configuration "awsvpcConfiguration={subnets=[%SUBNETS%],securityGroups=[%SEC_GROUPS%],assignPublicIp=ENABLED}" --region %AWS_REGION%  2>nul || ^
  aws ecs update-service --cluster %ECS_CLUSTER% --service auth-service --force-new-deployment --region %AWS_REGION%

aws ecs create-service --cluster %ECS_CLUSTER% --service-name customer-service --task-definition customer-service --desired-count 1 --launch-type FARGATE --network-configuration "awsvpcConfiguration={subnets=[%SUBNETS%],securityGroups=[%SEC_GROUPS%],assignPublicIp=ENABLED}" --region %AWS_REGION% 2>nul || ^
  aws ecs update-service --cluster %ECS_CLUSTER% --service customer-service --force-new-deployment --region %AWS_REGION%

echo.
echo === Despliegue completado ===
pause

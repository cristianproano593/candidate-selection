@echo off
SET AWS=C:\Program Files\Amazon\AWSCLIV2\aws.exe
FOR /F "tokens=*" %%i IN ('"%AWS%" ecs list-tasks --cluster bright-horse-yk2wzg --service-name task-user2-service-7jt7lcff --query "taskArns[0]" --output text') DO (
  SET TASK=%%i
  FOR /F "tokens=*" %%j IN ('"%AWS%" ecs describe-tasks --cluster bright-horse-yk2wzg --tasks !TASK! --query "tasks[0].attachments[0].details[?name==`networkInterfaceId`].value" --output text') DO (
    SET ENI=%%j
    FOR /F "tokens=*" %%k IN ('"%AWS%" ec2 describe-network-interfaces --network-interface-ids !ENI! --query "NetworkInterfaces[0].Association.PublicIp" --output text') DO (
      curl -X POST http://%%k:8090/auth/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"password\"}"
    )
  )
)

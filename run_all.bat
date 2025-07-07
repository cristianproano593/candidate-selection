@echo off
cd /d "D:\SPRINGBOOT PROJECTS\candidate_management"

start "common" cmd /k "run_common.bat"
timeout /t 10 >nul
start "auth-service" cmd /k "run_auth_service.bat"
start "customer-service" cmd /k "run_customer_service.bat"

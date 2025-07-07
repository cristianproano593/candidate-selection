@echo off
cd /d "D:\SPRINGBOOT PROJECTS\candidate_management\customer-service"

set DB_HOST=bdd-users.clwoggwyavt1.us-east-2.rds.amazonaws.com
set DB_PORT=3306
set DB_NAME=bdd_users
set DB_USER=root
set DB_PASS=P#adad7#
set JWT_SECRET=c29jaWFscGFwZXJjaGlsZHJlbmJlZW5qb2JtYWduZXRodXNiYW5kY29sbGVjdGJ1cm4=
set JWT_EXPIRATION_MS=3600000

call ..\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod -Dserver.port=8091
pause

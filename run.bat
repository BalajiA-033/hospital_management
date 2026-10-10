@echo off
echo ========================================================
echo   Launching Smart Hospital Management System
echo ========================================================
echo.

set CP=target\classes;%USERPROFILE%\.m2\repository\com\formdev\flatlaf\3.4\flatlaf-3.4.jar;%USERPROFILE%\.m2\repository\org\mindrot\jbcrypt\0.4\jbcrypt-0.4.jar;%USERPROFILE%\.m2\repository\com\mysql\mysql-connector-j\8.3.0\mysql-connector-j-8.3.0.jar;%USERPROFILE%\.m2\repository\com\h2database\h2\2.2.224\h2-2.2.224.jar

java -cp "%CP%" com.hospital.Main

pause

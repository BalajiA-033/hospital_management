Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "  Launching Smart Hospital Management System" -ForegroundColor Green
Write-Host "========================================================" -ForegroundColor Cyan

$m2 = "$HOME\.m2\repository"
$cp = "target\classes;$m2\com\formdev\flatlaf\3.4\flatlaf-3.4.jar;$m2\org\mindrot\jbcrypt\0.4\jbcrypt-0.4.jar;$m2\com\mysql\mysql-connector-j\8.3.0\mysql-connector-j-8.3.0.jar;$m2\com\h2database\h2\2.2.224\h2-2.2.224.jar"

java -cp $cp com.hospital.Main

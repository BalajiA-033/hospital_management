Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "  Starting Smart Hospital Management System Web App" -ForegroundColor Green
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "Starting Web Server at http://localhost:8080 ..." -ForegroundColor Yellow

java -cp . WebServer 8080

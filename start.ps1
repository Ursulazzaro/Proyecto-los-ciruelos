Start-Process powershell -ArgumentList "-NoExit", "-ExecutionPolicy Bypass", "-Command", "./start-backend.ps1"

# Ir al frontend (ruta relativa)
Set-Location ".\frontend\frontend"

# Levantar Angular
ng serve

cd ..
cd ..
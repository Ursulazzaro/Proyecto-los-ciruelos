# Ir al backend (ruta relativa)
Set-Location ".\backend\Seminario"

# Variables de entorno
$env:DB_URL="jdbc:mysql://localhost:3307/los_ciruelos?createDatabaseIfNotExist=true&serverTimezone=UTC"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="1234"
$env:FIREBASE_CREDENTIALS_PATH="./src/main/resources/firebase_credentials.json"

./mvnw.cmd spring-boot:run
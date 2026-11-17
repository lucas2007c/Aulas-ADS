@echo off
chcp 65001 > nul
echo ========================================================
echo   Executando Testes Automatizados (AutoEstoque)
echo ========================================================

if not exist bin\app\TestRunner.class (
    echo Binarios de teste nao encontrados. Executando build.bat primeiro...
    call build.bat
)

java -cp "bin;lib\mysql-connector-j-8.4.0.jar" app.TestRunner
pause

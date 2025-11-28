@echo off
REM Script de compilación - Motor NoSQL (Windows)

echo === Compilando Motor de Base de Datos NoSQL ===

REM Crear directorio de salida
if not exist out mkdir out

REM Compilar todos los archivos Java
javac -d out ^
    src/main/java/com/motor/utils/*.java ^
    src/main/java/com/motor/algorithms/*.java ^
    src/main/java/com/motor/index/*.java ^
    src/main/java/com/motor/storage/*.java ^
    src/main/java/com/motor/core/*.java ^
    src/main/java/com/motor/query/*.java ^
    src/main/java/com/motor/Main.java

if %ERRORLEVEL% EQU 0 (
    echo Compilacion exitosa
    echo.
    echo === Ejecutando demostracion ===
    java -cp out com.motor.Main
) else (
    echo Error de compilacion
)

pause

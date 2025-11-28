@echo off
echo Compilando Motor de Base de Datos No Relacional...
echo.

REM Crear directorio de salida
if not exist "bin" mkdir bin

REM Compilar todos los archivos Java
javac -d bin src/main/java/com/motor/index/*.java
javac -d bin -cp bin src/main/java/com/motor/core/*.java
javac -d bin -cp bin src/main/java/com/motor/utils/*.java
javac -d bin -cp bin src/main/java/com/motor/storage/*.java
javac -d bin -cp bin src/main/java/com/motor/algorithms/*.java
javac -d bin -cp bin src/main/java/com/motor/query/*.java
javac -d bin -cp bin src/main/java/com/motor/Main.java

echo.
echo Compilacion completada!
echo Para ejecutar: java -cp bin com.motor.Main
pause

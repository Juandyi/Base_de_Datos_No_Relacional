#!/bin/bash
# Script de compilación - Motor NoSQL

set -e
echo "=== Compilando Motor de Base de Datos NoSQL ==="

# Crear directorio de salida
mkdir -p bin

# Buscar y compilar recursivamente todos los archivos .java bajo src/main/java
echo "Compilando fuentes Java en src/main/java -> bin"
if ! find src/main/java -name "*.java" -print0 | xargs -0 javac -d bin; then
    echo "✗ Error de compilación"
    exit 1
fi

echo "✓ Compilación exitosa"
echo ""
echo "=== Ejecutando demostración ==="
java -cp bin com.motor.Main

exit 0

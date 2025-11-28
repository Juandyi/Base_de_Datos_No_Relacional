
#!/bin/bash
set -e
echo "Compilando Motor de Base de Datos No Relacional..."
echo

# Crear directorio de salida
mkdir -p bin

echo "Compilando fuentes Java en src/main/java -> bin"
if ! find src/main/java -name "*.java" -print0 | xargs -0 javac -d bin; then
	echo "✗ Error de compilación"
	exit 1
fi

echo
echo "Compilación completada!"
echo "Para ejecutar: java -cp bin com.motor.Main"

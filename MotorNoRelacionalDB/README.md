# Motor de Base de Datos No Relacional

## Proyecto Final - Ciencias de la Computación I

## Autores:

Juan Diego Moreno Ramos - 20242020009
Jeronimo Bernal Rodriguez - 

### Temas del Pensum Implementados

| Tema | Implementación |
|------|----------------|
| **Algoritmos de Búsqueda** | `SearchAlgorithms.java` - Secuencial O(n), Binaria O(log n) |
| **Algoritmos de Ordenamiento** | `SortAlgorithms.java` - QuickSort, MergeSort, BubbleSort |
| **Análisis de Complejidad** | `ComplexityAnalyzer.java` - Notación O, Ω, θ |
| **Estructuras Lineales** | `Collection.java` - Lista de documentos |
| **Árbol B** | `BTreeIndex.java` - Índice con O(log n) |
| **Árbol B+** | `BPlusTreeIndex.java` - Índice con búsqueda por rango |
| **Tabla Hash** | `HashIndex.java` - Índice con O(1) promedio |
| **TDA** | `Document.java`, `Collection.java`, `Database.java` |

---

## Estructura del Proyecto

```
/MotorNoRelacionalDB
├── /src/main/java/com/motor
│   ├── Main.java                    # Punto de entrada
│   ├── /core
│   │   ├── Database.java            # Base de datos
│   │   ├── Collection.java          # Colección (usa listas e índices)
│   │   └── Document.java            # Documento JSON
│   ├── /storage
│   │   ├── StorageManager.java      # Persistencia
│   │   └── FileHandler.java         # Manejo de archivos
│   ├── /index
│   │   ├── Index.java               # Interfaz de índices
│   │   ├── HashIndex.java           # Tabla Hash - O(1)
│   │   ├── BTreeIndex.java          # Árbol B - O(log n)
│   │   └── BPlusTreeIndex.java      # Árbol B+ - O(log n) + rangos
│   ├── /query
│   │   ├── QueryProcessor.java      # Procesador de consultas
│   │   ├── QueryParser.java         # Parser de sintaxis
│   │   └── QueryExecutor.java       # Ejecutor
│   ├── /algorithms
│   │   ├── SearchAlgorithms.java    # Búsqueda secuencial/binaria
│   │   └── SortAlgorithms.java      # QuickSort, MergeSort, etc.
│   └── /utils
│       ├── JsonUtils.java           # Serialización JSON
│       ├── ComplexityAnalyzer.java  # Análisis de complejidad
│       └── Exceptions.java          # Excepciones personalizadas
```

---

## Compilar y Ejecutar

### Opción 1: Usar el script incluido (recomendado)

Windows (PowerShell o doble clic):

```powershell
.\compilar.bat
```

Linux / macOS:

```bash
./compilar.sh
```

El script genera los `.class` en el directorio `bin` y deja listo el comando de ejecución.

### Opción 2: Compilar manualmente con `javac`

En Windows PowerShell (compila recursivamente todos los `.java` bajo `src/main/java`):

```powershell
# Recopila todas las rutas .java y las pasa a javac
javac -d bin (Get-ChildItem -Path src\\main\\java -Recurse -Filter *.java | ForEach-Object -ExpandProperty FullName)
```

En sistemas Unix (bash/zsh) con glob recursivo habilitado o usando un shell que soporte `**`:

```bash
javac -d bin src/main/java/com/motor/**/*.java
```

### Ejecutar la aplicación

```powershell
java -cp bin com.motor.Main
```

Notas:
- Si modificas el orden de compilación a mano, asegúrate de compilar primero los paquetes que son dependencias (por ejemplo `index` y `core`) o compilar todo de una vez.
- Recomiendo usar el script `compilar.bat` en Windows para evitar problemas de orden y classpath.

---

## Complejidades Implementadas

| Estructura | Insertar | Buscar | Eliminar |
|------------|----------|--------|----------|
| HashIndex | O(1)* | O(1)* | O(1)* |
| BTreeIndex | O(log n) | O(log n) | O(log n) |
| BPlusTreeIndex | O(log n) | O(log n) | O(log n) |
| Lista | O(1) | O(n) | O(n) |

| Ordenamiento | Mejor | Promedio | Peor |
|--------------|-------|----------|------|
| QuickSort | O(n log n) | O(n log n) | O(n²) |
| MergeSort | O(n log n) | O(n log n) | O(n log n) |
| BubbleSort | O(n) | O(n²) | O(n²) |

*Caso promedio, puede degradar a O(n) con colisiones

---

## Ejemplos de Uso

```java
// Crear base de datos
Database db = new Database("miDB");

// Crear colección
Collection usuarios = db.createCollection("usuarios");

// Crear índice (Árbol B+)
usuarios.createIndex("email", "bplus");

// Insertar documento
usuarios.insert(Map.of("nombre", "Juan", "email", "juan@mail.com"));

// Buscar (usa el índice)
Document doc = usuarios.findOne(Map.of("email", "juan@mail.com"));

// Ordenar por campo
List<Document> ordenados = usuarios.findSorted("nombre", "quicksort", true);
```

---

## Sintaxis de Consultas

```
INSERT coleccion {campo:"valor", campo2:123}
FIND coleccion {campo:"valor"}
FINDONE coleccion {campo:"valor"}
UPDATE coleccion WHERE {campo:"valor"} SET {campo:"nuevo"}
DELETE coleccion WHERE {campo:"valor"}
CREATE_INDEX coleccion campo tipo
COUNT coleccion
```

---

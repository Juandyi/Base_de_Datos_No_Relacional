package com.motor.algorithms;

import java.util.Arrays;

/**
 * ALGORITMOS DE ORDENAMIENTO
 * 
 * Tipos implementados:
 * 1. QuickSort (Divide y Vencerás)
 * 2. MergeSort (Divide y Vencerás)
 * 3. BubbleSort (Fuerza Bruta)
 */
public class SortAlgorithms {
    
    /**
     * QUICKSORT (Divide y Vencerás)
     * 
     * Estrategia:
     * 1. Elegir un pivote
     * 2. Particionar: menores a la izq, mayores a la der
     * 3. Ordenar recursivamente cada partición
     * 
     * Recurrencia: T(n) = 2T(n/2) + O(n)  [caso promedio]
     * 
     * Complejidad:
     * - Mejor caso: O(n log n)
     * - Caso promedio: O(n log n)
     * - Peor caso: O(n²) - cuando el pivote es siempre el menor/mayor
     * 
     * Espacio: O(log n) para la pila de recursión
     */
    public static <T extends Comparable<T>> void quickSort(T[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);   // Ordenar izquierda
            quickSort(arr, pivotIndex + 1, high);  // Ordenar derecha
        }
    }
    
    private static <T extends Comparable<T>> int partition(T[] arr, int low, int high) {
        T pivot = arr[high];  // Último elemento como pivote
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (arr[j].compareTo(pivot) <= 0) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }
    
    /**
     * MERGESORT (Divide y Vencerás)
     * 
     * Estrategia:
     * 1. Dividir el arreglo en dos mitades
     * 2. Ordenar cada mitad recursivamente
     * 3. Mezclar las dos mitades ordenadas
     * 
     * Recurrencia: T(n) = 2T(n/2) + O(n)
     * 
     * Complejidad:
     * - Mejor caso: O(n log n)
     * - Caso promedio: O(n log n)
     * - Peor caso: O(n log n) ← Siempre garantizado
     * 
     * Espacio: O(n) - necesita arreglo auxiliar
     */
    public static <T extends Comparable<T>> void mergeSort(T[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            
            mergeSort(arr, left, mid);        // Ordenar mitad izquierda
            mergeSort(arr, mid + 1, right);   // Ordenar mitad derecha
            merge(arr, left, mid, right);     // Mezclar
        }
    }
    
    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> void merge(T[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        Object[] L = new Object[n1];
        Object[] R = new Object[n2];
        
        // Copiar datos a arreglos temporales
        for (int i = 0; i < n1; i++) L[i] = arr[left + i];
        for (int j = 0; j < n2; j++) R[j] = arr[mid + 1 + j];
        
        // Mezclar los arreglos temporales
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (((T) L[i]).compareTo((T) R[j]) <= 0) {
                arr[k] = (T) L[i];
                i++;
            } else {
                arr[k] = (T) R[j];
                j++;
            }
            k++;
        }
        
        // Copiar elementos restantes
        while (i < n1) { arr[k] = (T) L[i]; i++; k++; }
        while (j < n2) { arr[k] = (T) R[j]; j++; k++; }
    }
    
    /**
     * BUBBLESORT (Fuerza Bruta)
     * 
     * Estrategia: Comparar pares adyacentes y intercambiar si están desordenados.
     * Repetir hasta que no haya intercambios.
     * 
     * Complejidad:
     * - Mejor caso: O(n) - arreglo ya ordenado
     * - Caso promedio: O(n²)
     * - Peor caso: O(n²) - arreglo en orden inverso
     * 
     * Espacio: O(1) - ordenamiento in-place
     * 
     * Nota: Ineficiente, solo para fines educativos
     */
    public static <T extends Comparable<T>> void bubbleSort(T[] arr) {
        int n = arr.length;
        boolean swapped;
        
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].compareTo(arr[j + 1]) > 0) {
                    swap(arr, j, j + 1);
                    swapped = true;
                }
            }
            // Optimización: si no hubo intercambios, ya está ordenado
            if (!swapped) break;
        }
    }
    
    // Método auxiliar para intercambiar elementos
    private static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    /**
     * Demostración de los algoritmos
     */
    public static void demo() {
        System.out.println("\n=== DEMOSTRACIÓN: ALGORITMOS DE ORDENAMIENTO ===");
        
        Integer[] original = {64, 34, 25, 12, 22, 11, 90, 5, 77, 30};
        System.out.println("Arreglo original: " + Arrays.toString(original));
        
        // QuickSort
        Integer[] arr1 = original.clone();
        long inicio = System.nanoTime();
        quickSort(arr1, 0, arr1.length - 1);
        long tiempo = System.nanoTime() - inicio;
        System.out.println("\nQuickSort:");
        System.out.println("  Resultado: " + Arrays.toString(arr1));
        System.out.println("  Tiempo: " + tiempo + " ns");
        System.out.println("  Complejidad: O(n log n) promedio");
        
        // MergeSort
        Integer[] arr2 = original.clone();
        inicio = System.nanoTime();
        mergeSort(arr2, 0, arr2.length - 1);
        tiempo = System.nanoTime() - inicio;
        System.out.println("\nMergeSort:");
        System.out.println("  Resultado: " + Arrays.toString(arr2));
        System.out.println("  Tiempo: " + tiempo + " ns");
        System.out.println("  Complejidad: O(n log n) garantizado");
        
        // BubbleSort
        Integer[] arr3 = original.clone();
        inicio = System.nanoTime();
        bubbleSort(arr3);
        tiempo = System.nanoTime() - inicio;
        System.out.println("\nBubbleSort:");
        System.out.println("  Resultado: " + Arrays.toString(arr3));
        System.out.println("  Tiempo: " + tiempo + " ns");
        System.out.println("  Complejidad: O(n²)");
    }
}

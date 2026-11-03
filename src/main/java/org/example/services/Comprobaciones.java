package org.example.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

// Comprueba los archivos y prepara las carpetas necesarias
public class Comprobaciones {

    // Estas comprobaciones se usan directamente, sin crear un objeto de esta clase
    private Comprobaciones() {
    }

    public static void comprobarOrigen(File origen) throws IOException {
        if (!origen.isDirectory()) {
            throw new IOException("No existe la carpeta " + origen.getAbsolutePath() + ". Ejecuta el programa desde la raíz del proyecto.");
        }
    }

    public static File buscarUltimoCompleto(File backups) throws IOException {
        File[] carpetas = listarArchivos(backups);
        File ultimo = null;

        for (int i = 0; i < carpetas.length; i++) {
            File carpeta = carpetas[i];
            File completo = new File(carpeta, "completo");

            // Solo tenemos en cuenta las copias completas que hayan terminado
            if (carpeta.getName().startsWith("backup_") && !carpeta.getName().endsWith(".pendiente") && completo.isDirectory()) {

                // La fecha del nombre permite saber cuál es el más reciente
                if (ultimo == null || carpeta.getName().compareTo(ultimo.getName()) > 0) {
                    ultimo = carpeta;
                }
            }
        }
        return ultimo;
    }

    public static boolean necesitaCopia(File archivo, String ruta, File completo) throws IOException {
        if (completo == null) {
            return true;
        }
        // Buscamos el mismo archivo dentro de la copia completa
        File guardado = new File(completo, ruta);
        if (!guardado.isFile()) {
            return true; // Es un archivo nuevo
        }
        // Comparamos lo que contiene el archivo con lo que guardamos en el completo.
        // Si devuelve -1 son iguales; cualquier otro resultado indica un cambio.
        return Files.mismatch(archivo.toPath(), guardado.toPath()) != -1;
    }

    public static File[] listarArchivos(File carpeta) throws IOException {
        File[] archivos = carpeta.listFiles();
        // Si no podemos leer la carpeta, avisamos del problema
        if (archivos == null) {
            throw new IOException("No se pudo leer la carpeta: " + carpeta);
        }
        return archivos;
    }

    public static void prepararCarpetas(File origen, File backups) throws IOException {
        comprobarOrigen(origen);
        crearCarpeta(backups);
    }

    public static void crearCarpeta(File carpeta) throws IOException {
        // Creamos esta carpeta y las que hagan falta para llegar hasta ella
        if (!carpeta.isDirectory() && !carpeta.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta: " + carpeta);
        }
    }
}

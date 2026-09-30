package org.example.services;

import java.io.File;
import java.io.IOException;

// Comprueba los archivos y prepara las carpetas necesarias
public class Comprobaciones {

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

            if (carpeta.getName().startsWith("backup_") && !carpeta.getName().endsWith(".pendiente") && completo.isDirectory()) {

                // La fecha del nombre permite saber cuál es el más reciente
                if (ultimo == null || carpeta.getName().compareTo(ultimo.getName()) > 0) {
                    ultimo = carpeta;
                }
            }
        }
        return ultimo;
    }

    public static boolean necesitaCopia(File archivo, String ruta, File completo) {
        if (completo == null) {
            return true;
        }
        File guardado = new File(completo, ruta);
        if (!guardado.isFile()) {
            return true; // Es un archivo nuevo
        }
        // Se copia si se ha modificado después de la copia completa
        return archivo.lastModified() > guardado.lastModified();
    }

    public static File[] listarArchivos(File carpeta) throws IOException {
        File[] archivos = carpeta.listFiles();
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
        if (!carpeta.isDirectory() && !carpeta.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta: " + carpeta);
        }
    }
}

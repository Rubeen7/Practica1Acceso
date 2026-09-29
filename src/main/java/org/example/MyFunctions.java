package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class MyFunctions {

    private File origen = new File("src/main/java/org/example/images");
    private File backups = new File("backups");

    // Creación de backups

    public ResultadoBackup crearCompleto() throws IOException {
        comprobarOrigen();

        File grupo = nuevaCarpeta(backups, "backup_");
        File completo = new File(grupo, "completo");

        // null indica que se copian todos los archivos, sin comparar
        int cantidad = copiarCarpeta(origen, completo, "", null);
        File terminado = terminarCopia(grupo);

        return new ResultadoBackup(new File(terminado, "completo"), cantidad);
    }

    public ResultadoBackup crearIncremental() throws IOException {
        comprobarOrigen();
        File grupo = buscarUltimoCompleto();

        if (grupo == null) {
            throw new IOException("Primero debes crear un backup completo (opción 1).");
        }

        File completo = new File(grupo, "completo");
        File incremental = nuevaCarpeta(grupo, "incremental_");
        int cantidad = copiarCarpeta(origen, incremental, "", completo);
        File terminado = terminarCopia(incremental);

        return new ResultadoBackup(terminado, cantidad);
    }

    // Copia de archivos y comparación con el backup completo

    private int copiarCarpeta(File actual, File destino, String ruta, File completo)
            throws IOException {

        int cantidad = 0;

        // En el completo se conservan también las carpetas vacías
        if (completo == null) {
            crearCarpeta(destino);
        }

        File[] archivos = listarArchivos(actual);

        for (int i = 0; i < archivos.length; i++) {
            File archivo = archivos[i];
            String rutaArchivo = ruta + archivo.getName();
            File copia = new File(destino, archivo.getName());

            if (Files.isSymbolicLink(archivo.toPath())) {
                throw new IOException("No se admiten enlaces simbólicos: " + archivo);
            } else if (archivo.isDirectory()) {
                // Para una subcarpeta, repetimos este método con esa carpeta
                cantidad += copiarCarpeta(archivo, copia, rutaArchivo + "/", completo);
            } else if (archivo.isFile()) {
                if (necesitaCopia(archivo, rutaArchivo, completo)) {
                    crearCarpeta(destino);
                    Files.copy(
                            archivo.toPath(),
                            copia.toPath(),
                            StandardCopyOption.COPY_ATTRIBUTES
                    );
                    cantidad++;
                }
            } else {
                throw new IOException("No se puede leer el archivo: " + archivo);
            }
        }

        return cantidad;
    }

    private boolean necesitaCopia(File archivo, String ruta, File completo) {
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

    // Métodos auxiliares para trabajar con carpetas

    private void comprobarOrigen() throws IOException {
        if (!origen.isDirectory()) {
            throw new IOException("No existe la carpeta " + origen.getAbsolutePath()
                    + ". Ejecuta el programa desde la raíz del proyecto.");
        }

        crearCarpeta(backups);
    }

    private File buscarUltimoCompleto() throws IOException {
        File[] carpetas = listarArchivos(backups);
        Arrays.sort(carpetas);

        for (int i = carpetas.length - 1; i >= 0; i--) {
            File carpeta = carpetas[i];
            File completo = new File(carpeta, "completo");

            if (carpeta.getName().startsWith("backup_")
                    && !carpeta.getName().endsWith(".pendiente")
                    && completo.isDirectory()) {

                return carpeta;
            }
        }

        return null;
    }

    private File nuevaCarpeta(File padre, String prefijo) throws IOException {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSSSSSSSS");
        String fecha = LocalDateTime.now().format(formato);
        File carpeta = new File(padre, prefijo + fecha + ".pendiente");

        if (!carpeta.mkdir()) {
            throw new IOException("No se pudo crear la carpeta: " + carpeta);
        }
        return carpeta;
    }

    private File terminarCopia(File carpeta) throws IOException {
        // Quitamos .pendiente solo cuando todos los archivos se han copiado
        String nombre = carpeta.getName().replace(".pendiente", "");
        File terminada = new File(carpeta.getParentFile(), nombre);

        Files.move(carpeta.toPath(), terminada.toPath());
        return terminada;
    }

    private void crearCarpeta(File carpeta) throws IOException {
        if (!carpeta.isDirectory() && !carpeta.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta: " + carpeta);
        }
    }

    private File[] listarArchivos(File carpeta) throws IOException {
        File[] archivos = carpeta.listFiles();

        if (archivos == null) {
            throw new IOException("No se pudo leer la carpeta: " + carpeta);
        }

        return archivos;
    }
}

package org.example.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupCompleto {

    // Ponemos la fecha en el nombre para saber cuándo se hizo cada copia
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private final File origen;
    private final File backups;
    private File carpetaCreada;

    public BackupCompleto(File origen, File backups) {
        this.origen = origen;
        this.backups = backups;
    }

    public int crearCompleto() throws IOException {
        Comprobaciones.prepararCarpetas(origen, backups);

        // En esta carpeta guardamos la copia completa y luego las copias de los cambios
        File grupo = nuevaCarpeta(backups, "backup_");
        File completo = new File(grupo, "completo");

        // Pasamos null porque queremos copiarlo todo, sin comparar con otra copia
        int cantidad = copiarCarpeta(origen, completo, "", null);
        File terminado = terminarCopia(grupo);

        carpetaCreada = new File(terminado, "completo");
        return cantidad;
    }

    public File getCarpetaCreada() {
        return carpetaCreada;
    }

    // Usamos este método para copiar archivos en los dos tipos de backup
    public static int copiarCarpeta(File actual, File destino, String ruta, File completo)
            throws IOException {

        int cantidad = 0;
        boolean carpetaCreada = false;

        // En el completo se conservan también las carpetas vacías
        if (completo == null) {
            Comprobaciones.crearCarpeta(destino);
            carpetaCreada = true;
        }

        File[] archivos = Comprobaciones.listarArchivos(actual);

        for (int i = 0; i < archivos.length; i++) {
            File archivo = archivos[i];
            // Recordamos en qué carpeta está el archivo para buscarlo en la copia completa
            String rutaArchivo = ruta + archivo.getName();
            File copia = new File(destino, archivo.getName());

            if (Files.isSymbolicLink(archivo.toPath())) {
                throw new IOException("No se admiten enlaces simbólicos: " + archivo);
            } else if (archivo.isDirectory()) {
                // Si encontramos otra carpeta dentro, copiamos también su contenido
                cantidad += copiarCarpeta(archivo, copia, rutaArchivo + "/", completo);
            } else if (archivo.isFile()) {
                if (Comprobaciones.necesitaCopia(archivo, rutaArchivo, completo)) {
                    if (!carpetaCreada) {
                        Comprobaciones.crearCarpeta(destino);
                        carpetaCreada = true;
                    }
                    // Conservamos también la fecha del archivo para poder compararla después
                    Files.copy(archivo.toPath(), copia.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
                    cantidad++;
                }
            } else {
                throw new IOException("No se puede leer el archivo: " + archivo);
            }
        }

        return cantidad;
    }

    public static File nuevaCarpeta(File padre, String prefijo) throws IOException {
        String fecha = LocalDateTime.now().format(FORMATO);
        // Añadimos .pendiente al nombre para saber que la copia aún no ha terminado
        File carpeta = new File(padre, prefijo + fecha + ".pendiente");

        if (!carpeta.mkdir()) {
            throw new IOException("No se pudo crear la carpeta: " + carpeta);
        }
        return carpeta;
    }

    public static File terminarCopia(File carpeta) throws IOException {
        // Quitamos .pendiente solo cuando todos los archivos se han copiado
        String nombre = carpeta.getName().replace(".pendiente", "");
        File terminada = new File(carpeta.getParentFile(), nombre);

        Files.move(carpeta.toPath(), terminada.toPath());
        return terminada;
    }
}

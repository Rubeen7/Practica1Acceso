package org.example.services;

import java.io.File;
import java.io.IOException;

public class BackupIncremental {

    private final File origen;
    private final File backups;
    private File carpetaCreada;

    public BackupIncremental(File origen, File backups) {
        this.origen = origen;
        this.backups = backups;
    }

    public int crearIncremental() throws IOException {
        Comprobaciones.prepararCarpetas(origen, backups);

        // Buscamos la última copia completa para ver qué archivos han cambiado
        File grupo = Comprobaciones.buscarUltimoCompleto(backups);
        if (grupo == null) {
            throw new IOException("Primero debes crear un backup completo (opción 1).");
        }

        // Solo se compara con el completo, no con incrementales anteriores
        File completo = new File(grupo, "completo");
        File incremental = BackupCompleto.nuevaCarpeta(grupo, "incremental_");
        // Revisamos los archivos y las carpetas comparándolos con la copia completa.
        int cantidad = BackupCompleto.copiarCarpeta(origen, incremental, "", completo);
        carpetaCreada = BackupCompleto.terminarCopia(incremental);

        return cantidad;
    }

    public File getCarpetaCreada() {
        return carpetaCreada;
    }
}

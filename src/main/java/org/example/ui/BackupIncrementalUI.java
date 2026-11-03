package org.example.ui;

import org.example.services.BackupIncremental;

import java.io.File;
import java.io.IOException;

public class BackupIncrementalUI implements OpcionBackup {
    private final BackupIncremental backup;

    public BackupIncrementalUI(File origen, File backups) {
        this.backup = new BackupIncremental(origen, backups);
    }

    @Override
    public void ejecutar() throws IOException {
        // Pedimos que se copien los cambios y mostramos el resultado
        int cantidad = backup.crearIncremental();
        mostrarResultado(backup.getCarpetaCreada(), cantidad);
    }
}

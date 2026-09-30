package org.example.ui;

import org.example.services.BackupCompleto;

import java.io.File;
import java.io.IOException;

public class BackupCompletoUI implements OpcionBackup {
    private final BackupCompleto backup;

    public BackupCompletoUI(File origen, File backups) {
        this.backup = new BackupCompleto(origen, backups);
    }

    @Override
    public void ejecutar() throws IOException {
        int cantidad = backup.crearCompleto();
        mostrarResultado(backup.getCarpetaCreada(), cantidad);
    }
}

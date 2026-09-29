package org.example;

import java.io.File;

// Guarda los dos datos que Main necesita mostrar después de hacer una copia.
public class ResultadoBackup {
    private File carpeta;
    private int cantidad;

    public ResultadoBackup(File carpeta, int cantidad) {
        this.carpeta = carpeta;
        this.cantidad = cantidad;
    }

    public File getCarpeta() {
        return carpeta;
    }

    public int getCantidad() {
        return cantidad;
    }
}

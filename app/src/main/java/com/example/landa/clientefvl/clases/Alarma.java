package com.example.landa.clientefvl.clases;

public class Alarma {

    private String id;
    private String appMovil;
    private boolean solucionado=false;
    private String descripcion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppMovil() {
        return appMovil;
    }

    public void setAppMovil(String appMovil) {
        this.appMovil = appMovil;
    }

    public boolean isSolucionado() {
        return solucionado;
    }

    public void setSolucionado(boolean solucionado) {
        this.solucionado = solucionado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

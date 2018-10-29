package com.example.landa.clientefvl.clases;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Usuario {

    String Identificador;
    String nombre;
    String apellido;

    public String getIdentificador() {
        return Identificador;
    }

    public void setIdentificador(String identificador) {
        Identificador = identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }


    public static ArrayList<Usuario> getUsuarios(String jason) {
         Gson gson = new Gson();
         Type type= new TypeToken<ArrayList<Usuario>>(){}.getType();
         return gson.fromJson(jason,type);
    }

}

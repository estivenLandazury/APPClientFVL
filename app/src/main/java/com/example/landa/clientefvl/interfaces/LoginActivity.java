package com.example.landa.clientefvl.interfaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.landa.clientefvl.R;
import com.example.landa.clientefvl.clases.Constante;
import com.example.landa.clientefvl.clases.User;
import com.example.landa.clientefvl.clases.Usuario;
import com.example.landa.clientefvl.com.WEBUtilDomi;
import com.google.gson.Gson;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public final static  String CONEXION= Constante.CONEXION;
    EditText nombreUsuarioText, contraseñaText;
    Button ingresarBtn, registrarBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nombreUsuarioText= findViewById(R.id.nombreUsuario);
        contraseñaText= findViewById(R.id.contraseña);
        ingresarBtn= findViewById(R.id.ingresar);
        registrarBtn= findViewById(R.id.Registrar);

        ingresarBtn.setOnClickListener(this);
        registrarBtn.setOnClickListener(this);

    }




    @Override
    public void onClick(View v) {

        if(v.equals(ingresarBtn)){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        Gson gson = new Gson();
                        User usuario = new User();
                        usuario.setEmail("");
                        usuario.setPassword(contraseñaText.getText().toString());
                        usuario.setUsername(nombreUsuarioText.getText().toString());

                        String jsonNombre = gson.toJson(usuario);


                        final String respuesta =  WEBUtilDomi.JsonByPOSTrequest(CONEXION+"Autenticade", jsonNombre);
                        Log.e(">>>>>",""+"antes del rum"+ respuesta);
                        Intent Menu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(Menu);
//
                    } catch (IOException e) {
                        showToats("Error al loguearse, verifique su nombre de usuario");
                        Log.e(">>>>>",""+"catch del rum");

                        e.printStackTrace();
                    }
                }
            }).start();

        }

        if(v.equals(registrarBtn)){
            Intent RegistrarCuenta= new Intent(getApplicationContext(), RegistroCuentaActivity.class);
            startActivity(RegistrarCuenta);
        }

    }





    private void showToats(final String mensaje) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        });

    }
}

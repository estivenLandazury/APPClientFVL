package com.example.landa.clientefvl.interfaces;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.landa.clientefvl.R;
import com.example.landa.clientefvl.clases.Usuario;
import com.example.landa.clientefvl.com.WEBUtilDomi;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RegistroUsuarioActivity extends AppCompatActivity  implements View.OnClickListener {

    public String url = "https://reqres.in/api/users/2";
    OkHttpClient client = new OkHttpClient();

    EditText nombreText, apellidoText, IdentificadorText;
    Button btnAgregar;

    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        nombreText= findViewById(R.id.editText);
        apellidoText= findViewById(R.id.editText2);
        IdentificadorText= findViewById(R.id.editText3);

        btnAgregar=findViewById(R.id.button);

        btnAgregar.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if(v.equals(btnAgregar)){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Gson gson = new Gson();
                        Usuario usuario = new Usuario();
                        usuario.setIdentificador(IdentificadorText.getText().toString());
                        usuario.setNombre(nombreText.getText().toString());
                        usuario.setApellido(apellidoText.getText().toString());
                        String jsonNombre = gson.toJson(usuario);
                        final String respuesta = WEBUtilDomi.JsonByPOSTrequest("http://192.168.0.21:8080/usuario", jsonNombre);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegistroUsuarioActivity.this, "Se ha Registrado el usuario correctamente", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (IOException e) {
                        Toast.makeText(RegistroUsuarioActivity.this, "HUbo un problema al registrar el usuario", Toast.LENGTH_SHORT).show();

                        e.printStackTrace();
                    }
                }
            }).start();

        }

    }
}




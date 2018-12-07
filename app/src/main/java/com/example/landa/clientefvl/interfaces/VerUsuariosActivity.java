package com.example.landa.clientefvl.interfaces;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.landa.clientefvl.R;
import com.example.landa.clientefvl.clases.HttpRequest;
import com.example.landa.clientefvl.clases.Usuario;
import com.example.landa.clientefvl.clases.UsuarioAdapter;
import com.example.landa.clientefvl.com.WEBUtilDomi;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class VerUsuariosActivity extends AppCompatActivity {

    Spinner spinnerParametro;
    ListView listaUsuario;
    EditText dato;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuarios);




        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                try {
                    //Bluestacks
                    /*
                    final String json = WEBUtilDomi.GETrequest("http://172.30.173.136:8080/usuario");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(VerUsuariosActivity.this, ""+json, Toast.LENGTH_SHORT).show();
                        }
                    });

                */

                    /*  Post envió de datos*/

                    Gson gson = new Gson();
                    Usuario usuario = new Usuario();
                    String v= "02/08/2018";

                    usuario.setNombre("Andres");
                    usuario.setApellido("Navarro");
                    usuario.setNumeroDocumento("12346583");
                    Date m = new Date("2/03/2018");



                    String jsonNombre = gson.toJson(usuario);
                    final String respuesta = WEBUtilDomi.JsonByPOSTrequest("http://172.30.159.83:8080/usuarios", jsonNombre);

                   showToats("Se registro el usuario correctamente"+ "\n"+ respuesta);

                } catch (IOException e) {
                    e.printStackTrace();
                        Log.e(">>>>>",""+"error al conectarse");
                }
            }
        }).start();






    }


    public Date ParseFecha(String fecha)
    {


        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaDate = null;

        try {
            fechaDate = formato.parse(fecha);


            Log.e(">>>>>",""+"la fecha es "+ fechaDate);

        }
        catch (ParseException ex)
        {
            Log.e(">>>>>",""+"Error al parser la fecha");
        }
        return fechaDate;
    }


    private void showToats(final String mensaje) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VerUsuariosActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        });

    }

}

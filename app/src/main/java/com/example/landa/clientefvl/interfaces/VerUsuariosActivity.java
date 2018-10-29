package com.example.landa.clientefvl.interfaces;

import android.net.Uri;
import android.os.AsyncTask;
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
import java.util.ArrayList;

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
                    usuario.setIdentificador("12346583");
                    usuario.setNombre("Andres");
                    usuario.setApellido("Navarro");
                    String jsonNombre = gson.toJson(usuario);
                    final String respuesta = WEBUtilDomi.JsonByPOSTrequest("http://172.30.173.136:8080/usuario", jsonNombre);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(VerUsuariosActivity.this, ""+respuesta, Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

/*
    public void inicializar() throws IOException {
        this.spinnerParametro= (Spinner)findViewById(R.id.spinner);
        this.dato= (EditText)findViewById(R.id.nameUsu);
        this.listaUsuario= findViewById(R.id.listaUsuario);

        OkHttpClient client=new OkHttpClient();
        String url ="http://172.30.173.136:8080/usuario";
        Request request= new Request.Builder().url(url).get().addHeader("Content-Type", "application/json").build();
           Response response = client.newCall(request).execute();
        String myResponse =response.body().string();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
           e.printStackTrace();
           Log.d("CREATION","erro parcero");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {


                if(response.isSuccessful()){
                    final String myResponse =response.body().string();
                    VerUsuariosActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("CREATION","no hay conexión 2" +myResponse);

                            dato.setText(myResponse);
                        }
                    });
                }else{
                    Log.d("CREATION","no hay conexión mijito");

                }
            }
        });


    }

    public class  getUsuarios extends AsyncTask<String,Void,String>{


        public String doInBackground(String... params){
         try {
             return  HttpRequest.get(params[0]).accept("application/json").body();

         }catch (Exception e){
           return "";
         }

        }


        public void  onPostExecute(String result){

            if(result.isEmpty()){
                Toast.makeText(null,"no hay registro de usuarios", Toast.LENGTH_LONG).show();

            }else{
                ArrayList<Usuario> usuarios= Usuario.getUsuarios(result);
                ArrayList<Usuario> usuarios_aux= new ArrayList<>();

                if(spinnerParametro.getSelectedItem().toString().equals("ListarTodo")){
                    usuarios_aux= usuarios;
                }else{
                    for(int i=0; i< usuarios.size(); i++){
                        switch (spinnerParametro.getSelectedItem().toString()){
                            case "Identificador":
                                if(usuarios.get(i).getIdentificador().equals(dato.getText().toString().trim())){
                                   usuarios_aux.add(usuarios.get(i));
                                }

                                break;
                            case"Nombre":
                                if(usuarios.get(i).getNombre().equals(dato.getText().toString().trim())){
                                    usuarios_aux.add(usuarios.get(i));
                                }
                                break;
                            case"Apellido":
                                if(usuarios.get(i).getApellido().equals(dato.getText().toString().trim())){
                                    usuarios_aux.add(usuarios.get(i));
                                }
                                break;
                        }
                    }
                }


                if(usuarios_aux.size()!=0){
                       UsuarioAdapter adapter= new UsuarioAdapter(VerUsuariosActivity.this,usuarios_aux );
                       listaUsuario.setAdapter(adapter);
                }
            }
        }

    }

    */
}

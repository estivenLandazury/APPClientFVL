package com.example.landa.clientefvl.interfaces;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.landa.clientefvl.R;
import com.example.landa.clientefvl.clases.AppMovil;
import com.example.landa.clientefvl.clases.Constante;
import com.example.landa.clientefvl.clases.Manilla;
import com.example.landa.clientefvl.clases.RolUsuario;
import com.example.landa.clientefvl.clases.User;
import com.example.landa.clientefvl.clases.Usuario;
import com.example.landa.clientefvl.clases.UsuarioApp;
import com.example.landa.clientefvl.clases.UsuarioDocumento;
import com.example.landa.clientefvl.com.WEBUtilDomi;
import com.google.gson.Gson;

import java.io.IOException;

public class RegistroCuentaActivity extends AppCompatActivity  implements View.OnClickListener {

    public final static String CONEXION= Constante.CONEXION;
    private Spinner tipodocumentos;

    String documentoSelecionado;
    EditText textNombre, textApellido, textNumerodocumento, textContraseña, textRepetContraseña, textCorreo;
    Button btnRegistrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cuenta);

        textNombre=findViewById(R.id.nombreCuenta);
        textApellido= findViewById(R.id.apellidoCuenta);
        textNumerodocumento=findViewById(R.id.CuentanumeroDoc);
        textContraseña=findViewById(R.id.claveCuenta);
        textRepetContraseña=findViewById(R.id.claveCuentaDos);
        tipodocumentos= findViewById(R.id.cuentaTipoDoc);
        textCorreo=findViewById(R.id.correoCuenta);
        btnRegistrar=findViewById(R.id.btnRegistrarCuenta);

        final ArrayAdapter<String> myAdapter= new ArrayAdapter<String>(RegistroCuentaActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.TipoDocumento));
        myAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        tipodocumentos.setAdapter(myAdapter);

        tipodocumentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                documentoSelecionado= myAdapter.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnRegistrar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if(v.equals(btnRegistrar)){


            new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    try {

                        /*  Post envió de datos*/

                        /*Crea la cuenta del usuario, para poder loguearse posteriormente*/
                        crearCuentaUsuario(textNombre.getText().toString(),textCorreo.getText().toString(),textContraseña.getText().toString(),textRepetContraseña.getText().toString());
                       /** se obtiene la cuenta creada anteriormenet*/
                        User cuenta= obtenerCuenta(textNombre.getText().toString());


                        /*Se cea el Usuario para mandarlo por el JSON*/
                        Gson gson = new Gson();
                        Usuario usuario = new Usuario();
                        usuario.setNombre(textNombre.getText().toString());
                        usuario.setApellido(textApellido.getText().toString());
                        usuario.setNumeroDocumento(textNumerodocumento.getText().toString());
                        usuario.setUser(cuenta.getId());
                        String jsonNombre = gson.toJson(usuario);

                        /*se envía el usuario por el JSON*/
                        final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION+"usuarios", jsonNombre);


                        /*Se crea el registro de la app que será  asociada al usuario*/
                        crearAppMovil(textNombre.getText().toString());



                        /*Se obtiene el objeto app, a partir del nombre de la app, que es el mismo de la cuenta de usuario*/
                        AppMovil app= obtenerApp(textNombre.getText().toString());

                        /*Obtengo el JSON de  usuario a partir de su numero de documento*/
                        String Jsonusu = JsonUsuarioDocumento(usuario.getNumeroDocumento());

                        /*obtengo el usuario desde la base de datos, para obtener el id asignado*/
                        Usuario usu=  retornarUsuario(Jsonusu);

                        /* Aquí se añade el rol de usuario encargado por defualt creado*/
                        añadirRolUsuario(usu.getId(), Constante.ROL_ENCARGADO+"");

                        /* Aquí se añade el tipo documento que el usuario utiliza*/
                        int tipoDocumento= tipoDocumentoSeleccionado();
                        añadirUsuarioDocumento(usu.getId(),""+tipoDocumento );

                         /* se asocia el usuario registrado y la app que enviará el estado de alarma a la web*/
                        crearUsuarioApp(usu.getId(),app.getId());

                       Constante.setIdApp(app.getId());

                        showToats("Se registro el usuario correctamente");

                        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(login);


                    } catch (IOException e) {
                        e.printStackTrace();
                        showToats("Error al registrar el usuario"+ "\n");

                        Log.e(">>>>>",""+"error al conectarse");
                    }
                }
            }).start();


        }

    }



    private void showToats(final String mensaje) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegistroCuentaActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        });

    }


    public int tipoDocumentoSeleccionado(){
        int tipo=0;
        if(documentoSelecionado.equals("cedula")){
            tipo= Constante.TIPO_DOC_CEDULA;
        }else if(documentoSelecionado.equals("Tarjeta Identidad")){
            tipo= Constante.TIPO_DOC_IDENTIDAD;
        }

        return tipo;
    }


    public void crearAppMovil(String nombreApp){
        Gson json= new Gson();
        AppMovil app= new AppMovil();
        app.setNombre(nombreApp);
        String jsonNombre = json.toJson(app);
        try {
            final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION+"Apps", jsonNombre);

        } catch (IOException e) {
            e.printStackTrace();

            Log.e(">>>>>",""+"error al crear la AppMovil");


        }

    }


    public AppMovil obtenerApp(String nombreApp){
        Gson manilla = new Gson();
        AppMovil App=null;
        try {
            String  AppM = WEBUtilDomi.GETrequest(CONEXION+"AppName/"+nombreApp);
            App= manilla.fromJson(AppM, AppMovil.class);
        } catch (IOException e) {
            Log.e(">>>>>",""+"error al traer App");
            e.printStackTrace();
        }
        return App;
    }


    public User obtenerCuenta(String username){
        Gson gson = new Gson();
        User cuenta= null;
        try {
            String  AppM = WEBUtilDomi.GETrequest(CONEXION+"GetUser/"+username);
            cuenta= gson.fromJson(AppM, User.class);
        } catch (IOException e) {
            Log.e(">>>>>",""+"error al traer la cuenta con username: "+ username);
            e.printStackTrace();
        }

        return cuenta;
    }


    public void crearUsuarioApp(String idUsuario, String idApp){
        Gson json= new Gson();
        UsuarioApp usuApp= new UsuarioApp();
        usuApp.setUsuario(idUsuario);
        usuApp.setAppMovil(idApp);
        String jsonNombre = json.toJson(usuApp);
        try {
            final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION+"UsuarioApps", jsonNombre);

        } catch (IOException e) {
            e.printStackTrace();

            Log.e(">>>>>",""+"error al crear el usuarioApp");


        }



    }


    public void crearCuentaUsuario(String username, String email, String password, String repeatPassword){


       if(!password.equals(repeatPassword)){

           showToats("Las contraseñas no coinciden, verifique");
       }


        Gson json= new Gson();
        User cuenta=new User();
        cuenta.setUsername(username);
        cuenta.setPassword(password);
        cuenta.setEmail(email);
        String jsonNombre = json.toJson(cuenta);

        /*se envía el usuario por el JSON*/
        try {
            final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION+"AddUser", jsonNombre);
        } catch (IOException e) {
            e.printStackTrace();
            showToats("Error al crear la cuenta, verifique la conexión a internet");

            Log.e(">>>>>",""+"error al crear la cuenta");


        }


    }


    public String JsonUsuarioDocumento(String numeroDocumento){

        String Jsonusu = null;
        try {
            Jsonusu = WEBUtilDomi.GETrequest(CONEXION+"usuario/"+numeroDocumento);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(">>>>>",""+"error al tarer Json de usuario");

        }
        return Jsonusu;
    }

    public Usuario retornarUsuario(String json){
        Gson gson = new Gson();
        Usuario  usuario = gson.fromJson(json, Usuario.class);
        return usuario;
    }





    public void añadirUsuarioDocumento(String idUsuario, String tipoDocumento){
        Gson gson = new Gson();
        UsuarioDocumento usud= new UsuarioDocumento();
        usud.setTipoDocumento(tipoDocumento);
        usud.setUsuario(idUsuario);
        String jsonNombre = gson.toJson(usud);

        try{
            final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION+"UsuarioDocumentos", jsonNombre);

        }catch (IOException e){
            Log.e(">>>>>",""+"error al añadir UsuarioDocumento");

        }


    }
    public void añadirRolUsuario(String idUsuario, String RolUsuario){
        Gson gson = new Gson();
        com.example.landa.clientefvl.clases.RolUsuario rol= new RolUsuario();
        rol.setTipoUsuario(RolUsuario);
        rol.setUsuario(idUsuario);
        String jsonNombre = gson.toJson(rol);
        try {
            final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION+"rolUsuarios", jsonNombre);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(">>>>>",""+"error al añadir rolUsuario");

        }
    }



    private String replaceCadena(String macId){
        String cadena= macId.replace(":","-");
        return cadena;
    }


    public Manilla existeManilla(String macDispositivo){
        Gson manilla = new Gson();
        Manilla mll=null;
        String MacId=replaceCadena(macDispositivo);
        try {
            String  manill = WEBUtilDomi.GETrequest(CONEXION+"Manilla/"+MacId);
            mll= manilla.fromJson(manill, Manilla.class);
        } catch (IOException e) {
            Log.e(">>>>>",""+"error al traer manilla"+MacId);
            e.printStackTrace();
        }

        return mll;

    }

}

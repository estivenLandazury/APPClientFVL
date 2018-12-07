package com.example.landa.clientefvl.interfaces;

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
import com.example.landa.clientefvl.clases.Constante;
import com.example.landa.clientefvl.clases.Manilla;
import com.example.landa.clientefvl.clases.ManillaApp;
import com.example.landa.clientefvl.clases.ManillaUsuario;
import com.example.landa.clientefvl.clases.RolUsuario;
import com.example.landa.clientefvl.clases.Usuario;
import com.example.landa.clientefvl.clases.UsuarioDocumento;
import com.example.landa.clientefvl.com.WEBUtilDomi;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Date;


public class Registro extends AppCompatActivity  implements View.OnClickListener {

    private Spinner tipodocumentos,tipoUsuario;
    EditText textNombre, textApellido, textNumerodocumento, nombreDispositivo, macDispositivo;
    Button btnRegistrar;
    String documentoSelecionado;
    String TipoUsuarioSelecionado;


    public final static String CONEXION= Constante.CONEXION;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        textNombre= findViewById(R.id.nombreText);
        textApellido= findViewById(R.id.apellidoText);
        textNumerodocumento= findViewById(R.id.numeroDocText);
        nombreDispositivo= findViewById(R.id.nombreDispositivo);
        nombreDispositivo.setEnabled(false);
        macDispositivo= findViewById(R.id.macDispositivo);
        macDispositivo.setEnabled(false);
        btnRegistrar=findViewById(R.id.btnRegistrar);




        tipodocumentos= findViewById(R.id.tipoDoc);

        recibirInformacio();
        final ArrayAdapter<String> myAdapter= new ArrayAdapter<String>(Registro.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.TipoDocumento));
        myAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        tipodocumentos.setAdapter(myAdapter);



        tipoUsuario=findViewById(R.id.tipoUsu);
        final ArrayAdapter<String> myAdapter2= new ArrayAdapter<String>(Registro.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.TipoUsuario));
        myAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        tipoUsuario.setAdapter(myAdapter2);

        btnRegistrar.setOnClickListener(this);



        /** Tipo Usuario*/
        tipoUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoUsuarioSelecionado=myAdapter2.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       /** Tipo documentos select*/
        tipodocumentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                documentoSelecionado= myAdapter.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });










           }



    private void showToats(final String mensaje) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Registro.this, mensaje, Toast.LENGTH_LONG).show();
            }
        });

    }



    @Override
    public void onClick(View v) {


        if(v.equals(btnRegistrar)){

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

                        /*Se cea el Usuario para mandarlo por el JSON*/
                        Gson gson = new Gson();
                        Usuario usuario = new Usuario();
                        usuario.setNombre(textNombre.getText().toString());
                        usuario.setApellido(textApellido.getText().toString());
                        usuario.setNumeroDocumento(textNumerodocumento.getText().toString());
                        String jsonNombre = gson.toJson(usuario);

                        /*se envía el usuario por el JSON*/
                        final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION+"usuarios", jsonNombre);

                        /*Obtengo el JSON de  usuario a partir de su numero de documento*/
                        String Jsonusu = JsonUsuarioDocumento(usuario.getNumeroDocumento());

                        /*obtengo el usuario desde la base de datos, para obtener el id asignado*/
                        Usuario usu=  retornarUsuario(Jsonusu);

                        /* Aquí se añade el rol de usuario creado anteriormente*/
                         int tipoUsuario= tipoUsuarioSeleccionado();
                         añadirRolUsuario(usu.getId(), tipoUsuario+"");

                        /* Aquí se añade el tipo documento que el usuario utiliza*/
                         int tipoDocumento= tipoDocumentoSeleccionado();
                         añadirUsuarioDocumento(usu.getId(),""+tipoDocumento );

                         añadirmanilla(nombreDispositivo.getText().toString(),macDispositivo.getText().toString(),usu.getId());

                         /* Aqí obtengo la manilla creada, o la que ya existía*/
                         Manilla manilla= existeManilla(macDispositivo.getText().toString());

                         añadirManillaUsuario(manilla.getMacId(), usu.getId());
                         Constante.setIdDispositivo(manilla.getMacId());

                         añadirManillaApp(Constante.getIdApp(), Constante.getIdDispositivo());





                        showToats("Se registro el usuario correctamente");


                    } catch (IOException e) {
                        e.printStackTrace();
                        showToats("Error al registrar el usuario"+ "\n");

                        Log.e(">>>>>",""+"error al conectarse");
                    }
                }
            }).start();






        }

    }


    public int tipoUsuarioSeleccionado(){

        int tipo=0;

        if(TipoUsuarioSelecionado.equals("Ambulatorio")){
            tipo= Constante.ROL_AMBULATORIO;
        }else
        if(TipoUsuarioSelecionado.equals("Hospitalario")){
            tipo= Constante.ROL_HOSPITALARIO;
        }else if(TipoUsuarioSelecionado.equals("Acompañante")){
            tipo= Constante.ROL_ACOMPAÑANTE;
        }



        return tipo;
    }


 public void añadirManillaApp(String idApp, String idDispositivo){
        Gson json= new Gson();
     ManillaApp maniApp= new ManillaApp();
     maniApp.setAppMovil(idApp);
     maniApp.setManilla(idDispositivo);
     String jsonNombre = json.toJson(maniApp);

     try{
         final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION+"ManillaApps", jsonNombre);

     }catch (IOException e){
         Log.e(">>>>>",""+"error al añadir la manillaApp "+"idApp: "+ idApp +" idMovil: "+ idDispositivo);

     }

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
        RolUsuario rol= new RolUsuario();
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


    public void limpiarDatos(){
        textNombre.setText("");
        textApellido.setText("");
        textNumerodocumento.setText("");

    }



    private void recibirInformacio(){
        Bundle extras= getIntent().getExtras();
        String nombreDis= extras.getString("nombreDispositivo");
        String macDis= extras.getString("macDispositivo");
        nombreDispositivo.setText(nombreDis);
        macDispositivo.setText(macDis);
    }


    public void añadirmanilla(String nombreDispositivo, String macDispositivo, String idUsuario){

        /*Quita los :  del mac id y los remplaza por -*/
        String MCD= replaceCadena(macDispositivo);

        Manilla mani= existeManilla(MCD);

        if(mani==null){
            /*Se registra la manilla, ya que no existe en la base de datos*/
            Gson gmanil= new Gson();
            Manilla manilla= new Manilla();
            String MCDA= replaceCadena(macDispositivo);
            manilla.setMacId(MCDA);
            manilla.setNombre(nombreDispositivo);
            String manillaJSON=  gmanil.toJson(manilla);
            try {
                WEBUtilDomi.JsonByPOSTrequest(CONEXION+"Manillas", manillaJSON);
            } catch (IOException e) {
                Log.e(">>>>>",""+"error al agregar manilla solamente");

                e.printStackTrace();
            }

        }


    }


    public void añadirManillaUsuario(String macDispositivo, String idUsuario){


        Gson gson= new Gson();
        ManillaUsuario manillaUsuario= new ManillaUsuario();
        manillaUsuario.setUsuario(idUsuario);
        manillaUsuario.setManilla(macDispositivo);

        String convertirJson= gson.toJson(manillaUsuario);

        try {
            WEBUtilDomi.JsonByPOSTrequest(CONEXION+"ManillaUsuarios", convertirJson);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(">>>>>",""+"error al agregar ManillaUsuario: "+ "macId: "+manillaUsuario.getManilla()+ "  UsuarioId: "+ manillaUsuario.getUsuario());

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

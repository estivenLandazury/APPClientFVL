package com.example.landa.clientefvl.interfaces;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.landa.clientefvl.R;
import com.example.landa.clientefvl.clases.Alarma;
import com.example.landa.clientefvl.clases.Constante;
import com.example.landa.clientefvl.com.WEBUtilDomi;
import com.google.gson.Gson;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ListView lv1;
    BluetoothAdapter mBluetoAdapter;
    public String nombreDispositivo;
    public String macDispositivo;


    public final static String CONEXION= Constante.CONEXION;

    String bluetoothNombre;
    String bluetoohtMac;
    Vibrator vibrar;

    BluetoothDevice mBluetoDEvice;
    BroadcastReceiver mbroadcastReceiber;
    private String[] opciones = { "Registrar Usuario", "Ver Usuarios", "Administrar Bluetooh", "Registro"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibrar= (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        lv1= findViewById(R.id.menuList);
        ArrayAdapter adapter= new ArrayAdapter(this,android.R.layout.simple_list_item_1, opciones);
        lv1.setAdapter(adapter);

        IntentFilter filterBusqueda= new IntentFilter();
        filterBusqueda.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filterBusqueda.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filterBusqueda.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        registerReceiver(mReceiver,filterBusqueda);

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String opcion = lv1.getItemAtPosition(position).toString();
                if(opcion.equals("Registrar Usuario")){
                    Intent intent = new Intent(MainActivity.this,RegistroUsuarioActivity.class);
                    startActivity(intent);
                }

                if(opcion.equals("Login")){
                    Intent intent= new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);


                }



                if(opcion.equals("Registro")){
                    Intent intent= new Intent(MainActivity.this,Registro.class);
//                    intent.putExtra("nombreDispositivo", nombreDispositivo);
//                    intent.putExtra("macDispositivo", macDispositivo);
                    startActivity(intent);


                }

                if(opcion.equals("Ver Usuarios")){
                    Intent intent = new Intent(MainActivity.this,VerUsuariosActivity.class);
                    startActivity(intent);
                }
                if(opcion.equals("Administrar Bluetooh")){
                    Intent intent = new Intent(MainActivity.this,BluetoohActivity.class);
                    startActivity(intent);
                }
            }
        });




    }



    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            // Filtramos por la accion. que  Nos interesa detectar
            String action = intent.getAction();
            BluetoothDevice device= intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (action){
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    showToats("Está conectado al dispositvo "+ device.getName());



                    nombreDispositivo=device.getName();
                    macDispositivo= device.getAddress();

                    Constante.setNomreBluetooth(device.getName());
                    Constante.setMacBluetooth(device.getAddress());





                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
                    showToats("Se desconecto del dispositivo--"+ device.getName());

                   /* ArrayAdapter arrayAdapter = new ArrayAdapter<BluetoothDevice>(context,android.R.layout.simple_list_item_1, devicesLista);
                    viewsDevices.setAdapter(arrayAdapter);*/
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    showToats("Se ha desconectado del dispositivo"+ device.getName());
                    if(Constante.isAlarmaActivada()==true){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                añadirAlarma();
                                vibrar.vibrate(15000);
                                showToats("Se perdió la conexión con la manilla");


                            }
                        }).start();
                    }

                    Log.d("ACTION", "Dispositivo encontrado: " + device.getName() + "; MAC " + device.getAddress());



                    break;

                case BluetoothAdapter.ACTION_STATE_CHANGED:

                    if(mBluetoAdapter.isEnabled()) {
                        showToats("El bluetooh se ha activado ");
                    }

                    if(!mBluetoAdapter.isEnabled()){
                        showToats("El bluetooh se ha desactivado");

                    }
                    break;




            }
        }
    };


    public void  añadirAlarma(){
        Gson gson= new Gson();
        Alarma alarma= new Alarma();
        alarma.setAppMovil(Constante.getIdApp());
        alarma.setSolucionado(false);
        alarma.setDescripcion("Posible menor de edad en peligro");
        String AlarmaJson=  gson.toJson(alarma);

        try {
            WEBUtilDomi.JsonByPOSTrequest(CONEXION+"Alarmas", AlarmaJson);
        } catch (IOException e) {
            Log.e(">>>>>",""+"error al enviar alarma");

            e.printStackTrace();
        }



    }

    public String getNombreDispositivo() {
        return nombreDispositivo;
    }

    public void setNombreDispositivo(String nombreDispositivo) {
        this.nombreDispositivo = nombreDispositivo;
    }

    public String getMacDispositivo() {
        return macDispositivo;
    }

    public void setMacDispositivo(String macDispositivo) {
        this.macDispositivo = macDispositivo;
    }

    private void showToats(final String mensaje) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        });

    }




}

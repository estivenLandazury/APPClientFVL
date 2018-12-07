package com.example.landa.clientefvl.interfaces;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import com.example.landa.clientefvl.R;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class BluetoohActivity extends AppCompatActivity  {

    private final static int REQUEST_ENABLE = 0;
    private final static int REQUEST_DISCOVER_BT = 1;
    //Identificador del servicio////
    private static final UUID BTMODULEUUID = null;
    private BluetoothSocket socket;
    private String nombreDispositivo;
    private String MacDispositivo;




    //Esto es simplemente un String normal a diferencia que al agregar una sentancia en un bucle se agrega los espacios automaticamente
//for(hasta 20 veces)
//String cadena += " " + "Dato" ---> En un string normal se debe crear el espacio y luego agregar el dato
//Con esto se traduce a = DataStringIN.append(dato);
    private StringBuilder DataStringIN = new StringBuilder();

    //Llama a la sub- clase y llamara los metodos que se encuentran dentro de esta clase

    TextView mpairedTv;
    ImageView mBlueIv;
    Button btnOn, btnOff, btnDiscoverable, btndevices, btnLinked;
    BluetoothAdapter mBluetoAdapter;
    BluetoothDevice mBluetoDEvice;
    BroadcastReceiver mbroadcastReceiber;
    ListView viewsDevices;
    ArrayList<BluetoothDevice> devicesLista;
    BluetoothDevice[] btArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooh);

        mpairedTv = findViewById(R.id.Lista);
        mBlueIv = findViewById(R.id.imagenBluetooh);
        btnOff = findViewById(R.id.ApagarBluetooh);
        btnOn = findViewById(R.id.EncenderBluetooh);
        btnDiscoverable = findViewById(R.id.VisibilidadBluetooh);
        btnLinked = findViewById(R.id.VinculadosBluetooh);
        btndevices = findViewById(R.id.DisponiblesBluetooh);
        viewsDevices = findViewById(R.id.listaDispositivos);


        devicesLista= new ArrayList<>();
        viewsDevices.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mBluetoAdapter = BluetoothAdapter.getDefaultAdapter();



        //Dispositivos vinculados------
        btnLinked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mBluetoAdapter.isEnabled()) {
                    mpairedTv.setText("Dispositivos Vinculados");
                    Set<BluetoothDevice> devices = mBluetoAdapter.getBondedDevices();
                    for (BluetoothDevice devic : devices) {
                        mpairedTv.append("\nDevice" + devic.getName() + ", " + devic.getAddress() + ", " + devic);


                        /*if(devic.getName().contains("BBH")){
                            ClienteClass clienteClass= new ClienteClass(devic);
                            clienteClass.start();
                        }*/

                    }

                } else {
                    showToats("Encienda el bluetooh para obtener los dispositivos");


                }

            }
        });


        //Dispositivos disponibles
        btndevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBluetoAdapter.isEnabled()){


                    if(mBluetoAdapter.isDiscovering()){
                        showToats("Ya se inició la busqueda de dispositivos");
                    }else if(mBluetoAdapter.startDiscovery()){

                        showToats("Buscando dispositivos.....");


                    }else{
                        showToats("No fué posibe realizar la busqueda de dispositivos");

                    }

            }
        }});


        //Visibilidad de dispositivos ---------------

        btnDiscoverable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mBluetoAdapter.isDiscovering()){
                    showToats("Visible para otros dispositivos");
                    Intent intent= new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

                    // REQUEST_ENABLE_BT es un valor entero que vale 1
                    startActivityForResult(intent, REQUEST_DISCOVER_BT);
                }

            }
        });


        //Apagando Dispositivo
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoAdapter.isEnabled()) {
                    mBluetoAdapter.disable();
                    showToats("Desabilitando bluetooth");
                    mBlueIv.setImageResource(R.drawable.ic_action_off);
                    mpairedTv.setText(" ");

                } else {
                    showToats("El bluetooh está desactivado");


                }
            }
        });



        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBluetoAdapter.isEnabled()) {
                    showToats("Encendiendo Bluetooth");
                    // El Bluetooth está apagado, solicitamos permiso al usuario para iniciar
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE);
                } else {
                    showToats("El bluetooth se encuentra encendido");
                }
            }
        });




// Obtenemos el adaptador Bluetooth. Si es NULL, significara que el
// dispositivo no posee Bluetooth, por lo que deshabilitamos el boton
// encargado de activar/desactivar esta caracteristica.
        if (mBluetoAdapter == null) {

            showToats("Su dispositivo no tiene acceso al uso de bluetooh");

            btnOff.setEnabled(false);
            btnOn.setEnabled(false);
            btnDiscoverable.setEnabled(false);
            btnLinked.setEnabled(false);
            btndevices.setEnabled(false);
            return;
        }


        IntentFilter filterBusqueda= new IntentFilter();
        filterBusqueda.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filterBusqueda.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filterBusqueda.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        registerReceiver(mReceiver,filterBusqueda);

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            // Filtramos por la accion. que  Nos interesa detectar
            String action = intent.getAction();
            BluetoothDevice device= intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            switch (action){
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    showToats("Está conectado al dispositvo "+ device.getName());
                    Log.d("DISCOVERY", "Busqueda de dispositvos iniciada");
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
                    showToats("Se desconecto del dispositivo--"+ device.getName());
                   /* ArrayAdapter arrayAdapter = new ArrayAdapter<BluetoothDevice>(context,android.R.layout.simple_list_item_1, devicesLista);
                    viewsDevices.setAdapter(arrayAdapter);*/
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    showToats("Se ha desconectado del dispositivo"+ device.getName());
                    // Extraemos el dispositivo del intent mediante la clave BluetoothDevice.EXTRA_DEVICE
                  /*  BluetoothDevice device= intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);*/
                    /*devicesLista.add(device);*/


                    showToats("Dispositivo encontrado: " + device.getName() + "; MAC " + device.getAddress());
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



    private class ClienteClass extends Thread
    {
       private BluetoothDevice device;

       public ClienteClass (BluetoothDevice device1){
           device=device1;

           Log.e(">>>>>",""+device.getType());
           Log.e(">>>>>",""+BluetoothDevice.DEVICE_TYPE_DUAL);


           try {

               ParcelUuid[] v= device.getUuids();

               /*socket = device.createRfcommSocketToServiceRecord(obtenerUUID()[0].getUuid());*/
              socket = device.createInsecureRfcommSocketToServiceRecord(obtenerUUID()[2].getUuid());


           } catch (IOException e) {
               e.printStackTrace();
           }


       }


       public void run(){
           try {
               socket.connect();
               showToats("se logró la conexión exitosamente");
           } catch (IOException e) {

               Log.e(">>>>>>",""+e.getLocalizedMessage());

               try{
                   socket.close();

               }catch (Exception i){
                   showToats("no se logró cerra la conexión");

               }

               e.printStackTrace();
               showToats("no se pudo establecer la conexión");

           }
       }


    }



    public  ParcelUuid[] obtenerUUID(){
        ParcelUuid[] uuids=null;
        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            Method getUuidsMethod = BluetoothAdapter.class.getDeclaredMethod("getUuids", null);
           uuids = (ParcelUuid[]) getUuidsMethod.invoke(adapter, null);

            if(uuids != null) {
                for (ParcelUuid uuid : uuids) {
                    Log.d("UUID", "UUID: " + uuid.getUuid().toString());
                }
            }else{
                Log.d("UUID", "Uuids no encontrados, asegura habilitar Bluetooth!");
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return uuids;
    }

    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) { }
    }


    private void showToats(final String mensaje) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BluetoohActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode){
            case REQUEST_ENABLE:
                if(resultCode== RESULT_OK){
                    //Bluetooh is on
                    mBlueIv.setImageResource(R.drawable.ic_action_on);
                    showToats("conexión a bluetooth exitosa");
                }else{
                    //user denied to turn bluetooh on

                    showToats("no puedes activar el bluetooh");
                }

                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

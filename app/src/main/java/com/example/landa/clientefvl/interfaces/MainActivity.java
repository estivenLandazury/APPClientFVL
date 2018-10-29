package com.example.landa.clientefvl.interfaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.landa.clientefvl.R;

public class MainActivity extends AppCompatActivity {
    private ListView lv1;
    private String[] opciones = { "Registrar Usuario", "Ver Usuarios", "Administrar Bluetooh"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lv1= findViewById(R.id.menuList);
        ArrayAdapter adapter= new ArrayAdapter(this,android.R.layout.simple_list_item_1, opciones);
        lv1.setAdapter(adapter);


        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String opcion = lv1.getItemAtPosition(position).toString();
                if(opcion.equals("Registrar Usuario")){
                    Intent intent = new Intent(MainActivity.this,RegistroUsuarioActivity.class);
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
}

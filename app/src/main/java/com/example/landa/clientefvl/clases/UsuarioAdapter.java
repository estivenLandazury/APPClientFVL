package com.example.landa.clientefvl.clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import com.example.landa.clientefvl.R;

public class UsuarioAdapter extends BaseAdapter {
    Context context;
    ArrayList<Usuario> usuariosArray;

    public UsuarioAdapter(Context context, ArrayList<Usuario> usuariosArray) {
        this.context = context;
        this.usuariosArray = usuariosArray;
    }

    @Override
    public int getCount() {
        return usuariosArray.size();
    }

    @Override
    public Object getItem(int position) {
        return usuariosArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view= layoutInflater.inflate(R.layout.layout, parent, false);


        TextView nombre= (TextView)view.findViewById(R.id.layoutNombre);
        TextView apellido= (TextView)view.findViewById(R.id.layoutApellido);
        TextView Identificador= (TextView)view.findViewById(R.id.layoutIdentificador);

        Usuario usuario= this.usuariosArray.get(position);

        if(usuario!=null){

            nombre.setText("Nombre: "+ usuario.getNombre());
            apellido.setText("Apellido: "+ usuario.getApellido());


        }




        return view;
    }
}

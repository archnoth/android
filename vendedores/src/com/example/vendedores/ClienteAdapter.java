package com.example.vendedores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dominio.Cliente;
import com.google.android.gms.drive.internal.r;

public class ClienteAdapter extends ArrayAdapter<Cliente> {
	    private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	    
	    public ClienteAdapter(Context context, int resource, ArrayList<Cliente> data) {
	        super(context, resource, data);
	        clientes = data;
	    }
	    	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	if (convertView == null) {
		        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        convertView = inflater.inflate(R.layout.cliente_adapter, null);
	        }
	        
	        ((TextView)convertView.findViewById(R.id.mensaje_text)).setText(clientes.get(position).getNombre());
	        
	        /*
	        if (clientes.get(position).getTiene_mensajes()) {
	        	convertView.findViewById(R.id.mensaje_icon).setVisibility(View.VISIBLE);
	        } else {
	        	convertView.findViewById(R.id.mensaje_icon).setVisibility(View.INVISIBLE);
	        }
	         */
	        return convertView;
	    }
	}

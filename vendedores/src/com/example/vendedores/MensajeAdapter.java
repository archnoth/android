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

import com.example.dominio.Mensaje;
import com.google.android.gms.drive.internal.r;

public class MensajeAdapter extends ArrayAdapter<Mensaje> {
	    private ArrayList<Mensaje> Mensajes = new ArrayList<Mensaje>();
	    
	    public MensajeAdapter(Context context, int resource, ArrayList<Mensaje> data) {
	        super(context, resource, data);
	        Mensajes = data;
	    }
	    	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	if (convertView == null) {
		        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        convertView = inflater.inflate(R.layout.mensajes_adapter, null);
	        }
	        
	        ((TextView)convertView.findViewById(R.id.label)).setText(Mensajes.get(position).getMensaje());
	        
	        /*
	        if (Mensajes.get(position).getTiene_mensajes()) {
	        	convertView.findViewById(R.id.mensaje_icon).setVisibility(View.VISIBLE);
	        } else {
	        	convertView.findViewById(R.id.mensaje_icon).setVisibility(View.INVISIBLE);
	        }
	         */
	        return convertView;
	    }
	}

package com.example.vendedores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.dominio.Cliente;

public class ClienteAdapter extends ArrayAdapter<Cliente> {
	    private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	    
	    public ClienteAdapter(Context context, int resource, ArrayList<Cliente> data) {
	        super(context, resource, data);
	        clientes = data;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View v = super.getView(position, convertView, parent);

	        if (clientes.get(position).getTiene_mensajes()) {
	            v.setBackgroundColor(Color.rgb(0, 179, 30));
	        } else {
	            v.setBackgroundColor(Color.WHITE); //or whatever was original
	        }

	        return v;
	    }
	}

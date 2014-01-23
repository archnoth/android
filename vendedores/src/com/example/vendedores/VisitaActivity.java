package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.example.dominio.Cliente;
import com.example.dominio.Usuario;
import com.example.dominio.Visita;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class VisitaActivity extends Activity  {
	
	private String motivo;
	private String descripcion;
	private Visita nueva_visita;
	private Cliente c;
	private Usuario u;
	ArrayList<String> lista = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visita);
		
		//AutoCompleteTextView auto=(AutoCompleteTextView)findViewById(R.id.autoCompleteMotivoNota);
		c=(Cliente)getIntent().getExtras().getParcelable("cliente");
		u =(Usuario)getIntent().getExtras().getParcelable("usuario");
		
		LongRunningGetIO th= new LongRunningGetIO();
		AsyncTask<Void, Void, List<String>> async=th.execute();
		try {
			 lista = (ArrayList<String>)async.get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		final Spinner auto=(Spinner)findViewById(R.id.autoCompleteMotivoNota);
		final EditText descripcion=(EditText)findViewById(R.id.editTextVisitaDEscripcion);
		ArrayAdapter<String> adapter=  new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, lista) ;
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		auto.setAdapter(adapter);
		
		Button b=(Button)findViewById(R.id.btn_RegistrarVisita);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				nueva_visita= new Visita(c,u,auto.getSelectedItem().toString(),descripcion.getText().toString()); 
			
				Gson gson = new Gson();
				String dataString = gson.toJson(nueva_visita, nueva_visita.getClass()).toString();
				PostNuevaVisita thred = new PostNuevaVisita();// llamo un proceso en
				// backgroud para realizar
				// la venta

				// inicia el proceso de vender
				
				
				AsyncTask<String, Void, String> async = thred.execute(dataString);
				try {
						// obtengo la respuesta asincrona
						String respuesta = (String) async.get();
						JSONObject json = new JSONObject(respuesta);
						Toast.makeText(VisitaActivity.this,respuesta,10).show();
					}
				  catch(Exception e)
				  {
					  e.printStackTrace();
				  }
			}
			
					
			});
		
		
	}

//carga los tipo de motivos de una visita.
private class LongRunningGetIO extends AsyncTask <Void, Void, List<String> > {
	
	 
	protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
       InputStream in = entity.getContent();
         StringBuffer out = new StringBuffer();
         int n = 1;
         while (n>0) {
             byte[] b = new byte[4096];
             n =  in.read(b);
             if (n>0) out.append(new String(b, 0, n));
         }
         return out.toString();
    }
	
	@Override
	protected  List<String> doInBackground(Void... params) {
		
		HttpClient httpClient = new DefaultHttpClient();
		 HttpContext localContext = new BasicHttpContext();
         HttpGet httpGet = new HttpGet("http://ventas.jm-ga.com/api/motivos/?key="+u.getKey());
           
  
             // Execute HTTP Post Request
         String text = null;
         try {
        	 HttpResponse response = httpClient.execute(httpGet, localContext);
        	 HttpEntity entity = response.getEntity();
               
             text = getASCIIContentFromEntity(entity);
               
         } catch (Exception e) {
        	 e.printStackTrace();
         }
			// return text;
			if (text != null) {

				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(text);

					JSONArray jarray = (JSONArray) jsonObject.get("motivos");
					for (int i = 0; i < jarray.length(); i++) {
						JSONObject motivo = jarray.getJSONObject(i);
						lista.add(motivo.getString("descripcion"));
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			return lista;
		}

	}



private class PostNuevaVisita extends AsyncTask <String, Void, String > {
	
	 
	protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
       InputStream in = entity.getContent();
         StringBuffer out = new StringBuffer();
         int n = 1;
         while (n>0) {
             byte[] b = new byte[4096];
             n =  in.read(b);
             if (n>0) out.append(new String(b, 0, n));
         }
         return out.toString();
    }
	
	@Override
	protected  String doInBackground(String... params) {
		 
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost("http://ventas.jm-ga.com/api/visita/");
         // Execute HTTP Post Request
         String text = null;
         try {
        	 
        	 StringEntity se = new StringEntity(params[0].toString(),"UTF8");
        	 //se.setContentEncoding("utf-8");
        	 //se.setChunked(false);
        	 se.setContentType("application/json");
        	 httpPost.setEntity(se);
        	 HttpResponse response = httpClient.execute(httpPost, localContext);
        	 HttpEntity entity = response.getEntity();
               
             text = getASCIIContentFromEntity(entity);
             return text;
               
         } catch (Exception e) {}
         //return text; 
         
			
		return null;
}
	@Override
	protected void onPostExecute(String results) {
		//((ProgressBar)findViewById(R.id.progressBarFactura)).setVisibility(View.INVISIBLE);
	}

}






}

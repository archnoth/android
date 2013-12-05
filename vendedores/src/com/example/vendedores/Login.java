package com.example.vendedores;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	public Usuario usuario=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void post(View view) {
	       
    	Button b = (Button)findViewById(R.id.btn_ingresar);
		b.setClickable(false);
		new LongRunningGetIO().execute();
		
    	
    }
	
	
private class LongRunningGetIO extends AsyncTask <Void, Void, String> {
		
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
		protected String doInBackground(Void... params) {
			 HttpClient httpClient = new DefaultHttpClient();
			 HttpContext localContext = new BasicHttpContext();
             HttpPost httpPost = new HttpPost("http://ventas.jm-ga.com/api/login/");
           
             // Add your data
        	 EditText username = (EditText)findViewById(R.id.editText1);
        	 EditText password = (EditText)findViewById(R.id.editTextPassword);
             String dataString = "{\"username\":\"" + username.getText().toString() +"\", \"password\":\""+ password.getText().toString() +"\"}";
             
             // Execute HTTP Post Request
             String text = null;
             try {
            	 StringEntity se = new StringEntity(dataString);
            	 se.setContentEncoding("UTF-8");
            	 se.setContentType("application/json");
            	 httpPost.setEntity(se);
            	 HttpResponse response = httpClient.execute(httpPost, localContext);
            	 HttpEntity entity = response.getEntity();
                   
                   text = getASCIIContentFromEntity(entity);
                   
             } catch (Exception e) {
            	 return e.getLocalizedMessage();
             }
             return text; 
        
	}	
		
		protected void onPostExecute(String results) {
			
			if (results!=null) {
				
				
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(results);
					
					Context context = getApplicationContext();
					int duration = Toast.LENGTH_SHORT;
					String api_key = jsonObject.getString("api_key");
					//String user_id=jsonObject.getString("user_id");
					String username=jsonObject.getString("username");
					String jsonClientes=jsonObject.getString("user_clients");
					JSONArray jarray=new JSONArray(jsonClientes);

					usuario = new Usuario("", "", username, "", "", api_key);
					
					for(int i=0;i<jarray.length();i++)
					{
						JSONObject dic_cliente= jarray.getJSONObject(i);
						Cliente cli= new Cliente(dic_cliente.getString("nombre"),dic_cliente.getString("direccion"),dic_cliente.getString("rut"));
						usuario.getListaClientes().add(cli);
					}
					
					Toast toast = Toast.makeText(context, usuario.toString(), duration);
					toast.show();
					
					if(usuario!=null)
					{
						Intent intent = new Intent();
						/*Bundle band = new Bundle();
						band.putParcelable("usuario",usuario);
						intent.putExtras(band);*/
						//intent.setClass(this, ListadoClientes.class);
						startActivity(intent);
						
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.getCause());
				}
					
					
			}
			Button b = (Button)findViewById(R.id.btn_ingresar);
			b.setClickable(true);
		}
    }
    
	
			

	
}
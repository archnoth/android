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
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dominio.Cliente;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class ServicioSaldoCliente  extends IntentService  {

	
	public ServicioSaldoCliente() {
		super("");
		// TODO Auto-generated constructor stub
	}

	protected String getASCIIContentFromEntity(HttpEntity entity)
			throws IllegalStateException, IOException {
		InputStream in = entity.getContent();
		StringBuffer out = new StringBuffer();
		int n = 1;
		while (n > 0) {
			byte[] b = new byte[4096];
			n = in.read(b);
			if (n > 0)
				out.append(new String(b, 0, n));
		}
		return out.toString();
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		
		Cliente cli=(Cliente)intent.getParcelableExtra("cliente");
		cargarSaldo(cli.getRut());
	}

	private void cargarSaldo(String rut) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost("http://ventas.jm-ga.com/api/clientes/saldo/?key="+((Sistema)getApplicationContext()).getUsu().getKey());
		// Execute HTTP Post Request
		String text = null;
		try {
			StringEntity se = new StringEntity(rut, "UTF8");
			se.setContentType("application/json");
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost,localContext);
			HttpEntity entity = response.getEntity();
			text = getASCIIContentFromEntity(entity);

		} catch (Exception e) {
		}
		if (text != null) {

			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(text);
				text=jsonObject.getString("saldo");	
				((Sistema)getApplicationContext()).setSaldo_cliente(Double.parseDouble(text));
				Intent saldo_cliente=new Intent("saldo_cliente");
				saldo_cliente.putExtra("saldo_cliente", text);
				LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(saldo_cliente);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

}

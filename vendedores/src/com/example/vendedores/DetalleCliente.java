package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import com.example.dominio.Cliente;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DetalleCliente extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_cliente);
		Cliente cliente=getIntent().getExtras().getParcelable("cliente");
		TextView nombre=(TextView)findViewById(R.id.editTextNombreCliente);
		TextView rut=(TextView)findViewById(R.id.editTextRutCliente);
		TextView direccion=(TextView)findViewById(R.id.editDireccionCliente);
		TextView tel=(TextView)findViewById(R.id.editTelCliente);
		TextView tel2=(TextView)findViewById(R.id.editTel2Cliente);
		TextView celular=(TextView)findViewById(R.id.editCelularCliente);
		TextView email=(TextView)findViewById(R.id.editEmailCliente);
		TextView web=(TextView)findViewById(R.id.editWebCliente);
		TextView lugar_entrega=(TextView)findViewById(R.id.editLugarEntrega);
		TextView dia_entrega=(TextView)findViewById(R.id.editDia_de_entregaCliente);
		TextView hora_entrega_desde=(TextView)findViewById(R.id.editHora_de_entrega_desde);
		TextView hora_entrega_hasta=(TextView)findViewById(R.id.editHora_de_entrega_hasta);
		nombre.setText("Nombre: "+cliente.getNombre());
		rut.setText("Rut: "+cliente.getRut());
		direccion.setText("Dir: "+cliente.getDireccion());
		tel.setText("Tel: "+cliente.getTel());
		tel2.setText("Tel2: "+cliente.getTel2());
		celular.setText("Celular: "+cliente.getCelular());
		email.setText("Email : "+cliente.getEmail());
		web.setText("Web: "+cliente.getWeb());
		lugar_entrega.setText("Dir-entrega: "+cliente.getLugar_entrega());
		Calendar dia_entega=Calendar.getInstance();
		dia_entega.set(Calendar.DAY_OF_WEEK, cliente.getDia_de_entrega());
		dia_entrega.setText("Dia de entrega: "+dia_entega.get(Calendar.DAY_OF_WEEK));
		
		hora_entrega_desde.setText("Hora de entrega desde: "+cliente.getHora_de_entrega_desde().get(Calendar.HOUR_OF_DAY));
		hora_entrega_hasta.setText("hasta : "+cliente.getHora_de_entrega_hasta().get(Calendar.HOUR_OF_DAY));
		
		
		ImageView mainImageView = (ImageView) findViewById(R.id.imageView);
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		String imageurl = "http://ventas.jm-ga.com/source/media/"+cliente.getUrl_imagen();
		  
		ImageDownloadMessageHandler imageDownloadMessageHandler1= new ImageDownloadMessageHandler(progressBar, mainImageView);
		ImageDownlaodThread imageDownlaodThread = new ImageDownlaodThread(imageDownloadMessageHandler1,imageurl);
		imageDownlaodThread.start();
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalle_cliente, menu);
		return true;
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		((ProgressBar)findViewById(R.id.progressBarDetalleAFactura)).setVisibility(View.INVISIBLE);
	}
	
	public void to_factura_activity(View view){
		((ProgressBar)findViewById(R.id.progressBarDetalleAFactura)).setVisibility(View.VISIBLE);
		Intent fac_intent = new Intent(getApplicationContext(),Factura.class); 
		fac_intent.putExtra("usuario",getIntent().getExtras().getParcelable("usuario")); 
		fac_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));  
	    startActivity(fac_intent);
	}
	
public void to_historico_activity(View view){
		
		Intent hist_intent = new Intent(getApplicationContext(),Historico.class); 
		hist_intent.putExtra("usuario",getIntent().getExtras().getParcelable("usuario"));  
	    startActivity(hist_intent);
	}


class ImageDownlaodThread extends Thread {
	  ImageDownloadMessageHandler imageDownloadMessageHandler;
	  String imageUrl;

	  public ImageDownlaodThread(ImageDownloadMessageHandler imageDownloadMessageHandler, String imageUrl) {
	   this.imageDownloadMessageHandler = imageDownloadMessageHandler;
	   this.imageUrl = imageUrl;
	  }

	  @Override
	  public void run() {
	   Drawable drawable = LoadImageFromWebOperations(imageUrl);
	   Message message = imageDownloadMessageHandler.obtainMessage(1, drawable);
	   imageDownloadMessageHandler.sendMessage(message);
	   System.out.println("Message sent");
	  }

	 }

	 class ImageDownloadMessageHandler extends Handler {
	  ProgressBar progressBar;
	  View imageTextView;

	  public  ImageDownloadMessageHandler(ProgressBar progressBar, View imageTextView) {
	   this.progressBar = progressBar;
	   this.imageTextView = imageTextView;
	  }

	  @Override
	  public void handleMessage(Message message) {
	   progressBar.setVisibility(View.GONE);
	   imageTextView.setBackgroundDrawable(((Drawable) message.obj));
	   imageTextView.setVisibility(View.VISIBLE);
	  }

	 }

	 Drawable LoadImageFromWebOperations(String url) {
	  Drawable d = null;
	  InputStream is = null;
	  try {
	   is = (InputStream) new URL(url).getContent();
	   d = Drawable.createFromStream(is, "src name");
	  } catch (MalformedURLException e) {
	   e.printStackTrace();
	  } catch (IOException e) {
	   e.printStackTrace();
	  }
	  return d;
	 }
}

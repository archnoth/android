package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

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
		TextView nombre=(TextView)findViewById(R.id.editText1);
		TextView rut=(TextView)findViewById(R.id.editText2);
		TextView direccion=(TextView)findViewById(R.id.editText3);
		nombre.setText(cliente.getNombre());
		rut.setText(cliente.getRut());
		direccion.setText(cliente.getDireccion());
		
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

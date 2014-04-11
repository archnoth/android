package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;


import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
import com.example.dominio.Producto;
import com.example.dominio.ProductoVenta;
import com.example.dominio.Usuario;
import com.example.dominio.Venta;
import com.google.gson.Gson;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.InputFilter;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Factura extends Activity {

	private Usuario usuario;
	private Cliente cliente;
	private static List<Producto> lista_productos;
	private static HashMap<String, Producto> diccionarioProductos;
	private BigDecimal monto_factura = new BigDecimal("0.0");
	private Venta nueva_venta;
	private Venta venta_original;
	private EditText last_text_cantidad;
	private JSONObject json;
	private Gson gson;
	private int descuento_contado_porcentaje=0;
	private int tipo;
	private int descuento_producto = 0;
	private Menu menu;
	private View context_view;
	private BigDecimal monto_factura_sin_descuento=BigDecimal.ZERO;
	private BigDecimal saldo_cliente=new BigDecimal("0.00");
	private Venta  ultima_venta;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_factura);
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
	    usuario = ((Sistema)getApplicationContext()).getUsu();
	    cliente = ((Cliente)getIntent().getExtras().getParcelable("cliente"));
		descuento_contado_porcentaje = ((Sistema)getApplicationContext()).getDescuento_contado();
		tipo=getIntent().getExtras().getInt("tipo");
		lista_productos = ((Sistema)getApplicationContext()).getLista_productos();
		if (lista_productos.size()>0){
			diccionarioProductos = new HashMap<String, Producto>();
			for (int i = 0; i < lista_productos.size(); i++) {
			
				diccionarioProductos.put(lista_productos.get(i).getCodigo(),lista_productos.get(i));
			}
		}
		ultima_venta = ((Sistema)getApplicationContext()).getUltima_venta();
		if (getIntent().getBooleanExtra("ultima_venta", false)) {
			cargar_ultima_venta(ultima_venta);
		} else {
			addRowToTableProductos(null);
		}
		if(tipo!=0 && tipo!=2)
		{
			JSONObject rut_cliente = null;
			try {
				rut_cliente = new JSONObject("{rut:+"+cliente.getRut()+"}");
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			SaldoCliente thread_saldo=new SaldoCliente();//llamo al consultar saldo;
			AsyncTask<JSONObject, Void, String> async_method = thread_saldo.execute(rut_cliente);
			try {
				saldo_cliente=new BigDecimal(async_method.get());
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			EditText saldo_value = (EditText) (findViewById(R.id.SaldoValue));
			switch(tipo){
				case 1:
					saldo_value.setText(saldo_cliente.add(monto_factura).toString());
					break;
				case 3:
					saldo_value.setText(saldo_cliente.subtract(monto_factura).toString());
					break;
			}
		}
		else{
			EditText saldo_value = (EditText) (findViewById(R.id.SaldoValue));
    		saldo_value.setText("---");
			}
		EditText monto_value = (EditText) (findViewById(R.id.MontoValue));
		monto_value.setText(monto_factura.toString());

	}

	private EditText generarEditText() {
		EditText editText = new EditText(getApplicationContext());
		editText.setTextColor(Color.BLACK);
		editText.setBackgroundColor(Color.WHITE);
		editText.setMaxLines(1);
		editText.setEms(4);
		editText.setHint("Cant");
		editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
		editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			boolean agregar = true;

			@Override
			public void afterTextChanged(Editable s) {

				try {
					ActualizarFilaFactura((TableRow) getCurrentFocus()
							.getParent(), true);
				} catch (Exception e) {
				}
				TableLayout tbl = (TableLayout) findViewById(R.id.tablaProductos);
				if (tbl.getChildCount() > 0) {
					if (last_text_cantidad.getParent() == tbl.getChildAt(tbl
							.getChildCount() - 1)) {
						if (agregar) {
							addRowToTableProductos(null);
							agregar = false;
						}
					}
					;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				try {
					ActualizarFilaFactura((TableRow) getCurrentFocus()
							.getParent(), false);
				} catch (Exception e) {
				}
			}
		});
		last_text_cantidad = editText;
		return editText;
	}

	private void addRowToTableProductos(ProductoVenta pv) {

		
		TableRow tbr = new TableRow(getApplicationContext());
		EditText divider = Factura.this.generarEditText();
		divider.setBackgroundColor(Color.BLACK);
		divider.setWidth(2);
		divider.setInputType(android.text.InputType.TYPE_NULL);
		//Button cruz = Factura.this.generarCruz();
		AutoCompleteTextView autocomplete = Factura.this.generarAutocomplete();
		EditText text_cant = Factura.this.generarEditText();
		tbr.addView(text_cant, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		tbr.addView(divider);
		tbr.addView(autocomplete);
		//tbr.addView(cruz);
		tbr.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		tbr.setBackgroundColor(Color.LTGRAY);
		tbr.setPadding(2, 1, 2, 1);
		TableLayout tbl = (TableLayout) findViewById(R.id.tablaProductos);
		tbl.addView(tbr, tbl.getChildCount());
		if (pv != null) {
			autocomplete.setText(pv.getProducto().toString());
			text_cant.setText(pv.getCantidad().toString());

		}
		tbr.setTag(R.id.datos_extra_producto,new DatosExtraProducto(0,0));
		registerForContextMenu(tbr);
	}

	private AutoCompleteTextView generarAutocomplete() {
		// Toast.makeText(Factura.this," selected", Toast.LENGTH_LONG).show();
		AutoCompleteTextView auto_gen = new AutoCompleteTextView(
				getApplicationContext());
		auto_gen.setAdapter(new ArrayAdapter<Producto>(this,
				android.R.layout.simple_list_item_1, lista_productos));
		auto_gen.setEms(12);
		auto_gen.setHint("Producto");
		auto_gen.setMaxLines(1);
		auto_gen.setHighlightColor(Color.BLUE);
		auto_gen.setHorizontallyScrolling(true);
		auto_gen.setCursorVisible(true);
		auto_gen.setTextColor(Color.BLACK);
		auto_gen.setBackgroundColor(Color.WHITE);
		auto_gen.setDropDownBackgroundResource(android.R.color.white);
		auto_gen.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				//ActualizarFilaFactura((TableRow)getCurrentFocus().getParent(), true);
				
			}
		
		});
		auto_gen.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				if(getCurrentFocus()!=null)
					ActualizarFilaFactura((TableRow)(getCurrentFocus().getParent()), true);
			}

			@Override
			public void afterTextChanged(Editable s) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
				if(getCurrentFocus()!=null)
					ActualizarFilaFactura((TableRow)(getCurrentFocus().getParent()), false);
			}
		});
		return auto_gen;

	}

	@Override
	protected void onResume() {
		super.onResume();

	}
	
	private void ActualizarFilaFactura(TableRow row, Boolean sumar) {
		try {
			 AutoCompleteTextView auto = (AutoCompleteTextView) row.getChildAt(2);
			   EditText cant = (EditText) row.getChildAt(0);
			   String codigo = "";
			   codigo = auto.getText().toString().split("\\s - \\s")[1];
			   int signo = 1;
			   BigDecimal descuento_contado_monto=BigDecimal.ZERO;
			   if (!sumar) signo = -1;
			   BigDecimal precio_uso = BigDecimal.ZERO;
		       switch (cliente.getTipo()) {
		        case 0:
		          precio_uso =  (diccionarioProductos.get(codigo)).getPrecio_cliente_final();
		          break;
		        case 1:
		          precio_uso = (diccionarioProductos.get(codigo)).getPrecio_mayorista();
		          break;
		        case 2:
		          precio_uso = (diccionarioProductos.get(codigo)).getPrecio_distribuidor();
		          break;
		       }
			      
			      precio_uso = precio_uso.multiply(new BigDecimal(cant.getText().toString().replaceAll("\\s+", ""))); 
			      this.monto_factura_sin_descuento = this.monto_factura_sin_descuento.add(new BigDecimal(signo).multiply(precio_uso));
			      BigDecimal descuento = BigDecimal.ZERO;
			      descuento = precio_uso.multiply(new BigDecimal(getDatosExtraProducto(row).getDescuento()).divide(new BigDecimal("100.0")));
			     
			     if (descuento_contado_porcentaje !=0 && tipo==0) {
			         descuento_contado_monto = precio_uso.multiply(new BigDecimal(descuento_contado_porcentaje).divide(new BigDecimal("100.0")));
			   }
			   BigDecimal descuento_cliente = BigDecimal.ZERO;
			   if(cliente.getDescuento_cliente()!=0)
			   {
			    descuento_cliente= precio_uso.multiply(new BigDecimal(cliente.getDescuento_cliente()).divide(new BigDecimal("100.0")));
			   }
			   descuento = descuento.add(descuento_cliente.add(descuento_contado_monto));
			   precio_uso = precio_uso.subtract(descuento);
			   this.monto_factura = this.monto_factura.add(new BigDecimal(signo).multiply(precio_uso));
			   EditText monto_value = (EditText) (findViewById(R.id.MontoValue));
			   monto_value.setText(monto_factura.toString());
			   if(tipo!=0&&tipo!=2)
			   {
				   EditText saldo_value = (EditText) (findViewById(R.id.SaldoValue));
				   switch(tipo){
					case 1:
						saldo_value.setText(saldo_cliente.add(monto_factura).toString());
						break;
					case 3:
						saldo_value.setText(saldo_cliente.subtract(monto_factura).toString());
						break;
				}
				   
			   }
		
		} catch (Exception e) {
		}
}

	public void Facturar(View view) throws JSONException {
		((Button)findViewById(R.id.Facturar)).setActivated(true);
		findViewById(R.id.progressFacturaLayout).setVisibility(View.VISIBLE);
		findViewById(R.id.scrollViewVenta).setFocusable(false);
		String error = null;
		Calendar fecha = Calendar.getInstance();
		//Double monto = 0.0;
		TableLayout tbl = (TableLayout) this.findViewById(R.id.tablaProductos);
		nueva_venta = new Venta(usuario, cliente, fecha, monto_factura.doubleValue(), tipo,monto_factura_sin_descuento.doubleValue());

		for (int i = 0; i < tbl.getChildCount();) {
			TableRow tr = (TableRow) tbl.getChildAt(i);
			AutoCompleteTextView auto = (AutoCompleteTextView) tr.getChildAt(2);
			EditText cant = (EditText) tr.getChildAt(0);
			String codigo = "";
			if((auto.getText()== null ||auto.getText().toString().equals("") || cant.getText()== null || cant.getText().toString().equals("")) &&( tr != tbl.getChildAt(tbl.getChildCount() - 1 )))
			{
				error="Producto o cantidad no seteada en la fila: "+i;
				((Button)findViewById(R.id.Facturar)).setActivated(false);
				findViewById(R.id.progressFacturaLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.scrollViewVenta).setFocusable(true);
				break;
			}
			try {
				if( tr != tbl.getChildAt(tbl.getChildCount() - 1 ))
					codigo = auto.getText().toString().split("\\s - \\s")[1];
			} catch (Exception e) {
				error="Error en el código del producto";
				if( tr == tbl.getChildAt(tbl.getChildCount() - 1 ) && (tbl.getChildCount() == 1 ))
				{
					error="Error, no hay productos para vender";
					((Button)findViewById(R.id.Facturar)).setActivated(false);
					findViewById(R.id.progressFacturaLayout).setVisibility(View.INVISIBLE);
					findViewById(R.id.scrollViewVenta).setFocusable(true);
					break;
				}
			}
			for (int j = 0; j < auto.getAdapter().getCount(); j++) {

				if (((Producto) auto.getAdapter().getItem(j)).getCodigo()
						.equals(codigo)) {
					
						nueva_venta.getProductos().add(
								new ProductoVenta((Producto) auto.getAdapter().getItem(j), Integer.parseInt(cant.getText().toString().replaceAll("\\s+", "")),getDatosExtraProducto(tr).getDescuento(),getDatosExtraProducto(tr).getSin_costo()));
				}
			}

			i = i + 1;
		}

		if (error==null) {
			json = new JSONObject();
			gson = new Gson();
			venta_original = new Venta(nueva_venta.getUsuario(),
					nueva_venta.getCliente(), nueva_venta.getFecha(),
					nueva_venta.getMonto(), nueva_venta.getTipo(),monto_factura_sin_descuento.doubleValue());
			venta_original.setProductos(nueva_venta.getProductos());

			String mensaje = "Advertencia!";
			DecimalFormat formatter = new DecimalFormat("##0.0######");
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Desea realizar una venta al cliente:"
					+ nueva_venta.getCliente().getNombre()
					+ "\n  por un monto de : $"
					+ formatter.format(this.nueva_venta.getMonto()) + "?");
			builder.setTitle(mensaje)
					.setCancelable(false)
					.setNegativeButton("Cancelar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									((Button)findViewById(R.id.Facturar)).setActivated(false);
									findViewById(R.id.progressFacturaLayout).setVisibility(
											View.INVISIBLE);
								}
							})
					.setPositiveButton("Aceptar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									try {
										VentaRecursiva();
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}else Toast.makeText(getApplicationContext(),error, Toast.LENGTH_LONG).show();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.factura, menu);
		
		menu.findItem(R.id.notificacion).setVisible(((Sistema)getApplicationContext()).getNotification());
		notificationReceiver nr=new notificationReceiver(menu.findItem(R.id.notificacion));
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(nr,new IntentFilter("notificacion"));
		
		RespuestasAsincronasReceiver ra=new RespuestasAsincronasReceiver();
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(ra,new IntentFilter("respuestaAsincrona"));
		
		
		if(ultima_venta!=null){
		menu.findItem(R.id.mas_info).setVisible(true);

		}
		
		this.menu = menu;
		if(tipo==0){
			menu.findItem(R.id.contado_radio_button).setChecked(true);
			menu.findItem(R.id.credito_radio_button).setChecked(false);
		}
		else{
			menu.findItem(R.id.credito_radio_button).setChecked(true);
			menu.findItem(R.id.contado_radio_button).setChecked(false);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		
		
		EditText monto_value = (EditText) (findViewById(R.id.MontoValue));
		EditText saldo_value = (EditText) (findViewById(R.id.SaldoValue));
		
		
		switch (item.getItemId()) {
		
			case R.id.contado_radio_button:
				
				if (!item.isChecked())
		    	{
					BigDecimal descuento_contado_monto = monto_factura_sin_descuento.multiply(new BigDecimal(descuento_contado_porcentaje).divide(new BigDecimal(100)));
					monto_factura = monto_factura.subtract(descuento_contado_monto);
		    	}
				tipo=0;
				item.setChecked(true);
	            this.menu.findItem(R.id.credito_radio_button).setChecked(false);
	            monto_value = (EditText) (findViewById(R.id.MontoValue));
	    		monto_value.setText(monto_factura.toString());
	    		saldo_value = (EditText) (findViewById(R.id.SaldoValue));
	    		saldo_value.setText("---");
	    		Toast.makeText(Factura.this,"COMPRA AL CONTADO", Toast.LENGTH_LONG).show();
		        return true;
		        
		    case R.id.credito_radio_button:
		    	
	    		//if(this.menu.findItem(R.id.contado_radio_button).isChecked())
		    	if (!item.isChecked())
	    		{

		    		BigDecimal descuento_contado_monto = monto_factura_sin_descuento.multiply(new BigDecimal(descuento_contado_porcentaje).divide(new BigDecimal(100)));
					monto_factura = monto_factura.add(descuento_contado_monto);	
	    		}
		    	tipo=1;
		    	item.setChecked(true);
		    	this.menu.findItem(R.id.contado_radio_button).setChecked(false);
		    	monto_value = (EditText) (findViewById(R.id.MontoValue));
	    		monto_value.setText(monto_factura.toString());
	    		monto_value = (EditText) (findViewById(R.id.MontoValue));
	    		monto_value.setText(monto_factura.toString());
	    		saldo_value = (EditText) (findViewById(R.id.SaldoValue));
	    		saldo_value.setText(saldo_cliente.add(monto_factura).toString());
		    	Toast.makeText(Factura.this,"COMPRA A CREDITO", Toast.LENGTH_LONG).show();
		        
		    	return true;
		    
		    case R.id.mas_info:
		    	Intent detalle_venta= new Intent(getApplicationContext(),Datos_ultima_venta.class);
		    	detalle_venta.putExtra("venta",ultima_venta);
		    	startActivity(detalle_venta);
		    	return true;
		   
		    case R.id.notificacion:
				Intent notificaciones= new Intent(getApplicationContext(),Notificaciones.class);
		    	startActivity(notificaciones);
		    	return true;	
		    	
		    default:
		    	
	            return super.onOptionsItemSelected(item); 
		}
		
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    context_view=v;
	    inflater.inflate(R.menu.factura_floating_context, menu);
	    menu.findItem(R.id.sin_costo).setChecked(getDatosExtraProducto(context_view).getSin_costo() == 1);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.borrar_fila:
	        	Toast.makeText(Factura.this,"BORRAR FILA", Toast.LENGTH_LONG).show();
	        	TableLayout tbl = (TableLayout) findViewById(R.id.tablaProductos);
				if (tbl.getChildAt(tbl.getChildCount() - 1) != context_view) {
					tbl.removeView((View) context_view);
					ActualizarFilaFactura((TableRow)context_view, false);
				}
	            return true;
	        case R.id.agregar_descuento:
	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        	final View vista = getLayoutInflater().inflate(R.layout.input_descuento,null);
	        	((EditText)vista.findViewById(R.id.input_descuento_editText)).setText(""+descuento_producto);
	        	builder.setTitle("Agregar descuento")
	        			.setView(vista)
				        .setNegativeButton("Cancelar",
				                new DialogInterface.OnClickListener() {
				                    public void onClick(DialogInterface dialog, int id) {
				                        dialog.cancel();
				                    }
				                })
				        .setPositiveButton("Aceptar",
				                new DialogInterface.OnClickListener() {
				                    public void onClick(DialogInterface dialog, int id) {
				                    	try{
				                    		descuento_producto = Integer.parseInt(((EditText)vista.findViewById(R.id.input_descuento_editText)).getText().toString());
				                    	}catch(NumberFormatException e){
				                    		if (((EditText)vista.findViewById(R.id.input_descuento_editText)).getText().toString().equals("")) {
				                    			descuento_producto = 0;
				                    		}
				                    	}
				                    	ActualizarFilaFactura((TableRow)context_view, false);
				                    	getDatosExtraProducto(context_view).setDescuento(descuento_producto);
				                    	ActualizarFilaFactura((TableRow)context_view, true);
				                    }
				                });
				AlertDialog alert = builder.create();
				alert.show();
	            return true;
	        
	        case R.id.sin_costo:
	            if (item.isChecked()){
	            	item.setChecked(false);
	            	ActualizarFilaFactura((TableRow)context_view, true);
	            	getDatosExtraProducto(context_view).setSin_costo(0);
	            }
	            else{
	            	item.setChecked(true);
	            	ActualizarFilaFactura((TableRow)context_view, false);
	            	getDatosExtraProducto(context_view).setSin_costo(1);
	            }
	        	
	        	//aca tengo que asociar al producto venta esta variable
	        	
	        default:
	            return super.onContextItemSelected(item);
	    }
	}

private DatosExtraProducto getDatosExtraProducto(View v){
	return ((DatosExtraProducto)v.getTag(R.id.datos_extra_producto));
}
	
private class DatosExtraProducto {
	private int descuento;
	private int sin_costo;
	
	public DatosExtraProducto(int descuento, int sin_costo) {
		this.descuento = descuento;
		this.sin_costo = sin_costo;
	}
	
	public int getDescuento() {
		return descuento;
	}
	public void setDescuento(int descuento) {
		this.descuento = descuento;
	}
	public int getSin_costo() {
		return sin_costo;
	}
	public void setSin_costo(int sin_costo) {
		this.sin_costo = sin_costo;
	}
}


private class SaldoCliente extends AsyncTask<JSONObject, Void, String> {

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
	protected String doInBackground(JSONObject... params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost("http://ventas.jm-ga.com/api/clientes/saldo/?key="+usuario.getKey());
		// Execute HTTP Post Request
		String text = null;
		try {
			StringEntity se = new StringEntity(params[0].toString(), "UTF8");
			se.setContentType("application/json");
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost,
					localContext);
			HttpEntity entity = response.getEntity();

			text = getASCIIContentFromEntity(entity);
			

		} catch (Exception e) {
		}
		if (text != null) {

			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(text);

				text=jsonObject.getString("saldo");

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return text;
	}

	
	
}


	private class PostNuevaVenta extends AsyncTask<String, Void, String> {

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
		protected String doInBackground(String... params) {

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			String url="http://ventas.jm-ga.com/api/ventas/concreta/";
			
			if(getIntent().getExtras().getInt("tipo")== 2 ||getIntent().getExtras().getInt("tipo")== 3) 
				url="http://ventas.jm-ga.com/api/ventas/devolucion/";
			
			HttpPost httpPost = new HttpPost(url);
			// Execute HTTP Post Request
			String text = null;
			try {
				StringEntity se = new StringEntity(params[0].toString(), "UTF8");
				se.setContentType("application/json");
				httpPost.setEntity(se);
				se.setContentType("application/json");
				httpPost.setEntity(se);
				HttpResponse response = httpClient.execute(httpPost,
						localContext);
				HttpEntity entity = response.getEntity();

				text = getASCIIContentFromEntity(entity);
				return text;

			} catch (Exception e) {
			}
			// return text;

			return null;
		}

		@Override
		protected void onPostExecute(String results) {
			// ((ProgressBar)findViewById(R.id.progressBarFactura)).setVisibility(View.INVISIBLE);
		}

	}

	private class PostNuevaVentaTentativa extends
			AsyncTask<String, Void, String> {

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
		protected String doInBackground(String... params) {

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(
					"http://ventas.jm-ga.com/api/ventas/tentativa/");
			// Execute HTTP Post Request
			String text = null;
			try {
				StringEntity se = new StringEntity(params[0].toString(), "UTF8");
				se.setContentType("application/json");
				httpPost.setEntity(se);
				HttpResponse response = httpClient.execute(httpPost,
						localContext);
				HttpEntity entity = response.getEntity();

				text = getASCIIContentFromEntity(entity);
				return text;

			} catch (Exception e) {
			}
			// return text;

			return null;
		}

		@Override
		protected void onPostExecute(String results) {
			// ((ProgressBar)findViewById(R.id.progressBarFactura)).setVisibility(View.INVISIBLE);
		}

	}

	public void VentaRecursiva() throws JSONException {
		
		String dataString = gson.toJson(nueva_venta, nueva_venta.getClass())
				.toString(); // venta tentativa espera confirmacion
		JSONObject aux_fecha = new JSONObject(dataString); // venta tentativa
															// espera
															// confirmacion
		JSONObject f = (JSONObject) aux_fecha.get("fecha");
		try {
			Integer month = (Integer) f.get("month");
			month = month + 1;
			f.put("month", month);
			dataString = aux_fecha.toString();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		PostNuevaVenta thred = new PostNuevaVenta();// llamo un proceso en
													// backgroud para realizar
													// la venta

		// inicia el proceso de vender
		AsyncTask<String, Void, String> async = thred.execute(dataString);
		try {
			// obtengo la respuesta asincrona
			String respuesta = (String) async.get();
			json = new JSONObject(respuesta);

			// si se creo la venta
			if (json.getString("response").toString()
					.equalsIgnoreCase("Venta creada")) {
				Calendar fecha_venta_registrada = Calendar.getInstance();
				fecha_venta_registrada.set(Calendar.YEAR, Integer
						.parseInt(((JSONObject) json.get("fecha"))
								.getString("year")));
				fecha_venta_registrada.set(Calendar.MONTH, Integer
						.parseInt(((JSONObject) json.get("fecha"))
								.getString("month")) - 1);
				fecha_venta_registrada.set(Calendar.DAY_OF_MONTH, Integer
						.parseInt(((JSONObject) json.get("fecha"))
								.getString("dayOfMonth")));
				fecha_venta_registrada.set(Calendar.HOUR_OF_DAY, Integer
						.parseInt(((JSONObject) json.get("fecha"))
								.getString("hourOfDay")));
				fecha_venta_registrada.set(Calendar.MINUTE, Integer
						.parseInt(((JSONObject) json.get("fecha"))
								.getString("minute")));
				fecha_venta_registrada.set(Calendar.SECOND, Integer
						.parseInt(((JSONObject) json.get("fecha"))
								.getString("second")));

				// JSONObject
				// cliente_venta_creada=((JSONObject)(json.get("cliente")));

				// si la venta no es igual a la que envie originalmente,
				// entonces llamo a venta tentativa!!!!
				if (!(venta_original.getMonto() == Double.parseDouble(json
						.getString("monto").toString())
						&& venta_original
								.getCliente()
								.getRut()
								.equalsIgnoreCase(json.getString("rut_cliente"))
						&& venta_original
								.getCliente()
								.getNombre()
								.equalsIgnoreCase(
										json.getString("nombre_cliente"))
						&& venta_original.getFecha().get(Calendar.YEAR) == fecha_venta_registrada
								.get(Calendar.YEAR)
						&& venta_original.getFecha().get(Calendar.MONTH) == fecha_venta_registrada
								.get(Calendar.MONTH)
						&& venta_original.getFecha().get(Calendar.DAY_OF_MONTH) == fecha_venta_registrada
								.get(Calendar.DAY_OF_MONTH)
						&& venta_original.getFecha().get(Calendar.HOUR_OF_DAY) == fecha_venta_registrada
								.get(Calendar.HOUR_OF_DAY)
						&& venta_original.getFecha().get(Calendar.MINUTE) == fecha_venta_registrada
								.get(Calendar.MINUTE) && venta_original
						.getFecha().get(Calendar.SECOND) == fecha_venta_registrada
						.get(Calendar.SECOND))) {

					// llamo a crear venta tentativa con nueva_venta
					dataString = gson.toJson(venta_original,
							venta_original.getClass()).toString();
					JSONObject aux = new JSONObject(dataString); // venta
																	// tentativa
																	// espera
																	// confirmacion
					aux.put("venta_id", json.getString("venta_id"));
					aux.put("monto", json.getString("monto"));
					JSONObject fecha = (JSONObject) aux.get("fecha");
					try {
						Integer month = (Integer) fecha.get("month");
						month = month + 1;
						fecha.put("month", month);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					PostNuevaVentaTentativa thred_venta_tentativa = new PostNuevaVentaTentativa();// llamo
																									// un
																									// proceso
																									// en
																									// backgroud
																									// para
																									// realizar
																									// la
																									// venta
					// inicia el proceso de vender
					AsyncTask<String, Void, String> th_async_tentativa = thred_venta_tentativa.execute(aux.toString());
					// falta controlar errores de las ventas tentativas segun la
					// respuesta del server para la venta tentativa
				}
				findViewById(R.id.progressFacturaLayout).setVisibility(
						View.INVISIBLE);
				((Button)findViewById(R.id.Facturar)).setActivated(false);
				String mensaje = "Venta exitosa!";
				DecimalFormat formatter = new DecimalFormat("##0.0######");

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Su pedido se proceso correctamente.\n"
						+ "Para el cliente :"
						+ nueva_venta.getCliente().getNombre()
						+ "\n  Con un monto de :"
						+ formatter.format(this.nueva_venta.getMonto())
						+ "\n ¿Desea agregar notas sobre este Pedido?");
				builder.setTitle(mensaje)
						.setCancelable(false)
						.setNegativeButton("Cancelar",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										findViewById(R.id.scrollViewVenta)
												.setFocusable(true);
										dialog.cancel();
										Intent loc = new Intent(
												getApplicationContext(),
												DetalleCliente.class);
										loc.putExtra(
												"cliente",
												getIntent().getExtras()
														.getParcelable(
																"cliente"));
										loc.putExtra(
												"usuario",
												getIntent().getExtras()
														.getParcelable(
																"usuario"));
										startActivity(loc);

									}
								})
						.setPositiveButton("Aceptar",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										try {
											findViewById(R.id.scrollViewVenta)
													.setFocusable(true);
											// Toast.LENGTH_LONG).show();
											Intent loc = new Intent(
													getApplicationContext(),
													NotaActivity.class);
											loc.putExtra(
													"usuario",
													getIntent().getExtras()
															.getParcelable(
																	"usuario"));
											loc.putExtra(
													"cliente",
													getIntent().getExtras()
															.getParcelable(
																	"cliente"));
											loc.putExtra("venta_id",
													json.getString("venta_id"));
											startActivity(loc);
										} catch (Exception e) {
										}
									}
								});
				AlertDialog alert = builder.create();
				alert.show();

			}
			if (json.getString("response").toString()
					.equalsIgnoreCase("Stock insuficiente")) {

				String prod_a_mostrar = "";

				JSONObject ventaObj = ((JSONObject) json.get("venta"));
				BigDecimal mnt = BigDecimal.ZERO;
				// sino llamo nuevamente al proceso de vender con nueva_venta
				// arreglada

				ArrayList<ProductoVenta> nuevaListaProductos = new ArrayList<ProductoVenta>();
				JSONArray productos_nueva_venta = (JSONArray) ventaObj
						.get("productos");
				for (int i = 0; i < productos_nueva_venta.length(); i++) {
					String codigo = ((JSONObject) productos_nueva_venta.get(i))
							.get("codigo").toString();
					Producto p = diccionarioProductos.get(codigo);
					int cant = Integer
							.parseInt(((JSONObject) productos_nueva_venta
									.get(i)).get("cantidad").toString());
					int descuento = Integer
							.parseInt(((JSONObject) productos_nueva_venta
									.get(i)).get("descuento").toString());
					int sin_costo = Integer
							.parseInt(((JSONObject) productos_nueva_venta
									.get(i)).get("sin_costo").toString());
					
					ProductoVenta pv = new ProductoVenta(p, cant,descuento,sin_costo);
					
					if(sin_costo!=1)
					{
						
						BigDecimal descuento_prod =BigDecimal.ZERO;
					
						switch (nueva_venta.getCliente().getTipo()) {
						case 0:
						
							descuento_prod = p.getPrecio_cliente_final().multiply(new BigDecimal(descuento).divide(new BigDecimal(100)));
							mnt = mnt.add(p.getPrecio_cliente_final().subtract(descuento_prod).multiply(new BigDecimal(cant)));
							break;
							
						case 1:
							descuento_prod = p.getPrecio_mayorista().multiply(new BigDecimal(descuento).divide(new BigDecimal(100)));
							mnt = mnt.add(p.getPrecio_mayorista().subtract(descuento_prod).multiply(new BigDecimal(cant)));
							break;
							
						case 2:
							descuento_prod = p.getPrecio_distribuidor().multiply(new BigDecimal(descuento).divide(new BigDecimal(100)));
							mnt = mnt.add(p.getPrecio_distribuidor().subtract(descuento_prod).multiply(new BigDecimal(cant)));
							break;
							
					}
					}
					nuevaListaProductos.add(pv);
					prod_a_mostrar = "Producto :" + prod_a_mostrar
							+ pv.getProducto().getNombre() + "\nCodigo :"
							+ pv.getProducto().getCodigo() + "\nCantidad :"
							+ pv.getCantidad().toString() + "\n";
				}

				nueva_venta.setProductos(nuevaListaProductos);
				
				if (descuento_contado_porcentaje != 0 && tipo==0) {
					BigDecimal descuento_contado_monto= mnt.multiply(new BigDecimal(descuento_contado_porcentaje).divide(new BigDecimal(100)));
					mnt = mnt.subtract(descuento_contado_monto) ;
					nueva_venta.setMonto(mnt.doubleValue());
				} else {
					nueva_venta.setMonto(mnt.doubleValue());
				}
				findViewById(R.id.progressFacturaLayout).setVisibility(
						View.INVISIBLE);
				((Button)findViewById(R.id.Facturar)).setActivated(false);
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				ArrayAdapter<ProductoVenta> adaptador_venta_modificada = new ArrayAdapter<ProductoVenta>(
						this.getApplicationContext(),
						R.layout.lista_productos_venta,
						nueva_venta.getProductos());
				builder.setAdapter(adaptador_venta_modificada, null);
				DecimalFormat formatter = new DecimalFormat("##0.0######");
				builder.setTitle(
						"Desea Realizar la siguiente venta por un total de :"
								+ formatter.format(this.nueva_venta.getMonto())
								+ "?")
						.setCancelable(false)
						.setNegativeButton("Cancelar",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										findViewById(R.id.scrollViewVenta)
												.setFocusable(true);
										dialog.cancel();
									}
								})
						.setPositiveButton("Aceptar",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										try {
											VentaRecursiva();
										} catch (Exception e) {
										}
									}
								});
				AlertDialog alert = builder.create();
				alert.show();

			}
			if(json.getString("response").toString().equalsIgnoreCase("Devolucion creada"))
			{
				findViewById(R.id.progressFacturaLayout).setVisibility(View.INVISIBLE);
				((Button)findViewById(R.id.Facturar)).setActivated(false);
				Toast.makeText(Factura.this,"devolucion creada ", Toast.LENGTH_LONG).show();
				
			}

		} catch (Exception e) {
			System.out.print(e.getMessage());
		}

	}

	private void cargar_ultima_venta(Venta ultima_venta) {
		for (int i = 0; i < ultima_venta.getProductos().size(); i++) {
			ProductoVenta pv = ultima_venta.getProductos().get(i);
			addRowToTableProductos(pv);
			TableLayout tbl = (TableLayout) findViewById(R.id.tablaProductos);
			if (tbl.getChildCount() > 1
					&& i != ultima_venta.getProductos().size() - 1) {
				tbl.removeViewAt(tbl.getChildCount() - 1);
			}
		}
		
		this.monto_factura_sin_descuento=new BigDecimal(ultima_venta.getMonto_sin_descuentos().toString());
		this.monto_factura =new BigDecimal(ultima_venta.getMonto().toString());
		this.tipo=ultima_venta.getTipo();

	}

}

package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

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
import com.example.dominio.Nota;
import com.google.gson.Gson;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class NotaActivity extends Activity {

	 Button btnSelectDate,btnSelectTimeInit,btnSelectTimeEnd,btn_registrar_nota;
     
     static final int DATE_DIALOG_ID = 0;
     static final int TIME_INIT_DIALOG_ID = 1;
     static final int TIME_END_DIALOG_ID = 2;
     private boolean datePicked = false;
     private boolean initTime= false;
     private boolean endTime= false;
     private Cliente cliente=null;
     
     // variables to save user selected date and time
     public  int year,month,day,hourInit,hourEnd,minuteInit,minuteEnd=1;  
     // declare  the variables to Show/Set the date and time when Time and  Date Picker Dialog first appears
     private int mYear, mMonth, mDay,mHourInit,mMinuteInit,mHourEnd,mMinuteEnd=1; 
     
     // constructor
     public NotaActivity()
     {
                 // Assign current Date and Time Values to Variables
    	 		 
     }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nota);
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		
		cliente=(Cliente)getIntent().getExtras().getParcelable("cliente");
		 
		 Calendar cal_dia = Calendar.getInstance();
		 if (cliente.getDia_de_entrega()!=null)
		 {
			 Integer dia_semana=cal_dia.get(Calendar.DAY_OF_WEEK);
		 
	 		 while(cliente.getDia_de_entrega()!=dia_semana)
	 		 {
	 			 cal_dia.add(Calendar.DAY_OF_WEEK, 1);
	 			 dia_semana=cal_dia.get(Calendar.DAY_OF_WEEK);
	 		 }
		 }           
        final Calendar c = cal_dia;
        mYear = c.get(Calendar.YEAR);
        
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHourInit = c.get(Calendar.HOUR_OF_DAY);
        mMinuteInit = c.get(Calendar.MINUTE);
        mHourEnd = c.get(Calendar.HOUR_OF_DAY);
        mMinuteEnd= c.get(Calendar.MINUTE);
        if(cliente.getHora_de_entrega_desde()!=null)
        {
        	hourInit = cliente.getHora_de_entrega_desde().get(Calendar.HOUR_OF_DAY);
        	minuteInit = cliente.getHora_de_entrega_desde().get(Calendar.MINUTE);
        }else
        {
        	hourInit=minuteInit=0;
        }
        if(cliente.getHora_de_entrega_hasta()!=null) {	
        	hourEnd = cliente.getHora_de_entrega_hasta().get(Calendar.HOUR_OF_DAY);
        	minuteEnd=cliente.getHora_de_entrega_hasta().get(Calendar.MINUTE);
        }
        else
        {
        	hourEnd=minuteEnd=0;
        }
        year=mYear;
        //month=mMonth;
        day=mDay;
        // get the references of buttons
        btnSelectDate=(Button)findViewById(R.id.buttonSelectDate);
        btnSelectTimeInit=(Button)findViewById(R.id.btn_tiempo_entrega_desde);
        btnSelectTimeEnd=(Button)findViewById(R.id.btn_tiempo_entrega_hasta);
        btn_registrar_nota=(Button)findViewById(R.id.btn_registrar_nota);
        btnSelectDate.setText("Fecha elegida : "+mDay+"-"+(mMonth+1)+"-"+mYear);
        btnSelectTimeInit.setText("Hora elegida :"+hourInit+"-"+minuteInit);
        btnSelectTimeEnd.setText("Hora elegida :"+hourEnd+"-"+minuteEnd);
        // Set ClickListener on btnSelectDate 
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View v) {
                // Show the DatePickerDialog
            	btnSelectDate.setActivated(true);
                 showDialog(DATE_DIALOG_ID);
            }
        });
      
        // Set ClickListener on btnSelectTime
        btnSelectTimeInit.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View v) {
                // Show the TimePickerDialog
            	btnSelectTimeInit.setActivated(true);
                 showDialog(TIME_INIT_DIALOG_ID);
            }
        });
        
     // Set ClickListener on btnSelectTime
        btnSelectTimeEnd.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View v) {
                // Show the TimePickerDialog
            	btnSelectTimeEnd.setActivated(true);
                 showDialog(TIME_END_DIALOG_ID);
            }
        });
		
        btn_registrar_nota.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((Button)findViewById(R.id.btn_registrar_nota)).setActivated(true);
				findViewById(R.id.progressBarNotaLayout).setVisibility(View.VISIBLE);
				findViewById(R.id.scrollViewNota).setFocusable(false);
				EditText txt=(EditText)findViewById(R.id.editTextRedaccionNota);
				Calendar cal_date=Calendar.getInstance();
				cal_date.set(year, month, day);
				
				Calendar cal_time_ini=Calendar.getInstance();
				cal_time_ini.set(Calendar.MONTH, 1);
				cal_time_ini.set(Calendar.HOUR_OF_DAY,hourInit);
				cal_time_ini.set(Calendar.MINUTE,minuteInit);
				cal_time_ini.set(Calendar.SECOND,00);
				
				Calendar cal_time_end=Calendar.getInstance();
				cal_time_end.set(Calendar.MONTH, 1);
				cal_time_end.set(Calendar.HOUR_OF_DAY,hourEnd);
				cal_time_end.set(Calendar.MINUTE,minuteEnd);
				cal_time_end.set(Calendar.SECOND,00);
				
				Nota n = new Nota(txt.getText().toString(), cal_time_ini,cal_time_end, cal_date);
				
				if(!datePicked && cliente.getDia_de_entrega()== null) {
					n.setFecha_de_entrega(null);
				}
				if(!initTime && cliente.getHora_de_entrega_desde()== null) {
					n.setHora_de_entrega_desde(null);
				} 
				if(!endTime && cliente.getHora_de_entrega_hasta()== null) {
					n.setHora_de_entrega_hasta(null);
				}
				
				Gson gson=new Gson();
				String dataString = gson.toJson(n, n.getClass()).toString();
				JSONObject aux=null;
				try {
					aux = new JSONObject(dataString);
					aux.put("venta_id", getIntent().getExtras().get("venta_id"));
					JSONObject f=(JSONObject)aux.get("fecha_de_entrega");
					Integer month=(Integer) f.get("month");
					month=month+1;
					f.put("month",month);
					aux.put("fecha_de_entrega", f);
					dataString=aux.toString();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//venta tentativa espera confirmacion
				
				PostRegistrarNota thred_registrar_nota=new PostRegistrarNota();//llamo un proceso en backgroud para realizar la venta
				//inicia el proceso de vender
    			
				AsyncTask<String, Void, String> th_async_regitrar_nota=thred_registrar_nota.execute(dataString);	     
    			String respuesta;
				try {
					respuesta = (String)th_async_regitrar_nota.get();
					Toast.makeText(NotaActivity.this,respuesta,10 ).show();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((Button)findViewById(R.id.btn_registrar_nota)).setActivated(false);
				findViewById(R.id.progressBarNotaLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.scrollViewNota).setFocusable(true);
			}
		});
	}

	// Register  DatePickerDialog listener
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                       // the callback received when the user "sets" the Date in the DatePickerDialog
                               public void onDateSet(DatePicker view, int yearSelected,
                                                     int monthOfYear, int dayOfMonth) {
                                  year = yearSelected;
                                  month = monthOfYear;
                                  day = dayOfMonth;
                                  // Set the Selected Date in Select date Button
                                  btnSelectDate.setText("Fecha elegida : "+day+"-"+month+"-"+year);
                                  btnSelectDate.setActivated(false);
                                  datePicked = true;
                               }
                           };

      // Register  TimePickerDialog listener                 
      private TimePickerDialog.OnTimeSetListener mTimeInitSetListener =new TimePickerDialog.OnTimeSetListener() {
                        // the callback received when the user "sets" the TimePickerDialog in the dialog
                           public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                                       hourInit = hourOfDay;
                                       minuteInit = min;
                                       // Set the Selected Date in Select date Button
                                       btnSelectTimeInit.setText("Hora elegida :"+hourInit+"-"+minuteInit);
                                       btnSelectTimeInit.setActivated(false);
                                       initTime = true;
                                     }
                               };
       private TimePickerDialog.OnTimeSetListener mTimeEndSetListener =new TimePickerDialog.OnTimeSetListener() {
                                   // the callback received when the user "sets" the TimePickerDialog in the dialog
                                      public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                                                  hourEnd = hourOfDay;
                                                  minuteEnd = min;
                                                  // Set the Selected Date in Select date Button
                                                  btnSelectTimeEnd.setText("Hora elegida :"+hourEnd+"-"+minuteEnd);
                                                  btnSelectTimeEnd.setActivated(true);
                                                  endTime = true;
                                                }
                                          };

   // Method automatically gets Called when you call showDialog()  method
   @Override
   protected Dialog onCreateDialog(int id) {
           switch (id) {
               case DATE_DIALOG_ID:
            	   // create a new DatePickerDialog with values you want to show 
            	   return new DatePickerDialog(this,mDateSetListener,mYear, mMonth, mDay);
                       // create a new TimePickerDialog with values you want to show 
               case TIME_INIT_DIALOG_ID:
                   return new TimePickerDialog(this,mTimeInitSetListener, mHourInit, mMinuteInit, false);
               
               case TIME_END_DIALOG_ID:
                   return new TimePickerDialog(this,mTimeEndSetListener, mHourEnd, mMinuteEnd, false);
                              
			}
           return null;
   }
                           
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.nota, menu);
			menu.findItem(R.id.notificacion).setVisible(((Sistema)getApplicationContext()).getNotification());
			notificationReceiver nr=new notificationReceiver(menu.findItem(R.id.notificacion));
			LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(nr,new IntentFilter("notificacion"));
			return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		
			case R.id.notificacion:
				Intent notificaciones= new Intent(getApplicationContext(),Notificaciones.class);
		    	startActivity(notificaciones);
		    	return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private class PostRegistrarNota extends AsyncTask <String, Void, String > {
		
		 
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
            HttpPost httpPost = new HttpPost("http://ventas.jm-ga.com/api/notas/");
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

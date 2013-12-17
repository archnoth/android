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

import com.example.dominio.Nota;
import com.google.gson.Gson;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.view.Menu;
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
     
     // variables to save user selected date and time
     public  int year,month,day,hourInit,hourEnd,minuteInit,minuteEnd;  
     // declare  the variables to Show/Set the date and time when Time and  Date Picker Dialog first appears
     private int mYear, mMonth, mDay,mHourInit,mMinuteInit,mHourEnd,mMinuteEnd; 
     
     // constructor
     public NotaActivity()
     {
                 // Assign current Date and Time Values to Variables
                 final Calendar c = Calendar.getInstance();
                 mYear = c.get(Calendar.YEAR);
                 mMonth = c.get(Calendar.MONTH);
                 mDay = c.get(Calendar.DAY_OF_MONTH);
                 mHourInit = c.get(Calendar.HOUR_OF_DAY);
                 mMinuteInit = c.get(Calendar.MINUTE);
                 mHourEnd = c.get(Calendar.HOUR_OF_DAY);
                 mMinuteEnd= c.get(Calendar.MINUTE);
     }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nota);
		
        // get the references of buttons
        btnSelectDate=(Button)findViewById(R.id.buttonSelectDate);
        btnSelectTimeInit=(Button)findViewById(R.id.btn_tiempo_entrega_desde);
        btnSelectTimeEnd=(Button)findViewById(R.id.btn_tiempo_entrega_hasta);
        btn_registrar_nota=(Button)findViewById(R.id.btn_registrar_nota);
        
        // Set ClickListener on btnSelectDate 
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View v) {
                // Show the DatePickerDialog
                 showDialog(DATE_DIALOG_ID);
            }
        });
      
        // Set ClickListener on btnSelectTime
        btnSelectTimeInit.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View v) {
                // Show the TimePickerDialog
                 showDialog(TIME_INIT_DIALOG_ID);
            }
        });
        
     // Set ClickListener on btnSelectTime
        btnSelectTimeEnd.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View v) {
                // Show the TimePickerDialog
                 showDialog(TIME_END_DIALOG_ID);
            }
        });
		
        btn_registrar_nota.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText txt=(EditText)findViewById(R.id.editTextRedaccionNota);
				Calendar cal_date=Calendar.getInstance();
				cal_date.set(year, month, day);
				
				Calendar cal_time_ini=Calendar.getInstance();
				cal_date.set(year, month, day, hourInit, minuteInit);
				
				Calendar cal_time_end=Calendar.getInstance();
				cal_date.set(year, month, day, hourEnd, minuteEnd);
				
				Nota n = new Nota(txt.getText().toString(), cal_time_ini,cal_time_end, cal_date);
				if(!datePicked) {
					n.setFecha_de_entrega(null);
				}
				if(!initTime) {
					n.setHora_de_entrega_desde(null);
				}
				if(!endTime) {
					n.setHora_de_entrega_hasta(null);
				}
				
				Gson gson=new Gson();
				String dataString = gson.toJson(n, n.getClass()).toString();
				
				JSONObject aux=null;
				try {
					aux = new JSONObject(dataString);
					aux.put("venta_id", getIntent().getExtras().get("venta_id"));
    				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //venta tentativa espera confirmacion
				
				PostRegistrarNota thred_registrar_nota=new PostRegistrarNota();//llamo un proceso en backgroud para realizar la venta
				//inicia el proceso de vender
    			
				AsyncTask<String, Void, String> th_async_regitrar_nota=thred_registrar_nota.execute(aux.toString());	     
    			String respuesta;
				try {
					respuesta = (String)th_async_regitrar_nota.get();
					Toast.makeText(NotaActivity.this,respuesta, Toast.LENGTH_LONG).show();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
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
                                  btnSelectDate.setText("Fecha elejida : "+day+"-"+month+"-"+year);
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
                                       btnSelectTimeInit.setText("Hora elejida :"+hourInit+"-"+minuteInit);
                                       initTime = true;
                                     }
                               };
       private TimePickerDialog.OnTimeSetListener mTimeEndSetListener =new TimePickerDialog.OnTimeSetListener() {
                                   // the callback received when the user "sets" the TimePickerDialog in the dialog
                                      public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                                                  hourEnd = hourOfDay;
                                                  minuteEnd = min;
                                                  // Set the Selected Date in Select date Button
                                                  btnSelectTimeEnd.setText("Hora elejida :"+hourEnd+"-"+minuteEnd);
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
		return true;
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
            	 StringEntity se = new StringEntity(params[0].toString());
            	 se.setContentEncoding("UTF-8");
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

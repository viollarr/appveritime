package br.com.veritime2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


public class MainActivity extends Activity {

	private Activity instaciaActivity = MainActivity.this;
	private SharedPreferences sharedPreferences;
	private final String PREFS_PRIVATE = "PREFS_PRIVATE";
	private JSONArray grupos;
	private JSONArray andamentoArray;
	String finalizados = "0";
	String emAtraso = "0";
	String emEspera = "0";
	String emAndamento = "0";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//INICIANDO O SERVIÇO PARA SER USADO COM AS NOTIFICAÇÕES 
		//Context contexto = null;
		//agendar(contexto,240);
		
		
		
		
		
		ConsultaImeil();
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new LocationListener() {
    	    public void onLocationChanged(Location location) {
    	    	GravaPreferencias("latitudeFinal",""+location.getLatitude());
				GravaPreferencias("longitudeFinal",""+location.getLongitude());
    	    }

    	    public void onStatusChanged(String provider, int status, Bundle extras) {}

    	    public void onProviderEnabled(String provider) {}

    	    public void onProviderDisabled(String provider) {}
    	  };

    	  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 50, locationListener);
	}
	
	@SuppressWarnings("unused")
	private void agendar(Context contexto, int segundos){
		
		//Log.i("teste", "Chamando a activity Servico");
		Intent it = new Intent("SERVICO");
		PendingIntent p = PendingIntent.getService(contexto, 0, it, 0);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.add(Calendar.SECOND, segundos);
		AlarmManager alarme = (AlarmManager) contexto.getSystemService(Context.ALARM_SERVICE);
		long tempo = c.getTimeInMillis();
		alarme.set(AlarmManager.RTC_WAKEUP, tempo, p);
		
		/*
		Intent intent = new Intent(contexto, br.com.veritime2.MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //contexto.startActivity(intent);
        PendingIntent p = PendingIntent.getService(contexto, 0, intent, 0);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.add(Calendar.SECOND, segundos);
		AlarmManager alarme = (AlarmManager) contexto.getSystemService(Context.ALARM_SERVICE);
		long tempo = c.getTimeInMillis();
		alarme.set(AlarmManager.RTC_WAKEUP, tempo, p);
		*/
		
	}
	
	public void GravaPreferencias( String nomeCampo, String conteudoCampo) {
		 
		sharedPreferences = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
		 
		Editor prefsPrivateEditor = sharedPreferences.edit();
		
		prefsPrivateEditor.putString(nomeCampo, conteudoCampo);
		prefsPrivateEditor.commit();
		 
	}
	
	protected void ConsultaImeil(){
		//Context context = null;
		String urlImei = "http://www.veritime.com.br/admin/acesso/recuperarImei";
		
		ProcessoImei processo2 = new ProcessoImei(this);
        processo2.execute(urlImei);
		
	}
 
    public class ProcessoImei extends AsyncTask <String, String, Integer>{
    	private String strJson;
    	private HttpClient httpclient = null;
    	
    	public ProcessoImei(Context context){
    	}
    	
    	@Override
    	protected void onPreExecute(){

    	}
    	
    	
    	@Override
    	protected Integer doInBackground(String... paramss){
    		strJson = null;

    		try{
    			URI urlService = new URI(paramss[0]);
    			TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);		
				String deviceid = tm.getDeviceId();
    			
    			ArrayList<NameValuePair> parametrosPost = new ArrayList<NameValuePair>();  
    			parametrosPost.add(new BasicNameValuePair("idTelefone", deviceid));
    			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parametrosPost);
    			
    			HttpPost http = new HttpPost(urlService);
    			http.setEntity(formEntity);
    			
				
    			//Tratamento de timeout
    			HttpParams httpParameters = new BasicHttpParams();
    			HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
    			HttpConnectionParams.setSoTimeout(httpParameters,  4000);
    			httpclient = new DefaultHttpClient(httpParameters);
    			
    			
    			http.setHeader("Accept", "Aplication/json");   			
    			HttpResponse httpResponse = httpclient.execute(http);
    			int receivedStatusCode = httpResponse.getStatusLine().getStatusCode();

    			
    			if(receivedStatusCode == HttpStatus.SC_OK){
    				InputStream inputStream = httpResponse.getEntity().getContent();   				
    				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8);
    				StringBuilder builder = new StringBuilder();
    				for (String line = null; (line = reader.readLine()) != null;) {
    				    builder.append(line).append("\n");
    				}
    				
    				strJson = builder.toString();
    				
    				return 1;
    			}else{
    				return 0;
    			}
    			
    		}catch(Exception e){		 
				return 0; 			
    		}

    	}
    	
    	@Override
    	protected void onProgressUpdate(String... values){
    		//progress.setMessage(values[0]);
    	}
    	
    	@Override
    	protected void onPostExecute(Integer result){
    		
    		if(result==0){
 				//QUANDO NAO SE CONSEGUE CONEXAO COM SERVIDOR RECUPERA CONTEÚDO JA SALVO NO CACHE DO APARELHO (Erro 1.:)-------------------------------------------------------------
    			
    			sharedPreferences = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
    			
    			if(sharedPreferences.getString("strJson", "strJson").equals("strJson")){
    				finish();
    				intencao(Login.class);
    			}
    			else{
    			
    				Toast.makeText(instaciaActivity, "Você está sem acesso à internet. Os dados serão sincronizados na próxima conexão.", Toast.LENGTH_LONG).show();
					Intent it =  new  Intent(instaciaActivity, GruposAtendimentos.class);
	
		    		finish();
		  			startActivity(it);
    			}
			
				//-----------------------------------------------------------------------------------------    			
    		}else if(result == 1){
    			try{
     				
    				/*
    				AlertDialog.Builder errorAlertDialog = new AlertDialog.Builder(instaciaActivity);
    	    		errorAlertDialog.setTitle("Erro");
    	    		errorAlertDialog.setMessage(strJson);
    	    		errorAlertDialog.setNeutralButton("OK", null);
    	    		errorAlertDialog.show();
    				*/
    				GravaPreferencias("strJson", strJson);
    				       				
    				JSONObject json = new JSONObject(strJson.substring(strJson.indexOf('{')));
    				JSONArray acesso = json.getJSONArray("login");
    				JSONObject fun = new JSONObject(acesso.getString(0));
    				
    				grupos  = fun.getJSONArray("grupos");
    				for(Integer i1 = 0; i1 < grupos.length(); i1++){

    					JSONObject at 	= new JSONObject(grupos.getString(i1));
    					

    					if(at.getString("status").substring(0, 9).equals("finalizad")){	
    						if(at.getInt("qtde") < 50){
    							finalizados = at.getString("qtde");
    						}
    						else{
    							finalizados = "50";
    						}
    					}
    					else if(at.getString("status").substring(0, 9).equals("em_espera")){
    						if(at.getInt("qtde") < 50){
    							emEspera = at.getString("qtde");
    						}
    						else{
    							emEspera = "50";
    						}
    					}
    					else if(at.getString("status").substring(0, 9).equals("em_andame")){
    						if(at.getInt("qtde") < 50){
    							emAndamento = at.getString("qtde");
    						}
    						else{
    							emAndamento = "50";
    						}
    					}			
    					else if(at.getString("status").substring(0, 9).equals("em_atraso")){
    						if(at.getInt("qtde") < 50){
    							emAtraso = at.getString("qtde");
    						}
    						else{
    							emAtraso = "50";
    						}
    					}	
    								
    				}
    				
    				andamentoArray = fun.getJSONArray("emAndamento");
    				
    				if(andamentoArray.length() > 0){
    				
	    				for(Integer i = 0; i < andamentoArray.length(); i++ ){
	    					JSONObject and 	= new JSONObject(andamentoArray.getString(i));

	    					GravaPreferencias("CheckId", and.getString("idatendimento"));
	    					GravaPreferencias("CheckTitulo", and.getString("titulo"));
	    					GravaPreferencias("CheckInTitulo", and.getString("titulo"));
	    					GravaPreferencias("CheckAberto", "true");
	    				}
    				
    				}
    				else{
    					GravaPreferencias("CheckAberto", "false");
    				
    				}
    				
    				GravaPreferencias("finalizados",finalizados);//qtd
    				GravaPreferencias("emEspera",emEspera);//qtd
    				GravaPreferencias("emAndamento",emAndamento);//qtd
    				GravaPreferencias("emAtraso",emAtraso); //qtd
    				
					GravaPreferencias("Andamento", fun.getString("emAndamento")); //Array grupos
					GravaPreferencias("Espera", fun.getString("emEspera")); //Array grupos
					GravaPreferencias("Atraso", fun.getString("emAtraso")); //Array grupos
					GravaPreferencias("Finalizado", fun.getString("finalizado")); //Array grupos
    				
    				GravaPreferencias("sms",fun.getString("sms"));
    				GravaPreferencias("email",fun.getString("email"));
    				GravaPreferencias("notificacao_valida","true");
    				GravaPreferencias("notificacao_texto",fun.getString("notificacao"));
    				GravaPreferencias("nome_usuario",fun.getString("nome_usuario"));
    				GravaPreferencias("id_usuario",fun.getString("id_usuario"));
    				
    				GravaPreferencias("salvarCheckIn","false");
    				GravaPreferencias("salvarCheckOut","false");
					
					
      				
    				if(fun.getBoolean("acesso")){
      				
    					Intent it =  new  Intent(instaciaActivity, GruposAtendimentos.class);

    			    	finish();
    			  		startActivity(it); 
    					
    				}
    				else{
 						Toast.makeText(instaciaActivity, "Este aparelho não está registrado no sistema. Acesse com seu login e senha.", Toast.LENGTH_SHORT).show();
 						finish();
						intencao(Login.class);					
    				}
    				
        			}catch(Exception e){
        				Toast.makeText(instaciaActivity, "Este aparelho não está registrado no sistema. Acesse com seu login e senha.", Toast.LENGTH_SHORT).show();
        				finish();
						intencao(Login.class);
        		}
	      			
    		}
    	}
    	
    }
	
    
	private void intencao(Class<Login> nomeClass){
   		Intent it =  new  Intent(instaciaActivity, nomeClass);
  		startActivity(it);             		 
 	 }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}

package br.com.veritime2;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

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
import android.util.Log;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.MediaStore.Audio;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GruposAtendimentos extends Activity {
	String idUsuarioAtendimento;
	private Button iniciar_servico;
	private Button finalizar_servico;
	private Button grupos_atendimentos;
	private Button grupos_espera;
	private Button grupos_atrasados;
	private Button grupos_finalizados;
	private Button qtd_atendimento;
	private Button qtd_espera;
	private Button qtd_atrasados;
	private Button qtd_finalizados;
	private Button atualizar;
	private Button meuLocal;
	private String latitudeFinal;
	private String longitudeFinal;
	private String latitudeUsuario;
	private String longitudeUsuario;
	private String horarioFinal;
	private String idAtendimentoFinal;
	private String idUsuarioFinal;
	private String tipoCheckFinal;	
	private String titulo;
	private String txtStatus;
	private String txtObservavao;
	private String idRelacionamento;
	private SharedPreferences sharedPreferences;
	private final String PREFS_PRIVATE = "PREFS_PRIVATE";
	private TextView saudacao;
	private String texto;
	private Activity instaciaActivity = GruposAtendimentos.this;
	private JSONArray grupos;
	private JSONArray andamentoArray;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Log.i("Serviço", "Finaliza o serviço");
		stopService(new Intent("INICIAR_SERVICO"));
		setContentView(R.layout.gruposatendimentos);
		saudacao 			= (TextView) findViewById(R.id.texto);
		//iniciar_servico		= (Button) findViewById(R.id.btnIniciarServico);
		//finalizar_servico		= (Button) findViewById(R.id.btnFinalizarServico);
		grupos_atendimentos	= (Button) findViewById(R.id.grupos_atendimentos);
		grupos_espera 		= (Button) findViewById(R.id.grupos_espera);
		grupos_atrasados 	= (Button) findViewById(R.id.grupos_atrasados);
		grupos_finalizados 	= (Button) findViewById(R.id.grupos_finalizados);
		atualizar			= (Button) findViewById(R.id.atualizar);
		meuLocal			= (Button) findViewById(R.id.local);		
		qtd_atendimento 	= (Button) findViewById(R.id.qtd_atendimento);
		qtd_espera 			= (Button) findViewById(R.id.qtd_espera);
		qtd_atrasados 		= (Button) findViewById(R.id.qtd_atrasados);
		qtd_finalizados 	= (Button) findViewById(R.id.qtd_finalizados);
		
		try{
		 
		sharedPreferences = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
			
		 String strJson = sharedPreferences.getString("strJson", "strJson");
		 idUsuarioAtendimento = sharedPreferences.getString("id_usuario", "id_usuario");
	 		 
		 GravaPreferencias("strJson", strJson);

		 
		 
		 if(!sharedPreferences.getString("emAndamento", "emAndamento").equals("0")){
			 GravaPreferencias("CheckAberto", "true");
		 }
		 else{
			 GravaPreferencias("CheckAberto", "false");
		 }
		 
		 texto = "Olá <b>"+sharedPreferences.getString("nome_usuario", "nome_usuario")+"</b>,<br>esta é a sua lista de Atendimentos:";
		 saudacao.setText(Html.fromHtml(texto));
		 
		 /* TESTE DE INICIO DE SERVIÇO */
		 /*
		 iniciar_servico.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Toast.makeText(instaciaActivity, "Teste inicio", Toast.LENGTH_LONG).show();
				startService(new Intent("INICIAR_SERVICO"));
			}
		 });
		 
		 finalizar_servico.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Toast.makeText(instaciaActivity, "Teste fim", Toast.LENGTH_LONG).show();
				stopService(new Intent("INICIAR_SERVICO"));
			}
		 });
		  */
		 /* FIM DE TESTE DE INICIO DE SERVIÇO */
		 
		 // Botão de Atendimento
		 grupos_atendimentos.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) {
				  if(Integer.parseInt(sharedPreferences.getString("emAndamento", "emAndamento"))==0){
					  Toast.makeText(instaciaActivity, "Você não possui atendimentos para este status!", Toast.LENGTH_LONG).show();
				  }else{
					  Acessar("em_andamento");
				  }
			  }
			});
		 qtd_atendimento.setText(sharedPreferences.getString("emAndamento", "emAndamento"));
		 qtd_atendimento.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) {
				  if(Integer.parseInt(sharedPreferences.getString("emAndamento", "emAndamento"))==0){
					  Toast.makeText(instaciaActivity, "Você não possui atendimentos para este status!", Toast.LENGTH_LONG).show();
				  }else{
					  Acessar("em_andamento");
				  }
			  }
			});
		 // Fim Botão de Atendimento
		 
		 // Botão de Espera
		 grupos_espera.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) {
				  if(Integer.parseInt(sharedPreferences.getString("emEspera", "emEspera"))==0){
					  Toast.makeText(instaciaActivity, "Você não possui atendimentos para este status!", Toast.LENGTH_LONG).show();
				  }else{
					  GravaPreferencias("retirar","retiraEspera");
					  Acessar("em_espera");
				  }
			  }
			});
		 qtd_espera.setText(sharedPreferences.getString("emEspera", "emEspera"));
		 qtd_espera.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) {
				  if(Integer.parseInt(sharedPreferences.getString("emEspera", "emEspera"))==0){
					  Toast.makeText(instaciaActivity, "Você não possui atendimentos para este status!", Toast.LENGTH_LONG).show();
				  }else{		
					  GravaPreferencias("retirar","retiraEspera");
					  Acessar("em_espera");
				  }
			  }
			});

		 // Fim Botão de Espera
		 
		 // Botão de Atrasados
		 grupos_atrasados.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) {
				  if(Integer.parseInt(sharedPreferences.getString("emAtraso", "emAtraso"))==0){
					  Toast.makeText(instaciaActivity, "Você não possui atendimentos para este status!", Toast.LENGTH_LONG).show();
				  }else{				
					  GravaPreferencias("retirar","retiraAtrasados");
					  Acessar("em_atraso");
				  }
			  }
			});
		 qtd_atrasados.setText(sharedPreferences.getString("emAtraso", "emAtraso"));
		 qtd_atrasados.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) {
				  if(Integer.parseInt(sharedPreferences.getString("emAtraso", "emAtraso"))==0){
					  Toast.makeText(instaciaActivity, "Você não possui atendimentos para este status!", Toast.LENGTH_LONG).show();
				  }else{				  
					  GravaPreferencias("retirar","retiraAtrasados");
					  Acessar("em_atraso");
				  }
			  }
			});

		 // Fim Botão de Atrasados
		 
		 // Botão de Finalizados		 
		 grupos_finalizados.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) {
				  if(Integer.parseInt(sharedPreferences.getString("finalizados", "finalizados"))==0){
					  Toast.makeText(instaciaActivity, "Você não possui atendimentos para este status!", Toast.LENGTH_LONG).show();
				  }else{				  
					  Acessar("finalizado");
				  }
			 }
			});
		 qtd_finalizados.setText(sharedPreferences.getString("finalizados", "finalizados"));
		 qtd_finalizados.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) {
				  if(Integer.parseInt(sharedPreferences.getString("finalizados", "finalizados"))==0){
					  Toast.makeText(instaciaActivity, "Você não possui atendimentos para este status!", Toast.LENGTH_LONG).show();
				  }else{				  
					  Acessar("finalizado");
				  }
			  }
			});

		 // Fim Botão de Finalizados

		 // Botão de Atualizar
		 atualizar.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) {
				  Toast.makeText(instaciaActivity, "Aguarde, realizando a Sincronização. ", Toast.LENGTH_SHORT).show();
				  
					if(sharedPreferences.getString("salvarCheckIn", "salvarCheckIn").equals("true")){
						GravaPreferencias("salvarCheckIn", "false");
						GravaPreferencias("CheckAberto", "false");
						RealizarCheck(sharedPreferences.getString("latitudeIn", "latitudeIn"), sharedPreferences.getString("longitudeIn", "longitudeIn"), sharedPreferences.getString("dataHoraIn", "dataHoraIn"), sharedPreferences.getString("idAtendimentoIn", "idAtendimentoIn"), sharedPreferences.getString("tipoCheckIn", "tipoCheckIn"),"","", sharedPreferences.getString("idRelacionamento", "idRelacionamento"), sharedPreferences.getString("CheckInTitulo", "CheckInTitulo"));
						// LIMPANDO OS DADOS DE CHECKIN
					}

					if(sharedPreferences.getString("salvarCheckOut", "salvarCheckOut").equals("true")){
						GravaPreferencias("salvarCheckOut", "false");
						RealizarCheck(sharedPreferences.getString("latitudeOut", "latitudeOut"), sharedPreferences.getString("longitudeOut", "longitudeOut"), sharedPreferences.getString("dataHoraOut", "dataHoraOut"), sharedPreferences.getString("idAtendimentoOut", "idAtendimentoOut"), sharedPreferences.getString("tipoCheckOut", "tipoCheckOut"), sharedPreferences.getString("txtObservacaoOut", "txtObservacaoOut"), sharedPreferences.getString("txtStatusOut", "txtStatusOut"), sharedPreferences.getString("idRelacionamentoOut", "idRelacionamentoOut"), sharedPreferences.getString("CheckOutTitulo", "CheckOutTitulo"));
						// LIMPANDO OS DADOS DE CHECKOUT
					}
						ConsultaImeil();


			  }
			});

		 // Fim Botão de Atualizar
		 
		 
		 // Botão de Meu Local
		 // Ao clicar nesse botão o usuario salva na tabela de usuários a sua localização atual
		 meuLocal.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) {
				  
				  informarLocal();
				  
			  }
			});

		 // Fim Botão de Atualizar
		 
		 
		 //startService(new Intent("INICIAR_SERVICO"));
		
		 if(sharedPreferences.getString("notificacao_valida", "notificacao_valida").equals("true")){
			 if(Integer.parseInt(sharedPreferences.getString("notificacao_texto", "notificacao_texto"))>0){
				 criarNotificacao(getApplicationContext(), new MensagemAlerta("Alerta Veritime", "Existe "+sharedPreferences.getString("notificacao_texto", "notificacao_texto")+" novos atendimentos cadastrados para você.","Atendimentos"), Atendimentos.class);
			 }
		 }
		}
		catch(Exception e){
			
		}
		
	        
	}
	
	public void Acessar(String tipoAtendimento){
		
		Intent it =  new  Intent(instaciaActivity, Atendimentos.class);
			GravaPreferencias("exibir",tipoAtendimento);
	    	startActivity(it);


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
		
		//Intent it = new Intent("SERVICO");
		//PendingIntent p = PendingIntent.getService(context, 0, it, 0);
		
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
    		//progressDialog = ProgressDialog.show(instaciaActivity, "Loanding", "Realizando o login pelo Imei...");
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
 
    	}
    	
    	@Override
    	protected void onPostExecute(Integer result){
    		
    		if(result == 0){
    			Toast.makeText(instaciaActivity, "Não foi possivel realizar a sincronização", Toast.LENGTH_LONG).show();
    		}
    		else if(result == 1){
    			try{
    				
    				sharedPreferences = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
    				
    				String finalizados = "0";
    				String emAtraso = "0";
    				String emEspera = "0";
    				String emAndamento = "0";
 
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
	    					GravaPreferencias("CheckAberto", "true");
	    				}
    				
    				}
    				else{
    					GravaPreferencias("CheckAberto", "false");
    				
    				}
    				
    				
  				  qtd_finalizados.setText(finalizados);
  				  qtd_atrasados.setText(emAtraso);
  				  qtd_espera.setText(emEspera);
  				  qtd_atendimento.setText(emAndamento);
   				
    				
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
    				
    				if(sharedPreferences.getString("notificacao_valida", "notificacao_valida").equals("true")){
    					 if(Integer.parseInt(sharedPreferences.getString("notificacao_texto", "notificacao_texto"))>0){
    						 criarNotificacao(getApplicationContext(), new MensagemAlerta("Alerta Veritime", "Existe "+sharedPreferences.getString("notificacao_texto", "notificacao_texto")+" novos atendimentos cadastrados para você.","Atendimentos"), Atendimentos.class);
    					 }
    				 }
    				
    				Toast.makeText(instaciaActivity, "Sincronização realizada", Toast.LENGTH_LONG).show();
    			}
    			catch(Exception e){
    				
    			}
						      			
    		}
    	}
    	
    }

	protected void RealizarCheck(String latitude, String longitude, String horario, String idAtendimento, String check, String observacao, String status, String idrelacionamento, String tituloAtendimento){
		latitudeFinal = latitude;
		longitudeFinal = longitude;
		horarioFinal = horario;
		idAtendimentoFinal = idAtendimento;
		tipoCheckFinal = check;
		txtStatus = status;
		txtObservavao = observacao;
		idRelacionamento = idrelacionamento;
		titulo		 = tituloAtendimento;
				
		String urlCheckin = "http://www.veritime.com.br/admin/acesso/realizarCheck";
		ProcessoCheck processo3 = new ProcessoCheck(this);
       processo3.execute(urlCheckin);
		
	}
	
	public class ProcessoCheck extends AsyncTask <String, String, Integer>{
	    	//private ProgressDialog progress;
	    	private String strJson2;
	    	private HttpClient httpclient = null;
	    	
	    	//private ProgressDialog progressDialog;
	    	
	    	public ProcessoCheck(Context context){
	    	}
	    	
	    	@Override
	    	protected void onPreExecute(){
	    		//progressDialog = ProgressDialog.show(instaciaActivity, "Aguarde", "Realizando o seu Check-in...");
	    		
	    	}
	    	
	    	
	    	@Override
	    	protected Integer doInBackground(String... paramss){
	    		strJson2 = null;

	    		try{
	    			sharedPreferences = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
	    			URI urlService = new URI(paramss[0]);
	    				    			
	    			ArrayList<NameValuePair> parametrosPost = new ArrayList<NameValuePair>();  
	    			parametrosPost.add(new BasicNameValuePair("latitude", latitudeFinal));
	    			parametrosPost.add(new BasicNameValuePair("longitude", longitudeFinal));
	    			parametrosPost.add(new BasicNameValuePair("idAtendimento", idAtendimentoFinal));
	    			parametrosPost.add(new BasicNameValuePair("idUsuario", sharedPreferences.getString("id_usuario", "id_usuario")));
	    			parametrosPost.add(new BasicNameValuePair("dataHora", horarioFinal));
	    			parametrosPost.add(new BasicNameValuePair("tipoCheck", tipoCheckFinal));
	    			parametrosPost.add(new BasicNameValuePair("txtStatus", txtStatus));
	    			parametrosPost.add(new BasicNameValuePair("txtObservacao", txtObservavao));
	    			parametrosPost.add(new BasicNameValuePair("notif_email", sharedPreferences.getString("email", "email")));
	    			parametrosPost.add(new BasicNameValuePair("titulo", titulo));
	    			parametrosPost.add(new BasicNameValuePair("idRelacionamento",idRelacionamento ));
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
	    				
	    				strJson2 = builder.toString();
	    				
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
	    	
	    	protected void onPostExecute(Integer result){
	    	
	    		if(result == 0){
					  
					  ConsultaImeil();				   

	    			//Log.i("CHEKIN", "ERROR "+strJson2);
	    		}
	    		else if(result == 1){
					  
					  ConsultaImeil();				   

	    			//Log.i("CHEKIN", "REALIZADO "+strJson2);
	    		}
	    		//progressDialog.cancel();
	    	
		    	}
	    	
	    }

	protected void informarLocal(){
 		final LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 

		boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		
		// Se o GPS não tiver ativado ele faz a pergunta  se deseja ativar ou não se clicar em não ele utiliza a geolocalização obtido através da internet.
		if (!enabled) {
			  AlertDialog.Builder builder = new AlertDialog.Builder(this);
			    builder.setMessage("Para uma geolocalização mais precisa, ative o GPS em um local aberto.")
			           .setCancelable(false)
			           .setPositiveButton("Ativar", new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int id) {
		 
			        			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			        			startActivity(intent);
			        			Toast.makeText(instaciaActivity, "Aguarde enquanto a sua localização é encontrada. O GPS pode levar alguns minutos.", Toast.LENGTH_LONG).show();
			            		LocationListener locationListener = new LocationListener() {
			                	    public void onLocationChanged(Location location) {
						         	       Double latPoint = location.getLatitude();
							       		   Double lngPoint = location.getLongitude();
						         	 
						         	       MeuLocal (latPoint.toString(), lngPoint.toString());
			                	    }

			                	    public void onStatusChanged(String provider, int status, Bundle extras) {}

			                	    public void onProviderEnabled(String provider) {}

			                	    public void onProviderDisabled(String provider) {}
			                	  };

			                	  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 50, locationListener);			        			
			               }
			           })
			           .setNegativeButton("Não", new DialogInterface.OnClickListener() {
			               @SuppressWarnings("unused")
						public void onClick(DialogInterface dialog, int id) {

			          	       String bestProvider = locationManager.getBestProvider(new Criteria(),true); 
			        	       
			         	       Location location = locationManager.getLastKnownLocation(bestProvider); 
			         	       Double latPoint = location.getLatitude();
				       		   Double lngPoint = location.getLongitude();
			         	 
			         	       MeuLocal (latPoint.toString(), lngPoint.toString());
			         		
			         		   LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

			               }
			           });
			    AlertDialog alert = builder.create();
			    alert.show();

		}
		// Se já tiver habilitado o GPS ele pega a localização através dele
		else{
    	    Toast.makeText(instaciaActivity, "Aguarde enquanto a sua localização é encontrada. O GPS pode levar alguns minutos.", Toast.LENGTH_LONG).show();		
			LocationListener locationListener = new LocationListener() {
       	    public void onLocationChanged(Location location) {
         	       Double latPoint = location.getLatitude();
	       		   Double lngPoint = location.getLongitude();
         	 
         	       MeuLocal (latPoint.toString(), lngPoint.toString());
       			
       	    }

       	    public void onStatusChanged(String provider, int status, Bundle extras) {}

       	    public void onProviderEnabled(String provider) {}

       	    public void onProviderDisabled(String provider) {}
       	  };

       	  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 50, locationListener);
			
		}

	}
	protected void MeuLocal(String latitude, String longitude){
		latitudeUsuario = latitude;
		longitudeUsuario = longitude;
				
		String urlCheckin = "http://www.veritime.com.br/admin/acesso/meuLocal";
		ProcessoMeuLocal processoMeuLocal = new ProcessoMeuLocal(this);
		processoMeuLocal.execute(urlCheckin);
		
	}
	
	public class ProcessoMeuLocal extends AsyncTask <String, String, Integer>{
	    	//private ProgressDialog progress;
	    	private String strJson3;
	    	private HttpClient httpclient = null;
	    	
	    	//private ProgressDialog progressDialog;
	    	
	    	public ProcessoMeuLocal(Context context){
	    	}
	    	
	    	@Override
	    	protected void onPreExecute(){
	    		//progressDialog = ProgressDialog.show(instaciaActivity, "Aguarde", "Realizando o seu Check-in...");
	    		
	    	}
	    	
	    	
	    	@Override
	    	protected Integer doInBackground(String... paramss){
	    		strJson3 = null;

	    		try{
	    			sharedPreferences = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
	    			URI urlService = new URI(paramss[0]);
	    				    			
	    			ArrayList<NameValuePair> parametrosPost = new ArrayList<NameValuePair>();  
	    			parametrosPost.add(new BasicNameValuePair("latitude", latitudeUsuario));
	    			parametrosPost.add(new BasicNameValuePair("longitude", longitudeUsuario));
	    			parametrosPost.add(new BasicNameValuePair("idUsuario", sharedPreferences.getString("id_usuario", "id_usuario")));
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
	    				
	    				strJson3 = builder.toString();
	    				
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
	    	
	    	protected void onPostExecute(Integer result){
	    	
	    		if(result == 0){			   
	    			Toast.makeText(instaciaActivity, "Para atualizar seu local é preciso conexão com internet", Toast.LENGTH_LONG).show();
	    			//Log.i("MEULOCAL", "ERROR "+strJson3);
	    		}
	    		else if(result == 1){
	    			try{
		    			JSONObject json = new JSONObject(strJson3.substring(strJson3.indexOf('{')));
	    				JSONArray acesso = json.getJSONArray("login");
	    				JSONObject fun = new JSONObject(acesso.getString(0));
	    				
	    				
		    			Toast.makeText(instaciaActivity, fun.getString("mensagem"), Toast.LENGTH_LONG).show();
		    			//Log.i("MEULOCAL", "REALIZADO "+strJson3);
	    			}
	    			catch(Exception e){
	    				
	    			}
	    		}
	    		//progressDialog.cancel();
	    	
		    	}
	    	
	    }
	
	
	
    public class MensagemAlerta {
    	 
    	private CharSequence title;
    	private CharSequence body;
    	private CharSequence subTitle;
     
    	public MensagemAlerta(CharSequence title, CharSequence body,
    			CharSequence subTitle) {
     
    		this.title = title;
    		this.body = body;
    		this.subTitle = subTitle;
    	}
     
    	public CharSequence getTitle() {
    		return title;
    	}
     
    	public void setTitle(CharSequence title) {
    		this.title = title;
    	}
     
    	public CharSequence getBody() {
    		return body;
    	}
     
    	public void setBody(CharSequence body) {
    		this.body = body;
    	}
     
    	public CharSequence getSubTitle() {
    		return subTitle;
    	}
     
    	public void setSubTitle(CharSequence subTitle) {
    		this.subTitle = subTitle;
    	}
     
    }
    
	@SuppressWarnings("deprecation")
	protected void criarNotificacao(Context context, MensagemAlerta messagesAlerts, Class<?> activity) {
		GravaPreferencias("notificacao_valida","false");
 
		// Recupera o serviço do NotificationManager
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
 
		Notification notificaction = new Notification(R.drawable.ic_veritime,
				messagesAlerts.getTitle(), System.currentTimeMillis());
 
		notificaction.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "2");
		// notification.sound = Uri.parse("file:///sdcard/recording33490.3gpp");
 
		// Flag que vibra e emite um sinal sonoro até o usuário clicar na
		// notificação
		//notificaction.flags |= Notification.FLAG_INSISTENT;
 
		// Flag utilizada para remover a notificação da toolbar quando usuário
		// tiver clicado nela.
		notificaction.flags |= Notification.FLAG_AUTO_CANCEL;
 
		// PendingIntent para executar a Activity se o usuário selecionar a
		// notificão
		PendingIntent p = PendingIntent.getActivity(this, 0,
				new Intent(this.getApplicationContext(), MainActivity.class), 0);
 
		// Informações
		notificaction.setLatestEventInfo(this, messagesAlerts.getSubTitle(),
				messagesAlerts.getBody(), p);
 
		// espera 100ms e vibra por 1000ms, depois espera por 1000 ms e vibra
		// por
		// 1000ms.
		notificaction.vibrate = new long[] { 100, 500, 100, 500, 100, 500 };
 
		// id que identifica esta notifição
		notificationManager.notify(R.string.app_name, notificaction);	
	}
    public void onBackPressed(){
    	Log.i("Serviço", "Inicia o serviço onBack()");
 	   	startService(new Intent("INICIAR_SERVICO"));
    	System.exit(0);
	   //super.onDestroy();
	   //finish();
    	//android.os.Process.killProcess(android.os.Process.myPid());
    	//android.os.Process.killProcess(android.os.Process.myPid());
    	//NÃO IMPLEMENTADO NADA  PARA FORÇAR A SAÍDA PELO BOTÃO DO APARELHO E NÃO DEIXAR DAR TELA BRANCA.
	}
    
   
    public void onDestroy(){
    	Log.i("Serviço", "Inicia o serviço");
    	startService(new Intent("INICIAR_SERVICO"));
    	super.onDestroy();
    }
    

	public boolean onCreateOptionsMenu(Menu menu) {
	
	    super.onCreateOptionsMenu(menu);
	
	
	
	    menu.add(Menu.NONE, 0, 0, "Sair");
	
	    //menu.add(Menu.NONE, 1, 1, "Settings");
	
	    //menu.add(Menu.NONE, 2, 2, "Other");
	
	    return true;
	
	}
	
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {

        case 0:

            finish();

        }

        return false;

    }
	
    
}


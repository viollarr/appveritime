package br.com.veritime2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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

//import android.telephony.SmsManager; 
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Detalhes  extends Activity  {
	private Button btnGps;
	private ImageView voltar;
	private Button rota;
	private TextView txtTitulo;
	private TextView txtEmpresa;
	private TextView txtEndereco;
	private TextView txtCidade;
	private TextView txtDataHora;
	private TextView txtTempoEstimado;  
	private TextView txtDescricao;
	private TableRow tableRow7;
	private Boolean tipoTexto;
	private String enderecoAtendimento;
	private String enderecoNumeroAtendimento;
	private String enderecoComplementoAtendimento;
	private String enderecoBairroAtendimento;
	private String cidadeAtendimento;
	private String dataHoraCheckIn;
	private String dataHoraCheckOut;
	private String temContraSenha;
	private String contraSenha;
	private String latitudeAtendimento;
	private String longitudeAtendimento;
	private String latitudeFinal;
	private String longitudeFinal;
	private String horarioFinal;
	private String idAtendimentoFinal;
	private String idUsuarioFinal;
	private String tipoCheckFinal;
	private String titulo;
	private String txtStatus;
	private String txtObservavao;
	private String empresa;
	private String idAtendimento;
	private String telefoneCriador;
	private String txtBotaoStatus;
	private String idRelacionamento;
	private Activity instaciaActivity = Detalhes.this;
	private SharedPreferences sharedPreferences;
	private final String PREFS_PRIVATE = "PREFS_PRIVATE";
	private JSONArray grupos;
	String finalizados = "0";
	String emAtraso = "0";
	String emEspera = "0";
	String emAndamento = "0";
	int retirarBotao;

	
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalhes_atendimento);
		sharedPreferences = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
		voltar = (ImageView) findViewById(R.id.imageView1);
		rota = (Button) findViewById(R.id.rota);
		
		// contadores dos grupos de atendimentos
		finalizados = sharedPreferences.getString("finalizados", "finalizados");
		emAtraso = sharedPreferences.getString("emAtraso", "emAtraso");
		emEspera = sharedPreferences.getString("emEspera", "emEspera");
		emAndamento = sharedPreferences.getString("emAndamento", "emAndamento");

		
	 	btnGps = (Button) findViewById(R.id.btnGps);
		txtTitulo = (TextView) findViewById(R.id.txtTitulo);
		txtEmpresa = (TextView) findViewById(R.id.txtEmpresa);
		txtEndereco = (TextView) findViewById(R.id.txtEndereco);
		txtCidade = (TextView) findViewById(R.id.txtCidade);
		txtDataHora = (TextView) findViewById(R.id.txtDataHora);
		txtTempoEstimado = (TextView) findViewById(R.id.txtTempoEstimado);
		txtDescricao = (TextView) findViewById(R.id.txtDescricao);
		tableRow7 = (TableRow) findViewById(R.id.tableRow7);
		
		
		Intent i = getIntent();
		txtTitulo.setText(i.getStringExtra("titulo"));
		txtEmpresa.setText(i.getStringExtra("empresa"));
		txtEndereco.setText(i.getStringExtra("endereco")+", "+i.getStringExtra("enderecoNumero")+" "+i.getStringExtra("enderecoComplemento")+", "+i.getStringExtra("enderecoBairro"));
		txtCidade.setText(i.getStringExtra("cidade"));
		latitudeAtendimento = i.getStringExtra("latitudeAtendimento");
		longitudeAtendimento = i.getStringExtra("longitudeAtendimento");
		txtDataHora.setText(i.getStringExtra("dataHora"));
		txtTempoEstimado.setText(i.getStringExtra("tempoEstimado"));
		if(i.getStringExtra("descricao").length() > 0){
			tableRow7.setVisibility(View.VISIBLE);
			txtDescricao.setVisibility(View.VISIBLE);
			txtDescricao.setText(i.getStringExtra("descricao"));
		}
		else{
			tableRow7.setVisibility(View.INVISIBLE);
			txtDescricao.setVisibility(View.INVISIBLE);	
		}
		dataHoraCheckIn 				= i.getStringExtra("data_hora_checkin");
		dataHoraCheckOut 				= i.getStringExtra("data_hora_checkout");
		temContraSenha 					= i.getStringExtra("temContraSenha");
		contraSenha 					= i.getStringExtra("contraSenha");
		idAtendimento 					= i.getStringExtra("idAtendimento");
		idUsuarioFinal 					= i.getStringExtra("id_usuario");
		enderecoAtendimento				= i.getStringExtra("endereco");
		enderecoNumeroAtendimento		= i.getStringExtra("enderecoNumero");
		enderecoComplementoAtendimento	= i.getStringExtra("enderecoComplemento");
		enderecoBairroAtendimento		= i.getStringExtra("enderecoBairro");
		cidadeAtendimento				= i.getStringExtra("cidade");
		telefoneCriador					= i.getStringExtra("telefoneCriador");
		txtBotaoStatus					= i.getStringExtra("status");
		idRelacionamento				= i.getStringExtra("idRelacionamento");		
		titulo 							= i.getStringExtra("titulo");
		empresa 						= i.getStringExtra("empresa");
		
		//retirar item do contador de grupos se houver
		retirarBotao 					= i.getIntExtra("retirarBotao", 999);
		
		
		voltar.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) {
				  Intent it =  new  Intent(instaciaActivity, GruposAtendimentos.class);
				  finish();
				  startActivity(it);
			  }
		});
		
		rota.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) {
		        	//String partida 	= sharedPreferences.getString("latitudeFinal", "latitudeFinal")+", "+sharedPreferences.getString("longitudeFinal", "longitudeFinal");
		        	//String destino 	= latitudeAtendimento+", "+longitudeAtendimento;
		        	//String url		= "http://maps.google.com/maps?f=d&saddr="+partida+"&daddr="+destino+"&hl=pt";
		        	String url		= "geo:0,0?q="+enderecoAtendimento+","+enderecoNumeroAtendimento+" - "+enderecoBairroAtendimento+","+cidadeAtendimento;
		            Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		            startActivity(it);
			  }
		});
		
		
		Button backButton = (Button)this.findViewById(R.id.btnVoltar);
		backButton.setOnClickListener(new OnClickListener() {
		  @Override
		  public void onClick(View v) {
			Intent it =  new  Intent(instaciaActivity, Atendimentos.class);
			finish();
			startActivity(it);
		  }
		});
		
		
		if(sharedPreferences.getString("salvarCheckIn", "salvarCheckIn").equals("true")){
			if((sharedPreferences.getString("salvarCheckOut", "salvarCheckOut").equals("true"))&&( sharedPreferences.getString("idAtendimentoIn", "idAtendimentoIn").equals(idAtendimento))&&( sharedPreferences.getString("idAtendimentoOut", "idAtendimentoOut").equals(idAtendimento))){
				btnGps.setText("Atendimento Finalizado");
				GravaPreferencias("CheckAberto", "false");
				btnGps.setEnabled(false);
			}		
			else if(((sharedPreferences.getString("idAtendimentoIn", "idAtendimentoIn").equals(idAtendimento))||( sharedPreferences.getString("idAtendimentoOut", "idAtendimentoOut").equals(idAtendimento)))){
				btnGps.setText("Check-Out");
				tipoTexto = false;
			}
			else{
				btnGps.setText("Check-In");
				tipoTexto = true;
			}
		}
		else{
			if((dataHoraCheckIn.length() == 4)||(txtBotaoStatus.equals("em_atraso"))||(txtBotaoStatus.equals("em_espera"))){
				btnGps.setText("Check-In");
				tipoTexto = true;
			}
			else if((dataHoraCheckOut.length() == 4)||(txtBotaoStatus.equals("em_andamento"))){
				btnGps.setText("Check-Out");
				tipoTexto = false;
			}
			else{
				btnGps.setText("Atendimento Finalizado");
				btnGps.setEnabled(false);
			}		

		}
		btnGps.setOnClickListener(new Button.OnClickListener() {
	        public void onClick(View v){
	        	IniciarServico(idAtendimento, tipoTexto, idRelacionamento);
	        }
	    });
	}
	
    @SuppressLint("SimpleDateFormat")
	protected void IniciarServico(String idAtendimento, Boolean tipoTexto, String idRelacionamento) {
   	final String atendimento = idAtendimento;
   	final String relacionamento = idRelacionamento;
   	final String check ;
   	if(tipoTexto){
   		check = "1";
   	}
   	else{
   		check = "0";
   	}
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
			                	      Atualizar(location, atendimento, check, relacionamento);
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
			         	 
			         	       Atualizar(location, atendimento, check, relacionamento);
			         		
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
       	      Atualizar(location, atendimento, check, relacionamento);
       	    }

       	    public void onStatusChanged(String provider, int status, Bundle extras) {}

       	    public void onProviderEnabled(String provider) {}

       	    public void onProviderDisabled(String provider) {}
       	  };

       	  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 50, locationListener);
			
		}
		   
		
	}

	@SuppressLint("SimpleDateFormat")
	protected void Atualizar(Location location, String idAtendimento, String check, String idRelacionamento) {
		Double latPoint = location.getLatitude();
		Double lngPoint = location.getLongitude(); 	
		Date dataHora = new Date();
		int novoStatusEspera;
		int novoStatusAtraso;
		int novoStatusAndamento;

		if(check == "1"){
			RealizarCheck(latPoint.toString(), lngPoint.toString(), String.valueOf(new SimpleDateFormat("yyyy-MM-dd H:mm:ss").format(dataHora)), idAtendimento, check, "","", idRelacionamento, titulo);
			
			
			if(sharedPreferences.getString("retirar", "retirar").equals("retiraEspera")){
				novoStatusEspera = Integer.parseInt(emEspera)-1;
				novoStatusAndamento = Integer.parseInt(emAndamento)+1;
				emEspera = ""+novoStatusEspera;
				emAndamento = ""+novoStatusAndamento;
				
				JSONArray novo;
				try {
					novo = new JSONArray(sharedPreferences.getString("Espera", "Espera"));
					String NovoArrayEspera = "[";
					String NovoArrayAndamento = "[";
					 
					for(int x = 0; x < novo.length(); x++){
						if(x != retirarBotao ){
							 
							NovoArrayEspera += novo.getString(x)+",";
							 
						}
						else{
							
							NovoArrayAndamento += novo.getString(x)+",";
						}
					}
					
					NovoArrayEspera =NovoArrayEspera.substring(0, NovoArrayEspera.length()-1);
					NovoArrayEspera += "]";
					
					NovoArrayAndamento =NovoArrayAndamento.substring(0, NovoArrayAndamento.length()-1);
					NovoArrayAndamento += "]";
					
					
					GravaPreferencias("Espera",NovoArrayEspera);
					GravaPreferencias("Andamento",NovoArrayAndamento);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
	
	 
				GravaPreferencias("emEspera",emEspera);
				GravaPreferencias("emAndamento",emAndamento);
			
			}
			else{
				novoStatusAtraso = Integer.parseInt(emAtraso)-1;
				novoStatusAndamento = Integer.parseInt(emAndamento)+1;
				emAtraso = ""+novoStatusAtraso;
				emAndamento = ""+novoStatusAndamento;
				
				JSONArray novo;
				try {
					novo = new JSONArray(sharedPreferences.getString("Atraso", "Atraso"));
					String NovoArrayAtraso = "[";
					String NovoArrayAndamento = "[";
					 
					for(int x = 0; x < novo.length(); x++){
						if(x != retirarBotao ){
							 
							NovoArrayAtraso += novo.getString(x)+",";
							 
						}
						else{
							
							NovoArrayAndamento += novo.getString(x)+",";
						}
					}
					
					NovoArrayAtraso =NovoArrayAtraso.substring(0, NovoArrayAtraso.length()-1);
					NovoArrayAtraso += "]";
					
					NovoArrayAndamento =NovoArrayAndamento.substring(0, NovoArrayAndamento.length()-1);
					NovoArrayAndamento += "]";
					
					
					GravaPreferencias("Atraso",NovoArrayAtraso);
					GravaPreferencias("Andamento",NovoArrayAndamento);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
	
	 
				GravaPreferencias("emAtraso",emAtraso);
				GravaPreferencias("emAndamento",emAndamento);
				
			
			}

		}
		else{
			Intent it =  new  Intent(instaciaActivity, CheckOut.class);
			
			it.putExtra("latitude", latPoint.toString());
			it.putExtra("longitude", lngPoint.toString());				
			it.putExtra("dataHora",  String.valueOf(new SimpleDateFormat("yyyy-MM-dd H:mm:ss").format(dataHora)));
			it.putExtra("idAtendimento", idAtendimento);
			it.putExtra("tipoCheck", check);
			it.putExtra("temContraSenha", temContraSenha);
			it.putExtra("contraSenha", contraSenha);
			it.putExtra("idUsuario", idUsuarioFinal);
			it.putExtra("titulo", titulo);
			it.putExtra("empresa", empresa);
			it.putExtra("retirarBotao",retirarBotao);
			it.putExtra("telefoneCriador", telefoneCriador);
			it.putExtra("idRelacionamento", idRelacionamento);
			startActivity(it);
		}
		
	}
	
	protected void RealizarCheck(String latitude, String longitude, String horario, String idAtendimento, String check, String observacao, String status, String idRelacionamento, String tituloAtendimento){
		latitudeFinal = latitude;
		longitudeFinal = longitude;
		horarioFinal = horario;
		idAtendimentoFinal = idAtendimento;
		tipoCheckFinal = check;
		txtStatus = status;
		txtObservavao = observacao;
		
		if(sharedPreferences.getString("salvarCheckIn", "salvarCheckIn") == "true"){
			GravaPreferencias("salvarCheckIn", "false");
			RealizarCheckCache(sharedPreferences.getString("latitudeIn", "latitudeIn"), sharedPreferences.getString("longitudeIn", "longitudeIn"), sharedPreferences.getString("dataHoraIn", "dataHoraIn"), sharedPreferences.getString("idAtendimentoIn", "idAtendimentoIn"), sharedPreferences.getString("tipoCheckIn", "tipoCheckIn"),"","", sharedPreferences.getString("idRelacionamento", "idRelacionamento"), sharedPreferences.getString("CheckInTitulo", "CheckInTitulo"));
			// LIMPANDO OS DADOS DE CHECKIN
		}

		if(sharedPreferences.getString("salvarCheckOut", "salvarCheckOut") == "true"){
			GravaPreferencias("salvarCheckOut", "false");
			RealizarCheckCache(sharedPreferences.getString("latitudeOut", "latitudeOut"), sharedPreferences.getString("longitudeOut", "longitudeOut"), sharedPreferences.getString("dataHoraOut", "dataHoraOut"), sharedPreferences.getString("idAtendimentoOut", "idAtendimentoOut"), sharedPreferences.getString("tipoCheckOut", "tipoCheckOut"), sharedPreferences.getString("txtObservacaoOut", "txtObservacaoOut"), sharedPreferences.getString("txtStatusOut", "txtStatusOut"), sharedPreferences.getString("idRelacionamentoOut", "idRelacionamentoOut"), sharedPreferences.getString("CheckOutTitulo", "CheckOutTitulo"));
			// LIMPANDO OS DADOS DE CHECKOUT
		}
		

				
		String urlCheckin = "http://www.veritime.com.br/admin/acesso/realizarCheck";
		ProcessoCheck processo3 = new ProcessoCheck(this);
       processo3.execute(urlCheckin);
		
	}
	
	public class ProcessoCheck extends AsyncTask <String, String, Integer>{
	    	private ProgressDialog progress;
	    	private String strJson;
	    	private HttpClient httpclient = null;
	    	
	    	private ProgressDialog progressDialog;
	    	
	    	public ProcessoCheck(Context context){
	    	}
	    	
	    	@Override
	    	protected void onPreExecute(){
	    		progressDialog = ProgressDialog.show(instaciaActivity, "Aguarde", "Realizando o seu Check-in...");
	    	}
	    	
	    	
	    	@Override
	    	protected Integer doInBackground(String... paramss){
	    		strJson = null;

	    		try{
	    			sharedPreferences = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
	    			URI urlService = new URI(paramss[0]);
	    				    			
	    			ArrayList<NameValuePair> parametrosPost = new ArrayList<NameValuePair>();  
	    			parametrosPost.add(new BasicNameValuePair("latitude", latitudeFinal));
	    			parametrosPost.add(new BasicNameValuePair("longitude", longitudeFinal));
	    			parametrosPost.add(new BasicNameValuePair("idAtendimento", idAtendimentoFinal));
	    			parametrosPost.add(new BasicNameValuePair("idUsuario", idUsuarioFinal));
	    			parametrosPost.add(new BasicNameValuePair("dataHora", horarioFinal));
	    			parametrosPost.add(new BasicNameValuePair("tipoCheck", tipoCheckFinal));
	    			parametrosPost.add(new BasicNameValuePair("txtStatus", txtStatus));
	    			parametrosPost.add(new BasicNameValuePair("txtObservacao", txtObservavao));
	    			parametrosPost.add(new BasicNameValuePair("notif_email", sharedPreferences.getString("email", "email")));
	    			parametrosPost.add(new BasicNameValuePair("titulo",titulo ));
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
	    		progress.setMessage(values[0]);
	    	}
	    	
	    	protected void onPostExecute(Integer result){

	    		btnGps = (Button) findViewById(R.id.btnGps);
	    		progressDialog.cancel();
	    		
	    		
	    		if(result==0){
	    			try{
	    				
	    				Log.i("CHEKIN", "ERROR "+strJson);
					//TRATA ERRO NA REDE (Erro 1.:)-------------------------------------------------------------
		    			GravaPreferencias("latitudeIn", latitudeFinal);
		    			GravaPreferencias("longitudeIn", longitudeFinal);
		    			GravaPreferencias("idAtendimentoIn", idAtendimentoFinal);
		    			GravaPreferencias("idUsuarioIn", idUsuarioFinal);
		    			GravaPreferencias("dataHoraIn", horarioFinal);
		    			GravaPreferencias("tipoCheckIn", tipoCheckFinal);
		    			GravaPreferencias("idRelacionamento", idRelacionamento);
		    			GravaPreferencias("salvarCheckIn", "true");
		    			
		    			
		    			
		    			Toast.makeText(instaciaActivity, "Problema encontrado na rede, validação será feita em um outro momento de forma automática.", Toast.LENGTH_SHORT).show();
		    			
						GravaPreferencias("CheckAberto", "true");
    					GravaPreferencias("CheckInTitulo", titulo);
    					GravaPreferencias("CheckTitulo", titulo);
    					GravaPreferencias("CheckEmpresa", empresa);
    					GravaPreferencias("CheckId", idAtendimento);
    					
    					Intent it =  new  Intent(instaciaActivity, GruposAtendimentos.class);
	
						
						if(sharedPreferences.getString("sms", "sms").equals("sim")){
							SmsManager smsManager = SmsManager.getDefault();
							smsManager.sendTextMessage(telefoneCriador, null, "Check-In realizado para o atendimento: "+titulo, null, null);
						}
						
						finish();
						startActivity(it);
    					
		    			
	    			}
	    			catch(Exception e){
	    				
	    			}
	    			
					//-----------------------------------------------------------------------------------------    			
	    		}else if(result == 1){
	    			try{
	    				
	    				Log.i("CHEKIN", "correto "+strJson);
	    				JSONObject json = new JSONObject(strJson.substring(strJson.indexOf('{')));
	    				JSONArray acesso = json.getJSONArray("login");
	    				JSONObject fun = new JSONObject(acesso.getString(0));
	    				
    					Toast.makeText(instaciaActivity, fun.getString("tipoCheck")+" realizado com sucesso!", Toast.LENGTH_LONG).show();
    					//txtCheck.setText(fun.getString("tipoCheck")+" realizado com sucesso.");
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
	    				
	    				
	    				GravaPreferencias("finalizados",finalizados);
	    				GravaPreferencias("emEspera",emEspera);
	    				GravaPreferencias("emAndamento",emAndamento);
	    				GravaPreferencias("emAtraso",emAtraso);						
 	    				
						GravaPreferencias("Andamento", fun.getString("emAndamento"));
						GravaPreferencias("Espera", fun.getString("emEspera"));
						GravaPreferencias("Atraso", fun.getString("emAtraso"));
						GravaPreferencias("Finalizado", fun.getString("finalizado"));
						
    					GravaPreferencias("CheckAberto", "true");
    					GravaPreferencias("CheckInTitulo", titulo);
    					GravaPreferencias("CheckTitulo", titulo);
    					GravaPreferencias("CheckEmpresa", empresa);
    					GravaPreferencias("CheckId", idAtendimento);
    					GravaPreferencias("notificacao_valida","true");
    					
  					  					
    					Intent it =  new  Intent(instaciaActivity, GruposAtendimentos.class);
						
    					GravaPreferencias("strJson",strJson);		
						
						finish();				
						startActivity(it);	 
						
							
	        		}catch(Exception e){
	    				//Log.d(DEBUG, "Erro 2.: "+getString(R.string.incorrect_cod)+" "+e);
	    				//saidaTextView.setText(getString(R.string.incorrect_cod));
	    			}
		      			
	    		}
	    	}
	    	
	    }

	
	
	protected void RealizarCheckCache(String latitude, String longitude, String horario, String idAtendimento, String check, String observacao, String status, String idrelacionamento, String tituloAtendimento){
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
	
	public class ProcessoCheckCache extends AsyncTask <String, String, Integer>{
	    	//private ProgressDialog progress;
	    	private String strJson2;
	    	private HttpClient httpclient = null;
	    	
	    	//private ProgressDialog progressDialog;
	    	
	    	public ProcessoCheckCache(Context context){
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
	    	
	    	
		    }
	    	
	    }
	
	
	public void GravaPreferencias( String nomeCampo, String conteudoCampo) {
		 
		sharedPreferences = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
		 
		Editor prefsPrivateEditor = sharedPreferences.edit();
		
		prefsPrivateEditor.putString(nomeCampo, conteudoCampo);
		prefsPrivateEditor.commit();
		 
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		
	    super.onCreateOptionsMenu(menu);
	
	
	
	    menu.add(Menu.NONE, 0, 0, "Visualizar Rota");
	
	    //menu.add(Menu.NONE, 1, 1, "Settings");
	
	    //menu.add(Menu.NONE, 2, 2, "Other");
	
	    return true;
	
	}
	
    public boolean onOptionsItemSelected(MenuItem item){
    	sharedPreferences = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
        switch (item.getItemId()) {

        case 0:
        	//String partida 	= sharedPreferences.getString("latitudeFinal", "latitudeFinal")+", "+sharedPreferences.getString("longitudeFinal", "longitudeFinal");
        	//String destino 	= latitudeAtendimento+", "+longitudeAtendimento;
        	//String url		= "http://maps.google.com/maps?f=d&saddr="+partida+"&daddr="+destino+"&hl=pt";
        	String url		= "geo:0,0?q="+enderecoAtendimento+","+enderecoNumeroAtendimento+" - "+enderecoBairroAtendimento+","+cidadeAtendimento;
            Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(it);

        }

        return false;

    }


}

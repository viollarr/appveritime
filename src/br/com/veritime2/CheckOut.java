package br.com.veritime2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

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


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class CheckOut extends Activity {
	private	String		temContraSenha;
	private	String		contraSenha;
	private	String		latitudeFinal;
	private	String		longitudeFinal;
	private	String		horarioFinal;
	private	String		idAtendimentoFinal;
	private	String		idUsuarioFinal;
	private	String		tipoCheckFinal;
	private	String		observacaoFinal;
	private	String		txtStatus;
	private	EditText 	txtContraSenha;
	private	EditText 	txtObservacao;
	private	Spinner 	combo;
	private	Button 		validar;
	private ImageView 	voltar;
	private String 		titulo;
	private String		telefoneCriador;
	private String 		idRelacionamento;
	private String 		txtObservacaoCahce;
	String 		empresa;
	private JSONArray 	grupos;
	private Activity 	instaciaActivity = CheckOut.this;
	private SharedPreferences sharedPreferences;
	private final String PREFS_PRIVATE = "PREFS_PRIVATE";
	String finalizados = "0";
	String emAtraso = "0";
	String emEspera = "0";
	String emAndamento = "0";
	int retirarBotao;
	int novoStatusFinalizado;
	int novoStatusAndamento;
	private static final String[] status = new String[]{"concluído","não concluído"}; 

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkout); 
		sharedPreferences = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
		validar = (Button) findViewById(R.id.validar);
		txtContraSenha = (EditText) findViewById(R.id.txtContraSenha);
		txtObservacao = (EditText) findViewById(R.id.txtObservacao);
		
		
		finalizados = sharedPreferences.getString("finalizados", "finalizados");
		emAtraso = sharedPreferences.getString("emAtraso", "emAtraso");
		emEspera = sharedPreferences.getString("emEspera", "emEspera");
		emAndamento = sharedPreferences.getString("emAndamento", "emAndamento");
		
		Button backButton = (Button)this.findViewById(R.id.btnVoltar);
		backButton.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) {
			    finish();
			  }
			});
		voltar = (ImageView)this.findViewById(R.id.imageView1);
		voltar.setOnClickListener(new OnClickListener() {
		  @Override
		  public void onClick(View v) {
			  Intent it =  new  Intent(instaciaActivity, GruposAtendimentos.class);
			  finish();
			  startActivity(it);
		  }
		});

        combo = (Spinner) findViewById(R.id.status);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        combo.setAdapter(adp);
		
		Intent i = getIntent();
		 temContraSenha		= i.getStringExtra("temContraSenha");
		 contraSenha		= i.getStringExtra("contraSenha");
		 latitudeFinal		= i.getStringExtra("latitude");
		 longitudeFinal		= i.getStringExtra("longitude");
		 horarioFinal		= i.getStringExtra("dataHora");
		 idAtendimentoFinal	= i.getStringExtra("idAtendimento");
		 idUsuarioFinal		= i.getStringExtra("idUsuario");
		 tipoCheckFinal		= i.getStringExtra("tipoCheck");
		 titulo 			= i.getStringExtra("titulo");
		 empresa 			= i.getStringExtra("empresa");
		 telefoneCriador	= i.getStringExtra("telefoneCriador");
		 idRelacionamento	= i.getStringExtra("idRelacionamento");
		 retirarBotao 		= i.getIntExtra("retirarBotao", 999);
		 
		 
		 if(sharedPreferences.getString("salvarCheckOut", "salvarCheckOut") == "true"){
			// validar.setEnabled(false);
		 }
		 
		 validar.setOnClickListener(new Button.OnClickListener() {
		        public void onClick(View v){
		        	if((temContraSenha.equals("nao"))||((temContraSenha.equals("sim"))&&(contraSenha.equals(txtContraSenha.getText().toString())))){
						
							if(combo.getSelectedItemPosition() == 0){
								txtStatus = "finalizado";
							}						
							else if(combo.getSelectedItemPosition() == 1){
								txtStatus = "nao_concluido";
				        	}				
				        	else{
								txtStatus = "nao_concluido";
							}
							
							//GravaPreferencias("CheckAberto", "false");
							RealizarCheck(latitudeFinal, longitudeFinal, horarioFinal, idAtendimentoFinal, tipoCheckFinal, txtObservacao.getText().toString(), txtStatus, idRelacionamento, titulo);
							novoStatusAndamento = Integer.parseInt(emAndamento)-1;
							emAndamento = ""+novoStatusAndamento;
							GravaPreferencias("emAndamento",emAndamento);
							
							if(txtStatus.equals("finalizado")){
								novoStatusFinalizado = Integer.parseInt(finalizados)+1;
								finalizados = ""+novoStatusFinalizado;
								GravaPreferencias("finalizados",finalizados);
							}
							
							
							JSONArray novo;
							try {
								novo = new JSONArray(sharedPreferences.getString("Finalizado", "Finalizado"));
								String NovoArrayFinalizado;
								
								
								if(novo.length()>0){
									
									NovoArrayFinalizado = sharedPreferences.getString("Finalizado", "Finalizado");
									NovoArrayFinalizado = NovoArrayFinalizado.substring(0, NovoArrayFinalizado.length()-1);
									NovoArrayFinalizado += ","+sharedPreferences.getString("Andamento", "Andamento").substring(1, sharedPreferences.getString("Andamento", "Andamento").length());
									
								}
								else{
									 NovoArrayFinalizado = sharedPreferences.getString("Andamento", "Andamento");
								}
								
								
								if(txtStatus.equals("finalizado")){
									GravaPreferencias("Finalizado",NovoArrayFinalizado);
								}
								
								GravaPreferencias("Andamento","[]");
								
							} catch (JSONException e) {
								
								e.printStackTrace();
							}
		        	}
			        else{
				    	AlertDialog.Builder errorAlertDialog = new AlertDialog.Builder(instaciaActivity);
						errorAlertDialog.setTitle("Atenção");
						errorAlertDialog.setMessage("Assinatura inválida");
						errorAlertDialog.setNeutralButton("OK", null);
						errorAlertDialog.show();
			        }
		        }
		    });		
		
	}
	
	protected void RealizarCheck(String latitude, String longitude, String horario, String idAtendimento, String check, String observacao, String Status, String idRelacionamento, String tituloAtendimento){
		latitudeFinal = latitude;
		longitudeFinal = longitude;
		horarioFinal = horario;
		idAtendimentoFinal = idAtendimento;
		tipoCheckFinal = check;
		observacaoFinal = observacao;
		
		if(sharedPreferences.getString("salvarCheckIn", "salvarCheckIn") == "true"){
			GravaPreferencias("salvarCheckIn", "false");
			RealizarCheck(sharedPreferences.getString("latitudeIn", "latitudeIn"), sharedPreferences.getString("longitudeIn", "longitudeIn"), sharedPreferences.getString("dataHoraIn", "dataHoraIn"), sharedPreferences.getString("idAtendimentoIn", "idAtendimentoIn"), sharedPreferences.getString("tipoCheckIn", "tipoCheckIn"),"","", sharedPreferences.getString("idRelacionamento", "idRelacionamento"), sharedPreferences.getString("CheckInTitulo", "CheckInTitulo"));			    					
			// LIMPANDO OS DADOS DE CHECKIN
		}
		
		
		if(sharedPreferences.getString("salvarCheckOut", "salvarCheckOut") == "true"){
			GravaPreferencias("salvarCheckOut", "false");
			RealizarCheck(sharedPreferences.getString("latitudeOut", "latitudeOut"), sharedPreferences.getString("longitudeOut", "longitudeOut"), sharedPreferences.getString("dataHoraOut", "dataHoraOut"), sharedPreferences.getString("idAtendimentoOut", "idAtendimentoOut"), sharedPreferences.getString("tipoCheckOut", "tipoCheckOut"), sharedPreferences.getString("txtObservacaoOut", "txtObservacaoOut"), sharedPreferences.getString("txtStatusOut", "txtStatusOut"), sharedPreferences.getString("idRelacionamentoOut", "idRelacionamentoOut"), sharedPreferences.getString("CheckOutTitulo", "CheckOutTitulo"));			    					
			// LIMPANDO OS DADOS DE CHECKIN
		}
				
		String urlCheckin = "http://www.veritime.com.br/admin/acesso/realizarCheck";
		ProcessoCheck processo3 = new ProcessoCheck(this);
       processo3.execute(urlCheckin);
		
	}
	
	public class ProcessoCheck extends AsyncTask <String, String, Integer>{
    	private ProgressDialog progress;
    	private String strJson;
    	private HttpClient httpclient = null;
    	ProgressDialog progressDialog;
    	
    	public ProcessoCheck(Context context){
    	}
    	
    	@Override
    	protected void onPreExecute(){
    		progressDialog = ProgressDialog.show(instaciaActivity, "Aguarde", "Validando o seu Check-out...");
    	}
    	
    	
    	@Override
    	protected Integer doInBackground(String... paramss){
    		strJson = null;

    		try{
    			URI urlService = new URI(paramss[0]);
    				    			
    			ArrayList<NameValuePair> parametrosPost = new ArrayList<NameValuePair>();  
    			parametrosPost.add(new BasicNameValuePair("latitude", latitudeFinal));
    			parametrosPost.add(new BasicNameValuePair("longitude", longitudeFinal));
    			parametrosPost.add(new BasicNameValuePair("idAtendimento", idAtendimentoFinal));
    			parametrosPost.add(new BasicNameValuePair("idUsuario", idUsuarioFinal));
    			parametrosPost.add(new BasicNameValuePair("dataHora", horarioFinal));
    			parametrosPost.add(new BasicNameValuePair("tipoCheck", tipoCheckFinal));
    			parametrosPost.add(new BasicNameValuePair("txtStatus", txtStatus));
    			parametrosPost.add(new BasicNameValuePair("txtObservacao", observacaoFinal));
    			parametrosPost.add(new BasicNameValuePair("notif_email", sharedPreferences.getString("email", "email")));
    			parametrosPost.add(new BasicNameValuePair("titulo",titulo ));
    			parametrosPost.add(new BasicNameValuePair("idRelacionamento",idRelacionamento ));
    			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parametrosPost);
    			
    			
    			
				HttpPost http = new HttpPost(urlService);
    			http.setEntity(formEntity);
    			
				

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
    			//Log.d(DEBUG, "Erro 1.: "+ e);  			 
				return 0; 			
    		}

    	}
    	
    	@Override
    	protected void onProgressUpdate(String... values){
    		progress.setMessage(values[0]);
    	}
    	
    	@Override
    	protected void onPostExecute(Integer result){

    		if(result==0){
    			try{
				//TRATA ERRO NA REDE (Erro 1.:)-------------------------------------------------------------
	    			GravaPreferencias("latitudeOut", latitudeFinal);
	    			GravaPreferencias("longitudeOut", longitudeFinal);
	    			GravaPreferencias("idAtendimentoOut", idAtendimentoFinal);
	    			GravaPreferencias("idUsuarioOut", idUsuarioFinal);
	    			GravaPreferencias("dataHoraOut", horarioFinal);
	    			GravaPreferencias("tipoCheckOut", tipoCheckFinal);
	    			GravaPreferencias("txtStatusOut", txtStatus);
	    			GravaPreferencias("txtObservacaoOut", observacaoFinal);
	    			GravaPreferencias("salvarCheckOut", "true");
	    			GravaPreferencias("idRelacionamentoOut", idRelacionamento);
	    			
	    			Toast.makeText(instaciaActivity, "Você está sem acesso à internet. Os dados estão salvos e serão enviados de forma automática na próxima conexão.", Toast.LENGTH_SHORT).show();
	    								
					//GravaPreferencias("CheckAberto", "false");
					GravaPreferencias("CheckOutTitulo", titulo);
					
					Intent it =  new  Intent(instaciaActivity, GruposAtendimentos.class);
					
					it.putExtra("id_usuario", idUsuarioFinal);			
					
					if(sharedPreferences.getString("sms", "sms").equals("sim")){
						SmsManager smsManager = SmsManager.getDefault();
						smsManager.sendTextMessage(telefoneCriador, null, "Check-Out realizado para o atendimento: "+titulo, null, null);
					}					
					
					finish();
					startActivity(it);
	    			
    			}
    			catch(Exception e){
    				
    			}
    			
				//-----------------------------------------------------------------------------------------    			
    		}else if(result == 1){
    			try{
    				
    				JSONObject json = new JSONObject(strJson.substring(strJson.indexOf('{')));
    				JSONArray acesso = json.getJSONArray("login");
    				JSONObject fun = new JSONObject(acesso.getString(0));
    				
    				
    				if(fun.getBoolean("acesso")){
 
    					Toast.makeText(instaciaActivity, fun.getString("tipoCheck")+" validado com sucesso!", Toast.LENGTH_SHORT).show();
    					
    					
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
    					
    					GravaPreferencias("CheckAberto", "false");
    					
    					
    					
    					Intent it =  new  Intent(instaciaActivity, GruposAtendimentos.class);	
						
						finish();
						startActivity(it);
  					
    					
    				}
    				else{

    				
    					Toast.makeText(instaciaActivity, "Problema encontrado na rede, validação será feita em um outro momento de forma automática.", Toast.LENGTH_SHORT).show();
    				
    				}	    				
        		}catch(Exception e){
    				//Log.d(DEBUG, "Erro 2.: "+getString(R.string.incorrect_cod)+" "+e);
    				//saidaTextView.setText(getString(R.string.incorrect_cod));
    			}
	      			
    		}
    	}
    	
    }

	protected void RealizarCheckCache(String latitude, String longitude, String horario, String idAtendimento, String check, String observacao, String status, String idrelacionamento, String tituloAtendimento){
		latitudeFinal 		= latitude;
		longitudeFinal 		= longitude;
		horarioFinal 		= horario;
		idAtendimentoFinal 	= idAtendimento;
		tipoCheckFinal 		= check;
		txtStatus 			= status;
		txtObservacaoCahce 	= observacao;
		idRelacionamento 	= idrelacionamento;
		titulo		 		= tituloAtendimento;
				
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
	    			parametrosPost.add(new BasicNameValuePair("txtObservacao", txtObservacaoCahce));
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
}

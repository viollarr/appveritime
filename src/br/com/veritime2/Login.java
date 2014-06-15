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
import org.json.JSONObject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {
	Button login;
	EditText email;
	EditText senha;
	TextView site;
	TextView texto_login_sem_internet;
	TextView texto_login_campo_vazio;
	TextView texto_login_usuario_desativado;
	TextView texto_login_usuario_senha_incorretos;
	TextView texto_login_aguarde;
	private SharedPreferences sharedPreferences;
	private final String PREFS_PRIVATE = "PREFS_PRIVATE";
	private Activity instaciaActivity = Login.this;
	private JSONArray grupos;
	private JSONArray andamentoArray;
	String finalizados = "0";
	String emAtraso = "0";
	String emEspera = "0";
	String emAndamento = "0";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		site									= (TextView) findViewById(R.id.site);
		texto_login_sem_internet 				= (TextView) findViewById(R.string.texto_login_sem_internet);
		texto_login_campo_vazio 				= (TextView) findViewById(R.string.texto_login_campo_vazio);
		texto_login_usuario_desativado 			= (TextView) findViewById(R.string.texto_login_usuario_desativado);
		texto_login_usuario_senha_incorretos 	= (TextView) findViewById(R.string.texto_login_usuario_senha_incorretos);
		texto_login_aguarde 					= (TextView) findViewById(R.string.texto_login_aguarde);
		
		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new Button.OnClickListener() {
		        public void onClick(View v){
		        	email = (EditText) findViewById(R.id.email);
		        	senha = (EditText) findViewById(R.id.senha);
		        	Logar(email, senha);
	        }});
		
		site.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				String dominio = "http://www.veritime.com.br";
				Uri uri = Uri.parse(dominio);
			    
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(it); 
				
			  }
		});
	        
	}
	
	protected void Logar(EditText login, EditText password) {
		String urlServiceStr = "http://www.veritime.com.br/admin/acesso/logarApp";
    	if((login.length()>0)&&(password.length()>0)){
    	
    		Processo processo = new Processo(this);
            processo.execute(urlServiceStr);
    		
    	}
    	else{
    		AlertDialog.Builder errorAlertDialog = new AlertDialog.Builder(instaciaActivity);
    		errorAlertDialog.setTitle("Erro");
    		errorAlertDialog.setMessage("Login ou senha não informados.");
    		errorAlertDialog.setNeutralButton("OK", null);
    		errorAlertDialog.show();
    	}
		
	}
	
	   public class Processo extends AsyncTask <String, String, Integer>{
	    	private ProgressDialog progress;
	    	private String strJson;
	    	private HttpClient httpclient = null;
	    	private ProgressDialog progressDialog;
	    	TextView dataHora;
			TextView nomeCliente;
			TextView status;
	    	
	    	public Processo(Context context){
	    	}
	    	
	    	@Override
	    	protected void onPreExecute(){
	    		progressDialog = ProgressDialog.show(instaciaActivity, "Loanding", "Acessando o sistema...");
	    	}
	    	
	    	
	    	@Override
	    	protected Integer doInBackground(String... paramss){
	    		strJson = null;

	    		try{
	    			URI urlService = new URI(paramss[0]);
	    			TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);		
					String deviceid 		= tm.getDeviceId();
	    			
	    			ArrayList<NameValuePair> parametrosPost = new ArrayList<NameValuePair>();  
	    			parametrosPost.add(new BasicNameValuePair("login", email.getText().toString() ));  				//			
	    			parametrosPost.add(new BasicNameValuePair("senha", senha.getText().toString()));				//
	    			parametrosPost.add(new BasicNameValuePair("telefoneId", deviceid));
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
	    	
	    	@Override
	    	protected void onPostExecute(Integer result){
	    		progressDialog.cancel();
	    		
	    		if(result==0){
	    				Toast.makeText(instaciaActivity, "Para o primeiro acesso ao sistema é preciso conexão com internet.", Toast.LENGTH_SHORT).show();
	    				//finish();
	    				email.setText("");
	    				senha.setText("");
	    		}else if(result == 1){
	    			try{
	    				String tituloAlert 	= null;
	    				String mensagem 	= null;
	    				
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
		    				
		    			    if(fun.getBoolean("acesso")){
		    			    	
		    			    	Intent it =  new  Intent(instaciaActivity, GruposAtendimentos.class);
		    			    	finish();
		    			  		startActivity(it);  
		   					
		    				}
		    				else{
		    					
		    					email.setText("");
		    					senha.setText("");
		    					if(fun.getString("mensagem") == "erro1"){
		    						tituloAlert = "Error";
		    						mensagem = "Usuário inativo. Por favor, entre em contato com o administrador do sistema.";
		    					}
		    					else{
		    						tituloAlert = "Error";
		    						mensagem = "Nome de usuário incorreto ou senha incorreta.";
		    					}
		    					
	        				AlertDialog.Builder errorAlertDialog = new AlertDialog.Builder(instaciaActivity);
	    					errorAlertDialog.setTitle(tituloAlert);
	        				errorAlertDialog.setMessage(mensagem);
	        				errorAlertDialog.setNeutralButton("OK", null);
	        				errorAlertDialog.show();   
	        				finish();
	    					
		    				}
	     				
	        			}catch(Exception e){
	        				finish();
	    			}
		      			
	    		}
	    	}
	    	
	    }
	   
	   public void GravaPreferencias( String nomeCampo, String conteudoCampo) {
			 
			sharedPreferences = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
			 
			Editor prefsPrivateEditor = sharedPreferences.edit();
			
			prefsPrivateEditor.putString(nomeCampo, conteudoCampo);
			prefsPrivateEditor.commit();
			 
		}

}

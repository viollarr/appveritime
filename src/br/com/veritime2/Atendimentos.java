package br.com.veritime2;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Atendimentos extends ListActivity implements OnItemClickListener{
	private TextView saudacao;
	private ImageView voltar;
	private String texto;
	private String textoStatus;
	private String data[] = null;
	private String hora[] = null;
	private String tempo[] = null;
	private String tempTelefone;
	private String telefoneCriado;
	private SharedPreferences sharedPreferences;
	private final String PREFS_PRIVATE = "PREFS_PRIVATE";
	private AtendimentoAdapter adapter;
	String idUsuarioAtendimento;
	private Activity instaciaActivity = Atendimentos.this;
	public String nome_usuario;
	private String exibir;
	private String consulta;

	
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabela);
		ListView lv = getListView();
		lv.setClickable(true);
		lv.setOnItemClickListener(this);
		voltar = (ImageView) findViewById(R.id.imageView1);
		sharedPreferences = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
		saudacao 			= (TextView) findViewById(R.id.texto);
			 
		 exibir = sharedPreferences.getString("exibir", "exibir");
		 idUsuarioAtendimento = sharedPreferences.getString("id_usuario", "id_usuario");
		
		 
		 if(exibir.equals("em_andamento")){
			 consulta = "Andamento";
			 textoStatus = "em andamento:";
		 }
		 else if(exibir.equals("em_espera")){
			 consulta = "Espera";
			 textoStatus = "em espera:";
		 }
		 else if(exibir.equals("em_atraso")){
			 consulta = "Atraso";
			 textoStatus = "em atraso:";
		 }
		 else if(exibir.equals("finalizado")){
			 consulta = "Finalizado";
			 textoStatus = "finalizados:";
		 }

		 
		voltar.setOnClickListener(new OnClickListener() {
		  @Override
		  public void onClick(View v) {
		    finish();
		  }
		});
	
		texto = "Olá <b>"+sharedPreferences.getString("nome_usuario", "nome_usuario")+"</b>,<br>esta é a sua lista de Atendimentos "+textoStatus;
		saudacao.setText(Html.fromHtml(texto));
		
		
		
		List<Atendimento> atendimento = new ArrayList<Atendimento>();
		
		try {
			JSONArray novo = new JSONArray(sharedPreferences.getString(consulta, consulta));
			
			for(Integer i1 = 0; i1 <novo.length(); i1++){
				
				JSONObject atendimentoJ = new JSONObject(novo.getString(i1));
								data = atendimentoJ.getString("data_agendada").split("-");
				hora = atendimentoJ.getString("hora_agendada").split(":");
				tempo = atendimentoJ.getString("tempo_estimado").split(":");
				tempTelefone = atendimentoJ.getString("telefoneCriado").replaceAll("-", "");
				tempTelefone = tempTelefone.replaceAll(" ", "");
				tempTelefone = tempTelefone.replace("(", "");
				tempTelefone = tempTelefone.replace(")", "");
				telefoneCriado = "0"+tempTelefone;
				
				Atendimento atendimentoObj = new Atendimento();			
				
				atendimentoObj.setIdAtendimento(atendimentoJ.getString("idatendimento"));
				atendimentoObj.setStatus(atendimentoJ.getString("status"));
				atendimentoObj.setNomeCliente(atendimentoJ.getString("nome_cliente"));
				atendimentoObj.setTitulo(atendimentoJ.getString("titulo"));
				atendimentoObj.setBairro(atendimentoJ.getString("bairro"));
				atendimentoObj.setData(data[2]+"/"+data[1]+"/"+data[0]);
				atendimentoObj.setHora(hora[0]+":"+hora[1]+"h");
				atendimentoObj.setTempoEstimado(tempo[0]+":"+tempo[1]+"h");
				atendimentoObj.setEndereco(atendimentoJ.getString("endereco"));
				atendimentoObj.setEnderecoNumero(atendimentoJ.getString("endereco_numero"));
				atendimentoObj.setEnderecoComplemento(""+atendimentoJ.getString("endereco_complemento"));
				atendimentoObj.setCidade(atendimentoJ.getString("cidade"));
				atendimentoObj.setLatitude(atendimentoJ.getString("latitude"));
				atendimentoObj.setLongitude(atendimentoJ.getString("longitude"));
				atendimentoObj.setDescricao(atendimentoJ.getString("descricao"));
				atendimentoObj.setDataHoraCheckIn(atendimentoJ.getString("data_hora_checkin"));
				atendimentoObj.setDataHoraCheckOut(atendimentoJ.getString("data_hora_checkout"));
				atendimentoObj.setTemContraSenha(atendimentoJ.getString("tem_contra_senha"));
				atendimentoObj.setContraSenha(atendimentoJ.getString("contra_senha"));
				atendimentoObj.setTelefoneCriador(telefoneCriado);
				atendimentoObj.setStatus(atendimentoJ.getString("status"));
				atendimentoObj.setIdRelacionamento(atendimentoJ.getString("idusuario_atendimento"));
				
				
				atendimento.add(atendimentoObj);
				
			}
			
			adapter = new AtendimentoAdapter(getApplicationContext(), atendimento);
			
			lv.setAdapter(adapter);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	        
	}



	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		sharedPreferences = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
		Atendimento at = adapter.getItem(arg2);		

		if(sharedPreferences.getString("CheckAberto", "CheckAberto").equals("true")){
			if(sharedPreferences.getString("CheckId", "CheckId").equals(at.getIdAtendimento())){
									
				Intent it =  new  Intent(instaciaActivity, Detalhes.class);
				
				it.putExtra("titulo", at.getTitulo());
				it.putExtra("empresa", at.getNomeCliente());
				it.putExtra("endereco", at.getEndereco());
				it.putExtra("enderecoNumero", at.getEnderecoNumero());
				it.putExtra("enderecoComplemento", ""+at.getEnderecoComplemento());
				it.putExtra("enderecoBairro", at.getBairro());
				it.putExtra("cidade", at.getCidade());
				it.putExtra("latitudeAtendimento", at.getLatitude());
				it.putExtra("longitudeAtendimento", at.getLongitude());
				it.putExtra("dataHora", at.getData()+" às "+at.getHora());
				it.putExtra("tempoEstimado", at.getTempoEstimado());
				it.putExtra("descricao", at.getDescricao());
				it.putExtra("idAtendimento", at.getIdAtendimento());
				it.putExtra("data_hora_checkin", at.getDataHoraCheckIn());
				it.putExtra("data_hora_checkout", at.getDataHoraCheckOut());
				it.putExtra("temContraSenha", at.getTemContraSenha());
				it.putExtra("contraSenha", at.getContraSenha());
				it.putExtra("telefoneCriador", at.getTelefoneCriador());
				it.putExtra("id_usuario", idUsuarioAtendimento);
				it.putExtra("status", at.getStatus());
				it.putExtra("idRelacionamento", at.getIdRelacionamento());
				it.putExtra("retirarBotao",arg2);
				
				finish();
				startActivity(it);
			}
			else{
				AlertDialog.Builder errorAlertDialog = new AlertDialog.Builder(instaciaActivity);
				errorAlertDialog.setTitle("Atenção");
				errorAlertDialog.setMessage("Para iniciar este atendimento é preciso finalizar o '"+sharedPreferences.getString("CheckTitulo", "CheckTitulo")+"' para poder iniciar outro.");
				errorAlertDialog.setNeutralButton("OK", null);
				errorAlertDialog.show();
			}
		}
		else{
			
			Intent it =  new  Intent(instaciaActivity, Detalhes.class);
			
			it.putExtra("titulo", at.getTitulo());
			it.putExtra("empresa", at.getNomeCliente());
			it.putExtra("endereco", at.getEndereco());
			it.putExtra("enderecoNumero", at.getEnderecoNumero());
			it.putExtra("enderecoComplemento", ""+at.getEnderecoComplemento());
			it.putExtra("enderecoBairro", at.getBairro());
			it.putExtra("cidade", at.getCidade());
			it.putExtra("latitudeAtendimento", at.getLatitude());
			it.putExtra("longitudeAtendimento", at.getLongitude());
			it.putExtra("dataHora", at.getData()+" às "+at.getHora());
			it.putExtra("tempoEstimado", at.getTempoEstimado());
			it.putExtra("descricao", at.getDescricao());
			it.putExtra("idAtendimento", at.getIdAtendimento());
			it.putExtra("data_hora_checkin", at.getDataHoraCheckIn());
			it.putExtra("data_hora_checkout", at.getDataHoraCheckOut());
			it.putExtra("temContraSenha", at.getTemContraSenha());
			it.putExtra("contraSenha", at.getContraSenha());
			it.putExtra("telefoneCriador", at.getTelefoneCriador());
			it.putExtra("id_usuario", idUsuarioAtendimento);
			it.putExtra("status", at.getStatus());
			it.putExtra("idRelacionamento", at.getIdRelacionamento());
			it.putExtra("retirarBotao",arg2);
			
			finish();
			startActivity(it);
										
		}    							
		
		
	}
	
}

package br.com.veritime2;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AtendimentoAdapter extends BaseAdapter {
	private List<Atendimento> atendimento;
	private LayoutInflater mInflater;
	private ViewHolder holder;

	
	static class ViewHolder{
		private TextView tvClienteAtendimento;
		private TextView tvTituloDataHoraAtendimento;
		private TextView tvEnderecoAtendimento;
		private RelativeLayout cores;

	}

	
	public AtendimentoAdapter(Context context, List<Atendimento> atendimento) {
		mInflater = LayoutInflater.from(context);
		this.atendimento = atendimento;
	}

	@Override
	public int getCount() {
		return atendimento.size();
	}

	@Override
	public Atendimento getItem(int index) {
		return atendimento.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int posicao, View convertView, ViewGroup arg2) {
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.atendimento, null);
			holder = new ViewHolder();
			

			holder.tvClienteAtendimento = (TextView) convertView.findViewById(R.id.clienteAtendimento);
			holder.tvTituloDataHoraAtendimento = (TextView) convertView.findViewById(R.id.tituloDataHoraAtendimento);
			holder.tvEnderecoAtendimento = (TextView) convertView.findViewById(R.id.enderecoAtendimento);
			holder.cores = (RelativeLayout) convertView.findViewById(R.id.atendimentoRelativo);
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}


		Atendimento at = atendimento.get(posicao);

		holder.tvClienteAtendimento.setText(at.getNomeCliente());
		holder.tvTituloDataHoraAtendimento.setText(at.getData()+" - "+at.getHora());
		holder.tvEnderecoAtendimento.setText(at.getEndereco()+", "+at.getEnderecoNumero()+" - "+at.getBairro());
		if(at.getStatus().equals("em_andamento")){
			holder.cores.setBackgroundResource(R.drawable.list_selector_andamento);
		}
		else if(at.getStatus().equals("em_espera")){
			holder.cores.setBackgroundResource(R.drawable.list_selector_espera);			
		}
		else if(at.getStatus().equals("em_atraso")){
			holder.cores.setBackgroundResource(R.drawable.list_selector_atraso);
		}
		else if(at.getStatus().equals("finalizado")){
			holder.cores.setBackgroundResource(R.drawable.list_selector_finalizado);
		}
		

		return convertView;
	}

}

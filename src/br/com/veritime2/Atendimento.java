package br.com.veritime2;


public class Atendimento {
	private Integer id;
	private String idAtendimento;
	private String prioridade;
	private String titulo;
	private String data;
	private String hora;
	private String tempoEstimado;
	private String nomeCliente;
	private String bairro;
	private String status;
	private String endereco;
	private String enderecoNumero;
	private String enderecoComplemento;
	private String cidade;
	private String latitude;
	private String longitude;
	private String descricao;
	private String dataHoraCheckIn;
	private String dataHoraCheckOut;
	private String temContraSenha;
	private String contraSenha;
	private String telefoneCriador;
	private String idRelacionamento;
	
	
	public Atendimento() {
	    this("","","","","","","","","","","","","","","","","","","","","");
	}
	 
    public Atendimento(String idAtendimento, String status, String nomeCliente,String titulo,String bairro,String data,String hora,String tempoEstimado,String endereco,String enderecoNumero,String enderecoComplemento,String cidade,String latitude,String longitude,String descricao,String dataHoraCheckIn,String dataHoraCheckOut,String temContraSenha,String contraSenha, String telefoneCriador, String idRelacionamento) {
    	
    	this.idAtendimento 			= idAtendimento;
    	this.nomeCliente 			= nomeCliente;
        this.titulo 				= titulo;
        this.bairro 				= bairro;
        this.data 					= data;
        this.hora 					= hora;
        this.tempoEstimado 			= tempoEstimado;
        this.endereco 				= endereco;
        this.enderecoNumero 		= enderecoNumero;
        this.enderecoComplemento	= enderecoComplemento;
        this.cidade 				= cidade;
        this.latitude				= latitude;
        this.longitude				= longitude;
        this.descricao 				= descricao;
        this.dataHoraCheckIn 		= dataHoraCheckIn;
        this.dataHoraCheckOut 		= dataHoraCheckOut;
        this.temContraSenha 		= temContraSenha;
        this.contraSenha 			= contraSenha;
        this.status 				= status;
        this.telefoneCriador		= telefoneCriador;
        this.idRelacionamento		= idRelacionamento;
    }

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIdAtendimento() {
		return idAtendimento;
	}
	public void setIdAtendimento(String idAtendimento) {
		this.idAtendimento = idAtendimento;
	}	
	public String getPrioridade() {
		return prioridade;
	}
	public void setPrioridade(String prioridade) {
		this.prioridade = prioridade;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public String getTempoEstimado() {
		return tempoEstimado;
	}
	public void setTempoEstimado(String tempoEstimado) {
		this.tempoEstimado = tempoEstimado;
	}
	public String getNomeCliente() {
		return nomeCliente;
	}
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getEnderecoNumero() {
		return enderecoNumero;
	}

	public void setEnderecoNumero(String enderecoNumero) {
		this.enderecoNumero = enderecoNumero;
	}

	public String getEnderecoComplemento() {
		return enderecoComplemento;
	}

	public void setEnderecoComplemento(String enderecoComplemento) {
		this.enderecoComplemento = enderecoComplemento;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}	
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDataHoraCheckIn() {
		return dataHoraCheckIn;
	}

	public void setDataHoraCheckIn(String dataHoraCheckIn) {
		this.dataHoraCheckIn = dataHoraCheckIn;
	}

	public String getDataHoraCheckOut() {
		return dataHoraCheckOut;
	}

	public void setDataHoraCheckOut(String dataHoraCheckOut) {
		this.dataHoraCheckOut = dataHoraCheckOut;
	}

	public String getTemContraSenha() {
		return temContraSenha;
	}

	public void setTemContraSenha(String temContraSenha) {
		this.temContraSenha = temContraSenha;
	}

	public String getContraSenha() {
		return contraSenha;
	}

	public void setContraSenha(String contraSenha) {
		this.contraSenha = contraSenha;
	}

	public String getTelefoneCriador() {
		return telefoneCriador;
	}

	public void setTelefoneCriador(String telefoneCriador) {
		this.telefoneCriador = telefoneCriador;
	}

	public String getIdRelacionamento() {
		return idRelacionamento;
	}

	public void setIdRelacionamento(String idRelacionamento) {
		this.idRelacionamento = idRelacionamento;
	}

	
}

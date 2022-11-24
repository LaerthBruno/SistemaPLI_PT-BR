package tcc.sistemasumarizacao.classesbase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Laerth Bruno de Brito Gomes e Hilario Tomaz Alves de Oliveira.
 * 
 * Classe que representa um documento.
 * 
 */

public class Documento {
	
  // Atributos
	
  private long id;
  private String nome;
  private String texto;
  private String titulo;
  private List<Token> tokens;
  private List<Token> conceitos;
  private List<Sentenca> sentencas;
	
  
  // Construtores
  
  public Documento() {
	
  }

  public Documento(String texto) {
		
    this.setTexto( texto );
  
  }

  public Documento(String nome, String texto) {
	
	this.nome = nome;
	this.texto = texto;

  }
	
  
  // Métodos
  
  public long getId() {
		
    return id;
	
  }
	
  public void setId(long id) {
		
    this.id = id;
	
  }
	
  public String getNome() {
		
    return nome;
	
  }
	
  public void setNome(String nome) {
		
    this.nome = nome;
	 
  }
	
  public String getTexto() {
		
    return texto;
	
  }
	
  public void setTexto(String texto) {
		
    this.texto = texto;
	
  }
	
  public List<Token> getTokens() {
		
    return tokens;
	
  }
	
  public void setTokens(List<Token> tokens) {
		
    this.tokens = tokens;
	
  }
	
  public List<Token> getConceitos() {
		
    return conceitos;
	
  }
	
  public void setConceitos(List<Token> conceitos) {
		
    this.conceitos = conceitos;
	
  }
	
  public List<Sentenca> getSentencas() {
		
    return sentencas;
	
  }
	
  public void setSentencas(List<Sentenca> sentencas) {
		
    this.sentencas = sentencas;
	
  }
	
  public String getTitulo() {
		
    return titulo;
	
  }
	
  public void setTitulo(String titulo) {
		
    this.titulo = titulo;
	
  }
	
  public boolean addSentenca(Sentenca sentenca){
		
   if(this.sentencas == null){
		  
     this.sentencas = new ArrayList<Sentenca>();
	
   }
	
   return this.sentencas.add(sentenca);
	
  }

  public boolean addConceito(Token conceito){
	
    if( this.conceitos == null ){
	
      this.conceitos = new ArrayList<Token>();
		
    }
	
    return this.conceitos.add(conceito);
	
  }

  public boolean addConceitos(List<Token> conceitos){
		
    if( this.conceitos == null ){
		
      this.conceitos = new ArrayList<Token>();
			
    }

    return this.conceitos.addAll( conceitos );
		
  }

  public boolean addTokens(List<Token> tokens){
	
    if(this.tokens == null){
	
      this.tokens = new ArrayList<Token>();
		
    }
		
    return this.tokens.addAll( tokens );
	
  }
  
  @Override
	
  public String toString() {
	
    return this.getNome();
    
  }

}

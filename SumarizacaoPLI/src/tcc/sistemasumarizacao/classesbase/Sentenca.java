package tcc.sistemasumarizacao.classesbase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Laerth Bruno de Brito Gomes e Hilario Tomaz Alves de Oliveira.
 * 
 * Classe que representa uma sentença (frase).
 * 
 */

public class Sentenca implements Comparable<Sentenca> {
	
  // Atributos
	
  private long id;
  private String idString;
  private String texto;	
  private List<Token> tokens;
  private List<Token> conceitos;
  private double relevancia;
	
  
  // Construtores
  
  public Sentenca(){
	
  }
	
  public Sentenca(String texto, List<Token> tokens, List<Token> conceitos) {

	this.texto = texto;
	this.tokens = tokens;
	this.conceitos = conceitos;
	
  }
	
  
  // Métodos
  
  public long getId() {
		
	return id;
	
  }
	
  public void setId(long id) {
		
	this.id = id;
	
  }

  public String getIdString() {
	
	return idString;
	
  }

  public void setIdString(String idString) {
	
    this.idString = idString;

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
	
  public boolean addConceito(Token conceito){
		
	if(this.conceitos == null){
		  
	  this.conceitos = new ArrayList<Token>();
	  
	}
	
	return this.conceitos.add(conceito);
	
  }
	
  public boolean addToken(Token token){
		
	if(this.tokens == null){
		  
	  this.tokens = new ArrayList<Token>();
	  
	}
	
	return this.tokens.add(token);
	
  }

  public double getRelevancia() {
	
	return relevancia;
	
  }

  public void setRelevancia(double relevancia) {
	
    this.relevancia = relevancia;
	
  }

  @Override

  public String toString() {
	
    return this.getTexto();
    
  }

  @Override

  public int compareTo(Sentenca sentenca) {
	
    return Long.compare( this.getId(), sentenca.getId() );
    
  }
  
}

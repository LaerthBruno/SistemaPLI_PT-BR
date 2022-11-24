
package tcc.sistemasumarizacao.classesbase;

import java.util.List;

/**
 * @author Laerth Bruno de Brito Gomes e Hilario Tomaz Alves de Oliveira.
 * 
 * Classe que representa um token.
 * 
 */

public class Token {
	
  // Atributos
	
  private long id;
  private String texto;
  private String raiz;
  private String pos;
  private double relevancia;
  private boolean stopWord;
  private String tipo;
  private String classePOS;
  private List<Token> subTokens;
	
  // Construtores
	
  public Token() {
	
  }
	
  public Token(String texto) {
		
	this.setTexto( texto );
	
  }

  
  // Métodos
	
  public long getId() {
	  
	return id;
	
  }
	
  public void setId(long id) {
		
	this.id = id;
	
  }
	
  public String getTexto() {
		
	return texto;
	
  }
	
  public void setTexto(String texto) {
		
	this.texto = texto;
	
  }
	
  public String getPos() {
		
	return pos;
	
  }
	
  public void setPos(String pos) {
	  
	this.pos = pos;
	
  }
	
  public double getRelevancia() {
		
	return relevancia;
	
  }
	
  public void setRelevancia(double relevancia) {
		
	this.relevancia = relevancia;
	
  }
	
  public String getRaiz() {
		
	return raiz;
	
  }
	
  public void setRaiz(String raiz) {
		
	this.raiz = raiz;
	
  }
	
  public boolean isStopWord() {
		
	return stopWord;
	
  }
	
  public void setStopWord(boolean stopWord) {
		
	this.stopWord = stopWord;
	
  }
	
  public String getTipo() {
		
	return tipo;
	
  }
	
  public void setTipo(String tipo) {
		
	this.tipo = tipo;
	
  }
	
  public String getClassePOS() {
		
	return classePOS;
	
  }
	
  public void setClassePOS(String classePOS) {
		
	this.classePOS = classePOS;
	
  }

  public List<Token> getSubTokens() {
	
	return subTokens;
	
  }

  public void setSubTokens(List<Token> subTokens) {
	
    this.subTokens = subTokens;
	
  }

  @Override
	
  public int hashCode() {
	
	final int prime = 31;
	
	int result = 1;
	
	result = prime * result + ( ( this.raiz == null) ? 0 : this.raiz.hashCode());
	
	return result;
	
  }

  @Override
	
  public boolean equals(Object obj) {
	
	if ( this == obj ) {
		
	  return true;
		
	}
	
	if( obj == null ) {
		
	  return false;
		
	}
	
	if( getClass() != obj.getClass() ) {
		
	  return false;
		
	}
	
	Token other = (Token) obj;
	
	if( this.raiz == null ) {
		
	  if( other.raiz != null ) {
				
	    return false;
		
	  }
		
	} else if( !this.raiz.equals( other.raiz ) ) {
		
	  return false;
		
	}
	
	return true;
	
  }

  @Override

  public String toString() {
	
    return this.getTexto();
    
  }
  
}

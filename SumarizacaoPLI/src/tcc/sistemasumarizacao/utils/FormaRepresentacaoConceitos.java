
package tcc.sistemasumarizacao.utils;

/**
 * @author Laerth Bruno de Brito Gomes e Hilario Tomaz Alves de Oliveira.
 * 
 * Classe Enum que possui as formas de representa��o dos conceitos.
 * 
 */

public enum FormaRepresentacaoConceitos {

  UNIGRAMA ( "uni" ), BIGRAMA ( "big" ), BIGRAMA_UNIGRAMA ( "big_uni" );
	
  // Atributos
	
  private String label;
	
  
  // Construtor
  
  private FormaRepresentacaoConceitos(String label) {
	  
    this.label = label;
    
  }

  
  // M�todos
  
  public String getLabel() {
	
    return label;

  }

  public void setLabel(String label) {
	
    this.label = label;

  }
  
}

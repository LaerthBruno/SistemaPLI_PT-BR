package tcc.sistemasumarizacao.utils;

/**
 * @author Laerth Bruno de Brito Gomes e Hilario Tomaz Alves de Oliveira.
 * 
 * Classe Enum que representa os m�todos de pondera��o de conceitos.
 * 
 */

public enum MetodosPonderacaoConceitos {

  FREQUENCIA_DOCUMENTOS ( "freqdoc" ), POSICAO_SENTENCAS ( "possent" ), COMBINADOS ( "comb" );

  // Atributos
	
  private String label;
	
  
  // Construtor
  
  private MetodosPonderacaoConceitos(String label) {
		  
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

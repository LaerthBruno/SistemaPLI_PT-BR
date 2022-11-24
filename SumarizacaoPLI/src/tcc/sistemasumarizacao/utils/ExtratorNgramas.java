
package tcc.sistemasumarizacao.utils;

import java.util.ArrayList;
import java.util.List;

import tcc.sistemasumarizacao.classesbase.Token;

public class ExtratorNgramas {

  // Constructor
	
  public ExtratorNgramas() {
	
  }
  
  
  // Methods
  
  private static List<Token> ngramas(List<Token> tokens, int n) {
	
    List<Token> ngrams = new ArrayList<Token>();

    int id = 1;
    
    for (int i = 0; i < tokens.size() - n + 1; i++) {
	
      Token nGram = ExtratorNgramas.concatenar( tokens, i, i + n );
      
      nGram.setId( id++ );
      
      if( nGram != null ) {
    	  
        ngrams.add( nGram );
      
      }
      
    }
	  
    return ngrams;
	
  }

  private static Token concatenar(List<Token> tokens, int inicio, int fim) {
	
    StringBuilder textoNgrama = new StringBuilder();
    StringBuilder raizNgrama = new StringBuilder();
    
    ArrayList<Token> subTokens = new ArrayList<Token>();
    
    for (int i = inicio; i < fim; i++) {
	  
      textoNgrama.append( ( i > inicio ? " " : "" ) + tokens.get( i ).getTexto() );
      
      raizNgrama.append( ( i > inicio ? " " : "" ) + tokens.get( i ).getRaiz() );
      
      subTokens.add( tokens.get( i ) );
      
    } // FOR TOKENS
	    
    Token token = new Token( textoNgrama.toString() );
	
    token.setRaiz( raizNgrama.toString().replaceAll( "(\\.|,|'|`)+", "" ) );
    
    token.setSubTokens( subTokens );
    
    return token;
    
  }
	
  public static ArrayList<Token> extractNGramas(List<Token> tokens,int minNgrama, int maxNgrama){
	        
	if( tokens == null ) {
		
	  return null;
	  
	}
	
    ArrayList<Token> ngrams = new ArrayList<Token>();   
	
    for (int n = minNgrama; n <= maxNgrama; n++) {
	
      List<Token> ngramsAux = ExtratorNgramas.ngramas( tokens, n );
      
      if( ngramsAux != null ) {
    	  
        ngrams.addAll( ngramsAux );
        
      }
	  
    } // FOR NGRAMS
	
    return ngrams;
	
  }

}

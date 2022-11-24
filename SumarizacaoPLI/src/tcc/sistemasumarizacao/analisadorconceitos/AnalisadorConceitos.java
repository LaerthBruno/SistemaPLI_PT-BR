
package tcc.sistemasumarizacao.analisadorconceitos;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import tcc.sistemasumarizacao.classesbase.Documento;
import tcc.sistemasumarizacao.classesbase.GrupoDocumentos;
import tcc.sistemasumarizacao.classesbase.Sentenca;
import tcc.sistemasumarizacao.classesbase.Token;
import tcc.sistemasumarizacao.utils.ExtratorNgramas;
import tcc.sistemasumarizacao.utils.FormaRepresentacaoConceitos;
import tcc.sistemasumarizacao.utils.MetodosPonderacaoConceitos;
import tcc.sistemasumarizacao.utils.Utils;

/**
 * @author Laerth Bruno de Brito Gomes e Hilario Tomaz Alves de Oliveira.
 * 
 * Classe que realiza a extração e ponderação dos conceitos no processo sumarização.
 * 
 */

public class AnalisadorConceitos {

  // Construtor
	
  public AnalisadorConceitos() {
	
  }
  
  
  // Métodos
  
  public void extrairConceitos(GrupoDocumentos grupoDocumentos, 
   FormaRepresentacaoConceitos formaRepresentacaoConceitos) {

    if( formaRepresentacaoConceitos == FormaRepresentacaoConceitos.UNIGRAMA ) {
    	
      this.extrairUnigramasComoConceitos( grupoDocumentos );
      
    } else if( formaRepresentacaoConceitos == FormaRepresentacaoConceitos.BIGRAMA ) {
    	
      this.extrairNgramasComoConceitos( grupoDocumentos, 2, 2 );
      
    } else if( formaRepresentacaoConceitos == FormaRepresentacaoConceitos.BIGRAMA_UNIGRAMA ) {
    	
      this.extrairNgramasComoConceitos( grupoDocumentos, 1, 2 );
    	
    }
    
  }

  public void ponderarPesosConceitos(GrupoDocumentos grupoDocumentos, 
   MetodosPonderacaoConceitos metododPonderacaoConceitos) {

    if( metododPonderacaoConceitos == MetodosPonderacaoConceitos.FREQUENCIA_DOCUMENTOS ) {
    	
      this.computarFrequenciaDocumentos( grupoDocumentos );
      
    } else if( metododPonderacaoConceitos == MetodosPonderacaoConceitos.POSICAO_SENTENCAS ) {
    	
      this.computarPosicaoSentencas( grupoDocumentos );
      
    } else if( metododPonderacaoConceitos == MetodosPonderacaoConceitos.COMBINADOS ) {        
    	
      this.computarRelevanciaCombinada( grupoDocumentos );
      
    }
  
  }

  private void computarRelevanciaCombinada(GrupoDocumentos grupoDocumentos) {
	
    HashMap<Token, Double> mapConceitos = new HashMap<Token, Double>();
		
    for(Documento documento: grupoDocumentos.getDocumentos()) {    
		     
	  for(Token conceito: documento.getConceitos()) {
				  
		mapConceitos.put( conceito, 0.0 );
			
	  } // FOR CONCEITOS
		  
    } // FOR DOCUMENTOS
		 
    double maiorRelevancia = 0;
    
	for(Entry<Token, Double> entry: mapConceitos.entrySet()) {
			
	  double posicaoSentencas = 0;
	  int frequenciaDocumentos = 0;  
	  
	  for(Documento documento: grupoDocumentos.getDocumentos()) {
			
		if( documento.getConceitos().contains( entry.getKey() ) ) {
			
		  frequenciaDocumentos++;
		  
		}
		
		for(Sentenca sentenca: documento.getSentencas()) {
				
		  if( sentenca.getConceitos() != null && 
		   sentenca.getConceitos().contains( entry.getKey() ) ) {
				
		    posicaoSentencas +=  ( 1 - ( ( sentenca.getId() - 1 ) / ( 1.0 * documento.getSentencas().size() ) ) );

		    break;
		    
		  }
			
		} // FOR SENTENCAS
			
	  } // FOR DOCUMENTOS
			
	  double relevancia = frequenciaDocumentos * posicaoSentencas;
	  
	  if( relevancia > maiorRelevancia ) {
		  
	    maiorRelevancia = relevancia;
	    
	  }
			  
	  entry.setValue( relevancia );
			
	} // FOR MAP CONCEITOS

	for(Entry<Token, Double> entry: mapConceitos.entrySet()) {
	
      entry.setValue( entry.getValue() / maiorRelevancia );
      
	} // FOR MAP CONCEITOS
	
	for(Documento documento: grupoDocumentos.getDocumentos()) {    
		     
	  for(Sentenca sentenca: documento.getSentencas()) {
				
		if( sentenca.getConceitos() != null ) {
					
		  for(Token conceito: sentenca.getConceitos()) {
			    	
	        if( mapConceitos.containsKey( conceito ) ) {
			    	  
	          double frequenciaDocumento = mapConceitos.get( conceito );
			        
	          conceito.setRelevancia( frequenciaDocumento );
			        
	        }
			      
	      } // FOR CONCEITOS
				
		}
				
	  } // FOR SENTENCAS
			  
	} // FOR DOCUMENTOS

  }

  private void computarPosicaoSentencas(GrupoDocumentos grupoDocumentos) {
	
    HashMap<Token, Double> mapConceitos = new HashMap<Token, Double>();
	
    for(Documento documento: grupoDocumentos.getDocumentos()) {    
	     
	  for(Token conceito: documento.getConceitos()) {
			  
		mapConceitos.put( conceito, 0.0 );
		
	  } // FOR CONCEITOS
	  
    } // FOR DOCUMENTOS
	 
    double maiorPosicaoSentencas = 0;
    
	for(Entry<Token, Double> entry: mapConceitos.entrySet()) {
		
	  double posicaoSentencas = 0;
	  
	  for(Documento documento: grupoDocumentos.getDocumentos()) {    
		
		for(Sentenca sentenca: documento.getSentencas()) {
			
		  if( sentenca.getConceitos() != null && sentenca.getConceitos().contains( entry.getKey() ) ) {
			
		    posicaoSentencas +=  ( 1 - ( ( sentenca.getId() - 1 ) / ( 1.0 * documento.getSentencas().size() ) ) );

		    break;
		    
		  }
		
		} // FOR SENTENCAS
		
	  } // FOR DOCUMENTOS
		
	  if( posicaoSentencas > maiorPosicaoSentencas ) {
		  
	    maiorPosicaoSentencas = posicaoSentencas;
		  
	  }
	  
	  entry.setValue( posicaoSentencas );
		
	} // FOR MAP CONCEITOS

	for(Entry<Token, Double> entry: mapConceitos.entrySet()) {
		
	  entry.setValue( entry.getValue() / maiorPosicaoSentencas );
	    
	} // FOR MAP CONCEITOS
		
	for(Documento documento: grupoDocumentos.getDocumentos()) {    
	     
	  for(Sentenca sentenca: documento.getSentencas()) {
			
		if( sentenca.getConceitos() != null ) {
				
		  for(Token conceito: sentenca.getConceitos()) {
		    	
	        if( mapConceitos.containsKey( conceito ) ) {
		    	  
	          double frequenciaDocumento = mapConceitos.get( conceito );
		        
	          conceito.setRelevancia( frequenciaDocumento );
		        
	        }
		      
	      } // FOR CONCEITOS
			
		}
			
	  } // FOR SENTENCAS
		  
	} // FOR DOCUMENTOS
	  
  }

  private void computarFrequenciaDocumentos(GrupoDocumentos grupoDocumentos) {
	
	HashMap<Token, Double> mapConceitos = new HashMap<Token, Double>();
	
	for(Documento documento: grupoDocumentos.getDocumentos()) {    
     
	  for(Token conceito: documento.getConceitos()) {
		  
	    mapConceitos.put( conceito, 0.0 );
	    
	  } // FOR CONCEITOS
    
	} // FOR DOCUMENTOS
    
	double maiorFrequenciaDocumentos = 0;
	
	for(Entry<Token, Double> entry: mapConceitos.entrySet()) {
		
	  double frequenciaDocumentos = 0;
	  
	  for(Documento documento: grupoDocumentos.getDocumentos()) {    
		
	    if( documento.getConceitos().contains( entry.getKey() ) ) {
	    	
	      frequenciaDocumentos++;
	    	
	    }
	    
	  } // FOR DOCUMENTOS
		
	  if( frequenciaDocumentos > maiorFrequenciaDocumentos ) {
		  
	    maiorFrequenciaDocumentos = frequenciaDocumentos; 
				  
	  }
	  
	  entry.setValue( frequenciaDocumentos );
	  
	} // FOR MAP CONCEITOS
	
	for(Entry<Token, Double> entry: mapConceitos.entrySet()) {
		
	  entry.setValue( entry.getValue() / maiorFrequenciaDocumentos );

	} // FOR MAP CONCEITOS

	for(Documento documento: grupoDocumentos.getDocumentos()) {    
	     
	  for(Sentenca sentenca: documento.getSentencas()) {
		
		if( sentenca.getConceitos() != null ) {
			
		  for(Token conceito: sentenca.getConceitos()) {
	    	
	        if( mapConceitos.containsKey( conceito ) ) {
	    	  
	          double frequenciaDocumento = mapConceitos.get( conceito );
	        
	          conceito.setRelevancia( frequenciaDocumento );
	        
	        }
	      
	      } // FOR CONCEITOS
		
		}
		
	  } // FOR SENTENCAS
	  
	} // FOR DOCUMENTOS
	
  }

  private void extrairUnigramasComoConceitos(GrupoDocumentos grupoDocumentos) {

    for(Documento documento: grupoDocumentos.getDocumentos()) {
    	
      for(Sentenca sentenca: documento.getSentencas()) {
    	  
        for(Token token: sentenca.getTokens()) {
        	
          if( !token.isStopWord() && !"Simbolo".equalsIgnoreCase( token.getTipo() ) ) {
        	  
            sentenca.addConceito( token );
            
          }
          
        } // FOR TOKENS
      
        documento.addConceitos( sentenca.getConceitos() );
        
      } // FOR SENTENCAS
            
    } // FOR DOCUMENTOS
    
  }

  private void extrairNgramasComoConceitos(GrupoDocumentos grupoDocumentos, int minimo, int maximo) {

    for(Documento documento: grupoDocumentos.getDocumentos()) {
	
      for(Sentenca sentenca: documento.getSentencas()) {
	  
        List<Token> bigramas = ExtratorNgramas.extractNGramas( sentenca.getTokens(), minimo, maximo );
        
        bigramas = Utils.filtrarConceitos( bigramas );
        
        if( bigramas != null && !bigramas.isEmpty() ) {
        	
          sentenca.setConceitos( bigramas );
        
          documento.addConceitos( bigramas );
        
        }
        
      } // FOR SENTENCAS
	  
    } // FOR DOCUMENTOS

  }

}

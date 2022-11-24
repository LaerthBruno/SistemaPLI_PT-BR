
package tcc.sistemasumarizacao.cogroo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import org.cogroo.analyzer.Analyzer;
import org.cogroo.analyzer.ComponentFactory;
import org.cogroo.text.impl.DocumentImpl;
import org.tartarus.snowball.ext.PorterStemmer;
import tcc.sistemasumarizacao.classesbase.Documento;
import tcc.sistemasumarizacao.classesbase.GrupoDocumentos;
import tcc.sistemasumarizacao.classesbase.Sentenca;
import tcc.sistemasumarizacao.classesbase.Token;
import tcc.sistemasumarizacao.utils.Utils;

/**
 * @author Laerth Bruno de Brito Gomes e Hilario Tomaz Alves de Oliveira.
 * 
 * Classe que realiza o processo de sumarização usando o corpus CSTNews.
 * 
 */

public class CogrooParser {

  // Atributos
	
  private ComponentFactory componentFactory;
  private Set<String> stopList;  
  private PorterStemmer porterStemmer;
  
  
  // Construtor
	
  public CogrooParser(File customPipeLineFile, Set<String> stopList) {
	 
    InputStream inputStream = null;
	
    try {
	
      inputStream = new FileInputStream( customPipeLineFile );

      this.componentFactory = ComponentFactory.create( inputStream );
  	
    }
    
    catch (FileNotFoundException e) {
	
      e.printStackTrace();
	
    }
    
    finally {
    
      if( inputStream != null ) {
    	  
        try {
		
          inputStream.close();
	 
        } 
      
        catch (IOException e) {
	
          e.printStackTrace();
	
        }
      
      }
      
    }
    
    if( stopList != null ) {
    	
      this.stopList = stopList;
    
    } else {
    	
      this.stopList = new HashSet<String>();
      
    }
    
    this.porterStemmer = new PorterStemmer();
    
  }

  
  // Métodos

  public void processarDocumento(Documento documento) {
	
    Analyzer cogroo = this.componentFactory.createPipe();
	
    DocumentImpl documentImpl = new DocumentImpl();
    
    documentImpl.setText( documento.getTexto() );
	
    cogroo.analyze( documentImpl );
	
    int idSentences = 1;
    
    for(org.cogroo.text.Sentence sentenceCogroo : documentImpl.getSentences()) {
    	 
      Sentenca sentenca = new Sentenca();
      
      sentenca.setId( idSentences++ );
      sentenca.setIdString( sentenca.getId() + "_" + documento.getNome() );

      sentenca.setTexto( sentenceCogroo.getText() );
      
      if( sentenceCogroo.getTokens() == null ) {
    	  
    	documento.addSentenca( sentenca );
          
        continue;
        
      }
      
      int idTokens = 1;
      
      for(org.cogroo.text.Token tokenCogroo : sentenceCogroo.getTokens()) {
    	 
        Token token = new Token();
      
        token.setId( idTokens++ );
        
        token.setTexto( tokenCogroo.getLexeme().replaceAll( "_", " " ) );
        
        String tokenNormalizado = Utils.normalizarString( token.getTexto() );
        
        porterStemmer.setCurrent( tokenNormalizado );
        
        porterStemmer.stem();
        
        token.setRaiz( porterStemmer.getCurrent() );
        
        if( this.stopList.contains( tokenNormalizado ) ) {
        	
          token.setStopWord( true );
          
        }
        
        String tipoToken = Utils.verificarTipoToken( token );
        
        token.setTipo( tipoToken );
        
        token.setClassePOS( tokenCogroo.getPOSTag() );
        
        sentenca.addToken( token );
        
      } // FOR TOKENS
  
      documento.addTokens( sentenca.getTokens() );
      documento.addSentenca( sentenca );
      
    } // FOR SENTENCES
    
  }

  public void processarGrupoDocumentos(GrupoDocumentos grupoDocumentos) {
	
    for(Documento documento: grupoDocumentos.getDocumentos()) {
    	
      this.processarDocumento( documento );
      
    } // FOR DOCUMENTOS
    
  }

  public Set<String> getStopList() {
	
    return stopList;

  }

  public void setStopList(Set<String> stopList) {
	
    this.stopList = stopList;

  }
  
}

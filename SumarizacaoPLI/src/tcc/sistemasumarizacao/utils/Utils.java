
package tcc.sistemasumarizacao.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tcc.sistemasumarizacao.classesbase.Corpus;
import tcc.sistemasumarizacao.classesbase.Documento;
import tcc.sistemasumarizacao.classesbase.GrupoDocumentos;
import tcc.sistemasumarizacao.classesbase.Sentenca;
import tcc.sistemasumarizacao.classesbase.Token;

/**
 * @author Laerth Bruno de Brito Gomes e Hilario Tomaz Alves de Oliveira.
 * 
 * Classe que possui diversos método utilitário usados no sistema de sumarização.
 * 
 */

public class Utils {

  public static Corpus construirCSTNewsCorpus(File diretorio) {
	  
	if( diretorio == null || !diretorio.exists() ) {
		
	  return null; 
	  
	}
	
	File[] diretoriosClusters = diretorio.listFiles();
	
	Corpus corpus = new Corpus( "CSTNews" );
	
	for(File diretorioGrupoDocumentos: diretoriosClusters) {
	  
	  String nomeGrupoDocumento = diretorioGrupoDocumentos.getName().split( "_" )[0];
	  
	  if( !nomeGrupoDocumento.startsWith( "C" ) ) {
		  
	    continue;
	    
	  }
	  
	  GrupoDocumentos grupoDocumento = new GrupoDocumentos( nomeGrupoDocumento );
	  
	  File diretorioDocumentos = new File( diretorioGrupoDocumentos + File.separator + 
			  "Textos-fonte" );
	  
	  File diretorioResumos = new File( diretorioGrupoDocumentos + File.separator + 
			  "Sumarios" + File.separator + "Novos sumarios" + File.separator + "Extratos" );
	  
	  File[] arquivosResumos = diretorioResumos.listFiles();
	  
	  for(File arquivoResumo: arquivosResumos) {
		  
	    try {
		
	      String resumoReferencia = Utils.lerArquivo( arquivoResumo );
	      
	      resumoReferencia = resumoReferencia.replaceAll( "<(.)*?>", "" );
	      
	      grupoDocumento.addResumoReferencia( resumoReferencia );
	      
	    }
	    
	    catch (IOException e) {
		
	      e.printStackTrace();
		
	    }	    	    
	    
	  }
	 
	  File[] arquivosDocumentos = diretorioDocumentos.listFiles();
	  
	  for(File arquivoDocumento: arquivosDocumentos) {
		  
	    if( !arquivoDocumento.getName().contains( "_titulo" ) ) {
	      
	      String textoDocumento;
		
	      try {
			
	        textoDocumento = Utils.lerArquivo( arquivoDocumento );
		
	      }

	      catch (IOException e) {
			
	        e.printStackTrace();
		
	        continue;
	        
	      }	
	    
	      String[] fragmentos = arquivoDocumento.getName().split( "_" );
	      
	      Documento documento = new Documento( fragmentos[0], textoDocumento );
	      
	      File arquivoTitulo = new File( 
	    		  arquivoDocumento.toString().replaceAll( "\\.txt", "_titulo.txt" ) );
	      
	      if( arquivoTitulo.exists()) {
	    	
	        try {
			
	          String titulo = Utils.lerArquivo( arquivoTitulo );
	          
	          documento.setTitulo( titulo );
	          
	        }
	        
	        catch (IOException e) {
			
	          e.printStackTrace();
			
	        }
	        
	      }	      
	      
	      grupoDocumento.addDocumento( documento );
	      
	    }
	    
	  }
	  
	  corpus.addGrupoDocumentos( grupoDocumento );
	  
	}
	
    return corpus;
    
  }
  
  public static String lerArquivo(File arquivo) throws IOException {
			
    FileInputStream inputStream = null;
			
	Scanner sc = null;
		    
	try {
			
	  inputStream = new FileInputStream( arquivo );

	  sc = new Scanner( inputStream, "UTF-8" );
		
	  StringBuilder texto = new StringBuilder();
	  
	  while ( sc.hasNextLine() ) {
			  
	    String linha = sc.nextLine();
		
	    texto.append( linha );
	    
	    texto.append( "\n" );
	    
	  }

	  texto.setLength( texto.length() - 1 );
	  
	  return texto.toString();
		
	}
	
	finally {
			
	  if ( inputStream != null ) {
			  
        inputStream.close();

	  }
		
	  if ( sc != null ) {
			  
	    sc.close();
		
	  }
		
	}
	
  }

  public static void criarArquivo(String texto, File arquivo) {
	  
	FileOutputStream fileStream = null;
	OutputStreamWriter outputStreamWriter = null;
	
    try {
      
      fileStream = new FileOutputStream( arquivo );
      outputStreamWriter = new OutputStreamWriter( fileStream, "UTF-8");
      
      outputStreamWriter.write( texto );
      
      outputStreamWriter.flush();
      
    }
    
    catch (IOException e) {
    
      e.printStackTrace();
      
    }
    
    finally {

      if( fileStream != null ) {
      	  
        try {
          
          fileStream.close();
          
        }
        
        catch (IOException e) {
          
          e.printStackTrace();
          
        }
        
      }

      if( outputStreamWriter != null ) {
    	  
        try {
      
          outputStreamWriter.close();
        
        }
      
        catch (IOException e) {
      
          e.printStackTrace();
        
        }
        
      }

    }
  
  }
  
  public static String normalizarString(String texto) {

    if( texto == null ) {
			
	  return null;
		
    }
	
    texto = Normalizer.normalize( texto, Normalizer.Form.NFD ).replaceAll( "[^\\p{ASCII}]", "" );
	
    texto = texto.replaceAll( "\\p{P}", "" );
	
    texto = texto.toLowerCase();
	
    return texto.trim();
  
  }

  public static String verificarTipoToken(Token token) {

    try {
	    	
	  Double.parseDouble( token.getTexto() );
	  
	  return "Numero";
	  
    }
	
    catch (Exception e) {
	
    }
	
    if( Utils.isSimbolo( token.getTexto() ) ) {
	  		    	
	  return "Simbolo";
	  
    }
	
    return "Palavra";

  }

  private static boolean isSimbolo(String texto) {
	
    texto = texto.replaceAll( "_", "+" );
    
    Pattern pattern = Pattern.compile( "(\\W)+" );
    Matcher matcher = pattern.matcher( texto );
    
    return matcher.matches();
    
  }

  public static Set<String> carregarStopList(File arquivo) throws Exception {
  	    
	if( !arquivo.exists() ) {
		
	  throw new Exception( "Arquivo [" + arquivo + "] não Encontrado!" );
	  
	}
	
    try {
  	
      String texto = Utils.lerArquivo( arquivo );
  	
      if( texto == null ) {
      	  
        return null;
        
      }
      
      String[] linhas = texto.split( "\r?\n" );
      
      Set<String> palavras = new HashSet<String>();
      
      for(String linha: linhas) {

    	String linhaNormalizada = Utils.normalizarString( linha );
    	
        palavras.add( linhaNormalizada );
        
      }
     
      return palavras;
      
    }
    
    catch (IOException e) {
  	
      e.printStackTrace();
  	
    }
    
    return null;

  }

  public static List<Token> filtrarConceitos(List<Token> conceitos) {
	
    if( conceitos == null || conceitos.isEmpty() ) {
			
	  return null;
		
    }
			
    ArrayList<Token> conceitosFiltrados = new ArrayList<Token>();
	
    FORCONCEITOS: for(Token conceito: conceitos) {

      if( conceito.getTexto().startsWith( "'" ) || 
       conceito.getTexto().contains( "\"" ) || 
       conceito.getTexto().contains( "(" ) || 
       conceito.getTexto().contains( ")" ) ||
       conceito.getTexto().contains( "?" ) ) {
	
        continue;
		
      }
		   
      if( conceito.getSubTokens() != null ) {
		    	  
		int contagemStopWords = 0;
		
		for(Token subToken: conceito.getSubTokens()) {
		        	          
		  if( "Simbolo".equalsIgnoreCase( subToken.getTipo() ) ) {
		        	  
		    continue FORCONCEITOS;
		    
		  } else if( subToken.isStopWord() ) {
		            	
		    contagemStopWords++;
		    
		  } 
		  
		} // FOR SUBTOKENS
		
		if( contagemStopWords == conceito.getSubTokens().size() ) {
		    	  
		  continue;
		  
		}
		
      }
		
      conceitosFiltrados.add( conceito );
		
    } // FOR CONCEPTS
	
    return conceitosFiltrados.isEmpty() ? null: conceitosFiltrados;
	
  }

  public static void salvarResumo(GrupoDocumentos grupoDocumentos, File diretorioResumos) {

    File grupoDocumentosDiretorio = 
    		new File( diretorioResumos + File.separator + grupoDocumentos.getNome() );

    if( !grupoDocumentosDiretorio.exists() ) {
    	
      grupoDocumentosDiretorio.mkdir();
    	
    }
    
    for(Entry<String, String> entry: grupoDocumentos.getMapResumos().entrySet()) {
	
      File arquivoResumos = new File( grupoDocumentosDiretorio + File.separator + 
       entry.getKey() + ".txt" );

      Utils.criarArquivo( entry.getValue(), arquivoResumos );
		
    } // FOR RESUMOS
	
  }
  
  public static HashMap<Token, ArrayList<Sentenca>> montarMapConceitosSentencas(GrupoDocumentos grupoDocumentos) {

    HashMap<Token, ArrayList<Sentenca>> mapConceitosSentencas = 
    		new HashMap<Token, ArrayList<Sentenca>>();
	
    for(Documento documento: grupoDocumentos.getDocumentos()) {
    	
      for(Sentenca sentenca: documento.getSentencas()) {
		  
	    if( sentenca.getConceitos() != null ) {
	     	
	      for(Token conceito: sentenca.getConceitos()) {
	    	
	        ArrayList<Sentenca> sentencasPorConceito = null;
	    	
	        if( mapConceitosSentencas.containsKey( conceito ) ) {
	        	
	          sentencasPorConceito = mapConceitosSentencas.get( conceito );
	        
	        } else {
	        	
	          sentencasPorConceito = new ArrayList<Sentenca>();
	        
	        }
	      
	        sentencasPorConceito.add( sentenca );
	      
	        mapConceitosSentencas.put( conceito, sentencasPorConceito );
	      
	      } // FOR CONCEITOS
	    
	    }
	  
      } // FOR SENTENCAS
    
    } // FOR DOCUMENTOS
    
	return mapConceitosSentencas;
	
  }
  
}
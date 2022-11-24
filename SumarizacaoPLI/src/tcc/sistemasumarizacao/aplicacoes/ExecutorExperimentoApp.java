
package tcc.sistemasumarizacao.aplicacoes;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import tcc.sistemasumarizacao.analisadorconceitos.AnalisadorConceitos;
import tcc.sistemasumarizacao.analisadorpli.AnalisadorPLI;
import tcc.sistemasumarizacao.classesbase.Corpus;
import tcc.sistemasumarizacao.classesbase.Documento;
import tcc.sistemasumarizacao.classesbase.GrupoDocumentos;
import tcc.sistemasumarizacao.classesbase.Sentenca;
import tcc.sistemasumarizacao.cogroo.CogrooParser;
import tcc.sistemasumarizacao.sistemasumarizacaopli.SistemaSumarizacaoPLI;
import tcc.sistemasumarizacao.utils.FormaRepresentacaoConceitos;
import tcc.sistemasumarizacao.utils.MetodosPonderacaoConceitos;
import tcc.sistemasumarizacao.utils.Utils;

/**
 * @author Laerth Bruno de Brito Gomes e Hilario Tomaz Alves de Oliveira.
 * 
 * Classe que realiza o processo de sumarização usando o corpus CSTNews.
 * 
 */

public class ExecutorExperimentoApp {

  public static void main(String[] args) {
	  
	String caminhoDiretorioResumos = "D:\\Hilario\\Pesquisa\\Experimentos\\Sumarizacao\\Laerth\\ResumosTxt";
	String caminhoDiretorioCorpusCSTNews = "D:\\Hilario\\Pesquisa\\Recursos\\Sumarizacao\\Corpora\\CSTNews 6.0";
	String caminhoArquivoModeloCogroo = "recursos/models_pt_BR.xml";
	String caminhoArquivoStopWords = "recursos/Portuguese StopList.txt";
	
	File diretorioResumos = new File( caminhoDiretorioResumos );
	  
	File diretorioCorpus = new File( caminhoDiretorioCorpusCSTNews );
	
	if( !diretorioResumos.exists() ) {
		
	  diretorioResumos.mkdir();
	  
	}
	
    Corpus corpus = Utils.construirCSTNewsCorpus( diretorioCorpus );
	
    if( corpus == null ) {
    	
      throw new Error( "Erro ao carregar o corpus." );
      
    }
    
    File customPipeLineFile = new File( caminhoArquivoModeloCogroo );
    
    File stopListFile = new File( caminhoArquivoStopWords );
	
	Set<String> stopList = null;
	
	try {
		 
	 stopList = Utils.carregarStopList( stopListFile );
	
	} 
	
	catch (Exception e1) {
	
	  e1.printStackTrace();
	
	}
	
	if( stopList == null ) {
		
	  stopList = new HashSet<String>();
	  
	}
   
	CogrooParser cogrooParser = new CogrooParser( customPipeLineFile, stopList );

    AnalisadorConceitos analisadorConceitos = new AnalisadorConceitos();
    
	SistemaSumarizacaoPLI sistemaSumarizacaoPLI = 
			new SistemaSumarizacaoPLI( analisadorConceitos );
	
    System.out.println( "\n  Corpus: " + corpus.getNome() + " -- " + 
     corpus.getGrupoDocumentos().size() );

    List<FormaRepresentacaoConceitos> representacoesConceitos =
    		Arrays.asList( FormaRepresentacaoConceitos.values() );
    
    List<MetodosPonderacaoConceitos> metodosPonderacaoConceitos =
    		Arrays.asList( MetodosPonderacaoConceitos.values() );
      
    AnalisadorPLI analisadorPLI = new AnalisadorPLI();
    
    int limiarSumarizacao = 110;
    
    int contadorProgresso = 1;
    int totalGrupoDocumentos = corpus.getGrupoDocumentos().size();

    for(GrupoDocumentos grupoDocumentos: corpus.getGrupoDocumentos()) {
    	
      cogrooParser.processarGrupoDocumentos( grupoDocumentos );
    
    } // FOR GRUPO DOCUMENTOS
    
    StringBuilder nomeResumo = new StringBuilder();
    
    for(FormaRepresentacaoConceitos representacaoConceitos: representacoesConceitos) {

      for(MetodosPonderacaoConceitos metodoPonderacaoConceitos: metodosPonderacaoConceitos) {

        nomeResumo.append( representacaoConceitos.getLabel() );
        nomeResumo.append( "_" );
        nomeResumo.append( metodoPonderacaoConceitos.getLabel() );

        System.out.println( "\n    Configuração: " + nomeResumo );

        for(GrupoDocumentos grupoDocumentos: corpus.getGrupoDocumentos()) {
    	
          System.out.println( "\n      Grupo " + contadorProgresso++ + " de " + 
           totalGrupoDocumentos + " : " + grupoDocumentos.getNome() );
        
          String resumo = sistemaSumarizacaoPLI.resumirGrupoDocumentos( grupoDocumentos,
           representacaoConceitos, metodoPonderacaoConceitos, analisadorPLI, 
           limiarSumarizacao );
        		  
          System.out.println( "\n        Resumo: " + resumo.replaceAll( "\n", " " ) );
        	      
          grupoDocumentos.addResumo( nomeResumo.toString(), resumo );
          
          // Limpar dados do processo de sumarização
          
          for(Documento documento: grupoDocumentos.getDocumentos()) {
        	
        	for(Sentenca sentenca: documento.getSentencas()) {
        		
        	  sentenca.setConceitos( null );
        	  
        	  sentenca.setRelevancia( 0 );
        	  
        	} // FOR SENTENCAS
        	
            documento.setConceitos( null );
            
          } // FOR DOCUMENTOS
          
        } // FOR GRUPO DOCUMENTOS
        
        nomeResumo.setLength( 0 );

        contadorProgresso = 1;
        
      } // FOR MÉTODOS PONDERAÇÃO CONCEITOS
      
    } // FOR FORMA REPRESENTAÇÃO CONCEITOS
    
    for(GrupoDocumentos grupoDocumentos: corpus.getGrupoDocumentos()) {
    	   
      Utils.salvarResumo( grupoDocumentos, diretorioResumos );
    	
    } // FOR GRUPO DOCUMENTOS
    
  }
  
}

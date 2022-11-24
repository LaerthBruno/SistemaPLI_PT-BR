
package tcc.sistemasumarizacao.aplicacoes;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import tcc.sistemasumarizacao.analisadorconceitos.AnalisadorConceitos;
import tcc.sistemasumarizacao.analisadorpli.AnalisadorPLI;
import tcc.sistemasumarizacao.classesbase.Corpus;
import tcc.sistemasumarizacao.classesbase.GrupoDocumentos;
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

public class SistemaSumarizacaoPLIApp {

  public static void main(String[] args) {
	  
	String caminhoDiretorioResumos = "D:\\Hilario\\Pesquisa\\Experimentos\\Sumarizacao\\Laerth\\Resumos";
	String caminhoDiretorioCorpusCSTNews = "D:\\Hilario\\Pesquisa\\Recursos\\Sumarizacao\\Corpora\\CSTNews 6.0";
	String caminhoArquivoModeloCogroo = "recursos/models_pt_BR.xml";
	String caminhoArquivoStopWords = "recursos/Portuguese StopList.txt";
	
	File diretorioResumos = new File( caminhoDiretorioResumos );
	  
	File diretorioCorpus = new File( caminhoDiretorioCorpusCSTNews );
	
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

    FormaRepresentacaoConceitos formaRepresentacaoConceitos = 
    		FormaRepresentacaoConceitos.BIGRAMA; 
    
    MetodosPonderacaoConceitos metododPonderacaoConceitos = 
    		MetodosPonderacaoConceitos.POSICAO_SENTENCAS;
    
    AnalisadorPLI analisadorPLI = new AnalisadorPLI();
    
    int limiarSumarizacao = 110;
    
    int contadorProgresso = 1;
    int totalGrupoDocumentos = corpus.getGrupoDocumentos().size();
    
    for(GrupoDocumentos grupoDocumentos: corpus.getGrupoDocumentos()) {
    	
      System.out.println( "\n    Grupo " + contadorProgresso++ + " de " + 
       totalGrupoDocumentos + " : " + grupoDocumentos.getNome() );
      
      cogrooParser.processarGrupoDocumentos( grupoDocumentos );
    
      String resumo = sistemaSumarizacaoPLI.resumirGrupoDocumentos( grupoDocumentos,
       formaRepresentacaoConceitos, metododPonderacaoConceitos, analisadorPLI, 
       limiarSumarizacao );
      
      StringBuilder nomeResumo = new StringBuilder();
      
      nomeResumo.append( formaRepresentacaoConceitos.getLabel() );
      nomeResumo.append( "_" );
      nomeResumo.append( metododPonderacaoConceitos.getLabel() );
      
      System.out.println( "\n      Resumo [" + nomeResumo + "]: " + 
      resumo.replaceAll( "\n", " " ) );
      
      grupoDocumentos.addResumo( nomeResumo.toString(), resumo );
      
      Utils.salvarResumo( grupoDocumentos, diretorioResumos );
      
    }
    
  }
  
}

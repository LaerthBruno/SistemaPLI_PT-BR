
package tcc.sistemasumarizacao.sistemasumarizacaopli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import tcc.sistemasumarizacao.analisadorconceitos.AnalisadorConceitos;
import tcc.sistemasumarizacao.analisadorpli.AnalisadorPLI;
import tcc.sistemasumarizacao.classesbase.Documento;
import tcc.sistemasumarizacao.classesbase.GrupoDocumentos;
import tcc.sistemasumarizacao.classesbase.Sentenca;
import tcc.sistemasumarizacao.classesbase.Token;
import tcc.sistemasumarizacao.utils.FormaRepresentacaoConceitos;
import tcc.sistemasumarizacao.utils.MetodosPonderacaoConceitos;
import tcc.sistemasumarizacao.utils.Utils;


/**
 * @author Laerth Bruno de Brito Gomes e Hilario Tomaz Alves de Oliveira.
 * 
 * Classe que realiza o processo de sumarização usando o corpus CSTNews.
 * 
 */

public class SistemaSumarizacaoPLI {

  // Atributos
	
  private AnalisadorConceitos analisadorConceitos;
  
  
  // Construtor
  
  public SistemaSumarizacaoPLI(AnalisadorConceitos analisadorConceitos) {
	
    this.analisadorConceitos = analisadorConceitos;
    
  }

  
  // Métodos
  
  public String resumirGrupoDocumentos(GrupoDocumentos grupoDocumentos, 
   FormaRepresentacaoConceitos formaRepresentacaoConceitos, 
   MetodosPonderacaoConceitos metododPonderacaoConceitos, AnalisadorPLI analisadorPLI, 
   int limiarSumarizacao) {
	
	// 1ª Extrair os Conceitos (unigramas, bigramas ou uni_bi)
	
	this.analisadorConceitos.extrairConceitos( grupoDocumentos, formaRepresentacaoConceitos );
	
	/* 2ª Ponderar os pesos dos conceitos (frequencia dos documentos, posicao das sentenças 
	 * ou combinados) */
	  
	this.analisadorConceitos.ponderarPesosConceitos( grupoDocumentos, 
			metododPonderacaoConceitos );
	
	// 3ª Selecionar as sentenças usando o modelo de PLI
	  
	List<Sentenca> sentencas = new ArrayList<Sentenca>();
	
	for(Documento documento: grupoDocumentos.getDocumentos()) {
		
	  for(Sentenca sentenca: documento.getSentencas()) {
		
        if( sentenca.getConceitos() != null ) {
		
		  sentencas.add( sentenca );
		
        }
		
	  } // FOR SENTENCAS
	
	} // FOR DOCUMENTOS
	
	HashMap<Token, ArrayList<Sentenca>> mapaConceitosSentencas = 
			Utils.montarMapConceitosSentencas( grupoDocumentos );	
			
	List<Sentenca> sentencasSelecionadas = 
			analisadorPLI.selecionarSentencasPLI( mapaConceitosSentencas, sentencas, 
					limiarSumarizacao );
	
	// 4ª Montar e retornar o resumo
	
	if( sentencasSelecionadas != null ) {
	  
	  Collections.sort( sentencasSelecionadas );
	  
	  StringBuilder resumo = new StringBuilder();
	  
	  for(Sentenca sentenca: sentencasSelecionadas) {
			
		resumo.append( sentenca.getTexto() );
		resumo.append( "\n" );
		
	  } // FOR SENTENCAS
		
	  resumo.setLength( resumo.length() - 1 );
	  
	  return resumo.toString();
	  
	}
	
    return null;

  }

}

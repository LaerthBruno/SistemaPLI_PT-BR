
package tcc.sistemasumarizacao.utils;

import java.util.Comparator;

import tcc.sistemasumarizacao.classesbase.Sentenca;

public class ComparadorSentencasPorRelevancia implements Comparator<Sentenca> {

  @Override
	
  public int compare(Sentenca sentenca1, Sentenca sentenca2) {
	
    return Double.compare( sentenca2.getRelevancia(), sentenca1.getRelevancia() );
	
  }

}

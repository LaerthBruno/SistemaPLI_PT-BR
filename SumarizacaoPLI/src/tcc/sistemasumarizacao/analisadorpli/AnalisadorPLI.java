package tcc.sistemasumarizacao.analisadorpli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import net.sf.javailp.Constraint;
import net.sf.javailp.Linear;
import net.sf.javailp.OptType;
import net.sf.javailp.Problem;
import net.sf.javailp.Result;
import net.sf.javailp.Solver;
import net.sf.javailp.SolverFactory;
import net.sf.javailp.SolverFactoryGLPK;
import tcc.sistemasumarizacao.classesbase.Sentenca;
import tcc.sistemasumarizacao.classesbase.Token;

/**
 * @author Laerth Bruno de Brito Gomes e Hilario Tomaz Alves de Oliveira.
 * 
 * Classe que realiza a manipulação do modelo PLI.
 * 
 */

public class AnalisadorPLI {

  // Construtor
	
  public AnalisadorPLI() {

  }
  
  // Métodos
  
 public List<Sentenca> selecionarSentencasPLI(HashMap<Token, ArrayList<Sentenca>> mapaConceitosSentencas, 
  List<Sentenca> sentencas, int limiarSumarizacao) {
					
   // Criação do Problema
	 
   Problem problem = new Problem();
	
   // Criação da Função Objetivo
	
   Linear objectiveFunction = new Linear();

   int idConcept = 1;
	
   for(Token conceito: mapaConceitosSentencas.keySet()) {
							  
     conceito.setId( idConcept++ );
	
     String label = "c_" + conceito.getId();
	
     objectiveFunction.add( conceito.getRelevancia(), label );

     problem.setVarLowerBound( label, 0 );
     problem.setVarUpperBound( label, 1 );

     problem.setVarType( label, Integer.class );
	
   } // FOR CONCEITOS
   
   problem.setObjective( objectiveFunction, OptType.MAX );

   // Restrição do Tamanho do Resumo
	
   Linear constraintSentencaLength = new Linear();
	
   for(Sentenca sentenca: sentencas) {
	   
     String labelSentenca = "s_" + sentenca.getIdString();
	
     constraintSentencaLength.add( sentenca.getTokens().size(), labelSentenca );
	
     problem.setVarLowerBound( labelSentenca, 0 );
	 problem.setVarUpperBound( labelSentenca, 1 );
	
	 problem.setVarType( labelSentenca, Integer.class );

   } // FOR SENTECAS

   problem.add( new Constraint( "Constraint_SummaryLength", constraintSentencaLength, "<=", limiarSumarizacao ) );
	
   /*
	 *  Restrições que garantem a integridade do modelo.
	 *  
   */
	
   int idRestricao = 1;
   
   for(Entry<Token, ArrayList<Sentenca>> entry: mapaConceitosSentencas.entrySet()) {
	
     Linear constraintConceptSentencas = new Linear();
	
     String labelConcept = "c_" + entry.getKey().getId();
	
     constraintConceptSentencas.add( -1, labelConcept );
	
     Set<String> sentencasIds = new HashSet<String>();
	
     for(int i = 0; i < entry.getValue().size(); i++) {
	
       Sentenca sentenca = entry.getValue().get( i );      
		
       if( sentencasIds.contains( sentenca.getIdString() ) ) {
						        	
	     continue;
		
       }

       sentencasIds.add( sentenca.getIdString() );
		
       Linear constraintConceptSentenca = new Linear();

       String labelSentenca = "s_" + sentenca.getIdString();
		
       constraintConceptSentenca.add( -1, labelConcept );
       constraintConceptSentenca.add( 1, labelSentenca );

       problem.add( new Constraint( "Constraint_ConceptSentenca_" + idRestricao++, constraintConceptSentenca, "<=", 0 ) );
		
       constraintConceptSentencas.add( 1, labelSentenca );
		
     } // FOR SentencaS
	
     problem.add( new Constraint( "Constraint_ConceptSentencas_" + idRestricao++, constraintConceptSentencas, ">=", 0 ) );
	
   } // FOR MAP Conceitos e SentencaS

   // Criação do Solver
	
   SolverFactory factory = new SolverFactoryGLPK();
	
   factory.setParameter( Solver.VERBOSE, 0 ); 
   factory.setParameter( Solver.TIMEOUT, 100 );

   Solver solver = factory.get();
	
   // Obtenção do resultado do modelo.
	
   Result result = solver.solve( problem );

   List<Sentenca> sentencasSelecionadas = new ArrayList<Sentenca>();
	
   // Obter Sentenças Selecionadas na Solução.

   for(Sentenca sentenca: sentencas) {
	
     Number number = result.get( "s_" + sentenca.getIdString() );
	
     if( number.intValue() == 1 ) {

    	 sentenca.setRelevancia( result.getObjective().doubleValue() );
		
    	 sentencasSelecionadas.add( sentenca );
		
     }
	
   } // FOR SentencaS

   return sentencasSelecionadas.isEmpty() ? null : sentencasSelecionadas;
	
 }

}

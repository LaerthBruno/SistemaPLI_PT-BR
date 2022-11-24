package tcc.sistemasumarizacao.classesbase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Laerth Bruno de Brito Gomes e Hilario Tomaz Alves de Oliveira.
 * 
 * Classe que representa um Corpus.
 * 
 */

public class Corpus {

  // Atributos
	
  private long id;
  private String nome;	
  private List<GrupoDocumentos> grupoDocumentos;
	
  
  // Construtores
  
  public Corpus() {
	
  }
	
  public Corpus(String nome) {

   this.nome = nome;
 
  }

  
  // Métodos
  
  public long getId() {
	  
    return id;
    
  }

  public void setId(long id) {
  
    this.id = id;
	
  }

  public String getNome() {
	  
    return nome;
	
  }

  public void setNome(String nome) {

    this.nome = nome;
   
  }

  public List<GrupoDocumentos> getGrupoDocumentos() {
		
    return grupoDocumentos;
	
  }

  public void setGrupoDocumentos(List<GrupoDocumentos> grupoDocumentos) {
	
    this.grupoDocumentos = grupoDocumentos;
	
  }
	
  public boolean addGrupoDocumentos(GrupoDocumentos grupoDocumentos){
	  
	if(this.grupoDocumentos == null){
		
	  this.grupoDocumentos = new ArrayList<GrupoDocumentos>();
		
	}
	
	return this.grupoDocumentos.add(grupoDocumentos);
	
  }
  
  @Override
	
  public String toString() {
	
	return this.getNome();
	
  }
  
}

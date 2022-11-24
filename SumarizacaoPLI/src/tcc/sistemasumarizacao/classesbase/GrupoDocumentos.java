package tcc.sistemasumarizacao.classesbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Laerth Bruno de Brito Gomes e Hilario Tomaz Alves de Oliveira.
 * 
 * Classe que representa uma coleção de documentos.
 * 
 */

public class GrupoDocumentos {
	
  // Atributos
	
  private long id;
  private String nome;	
  private List<Documento> documentos;
  private List<String> resumosReferencia;
  private Map<String, String> mapResumos;
	
  
  // Construtor
  
  public GrupoDocumentos(String nome) {

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

  public List<Documento> getDocumentos() {
		
	return documentos;
	
  }

  public void setDocumentos(List<Documento> documentos) {
		
	this.documentos = documentos;
	
  }
  
  public boolean addDocumento(Documento documento){
		
	if(this.documentos == null){
		  
	  this.documentos = new ArrayList<Documento>();
		
	}
	
	return this.documentos.add(documento);
	
  }
	
  public List<String> getResumosReferencia() {
		
    return resumosReferencia;
	  
  }

  public void setResumosReferencia(List<String> resumosReferencia) {
		
	this.resumosReferencia = resumosReferencia;
	
  }
  
  public Map<String, String> getMapResumos() {
	
    return mapResumos;

  }

  public void setMapResumos(Map<String, String> mapResumos) {
	
    this.mapResumos = mapResumos;

  }
	
  public boolean addResumoReferencia(String resumoReferencia){
		
	if(this.resumosReferencia == null){
		  
	  this.resumosReferencia = new ArrayList<String>();
		
	}
	
	return this.resumosReferencia.add( resumoReferencia );
	
  }
  
  public void addResumo(String nome, String resumo) {
	  
    if( this.mapResumos == null ) {
    	
      this.mapResumos = new HashMap<String, String>();
      
    }
    
    this.mapResumos.put( nome, resumo );
    
  }  
  
  @Override
	
  public String toString() {
	
    return this.getNome();
    
  }
	
}

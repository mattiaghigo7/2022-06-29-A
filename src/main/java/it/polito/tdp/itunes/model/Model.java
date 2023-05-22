package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private List<Album> allAlbum;
	private SimpleDirectedWeightedGraph<Album, DefaultWeightedEdge> graph;
	private ItunesDAO dao;
	private List<Album> migliore;
	private int bestScore;
	
	public Model() {
		this.dao = new ItunesDAO();
	}
	
	public List<Album> getPath(Album a1, Album a2, int n){
		List<Album> parziale = new ArrayList<>();
		migliore = new ArrayList<>();
		bestScore = 0;
		parziale.add(a1);
		ricorsione(parziale, a2, n);
		return migliore;
	}
	
	private void ricorsione(List<Album> parziale, Album a2, int n) {
		Album corrente = parziale.get(parziale.size()-1);
		//condizione di uscita
		if(corrente.equals(a2)) {
			//controllo se questa soluzione è migliore del best
			if(getScore(parziale)>this.bestScore) {
				this.migliore=new ArrayList<>(parziale);
				this.bestScore=getScore(parziale);
			}
			return;
		}
		//continuo ad aggiungere elementi in parziale
		List<Album> successori = Graphs.successorListOf(this.graph, corrente);
		for(Album a : successori) {
			if(this.graph.getEdgeWeight(this.graph.getEdge(corrente, a))>=n) {
				parziale.add(a);
				ricorsione(parziale,a2,n);
				parziale.remove(a);
			}
		}
	}

	private int getScore(List<Album> parziale) {
		Album source = parziale.get(0);
		int score = 0;
		for(Album a : parziale) {
			if(getBilancio(a)>getBilancio(source)) {
				score++;
			}
		}
		return score;
	}

	public void buildGraph(int n) {
		this.allAlbum = new ArrayList<>();
		this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.loadNodes(n);
		Graphs.addAllVertices(this.graph, this.allAlbum);
		System.out.println(this.graph.vertexSet().size());
		
		for(Album a1 : this.allAlbum) {
			for(Album a2 : this.allAlbum) {
				int peso = a1.getNumSongs()-a2.getNumSongs();
				if(peso>0) {
					Graphs.addEdge(this.graph,a2,a1,peso);
				}
			}
		}
		System.out.println(this.graph.edgeSet().size());
	}
	
	public List<bilancioAlbum> getAdiacenti(Album a){
		List<Album> successori = Graphs.successorListOf(this.graph, a);
		List<bilancioAlbum> bilancioSuccessori = new ArrayList<>();
		for(Album al : successori) {
			bilancioSuccessori.add(new bilancioAlbum(al,getBilancio(al)));
		}
		bilancioSuccessori.sort(null);
		return bilancioSuccessori;
	}
	
	private int getBilancio(Album a) {
		int bilancio = 0;
		List<DefaultWeightedEdge> edgesIn = new ArrayList<>(this.graph.incomingEdgesOf(a));
		List<DefaultWeightedEdge> edgesOut = new ArrayList<>(this.graph.outgoingEdgesOf(a));
		for(DefaultWeightedEdge edge : edgesIn) {
			bilancio += this.graph.getEdgeWeight(edge);
		}
		for(DefaultWeightedEdge edge : edgesOut) {
			bilancio -= this.graph.getEdgeWeight(edge);
		}
		return bilancio;
	}
	
	public List<Album> getVertices(){
		List<Album> allVertices = new ArrayList<>(this.graph.vertexSet());
		allVertices.sort(null);
		return allVertices;
	}
	
	private void loadNodes(int n) {
		if(this.allAlbum.isEmpty()) {
			this.allAlbum = dao.getFilteredAlbums(n);
		}
	}

	public int getNumVertices() {
		return this.graph.vertexSet().size();
	}

	public int getNumEdges() {
		return this.graph.edgeSet().size();
	}
}
/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Model;
import it.polito.tdp.itunes.model.bilancioAlbum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAdiacenze"
    private Button btnAdiacenze; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA2"
    private ComboBox<Album> cmbA2; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doCalcolaAdiacenze(ActionEvent event) {
    	Album album = this.cmbA1.getValue();
    	if(album==null) {
    		this.txtResult.setText("Please select an element from combo box.\n");
    		return;
    	}
    	List<bilancioAlbum> bilanci = model.getAdiacenti(album);
    	this.txtResult.setText("Printing successors of node "+album+"\n");
    	for(bilancioAlbum b : bilanci) {
    		this.txtResult.appendText(b+"\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	String input = this.txtX.getText();
    	if(input=="") {
    		this.txtResult.setText("Input string for X is empty");
    		return;
    	}
    	try {
    		Integer inputNum = Integer.parseInt(input);
    		Album a1 = this.cmbA1.getValue();
        	Album a2 = this.cmbA2.getValue();
        	if(a1==null || a2==null) {
        		this.txtResult.setText("Please select elements from combo box.\n");
        		return;
        	}
        	List<Album> path = model.getPath(a1, a2, inputNum);
        	if(path.isEmpty()) {
        		this.txtResult.setText("No path between "+a1+" and "+a2);
        		return;
        	}
        	this.txtResult.setText("Printing path between "+a1+" and "+a2+"\n");
        	for(Album a : path) {
        		this.txtResult.appendText(a+"\n");
        	}
    	} catch (NumberFormatException e) {
    		this.txtResult.setText("Input string for X is not a valid number");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String input = this.txtN.getText();
    	if(input=="") {
    		this.txtResult.setText("Input string for N is empty");
    		return;
    	}
    	try {
    		Integer inputNum = Integer.parseInt(input);
    		model.buildGraph(inputNum);
    		int numV = model.getNumVertices();
    		int numE = model.getNumEdges();
    		this.txtResult.setText("Graph correctly created.\n");
    		this.txtResult.appendText("Number vertices: "+numV+"\n");
    		this.txtResult.appendText("Number edges: "+numE+"\n");
    		this.cmbA1.getItems().setAll(model.getVertices());
    		this.cmbA2.getItems().setAll(model.getVertices());
    	} catch (NumberFormatException e) {
    		this.txtResult.setText("Input string for N is not a valid number");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdiacenze != null : "fx:id=\"btnAdiacenze\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA2 != null : "fx:id=\"cmbA2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }

    
    public void setModel(Model model) {
    	this.model = model;
    }
}

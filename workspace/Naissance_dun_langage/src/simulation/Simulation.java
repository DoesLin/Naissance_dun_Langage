package simulation;

import java.awt.Frame;
import java.io.IOException;

import javax.swing.JOptionPane;

import simulation.ihm.Controleur;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import simulation.systeme.lecture.LecteurConfigurationSysteme;
import simulation.systeme.Systeme;

/**
 * Classe principale de l'application
 * 
 * Elle recupere les differentes ressources du projet, configure le systeme,
 * puis lance la simulation et affiche les resultats dans une IHM
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class Simulation extends Application {
	
	private static final String urlConfig = "config/config.xml";
	
	private static final String urlFXLM = "resources/scene.fxml";
	private static final String urlCSS = "resources/scene.css";
	private static final String urlIcon = "simulation/resources/java-icon.png";
	
	private static final String titreApp = "Naissance d'un langage";

	private Controleur controleur;
	LecteurConfigurationSysteme lecteurConfigSysteme;
	
	/**
	 * Methode main habituelle, pouvant prendre des arguments en parametre
	 * Actuellement, aucun argument n'est considere
	 * 
	 * @param args	les arguments pour l'application
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage scene) {
		try {
			lecteurConfigSysteme = new LecteurConfigurationSysteme();
			Systeme.configurer(lecteurConfigSysteme.lireConfigSystemeDepuisFichier(urlConfig));
			Systeme.lancer();
			
			// Lancement de l'IHM
		
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource(urlFXLM));   
	        
	        BorderPane root = null;
	        try {
				root = (BorderPane)loader.load();
			}
			catch (IOException exception) {
				exception.printStackTrace();
			}
	        
			Scene vue = new Scene(root);
			vue.getStylesheets().add(getClass().getResource(urlCSS).toExternalForm());
			scene.setScene(vue);
			
			controleur = (Controleur)loader.getController();
			controleur.lancerListeners();

			scene.getIcons().add(new Image(urlIcon));
			scene.setTitle(titreApp);
			scene.show();
		}
		catch (Exception exception) {
			JOptionPane.showMessageDialog(new Frame(), exception.getMessage(), exception.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
}
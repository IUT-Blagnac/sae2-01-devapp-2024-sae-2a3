package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.CompteEditorPaneViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.data.CompteCourant;

/**
 * Classe responsable de la gestion de la fenêtre d'édition des comptes dans
 * l'application DailyBank.
 * 
 * @see CompteEditorPaneViewController
 * @author IUT Blagnac
 */
public class CompteEditorPane {

	private Stage cepStage;
	private CompteEditorPaneViewController cepViewController;

	/**
	 * Classe responsable de la gestion de la fenêtre d'édition des comptes dans
	 * l'application DailyBank.
	 * 
	 * @see CompteEditorPaneViewController
	 * @author IUT Blagnac
	 */
	public CompteEditorPane(Stage _parentStage, DailyBankState _dbstate) {

		try {
			FXMLLoader loader = new FXMLLoader(CompteEditorPaneViewController.class.getResource("compteeditorpane.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.cepStage = new Stage();
			this.cepStage.initModality(Modality.WINDOW_MODAL);
			this.cepStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.cepStage);
			this.cepStage.setScene(scene);
			this.cepStage.setTitle("Gestion d'un compte");
			this.cepStage.setResizable(false);

			this.cepViewController = loader.getController();
			this.cepViewController.initContext(this.cepStage, _dbstate);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Affiche la fenêtre d'édition d'un compte.
	 *
	 * @param client Le client associé au compte
	 * @param cpte   Le compte à éditer
	 * @param em     Le mode d'édition du compte
	 * @return Le compte courant modifié
	 * @author IUT Blagnac
	 */
	public CompteCourant doCompteEditorDialog(Client client, CompteCourant cpte, EditionMode em) {
		return this.cepViewController.displayDialog(client, cpte, em);
	}
}

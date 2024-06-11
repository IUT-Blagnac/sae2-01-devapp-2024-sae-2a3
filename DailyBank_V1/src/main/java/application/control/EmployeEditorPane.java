package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.EmployeEditorPaneViewController;
import application.view.EmployesManagementViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Employe;

/**
 * Classe responsable de la gestion de la fenêtre d'édition d'un employé dans
 * l'application DailyBank.
 * 
 * @see EmployeEditorPaneViewController
 */
public class EmployeEditorPane {

	/**
	 * Constructeur de la classe EmployeEditorPane.
	 *
	 * @param _parentStage Fenêtre parente
	 * @param _dbstate     État courant de l'application
	 * @author AMERI Mohammed
	 */
	private Stage cepStage;
	private EmployeEditorPaneViewController cepViewController;
	private DailyBankState dailyBankState;

	public EmployeEditorPane(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(EmployesManagementViewController.class.getResource("employeeditorpane.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.cepStage = new Stage();
			this.cepStage.initModality(Modality.WINDOW_MODAL);
			this.cepStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.cepStage);
			this.cepStage.setScene(scene);
			this.cepStage.setTitle("Gestion d'un employe");
			this.cepStage.setResizable(false);

			this.cepViewController = loader.getController();
			this.cepViewController.initContext(this.cepStage, this.dailyBankState);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la boîte de dialogue d'édition du employé.
	 *
	 * @param employe L'employé à éditer
	 * @param em      Le mode d'édition (ajout ou modification)
	 * @return L'employé édité
	 * @author AMERI Mohammed
	 */
	public Employe doEmployeEditorDialog(Employe employe, EditionMode em) {
		return this.cepViewController.displayDialog(employe, em);
	}
}

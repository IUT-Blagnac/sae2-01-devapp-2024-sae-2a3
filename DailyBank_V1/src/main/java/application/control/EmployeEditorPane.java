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

	
	private Stage eepStage;
	private EmployeEditorPaneViewController eepViewController;
	private DailyBankState dailyBankState;

	/**
	 * Constructeur de la classe EmployeEditorPane.
	 *
	 * @param _parentStage Fenêtre parente
	 * @param _dbstate     État courant de l'application
	 * @author AMERI Mohammed
	 */
	public EmployeEditorPane(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(EmployesManagementViewController.class.getResource("employeeditorpane.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.eepStage = new Stage();
			this.eepStage.initModality(Modality.WINDOW_MODAL);
			this.eepStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.eepStage);
			this.eepStage.setScene(scene);
			this.eepStage.setTitle("Gestion d'un employe");
			this.eepStage.setResizable(false);

			this.eepViewController = loader.getController();
			this.eepViewController.initContext(this.eepStage, this.dailyBankState);

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
		return this.eepViewController.displayDialog(employe, em);
	}
}

package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.PrelevementEditorController;
import application.view.PrelevementManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.CompteCourant;
import model.data.Prelevement;

/**
 * Classe responsable de la gestion de la fenêtre d'édition d'un prélèvemebt
 * automatique dans l'application DailyBank.
 * 
 * @see PrelevementEditorController
 */
public class PrelevementEditorPane {

	private Stage pepStage;
	private PrelevementEditorController pepViewController;
	private DailyBankState dailyBankState;

	/**
	 * Constructeur de la classe ClientEditorPane.
	 *
	 * @param _parentStage Fenêtre parente
	 * @param _dbstate     État courant de l'application
	 */
	public PrelevementEditorPane(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(
					PrelevementManagementController.class.getResource("prelevementEditor.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource(dailyBankState.getThemeActuel().getCssFile()).toExternalForm());

			this.pepStage = new Stage();
			this.pepStage.initModality(Modality.WINDOW_MODAL);
			this.pepStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.pepStage);
			this.pepStage.setScene(scene);
			this.pepStage.setTitle("Gestion d'un prélèvement");
			this.pepStage.setResizable(false);

			this.pepViewController = loader.getController();
			this.pepViewController.initContext(this.pepStage, this.dailyBankState);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la boîte de dialogue d'édition d'un prélèvement.
	 *
	 * @param pm Le prélèvement à éditer
	 * @param em Le mode d'édition (ajout ou modification)
	 * @return Le prélèvement modifié
	 */
	public Prelevement doPrelevementEditorDialog(Prelevement pm, EditionMode em, CompteCourant cc) {
		return this.pepViewController.displayDialog(pm, em, cc);
	}
}

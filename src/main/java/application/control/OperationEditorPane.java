package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.CategorieOperation;
import application.tools.StageManagement;
import application.view.OperationEditorPaneViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.CompteCourant;
import model.data.Operation;

public class OperationEditorPane {

	private Stage oepStage;
	private OperationEditorPaneViewController oepViewController;

	public OperationEditorPane(Stage _parentStage, DailyBankState _dbstate) {

		try {
			FXMLLoader loader = new FXMLLoader(
					OperationEditorPaneViewController.class.getResource("operationeditorpane.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, 500 + 20, 250 + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.oepStage = new Stage();
			this.oepStage.initModality(Modality.WINDOW_MODAL);
			this.oepStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.oepStage);
			this.oepStage.setScene(scene);
			this.oepStage.setTitle("Enregistrement d'une opération");
			this.oepStage.setResizable(false);

			this.oepViewController = loader.getController();
			this.oepViewController.initContext(this.oepStage, _dbstate);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Operation doOperationEditorDialog(CompteCourant cpte, CategorieOperation cm) {
		return this.oepViewController.displayDialog(cpte, cm);
	}
}

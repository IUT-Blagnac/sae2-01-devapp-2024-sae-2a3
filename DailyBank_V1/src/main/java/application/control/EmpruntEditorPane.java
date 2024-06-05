package application.control;

import java.io.IOException;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.CategorieOperation;
import application.tools.StageManagement;
import application.view.EmpruntEditorPaneViewController;
import application.view.OperationEditorPaneViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.CompteCourant;
import model.data.Operation;

public class EmpruntEditorPane {
    private Stage oepStage;
	private EmpruntEditorPaneViewController oepViewController;

	public EmpruntEditorPane(Stage _parentStage, DailyBankState _dbstate) {
        try {
			FXMLLoader loader = new FXMLLoader(OperationEditorPaneViewController.class.getResource("emprunteditorpane.fxml"));
			Scene scene = new Scene(loader.load());
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

            this.oepStage = new Stage();
            this.oepStage.initModality(Modality.WINDOW_MODAL);
            this.oepStage.initOwner(_parentStage);
            this.oepStage.setScene(scene);
            this.oepStage.setTitle("Simulation d'emprunt");

            this.oepViewController = loader.getController();
            this.oepViewController.initContext(this.oepStage, _dbstate);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doEmpruntSimulationDialog() {
        this.oepStage.showAndWait();
    }
}

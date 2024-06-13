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

/**
 * Classe responsable de la gestion de la fenêtre d'emprunt 
 * 
 * @see EmpruntEditorPaneViewController 
 */
public class EmpruntEditorPane {
    private Stage eepStage;
	private EmpruntEditorPaneViewController eepViewController;

    /**
     * Constructeur de la classe EmpruntEditorPane 
     * 
     * @param _parentStage Fenêtre parent
     * @param _dbstate Etat courant de l'application 
     * @author AMERI Mohammed
     */
	public EmpruntEditorPane(Stage _parentStage, DailyBankState _dbstate) {
        try {
			FXMLLoader loader = new FXMLLoader(OperationEditorPaneViewController.class.getResource("emprunteditorpane.fxml"));
			Scene scene = new Scene(loader.load());
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

            this.eepStage = new Stage();
            this.eepStage.initModality(Modality.WINDOW_MODAL);
            this.eepStage.initOwner(_parentStage);
            this.eepStage.setScene(scene);
            this.eepStage.setTitle("Simulation d'emprunt/assurance");
            this.eepStage.setResizable(false);

            this.eepViewController = loader.getController();
            this.eepViewController.initContext(this.eepStage, _dbstate);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche la boîte de dialogue d'édition des emprunts
     * 
     * @author AMERI Mohammed
     */
    public void doEmpruntSimulationDialog() {
        this.eepStage.showAndWait();
    }
}

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
    private Stage empStage;
	private EmpruntEditorPaneViewController empViewController;

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

            this.empStage = new Stage();
            this.empStage.initModality(Modality.WINDOW_MODAL);
            this.empStage.initOwner(_parentStage);
            this.empStage.setScene(scene);
            this.empStage.setTitle("Simulation d'emprunt/assurance");
            this.empStage.setResizable(false);

            this.empViewController = loader.getController();
            this.empViewController.initContext(this.empStage, _dbstate);

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
        this.empStage.showAndWait();
    }
}

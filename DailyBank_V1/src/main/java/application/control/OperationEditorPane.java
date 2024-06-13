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

/**
 * Classe de contrôleur pour la boîte de dialogue d'édition des opérations.
 * 
 * @see OperationEditorPaneViewController
 * @author IUT Blagnac
 */
public class OperationEditorPane {

	private Stage oepStage;
	private OperationEditorPaneViewController oepViewController;
	/**
	 * Constructeur de la classe OperationEditorPane.
	 * 
	 * @param _parentStage Le stage parent
	 * @param _dbstate     L'état courant de l'application
	 * @author IUT Blagnac
	 */
	public OperationEditorPane(Stage _parentStage, DailyBankState _dbstate) {

		try {
			FXMLLoader loader = new FXMLLoader(
					OperationEditorPaneViewController.class.getResource("operationeditorpane.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, 520, 350);
			scene.getStylesheets().add(DailyBankApp.class.getResource(_dbstate.getThemeActuel().getCssFile()).toExternalForm());

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

	/**
	 * Affiche la boîte de dialogue d'édition des opérations.
	 * 
	 * @param cpte Le compte courant associé à l'opération
	 * @param cm   La catégorie de l'opération
	 * @return L'opération créée ou modifiée, ou null si aucune opération n'a été
	 *         enregistrée
	 * @author IUT Blagnac
	 */
	public Operation doOperationEditorDialog(CompteCourant cpte, CategorieOperation cm) {
        return this.oepViewController.displayDialog(cpte, cm);
	}
}

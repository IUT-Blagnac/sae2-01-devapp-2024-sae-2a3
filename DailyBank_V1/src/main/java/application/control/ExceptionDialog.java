package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.StageManagement;
import application.view.ExceptionDialogViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.orm.exception.ApplicationException;

/**
 * Classe de contrôleur pour la boîte de dialogue des exceptions.
 * 
 * @see ExceptionDialogViewController
 * @author IUT Blagnac
 */
public class ExceptionDialog {

	private Stage edStage;
	private ExceptionDialogViewController edViewController;

	/**
	 * Constructeur de la classe ExceptionDialog.
	 * 
	 * @param _parentStage Le stage parent
	 * @param _dbstate     L'état courant de l'application
	 * @param ae           L'exception applicative
	 * @author IUT Blagnac
	 */
	public ExceptionDialog(Stage _parentStage, DailyBankState _dbstate, ApplicationException ae) {

		try {
			FXMLLoader loader = new FXMLLoader(ExceptionDialogViewController.class.getResource("exceptiondialog.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, 700 + 20, 400 + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource(_dbstate.getThemeActuel().getCssFile()).toExternalForm());

			this.edStage = new Stage();
			this.edStage.initModality(Modality.WINDOW_MODAL);
			this.edStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.edStage);
			this.edStage.setScene(scene);
			this.edStage.setTitle("Opération impossible");
			this.edStage.setResizable(false);

			this.edViewController = loader.getController();
			this.edViewController.initContext(this.edStage, _dbstate, ae);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la boîte de dialogue des exceptions.
	 * 
	 * @author IUT Blagnac
	 */
	public void doExceptionDialog() {
		this.edViewController.displayDialog();
	}

	/*
	 * Test : ApplicationException ae = new ApplicationException(Table.NONE,
	 * Order.OTHER, "M", null ); ExceptionDialogTemp ed = new
	 * ExceptionDialogTemp(aStage, dbs, ae); ed.doExceptionDisplay();
	 */
}

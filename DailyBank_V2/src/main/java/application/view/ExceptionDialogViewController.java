package application.view;

import java.io.PrintWriter;
import java.io.StringWriter;

import application.DailyBankState;
import application.control.ExceptionDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.orm.exception.ApplicationException;

/**
 * Contrôleur pour la boîte de dialogue des exceptions.
 * 
 * @see ExceptionDialog
 * @author IUT Blagnac
 */
public class ExceptionDialogViewController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage containingStage;

	// Données de la fenêtre
	private ApplicationException aException;

	// Manipulation de la fenêtre

	/**
	 * Initialise le contexte du contrôleur.
	 * 
	 * @param _containingStage Le stage contenant la scène
	 * @param _dbstate         L'état courant de l'application
	 * @param _ae              L'exception applicative
	 * @author IUT Blagnac
	 */
	public void initContext(Stage _containingStage, DailyBankState _dbstate, ApplicationException _ae) {
		this.containingStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.aException = _ae;
		this.configure();
	}

	
	/**
	 * Configure les composants de la fenêtre.
	 * 
	 * @author IUT Blagnac
	 */
	private void configure() {
		this.containingStage.setOnCloseRequest(e -> this.closeWindow(e));
		this.lblTitre.setText(this.aException.getMessage());
		this.txtTable.setText(this.aException.getTableName().toString());
		this.txtOpe.setText(this.aException.getOrder().toString());
		this.txtException.setText(this.aException.getClass().getName());
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		this.aException.printStackTrace(pw);
		this.txtDetails.setText(sw.toString());
	}

	/**
	 * Affiche la boîte de dialogue des exceptions.
	 * 
	 * @author IUT Blagnac
	 */
	public void displayDialog() {
		this.containingStage.showAndWait();
	}

	// Gestion du stage
	/**
	 * Ferme la fenêtre.
	 * 
	 * @param e L'événement de fermeture
	 * @return Object null
	 * @author IUT Blagnac
	 */
	private Object closeWindow(WindowEvent e) {
		return null;
	}

	// Attributs de la scene + actions

	@FXML
	private Label lblTitre;
	@FXML
	private TextField txtTable;
	@FXML
	private TextField txtOpe;
	@FXML
	private TextField txtException;
	@FXML
	private TextArea txtDetails;

	/**
	 * Gère la fermeture de la fenêtre (bouton FXML).
	 * 
	 * @author IUT Blagnac
	 */
	@FXML
	private void doOK() {
		this.containingStage.close();
	}
}

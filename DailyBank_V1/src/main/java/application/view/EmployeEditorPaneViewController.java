package application.view;

import java.util.regex.Pattern;

import application.DailyBankState;
import application.control.ExceptionDialog;
import application.tools.AlertUtilities;
import application.tools.ConstantesIHM;
import application.tools.EditionMode;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Employe;
import model.orm.exception.ApplicationException;
import model.orm.exception.Order;
import model.orm.exception.Table;

/**
 * Contrôleur pour l'édition des informations employé dans une fenêtre.
 * 
 * @see EmployeEditorPane
 */
public class EmployeEditorPaneViewController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage containingStage;

	// Données de la fenêtre

	private Employe employeEdite;
	private EditionMode editionMode;
	private Employe employeResultat;

	// Manipulation de la fenêtre
	/**
	 * Initialise le contexte du contrôleur.
	 * 
	 * @param _containingStage Le stage contenant la scène
	 * @param _em              Le contrôleur de dialogue associé
	 * @param _dbstate         L'état courant de l'application
	 */
	public void initContext(Stage _containingStage, DailyBankState _dbstate) {
		this.containingStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	private void configure() {
		this.containingStage.setOnCloseRequest(e -> this.closeWindow(e));
	}
	 /**
	  * Affiche la boîte de dialogue d'édition des informations employé.
	  * @param employe L'employé à éditer (null pour une création)
	  * @param mode Le mode d'édition (création, modification, suppression)
	  * @return L'employé résultat après l'édition, ou null si aucune édition n'a été
	 *         effectuée
	  */
	public Employe displayDialog(Employe employe, EditionMode mode) {

		this.editionMode = mode;
		if (employe == null) {
			this.employeEdite = new Employe(0, "", "", "", "", "N", this.dailyBankState.getEmployeActuel().idAg);
		} else {
			this.employeEdite = new Employe(employe);
		}
		this.employeResultat = null;
		switch (mode) {
		case CREATION:
			this.txtIdEmp.setDisable(true);
			this.txtNom.setDisable(false);
			this.txtPrenom.setDisable(false);
			this.lblMessage.setText("Informations sur le nouvel employe");
			this.butOk.setText("Ajouter");
			this.butCancel.setText("Annuler");
			break;
		case MODIFICATION:
			this.txtIdEmp.setDisable(true);
			this.txtNom.setDisable(false);
			this.txtPrenom.setDisable(false);
			this.lblMessage.setText("Informations employe");
			this.butOk.setText("Modifier");
			this.butCancel.setText("Annuler");
			break;
		case SUPPRESSION:

			ApplicationException ae = new ApplicationException(Table.NONE, Order.OTHER, "SUPPRESSION EMPLOYE NON PREVUE",
					null);
			ExceptionDialog ed = new ExceptionDialog(this.containingStage, this.dailyBankState, ae);
			ed.doExceptionDialog();

			break;
		}
		// Paramétrages spécifiques pour les chefs d'agences
		if (ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
			// rien pour l'instant
		}
		// initialisation du contenu des champs
		this.txtIdEmp.setText("" + this.employeEdite.idEmploye);
		this.txtNom.setText(this.employeEdite.nom);
		this.txtPrenom.setText(this.employeEdite.prenom);
		this.txtDroit.setText(this.employeEdite.droitsAccess);
		this.txtLogin.setText(this.employeEdite.login);
		this.txtMDP.setText(this.employeEdite.motPasse);

		this.employeResultat = null;

		this.containingStage.showAndWait();
		return this.employeResultat;
	}

	// Gestion du stage
	/**
	 * Ferme la fenêtre.
	 * 
	 * @param e
	 * @return Object null
	 */
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions

	@FXML
	private Label lblMessage;
	@FXML
	private TextField txtIdEmp;
	@FXML
	private TextField txtNom;
	@FXML
	private TextField txtPrenom;
	@FXML
	private TextField txtDroit;
	@FXML
	private TextField txtLogin;
	@FXML
	private TextField txtMDP;
	@FXML
	private Button butOk;
	@FXML
	private Button butCancel;

	/**
	 * Action associée au bouton Annuler (FXML).
	 * 
	 */
	@FXML
	private void doCancel() {
		this.employeResultat = null;
		this.containingStage.close();
	}
	/**
	 * Action associée au bouton Ajouter/Modifier/Supprimer (FXML).
	 */
	@FXML
	private void doAjouter() {
		switch (this.editionMode) {
		case CREATION:
			if (this.isSaisieValide()) {
				this.employeResultat = this.employeEdite;
				this.containingStage.close();
			}
			break;
		case MODIFICATION:
			if (this.isSaisieValide()) {
				this.employeResultat = this.employeEdite;
				this.containingStage.close();
			}
			break;
		case SUPPRESSION:
			this.employeResultat = this.employeEdite;
			this.containingStage.close();
			break;
		}

	}
	/**
	 * érifie la validité de la saisie des informations employé.
	 * - Vérifie que les champs ne sont pas vides
	 * - Vérifie que le login et le mot de passe sont valides
	 * - Vérifie que le login n'est pas déjà utilisé
	 * - Vérifie que le mot de passe est assez fort
	 * 
	 * @return true si la saisie est valide, false sinon
	 * 
	 */
	private boolean isSaisieValide() {
		this.employeEdite.nom = this.txtNom.getText().trim();
		this.employeEdite.prenom = this.txtPrenom.getText().trim();
		this.employeEdite.droitsAccess = this.txtDroit.getText().trim();
		this.employeEdite.login = this.txtLogin.getText().trim();
		this.employeEdite.motPasse = this.txtMDP.getText().trim();

		if (this.employeEdite.nom.isEmpty()) {
			AlertUtilities.showAlert(this.containingStage, "Erreur de saisie", null, "Le nom ne doit pas être vide",
					AlertType.WARNING);
			this.txtNom.requestFocus();
			return false;
		}
		if (this.employeEdite.prenom.isEmpty()) {
			AlertUtilities.showAlert(this.containingStage, "Erreur de saisie", null, "Le prénom ne doit pas être vide",
					AlertType.WARNING);
			this.txtPrenom.requestFocus();
			return false;
		}
		return true;
	}
}

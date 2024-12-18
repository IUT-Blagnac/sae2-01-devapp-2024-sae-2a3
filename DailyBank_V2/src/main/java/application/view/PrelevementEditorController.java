package application.view;

import application.DailyBankState;
import application.tools.AlertUtilities;
import application.tools.ConstantesIHM;
import application.tools.EditionMode;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.CompteCourant;
import model.data.Prelevement;

/**
 * Contrôleur pour l'édition des informations client dans une fenêtre.
 * 
 * @see PrelevementEditor
 */
public class PrelevementEditorController {
	private DailyBankState dailyBankState;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage peStage;

	// Données de la fenêtre

	private Prelevement prelevementEdite;
	private EditionMode editionMode;
	private Prelevement prelevementResultat;
	private CompteCourant compteEdite;

	// Manipulation de la fenêtre

	/**
	 * Initialise le contexte du contrôleur.
	 * 
	 * @param _containingStage Le stage contenant la scène
	 * @param _dbstate         L'état courant de l'application
	 */
	public void initContext(Stage _containingStage, DailyBankState _dbstate) {
		this.peStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	/**
	 * Configure les différents éléments de la fenêtre.
	 * 
	 */
	private void configure() {
		this.peStage.setOnCloseRequest(e -> this.closeWindow(e));
	}

	// Gestion du stage

	/**
	 * Ferme la fenêtre.
	 * 
	 * @param e L'événement de fermeture
	 * @return Object null
	 */
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	/**
	 * Affiche la fenêtre de dialogue d'édition d'un client.
	 * 
	 * @param pm   Le prélèvement à éditer
	 * @param mode Le mode d'édition (ajout ou modification)
	 * @return Le prélèvement édité ou null si l'opération a été annulée
	 * 
	 */
	public Prelevement displayDialog(Prelevement pm, EditionMode mode, CompteCourant cpte) {
		this.compteEdite = cpte;
		this.editionMode = mode;
		if (pm == null) {
			this.prelevementEdite = new Prelevement(0, 0, 0, "", 0);
		} else {
			this.prelevementEdite = new Prelevement(pm);
		}
		this.prelevementResultat = null;
		switch (mode) {
			case CREATION:
				this.txtIdpre.setDisable(true);
				this.txtmontant.setDisable(false);
				this.txtdate.setDisable(false);
				this.txtbeneficiaire.setDisable(false);

				this.lblMessage.setText("Informations sur le nouveau prélèvement");
				this.butOk.setText("Ajouter");
				this.butCancel.setText("Annuler");
				break;
			case MODIFICATION:
				this.txtIdpre.setDisable(true);
				this.txtIdpre.setText("" + pm.idPrelevement);
				this.txtmontant.setDisable(false);
				this.txtmontant.setText("" + pm.montant);
				this.txtdate.setDisable(false);
				this.txtdate.setText("" + pm.date);
				this.txtbeneficiaire.setDisable(false);
				this.txtbeneficiaire.setText("" + pm.beneficiaire);


				this.lblMessage.setText("Informations sur le prélèvement");
				this.butOk.setText("Modifier");
				this.butCancel.setText("Annuler");
				break;
			case SUPPRESSION:

				break;
		}
		// Paramétrages spécifiques pour les chefs d'agences
		if (ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
			// rien pour l'instant
		}

		this.prelevementResultat = null;

		this.peStage.showAndWait();
		return this.prelevementResultat;
	}

	@FXML
	private Label lblMessage;
	@FXML
	private TextField txtIdpre;
	@FXML
	private TextField txtmontant;
	@FXML
	private TextField txtdate;
	@FXML
	private TextField txtbeneficiaire;
	@FXML
	private Button butOk;
	@FXML
	private Button butCancel;

	/**
	 * Gestion du clic sur le bouton Annuler (FXML).
	 * 
	 */
	@FXML
	private void doCancel() {
		this.prelevementResultat = null;
		this.peStage.close();
	}

	/**
	 * Gestion du clic sur le bouton Ajouter (FXML).
	 * 
	 */
	@FXML
	private void doAjouter() {
		if (this.isSaisieValide()) {
			this.prelevementResultat = this.prelevementEdite;
			this.peStage.close();
		}
	}

	/**
	 * Vérifie que la saisie est valide.
	 * - Le bénéficiaire n'est pas vide
	 * - La date est comprise entre 1 et 28
	 * - Le montant est positif
	 * 
	 * @return true si la saisie est valide, false sinon
	 */
	private boolean isSaisieValide() {
		while (this.txtbeneficiaire.getText().isEmpty() || this.txtmontant.getText().isEmpty() || this.txtdate.getText().isEmpty()) {
			AlertUtilities.showAlert(this.peStage, "Erreur saisie", "Champs non remplis",
					"Veuillez remplir tous les champs", AlertType.ERROR);
			return false;
		}

		this.prelevementEdite.montant = Integer.parseInt(this.txtmontant.getText().trim());
		this.prelevementEdite.date = Integer.parseInt(this.txtdate.getText().trim());
		this.prelevementEdite.beneficiaire = this.txtbeneficiaire.getText().trim();
		this.prelevementEdite.idNumCompte = this.compteEdite.idNumCompte;

		if (this.prelevementEdite.date < 1 || this.prelevementEdite.date > 28) {
			AlertUtilities.showAlert(this.peStage, "Erreur date", "La date n'est pas valide",
					"Veuillez rentrer une date entre 1 et 28", AlertType.ERROR);
			this.txtdate.requestFocus();
			return false;
		}
		if (this.prelevementEdite.montant <= 0) {
			AlertUtilities.showAlert(this.peStage, "Erreur montant", "Le montant n'est pas valide",
					"Veuillez entrez un montant positif", AlertType.ERROR);
			this.txtmontant.requestFocus();
			return false;
		}

		return true;
	}

	
}

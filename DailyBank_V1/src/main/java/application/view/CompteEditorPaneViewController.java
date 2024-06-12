package application.view;

import java.util.Locale;

import application.DailyBankState;
import application.tools.AlertUtilities;
import application.tools.ConstantesIHM;
import application.tools.EditionMode;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;


/**
 * Contrôleur pour l'édition des informations client dans une fenêtre.
 * 
 * @see ClientEditorPane
 */
public class CompteEditorPaneViewController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage containingStage;

	// Données de la fenêtre
	private EditionMode editionMode;
	private Client clientDuCompte;
	private CompteCourant compteEdite;
	private CompteCourant compteResultat;

	// Manipulation de la fenêtre
	/**
	 * Initialise le contrôleur de la fenêtre.
	 * 
	 * @param _containingStage
	 * @param _dbstate
	 * @author IUT Blagnac
	 */
	public void initContext(Stage _containingStage, DailyBankState _dbstate) {
		this.containingStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}


	/**
	 * Configure la fenêtre de gestion des Clients
	 * 
	 * @author IUT Blagnac
	 */
	private void configure() {
		this.containingStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.txtDecAutorise.focusedProperty().addListener((t, o, n) -> this.focusDecouvert(t, o, n));
		this.txtSolde.focusedProperty().addListener((t, o, n) -> this.focusSolde(t, o, n));
	}

	/**
	 * Affiche la boîte de dialogue d'édition des informations client.
	 * 
	 * @param client Le client à éditer (null pour une création)
	 * @param mode   Le mode d'édition
	 * @return Le client créé/modifié
	 * @author AMERI Mohammed 
	 * @author SHULHINA Daria
	 */
	public CompteCourant displayDialog(Client client, CompteCourant cpte, EditionMode mode) {
		this.clientDuCompte = client;
		this.editionMode = mode;
		if (cpte == null) {
			this.compteEdite = new CompteCourant(0, 200, 0, "N", this.clientDuCompte.idNumCli);
		} else {
			this.compteEdite = new CompteCourant(cpte);
		}
		this.compteResultat = null;
		this.txtIdclient.setDisable(true);
		this.txtIdAgence.setDisable(true);
		this.txtIdNumCompte.setDisable(true);
		switch (mode) {
		case CREATION:
			this.txtDecAutorise.setDisable(false);
			this.txtSolde.setDisable(false);
			this.lblMessage.setText("Informations sur le nouveau compte");
			this.lblSolde.setText("Solde (premier dépôt)");
			this.btnOk.setText("Ajouter");
			this.btnCancel.setText("Annuler");
			break;
		case MODIFICATION:
			this.txtDecAutorise.setDisable(false);
			this.txtSolde.setDisable(true);
			this.lblMessage.setText("Informations sur le compte");
			this.lblSolde.setText("Solde");
			this.btnOk.setText("Modifier");
			this.btnCancel.setText("Annuler");
			break;
		case SUPPRESSION:
		}

		// Paramétrages spécifiques pour les chefs d'agences
		if (ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
			// rien pour l'instant
		}

		// initialisation du contenu des champs
		this.txtIdclient.setText("" + this.compteEdite.idNumCli);
		this.txtIdNumCompte.setText("" + this.compteEdite.idNumCompte);
		this.txtIdAgence.setText("" + this.dailyBankState.getEmployeActuel().idAg);
		this.txtDecAutorise.setText("" + this.compteEdite.debitAutorise);
		this.txtSolde.setText(String.format(Locale.ENGLISH, "%10.02f", this.compteEdite.solde));

		this.compteResultat = null;

		this.containingStage.showAndWait();
		return this.compteResultat;
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
		this.doCancel();
		e.consume();
		return null;
	}

	/**
	 * Gestion du focus sur le champ txtDecAutorise.
	 * 
	 * @param txtField         Le champ en cours de saisie
	 * @param oldPropertyValue La valeur précédente du focus
	 * @param newPropertyValue La nouvelle valeur du focus
	 * @author IUT Blagnac
	 */
	private Object focusDecouvert(ObservableValue<? extends Boolean> txtField, boolean oldPropertyValue,
			boolean newPropertyValue) {
		if (oldPropertyValue) {
			try {
				int val;
				val = Integer.parseInt(this.txtDecAutorise.getText().trim());
				if (val < 0) {
					throw new NumberFormatException();
				}
				this.compteEdite.debitAutorise = val;
			} catch (NumberFormatException nfe) {
				this.txtDecAutorise.setText("" + this.compteEdite.debitAutorise);
			}
		}
		return null;
	}

	/**
	 * Gestion du focus sur le champ txtSolde.
	 * 
	 * @param txtField         Le champ en cours de saisie
	 * @param oldPropertyValue La valeur précédente du focus
	 * @param newPropertyValue La nouvelle valeur du focus
	 * @author IUT Blagnac
	 */
	private Object focusSolde(ObservableValue<? extends Boolean> txtField, boolean oldPropertyValue,
			boolean newPropertyValue) {
		if (oldPropertyValue) {
			try {
				double val;
				val = Double.parseDouble(this.txtSolde.getText().trim());
				if (val < 0) {
					throw new NumberFormatException();
				}
				this.compteEdite.solde = val;
			} catch (NumberFormatException nfe) {
				this.txtSolde.setText(String.format(Locale.ENGLISH, "%10.02f", this.compteEdite.solde));
			}
		}
		this.txtSolde.setText(String.format(Locale.ENGLISH, "%10.02f", this.compteEdite.solde));
		return null;
	}

	// Attributs de la scene + actions
	@FXML
	private Label lblMessage;
	@FXML
	private Label lblSolde;
	@FXML
	private TextField txtIdclient;
	@FXML
	private TextField txtIdAgence;
	@FXML
	private TextField txtIdNumCompte;
	@FXML
	private TextField txtDecAutorise;
	@FXML
	private TextField txtSolde;
	@FXML
	private Button btnOk;
	@FXML
	private Button btnCancel;

	/**
	 * Gestion de l'appui sur le bouton Annuler (FXML).
	 * 
	 * @author IUT Blagnac
	 */
	@FXML
	private void doCancel() {
		this.compteResultat = null;
		this.containingStage.close();
	}

	/**
	 * Gestion de l'appui sur le bouton Nouveau compte, Modifier compte ou Cloturer compte
	 */
	@FXML
	private void doAjouter() {
		if (!isSaisieValide()) {
			// La validation a échoué, une alerte a déjà été affichée
			return;
		}
		
		switch (this.editionMode) {
			case CREATION:
				if(Double.parseDouble(txtSolde.getText()) > 0) {
					this.compteResultat = this.compteEdite;
					this.containingStage.close();
				}else {
					AlertUtilities.showAlert(this.containingStage, "Erreur", "Le solde soit etre positif",null , AlertType.ERROR);
				}
				break;
			case MODIFICATION:
				try {
					this.compteResultat = this.compteEdite;
					// Appel à la méthode de mise à jour dans la base de données ici
					this.containingStage.close();
				} catch (Exception e) {
					AlertUtilities.showAlert(this.containingStage, "Erreur", "Une erreur s'est produite lors de la modification du compte",
							e.getMessage(), AlertType.ERROR);
				}
				break;
			case SUPPRESSION:
				try {
					this.compteResultat = this.compteEdite;
					// Appel à la méthode de suppression dans la base de données ici
					this.containingStage.close();
				} catch (Exception e) {
					AlertUtilities.showAlert(this.containingStage, "Erreur", "Une erreur s'est produite lors de la suppression du compte",
							e.getMessage(), AlertType.ERROR);
				}
				break;
		}
	}
	
	/**
	 * Vérifie que la saisie est valide.
	 * - Le solde du compte doit être supérieur au découvert autorisé
	 * 
	 * @return true si la saisie est valide, false sinon
	 * @author IUT Blagnac
	 */
	private boolean isSaisieValide() {
		
		// Convertir le texte en double pour vérifier le solde
		double solde = Double.parseDouble(txtSolde.getText());

		// Vérifier que le solde n'est pas négatif
		if (solde < 0) {
			// Afficher une alerte si le solde est négatif
			AlertUtilities.showAlert(this.containingStage, "Erreur de saisie", "Le solde ne peut pas être négatif",
					null, AlertType.WARNING);
			return false;
		}
	
		// Tous les contrôles sont passés, la saisie est valide
		return true;
	}

}

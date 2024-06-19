package application.view;

import java.util.ArrayList;
import java.util.Locale;

import application.DailyBankState;
import application.control.PrelevementManagement;
import application.tools.AlertUtilities;
import application.tools.ConstantesIHM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.CompteCourant;
import model.data.Prelevement;
import model.data.Client;

/**
 * Contrôleur pour la gestion des prélèvements automatiques.
 * 
 * @see PrelevementManagement
 */
public class PrelevementManagementController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à ClientsManagementController
	private PrelevementManagement pmDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage pmStage;

	// Données de la fenêtre
	private ObservableList<Prelevement> oListPrelevements;
	private Client clientDuCompte;
	private CompteCourant compteConcerne;

	/**
	 * Initialise le contexte du contrôleur.
	 * 
	 * @param _containingStage Le stage contenant la scène
	 * @param _pm              Le contrôleur de dialogue associé
	 * @param _dbstate         L'état courant de l'application
	 */
	public void initContext(Stage _containingStage, PrelevementManagement _pm, DailyBankState _dbstate, CompteCourant cc, Client c) {
		this.pmDialogController = _pm;
		this.pmStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.compteConcerne = cc;
		this.clientDuCompte = c;
		this.configure();
	}

	/**
	 * Configure les différents éléments de la fenêtre.
	 * 
	 */
	private void configure() {
		this.pmStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.oListPrelevements = FXCollections.observableArrayList();

		this.lvPrelevements.setItems(this.oListPrelevements);
		this.lvPrelevements.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.lvPrelevements.getFocusModel().focus(-1);
		this.lvPrelevements.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());
		this.validateComponentState();
		this.doRechercher();
	}

	/**
	 * Affiche la boîte de dialogue de gestion des prélèvements.
	 * 
	 */
	public void displayDialog() {
		this.pmStage.showAndWait();
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

	// Attributs de la scène + actions
	@FXML
	private Label lblInfosCompte;
	@FXML
	private ListView<Prelevement> lvPrelevements;
	@FXML
	private Button btnSupprPrelevement;
	@FXML
	private Button btnModifPrelevement;
	@FXML
	private Button btnNouveauPrelevement;

	/**
	 * Ferme la fenêtre (bouton FXML).
	 * 
	 */
	@FXML
	private void doCancel() {
		this.pmStage.close();
	}

	/**
	 * Recherche les prélévements automatiques en fonction des critères de recherche (bouton FXML).
	 * 
	 * @author AMERI Mohammed
	 */
	@FXML
	private void doRechercher() {
		
		ArrayList<Prelevement> listePre;
		listePre = this.pmDialogController.getPrelevements(this.compteConcerne.idNumCompte);

		this.oListPrelevements.clear();
		this.oListPrelevements.addAll(listePre);
		this.validateComponentState();
	}

	/**
	 * Ouvre la fenêtre de création d'un prélèvement (bouton FXML).
	 * 
	 * @author Rudy
	 */
	@FXML
	private void doNouveauPrelevement() {
		Prelevement pm;
		pm = this.pmDialogController.nouveauPrelevement();
		if(pm != null) {
			this.oListPrelevements.add(pm);
		}
		this.validateComponentState();
	}

	/**
	 * Ouvre la fenêtre d'édition d'un prélèvement (bouton FXML).
	 * 
	 * @author RAZAFINIRINA Mialisoa
	 */
	@FXML
	private void doModifierPrelevement() {
		int selectedIndice = this.lvPrelevements.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Prelevement preMod = this.oListPrelevements.get(selectedIndice);
			Prelevement pm;
			pm = this.pmDialogController.modifierPrelevement(preMod);
			if(pm != null) {
				this.oListPrelevements.set(selectedIndice, pm);
			}
		}
		this.validateComponentState();
	}

	/**
	 * Supprime le prélévement selectionné (bouton FXML).
	 * 
	 * @author Daria
	 */
	@FXML
	private void doSupprimerPrelevement() {
		int selectedIndice = this.lvPrelevements.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Prelevement preMod = this.oListPrelevements.get(selectedIndice);
			if (!AlertUtilities.confirmYesCancel(this.pmStage, "Supprimer prélévement automatique",
					"Êtes-vous sûr de vouloir supprimer ce prélévement automatique ?",
					"Il ne sera pas possible de restaurer ce prélévement, cependant, vous pourrez le recréer.\n\nPrélévement automatique :\nID : "
							+ preMod.idPrelevement + "\nMontant : " + preMod.montant + "\nDate : " + preMod.date + "\nBénéficiaire : " + preMod.beneficiaire + "\nCompte : " + preMod.idNumCompte, AlertType.CONFIRMATION)) return;
			this.pmDialogController.supprimerPrelevement(preMod);
			this.oListPrelevements.remove(selectedIndice);
		}
		this.validateComponentState();
	}

	/**
	 * Actualise l'état des composants de la fenêtre.
	 * 
	 */
	private void validateComponentState() {
		String info = "Cpt. : " + this.compteConcerne.idNumCompte + "  "
				+ String.format(Locale.ENGLISH, "%12.02f", this.compteConcerne.solde) + "  /  "
				+ String.format(Locale.ENGLISH, "%8d", this.compteConcerne.debitAutorise);
		this.lblInfosCompte.setText(info);

		int selectedIndice = this.lvPrelevements.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			this.btnModifPrelevement.setDisable(false);
			this.btnSupprPrelevement.setDisable(false);
		} else {
			this.btnModifPrelevement.setDisable(true);
			this.btnSupprPrelevement.setDisable(true);
		}
		if (!ConstantesIHM.estActif(clientDuCompte)) {
				this.btnSupprPrelevement.setDisable(true);
                this.btnModifPrelevement.setDisable(true);
                this.btnNouveauPrelevement.setDisable(true);
        }
	}
}
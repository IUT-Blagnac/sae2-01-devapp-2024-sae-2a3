package application.view;

import java.util.ArrayList;

import application.DailyBankState;
import application.control.PrelevementManagement;
import application.tools.AlertUtilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Prelevement;

/**
 * Contrôleur pour la gestion des prélèvements automatiques.
 * 
 * @see PrelevementManagement
 * @author SHULHINA Daria
 */
public class PrelevementManagementViewController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à ClientsManagementController
	private PrelevementManagement pmDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage cmStage;

	// Données de la fenêtre
	private ObservableList<Prelevement> oListPrelevements;

	/**
	 * Initialise le contexte du contrôleur.
	 * 
	 * @param _containingStage Le stage contenant la scène
	 * @param _pm              Le contrôleur de dialogue associé
	 * @param _dbstate         L'état courant de l'application
	 */
	public void initContext(Stage _containingStage, PrelevementManagement _pm, DailyBankState _dbstate) {
		this.pmDialogController = _pm;
		this.cmStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	/**
	 * Configure les différents éléments de la fenêtre.
	 * 
	 */
	private void configure() {
		this.cmStage.setOnCloseRequest(e -> this.closeWindow(e));

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
		this.cmStage.showAndWait();
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
	private TextField txtNum;
	@FXML
	private ListView<Prelevement> lvPrelevements;
	@FXML
	private Button btnSupprPrelevement;
	@FXML
	private Button btnModifPrelevement;

	/**
	 * Ferme la fenêtre (bouton FXML).
	 * 
	 */
	@FXML
	private void doCancel() {
		this.cmStage.close();
	}

	/**
	 * Recherche les prélévements automatiques en fonction des critères de recherche (bouton FXML).
	 * 
	 */
	@FXML
	private void doRechercher() {
		int numCompte;
		try {
			String nc = this.txtNum.getText();
			if (nc.equals("")) {
				numCompte = -1;
			} else {
				numCompte = Integer.parseInt(nc);
				if (numCompte < 0) {
					this.txtNum.setText("");
					numCompte = -1;
				}
			}
		} catch (NumberFormatException nfe) {
			this.txtNum.setText("");
			numCompte = -1;
		}
		
		ArrayList<Prelevement> listePre;
		listePre = this.pmDialogController.getPrelevements(numCompte);

		this.oListPrelevements.clear();
		this.oListPrelevements.addAll(listePre);
		this.validateComponentState();
	}

	/**
	 * Ouvre la fenêtre de création d'un prélèvement (bouton FXML).
	 * 
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
	 */
	@FXML
	private void doSupprimerPrelevement() {
		int selectedIndice = this.lvPrelevements.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Prelevement preMod = this.oListPrelevements.get(selectedIndice);
			if (!AlertUtilities.confirmYesCancel(this.cmStage, "Supprimer prélévement automatique",
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
		int selectedIndice = this.lvPrelevements.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			this.btnModifPrelevement.setDisable(false);
			this.btnSupprPrelevement.setDisable(false);
		} else {
			this.btnModifPrelevement.setDisable(true);
			this.btnSupprPrelevement.setDisable(true);
		}
	}
}
package application.view;

import java.util.ArrayList;
import java.util.Date;

import application.DailyBankState;
import application.control.ComptesManagement;
import application.tools.AlertUtilities;
import application.tools.ConstantesIHM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;
import model.data.Operation;
import model.orm.Access_BD_CompteCourant;
import model.orm.Access_BD_Operation;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;

public class ComptesManagementViewController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à ComptesManagementController
	private ComptesManagement cmDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage containingStage;

	// Données de la fenêtre
	private Client clientDesComptes;
	private ObservableList<CompteCourant> oListCompteCourant;

	// Manipulation de la fenêtre
	public void initContext(Stage _containingStage, ComptesManagement _cm, DailyBankState _dbstate, Client client) {
		this.cmDialogController = _cm;
		this.containingStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.clientDesComptes = client;
		this.configure();
	}

	private void configure() {
		String info;

		this.containingStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.oListCompteCourant = FXCollections.observableArrayList();
		this.lvComptes.setItems(this.oListCompteCourant);
		this.lvComptes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.lvComptes.getFocusModel().focus(-1);
		this.lvComptes.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());

		info = this.clientDesComptes.nom + "  " + this.clientDesComptes.prenom + "  (id : "
				+ this.clientDesComptes.idNumCli + ")";
		this.lblInfosClient.setText(info);

		this.loadList();
		this.validateComponentState();
	}

	public void displayDialog() {
		this.containingStage.showAndWait();
	}

	// Gestion du stage
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions

	@FXML
	private Label lblInfosClient;
	@FXML
	private ListView<CompteCourant> lvComptes;
	@FXML
	private Button btnVoirOpes;
	@FXML
	private Button btnVoirPrlv;
	@FXML
	private Button btnModifierCompte;
	@FXML
	private Button btnCloturerCompte;
	@FXML
	private Button btnSupprimerCompte;
	@FXML
	private Button btnSimulerEmprunt;
	

	@FXML
	private void doCancel() {
		this.containingStage.close();
	}

	@FXML
	private void doVoirOperations() {
		int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			CompteCourant cpt = this.oListCompteCourant.get(selectedIndice);
			this.cmDialogController.gererOperationsDUnCompte(cpt);
		}
		this.loadList();
		this.validateComponentState();
	}

	@FXML
	private void doVoirPrelevement() {
		int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			CompteCourant cpt = this.oListCompteCourant.get(selectedIndice);
			this.cmDialogController.gererPrelevementDUnCompte(cpt);
		}
		this.loadList();
		this.validateComponentState();
	}

	@FXML
	private void doModifierCompte() {
	}

	@FXML
	private void doCloturerCompte() {
		int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			CompteCourant cpt = this.oListCompteCourant.get(selectedIndice);
			if(cpt.solde == 0) {
				this.cmDialogController.cloturerCompte(cpt);
			} else if (cpt.solde > 0){
				Alert al = new Alert(AlertType.WARNING);
				al.setHeaderText("Votre compte n'est pas vide");
				al.show();
			} else if (cpt.solde < 0){
				Alert al = new Alert(AlertType.WARNING);
				al.setHeaderText("Remboursez votre decouvert");
				al.show();
			} 
		}
		this.loadList();
		this.validateComponentState();
	}

	@FXML
	private void doSupprimerCompte() {
		int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			CompteCourant cpt = this.oListCompteCourant.get(selectedIndice);
			if (ConstantesIHM.estCloture(cpt)) {
				// Chercher la dernière opération
				Date derniereOperation = null;
				try {
					// lecture BD de la liste des opérations du compte de l'utilisateur
					Access_BD_Operation ao = new Access_BD_Operation();
					ArrayList<Operation> listeOP = ao.getOperations(cpt.idNumCompte);
					if (!listeOP.isEmpty()) {
						derniereOperation = listeOP.get(0).dateOp; // Initialiser à la première opération
						for (Operation op : listeOP) {
							if (op.dateOp.after(derniereOperation)) {
								derniereOperation = op.dateOp;
							}
						}
					}
				} catch (ApplicationException e) {
					AlertUtilities.showAlert(this.containingStage, "Erreur", "Erreur lors de la récupération des opérations.", e.getMessage(), Alert.AlertType.ERROR);
					return;
				}
				
				if (derniereOperation != null) {
					String message = "Confirmez-vous la suppression du compte clôturé avec la dernière opération réalisée le : " + derniereOperation.toString() + " ?";
					boolean confirmation = AlertUtilities.showAlertConfirmation(this.containingStage, "Suppression de compte", message);
					if (confirmation) {
						this.cmDialogController.supprimerCompte(cpt);
						this.oListCompteCourant.remove(cpt);
					}
				} else {
					String message = "Confirmez-vous la suppression du compte clôturé avec aucune opération ?";
					boolean confirmation = AlertUtilities.showAlertConfirmation(this.containingStage, "Suppression de compte", message);
					if (confirmation) {
						this.cmDialogController.supprimerCompte(cpt);
						this.oListCompteCourant.remove(cpt);
					}				
				}
			} else {
				AlertUtilities.showAlert(this.containingStage, "Suppression impossible", "Le compte doit être clôturé avant d'être supprimé.", null, Alert.AlertType.WARNING);
			}
		} else {
			AlertUtilities.showAlert(this.containingStage, "Erreur de sélection", "Aucun compte sélectionné pour suppression.", null, Alert.AlertType.WARNING);
		}
		this.loadList();
		this.validateComponentState();
	}


	@FXML
	private void doNouveauCompte() {
		CompteCourant compte;
		compte = this.cmDialogController.creerNouveauCompte();
		if (compte != null) {
			this.oListCompteCourant.add(compte);
		}
		this.loadList();
		this.validateComponentState();
	}

	@FXML
	private void doSimulerEmprunt() {
		this.cmDialogController.simuEmprunt();
		this.validateComponentState();
	}

	private void loadList() {
		ArrayList<CompteCourant> listeCpt;
		listeCpt = this.cmDialogController.getComptesDunClient();
		this.oListCompteCourant.clear();
		this.oListCompteCourant.addAll(listeCpt);
	}

	private void validateComponentState() {
        //  // Non implémenté => désactivé
        this.btnModifierCompte.setDisable(true);
        this.btnCloturerCompte.setDisable(true);
        this.btnSupprimerCompte.setDisable(true);

        int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();

        if (selectedIndice >= 0) {
            this.btnVoirOpes.setDisable(false);
			this.btnVoirPrlv.setDisable(false);
            this.btnModifierCompte.setDisable(false);
            this.btnCloturerCompte.setDisable(false);
            CompteCourant compte = this.oListCompteCourant.get(selectedIndice);
            if (ConstantesIHM.estCloture(compte)) {
                this.btnCloturerCompte.setDisable(true);
                this.btnModifierCompte.setDisable(true);
                this.btnSupprimerCompte.setDisable(false);
            }
            if (this.dailyBankState.isChefDAgence()) {
                this.btnSimulerEmprunt.setDisable(false);
            } else {
                this.btnSimulerEmprunt.setDisable(true);
            }
        } else {
            this.btnVoirOpes.setDisable(true);
			this.btnVoirPrlv.setDisable(true);
        }
    }
}

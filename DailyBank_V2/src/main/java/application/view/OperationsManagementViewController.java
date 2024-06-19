package application.view;

import java.util.ArrayList;
import java.util.Locale;

import application.DailyBankState;
import application.control.OperationsManagement;
import application.control.RelevePDF;
import application.tools.NoSelectionModel;
import application.tools.PairsOfValue;
import application.tools.ConstantesIHM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;
import model.data.Operation;

/**
 * Contrôleur pour la gestion des opérations.
 * 
 * @see OperationsManagement
 * @author IUT Blagnac
 */
public class OperationsManagementViewController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à OperationsManagementController
	private OperationsManagement omDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage containingStage;

	// Données de la fenêtre
	private Client clientDuCompte;
	private CompteCourant compteConcerne;
	private ObservableList<Operation> oListOperations;

	// Manipulation de la fenêtre
	/**
	 * Initialise le contexte du contrôleur.
	 * 
	 * @param _containingStage Le stage contenant la scène
	 * @param _om              Le contrôleur de dialogue associé
	 * @param _dbstate         L'état courant de l'application
	 * @param client           Le client associé au compte
	 * @param compte           Le compte courant concerné
	 * @author IUT Blagnac
	 */
	public void initContext(Stage _containingStage, OperationsManagement _om, DailyBankState _dbstate, Client client,
			CompteCourant compte) {
		this.containingStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.omDialogController = _om;
		this.clientDuCompte = client;
		this.compteConcerne = compte;
		this.configure();
	}

	/**
	 * Configure les composants de la fenêtre.
	 * 
	 * @author IUT Blagnac
	 */
	private void configure() {
		this.containingStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.oListOperations = FXCollections.observableArrayList();
		this.lvOperations.setItems(this.oListOperations);
		this.lvOperations.setSelectionModel(new NoSelectionModel<Operation>());
		this.updateInfoCompteClient();
		this.validateComponentState();
	}

	/**
	 * Affiche la boîte de dialogue des opérations.
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
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions

	@FXML
	private Label lblInfosClient;
	@FXML
	private Label lblInfosCompte;
	@FXML
	private ListView<Operation> lvOperations;
	@FXML
	private Button btnDebit;
	@FXML
	private Button btnCredit;
	@FXML
	private Button btnVirement;
	@FXML
	private Button btnDebitExep;
	@FXML
	private Button btnRelevePDF;

	/**
	 * Ferme la fenêtre (bouton FXML).
	 * 
	 * @author IUT Blagnac
	 */
	@FXML
	private void doCancel() {
		this.containingStage.close();
	}

	/**
	 * Enregistre un débit (bouton FXML).
	 * 
	 * @author Rudy
	 */
	@FXML
	private void doDebit() {
		Operation op = this.omDialogController.enregistrerDebit();
		if (op != null) {
			this.updateInfoCompteClient();
			this.validateComponentState();
		}
	}

	/**
	 * Enregistre un crédit (bouton FXML).
	 * 
	 * @author Rudy
	 */
	@FXML
	private void doCredit() {
		Operation op = this.omDialogController.enregistrerCredit();
		if (op != null) {
			this.updateInfoCompteClient();
			this.validateComponentState();
		}
	}

	/**
	 * Enregistre un virement (bouton FXML).
	 * 
	 * @author Mohammed
	 */
	@FXML
	private void doVirement() {
		Operation op = this.omDialogController.enregistrerVirement();
		if (op != null) {
			this.updateInfoCompteClient();
			this.validateComponentState();
		}
	}

	/**
 	* Enregistre un debit exceptionnel 
  	* @author Mialisoa
  	*/
	@FXML
	private void doDebitExeptionnel() {
		Operation op = this.omDialogController.enregistrerDebitExeptionnel();
		if (op != null) {
			this.updateInfoCompteClient();
			this.validateComponentState();
		}
	}

	/**
     * Ouvre la fenêtre de création d'un prélèvement (bouton FXML).
     * 
     * @author Rudy
     */
    @FXML
    private void doRelevePDF() {
        RelevePDF.generateReleve(this.compteConcerne.idNumCompte, this.clientDuCompte, this.containingStage);
    }

	/**
	 * Valide l'état des composants de la fenêtre.
	 * 
	 * @author IUT Blagnac
	 */
	private void validateComponentState() {
		// Non implémenté => désactivé
		this.btnCredit.setDisable(false);
		this.btnDebit.setDisable(false);
		this.btnVirement.setDisable(false);

		if(ConstantesIHM.estCloture(compteConcerne)) {
			this.btnCredit.setDisable(true);
			this.btnDebit.setDisable(true);
			this.btnVirement.setDisable(true);
		}
		if (!ConstantesIHM.estActif(clientDuCompte)) {
				this.btnCredit.setDisable(true);
                this.btnDebit.setDisable(true);
                this.btnVirement.setDisable(true);
                this.btnDebitExep.setDisable(true);
        }
	}
	
	/**
	 * Met à jour les informations du compte client.
	 * 
	 * @author IUT Blagnac
	 */
	private void updateInfoCompteClient() {

		PairsOfValue<CompteCourant, ArrayList<Operation>> opesEtCompte;

		opesEtCompte = this.omDialogController.operationsEtSoldeDunCompte();

		ArrayList<Operation> listeOP;
		this.compteConcerne = opesEtCompte.getLeft();
		listeOP = opesEtCompte.getRight();

		String info;
		info = this.clientDuCompte.nom + "  " + this.clientDuCompte.prenom + "  (id : " + this.clientDuCompte.idNumCli
				+ ")";
		this.lblInfosClient.setText(info);

		info = "Cpt. : " + this.compteConcerne.idNumCompte + "  "
				+ String.format(Locale.ENGLISH, "%12.02f", this.compteConcerne.solde) + "  /  "
				+ String.format(Locale.ENGLISH, "%8d", this.compteConcerne.debitAutorise);
		this.lblInfosCompte.setText(info);

		this.oListOperations.clear();
		this.oListOperations.addAll(listeOP);

		this.validateComponentState();
	}
}

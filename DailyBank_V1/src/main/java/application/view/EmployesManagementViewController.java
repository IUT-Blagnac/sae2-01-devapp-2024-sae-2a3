package application.view;

import java.util.ArrayList;

import application.DailyBankState;
import application.control.ClientsManagement;
import application.control.EmployesManagement;
import application.tools.AlertUtilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.Employe;


/**
 * Contrôleur pour la fenêtre de gestion des Employés.
 * 
 * @see EmployesManagement
 */
public class EmployesManagementViewController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à ClientsManagementController
	private EmployesManagement cmDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage containingStage;

	// Données de la fenêtre
	private ObservableList<Employe> oListEmployes;

	// Manipulation de la fenêtre
	/**
	 * Initialise le contexte du contrôleur.
	 * 
	 * @param _containingStage Le stage contenant la scène
	 * @param _cm              Le contrôleur de dialogue associé
	 * @param _dbstate         L'état courant de l'application
	 * @author AMERI Mohammed 
	 */
	public void initContext(Stage _containingStage, EmployesManagement _cm, DailyBankState _dbstate) {
		this.cmDialogController = _cm;
		this.containingStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	/**
	 * Configure la fenêtre de gestion des Employés
	 * 
 	 * @author SHULHINA Daria
	 */
	private void configure() {
		this.containingStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.oListEmployes = FXCollections.observableArrayList();
		this.lvEmployes.setItems(this.oListEmployes);
		this.lvEmployes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.lvEmployes.getFocusModel().focus(-1);
		this.lvEmployes.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());
		this.validateComponentState();
	}

	/**
	 * Affiche la fenêtre de gestion des Employés
	 * 
 	 * @author CIARDI Rudy 
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
 	 * @author RAZAFINIRINA Mialisoa 
	 */
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions

	@FXML
	private TextField txtNum;
	@FXML
	private TextField txtNom;
	@FXML
	private TextField txtPrenom;
	@FXML
	private ListView<Employe> lvEmployes;
	@FXML
	private Button btnSupprEmploye;
	@FXML
	private Button btnModifEmploye;

	@FXML
	private void doCancel() {
		this.containingStage.close();
	}

	/**
	 * Ferme la fenêtre de gestion des Employés (bouton FXML).
	 * 
	 * @author CIARDI Rudy
	 */
	@FXML
	private void doRechercher() {
		int idEmploye;
		try {
			String nc = this.txtNum.getText();
			if (nc.equals("")) {
				idEmploye = -1;
			} else {
				idEmploye = Integer.parseInt(nc);
				if (idEmploye < 0) {
					this.txtNum.setText("");
					idEmploye = -1;
				}
			}
		} catch (NumberFormatException nfe) {
			this.txtNum.setText("");
			idEmploye = -1;
		}

		String debutNom = this.txtNom.getText();
		String debutPrenom = this.txtPrenom.getText();

		if (idEmploye != -1) {
			this.txtNom.setText("");
			this.txtPrenom.setText("");
		} else {
			if (debutNom.equals("") && !debutPrenom.equals("")) {
				this.txtPrenom.setText("");
			}
		}
		
		ArrayList<Employe> listeEmp;
		listeEmp = this.cmDialogController.getlisteEmployes(idEmploye, debutNom, debutPrenom);

		this.oListEmployes.clear();
		this.oListEmployes.addAll(listeEmp);
		this.validateComponentState();
	}

	/**
	 * Modifie un employé (bouton FXML).
	 * 
	 * @author RAZAFINIRINA Mialisoa 
	 */
	@FXML
	private void doModifierEmploye() {

		int selectedIndice = this.lvEmployes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Employe empMod = this.oListEmployes.get(selectedIndice);
			Employe result = this.cmDialogController.modifierEmploye(empMod);
			if (result != null) {
				this.oListEmployes.set(selectedIndice, result);
			}
		}
	}

	/**
	 * Supprime un employé (bouton FXML).
	 * 
	 * @author AMERI Mohammed 
	 */
	@FXML
	private void doSupprimerEmploye() {
		int selectedIndice = this.lvEmployes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Employe empMod = this.oListEmployes.get(selectedIndice);
			Employe result = this.cmDialogController.supprimerEmploye(empMod);
			if (result != null) {
				this.oListEmployes.set(selectedIndice, result);
				AlertUtilities.showAlert(this.containingStage, "Manipulation réussite", null, "Employé supprimer avec succès", AlertType.WARNING);
			}else{
				AlertUtilities.showAlert(this.containingStage, "Manipulation échouer", null, "Employé non supprimer", AlertType.WARNING);
			}
		}
		doRechercher();
	}

	/**
	 * Crée un nouvel employé (bouton FXML).
	 * 
	 * @author SHULHINA Daria
	 */
	@FXML
	private void doNouveauEmploye() {
		Employe employe;
		employe = this.cmDialogController.nouveauEmploye();
		if (employe != null) {
			this.oListEmployes.add(employe);
		}
	}
	/**
	 * Active/Désactie les boutons en fonction de l'état du compte selectionné
	 * 
	 * @author AMERI Mohammed
	 */
	private void validateComponentState() {
		// Non implémenté => désactivé
		this.btnSupprEmploye.setDisable(false);

		int selectedIndice = this.lvEmployes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			this.btnModifEmploye.setDisable(false);
		} else {
			this.btnModifEmploye.setDisable(true);
		}
	}
}

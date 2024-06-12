package application.view;

import java.util.Locale;

import application.DailyBankState;
import application.control.ClientEditorPane;
import application.tools.CategorieOperation;
import application.tools.ConstantesIHM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.CompteCourant;
import model.data.Operation;
import model.orm.Access_BD_Operation;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.ManagementRuleViolation;

/**
 * Contrôleur pour l'édition des informations client dans une fenêtre.
 * 
 * @see ClientEditorPane
 * @author IUT Blagnac
 */
public class OperationEditorPaneViewController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage containingStage;

	// Données de la fenêtre
	private CategorieOperation categorieOperation;
	private CompteCourant compteEdite;
	private Operation operationResultat;

	// Manipulation de la fenêtre
	/**
	 * Initialise le contexte du contrôleur.
	 * 
	 * @param _containingStage Le stage contenant la scène
	 * @param _dbstate         L'état courant de l'application
	 * @author IUT Blagnac
	 */
	public void initContext(Stage _containingStage, DailyBankState _dbstate) {
		this.containingStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	/**
	 * Configure les composants de la fenêtre.
	 */
	private void configure() {
		this.containingStage.setOnCloseRequest(e -> this.closeWindow(e));
	}

	/**
	 * Configure les composants de la fenêtre.
	 * 
	 * @param cpte Le compte courant concerné
	 * @param mode Le mode de transaction (débit ou crédit)
	 * @author IUT Blagnac
	 */
	public Operation displayDialog(CompteCourant cpte, CategorieOperation mode) {
		this.categorieOperation = mode;
		this.compteEdite = cpte;

		switch (mode) {
		case DEBIT:

			String info = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
					+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
			this.lblMessage.setText(info);

			this.btnOk.setText("Effectuer Débit");
			this.btnCancel.setText("Annuler débit");

			// Afficher le label et le textfield pour le compte destinataire du virement
			this.lblCompteDestinataire.setVisible(false);
			this.txtCompteDestinataire.setVisible(false);

			ObservableList<String> listTypesOpesPossibles = FXCollections.observableArrayList();
			listTypesOpesPossibles.addAll(ConstantesIHM.OPERATIONS_DEBIT_GUICHET);

			this.cbTypeOpe.setItems(listTypesOpesPossibles);
			this.cbTypeOpe.getSelectionModel().select(0);
			break;
		case CREDIT:
			String infoCredit = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde);
			this.lblMessage.setText(infoCredit);

			this.btnOk.setText("Effectuer Crédit");
			this.btnCancel.setText("Annuler crédit");

			// Afficher le label et le textfield pour le compte destinataire du virement
			this.lblCompteDestinataire.setVisible(false);
			this.txtCompteDestinataire.setVisible(false);

			ObservableList<String> listTypesOpesPossiblesCredit = FXCollections.observableArrayList();
			listTypesOpesPossiblesCredit.addAll(ConstantesIHM.OPERATIONS_CREDIT_GUICHET); // Ajouter les opérations de crédit possibles

			this.cbTypeOpe.setItems(listTypesOpesPossiblesCredit);
			this.cbTypeOpe.getSelectionModel().select(0);
			break;
		case VIREMENT:
			String infoVirement = "Cpt. : " + this.compteEdite.idNumCompte + "  "
				+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde);
			this.lblMessage.setText(infoVirement);
		
			this.btnOk.setText("Effectuer Virement");
			this.btnCancel.setText("Annuler virement");
		
			// Afficher le label et le textfield
			this.lblCompteDestinataire.setVisible(true);
			this.txtCompteDestinataire.setVisible(true);
			this.cbTypeOpe.setVisible(false);
			this.lblTypeOpe.setVisible(false);
		
			// Effacer le contenu du textfield pour permettre à l'utilisateur de saisir l'id du compte destinataire
			this.txtCompteDestinataire.setText("");
		
			break;
		}

		// Paramétrages spécifiques pour les chefs d'agences
		if (ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
			// rien pour l'instant
		}

		this.operationResultat = null;
		this.cbTypeOpe.requestFocus();

		this.containingStage.showAndWait();
		return this.operationResultat;
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
	private Label lblMessage;
	@FXML
	private Label lblMontant;
	@FXML
	private Label lblTypeOpe;
	@FXML
	private ComboBox<String> cbTypeOpe;
	@FXML
    private Label lblCompteDestinataire;
	@FXML
    private TextField txtCompteDestinataire;
	@FXML
	private TextField txtMontant;
	@FXML
	private Button btnOk;
	@FXML
	private Button btnCancel;

	/**
	 * Méthode appelée lors de l'appui sur le bouton "Annuler" (bouton FXML).
	 * 
	 * @author IUT Blagnac
	 */
	@FXML
	private void doCancel() {
		this.operationResultat = null;
		this.containingStage.close();
	}

	/**
	 * Méthode appelée lors de l'appui sur le bouton "Ajouter" (bouton FXML).
	 * 
	 * @author IUT Blagnac
	 */
	@FXML
	private void doAjouter() {
		switch (this.categorieOperation) {
		case DEBIT:
			// règles de validation d'un débit :
			// - le montant doit être un nombre valide
			// - et si l'utilisateur n'est pas chef d'agence,
			// - le débit ne doit pas amener le compte en dessous de son découvert autorisé
			double montant;

			this.txtMontant.getStyleClass().remove("borderred");
			this.lblMontant.getStyleClass().remove("borderred");
			this.lblMessage.getStyleClass().remove("borderred");
			String info = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
					+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
			this.lblMessage.setText(info);

			try {
				montant = Double.parseDouble(this.txtMontant.getText().trim());
				if (montant <= 0)
					throw new NumberFormatException();
			} catch (NumberFormatException nfe) {
				this.txtMontant.getStyleClass().add("borderred");
				this.lblMontant.getStyleClass().add("borderred");
				this.txtMontant.requestFocus();
				return;
			}
			if (this.compteEdite.solde - montant < this.compteEdite.debitAutorise && !this.dailyBankState.isChefDAgence()) {
				info = "Dépassement du découvert ! - Cpt. : " + this.compteEdite.idNumCompte + "  "
						+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
						+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
				this.lblMessage.setText(info);
				this.txtMontant.getStyleClass().add("borderred");
				this.lblMontant.getStyleClass().add("borderred");
				this.lblMessage.getStyleClass().add("borderred");
				this.txtMontant.requestFocus();
				return;
			}
			
			String typeOpDebit = this.cbTypeOpe.getValue();
			this.operationResultat = new Operation(-1, montant, null, null, this.compteEdite.idNumCli, typeOpDebit);
			this.containingStage.close();
			break;
		case CREDIT:
			// Règles de validation d'un crédit :
			// - Le montant doit être un nombre valide et positif
			double montantCredit;

			this.txtMontant.getStyleClass().remove("borderred");
			this.lblMontant.getStyleClass().remove("borderred");
			this.lblMessage.getStyleClass().remove("borderred");

			try {
				montantCredit = Double.parseDouble(this.txtMontant.getText().trim());
				if (montantCredit <= 0)
					throw new NumberFormatException();
			} catch (NumberFormatException nfe) {
				this.txtMontant.getStyleClass().add("borderred");
				this.lblMontant.getStyleClass().add("borderred");
				this.txtMontant.requestFocus();
				return;
			}

			String typeOpCredit = this.cbTypeOpe.getValue();
			// Créer l'objet Operation pour le crédit
			this.operationResultat = new Operation(-1, montantCredit, null, null, this.compteEdite.idNumCli, typeOpCredit);
			// Fermer la fenêtre de dialogue
			this.containingStage.close(); 
			break;
		case VIREMENT:
			// Règles de validation d'un virement :
			// - Le montant doit être un nombre valide et positif
			double montantVirement;
		
			this.txtMontant.getStyleClass().remove("borderred");
			this.lblMontant.getStyleClass().remove("borderred");
			this.lblMessage.getStyleClass().remove("borderred");
		
			try {
				montantVirement = Double.parseDouble(this.txtMontant.getText().trim());
				if (montantVirement <= 0)
					throw new NumberFormatException();
			} catch (NumberFormatException nfe) {
				this.txtMontant.getStyleClass().add("borderred");
				this.lblMontant.getStyleClass().add("borderred");
				this.txtMontant.requestFocus();
				return;
			}
		
			// Récupérer l'id du compte destinataire depuis la TextField
			String compteDestinataire = this.txtCompteDestinataire.getText();
			if (compteDestinataire == null || compteDestinataire.isEmpty()) {
				// Afficher un message d'erreur si aucun compte n'est sélectionné
				System.out.println("Erreur : Compte destinataire non sélectionné.");
				return;
			}

			int idCompteDestinataire;
			try {
				idCompteDestinataire = Integer.parseInt(compteDestinataire);
			} catch (NumberFormatException e) {
				// Gérer le cas où la chaîne de caractères ne peut pas être convertie en un entier
				System.out.println("Erreur : Numéro de compte destinataire invalide.");
				return;
			}
			
			// Créer l'objet Operation pour le crédit
			this.operationResultat = new Operation(-1, montantVirement, null, null, this.compteEdite.idNumCli, idCompteDestinataire, "Virement Compte à Compte");
			// Fermer la fenêtre de dialogue
			this.containingStage.close();
			
			break;
		}
	}
}

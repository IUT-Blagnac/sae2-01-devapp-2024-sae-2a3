package application.view;

import java.util.Locale;

import application.DailyBankState;
import application.tools.AlertUtilities;
import application.tools.CategorieOperation;
import application.tools.ConstantesIHM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.CompteCourant;
import model.data.Operation;

public class EmpruntEditorPaneViewController {
    // Etat courant de l'application
	private DailyBankState dailyBankState;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage containingStage;

	// Manipulation de la fenêtre
	public void initContext(Stage dialogStage, DailyBankState dailyBankState) {
        this.containingStage = dialogStage;
        this.dailyBankState = dailyBankState;
        this.configure();
    }

    private void configure() {
        this.containingStage.setOnCloseRequest(e -> this.closeWindow(e));
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
    private Label lblTaux;
    @FXML
    private Label lblMontant;
    @FXML
    private Label lblDuree;
    @FXML
    private Label lblPrime;

    @FXML
    private ComboBox cmbTypeSimulation;
	@FXML
    private TextField txtMontant;
    @FXML
    private TextField txtTaux;
    @FXML
    private TextField txtDuree;
    @FXML
    private TextField txtPrime;

    @FXML
    private Button btnSimuler;
    @FXML
    private Label lblResult;
	@FXML
	private Button btnCancel;
	

	@FXML
	private void doCancel() {
		this.containingStage.close();
	}

	@FXML
    private void doSimuler() {
        if (this.isSaisieValide()) {
            double montant = Double.parseDouble(this.txtMontant.getText());
            double taux = Double.parseDouble(this.txtTaux.getText());
            int duree = Integer.parseInt(this.txtDuree.getText());

            double resultat;
            if (this.cmbTypeSimulation.getValue().equals("Emprunt")) {
                resultat = (montant * taux / 100) / duree;  // Calcul pour un emprunt
                this.lblResult.setText("Résultat de la simulation d'emprunt : " + String.format("%.2f", resultat));
            } else if (this.cmbTypeSimulation.getValue().equals("Assurance")) {
                resultat = montant * (taux / 100); // Calcul pour une assurance (peut être ajusté selon les règles spécifiques)
                this.lblResult.setText("Prime d'assurance : " + String.format("%.2f", resultat));
            }
        }
    }

    private double calculerPaiementMensuel(double montant, double tauxAnnuel, int dureeAnnees) {
        int n = dureeAnnees * 12;
        double tauxMensuel = tauxAnnuel / 12;
        return montant * tauxMensuel / (1 - Math.pow(1 + tauxMensuel, -n));
    }

    private boolean isSaisieValide() {
        if (this.txtMontant.getText().isEmpty() || this.txtTaux.getText().isEmpty() || this.txtDuree.getText().isEmpty()) {
            AlertUtilities.showAlert(this.containingStage, "Erreur de saisie", null, "Veuillez remplir tous les champs.", AlertType.WARNING);
            return false;
        }
    
        try {
            double montant = Double.parseDouble(this.txtMontant.getText());
            double taux = Double.parseDouble(this.txtTaux.getText());
            int duree = Integer.parseInt(this.txtDuree.getText());
    
            if (montant <= 0 || taux <= 0 || duree <= 0) {
                AlertUtilities.showAlert(this.containingStage, "Erreur de saisie", null, "Les valeurs doivent être positives.", AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtilities.showAlert(this.containingStage, "Erreur de saisie", null, "Veuillez entrer des valeurs numériques valides.", AlertType.WARNING);
            return false;
        }
    
        return true;
    }
    
}


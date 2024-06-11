package application.view;

import application.DailyBankState;
import application.tools.AlertUtilities;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class EmpruntEditorPaneViewController {
    // Etat courant de l'application
    private DailyBankState dailyBankState;

    // Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
    private Stage containingStage;

    // Manipulation de la fenêtre
    public void initContext(Stage _containingStage, DailyBankState _dbstate) {
        this.containingStage = _containingStage;
        this.dailyBankState = _dbstate;
        this.configure();
    }

    private void configure() {
        this.containingStage.setOnCloseRequest(e -> this.closeWindow(e));
        this.cmbTypeSimulation.setItems(FXCollections.observableArrayList("Emprunt", "Assurance"));
        this.cmbTypeSimulation.setOnAction(e -> this.updateLabels());
        this.updateLabels();
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
    private ComboBox<String> cmbTypeSimulation;
    @FXML
    private TextField txtMontant;
    @FXML
    private TextField txtTaux;
    @FXML
    private TextField txtDuree;

    @FXML
    private Button btnSimuler;
    @FXML
    private Button btnCancel;

    @FXML
    private void doCancel() {
        this.containingStage.close();
    }

    @FXML
    private void doSimuler() {
        if (this.isSaisieValide()) {
            double montant = Double.parseDouble(txtMontant.getText());
            double taux = Double.parseDouble(txtTaux.getText());
            int duree = Integer.parseInt(txtDuree.getText());
    
            if (this.cmbTypeSimulation.getValue().equals("Emprunt")) {
                // Simulation d'emprunt
                double tauxMensuel = taux / 12 / 100; // Taux mensuel
                int nombreMensualites = duree * 12; // Nombre de mensualités
                double mensualite = montant * (tauxMensuel * Math.pow(1 + tauxMensuel, nombreMensualites)) / (Math.pow(1 + tauxMensuel, nombreMensualites) - 1);
                
                afficherNotification("Paiement mensuel de l'emprunt", String.format("%.2f", mensualite));
            } else if (this.cmbTypeSimulation.getValue().equals("Assurance")) {
                // Simulation d'assurance
                double coutAssurance = montant * (taux / 100) * duree;
                
                afficherNotification("Coût total de l'assurance", String.format("%.2f", coutAssurance));
            }
        }
    }

    private void afficherNotification(String titre, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titre);
    
        Label labelMessage = new Label(titre + " : " + message + " €");
        labelMessage.setFont(Font.font("Arial", 16));
        alert.setGraphic(null);
        alert.getDialogPane().setContent(labelMessage);
        alert.setHeaderText(null);
        
        alert.showAndWait();
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

    private void updateLabels() {
        String selection = this.cmbTypeSimulation.getValue();
        boolean isEmprunt = "Emprunt".equals(selection);
        boolean isAssurance = "Assurance".equals(selection);
    
        this.lblMontant.setVisible(isEmprunt || isAssurance);
        this.txtMontant.setVisible(isEmprunt || isAssurance);
    
        this.lblTaux.setVisible(isEmprunt || isAssurance);
        this.txtTaux.setVisible(isEmprunt || isAssurance);
    
        this.lblDuree.setVisible(isEmprunt || isAssurance);
        this.txtDuree.setVisible(isEmprunt || isAssurance);
    
        if (isEmprunt) {
            this.lblMontant.setText("Montant de l'emprunt :");
            this.lblTaux.setText("Taux d'intérêt annuel (%) :");
            this.lblDuree.setText("Durée de l'emprunt (année) :");
        } else if (isAssurance) {
            this.lblMontant.setText("Montant du prêt :");
            this.lblTaux.setText("Taux d’assurance (%) :");
            this.lblDuree.setText("Durée du crédit (année) :");
        }
    }
}

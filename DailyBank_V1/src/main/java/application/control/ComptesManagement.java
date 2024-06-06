package application.control;

import java.util.ArrayList;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.AlertUtilities;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.ComptesManagementViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.data.CompteCourant;
import model.data.Employe;
import model.orm.Access_BD_CompteCourant;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.Order;
import model.orm.exception.Table;

/**
 * Classe responsable de la gestion de la fenêtre de gestion des comptes dans
 * l'application DailyBank.
 * 
 * @see ComptesManagementViewController
 * @see Access_BD_CompteCourant
 * @author IUT Blagnac
 * @author SHULHINA Daria
 */
public class ComptesManagement {

	private Stage cmStage;
	private ComptesManagementViewController cmViewController;
	private DailyBankState dailyBankState;
	private Client clientDesComptes;

	/**
	 * Constructeur de la classe ComptesManagement.
	 *
	 * @param _parentStage Fenêtre parente
	 * @param _dbstate     État courant de l'application
	 * @param client       Client associé aux comptes
	 * @author IUT Blagnac
	 */
	public ComptesManagement(Stage _parentStage, DailyBankState _dbstate, Client client) {

		this.clientDesComptes = client;
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(ComptesManagementViewController.class.getResource("comptesmanagement.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.cmStage = new Stage();
			this.cmStage.initModality(Modality.WINDOW_MODAL);
			this.cmStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.cmStage);
			this.cmStage.setScene(scene);
			this.cmStage.setTitle("Gestion des comptes");
			this.cmStage.setResizable(false);

			this.cmViewController = loader.getController();
			this.cmViewController.initContext(this.cmStage, this, _dbstate, client);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la fenêtre de gestion des comptes.
	 * 
	 * @author IUT Blagnac
	 */
    public void doComptesManagementDialog() {
		this.cmViewController.displayDialog();
	}

	/**
	 * Gère les opérations d'un compte spécifique.
	 *
	 * @param cpt Compte courant à gérer
	 * @author IUT Blagnac
	 */
	public void gererOperationsDUnCompte(CompteCourant cpt) {
		OperationsManagement om = new OperationsManagement(this.cmStage, this.dailyBankState,
				this.clientDesComptes, cpt);
		om.doOperationsManagementDialog();
	}

	/**
	 * Crée un nouveau compte.
	 *
	 * @return Le compte courant créé
	 * @author IUT Blagnac
	 */
	public CompteCourant creerNouveauCompte() {
		CompteCourant compte;
		CompteEditorPane cep = new CompteEditorPane(this.cmStage, this.dailyBankState);
		compte = cep.doCompteEditorDialog(this.clientDesComptes, null, EditionMode.CREATION);
		if (compte != null) {
			try {

				Access_BD_CompteCourant acc = new Access_BD_CompteCourant(); 
				acc.insertCompte(compte); 

			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.cmStage.close();
				compte = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				compte = null;
			}
		}
		return compte;
	}

	/**
	 * Récupère les comptes d'un client.
	 *
	 * @return La liste des comptes courants du client
	 * @author IUT Blagnac
	 */
	public ArrayList<CompteCourant> getComptesDunClient() {
		ArrayList<CompteCourant> listeCpt = new ArrayList<>();

		try {
			Access_BD_CompteCourant acc = new Access_BD_CompteCourant();
			listeCpt = acc.getCompteCourants(this.clientDesComptes.idNumCli);
		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.cmStage.close();
			listeCpt = new ArrayList<>();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
			listeCpt = new ArrayList<>();
		}
		return listeCpt;
	}

	/**
	 * Modifie un compte.
	 *
	 * @param cpt Le compte à modifier
	 * @return Le compte modifié
	 * @author SHULHINA Daria
	 */
	public CompteCourant modifierCompte(CompteCourant cpt) {
		CompteEditorPane cep = new CompteEditorPane(this.cmStage, this.dailyBankState);
		CompteCourant result = cep.doCompteEditorDialog(clientDesComptes, cpt, EditionMode.MODIFICATION);
		if (result != null) {
			try {
				Access_BD_CompteCourant ac = new Access_BD_CompteCourant();
				ac.updateCompteCourant(result);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				result = null;
				this.cmStage.close();
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				result = null;
			}
		}
		return result;
	}

	/**
	 * Cloture un compte.
	 *
	 * @param compte Le compte à cloturer
	 * @author SHULHINA Daria
	 */
	public void cloturerCompte(CompteCourant compte) {
		if(compte!=null) {
			try {
				Access_BD_CompteCourant ac = new Access_BD_CompteCourant();
				ac.cloturerCompte(compte);
				if(Math.random()<-1){
					throw new ApplicationException(Table.CompteCourant, Order.INSERT, "todo : test exceptions", null);
				}
			}catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.cmStage.close();
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
			}	
		}
	}
}

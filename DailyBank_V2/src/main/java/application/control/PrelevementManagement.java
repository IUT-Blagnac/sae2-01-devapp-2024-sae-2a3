package application.control;

import java.util.ArrayList;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.PrelevementManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.CompteCourant;
import model.data.Prelevement;
import model.orm.Access_BD_Prelevement;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;

/**
 * Classe responsable de la gestion de la fenêtre de gestion des prélèvements
 * automatiques dans l'application DailyBank.
 * 
 * @see PrelevementManagementController
 * @see Access_BD_Prelevement
 */
public class PrelevementManagement {

	private Stage pmStage;
	private DailyBankState dailyBankState;
	private PrelevementManagementController pmcViewController;
	private CompteCourant compteConcerne;

	/**
	 * Constructeur de la classe PrelevementsMangement.
	 *
	 * @param _parentStage Fenêtre parente
	 * @param _dbstate     État courant de l'application
	 */
	public PrelevementManagement(Stage _parentStage, DailyBankState _dbstate, CompteCourant cc) {
		this.dailyBankState = _dbstate;
		this.compteConcerne = cc;
		try {
			FXMLLoader loader = new FXMLLoader(
					PrelevementManagementController.class.getResource("prelevementManagement.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource(dailyBankState.getThemeActuel().getCssFile()).toExternalForm());

			this.pmStage = new Stage();
			this.pmStage.initModality(Modality.WINDOW_MODAL);
			this.pmStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.pmStage);
			this.pmStage.setScene(scene);
			this.pmStage.setTitle("Gestion des prélèvements");
			this.pmStage.setResizable(false);

			this.pmcViewController = loader.getController();
			this.pmcViewController.initContext(this.pmStage, this, _dbstate, this.compteConcerne);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la fenêtre de gestion des employés.
	 * 
	 */
	public void doPrelevementManagementDialog() {
		this.pmcViewController.displayDialog();
	}

	/**
	 * Crée un nouveau prélèvement.
	 *
	 * @return Le prélèvement créé
	 * @author Rudy
	 */
	public Prelevement nouveauPrelevement() {
		Prelevement pm;
		PrelevementEditorPane pep = new PrelevementEditorPane(this.pmStage, this.dailyBankState);
		pm = pep.doPrelevementEditorDialog(null, EditionMode.CREATION, compteConcerne);
		if (pm != null) {
			try {
				Access_BD_Prelevement ap = new Access_BD_Prelevement();
				ap.insertPrelevement(pm);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.pmStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.pmStage.close();
				pm = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.pmStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				pm = null;
			}
		}
		return pm;
	}

	/**
	 * Modifie un prélèvement.
	 *
	 * @param pr Le prélèvement à modifier
	 * @return Le prélèvement modifié
	 * @author RAZAFINIRINA Mialisoa
	 */
	public Prelevement modifierPrelevement(Prelevement pr) {
		Prelevement pm;
		PrelevementEditorPane pep = new PrelevementEditorPane(this.pmStage, this.dailyBankState);
		pm = pep.doPrelevementEditorDialog(pr, EditionMode.MODIFICATION, compteConcerne);
		if (pm != null) {
			try {
				Access_BD_Prelevement ap = new Access_BD_Prelevement();
				ap.modifierPrelevement(pm);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.pmStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.pmStage.close();
				pm = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.pmStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				pm = null;
			}
		}
		return pm;
	}

	/**
	 * Supprime un prélèvement.
	 *
	 * @param pr Le prélèvement à supprimer
	 * @author Daria
	 */
	public void supprimerPrelevement(Prelevement pr) {
		if (pr != null) {
			try {
				Access_BD_Prelevement ap = new Access_BD_Prelevement();
				ap.supprimerPrelevement(pr);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.pmStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.pmStage.close();
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.pmStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
			}
		}
	}

	/**
	 * Obtient la liste des clients en fonction des critères de recherche.
	 *
	 * @param _numCompte   Le numéro de compte (ou -1 pour tous les clients)
	 * @param _debutNom    Le début du nom du client
	 * @param _debutPrenom Le début du prénom du client
	 * @return La liste des clients correspondant aux critères de recherche
	 * @author AMERI Mohammed
	 */
	public ArrayList<Prelevement> getPrelevements(int idCompte) {
		ArrayList<Prelevement> listePre = new ArrayList<>();
		try {
			// numCompte != -1 => recherche sur numCompte
			// numCompte == -1 => recherche sur l'agence

			Access_BD_Prelevement ac = new Access_BD_Prelevement();
			listePre = ac.getPrelevements(this.dailyBankState.getEmployeActuel().idAg, idCompte);

		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.pmStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.pmStage.close();
			listePre = new ArrayList<>();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.pmStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
			listePre = new ArrayList<>();
		}
		return listePre;
	}
}
package application.control;

import java.util.ArrayList;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.PrelevementManagementViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Prelevement;
import model.orm.Access_BD_Prelevement;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;

/**
 * Classe responsable de la gestion de la fenêtre de gestion des prélèvements
 * automatiques dans l'application DailyBank.
 * 
 * @see PrelevementManagementViewController
 * @see Access_BD_Prelevement
 * @author SHULHINA Daria
 */
public class PrelevementManagement {

	private Stage cmStage;
	private DailyBankState dailyBankState;
	private PrelevementManagementViewController pmcViewController;

	/**
	 * Constructeur de la classe PrelevementsMangement.
	 *
	 * @param _parentStage Fenêtre parente
	 * @param _dbstate     État courant de l'application
	 * @author SHULHINA Daria
	 */
	public PrelevementManagement(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(
					PrelevementManagementViewController.class.getResource("prelevementManagement.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.cmStage = new Stage();
			this.cmStage.initModality(Modality.WINDOW_MODAL);
			this.cmStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.cmStage);
			this.cmStage.setScene(scene);
			this.cmStage.setTitle("Gestion des clients");
			this.cmStage.setResizable(false);

			this.pmcViewController = loader.getController();
			this.pmcViewController.initContext(this.cmStage, this, _dbstate);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la fenêtre de gestion des employés.
	 * @author SHULHINA Daria
	 */
	public void doPrelevementManagementDialog() {
		this.pmcViewController.displayDialog();
	}

	/**
	 * Crée un nouveau prélèvement.
	 *
	 * @return Le prélèvement créé
	 */
	public Prelevement nouveauPrelevement() {
		Prelevement pm;
		PrelevementEditorPane pep = new PrelevementEditorPane(this.cmStage, this.dailyBankState);
		pm = pep.doPrelevementEditorDialog(null, EditionMode.CREATION);
		if (pm != null) {
			try {
				Access_BD_Prelevement ap = new Access_BD_Prelevement();
				ap.insertPrelevement(pm);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.cmStage.close();
				pm = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, ae);
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
	 * @author SHULHINA Daria
	 */
	public Prelevement modifierPrelevement(Prelevement pr) {
		Prelevement pm;
		PrelevementEditorPane pep = new PrelevementEditorPane(this.cmStage, this.dailyBankState);
		pm = pep.doPrelevementEditorDialog(pr, EditionMode.MODIFICATION);
		if (pm != null) {
			try {
				Access_BD_Prelevement ap = new Access_BD_Prelevement();
				ap.modifierPrelevement(pm);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.cmStage.close();
				pm = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				pm = null;
			}
		}
		return pm;
	}

	/**
	 * Supprime un prélèvement.
	 *
	 * @param pr Le prélèvement à modifier
	 * @author SHULHINA Daria
	 */
	public void supprimerPrelevement(Prelevement pr) {
		if (pr != null) {
			try {
				Access_BD_Prelevement ap = new Access_BD_Prelevement();
				ap.supprimerPrelevement(pr);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.cmStage.close();
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
			}
		}
	}

	/**
	 * Obtient la liste des clients en fonction des critères de recherche.
	 *
	 * @param _numCompte   Le numéro de compte (ou -1 pour tous les clients)
	 * @return La liste des clients correspondant aux critères de recherche
	 * @author SHULHINA Daria
	 */
	public ArrayList<Prelevement> getPrelevements(int _numCompte) {
		ArrayList<Prelevement> listePre = new ArrayList<>();
		try {
			// numCompte != -1 => recherche sur numCompte
			// numCompte == -1 => recherche sur l'agence

			Access_BD_Prelevement ac = new Access_BD_Prelevement();
			listePre = ac.getPrelevements(this.dailyBankState.getEmployeActuel().idAg, _numCompte);

		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.cmStage.close();
			listePre = new ArrayList<>();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
			listePre = new ArrayList<>();
		}
		return listePre;
	}
}
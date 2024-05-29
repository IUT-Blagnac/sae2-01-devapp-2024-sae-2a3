package application.control;

import java.util.ArrayList;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.EmployesManagementViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.data.Employe;
import model.orm.Access_BD_Client;
import model.orm.Access_BD_Employe;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;

public class EmployesManagement {

	private Stage cmStage;
	private DailyBankState dailyBankState;
	private EmployesManagementViewController cmViewController;

	public EmployesManagement(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(EmployesManagementViewController.class.getResource("employesmanagement.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.cmStage = new Stage();
			this.cmStage.initModality(Modality.WINDOW_MODAL);
			this.cmStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.cmStage);
			this.cmStage.setScene(scene);
			this.cmStage.setTitle("Gestion des employes");
			this.cmStage.setResizable(false);

			this.cmViewController = loader.getController();
			this.cmViewController.initContext(this.cmStage, this, _dbstate);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doEmployeManagementDialog() {
		this.cmViewController.displayDialog();
	}

	public Employe modifierEmploye(Employe c) {
		EmployeEditorPane cep = new EmployeEditorPane(this.cmStage, this.dailyBankState);
		Employe result = cep.doEmployeEditorDialog(c, EditionMode.MODIFICATION);
		if (result != null) {
			try {
				Access_BD_Employe ac = new Access_BD_Employe();
				ac.updateEmploye(result);
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

	public Employe nouveauEmploye() {
		Employe employe;
		EmployeEditorPane cep = new EmployeEditorPane(this.cmStage, this.dailyBankState);
		employe = cep.doEmployeEditorDialog(null, EditionMode.CREATION);
		if (employe != null) {
			try {
				Access_BD_Employe ac = new Access_BD_Employe();

				ac.insertEmploye(employe);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.cmStage.close();
				employe = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				employe = null;
			}
		}
		return employe;
	}

	public Employe supprimerEmploye(Employe c) {
		try {
			Access_BD_Employe ac = new Access_BD_Employe();
			ac.deleteEmploye(c);
		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			c = null;
			this.cmStage.close();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
			c = null;
		}
		
		return c;
	}

	public ArrayList<Employe> getlisteEmployes(int _idEmploye, String _debutNom, String _debutPrenom) {
		ArrayList<Employe> listeEmp = new ArrayList<>();
		try {
			Access_BD_Employe ac = new Access_BD_Employe();
			listeEmp = ac.getEmployes(this.dailyBankState.getEmployeActuel().idAg, _idEmploye, _debutNom, _debutPrenom);

		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.cmStage.close();
			listeEmp = new ArrayList<>();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.cmStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
			listeEmp = new ArrayList<>();
		}
		return listeEmp;
	}
}

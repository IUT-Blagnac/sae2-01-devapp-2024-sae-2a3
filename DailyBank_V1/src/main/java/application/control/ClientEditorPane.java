package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.ClientEditorPaneViewController;
import application.view.ClientsManagementViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;

public class ClientEditorPane {

	private Stage cepStage;
	private ClientEditorPaneViewController cepViewController;
	private DailyBankState dailyBankState;

	public ClientEditorPane(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(ClientsManagementViewController.class.getResource("clienteditorpane.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.cepStage = new Stage();
			this.cepStage.initModality(Modality.WINDOW_MODAL);
			this.cepStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.cepStage);
			this.cepStage.setScene(scene);
			this.cepStage.setTitle("Gestion d'un client");
			this.cepStage.setResizable(false);

			this.cepViewController = loader.getController();
			this.cepViewController.initContext(this.cepStage, this.dailyBankState);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Client doClientEditorDialog(Client client, EditionMode em) {
		return this.cepViewController.displayDialog(client, em);
	}
}

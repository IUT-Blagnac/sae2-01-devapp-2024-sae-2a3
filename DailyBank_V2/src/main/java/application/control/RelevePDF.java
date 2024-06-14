package application.control;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.orm.Access_BD_Operation;
import model.data.Client;
import model.data.Operation;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;

public class RelevePDF {

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

    public static void generateReleve(int idNumCompte, Client cc, Stage primaryStage) {
        // Récupérer le nom du propriétaire et le numéro du compte
        String nomProprietaire = "" + cc.nom + "_" + cc.prenom;
        String nomFichier = "RelevePDF_id_" + idNumCompte + "_" + nomProprietaire + ".pdf";

        try {
            // Afficher une boîte de dialogue pour choisir l'emplacement du fichier
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le Relevé PDF");
            fileChooser.setInitialFileName(nomFichier);

            // Afficher la boîte de dialogue et attendre que l'utilisateur choisisse l'emplacement
            File file = fileChooser.showSaveDialog(primaryStage);

            if (file != null) {
                // Enregistrer le PDF à l'emplacement choisi par l'utilisateur
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                addMetaData(document, "Relevé de Compte de " + nomProprietaire);
                addContent(document, idNumCompte);
                document.close();

                Alert al = new Alert(AlertType.INFORMATION);
		        al.setHeaderText("PDF Créé avec succès");
		        al.show();
            }else{
                Alert al = new Alert(AlertType.INFORMATION);
                al.setHeaderText("PDF non sauvegardé");
                al.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addMetaData(Document document, String title) {
        document.addTitle(title);
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("DailyBank");
        document.addCreator("DailyBank");
    }

    private static void addContent(Document document, int idNumCompte) throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Relevé de Compte", catFont));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Report generated for: " + System.getProperty("user.name") + ", " + new Date(), smallBold));
        addEmptyLine(preface, 2);

        // Récupérer les opérations du compte
        ArrayList<Operation> operations = getOperations(idNumCompte);

        // Créer la table des opérations
        PdfPTable table = new PdfPTable(4);

        PdfPCell c1 = new PdfPCell(new Phrase("Date Opération"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Date Valeur"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Montant"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Type Opération"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);

        for (Operation op : operations) {
            table.addCell(op.dateOp.toString());
            table.addCell(op.dateValeur.toString());
            table.addCell(String.valueOf(op.montant));
            table.addCell(op.idTypeOp);
        }

        preface.add(table);

        document.add(preface);
    }

    private static ArrayList<Operation> getOperations(int idNumCompte) {
        ArrayList<Operation> operations = new ArrayList<>();
        try {
            Access_BD_Operation ao = new Access_BD_Operation();
            operations = ao.getOperations(idNumCompte);
        } catch (DataAccessException | DatabaseConnexionException e) {
            e.printStackTrace();
        }
        return operations;
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}

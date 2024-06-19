package application.control;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.itextpdf.text.BaseColor;
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

    private static Font catFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    private static Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

    public static void generateReleve(int idNumCompte, Client cc, Stage primaryStage) {
        // Récupérer le nom du propriétaire et le numéro du compte
        String nomProprietaire = cc.nom + " " + cc.prenom;
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
                addContent(document, idNumCompte, cc);
                document.close();

                Alert al = new Alert(AlertType.INFORMATION);
                al.setHeaderText("PDF créé avec succès");
                al.show();
            } else {
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

    private static void addContent(Document document, int idNumCompte, Client cc) throws DocumentException {
        // Titre du document centré
        Paragraph title = new Paragraph("Relevé de Compte", catFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        addEmptyLine(document, 2);

        // Informations sur le client et le compte
        document.add(createInformationSection("Nom du propriétaire: ", cc.nom + " " + cc.prenom));
        document.add(createInformationSection("Adresse: ", cc.adressePostale));
        document.add(createInformationSection("Numéro de téléphone: ", cc.telephone));
        document.add(createInformationSection("Email: ", cc.email));
        document.add(createInformationSection("Numéro du compte: ", String.valueOf(idNumCompte)));

        addEmptyLine(document, 2);

        // Ajouter la section des opérations
        Paragraph operationsSection = new Paragraph("Opérations sur le compte :", smallBold);
        document.add(operationsSection);
        addEmptyLine(document, 1);

        // Récupérer les opérations du compte
        ArrayList<Operation> operations = getOperations(idNumCompte);

        // Créer la table des opérations
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        // En-têtes de colonnes
        PdfPCell c1 = new PdfPCell(new Phrase("Date Opération", tableHeaderFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.CYAN); // Couleur de fond pour l'en-tête
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Date Valeur", tableHeaderFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Montant", tableHeaderFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Type Opération", tableHeaderFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c1);

        // Ajouter les opérations avec alternance de couleurs
        boolean colorSwitch = false;
        for (Operation op : operations) {
            PdfPCell dateOpCell = new PdfPCell(new Phrase(op.dateOp.toString(), normalFont));
            dateOpCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            if (colorSwitch) {
                dateOpCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            }
            table.addCell(dateOpCell);

            PdfPCell dateValeurCell = new PdfPCell(new Phrase(op.dateValeur.toString(), normalFont));
            dateValeurCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            if (colorSwitch) {
                dateValeurCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            }
            table.addCell(dateValeurCell);

            PdfPCell montantCell = new PdfPCell(new Phrase(String.valueOf(op.montant), normalFont));
            montantCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            if (colorSwitch) {
                montantCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            }
            table.addCell(montantCell);

            PdfPCell idTypeOpCell = new PdfPCell(new Phrase(op.idTypeOp, normalFont));
            idTypeOpCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            if (colorSwitch) {
                idTypeOpCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            }
            table.addCell(idTypeOpCell);

            colorSwitch = !colorSwitch; // Alterner les couleurs des lignes
        }

        document.add(table);
    }

    private static Paragraph createInformationSection(String label, String value) {
        Paragraph p = new Paragraph();
        p.add(new Phrase(label, smallBold));
        p.add(new Phrase(value, normalFont));
        p.setAlignment(Element.ALIGN_LEFT);
        return p;
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

    private static void addEmptyLine(Document document, int number) throws DocumentException {
        for (int i = 0; i < number; i++) {
            document.add(new Paragraph(" "));
        }
    }
}

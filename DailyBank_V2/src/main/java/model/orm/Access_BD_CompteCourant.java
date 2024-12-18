package model.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.Alert.AlertType;
import application.tools.AlertUtilities;
import model.data.CompteCourant;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.ManagementRuleViolation;
import model.orm.exception.Order;
import model.orm.exception.RowNotFoundOrTooManyRowsException;
import model.orm.exception.Table;

/**
 *
 * Classe d'accès aux CompteCourant en BD Oracle.
 *
 */
public class Access_BD_CompteCourant {

	public Access_BD_CompteCourant() {
	}

	/**
	 * Recherche des CompteCourant d'un client à partir de son id.
	 *
	 * @param idNumCli id du client dont on cherche les comptes
	 * @return Tous les CompteCourant de idNumCli (ou liste vide)
	 * @throws DataAccessException        Erreur d'accès aux données (requête mal
	 *                                    formée ou autre)
	 * @throws DatabaseConnexionException Erreur de connexion
	 */
	public ArrayList<CompteCourant> getCompteCourants(int idNumCli)
			throws DataAccessException, DatabaseConnexionException {

		ArrayList<CompteCourant> alResult = new ArrayList<>();

		try {
			Connection con = LogToDatabase.getConnexion();
			String query = "SELECT * FROM CompteCourant where idNumCli = ?";
			query += " ORDER BY idNumCompte";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idNumCli);
			System.err.println(query);

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int idNumCompte = rs.getInt("idNumCompte");
				int debitAutorise = rs.getInt("debitAutorise");
				double solde = rs.getDouble("solde");
				String estCloture = rs.getString("estCloture");
				int idNumCliTROUVE = rs.getInt("idNumCli");

				alResult.add(new CompteCourant(idNumCompte, debitAutorise, solde, estCloture, idNumCliTROUVE));
			}
			rs.close();
			pst.close();
		} catch (SQLException e) {
			throw new DataAccessException(Table.CompteCourant, Order.SELECT, "Erreur accès", e);
		}

		return alResult;
	}

	/**
	 * Recherche d'un CompteCourant à partir de son id (idNumCompte).
	 *
	 * @param idNumCompte id du compte (clé primaire)
	 * @return Le compte ou null si non trouvé
	 * @throws RowNotFoundOrTooManyRowsException La requête renvoie plus de 1 ligne
	 * @throws DataAccessException               Erreur d'accès aux données (requête
	 *                                           mal formée ou autre)
	 * @throws DatabaseConnexionException        Erreur de connexion
	 */
	public CompteCourant getCompteCourant(int idNumCompte)
			throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		try {
			CompteCourant cc;

			Connection con = LogToDatabase.getConnexion();

			String query = "SELECT * FROM CompteCourant where" + " idNumCompte = ?";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idNumCompte);

			System.err.println(query);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				int idNumCompteTROUVE = rs.getInt("idNumCompte");
				int debitAutorise = rs.getInt("debitAutorise");
				double solde = rs.getDouble("solde");
				String estCloture = rs.getString("estCloture");
				int idNumCliTROUVE = rs.getInt("idNumCli");

				cc = new CompteCourant(idNumCompteTROUVE, debitAutorise, solde, estCloture, idNumCliTROUVE);
			} else {
				rs.close();
				pst.close();
				return null;
			}

			if (rs.next()) {
				throw new RowNotFoundOrTooManyRowsException(Table.CompteCourant, Order.SELECT,
						"Recherche anormale (en trouve au moins 2)", null, 2);
			}
			rs.close();
			pst.close();
			return cc;
		} catch (SQLException e) {
			throw new DataAccessException(Table.CompteCourant, Order.SELECT, "Erreur accès", e);
		}
	}

	/**
	 * Mise à jour d'un CompteCourant.
	 *
	 * cc.idNumCompte (clé primaire) doit exister seul cc.debitAutorise est mis à
	 * jour cc.solde non mis à jour (ne peut se faire que par une opération)
	 * cc.idNumCli non mis à jour (un cc ne change pas de client)
	 *
	 * @param cc IN cc.idNumCompte (clé primaire) doit exister seul
	 * @throws RowNotFoundOrTooManyRowsException La requête modifie 0 ou plus de 1
	 *                                           ligne
	 * @throws DataAccessException               Erreur d'accès aux données (requête
	 *                                           mal formée ou autre)
	 * @throws DatabaseConnexionException        Erreur de connexion
	 * @throws ManagementRuleViolation           Erreur sur le solde courant par
	 *                                           rapport au débitAutorisé (solde <
	 *                                           débitAutorisé)
	 */
	public void updateCompteCourant(CompteCourant cc) throws RowNotFoundOrTooManyRowsException, DataAccessException,
			DatabaseConnexionException, ManagementRuleViolation {
		try {
			if (cc.debitAutorise > 0) {
				cc.debitAutorise = -cc.debitAutorise;
			}
			if (cc.solde < cc.debitAutorise) {
				throw new ManagementRuleViolation(Table.CompteCourant, Order.UPDATE,
						"Erreur de règle de gestion : sole à découvert", null);
			}
			Connection con = LogToDatabase.getConnexion();

			String query = "UPDATE CompteCourant SET " + "debitAutorise = ? " + "WHERE idNumCompte = ?";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, cc.debitAutorise);
			pst.setInt(2, cc.idNumCompte);

			System.err.println(query);

			int result = pst.executeUpdate();
			pst.close();
			if (result != 1) {
				con.rollback();
				throw new RowNotFoundOrTooManyRowsException(Table.CompteCourant, Order.UPDATE,
						"Update anormal (update de moins ou plus d'une ligne)", null, result);
			}
			con.commit();
		} catch (SQLException e) {
			throw new DataAccessException(Table.CompteCourant, Order.UPDATE, "Erreur accès", e);
		}
	}

	/**
	 * Clôture d'un CompteCourant.
	 * 
	 * @param cc Le compte à clôturer
	 * @throws RowNotFoundOrTooManyRowsException
	 * @throws DataAccessException
	 * @throws DatabaseConnexionException
	 * @throws ManagementRuleViolation
	 * @author SHULHINA Daria
	 */
	public void cloturerCompte(CompteCourant cc) 
	throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException, ManagementRuleViolation {
		try {
			CompteCourant cAvant = this.getCompteCourant(cc.idNumCompte);
			if (cc.debitAutorise > 0) {
				cc.debitAutorise = -cc.debitAutorise;
			}
			if (cAvant.solde < cc.debitAutorise) {
				throw new ManagementRuleViolation(Table.CompteCourant, Order.UPDATE,
				"Erreur de règle de gestion : sole à découvert", null);
			}
			Connection con = LogToDatabase.getConnexion();
			String query = "UPDATE CompteCourant SET ESTCLOTURE = 'O' WHERE idNumCompte = ?";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, cc.idNumCompte);
			
			System.err.println(query);
			
			int result = pst.executeUpdate();
			pst.close();
			
			if (result != 1) {
				con.rollback();
				throw new RowNotFoundOrTooManyRowsException(Table.CompteCourant, Order.UPDATE,
				"Update anormal (update de moins ou plus d'une ligne)", null, result);
			}
			con.commit();
		} catch (SQLException e) {
			throw new DataAccessException(Table.CompteCourant, Order.UPDATE, 
			"Erreur accès", e);
		}
	}

	/**
	 * Insertion d'un nouveau compte 
	 * 
	 * @param compteC le compte courant à insérer 
	 * @throws DataAccessException pour les probleme lies au acces donnees
	 * @throws DatabaseConnexionException pour l'erreur de connexion à la base de donnees
	 * @throws RowNotFoundOrTooManyRowsException La requête modifie 0 ou plus de 1 ligne
	 * @throws ManagementRuleViolation 
	 * @author Mialisoa
	 */
	public void insertCompte(CompteCourant compteC) throws DataAccessException, DatabaseConnexionException, RowNotFoundOrTooManyRowsException, ManagementRuleViolation {
		try{
			Connection con = LogToDatabase.getConnexion(); 

			String query = "INSERT INTO CompteCourant VALUES ("+ "seq_id_compte.NEXTVAL" + ", "+"?" + ", " + "?" +", " +"?" + ", " + "?" + ")";

			PreparedStatement pst = con.prepareStatement(query); 
            pst.setInt(1, -compteC.debitAutorise);
            pst.setDouble(2, compteC.solde);
			pst.setInt(3, compteC.idNumCli);
            pst.setString(4, compteC.estCloture);
            

			int result = pst.executeUpdate(); 
			pst.close(); 
			if (compteC.solde <= 0){
				throw new ManagementRuleViolation(Table.CompteCourant, Order.INSERT, "Erreur de règle de gestion : montant initial doit être supérieur à 0", null); 
			}
			if(result != 1){
				con.rollback(); 
				throw new RowNotFoundOrTooManyRowsException(Table.CompteCourant, Order.INSERT, "Insertion anormale (insert de moins ou plus d'une ligne)", null, result);
			}
			query = "SELECT seq_id_compte.CURRVAL from DUAL"; 
			PreparedStatement pst2 = con.prepareStatement(query);
			ResultSet rs = pst2.executeQuery();
			rs.next();
			int numCompteBase = rs.getInt(1);
			con.commit(); 
			rs.close();
			pst2.close();
			compteC.idNumCompte = numCompteBase; 
		}catch(SQLException e){
			throw new DataAccessException(Table.CompteCourant, Order.INSERT, "Erreur acces", e); 
		}
	}

	/**
     * Suppression d'un compte clôturé en vérifiant la date de la dernière opération
     *
     * @param idNumCompte id du compte à supprimer
     * @throws DataAccessException        Erreur d'accès aux données
     * @throws DatabaseConnexionException Erreur de connexion
     * @throws ManagementRuleViolation    Si le compte n'est pas clôturé ou s'il n'y a pas de date de dernière opération
	* @throws RowNotFoundOrTooManyRowsException 
 	* @author SHULHINA Daria
     */
    public void supprimerCompte(int idNumCompte) throws DataAccessException, DatabaseConnexionException, ManagementRuleViolation, RowNotFoundOrTooManyRowsException {
        try {
            Connection con = LogToDatabase.getConnexion();

            // Vérifier que le compte est clôturé
            String query = "SELECT estCloture FROM CompteCourant WHERE idNumCompte = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, idNumCompte);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String estCloture = rs.getString("estCloture");
                if (!"O".equals(estCloture)) {
                    throw new ManagementRuleViolation(Table.CompteCourant, Order.DELETE, "Le compte n'est pas clôturé", null);
                }
            } else {
                throw new ManagementRuleViolation(Table.CompteCourant, Order.DELETE, "Compte non trouvé", null);
            }
            rs.close();
            pst.close();

            // Récupérer la date de la dernière opération
            query = "SELECT MAX(dateOp) as derniereDateOp FROM Operation WHERE idNumCompte = ?";
            pst = con.prepareStatement(query);
            pst.setInt(1, idNumCompte);
            rs = pst.executeQuery();
            if (rs.next()) {
                java.sql.Date derniereDateOp = rs.getDate("derniereDateOp");
                if (derniereDateOp != null) {
                    // Supprimer les opérations du compte
                    query = "DELETE FROM Operation WHERE idNumCompte = ?";
                    PreparedStatement pstDelOp = con.prepareStatement(query);
                    pstDelOp.setInt(1, idNumCompte);
                    pstDelOp.executeUpdate();
                    pstDelOp.close();

                    // Supprimer le compte
                    query = "DELETE FROM CompteCourant WHERE idNumCompte = ?";
                    PreparedStatement pstDelCompte = con.prepareStatement(query);
                    pstDelCompte.setInt(1, idNumCompte);
                    int result = pstDelCompte.executeUpdate();
                    pstDelCompte.close();

                    if (result != 1) {
                        con.rollback();
                        throw new RowNotFoundOrTooManyRowsException(Table.CompteCourant, Order.DELETE, "Suppression anormale (suppression de moins ou plus d'une ligne)", null, result);
                    }
                    con.commit();
                }
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            throw new DataAccessException(Table.CompteCourant, Order.DELETE, "Erreur accès", e);
        }
    }
}

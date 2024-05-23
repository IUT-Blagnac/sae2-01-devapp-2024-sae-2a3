package model.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.data.Employe;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.Order;
import model.orm.exception.RowNotFoundOrTooManyRowsException;
import model.orm.exception.Table;

/**
 * Classe d'accès aux Employe en BD Oracle.
 */
public class Access_BD_Employe {

	public Access_BD_Employe() {
	}

	/**
	 * Recherche d'un employé par son login / mot de passe.
	 *
	 * @param login    login de l'employé recherché
	 * @param password mot de passe donné
	 * @return un Employe ou null si non trouvé
	 * @throws RowNotFoundOrTooManyRowsException La requête renvoie plus de 1 ligne
	 * @throws DataAccessException               Erreur d'accès aux données (requête
	 *                                           mal formée ou autre)
	 * @throws DatabaseConnexionException        Erreur de connexion
	 */
	public Employe getEmploye(String login, String password)
			throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {

		Employe employeTrouve;

		try {
			Connection con = LogToDatabase.getConnexion();
			String query = "SELECT * FROM Employe WHERE" + " login = ?" + " AND motPasse = ?";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setString(1, login);
			pst.setString(2, password);

			ResultSet rs = pst.executeQuery();

			System.err.println(query);

			if (rs.next()) {
				int idEmployeTrouve = rs.getInt("idEmploye");
				String nom = rs.getString("nom");
				String prenom = rs.getString("prenom");
				String droitsAccess = rs.getString("droitsAccess");
				String loginTROUVE = rs.getString("login");
				String motPasseTROUVE = rs.getString("motPasse");
				int idAgEmploye = rs.getInt("idAg");

				employeTrouve = new Employe(idEmployeTrouve, nom, prenom, droitsAccess, loginTROUVE, motPasseTROUVE,
						idAgEmploye);
			} else {
				rs.close();
				pst.close();
				// Non trouvé
				return null;
			}

			if (rs.next()) {
				// Trouvé plus de 1 ... bizarre ...
				rs.close();
				pst.close();
				throw new RowNotFoundOrTooManyRowsException(Table.Employe, Order.SELECT,
						"Recherche anormale (en trouve au moins 2)", null, 2);
			}
			rs.close();
			pst.close();
			return employeTrouve;
		} catch (SQLException e) {
			throw new DataAccessException(Table.Employe, Order.SELECT, "Erreur accès", e);
		}
	}

    public void updateEmploye(Employe result) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateEmploye'");
    }

    public void insertEmploye(Employe employe) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insertEmploye'");
    }

    public Employe getEmployes(int idAg, int _numCompte, String _debutNom, String _debutPrenom) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEmployes'");
    }

	public void deleteEmploye(Employe employe) throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		try {
			// Obtenir une connexion à la base de données
			Connection con = LogToDatabase.getConnexion();
			
			// Préparer la requête SQL DELETE
			String query = "DELETE FROM Employe WHERE idEmploye = ?";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, employe.idEmploye);
			
			// Exécuter la requête
			int rowCount = pst.executeUpdate();
			
			// Vérifier le nombre de lignes affectées
			if (rowCount == 0) {
				// Aucune ligne n'a été supprimée
				pst.close();
				throw new RowNotFoundOrTooManyRowsException(Table.Employe, Order.DELETE, "Aucune ligne supprimée", null, 0);
			} else if (rowCount > 1) {
				// Plus d'une ligne a été supprimée, ce qui est anormal
				pst.close();
				throw new RowNotFoundOrTooManyRowsException(Table.Employe, Order.DELETE, "Plus d'une ligne supprimée", null, rowCount);
			}
			
			// Fermer le PreparedStatement
			pst.close();
		} catch (SQLException e) {
			throw new DataAccessException(Table.Employe, Order.DELETE, "Erreur accès", e);
		}
	}
	
}

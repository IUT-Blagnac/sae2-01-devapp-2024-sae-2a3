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

	public ArrayList<Employe> getEmployes(int idAg, int idEmp, String debutNom, String debutPrenom) 
				throws DataAccessException, DatabaseConnexionException {

        ArrayList<Employe> alResult = new ArrayList<>();
		try {
			Connection con = LogToDatabase.getConnexion();

			PreparedStatement pst;
			String query = "SELECT * FROM Employe WHERE idAg = ?";;

			if (idAg != -1) {

				if(idEmp == -1){
					pst = con.prepareStatement(query);
					pst.setInt(1, idAg);
				}else{
					query += " AND idEmploye = ?";
					query += " ORDER BY nom";
					pst = con.prepareStatement(query);
					pst.setInt(1, idAg);
					pst.setInt(2, idEmp);
				}

			}else if (!debutNom.isEmpty() || !debutPrenom.isEmpty()) {
					query += " AND UPPER(nom) LIKE ? AND UPPER(prenom) LIKE ?";
					pst = con.prepareStatement(query);
					pst.setInt(1, idAg);
					pst.setString(2, debutNom.toUpperCase() + "%");
					pst.setString(3, debutPrenom.toUpperCase() + "%");
			} else {
				query = "SELECT * FROM Employe";
				if (!debutNom.isEmpty() || !debutPrenom.isEmpty()) {
					query += " WHERE UPPER(nom) LIKE ? AND UPPER(prenom) LIKE ?";
					pst = con.prepareStatement(query);
					pst.setString(1, debutNom.toUpperCase() + "%");
					pst.setString(2, debutPrenom.toUpperCase() + "%");
				} else {
					pst = con.prepareStatement(query);
				}
			}

			System.err.println(query);

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int idEmploye = rs.getInt("idEmploye");
				String nom = rs.getString("nom");
				String prenom = rs.getString("prenom");
				String droitsAccess = rs.getString("droitsAccess");
				String login = rs.getString("login");
				String motPasse = rs.getString("motPasse");
				int idAgEmploye = rs.getInt("idAg");

				alResult.add(new Employe(idEmploye, nom, prenom, droitsAccess, login, motPasse, idAgEmploye));
			}
			rs.close();
			pst.close();
		} catch (SQLException e) {
			throw new DataAccessException(Table.Employe, Order.SELECT, "Erreur accès", e);
		}

		return alResult;
    }

    public void updateEmploye(Employe employe) throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		try {
			Connection con = LogToDatabase.getConnexion();

			String query = "UPDATE Employe SET " + 
						"nom = ?, " + 
						"prenom = ?, " + 
						"droitsAccess = ?, " + 
						"login = ?, " + 
						"motPasse = ? " + 
						"WHERE idEmploye = ?";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setString(1, employe.nom);
			pst.setString(2, employe.prenom);
			pst.setString(3, employe.droitsAccess);
			pst.setString(4, employe.login);
			pst.setString(5, employe.motPasse);
			pst.setInt(6, employe.idEmploye);

			System.err.println(query);

			int result = pst.executeUpdate();
			pst.close();
			if (result != 1) {
				con.rollback();
				throw new RowNotFoundOrTooManyRowsException(Table.Employe, Order.UPDATE,
						"Update anormal (update de moins ou plus d'une ligne)", null, result);
			}
			con.commit();
		} catch (SQLException e) {
			throw new DataAccessException(Table.Employe, Order.UPDATE, "Erreur accès", e);
		}
	}

    /**
	 * Insertion d'un employé.
	 * @param employe IN/OUT Tous les attributs IN sauf idNumEmp en OUT
	 * @throws RowNotFoundOrTooManyRowsException La requête insère 0 ou plus de 1 ligne
	 * @throws DataAccessException Erreur d'accès aux données (requête mal formée ou autre)
	 * @throws DatabaseConnexionException Erreur de connexion
	 */
    public void insertEmploye(Employe employe) throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		try {

			Connection con = LogToDatabase.getConnexion();

			String query = "INSERT INTO EMPLOYE VALUES (" + "seq_id_employe.NEXTVAL" + ", " + "?" + ", " + "?" + ", "
					+ "?" + ", " + "?" + ", " + "?" + ", " + "?" + ")"; 
			PreparedStatement pst = con.prepareStatement(query);
			pst.setString(1, employe.nom);
			pst.setString(2, employe.prenom);
			pst.setString(3, employe.droitsAccess);
			pst.setString(4, employe.login);
			pst.setString(5, employe.motPasse);
			pst.setInt(6, employe.idAg);

			System.err.println(query);

			int result = pst.executeUpdate();
			pst.close();

			if (result != 1) {
				con.rollback();
				throw new RowNotFoundOrTooManyRowsException(Table.Employe, Order.INSERT,
						"Insert anormal (insert de moins ou plus d'une ligne)", null, result);
			}

			query = "SELECT seq_id_employe.CURRVAL from DUAL";

			System.err.println(query);
			PreparedStatement pst2 = con.prepareStatement(query);

			ResultSet rs = pst2.executeQuery();
			rs.next();
			int numEmpBase = rs.getInt(1);

			con.commit();
			rs.close();
			pst2.close();

			employe.idEmploye = numEmpBase;

		} catch (SQLException e) {
			throw new DataAccessException(Table.Employe, Order.INSERT, "Erreur accès", e);
		}
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

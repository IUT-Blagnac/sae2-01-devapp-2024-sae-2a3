package model.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.data.Prelevement;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.Order;
import model.orm.exception.RowNotFoundOrTooManyRowsException;
import model.orm.exception.Table;

/**
 * Classe d'accès aux Prelevement en BD Oracle.
 * 
 */
public class Access_BD_Prelevement {

	public Access_BD_Prelevement() {
	}

	/**
	 * Recherche de tout les prélévements automatiques d'un compte.
	 *
	 * @param idAg        id de l'agence de l'employé effecuant la recherche
	 * @param idNumCompte id du compte dont on cherche tout les prévélements
	 *                    automatiques (-1 pour tout les comptes)
	 * @return Tout les prélévements automatiques du compte, liste vide si pas
	 *         de prélévement automatique
	 * @throws DataAccessException        Erreur d'accès aux données (requête mal
	 *                                    formée ou autre)
	 * @throws DatabaseConnexionException Erreur de connexion
	 */
	public ArrayList<Prelevement> getPrelevements(int idAg, int idNumCompte)
			throws DataAccessException, DatabaseConnexionException {

		ArrayList<Prelevement> alResult = new ArrayList<>();

		try {
			Connection con = LogToDatabase.getConnexion();
			String query = "SELECT PrelevementAutomatique.idPrelev, PrelevementAutomatique.montant, PrelevementAutomatique.dateRecurrente, PrelevementAutomatique.beneficiaire, PrelevementAutomatique.idNumCompte FROM PrelevementAutomatique, CompteCourant, Client WHERE PrelevementAutomatique.idNumCompte = CompteCourant.idNumCompte AND CompteCourant.idNumCli = Client.idNumCli AND Client.idAg = ?";
			if (idNumCompte != -1) {
				query += " AND PrelevementAutomatique.idNumCompte = ?";
			}
			query += " ORDER BY PrelevementAutomatique.dateRecurrente";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idAg);
			if (idNumCompte != -1) {
				pst.setInt(2, idNumCompte);
			}

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				int idPrelevement = rs.getInt("idPrelev");
				int montant = rs.getInt("montant");
				int date = rs.getInt("dateRecurrente");
				String beneficiaire = rs.getString("beneficiaire");
				int idNumCompteTrouve = rs.getInt("idNumCompte");

				alResult.add(new Prelevement(idPrelevement, montant, date, beneficiaire, idNumCompteTrouve));
			}
			rs.close();
			pst.close();
			return alResult;
		} catch (SQLException e) {
			throw new DataAccessException(Table.Operation, Order.SELECT, "Erreur accès", e);
		}
	}

	/**
	 * Insertion d'un prelevement dans la base de données.
	 * 
	 * @param pre Prelevement à insérer
	 * @throws RowNotFoundOrTooManyRowsException Erreur d'accès aux données
	 * @throws DataAccessException               Erreur d'accès aux données (requête
	 *                                           mal formée ou autre)
	 * @throws DatabaseConnexionException        Erreur de connexion
	 */
	public void insertPrelevement(Prelevement pre)
			throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		try {

			Connection con = LogToDatabase.getConnexion();

			String query = "INSERT INTO PRELEVEMENTAUTOMATIQUE VALUES (" + "seq_id_prelevauto.NEXTVAL" + ", " + "?"
					+ ", " + "?" + ", "
					+ "?" + ", " + "?" + ")";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, pre.montant);
			pst.setInt(2, pre.date);
			pst.setString(3, pre.beneficiaire);
			pst.setInt(4, pre.idNumCompte);

			System.err.println(query);

			int result = pst.executeUpdate();
			pst.close();

			if (result != 1) {
				con.rollback();
				throw new RowNotFoundOrTooManyRowsException(Table.PrelevementAutomatique, Order.INSERT,
						"Insert anormal (insert de moins ou plus d'une ligne)", null, result);
			}

			query = "SELECT seq_id_prelevauto.CURRVAL from DUAL";

			System.err.println(query);
			PreparedStatement pst2 = con.prepareStatement(query);

			ResultSet rs = pst2.executeQuery();
			rs.next();
			// int numCliBase = rs.getInt(1);

			con.commit();
			rs.close();
			pst2.close();

			// client.idNumCli = numCliBase;
		} catch (SQLException e) {
			throw new DataAccessException(Table.PrelevementAutomatique, Order.INSERT, "Erreur accès", e);
		}
	}

	/**
	 * Mise à jour d'un prélévement automatique.
	 *
	 * PrelevementAutomatique.idPrelev est la clé primaire et doit exister tous les
	 * autres champs
	 * sont des mises à jour.
	 *
	 * @param pre IN PrelevementAutomatique.idPrelev (clé primaire) doit exister
	 * @throws RowNotFoundOrTooManyRowsException La requête modifie 0 ou plus de 1
	 *                                           ligne
	 * @throws DataAccessException               Erreur d'accès aux données (requête
	 *                                           mal formée ou autre)
	 * @throws DatabaseConnexionException        Erreur de connexion
	 */
	public void modifierPrelevement(Prelevement pre)
			throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		try {
			Connection con = LogToDatabase.getConnexion();

			String query = "UPDATE PRELEVEMENTAUTOMATIQUE SET " + "montant = " + "? , " + "dateRecurrente = " + "? , "
					+ "beneficiaire = " + "? , " + "idNumCompte = " + "? " + " " + "WHERE idPrelev = ? ";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, pre.montant);
			pst.setInt(2, pre.date);
			pst.setString(3, pre.beneficiaire);
			pst.setInt(4, pre.idNumCompte);
			pst.setInt(5, pre.idPrelevement);

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
	 * Suppression d'un prélévement automatique.
	 *
	 * @param pre IN PrelevementAutomatique.idPrelev (clé primaire) doit exister
	 * @throws RowNotFoundOrTooManyRowsException La requête modifie 0 ou plus de 1
	 *                                           ligne
	 * @throws DataAccessException               Erreur d'accès aux données (requête
	 *                                           mal formée ou autre)
	 * @throws DatabaseConnexionException        Erreur de connexion
	 */
	public void supprimerPrelevement(Prelevement pre)
			throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		try {
			Connection con = LogToDatabase.getConnexion();

			String query = "DELETE FROM PrelevementAutomatique WHERE idPrelev = ?";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, pre.idPrelevement);

			System.err.println(query);

			int result = pst.executeUpdate();
			pst.close();
			if (result != 1) {
				con.rollback();
				throw new RowNotFoundOrTooManyRowsException(Table.Employe, Order.UPDATE,
						"Suppresion anormale (suppresion de moins ou plus d'une ligne)", null, result);
			}
			con.commit();
		} catch (SQLException e) {
			throw new DataAccessException(Table.Employe, Order.DELETE, "Erreur accès", e);
		}
	}

	/**
	 * Permet de vérifier si le compte à l'origine du prélévement existe dans
	 * l'agence.
	 *
	 * @return -1 en cas d'erreur, 0 si le compte n'existe pas dans l'agence, 1 si
	 *         le compte existe dans l'agence
	 * @param idCpt id du compte à vérifier
	 * @param idAg  id de l'agence à vérifier
	 * @throws RowNotFoundOrTooManyRowsException La requête renvoie plus de 1 ligne
	 * @throws DataAccessException               Erreur d'accès aux données (requête
	 *                                           mal formée ou autre)
	 * @throws DatabaseConnexionException        Erreur de connexion
	 */
	public int verifierPrelemevent(int idCpt, int idAg)
			throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {

		try {
			Connection con = LogToDatabase.getConnexion();
			String query = "SELECT COUNT(*) AS existe FROM CompteCourant, Client WHERE Client.idNumCli = CompteCourant.idNumCli AND CompteCourant.idNumCompte = ? AND Client.idAg = ?";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idCpt);
			pst.setInt(2, idAg);
			ResultSet rs = pst.executeQuery();

			int existe;
			if (rs.next()) {
				existe = rs.getInt("existe");
			} else {
				// Non trouvé ...
				rs.close();
				pst.close();
				return -1;
			}

			if (rs.next()) {
				// Plus de 2 ? Bizarre ...
				rs.close();
				pst.close();
				throw new RowNotFoundOrTooManyRowsException(Table.Client, Order.SELECT,
						"Recherche anormale (en trouve au moins 2)", null, 2);
			}
			rs.close();
			pst.close();
			return existe;
		} catch (SQLException e) {
			throw new DataAccessException(Table.Client, Order.SELECT, "Erreur accès", e);
		}
	}
}
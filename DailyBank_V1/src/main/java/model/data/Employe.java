package model.data;

/**
 * Classe Employe
 * 
 * @author IUT Blagnac
 */

public class Employe {

	public int idEmploye;
	public String nom, prenom, droitsAccess;
	public String login, motPasse;

	public int idAg;

	/**
	 * Constructeur de la classe Employe avec tous les paramètres.
	 * 
	 * @param idEmploye    L'identifiant de l'employé
	 * @param nom          Le nom de l'employé
	 * @param prenom       Le prénom de l'employé
	 * @param droitsAccess Les droits d'accès de l'employé ("chefAgence" ou
	 *                     "guichetier")
	 * @param login        Le login de l'employé
	 * @param motPasse     Le mot de passe de l'employé
	 * @param idAg         L'identifiant de l'agence bancaire de l'employé
	 * @author IUT Blagnac
	 */
	public Employe(int idEmploye, String nom, String prenom, String droitsAccess, String login, String motPasse,
			int idAg) {
		super();
		this.idEmploye = idEmploye;
		this.nom = nom;
		this.prenom = prenom;
		this.droitsAccess = droitsAccess;
		this.login = login;
		this.motPasse = motPasse;
		this.idAg = idAg;
	}

	/**
	 * Constructeur de la classe Employe à partir d'un Employe.
	 * 
	 * @param e L'employé à copier
	 * @author IUT Blagnac
	 */
	public Employe(Employe e) {
		this(e.idEmploye, e.nom, e.prenom, e.droitsAccess, e.login, e.motPasse, e.idAg);
	}

	/**
	 * Constructeur de la classe Employe sans paramètres.
	 * 
	 * Valeurs par défaut :
	 * - idEmploye : -1000
	 * - nom : null
	 * - prenom : null
	 * - droitsAccess : null
	 * - login : null
	 * - motPasse : null
	 * - idAg : -1000
	 * 
	 * @author IUT Blagnac
	 */
	public Employe() {
		this(-1000, null, null, null, null, null, -1000);
	}

	/**
	 * Méthode toString de la classe Employe.
	 * 
	 * @return String L'employé sous forme de chaîne de caractères
	 * @author IUT Blagnac
	 */
	@Override
	public String toString() {
		return "Employe [idEmploye=" + this.idEmploye + ", nom=" + this.nom + ", prenom=" + this.prenom
				+ ", droitsAccess=" + this.droitsAccess + ", login=" + this.login + ", motPasse=" + this.motPasse
				+ ", idAg=" + this.idAg + "]";
	}

}

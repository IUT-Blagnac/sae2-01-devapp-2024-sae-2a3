package model.data;

/**
 * Classe Client
 * 
 * @author IUT Blagnac
 */

public class Client {

	public int idNumCli;
	public String nom, prenom, adressePostale, email, telephone;
	public String estInactif;

	public int idAg;

	/**
	 * Constructeur de la classe Client avec tous les paramètres.
	 * 
	 * @param idNumCli       L'identifiant du client
	 * @param nom            Le nom du client
	 * @param prenom         Le prénom du client
	 * @param adressePostale L'adresse postale du client
	 * @param email          L'email du client
	 * @param telephone      Le téléphone du client
	 * @param estInactif     L'état du client ("O" pour inactif sinon "N" - @see ConstantesIHM)
	 * @param idAg           L'identifiant de l'agence bancaire du client
	 * @author IUT Blagnac
	 */
	public Client(int idNumCli, String nom, String prenom, String adressePostale, String email, String telephone,
			String estInactif, int idAg) {
		super();
		this.idNumCli = idNumCli;
		this.nom = nom;
		this.prenom = prenom;
		this.adressePostale = adressePostale;
		this.email = email;
		this.telephone = telephone;
		this.estInactif = estInactif;
		this.idAg = idAg;
	}

	/**
	 * Constructeur de la classe Client à partir d'un Client.
	 * 
	 * @param c Le client à copier
	 * @author IUT Blagnac
	 */
	public Client(Client c) {
		this(c.idNumCli, c.nom, c.prenom, c.adressePostale, c.email, c.telephone, c.estInactif, c.idAg);
	}

	/**
	 * Constructeur de la classe Client sans paramètres.
	 * 
	 * Valeurs par défaut :
	 * - idNumCli : -1000
	 * - nom : null
	 * - prenom : null
	 * - adressePostale : null
	 * - email : null
	 * - telephone : null
	 * - estInactif : "N"
	 * - idAg : -1000
	 * 
	 * @author IUT Blagnac
	 */
	public Client() {
		this(-1000, null, null, null, null, null, "Non", -1000);
	}

	/**
	 * Méthode toString de la classe Client.
	 * 
	 * @return String Le client sous forme de chaîne de caractères
	 * @author IUT Blagnac
	 */
	@Override
	public String toString() {
		return "[" + this.idNumCli + "]  " + this.nom.toUpperCase() + " " + this.prenom + "(" + this.email + ")  {"
				+ this.telephone + "}";
	}

}

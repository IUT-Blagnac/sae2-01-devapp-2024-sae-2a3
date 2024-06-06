package model.data;

/**
 * Classe AgenceBancaire
 * 
 * @author IUT Blagnac
 */

public class AgenceBancaire {

	public int idAg;

	public String nomAg;
	public String adressePostaleAg;
	public int idEmployeChefAg;

	/**
	 * Constructeur de la classe AgenceBancaire avec tous les paramètres.
	 * 
	 * @param idAg             L'identifiant de l'agence bancaire
	 * @param nomAg            Le nom de l'agence bancaire
	 * @param adressePostaleAg L'adresse postale de l'agence bancaire
	 * @param idEmployeChefAg  L'identifiant de l'employé chef de l'agence bancaire
	 * @author IUT Blagnac
	 */
	public AgenceBancaire(int idAg, String nomAg, String adressePostaleAg, int idEmployeChefAg) {
		super();
		this.idAg = idAg;
		this.nomAg = nomAg;
		this.adressePostaleAg = adressePostaleAg;
		this.idEmployeChefAg = idEmployeChefAg;
	}

	/**
	 * Constructeur de la classe AgenceBancaire à partir d'une AgenceBancaire.
	 * 
	 * @param ag L'agence bancaire à copier
	 * @author IUT Blagnac
	 */
	public AgenceBancaire(AgenceBancaire ag) {
		this(ag.idAg, ag.nomAg, ag.adressePostaleAg, ag.idEmployeChefAg);
	}

	/**
	 * Constructeur de la classe AgenceBancaire sans paramètres.
	 * 
	 * Valeurs par défaut :
	 * - idAg : -1000
	 * - nomAg : null
	 * - adressePostaleAg : null
	 * - idEmployeChefAg : -1000
	 * 
	 * @author IUT Blagnac
	 */
	public AgenceBancaire() {
		this(-1000, null, null, -1000);
	}

	/**
	 * Méthode toString de la classe AgenceBancaire.
	 * 
	 * @return String L'agence bancaire sous forme de chaîne de caractères
	 * @author IUT Blagnac
	 */
	@Override
	public String toString() {
		return "AgenceBancaire [idAg=" + this.idAg + ", nomAg=" + this.nomAg + ", adressePostaleAg="
				+ this.adressePostaleAg + ", idEmployeChefAg=" + this.idEmployeChefAg + "]";
	}
}

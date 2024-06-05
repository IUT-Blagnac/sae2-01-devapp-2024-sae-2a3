package model.data;

/**
 * Classe Prelevement
 * 
 */
public class Prelevement {

	public int idPrelevement, montant, date, idNumCompte;
	public String beneficiaire;

	/**
	 * Constructeur de la classe Prelevement avec tous les paramètres.
	 * 
	 * @param idPrelevement L'identifiant du prélèvement
	 * @param montant       Le montant du prélèvement
	 * @param date          La date du prélèvement
	 * @param beneficiaire  Le bénéficiaire du prélèvement
	 * @param idNumCompte   L'identifiant du numéro de compte du prélèvement
	 */
	public Prelevement(int idPrelevement, int montant, int date, String beneficiaire, int idNumCompte) {
		this.idPrelevement = idPrelevement;
		this.montant = montant;
		this.date = date;
		this.beneficiaire = beneficiaire;
		this.idNumCompte = idNumCompte;
	}

	/**
	 * Constructeur de la classe Prelevement à partir d'un Prelevement.
	 * 
	 * @param pm Le prélèvement à copier
	 */
	public Prelevement(Prelevement pm) {
		this(pm.idPrelevement, pm.montant, pm.date, pm.beneficiaire, pm.idNumCompte);
	}

	/**
	 * Constructeur de la classe Prelevement sans paramètres.
	 * 
	 * Valeurs par défaut :
	 * - idPrelevement : -1000
	 * - montant : -1000
	 * - date : -1000
	 * - beneficiaire : null
	 * - idNumCompte : -1000
	 * 
	 */
	public Prelevement() {
		this(-1000, -1000, -1000, null, -1000);
	}

	/**
	 * Méthode toString de la classe Prelevement.
	 * 
	 * @return String Le prélévement automatique sous forme de chaîne de caractères
	 */
	@Override
	public String toString() {
		return "[" + this.idPrelevement + "] Du compte n°" + this.idNumCompte + " à " + this.beneficiaire + " - " + montant + " € le " + this.date;
	}
}
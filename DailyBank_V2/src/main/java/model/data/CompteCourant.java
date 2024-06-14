package model.data;

/*
 * Attributs mis en public car cette classe ne fait que "véhiculer" des données.
 */

public class CompteCourant {

	public int idNumCompte;
	public int debitAutorise;
	public double solde;
	public String estCloture; // "O" ou "N"

	public int idNumCli;

	/**
	 * Constructeur de la classe CompteCourant avec tous les paramètres.
	 * 
	 * @param idNumCompte   L'identifiant du compte courant
	 * @param debitAutorise Le découvert autorisé du compte courant
	 * @param solde         Le solde du compte courant
	 * @param estCloture    L'état du compte courant ("O" pour oui ou "N" pour non - @see ConstantesIHM)
	 * @param idNumCli      L'identifiant du client du compte courant
	 * @author IUT Blagnac
	 */
	public CompteCourant(int idNumCompte, int debitAutorise, double solde, String estCloture, int idNumCli) {
		super();
		this.idNumCompte = idNumCompte;
		this.debitAutorise = debitAutorise;
		this.solde = solde;
		this.estCloture = estCloture;
		this.idNumCli = idNumCli;
	}

	/**
	 * Constructeur de la classe CompteCourant à partir d'un CompteCourant.
	 * 
	 * @param cc Le compte courant à copier
	 * @author IUT Blagnac
	 */
	public CompteCourant(CompteCourant cc) {
		this(cc.idNumCompte, cc.debitAutorise, cc.solde, cc.estCloture, cc.idNumCli);
	}

	/**
	 * Constructeur de la classe CompteCourant sans paramètres.
	 * 
	 * Valeurs par défaut :
	 * - idNumCompte : -1000
	 * - debitAutorise : -1000
	 * - solde : -1000
	 * - estCloture : "N"
	 * - idNumCli : -1000
	 * 
	 * @author IUT Blagnac
	 */
	public CompteCourant() {
		this(0, 0, 0, "N", -1000);
	}

	/**
	 * Méthode toString de la classe CompteCourant.
	 * 
	 * @return String Le compte courant sous forme de chaîne de caractères
	 * @author IUT Blagnac
	 */
	@Override
	public String toString() {
		String s = "" + String.format("%05d", this.idNumCompte) + " : Solde=" + String.format("%12.02f", this.solde)
				+ "  ,  Découvert Autorise=" + String.format("%8d", this.debitAutorise);
		if (this.estCloture == null) {
			s = s + " (Cloture)";
		} else {
			s = s + (this.estCloture.equals("N") ? " (Ouvert)" : " (Cloture)");
		}
		return s;
	}

}

package model.data;

import java.sql.Date;

/**
 * Classe Operation
 * 
 * @author IUT Blagnac
 */

public class Operation {

	public int idOperation;
	public double montant;
	public Date dateOp;
	public Date dateValeur;
	public int idNumCompte;
	public String idTypeOp;

	public int idNumCompteDebit;
	public int idNumCompteCredit;

	/**
	 * Constructeur de la classe Operation avec tous les paramètres.
	 * 
	 * @param idOperation L'identifiant de l'opération
	 * @param montant     Le montant de l'opération
	 * @param dateOp      La date de l'opération
	 * @param dateValeur  La date de valeur de l'opération
	 * @param idNumCompte L'identifiant du compte concerné par l'opération
	 * @param idTypeOp    L'identifiant du type d'opération
	 * @author IUT Blagnac
	 */
	public Operation(int idOperation, double montant, Date dateOp, Date dateValeur, int idNumCompte, String idTypeOp) {
		super();
		this.idOperation = idOperation;
		this.montant = montant;
		this.dateOp = dateOp;
		this.dateValeur = dateValeur;
		this.idNumCompte = idNumCompte;
		this.idTypeOp = idTypeOp;
	}

	/**
 	* Constructeur de la classe Operation avec plus les paramètres.
 	*
 	* @param idOperation 		L'identifiant de l'opération
 	* @param montant 	 		Le montant de l'opération
 	* @param dateOp 	 		La date de l'opération
 	* @param dateValeur 		La date de valeur de l'opération
 	* @param idNumCompteDebit 	Le numéro de compte sur lequel le montant est débité
 	* @param idNumCompteCredit 	Le numéro de compte sur lequel le montant est crédité
 	* @param idTypeOp 			L'identifiant du type d'opération
	* @author AMERI Mohammed
 	*/
	public Operation(int idOperation, double montant, Date dateOp, Date dateValeur, int idNumCompteDebit, int idNumCompteCredit, String idTypeOp) {
		super();
		this.idOperation = idOperation;
		this.montant = montant;
		this.dateOp = dateOp;
		this.dateValeur = dateValeur;
		this.idNumCompteDebit = idNumCompteDebit;
		this.idNumCompteCredit = idNumCompteCredit;
		this.idTypeOp = idTypeOp;
	}

	/**
	 * Constructeur de la classe Operation à partir d'une Operation.
	 * 
	 * @param o L'opération à copier
	 * @author IUT Blagnac
	 */
	public Operation(Operation o) {
		this(o.idOperation, o.montant, o.dateOp, o.dateValeur, o.idNumCompte, o.idTypeOp);
	}

	/**
	 * Constructeur de la classe Operation sans paramètres.
	 * 
	 * Valeurs par défaut :
	 * - idOperation : -1000
	 * - montant : -1000
	 * - dateOp : null
	 * - dateValeur : null
	 * - idNumCompte : -1000
	 * - idTypeOp : null
	 * 
	 * @author IUT Blagnac
	 */
	public Operation() {
		this(-1000, 0, null, null, -1000, null);
	}

	/**
	 * Méthode toString de la classe Operation.
	 * 
	 * @return String L'opération sous forme de chaîne de caractères
	 * @author IUT Blagnac
	 */
	@Override
	public String toString() {
		return this.dateOp + " : " + String.format("%25s", this.idTypeOp) + " "
				+ String.format("%10.02f", this.montant);

		// return "Operation [idOperation=" + idOperation + ", montant=" + montant + ", dateOp=" + dateOp + ", dateValeur="
		// 		+ dateValeur + ", idNumCompte=" + idNumCompte + ", idTypeOp=" + idTypeOp + "]";
	}
}

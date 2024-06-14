package model.data;

/*
 * Attributs mis en public car cette classe ne fait que "véhiculer" des données.
 */

public class TypeOperation {

	public String idTypeOp;

	/**
	 * Constructeur de la classe TypeOperation avec tous les paramètres.
	 * 
	 * @param idTypeOp L'identifiant du type d'opération
	 * @author IUT Blagnac
	 */
	public TypeOperation(String idTypeOp) {
		super();
		this.idTypeOp = idTypeOp;
	}

	/**
	 * Constructeur de la classe TypeOperation à partir d'un TypeOperation.
	 * 
	 * @param to Le type d'opération à copier
	 * @author IUT Blagnac
	 */
	public TypeOperation(TypeOperation to) {
		this(to.idTypeOp);
	}

	/**
	 * Constructeur de la classe TypeOperation sans paramètres.
	 * 
	 * Valeurs par défaut :
	 * - idTypeOp : null
	 * 
	 * @author IUT Blagnac
	 */
	public TypeOperation() {
		this((String) null);
	}

	/**
	 * Méthode toString de la classe TypeOperation.
	 * 
	 * @return String Le type d'opération sous forme de chaîne de caractères
	 * @author IUT Blagnac
	 */
	@Override
	public String toString() {
		return "TypeOperation [idTypeOp=" + this.idTypeOp + "]";
	}

}

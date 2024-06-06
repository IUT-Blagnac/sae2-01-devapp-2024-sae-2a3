package application.tools;

/**
 * Classe qui définit une paire de valeurs appelées 'left' et 'right'.
 *
 * Elle permet d'encapsuler 2 valeurs de retour dans un seul objet pour
 * certaines méthodes qui nécessitent un retour de deux valeurs simultanément
 * (une méthode ne peut retourner que un seul objet).
 *
 * @param <T> Type de la première valeur (left)
 * @param <U> Type de la deuxième valeur (right)
 * @author IUT Blagnac
 */
public class PairsOfValue<T, U> {
	private T left;
	private U right;

	/**
	 * Constructeur de la classe PairsOfValue.
	 * 
	 * @param _l La valeur de gauche (left) de la paire de valeurs
	 * @param _r La valeur de droite (right) de la paire de valeurs
	 * @author IUT Blagnac
	 */
	public PairsOfValue(T _l, U _r) {
		this.left = _l;
		this.right = _r;
	}

	/**
	 * Obtient la valeur de gauche.
	 * 
	 * @return T La valeur de gauche (left) de la paire de valeurs
	 * @author IUT Blagnac
	 */
	public T getLeft() {
		return this.left;
	}

	/**
	 * Obtient la valeur de droite.
	 * 
	 * @return U La valeur de droite (right) de la paire de valeurs
	 * @author IUT Blagnac
	 */
	public U getRight() {
		return this.right;
	}
}

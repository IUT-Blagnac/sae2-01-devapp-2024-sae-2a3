package application.tools;

/**
 * Catégories possibles de opérations dans l'application.
 *
 * Simplifie et limite les accès à la BD. Implicite en BD : un type opération
 * est un cas particulier de DEBIT ou CREDIT.
 *
 * @author IUT Blagnac
 */
public enum CategorieOperation {
	DEBIT, CREDIT, VIREMENT
}
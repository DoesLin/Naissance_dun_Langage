package simulation.systeme.lexique;

import java.util.ArrayList;

import simulation.systeme.Individu;

/**
 * Un lexique est un ensemble de lemmes, genere selon un initiateur et avec une taille initiale tailleInitiale,
 * qui evolue au fur et a mesure de la simulation et dont la taille ne peut pas depasser tailleMaximale.
 * 
 * Actuellement, la generation des lemme est : initiateur.lettre + indice, avec 1 <= indice <= tailleInitiale
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class Lexique extends ArrayList<Lemme> {
	private static final long serialVersionUID = 1L;
	
	private int tailleInitiale;
	private int tailleMaximale;
	
	/**
	 * Cree un lexique en initialisant sa taille initiale et maximale a 0
	 */
	public Lexique() {
		tailleInitiale = 0;
		tailleMaximale = 0;
	}
	
	/**
	 * Cree une copie du lexique passe en parametre
	 * 
	 * @param lexique	le lexique a copier
	 */
	public Lexique(Lexique lexique) {
		tailleInitiale = lexique.lireTailleInitiale();
		tailleMaximale = lexique.lireTailleMaximale();
		
		for (Lemme lemme : lexique) {
			add(lemme);
		}
	}
	
	/**
	 * Genere les lemmes du lexique selon l'individu passe en parametre,
	 * avec la taille initiale et maximale souhaitee
	 * 
	 * @param tailleInitiale	la taille initiale pour le lexique
	 * @param tailleMaximale	la taille maximale pour le lexique
	 * @param initiateur		l'initiateur des lemmes a generer, i.e le proprietaire du lexique
	 */
	public void generer(int tailleInitiale, int tailleMaximale, Individu initiateur) {
		this.tailleInitiale = tailleInitiale;
		this.tailleMaximale = tailleMaximale;
		
		for (int indice = 1; indice <= tailleInitiale; indice++) {
			add(new Lemme(Character.toString(initiateur.lireLettre()) + indice, initiateur));
		}
	}
	
	/**
	 * Renvoie la taille initiale du lexique
	 * 
	 * @return la taille initiale du lexique
	 */
	public int lireTailleInitiale() {
		return tailleInitiale;
	}
	
	/**
	 * Renvoie la taille maximale du lexique
	 * 
	 * @return la taille maximale du lexique
	 */
	public int lireTailleMaximale() {
		return tailleMaximale;
	}
	
	/**
	 * Renvoie true si le lexique est egal a sa taille maximale, sinon false
	 * 
	 * @return true si le lexique est egal a sa taille maximale, sinon false
	 */
	public boolean estPlein() {
		return size() == tailleMaximale;
	}
}
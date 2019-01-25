package simulation.systeme.evenement.modele;

/**
 * Une classe abstraite representant un evenenement de la simulation
 * Un evenement est caracterise par un ID et un evenement initiateur
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public abstract class Evenement {
	private static int compteur = 0;
	
	private int ID;
	private Evenement evenementInitiateur;
	
	/**
	 * Declenche l'evenement
	 * 
	 * @throws Exception	si l'implementation de l'evenement a provoque une exception
	 */
	public abstract void declencher() throws Exception;
	
	/**
	 * Cree un evenement en precisant son evenement initiateur
	 * 
	 * @param evenementInitiateur	l'evenement a l'origine de l'evenement courant
	 */
	public Evenement(Evenement evenementInitiateur) {
		ID = compteur++;
		this.evenementInitiateur = evenementInitiateur;
	}
	
	/**
	 * Renvoie l'ID de l'evenement
	 * 
	 * @return l'ID de l'evenement
	 */
	public int lireID() {
		return ID;
	}
	
	/**
	 * Renvoie l'evenement initiateur de l'evenement
	 * 
	 * @return l'evenement initiateur de l'evenement
	 */
	public Evenement lireEvenementInitiateur() {
		return evenementInitiateur;
	}
}

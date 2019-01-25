package simulation.systeme.lexique;

import simulation.systeme.Individu;

/**
 * Classe representant un lemme, i.e un mot utilise par les individus du systeme
 * 
 * Un lemme est caracterise par la chaine de caractere qui le represente ainsi que son initiateur
 * (l'individu a l'origine du lemme)
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class Lemme {
	private String chaine;
	private Individu initiateur;
	
	/**
	 * Cree un lemme depuis la chaine qu'il represente et son initiateur
	 * 
	 * @param chaine
	 * @param initiateur
	 */
	public Lemme(String chaine, Individu initiateur) {
		this.chaine = new String(chaine);
		this.initiateur = initiateur;
	}
	
	public static final Lemme QUELCONQUE = new Lemme("", null);
	
	/**
	 * Renvoie la chaine du lemme
	 * 
	 * @return la chaine du lemme
	 */
	public String lireChaine() {
		return chaine;
	}
	
	/**
	 * Renvoie l'initiateur du lemme
	 * 
	 * @return l'initiateur du lemme
	 */
	public Individu lireInitiateur() {
		return initiateur;
	}
	
	@Override
	public String toString() {
		return chaine;
	}
	
	@Override
	public int hashCode() {
		return (chaine + initiateur.toString()).hashCode();
	}
	
	@Override
	public boolean equals(Object objet) {
		if (objet == null)
			return false;
		if (objet == this)
			return true;
		if (!(objet instanceof Lemme))
			return false;

		Lemme lemme = (Lemme)objet;
		return lireChaine().equals(lemme.lireChaine()) && lireInitiateur().equals(lemme.lireInitiateur());
	}
}
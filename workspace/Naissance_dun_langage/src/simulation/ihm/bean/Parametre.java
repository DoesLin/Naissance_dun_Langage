package simulation.ihm.bean;

/**
 * Class "bean" utilisee exclusivement dans le TextFlow 
 * presentant les differents parametres de la simulation, dans l'IHM
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class Parametre {
	private String nom;
	private String valeur;
	
	/**
	 * Cree un parametre depuis son nom et sa valeur
	 *  
	 * @param nom		le nom du parametre
	 * @param valeur	la valeur du parametre
	 */
	public Parametre(String nom, String valeur) {
		this.nom = nom;
		this.valeur = valeur;
	}

	/**
	 * Renvoie le nom du parametre
	 * 
	 * @return le nom du parametre
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Definie le nom du parametre
	 * 
	 * @param nom	le nom du parametre
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * Renvoie la valeur du parametre
	 * 
	 * @return la valeur du parametre
	 */
	public String getValeur() {
		return valeur;
	}

	/**
	 * Definie la valeur du parametre
	 * 
	 * @param valeur	la valeur du parametre
	 */
	public void setValeur(String valeur) {
		this.valeur = valeur;
	}
}

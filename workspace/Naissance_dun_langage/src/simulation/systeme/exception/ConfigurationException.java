package simulation.systeme.exception;

/**
 * Exception levee lors de la lecture de la configuration du systeme
 * 
 * Cette classe d'exception est plus detaillee que les autres pour facilement
 * identifier l'origine d'une exception de configuration lors des tests
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class ConfigurationException extends Exception {
	private static final long serialVersionUID = 1L;
	private Objet objet;
	private Cause cause;

	/**
	 * Cree une exception de configuration sans parametres
	 */
	public ConfigurationException() {
		super();
	}

	/**
	 * Cree une exception de configuration en precisant l'objet concerne et la cause
	 * 
	 * @param objet		l'objet concerne par l'exception
	 * @param cause		la cause de l'exception
	 */
	public ConfigurationException(Objet objet, Cause cause) {
		super(objet + " " + cause);
		this.objet = objet;
		this.cause = cause;
	}
	
	/**
	 * Cree une exception de configuration en precisant l'objet concerne, la cause et un message
	 * a destination de l'utilisateur
	 * 
	 * @param objet		l'objet concerne par l'exception
	 * @param cause		la cause de l'exception
	 * @param message	un message explicite a destination de l'utilisateur
	 */
	public ConfigurationException(Objet objet, Cause cause, String message) {
		super(message);
		this.objet = objet;
		this.cause = cause;
	}
	
	/**
	 * Renvoie l'objet de l'exception
	 * 
	 * @return l'objet de l'exception
	 */
	public Objet lireObjet() {
		return objet;
	}
	
	/**
	 * Renvoie la cause de l'exception
	 * 
	 * @return la cause de l'exception
	 */
	public Cause lireCause() {
		return cause;
	}
	
	/**
	 * L'objet de l'exception
	 * 
	 * Cette enumeration est utilisee en paire avec Probleme pour designer une
	 * exception de configuration
	 * 
	 * @author Charles MECHERIKI Yongda LIN
	 *
	 */
	public enum Objet {
		FICHIER_CONFIGURATION,
		ELEMENT_CONFIG,
		ELEMENT_SYSTEME,
		ATTRIBUT_NOMBRE_INDIVIDUS,
		ATTRIBUT_URL_FICHIER_MATRICE_VOISINAGE,
		ATTRIBUT_TAILLE_INITIALE_LEXIQUE_PAR_DEFAUT,
		ATTRIBUT_TAILLE_MAXIMALE_LEXIQUE_PAR_DEFAUT,
		ELEMENT_IMPL_CONDITION_EMISSION_PAR_DEFAUT,
		ELEMENT_IMPL_CONDITION_RECEPTION_PAR_DEFAUT,
		ELEMENT_IMPL_CONDITION_MEMORISATION_PAR_DEFAUT,
		ELEMENT_IMPL_STRATEGIE_SELECTION_EMISSION_PAR_DEFAUT,
		ELEMENT_IMPL_STRATEGIE_SELECTION_ELIMINATION_PAR_DEFAUT, 
		ELEMENT_IMPL_STRATEGIE_SUCCESSION_PAR_DEFAUT,
		ATTRIBUT_TAILLE_INITIALE_LEXIQUE,
		ATTRIBUT_TAILLE_MAXIMALE_LEXIQUE,
		ELEMENT_IMPL_CONDITION_EMISSION,
		ELEMENT_IMPL_CONDITION_RECEPTION,
		ELEMENT_IMPL_CONDITION_MEMORISATION,
		ELEMENT_IMPL_STRATEGIE_SELECTION_EMISSION,
		ELEMENT_IMPL_STRATEGIE_SELECTION_ELIMINATION, 
		ELEMENT_IMPL_STRATEGIE_SUCCESSION,
		ELEMENT_TYPE_CRITERE_ARRET,
		ATTRIBUT_OBJECTIF_CRITERE_ARRET,
		ATTRIBUT_ID,
		MATRICE_VOISINAGE;
	}
	
	/**
	 * La cause de l'exception
	 * 
	 * Cette enumeration est utilisee en paire avec Objet pour designer une
	 * exception de configuration
	 * 
	 * @author Charles MECHERIKI Yongda LIN
	 *
	 */
	public enum Cause {
		ABSENT,
		INVALIDE_INTERVALLE,
		INVALIDE_UNIQUE,
		INVALIDE_ENUM;
	}
}

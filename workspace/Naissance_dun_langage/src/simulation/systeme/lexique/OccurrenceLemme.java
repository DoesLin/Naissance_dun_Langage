package simulation.systeme.lexique;

import simulation.systeme.Individu;
import simulation.systeme.evenement.enumeration.IssueEvenement;
import simulation.systeme.evenement.enumeration.TypeEvenement;
import simulation.systeme.temps.Date;

/**
 * Une occurrence de lemme, generee lors de chaque evenement de la simulation
 * 
 * Les occurrences de lemmes permettent de garder en memoire le deroulement exacte de la simulation 
 * (contexte et resultat de chaque evenement)
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class OccurrenceLemme {
	private static int compteur = 0;
	private int ID;
	private OccurrenceLemme occurrenceInitiatrice;
	private Individu individu;
	private Lemme lemme;
	private TypeEvenement typeEvenement;
	private IssueEvenement issueEvenement;
	private Date date;
	
	/**
	 * Cree une occurrence de lemme avec tous ses attributs - sauf l'ID, qui est genere
	 * 
	 * @param occurrenceInitiatrice		l'occurrence initiatrice de l'occurrence courante (typiquement, celle de l'ev. initiateur)
	 * @param individu					l'individu a l'origine de l'occurrence
	 * @param lemme						le lemme a l'origine de l'occurrence
	 * @param typeEvenement				le type de l'evenement a l'origine de l'occurrence
	 * @param issueEvenement			l'issue de l'evenement a l'origine de l'occurrence
	 * @param date						la date de l'occurrence
	 */
	public OccurrenceLemme(OccurrenceLemme occurrenceInitiatrice, Individu individu, Lemme lemme, TypeEvenement typeEvenement, IssueEvenement issueEvenement, Date date) {
		this.ID = ++compteur;
		this.occurrenceInitiatrice = occurrenceInitiatrice;
		this.individu = individu;
		this.lemme = lemme;
		this.typeEvenement = typeEvenement;
		this.issueEvenement = issueEvenement;
		this.date = date;
	}

	/**
	 * Renvoie l'ID de l'occurrence
	 * 
	 * @return l'ID de l'occurrence
	 */
	public int getID() {
		return ID;
	}
	
	/**
	 * Renvoie l'occurrence initiatrice de l'occurrence courante (typiquement, celle de l'ev. initiateur)
	 * 
	 * @return l'occurrence initiatrice de l'occurrence courante
	 */
	public OccurrenceLemme getOccurrenceInitiatrice() {
		return occurrenceInitiatrice;
	}
	
	/**
	 * Renvoie l'individu a l'origine de l'occurrence
	 * 
	 * @return l'individu a l'origine de l'occurrence
	 */
	public Individu getIndividu() {
		return individu;
	}
	
	/**
	 * Renvoie le lemme a l'origine de l'occurrence
	 * 
	 * @return le lemme a l'origine de l'occurrence
	 */
	public Lemme getLemme() {
		return lemme;
	}

	/**
	 * Renvoie le type de l'evenement a l'origine de l'occurrence
	 * 
	 * @return le type de l'evenement a l'origine de l'occurrence
	 */
	public TypeEvenement getTypeEvenement() {
		return typeEvenement;
	}
	
	/**
	 * Renvoie l'issue de l'evenement a l'origine de l'occurrence
	 * 
	 * @return l'issue de l'evenement a l'origine de l'occurrence
	 */
	public IssueEvenement getIssueEvenement() {
		return issueEvenement;
	}
	
	/**
	 * Renvoie la date de l'occurrence
	 * 
	 * @return la date de l'occurrence
	 */
	public Date getDate() {
		return date;
	}
}

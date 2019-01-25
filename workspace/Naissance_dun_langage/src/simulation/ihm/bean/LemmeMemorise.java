package simulation.ihm.bean;

import simulation.systeme.Individu;
import simulation.systeme.lexique.Lemme;
import simulation.systeme.temps.Date;

/**
 * Classe "bean" representant la memorisation d'un lemme par un individu, utilisee 
 * exclusivement dans la TableView presentant le detail du lexique, dans l'IHM
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class LemmeMemorise {
	private Individu emetteur;
	private Individu recepteur;
	private Lemme lemme;
	private Date dateMemorisation;
	
	/**
	 * Cree un lemme memorise depuis son emetteur, son recepteur, le lemme en question et 
	 * sa date de memorisation
	 * 
	 * @param emetteur			l'emetteur du lemme memorise
	 * @param recepteur			le recepteur du lemme memorise
	 * @param lemme				le lemme en question
	 * @param dateMemorisation	la date de memorisation
	 */
	public LemmeMemorise(Individu emetteur, Individu recepteur, Lemme lemme, Date dateMemorisation) {
		this.emetteur = emetteur;
		this.recepteur = recepteur;
		this.lemme = lemme;
		this.dateMemorisation = dateMemorisation;
	}

	/**
	 * Renvoie l'emetteur du lemme memorise
	 * 
	 * @return l'emetteur du lemme memorise
	 */
	public Individu getEmetteur() {
		return emetteur;
	}

	/**
	 * Definie l'emetteur du lemme memorise
	 * 
	 * @param emetteur	l'emetteur du lemme memorise
	 */
	public void setEmetteur(Individu emetteur) {
		this.emetteur = emetteur;
	}

	/**
	 * Renvoie le recepteur du lemme memorise
	 * 
	 * @return le recepteur du lemme memorise
	 */
	public Individu getRecepteur() {
		return recepteur;
	}

	/**
	 * Definie le recepteur du lemme memorise
	 * 
	 * @param recepteur	le recepteur du lemme memorise
	 */
	public void setRecepteur(Individu recepteur) {
		this.recepteur = recepteur;
	}
	
	/**
	 * Renvoie le lemme en question
	 * 
	 * @return le lemme en question
	 */
	public Lemme getLemme() {
		return lemme;
	}

	/**
	 * Definie le lemme en question
	 * 
	 * @param lemme	le lemme en question
	 */
	public void setLemme(Lemme lemme) {
		this.lemme = lemme;
	}

	/**
	 * Renvoie la date de memorisation
	 * 
	 * @return la date de memorisation
	 */
	public Date getDateMemorisation() {
		return dateMemorisation;
	}

	/**
	 * Definie la date de memorisation
	 * 
	 * @param dateMemorisation	la date de memorisation
	 */
	public void setDateMemorisation(Date dateMemorisation) {
		this.dateMemorisation = dateMemorisation;
	}
}

package simulation.systeme.temps;

/**
 * Classe utilisee par le systeme, responsable de la date courante
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class Horloge {
	private Date date;
	
	/**
	 * Cree une horloge en initialisant la date a sa valeur initiale
	 */
	public Horloge() {
		date = Date.valeurInitiale;
	}
	
	/**
	 * Renvoie la valeur actuelle de la date
	 * 
	 * @return la valeur actuelle de la date
	 */
	public Date lireDate() {
		return date;
	}

	/**
	 * Mets a jour la valeur actuelle de la date
	 * 
	 * @param date	la nouvelle date
	 */
	public void mettreAJourDate(Date date) {
		this.date = date;
	}
}
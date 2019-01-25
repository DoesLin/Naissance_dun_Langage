package simulation.systeme;

import java.util.ArrayList;
import java.util.TreeMap;

import simulation.systeme.enumeration.TypeCritereArret;
import simulation.systeme.evenement.modele.Evenement;
import simulation.systeme.exception.EcheancierException;
import simulation.systeme.temps.Date;

/**
 * La structure de donnees, accessible seulement depuis le systeme, qui stocke et declenche les evenements
 * L'utilisation de la classe TreeMap permet de conserver l'ordre d'insertion des evenements selon leur date
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class Echeancier extends TreeMap<Date, ArrayList<Evenement>>  {
	private static final long serialVersionUID = 1L;

	private final int evenementsDeclenchesMaxAvantInterruption = 2000;
	private int evenementsDeclenches;
	
	/**
	 * Initialise l'echeancier
	 */
	public Echeancier() {
		evenementsDeclenches = 0;
	}

	/**
	 * Ajoute l'evenement passe en parametre a l'echeancier en premier a la date donnee
	 * 
	 * @param evenement	l'evenement a ajouter a l'echeancier
	 * @param date		la date de l'evenement
	 */
	public void ajouterPremierEvenementEnDate(Evenement evenement, Date date) {
		ArrayList<Evenement> liste = get(date);
		
		if (liste != null) {
			liste.add(0, evenement);
		}
		else {
			liste = new ArrayList<Evenement>() ;
			liste.add(evenement);
			put(date, liste);
		}
	}
	
	/**
	 * Ajoute l'evenement passe en parametre a l'echeancier en dernier a la date donnee
	 * 
	 * @param evenement	l'evenement a ajouter a l'echeancier
	 * @param date		la date de l'evenement
	 */
	public void ajouterDernierEvenementEnDate(Evenement evenement, Date date) {
		ArrayList<Evenement> liste = get(date);

		if (liste != null) {
			liste.add(liste.size(), evenement);
		}
		else {
			liste = new ArrayList<Evenement>() ;
			liste.add(evenement);
			put(date, liste);
		}
	}
	
	/**
	 * Renvoie la date du prochain evenement de l'echeancier
	 * 
	 * @return la date du prochain evenement de l'echeancier
	 */
	public Date dateProchainEvenement() {
		return firstKey();
	}
	
	/**
	 * Declenche le prochain evenement de l'echeancier
	 * 
	 * @throws Exception	si l'etat de l'echeancier est incoherent (plus d'evenement a declencher)
	 */
	public void declencherProchainEvenement() throws Exception {
		Date prochaineDate = firstKey();
		ArrayList<Evenement> listeEvenements = get(prochaineDate);
		
		if (listeEvenements != null) {
			
			Evenement evenement = listeEvenements.get(0);
			
			if (evenement != null) {
				
				// On retire l'evenement avant de le declencher
				listeEvenements.remove(evenement);
				if (listeEvenements.isEmpty()) {
					remove(prochaineDate);
				}

				if (Systeme.lireTypeCritereArret() == TypeCritereArret.DATE_ATTEINTE && prochaineDate.lireValeur() >= Systeme.lireObjectifCritereArret()) {
					Systeme.declencherEvenementFinal(evenement, Systeme.lireTypeCritereArret() + " = " + Systeme.lireObjectifCritereArret());
				}

				evenementsDeclenches++;
				if (Systeme.lireTypeCritereArret() == TypeCritereArret.EVENEMENTS_DECLENCHES && evenementsDeclenches >= Systeme.lireObjectifCritereArret()) {
					Systeme.declencherEvenementFinal(evenement, Systeme.lireTypeCritereArret() + " = " + Systeme.lireObjectifCritereArret());
				}
				
				if (evenementsDeclenches > evenementsDeclenchesMaxAvantInterruption) {
					Systeme.declencherEvenementFinal(evenement, "Interruption (" + evenementsDeclenchesMaxAvantInterruption + " ev. generes)");
				}

				evenement.declencher();
			}
			else {
				throw new EcheancierException("Situation anormale : aucun evenement a declencher", null);
			}
		}
		else {
			throw new EcheancierException("Situation anormale : aucun evenement a declencher", null);
		}
	}
}

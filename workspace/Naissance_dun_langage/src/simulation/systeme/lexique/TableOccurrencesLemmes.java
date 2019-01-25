package simulation.systeme.lexique;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import simulation.systeme.Individu;
import simulation.systeme.evenement.enumeration.IssueEvenement;
import simulation.systeme.evenement.enumeration.TypeEvenement;
import simulation.systeme.temps.Date;

/**
 * Table des occurrences de lemmes d'un individu
 * 
 * La structure de cette classe est concue pour reduire les delais de recuperation des occurrences en fonction 
 * des criteres souhaites (type d'evenement, issue d'evenement, lemme, date, etc.)
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class TableOccurrencesLemmes extends HashMap<TypeEvenement, HashMap<IssueEvenement, HashMap<Lemme, ArrayList<OccurrenceLemme>>>> {
	private static final long serialVersionUID = 1L;

	/**
	 * Cree puis ajoute une occurrence de lemme a la table selon son contexte (type / issue d'evenement, individu, date, etc.)
	 * 
	 * @param occurrenceInitiatrice		l'occurrence initiatrice de l'occurrence courante (typiquement, celle de l'ev. initiateur)
	 * @param individu					l'individu a l'origine de l'occurrence
	 * @param lemme						le lemme a l'origine de l'occurrence
	 * @param typeEvenement				le type de l'evenement a l'origine de l'occurrence
	 * @param issueEvenement			l'issue de l'evenement a l'origine de l'occurrence
	 * @param date						la date de l'occurrence
	 * @return							l'occurrence creee
	 */
	public OccurrenceLemme nouvelleOccurenceLemme(OccurrenceLemme occurrenceInitiatrice, Individu individu, Lemme lemme, TypeEvenement typeEvenement, IssueEvenement issueEvenement, Date date) {
		HashMap<IssueEvenement, HashMap<Lemme, ArrayList<OccurrenceLemme>>> tableOccurrencesParIssue = new HashMap<IssueEvenement, HashMap<Lemme, ArrayList<OccurrenceLemme>>>();
		HashMap<Lemme, ArrayList<OccurrenceLemme>> tableOccurrencesParLemme = new HashMap<Lemme, ArrayList<OccurrenceLemme>>();	
		ArrayList<OccurrenceLemme> listeOccurrences = new ArrayList<OccurrenceLemme>();
		OccurrenceLemme nouvelleOccurrence = new OccurrenceLemme(occurrenceInitiatrice, individu, lemme, typeEvenement, issueEvenement, date);
		
		listeOccurrences.add(nouvelleOccurrence);
		if (!containsKey(typeEvenement)) {
			tableOccurrencesParLemme.put(lemme, listeOccurrences);
			tableOccurrencesParIssue.put(issueEvenement, tableOccurrencesParLemme);
			put(typeEvenement, tableOccurrencesParIssue);
		}
		else {
			tableOccurrencesParIssue = get(typeEvenement);
			if (!tableOccurrencesParIssue.containsKey(issueEvenement)) {
				tableOccurrencesParLemme.put(lemme, listeOccurrences);
				tableOccurrencesParIssue.put(issueEvenement, tableOccurrencesParLemme);
			}
			else {
				tableOccurrencesParLemme = tableOccurrencesParIssue.get(issueEvenement);
				if (!tableOccurrencesParLemme.containsKey(lemme)) {
					tableOccurrencesParLemme.put(lemme, listeOccurrences);
				}
				else {
					listeOccurrences = tableOccurrencesParLemme.get(lemme);
					listeOccurrences.add(nouvelleOccurrence);
				}
			}
		}
		
		return nouvelleOccurrence;
	}
	
	/**
	 * Recupere les occurrences de la table respectant les criteres passes en parametre
	 * 
	 * @param lemme				le lemme des occurrences a recuperer
	 * @param typeEvenement		le type d'evenement des occurrences a recuperer
	 * @param issueEvenement	l'issue d'evenement des occurrences a recuperer
	 * @param depuisDate		la date minimale des occurrences a recuperer
	 * @param jusquaDate		la date maximale des occurrences a recuperer
	 * 
	 * @return					les occurrences de la table respectant les criteres
	 */
	public ArrayList<OccurrenceLemme> obtenirListeOccurrencesLemmes(Lemme lemme, TypeEvenement typeEvenement, IssueEvenement issueEvenement, Date depuisDate, Date jusquaDate) {
		HashMap<TypeEvenement, HashMap<IssueEvenement, HashMap<Lemme, ArrayList<OccurrenceLemme>>>> tableOccurrencesFiltreesParType = new HashMap<TypeEvenement, HashMap<IssueEvenement, HashMap<Lemme, ArrayList<OccurrenceLemme>>>>();
		HashMap<IssueEvenement, HashMap<Lemme, ArrayList<OccurrenceLemme>>> tableOccurrencesFiltreesParIssue = new HashMap<IssueEvenement, HashMap<Lemme, ArrayList<OccurrenceLemme>>>();
		HashMap<Lemme, ArrayList<OccurrenceLemme>> tableOccurrencesFiltreesParLemme = new HashMap<Lemme, ArrayList<OccurrenceLemme>>();
		ArrayList<OccurrenceLemme> listeOccurrencesFiltrees = new ArrayList<OccurrenceLemme>();

		// Filtre les types d'evenements
		if (typeEvenement != TypeEvenement.QUELCONQUE) {
			if (containsKey(typeEvenement)) {
				tableOccurrencesFiltreesParType.put(typeEvenement, new HashMap<IssueEvenement, HashMap<Lemme, ArrayList<OccurrenceLemme>>>());
			}
		}
		else {
			for (TypeEvenement _typeEvenement : keySet()) {
				tableOccurrencesFiltreesParType.put(_typeEvenement, new HashMap<IssueEvenement, HashMap<Lemme, ArrayList<OccurrenceLemme>>>());
			}
		}
		
		// Filtre les issues d'evenements
		for (TypeEvenement _typeEvenement : tableOccurrencesFiltreesParType.keySet()) {
			tableOccurrencesFiltreesParIssue = tableOccurrencesFiltreesParType.get(_typeEvenement);
			if (issueEvenement != IssueEvenement.QUELCONQUE) {
				if (get(_typeEvenement).containsKey(issueEvenement)) {
					tableOccurrencesFiltreesParIssue.put(issueEvenement, new HashMap<Lemme, ArrayList<OccurrenceLemme>>());
				}
			}
			else {
				for (IssueEvenement _issueEvenement : get(_typeEvenement).keySet()) {
					tableOccurrencesFiltreesParIssue.put(_issueEvenement, new HashMap<Lemme, ArrayList<OccurrenceLemme>>());
				}
			}
		}
		
		// Filtre les lemmes
		for (TypeEvenement _typeEvenement : tableOccurrencesFiltreesParType.keySet()) {
			tableOccurrencesFiltreesParIssue = tableOccurrencesFiltreesParType.get(_typeEvenement);
			for (IssueEvenement _issueEvenement : tableOccurrencesFiltreesParIssue.keySet()) {
				tableOccurrencesFiltreesParLemme = tableOccurrencesFiltreesParIssue.get(_issueEvenement);
				if (lemme != Lemme.QUELCONQUE) {
					if (get(_typeEvenement).get(issueEvenement).containsKey(lemme)) {
						tableOccurrencesFiltreesParLemme.put(lemme, new ArrayList<OccurrenceLemme>());
					}
				}
				else {
					for (Lemme _lemme : get(_typeEvenement).get(_issueEvenement).keySet()) {
						tableOccurrencesFiltreesParLemme.put(_lemme, new ArrayList<OccurrenceLemme>());
					}
				}
			}
		}
		
		// Filtre les dates
		for (TypeEvenement _typeEvenement : tableOccurrencesFiltreesParType.keySet()) {
			tableOccurrencesFiltreesParIssue = tableOccurrencesFiltreesParType.get(_typeEvenement);
			for (IssueEvenement _issueEvenement : tableOccurrencesFiltreesParIssue.keySet()) {
				tableOccurrencesFiltreesParLemme = tableOccurrencesFiltreesParIssue.get(_issueEvenement);
				for (Lemme _lemme : tableOccurrencesFiltreesParLemme.keySet()) {
					for (OccurrenceLemme _occurrenceLemme : get(_typeEvenement).get(_issueEvenement).get(_lemme)) {
						if ((depuisDate == null || _occurrenceLemme.getDate().estApres(depuisDate)) && (jusquaDate == null || _occurrenceLemme.getDate().estAvant(jusquaDate))) {
							listeOccurrencesFiltrees.add(_occurrenceLemme);
						}
					}
				}
			}
		}
	
		Collections.sort(listeOccurrencesFiltrees, new ComparateurOccurrenceLemmeID());

		return listeOccurrencesFiltrees;
	}
	
	/**
	 * Recupere les occurrences de la table respectant les criteres passes en parametre
	 * 
	 * @param lemme				le lemme des occurrences a recuperer
	 * @param typeEvenement		le type d'evenement des occurrences a recuperer
	 * @param issueEvenement	l'issue d'evenement des occurrences a recuperer
	 * 
	 * @return					les occurrences de la table respectant les criteres
	 */
	public ArrayList<OccurrenceLemme> obtenirListeOccurrencesLemmes(Lemme lemme, TypeEvenement typeEvenement, IssueEvenement issueEvenement) {
		return obtenirListeOccurrencesLemmes(lemme, typeEvenement, issueEvenement, null, null);
	}
	
	
	/**
	 * Recupere les occurrences de la table respectant les criteres passes en parametre tries selon leur ID
	 * 
	 * @param lemme				le lemme des occurrences a recuperer
	 * @param typeEvenement		le type d'evenement des occurrences a recuperer
	 * @param issueEvenement	l'issue d'evenement des occurrences a recuperer
	 * @param depuisDate		la date minimale des occurrences a recuperer
	 * @param jusquaDate		la date maximale des occurrences a recuperer
	 * 
	 * @return					les occurrences de la table respectant les criteres tries selon leur ID
	 */
	public ArrayList<OccurrenceLemme> obtenirListeOccurrencesLemmesOrdonnee(Lemme lemme, TypeEvenement typeEvenement, IssueEvenement issueEvenement, Date depuisDate, Date jusquaDate) {
		ArrayList<OccurrenceLemme> listeOccurrences = obtenirListeOccurrencesLemmes(lemme, typeEvenement, issueEvenement, depuisDate, jusquaDate);
		
		Collections.sort(listeOccurrences, new ComparateurOccurrenceLemmeID());

		return listeOccurrences;
	}
}

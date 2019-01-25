package simulation.systeme;

import java.util.ArrayList;
import java.util.HashMap;

import simulation.systeme.enumeration.TypeCritereArret;
import simulation.systeme.evenement.executeur.ExecuteurEvenementsSysteme;
import simulation.systeme.evenement.modele.Evenement;
import simulation.systeme.lexique.Lemme;
import simulation.systeme.lexique.OccurrenceLemme;
import simulation.systeme.temps.Date;
import simulation.systeme.temps.Horloge;

/**
 * La classe - static - gerant le systeme de la simulation
 * 
 * Le systeme comporte notamment une configuration systeme avec les differents individus, un echeancier stockant les evenements de
 * la simulation, une horloge gerant les dates des evenements, un executeur qui se charge de generer l'evenement initial 
 * et l'evenement final, ainsi que des caches pour conserver temporairement des lemmes et occurrences de lemmes
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class Systeme {
	private static ConfigurationSysteme configurationSysteme;
	
	private static Echeancier echeancier = new Echeancier();
	private static Horloge horloge = new Horloge();

	private static HashMap<Integer, Lemme> cacheLemmes = new HashMap<Integer, Lemme>();
	private static HashMap<Integer, OccurrenceLemme> cacheOccurrencesLemmes = new HashMap<Integer, OccurrenceLemme>();
	
	private static ExecuteurEvenementsSysteme executeurEvenements = new ExecuteurEvenementsSysteme();
	
	private static String messageFin;
	private static boolean estArrete = false;
	
	/**
	 * Renvoie la configuration du systeme
	 * 
	 * @return la configuration du systeme
	 */
	public static ConfigurationSysteme obtenirConfigurationSysteme() {
		return configurationSysteme;
	}
	
	/**
	 * Renvoie le nombre d'individus du systeme
	 * 
	 * @return le nombre d'individus du systeme
	 */
	public static int lireNombreIndividus() {
		return configurationSysteme.lireNombreIndividus();
	}
	
	/**
	 * Renvoie le type de critere d'arret du systeme
	 * 
	 * @return le type de critere d'arret du systeme
	 */
	public static TypeCritereArret lireTypeCritereArret() {
		return configurationSysteme.lireCritereArret().lireTypeCritere();
	}
	
	/**
	 * Renvoie l'objectif du critere d'arret du systeme
	 * 
	 * @return l'objectif du critere d'arret du systeme
	 */
	public static int lireObjectifCritereArret() {
		return configurationSysteme.lireCritereArret().lireObjectif();
	}
	
	/**
	 * Renvoie les individus du systeme
	 * 
	 * @return les individus du systeme
	 */
	public static ArrayList<Individu> obtenirIndividus() {
		return configurationSysteme.obtenirIndividus();
	}
	
	/**
	 * Renvoie la date de l'horloge du systeme
	 * 
	 * @return la date de l'horloge du systeme
	 */
	public static Date lireDateHorloge() {
		return horloge.lireDate();
	}
	
	/**
	 * Renvoie le message de fin de la simulation
	 * 
	 * @return le message de fin de la simulation
	 */
	public static String lireMessageFin() {
		return messageFin;
	}

	/**
	 * Ajoute l'evenement donne a l'echeancier en premiere place pour la date donne
	 * 
	 * @param evenement		l'evenement a ajouter a l'echeancier
	 * @param date			la date de l'evenement
	 */
	public static void ajouterPremierEvenementEnDate(Evenement evenement, Date date) {
		echeancier.ajouterPremierEvenementEnDate(evenement, date);
	}
	
	/**
	 * Ajoute l'evenement donne a l'echeancier en derniere place pour la date donne
	 * 
	 * @param evenement		l'evenement a ajouter a l'echeancier
	 * @param date			la date de l'evenement
	 */
	public static void ajouterDernierEvenementEnDate(Evenement evenement, Date date) {
		echeancier.ajouterDernierEvenementEnDate(evenement, date);
	}
	
	/**
	 * Declenche le prochain evenement de l'echeancier (i.e celui avec la date la plus proche)
	 * et met a jour l'horloge avec cette meme date
	 */
	public static void declencherProchainEvenement() throws Exception {
		if (!estArrete) {
			horloge.mettreAJourDate(echeancier.dateProchainEvenement());
			echeancier.declencherProchainEvenement();
		}
	}

	/**
	 * Declenche l'evenement final du systeme en lui associant un evenement initiateur et un message de fin
	 * 
	 * @param evenementInitiateur	l'evenement ayant provoque la fin de la simualtion
	 * @param message				le message de fin de la simulation
	 */
	public static void declencherEvenementFinal(Evenement evenementInitiateur, String message) throws Exception {
		estArrete = true;
		messageFin = message;
		executeurEvenements.genererEvenementFinal(evenementInitiateur).declencher();
	}
	
	/**
	 * Stocke le lemme dans le cache sous l'ID d'evenement donne
	 * 
	 * @param evenementID	l'ID de l'evenement sous lequel stocker le lemme
	 * @param lemme			le lemme a stocker
	 */
	public static void ajouterLemmeCache(int evenementID, Lemme lemme) {
		cacheLemmes.put(evenementID, lemme);
	}

	/**
	 * Renvoie et supprime du cache le lemme stocke sous l'ID d'evenement donne
	 * 
	 * @param evenementID	l'ID de l'evenement ayant stocke le lemme
	 * @return				le lemme du cache stocke sous l'ID d'evenement donne
	 */
	public static Lemme consommerLemmeCache(int evenementID) {
		return cacheLemmes.remove(evenementID);
	}
	
	/**
	 * Stocke l'occurrence de lemme dans le cache sous l'ID d'evenement donne
	 * 
	 * @param evenementID	l'ID de l'evenement sous lequel stocker l'occurrence de lemme
	 * @param occurrence			l'occurrence de lemme a stocker
	 */
	public static void ajouterOccurrenceCache(int evenementID, OccurrenceLemme occurrence) {
		cacheOccurrencesLemmes.put(evenementID, occurrence);
	}

	/**
	 * Renvoie et supprime du cache l'occurrence de lemme stockee sous l'ID d'evenement donne
	 * 
	 * @param evenementID	l'ID de l'evenement ayant stocke l'occurrence de lemme
	 * @return				l'occurrence de lemme du cache stockee sous l'ID d'evenement donne
	 */
	public static OccurrenceLemme consommerOccurrenceCache(int evenementID) {
		return cacheOccurrencesLemmes.remove(evenementID);
	}

	/**
	 * Configure le systeme avec la configuration passee en parametre
	 * 
	 * @param configurationSysteme	la configuration pour le systeme
	 */
	public static void configurer(ConfigurationSysteme configurationSysteme) {
		Systeme.configurationSysteme = configurationSysteme;
	}
	
	/**
	 * Lance l'evenement initial et par consequent l'ensemble de la simulation
	 * 
	 * @throws Exception	si jamais l'enchainement des evenements de la simulation provoque une exception
	 */
	public static void lancer() throws Exception {
		if (configurationSysteme == null)
			throw new Exception("Impossible de lancer le systeme, ce dernier n'a pas ete correctement configure");
		
		executeurEvenements.genererEvenementInitial().declencher();
	}
}

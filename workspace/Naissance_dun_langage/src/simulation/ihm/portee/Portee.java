package simulation.ihm.portee;

import java.util.ArrayList;
import java.util.HashMap;

import simulation.ihm.bean.LemmeMemorise;
import simulation.ihm.bean.Parametre;
import simulation.systeme.Individu;
import simulation.systeme.evenement.enumeration.IssueEvenement;
import simulation.systeme.evenement.enumeration.TypeEvenement;
import simulation.systeme.lexique.OccurrenceLemme;
import simulation.systeme.temps.Date;

/** 
 * Classe abstraite representant une portee du systeme, dont le but est de recuperer des donnees requises par l'IHM
 * sur des portees differentes : individuelle (PorteeIndividu) ou bien globale (PorteeSysteme)
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public abstract class Portee {

	@Override
	public abstract String toString();
	
	/**
	 * Recupere les donnees necessaires a l'elaboration du diagramme de composition du lexique de 
	 * la portee a la date passee en parametre, i.e la proportion de lemme de chaque origine du lexique
	 * 
	 * @param date	la date a considerer pour le lexique
	 * @return		la proportion de lemme de chaque origine du lexique
	 */
	public abstract HashMap<Individu, Integer> obtenirDonneesDiagrammeCompositionLexique(Date date);
	
	/**
	 * Recupere les donnees necessaires a l'elaboration du diagramme decrivant l'evolution de la composition du lexique 
	 * de la portee i.e la proportion de lemme de chaque origine du lexique, pour chacune des dates reperes
	 * passees en parametre
	 * 
	 * @param datesRepere	les dates reperes du diagramme
	 * @return				la proportion de lemme de chaque origine du lexique pour chaque date
	 */
	public abstract HashMap<Date, HashMap<Individu, Integer>> obtenirDonneesDiagrammeEvolutionCompositionLexique(ArrayList<Date> datesRepere);
	
	/**
	 * Recupere les donnees necessaires a l'elaboration du diagramme decrivant l'evolution des occurrences de la portee, 
	 * i.e la proportion de lemme de chaque origine dans l'ensemble des occurrences, pour chacune des dates reperes 
	 * passees en parametre et pour le type d'evenement et l'issue d'evenement donnees
	 * 
	 * @param datesRepere		les dates reperes du diagramme
	 * @param typeEvenement		le type d'evenement des occurrences a recuperer
	 * @param issueEvenement	l'issue d'evenement des occurrences a recuperer
	 * @return					la proportion de lemme de chaque origine dans l'ensemble des occurrences
	 */
	public abstract HashMap<Date, HashMap<Individu, Integer>> obtenirDonneesDiagrammeEvolutionOccurrences(ArrayList<Date> datesRepere, TypeEvenement typeEvenement, IssueEvenement issueEvenement);
	
	/**
	 * Recupere la liste des parametre de la portee
	 * 
	 * @return	la liste des parametres de la portee
	 */
	public abstract ArrayList<Parametre> obtenirListeParametres();
	
	/**
	 * Recupere la liste des occurrences de lemmes de la portee jusqu'a la date passee en parametre pour 
	 * le type d'evenement et l'issue d'evenement donnes
	 * 
	 * @param date				la date limite des occurrences a recuperer
	 * @param typeEvenement		le type d'evenement des occurrences a recuperer
	 * @param issueEvenement	l'issue d'evenement des occurrences a recuperer
	 * @return					la liste des occurrences correspondant aux parametres
	 */
	public abstract ArrayList<OccurrenceLemme> obtenirListeOccurrences(Date date, TypeEvenement typeEvenement, IssueEvenement issueEvenement);
	
	/**
	 * Recupere la liste des lemmes memorises du lexique a la date donnee
	 *  
	 * @param date	la date a considerer
	 * @return		la liste des lemmes memorises du lexique
	 */
	public abstract ArrayList<LemmeMemorise> obtenirListeLemmesMemorises(Date date);
}
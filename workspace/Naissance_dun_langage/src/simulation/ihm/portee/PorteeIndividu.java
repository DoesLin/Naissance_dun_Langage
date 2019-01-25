package simulation.ihm.portee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import simulation.ihm.bean.LemmeMemorise;
import simulation.ihm.bean.Parametre;
import simulation.systeme.Individu;
import simulation.systeme.Systeme;
import simulation.systeme.evenement.enumeration.IssueEvenement;
import simulation.systeme.evenement.enumeration.TypeEvenement;
import simulation.systeme.lexique.ComparateurOccurrenceLemmeDate;
import simulation.systeme.lexique.Lemme;
import simulation.systeme.lexique.Lexique;
import simulation.systeme.lexique.OccurrenceLemme;
import simulation.systeme.temps.Date;

/**
 * Portee relative a un individu
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class PorteeIndividu extends Portee {
	private Individu individu;
	
	public PorteeIndividu(Individu individu) {
		this.individu = individu;
	}
	
	public Individu obtenirIndividu() {
		return individu;
	}
	
	@Override
	public String toString() {
		return individu.toString();
	}

	@Override
	public HashMap<Individu, Integer> obtenirDonneesDiagrammeCompositionLexique(Date date) {
		HashMap<Individu, Integer> repartition = new HashMap<Individu, Integer>();
		
		for (Individu individu : Systeme.obtenirIndividus()) {
			repartition.put(individu, 0);
		}

		for (Lemme lemme : individu.retrouverLexique(date)) {
			Individu _individu = lemme.lireInitiateur();
			repartition.put(_individu, repartition.get(_individu) + 1);
		}
		
		return repartition;
	}
	
	@Override
	public ArrayList<LemmeMemorise> obtenirListeLemmesMemorises(Date date) {
		ArrayList<OccurrenceLemme> listeOccurrences = new ArrayList<OccurrenceLemme>();
		HashMap<Integer, LemmeMemorise> tableLemmesMemorises = new HashMap<Integer, LemmeMemorise>();
		
		listeOccurrences.addAll(individu.obtenirTableOccurrencesLemmes().obtenirListeOccurrencesLemmes(Lemme.QUELCONQUE, TypeEvenement.MEMORISATION, IssueEvenement.SUCCES, Date.valeurInitiale, date));
		listeOccurrences.addAll(individu.obtenirTableOccurrencesLemmes().obtenirListeOccurrencesLemmes(Lemme.QUELCONQUE, TypeEvenement.ELIMINATION, IssueEvenement.SUCCES, Date.valeurInitiale, date));
		
		Collections.sort(listeOccurrences, new ComparateurOccurrenceLemmeDate());
		
		Lexique lexique = new Lexique();
		lexique.generer(individu.obtenirLexique().lireTailleInitiale(), individu.obtenirLexique().lireTailleMaximale(), individu);

		for (Lemme _lemme : lexique) {
			tableLemmesMemorises.put(_lemme.hashCode(), new LemmeMemorise(individu, individu, _lemme, Date.valeurInitiale));
		}

		for (OccurrenceLemme occurrence : listeOccurrences) {
			Lemme lemme = occurrence.getLemme();
			switch (occurrence.getTypeEvenement()) {
				case MEMORISATION:
					Individu emetteur = occurrence.getOccurrenceInitiatrice().getOccurrenceInitiatrice().getIndividu();
					Individu recepteur = occurrence.getIndividu();
					Date dateMemorisation = occurrence.getDate();
					tableLemmesMemorises.put(lemme.hashCode(), new LemmeMemorise(emetteur, recepteur, lemme, dateMemorisation));
					break;
				case ELIMINATION:
					tableLemmesMemorises.remove(lemme.hashCode());
					break;
				default:
					break;
			}
		}

		return new ArrayList<LemmeMemorise>(tableLemmesMemorises.values());
	}

	@Override
	public ArrayList<Parametre> obtenirListeParametres() {
		ArrayList<Parametre> listeParametres = new ArrayList<Parametre>();

		listeParametres.add(new Parametre("ID", String.valueOf(individu.lireID())));
		listeParametres.add(new Parametre("TailleLexiqueInitiale", String.valueOf(individu.obtenirLexique().lireTailleInitiale())));
		listeParametres.add(new Parametre("TailleLexiqueMaximale", String.valueOf(individu.obtenirLexique().lireTailleMaximale())));
		listeParametres.add(new Parametre("ImplementationConditionEmission", String.valueOf(individu.lireImplConditionEmission())));
		listeParametres.add(new Parametre("ImplementationConditionReception", String.valueOf(individu.lireImplConditionReception())));
		listeParametres.add(new Parametre("ImplementationConditionMemorisation", String.valueOf(individu.lireImplConditionMemorisation())));
		listeParametres.add(new Parametre("ImplementationStrategieSelectionEmission", String.valueOf(individu.lireImplStrategieSelectionEmission())));
		listeParametres.add(new Parametre("ImplementationStrategieSelectionElimination", String.valueOf(individu.lireImplStrategieSelectionElimination())));
		listeParametres.add(new Parametre("ImplementationStrategieSuccession", String.valueOf(individu.lireImplStrategieSuccession())));
		
		return listeParametres;
	}

	@Override
	public HashMap<Date, HashMap<Individu, Integer>> obtenirDonneesDiagrammeEvolutionCompositionLexique(ArrayList<Date> datesRepere) {
		HashMap<Date, HashMap<Individu, Integer>> repartitions = new HashMap<Date, HashMap<Individu, Integer>>();
		HashMap<Individu, Integer> repartitionDateCourante = null;
		
		for (Date date : datesRepere) {
			repartitions.put(date, new HashMap<Individu, Integer>());
			repartitionDateCourante = repartitions.get(date);
			
			for (Individu individu : Systeme.obtenirIndividus()) {
				repartitionDateCourante.put(individu, 0);
			}
			for (Lemme lemme : individu.retrouverLexique(date)) {
				Individu _individu = lemme.lireInitiateur();
				repartitionDateCourante.put(_individu, repartitionDateCourante.get(_individu) + 1);
			}
		}
		
		return repartitions;
	}

	@Override
	public HashMap<Date, HashMap<Individu, Integer>> obtenirDonneesDiagrammeEvolutionOccurrences(ArrayList<Date> datesRepere, TypeEvenement typeEvenement, IssueEvenement issueEvenement) {
		ArrayList<OccurrenceLemme> listeOccurrences = new ArrayList<OccurrenceLemme>();
		HashMap<Date, HashMap<Individu, Integer>> donnees = new HashMap<Date, HashMap<Individu, Integer>>();

		for (Date date : datesRepere) {
			HashMap<Individu, Integer> donneesDateCourante = new HashMap<Individu, Integer>();
			donnees.put(date, donneesDateCourante);
			
			for (Individu individu : Systeme.obtenirIndividus()) {
				donneesDateCourante.put(individu, 0);
			}

			listeOccurrences = individu.obtenirTableOccurrencesLemmes().obtenirListeOccurrencesLemmes(Lemme.QUELCONQUE, typeEvenement, issueEvenement, Date.valeurInitiale, date);
			
			for (OccurrenceLemme occurrence : listeOccurrences) {
				donneesDateCourante.put(occurrence.getLemme().lireInitiateur(), donneesDateCourante.get(occurrence.getLemme().lireInitiateur()) + 1);
			}
		}
		
		return donnees;
	}

	@Override
	public ArrayList<OccurrenceLemme> obtenirListeOccurrences(Date date, TypeEvenement typeEvenement, IssueEvenement issueEvenement) {
		ArrayList<OccurrenceLemme> listeOccurrences = new ArrayList<OccurrenceLemme>();
		
		listeOccurrences = individu.obtenirTableOccurrencesLemmes().obtenirListeOccurrencesLemmesOrdonnee(Lemme.QUELCONQUE, typeEvenement, issueEvenement, Date.valeurInitiale, date);

		return listeOccurrences;
	}
}

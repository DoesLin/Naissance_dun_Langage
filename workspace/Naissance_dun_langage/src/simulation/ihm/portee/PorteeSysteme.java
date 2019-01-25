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
 * Portee relative a l'ensemble du systeme
 * 
 * Dans la plupart des cas, les donnees recuperees correspondent a la sommes des donnees
 * de chaque individu
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class PorteeSysteme extends Portee {

	@Override
	public String toString() {
		return "Systeme";
	}

	@Override
	public HashMap<Individu, Integer> obtenirDonneesDiagrammeCompositionLexique(Date date) {
		HashMap<Individu, Integer> repartitions = new HashMap<Individu, Integer>();
		
		for (Individu individu : Systeme.obtenirIndividus()) {
			repartitions.put(individu, 0);
		}
		
		for (Individu _individu : Systeme.obtenirIndividus()) {
			for (Lemme lemme : _individu.retrouverLexique(date)) {
				Individu individu = lemme.lireInitiateur();
				repartitions.put(individu, repartitions.get(individu) + 1);
			}
		}
		
		return repartitions;
	}
	
	@Override
	public ArrayList<LemmeMemorise> obtenirListeLemmesMemorises(Date date) {
		ArrayList<OccurrenceLemme> listeOccurrences = new ArrayList<OccurrenceLemme>();
		ArrayList<LemmeMemorise> listeLemmesMemorises = new ArrayList<LemmeMemorise>();
		HashMap<Individu, HashMap<Integer, LemmeMemorise>> tableLemmesMemorises = new HashMap<Individu, HashMap<Integer, LemmeMemorise>>();
		
		for (Individu individu : Systeme.obtenirIndividus()) {
			listeOccurrences.addAll(individu.obtenirTableOccurrencesLemmes().obtenirListeOccurrencesLemmes(Lemme.QUELCONQUE, TypeEvenement.MEMORISATION, IssueEvenement.SUCCES, Date.valeurInitiale, date));
			listeOccurrences.addAll(individu.obtenirTableOccurrencesLemmes().obtenirListeOccurrencesLemmes(Lemme.QUELCONQUE, TypeEvenement.ELIMINATION, IssueEvenement.SUCCES, Date.valeurInitiale, date));
		
			Lexique lexique = new Lexique();
			lexique.generer(individu.obtenirLexique().lireTailleInitiale(), individu.obtenirLexique().lireTailleMaximale(), individu);
			tableLemmesMemorises.put(individu, new HashMap<Integer, LemmeMemorise>());
			for (Lemme lemme : lexique) {
				tableLemmesMemorises.get(individu).put(lemme.hashCode(), new LemmeMemorise(individu, individu, lemme, Date.valeurInitiale));
			}
		}
		
		Collections.sort(listeOccurrences, new ComparateurOccurrenceLemmeDate());

		for (OccurrenceLemme occurrence : listeOccurrences) {
			Lemme lemme = occurrence.getLemme();
			Individu recepteur = occurrence.getIndividu();
			switch (occurrence.getTypeEvenement()) {
				case MEMORISATION :
					Individu emetteur = occurrence.getOccurrenceInitiatrice().getOccurrenceInitiatrice().getIndividu();
					Date dateMemorisation = occurrence.getDate();
					tableLemmesMemorises.get(recepteur).put(lemme.hashCode(), new LemmeMemorise(emetteur, recepteur, lemme, dateMemorisation));
					break;
				case ELIMINATION :
					tableLemmesMemorises.get(recepteur).remove(lemme.hashCode());
					break;
				default:
					break;
			}
		}
		
		for (Individu individu : Systeme.obtenirIndividus()) {	
			listeLemmesMemorises.addAll(tableLemmesMemorises.get(individu).values());
		}
		
		return listeLemmesMemorises;
	}

	@Override
	public ArrayList<Parametre> obtenirListeParametres() {
		ArrayList<Parametre> listeParametres = new ArrayList<Parametre>();
		
		listeParametres.add(new Parametre("NombreIndividus", String.valueOf(Systeme.lireNombreIndividus())));
		listeParametres.add(new Parametre("TailleLexiqueInitialeParDefaut", String.valueOf(Systeme.obtenirConfigurationSysteme().lireTailleInitialeLexiqueParDefaut())));
		listeParametres.add(new Parametre("TailleLexiqueMaximaleParDefaut", String.valueOf(Systeme.obtenirConfigurationSysteme().lireTailleMaximaleLexiqueParDefaut())));
		listeParametres.add(new Parametre("ImplementationConditionEmissionParDefaut", String.valueOf(Systeme.obtenirConfigurationSysteme().lireImplConditionEmissionParDefaut())));
		listeParametres.add(new Parametre("ImplementationConditionReceptionParDefaut", String.valueOf(Systeme.obtenirConfigurationSysteme().lireImplConditionReceptionParDefaut())));
		listeParametres.add(new Parametre("ImplementationConditionMemorisationParDefaut", String.valueOf(Systeme.obtenirConfigurationSysteme().lireImplConditionMemorisationParDefaut())));
		listeParametres.add(new Parametre("ImplementationStrategieSelectionEmissionParDefaut", String.valueOf(Systeme.obtenirConfigurationSysteme().lireImplStrategieSelectionEmissionParDefaut())));
		listeParametres.add(new Parametre("ImplementationStrategieSelectionEliminationParDefaut", String.valueOf(Systeme.obtenirConfigurationSysteme().lireImplStrategieSelectionEliminationParDefaut())));
		listeParametres.add(new Parametre("ImplementationStrategieSuccessionParDefaut", String.valueOf(Systeme.obtenirConfigurationSysteme().lireImplStrategieSuccessionParDefaut())));
		listeParametres.add(new Parametre("TypeCritereArret", String.valueOf(Systeme.lireTypeCritereArret())));		
		listeParametres.add(new Parametre("ObjectifCritereArret", String.valueOf(Systeme.lireObjectifCritereArret())));
		
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
			
			for (Individu individu : Systeme.obtenirIndividus()) {
				for (Lemme lemme : individu.retrouverLexique(date)) {
					Individu _individu = lemme.lireInitiateur();
					repartitionDateCourante.put(_individu, repartitionDateCourante.get(_individu) + 1);
				}
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
			
			listeOccurrences.clear();
			for (Individu individu : Systeme.obtenirIndividus()) {
				donneesDateCourante.put(individu, 0);
				listeOccurrences.addAll(individu.obtenirTableOccurrencesLemmes().obtenirListeOccurrencesLemmes(Lemme.QUELCONQUE, typeEvenement, issueEvenement, Date.valeurInitiale, date));
			}

			for (OccurrenceLemme occurrenceLemme : listeOccurrences) {
				donneesDateCourante.put(occurrenceLemme.getLemme().lireInitiateur(), donneesDateCourante.get(occurrenceLemme.getLemme().lireInitiateur()) + 1);
			}
		}
		
		return donnees;
	}

	@Override
	public ArrayList<OccurrenceLemme> obtenirListeOccurrences(Date date, TypeEvenement typeEvenement, IssueEvenement issueEvenement) {
		ArrayList<OccurrenceLemme> listeOccurrences = new ArrayList<OccurrenceLemme>();
		
		for (Individu individu : Systeme.obtenirIndividus()) {
			listeOccurrences.addAll(individu.obtenirTableOccurrencesLemmes().obtenirListeOccurrencesLemmesOrdonnee(Lemme.QUELCONQUE, typeEvenement, issueEvenement, Date.valeurInitiale, date));
		}

		Collections.sort(listeOccurrences, new ComparateurOccurrenceLemmeDate());
		
		return listeOccurrences;
	}
}

package simulation.systeme.strategie.implementation;

import java.util.ArrayList;
import java.util.Collection;

import simulation.systeme.Individu;
import simulation.systeme.evenement.enumeration.IssueEvenement;
import simulation.systeme.evenement.enumeration.TypeEvenement;
import simulation.systeme.lexique.Lemme;
import simulation.systeme.lexique.TableOccurrencesLemmes;
import simulation.systeme.strategie.modele.StrategieSelection;

/**
 * Implementation de la classe StrategieSelection selectionnant le lemme le moins emis par l'individu courant
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class ImplStrategieSelectionLemmeMoinsEmis extends StrategieSelection {

	@Override
	public Lemme selectionnerLemme(Individu individuCourant, Lemme lemmeCourant) {
		int nombreMinEmissions = Integer.MAX_VALUE;
		int nombreEmissionsLemmeCourant = Integer.MAX_VALUE;
		Lemme lemmeMinEmissions = individuCourant.obtenirLexique().get(0);

		TableOccurrencesLemmes tableOccurrencesLemmes = individuCourant.obtenirTableOccurrencesLemmes();
		
		Collection<Lemme> lemmes = new ArrayList<Lemme>();
		if (tableOccurrencesLemmes.containsKey(TypeEvenement.EMISSION) && tableOccurrencesLemmes.get(TypeEvenement.EMISSION).containsKey(IssueEvenement.SUCCES)) {
			lemmes = tableOccurrencesLemmes.get(TypeEvenement.EMISSION).get(IssueEvenement.SUCCES).keySet();
		}
	
		for (Lemme _lemmeCourant : lemmes) {
			nombreEmissionsLemmeCourant = individuCourant.obtenirTableOccurrencesLemmes().obtenirListeOccurrencesLemmes(_lemmeCourant, TypeEvenement.EMISSION, IssueEvenement.SUCCES).size();
			
			if (individuCourant.connaitLemme(_lemmeCourant) && nombreEmissionsLemmeCourant < nombreMinEmissions) {
				lemmeMinEmissions = _lemmeCourant;
			}
		}
		
		return lemmeMinEmissions;
	}
}

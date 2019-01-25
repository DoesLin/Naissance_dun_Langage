package simulation.systeme.strategie.implementation;

import simulation.systeme.Individu;
import simulation.systeme.lexique.Lemme;
import simulation.systeme.strategie.modele.StrategieSelection;

/**
 * Implementation de la classe StrategieSelection selectionnant le premier lemme du lexique de l'individu courant
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class ImplStrategieSelectionPremierLemme extends StrategieSelection {

	@Override
	public Lemme selectionnerLemme(Individu individuCourant, Lemme lemmeCourant) {
		return individuCourant.obtenirLexique().get(0);
	}
}

package simulation.systeme.strategie.implementation;

import simulation.systeme.Individu;
import simulation.systeme.Voisin;
import simulation.systeme.strategie.modele.StrategieSuccession;

/**
 * Implementation de la classe StrategieSuccession determinant le premier voisin de l'individu courant comme successeur
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class ImplStrategieSuccessionPremierVoisin extends StrategieSuccession {

	@Override
	protected Voisin determinerSuccesseur(Individu individuCourant) {
		return individuCourant.obtenirVoisins().get(0);
	}
}
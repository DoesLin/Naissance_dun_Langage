package simulation.systeme.condition.implementation;

import simulation.systeme.Individu;
import simulation.systeme.condition.modele.Condition;
import simulation.systeme.lexique.Lemme;

/**
 * Implementation de la classe Condition qui n'est jamais satisfaite
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class ImplConditionJamaisSatisfaite extends Condition {
	
	@Override
	public boolean estSatisfaite(Individu individuCourant, Lemme lemmeCourant) {
		return false;
	}
}

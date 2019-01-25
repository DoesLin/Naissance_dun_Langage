package simulation.systeme.condition.implementation;

import java.util.Random;

import simulation.systeme.Individu;
import simulation.systeme.condition.modele.Condition;
import simulation.systeme.lexique.Lemme;

/**
 * Implementation de la classe Condition qui a une probabilite unfirme d'etre satisfaite
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class ImplConditionProbabiliteUniforme extends Condition {
	private final Random random = new Random();
	
	@Override
	public boolean estSatisfaite(Individu individuCourant, Lemme lemmeCourant) {
		return random.nextBoolean();
	}
}

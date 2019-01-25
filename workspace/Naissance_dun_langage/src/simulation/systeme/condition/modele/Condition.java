package simulation.systeme.condition.modele;

import simulation.systeme.Individu;
import simulation.systeme.condition.enumeration.ImplementationCondition;
import simulation.systeme.condition.implementation.ImplConditionJamaisSatisfaite;
import simulation.systeme.condition.implementation.ImplConditionProbabiliteUniforme;
import simulation.systeme.condition.implementation.ImplConditionToujoursSatisfaite;
import simulation.systeme.exception.ConditionException;
import simulation.systeme.exception.StrategieException;
import simulation.systeme.lexique.Lemme;

/**
 * Classe abstraite representant une condition d'emission / reception / memorisation sur un couple (Individu, Lemme)
 * 
 * Pour chaque nouvelle implementation concrete, veiller a ajouter au switch une valeur correspondante dans executerImplementation()
 * ainsi qu'une variable static
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public abstract class Condition {
	private final static ImplConditionProbabiliteUniforme conditionProbabiliteUniforme = new ImplConditionProbabiliteUniforme();
	private final static ImplConditionToujoursSatisfaite conditionToujoursSatisfaite = new ImplConditionToujoursSatisfaite();
	private final static ImplConditionJamaisSatisfaite conditionJamaisSatisfaite = new ImplConditionJamaisSatisfaite();

	/**
	 * Execute l'implementation de la condition d'emission pour l'individu passe en parametre et le lemme en emission donne
	 * 
	 * @param individuCourant		l'individu courant
	 * @param lemmeEnEmission		le lemme en emission
	 * @return						true si la condition est satisfaite, false sinon
	 * @throws ConditionException	si l'implementation a provoquee une exception
	 */
	public static boolean executerImplEmission(Individu individuCourant, Lemme lemmeEnEmission) throws ConditionException {
		return executerImpl(individuCourant.lireImplConditionEmission(), individuCourant, lemmeEnEmission);
	}
	
	/**
	 * Execute l'implementation de la condition de reception pour l'individu passe en parametre et le lemme en reception donne
	 * 
	 * @param individuCourant		l'individu courant
	 * @param lemmeEnReception		le lemme en reception
	 * @return						true si la condition est satisfaite, false sinon
	 * @throws ConditionException	si l'implementation a provoquee une exception
	 */
	public static boolean executerImplReception(Individu individuCourant, Lemme lemmeEnReception) throws ConditionException {
		return executerImpl(individuCourant.lireImplConditionReception(), individuCourant, lemmeEnReception);
	}
	
	/**
	 * Execute l'implementation de la condition de memorisation pour l'individu passe en parametre et le lemme en memorisation donne
	 * 
	 * @param individuCourant		l'individu courant
	 * @param lemmeEnReception		le lemme en memorisation
	 * @return						true si la condition est satisfaite, false sinon
	 * @throws ConditionException	si l'implementation a provoquee une exception
	 */
	public static boolean executerImplMemorisation(Individu individuCourant, Lemme lemmeEnMemorisation) throws ConditionException {
		return executerImpl(individuCourant.lireImplConditionMemorisation(), individuCourant, lemmeEnMemorisation);
	}
	
	
	/**
	 * Execute l'implementation de la strategie de succession passee en parametre pour l'individu donne
	 * 
	 * @param impl					l'implementation a considerer
	 * @param individuCourant		l'individu courant
	 * @param lemmeCourant			le lemme courant (en reception ou memorisation selon le cas)
	 * @return						true si la condition est satisfaite, false sinon
	 * @throws StrategieException	si l'implementation a provoquee une exception
	 */
	private static boolean executerImpl(ImplementationCondition impl, Individu individuCourant, Lemme lemmeCourant) throws ConditionException {
		Condition condition = null;

		switch (impl) {
			case CONDITION_PROBABILITE_UNIFORME:
				condition = conditionProbabiliteUniforme;
				break;
			case CONDITION_TOUJOURS_SATISFAITE:
				condition = conditionToujoursSatisfaite;
				break;
			case CONDITION_JAMAIS_SATISFAITE:
				condition = conditionJamaisSatisfaite;
				break;
			default:
				throw new ConditionException("L'implementation de condition '" + impl + "' n'est associee a aucune classe concrete (switch incomplet)", null);
		}
		
		try {
			return condition.estSatisfaite(individuCourant, lemmeCourant);
		}
		catch (Exception exception) {
			throw new ConditionException("Condition '" + impl + "' a provoquee une exception lors de son execution (contexte incoherent ?)", exception);
		}
	}
	
	/**
	 * Methode a implementer, representant une condition sur un couple (Individu, Lemme) passe en parametre
	 * 
	 * @param individuCourant	l'individu courant
	 * @param lemmeCourant		le lemme courant
	 * @return					true si la condition est satisfaite, false sinon
	 */
	protected abstract boolean estSatisfaite(Individu individuCourant, Lemme lemmeCourant);
}

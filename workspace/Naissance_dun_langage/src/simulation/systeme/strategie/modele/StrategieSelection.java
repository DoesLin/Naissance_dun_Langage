package simulation.systeme.strategie.modele;

import simulation.systeme.Individu;
import simulation.systeme.exception.StrategieException;
import simulation.systeme.lexique.Lemme;
import simulation.systeme.strategie.enumeration.ImplementationStrategieSelection;
import simulation.systeme.strategie.implementation.ImplStrategieSelectionLemmeAleatoire;
import simulation.systeme.strategie.implementation.ImplStrategieSelectionLemmeMoinsEmis;
import simulation.systeme.strategie.implementation.ImplStrategieSelectionPremierLemme;

/**
 * Classe abstraite representant une strategie de selection pour l'emission / l'elimination de lemme selon un Individu (cas d'emission) ou un couple 
 * (Individu, Lemme) (cas d'elimination)
 * 
 * Pour chaque nouvelle implementation concrete, veiller a ajouter au switch une valeur correspondante dans executerImplementation() 
 * ainsi qu'une variable static
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public abstract class StrategieSelection {
	private final static ImplStrategieSelectionLemmeAleatoire strategieSelectionLemmeAleatoire = new ImplStrategieSelectionLemmeAleatoire();
	private final static ImplStrategieSelectionPremierLemme strategieSelectionPremierLemme = new ImplStrategieSelectionPremierLemme();
	private final static ImplStrategieSelectionLemmeMoinsEmis strategieSelectionLemmeMoinsEmis = new ImplStrategieSelectionLemmeMoinsEmis();

	/**
	 * Execute l'implementation de la strategie de selection pour emission de l'individu passe en parametre
	 * 
	 * @param individuCourant		l'individu courant
	 * @return						le lemme selectionne
	 * @throws StrategieException	si l'implementation a provoquee une exception
	 */
	public static Lemme executerImplEmission(Individu individuCourant) throws StrategieException {
		return executerImpl(individuCourant.lireImplStrategieSelectionEmission(), individuCourant, null);
	}
	
	/**
	 * Execute l'implementation de la strategie de selection pour elimination pour le lemme et l'individu passes en parametre
	 * 
	 * @param individuCourant		l'individu courant
	 * @param lemmeCourant			le lemme en memorisation
	 * @return						le lemme selectionne
	 * @throws StrategieException	si l'implementation a provoquee une exception
	 */
	public static Lemme executerImplElimination(Individu individuCourant, Lemme lemmeCourant) throws StrategieException {
		return executerImpl(individuCourant.lireImplStrategieSelectionElimination(), individuCourant, lemmeCourant);
	}

	/**
	 * Execute l'implementation de la strategie de selection passee en parametre avec le contexte donne
	 * 
	 * @param impl					l'implementation a considerer
	 * @param individuCourant		l'individu courant
	 * @param lemmeCourant			le lemme courant (en memorisation dans le cas d'elimination)
	 * @return						le lemme selectionne
	 * @throws StrategieException	si l'implementation a provoquee une exception
	 */
	private static Lemme executerImpl(ImplementationStrategieSelection impl, Individu individuCourant, Lemme lemmeCourant) throws StrategieException {
		StrategieSelection strategie = null;

		switch (impl) {
			case SELECTION_LEMME_ALEATOIRE:
				strategie = strategieSelectionLemmeAleatoire;
				break;
			case SELECTION_PREMIER_LEMME:
				strategie = strategieSelectionPremierLemme;
				break;
			case SELECTION_LEMME_MOINS_EMIS:
				strategie = strategieSelectionLemmeMoinsEmis;
				break;
			default:
				throw new StrategieException("L'implementation de strategie '" + impl + "' n'est associee a aucune classe concrete (switch incomplet)", null);
		}
		
		try {
			return strategie.selectionnerLemme(individuCourant, lemmeCourant);
		}
		catch (Exception exception) {
			throw new StrategieException("Strategie '" + impl + "' a provoquee une exception lors de son execution (contexte incoherent ?)", exception);
		}
	}
	
	/**
	 * Methode a implementer, representant une strategie de selection de lemme sur un couple (Individu, Lemme) passe en parametre
	 * 
	 * @param individuCourant	l'individu courant
	 * @param lemmeCourant		le lemme courant (en memorisation dans le cas d'elimination)
	 * @return					le lemme selectionne
	 */
	protected abstract Lemme selectionnerLemme(Individu individuCourant, Lemme lemmeCourant);
}

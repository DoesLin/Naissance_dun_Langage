package simulation.systeme.strategie.modele;

import simulation.systeme.Individu;
import simulation.systeme.Voisin;
import simulation.systeme.exception.StrategieException;
import simulation.systeme.strategie.enumeration.ImplentationStrategieSuccession;
import simulation.systeme.strategie.implementation.ImplStrategieSuccessionPremierVoisin;
import simulation.systeme.strategie.implementation.ImplStrategieSuccessionVoisinAleatoire;

/**
 * Classe abstraite representant une strategie de succession d'un individu vers un de ses voisins
 * 
 * Pour chaque nouvelle implementation concrete, veiller a ajouter au switch une valeur correspondante dans executerImplementation() 
 * ainsi qu'une variable static
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public abstract class StrategieSuccession {
	private final static ImplStrategieSuccessionVoisinAleatoire strategieSuccessionVoisinAleatoire = new ImplStrategieSuccessionVoisinAleatoire();
	private final static ImplStrategieSuccessionPremierVoisin strategieSuccessionPremierVoisin = new ImplStrategieSuccessionPremierVoisin();
	
	/**
	 * Execute l'implementation de la strategie de succession passee en parametre pour l'individu donne
	 * 
	 * @param individuCourant		l'individu courant
	 * @return						le voisin successeur
	 * @throws StrategieException	si l'implementation a provoquee une exception
	 */
	public static Voisin executerImpl(Individu individuCourant) throws StrategieException {
		ImplentationStrategieSuccession implementation = individuCourant.lireImplStrategieSuccession();
		StrategieSuccession strategie = null;
		
		switch (implementation) {
			case SUCCESSION_VOISIN_ALEATOIRE:
				strategie = strategieSuccessionVoisinAleatoire;
				break;
			case SUCCESSION_PREMIER_VOISIN:
				strategie = strategieSuccessionPremierVoisin;
				break;
			default:
				throw new StrategieException("L'implementation de strategie '" + implementation + "' n'est associee a aucune classe concrete (switch incomplet)", null);
		}
		
		try {
			return strategie.determinerSuccesseur(individuCourant);
		}
		catch (Exception exception) {
			throw new StrategieException("Strategie '" + implementation + "' a provoquee une exception lors de son execution (contexte incoherent ?)", exception);
		}
	}

	/**
	 * Methode a implementer, representant une strategie de succession pour l'individu passe en parametre
	 * 
	 * @param individuCourant	l'individu courant
	 * @return					le voisin successeur
	 */
	protected abstract Voisin determinerSuccesseur(Individu individuCourant);
}

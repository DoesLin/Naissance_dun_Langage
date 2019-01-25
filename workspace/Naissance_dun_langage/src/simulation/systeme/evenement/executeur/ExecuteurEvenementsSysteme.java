package simulation.systeme.evenement.executeur;

import simulation.systeme.Individu;
import simulation.systeme.Systeme;
import simulation.systeme.evenement.modele.Evenement;
import simulation.systeme.temps.Date;

/**
 * Classe responsable d'executer les evenements relatifs au systeme i.e. evenement initial / final
 * 
 * L'utilite de cette classe est avant tout de separer les problematiques de gestion des evenements
 * de la definition du systeme
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class ExecuteurEvenementsSysteme {

	/**
	 * Genere et renvoie l'evenement initial du systeme
	 * 
	 * @return l'evenement initial du systeme
	 */
	public Evenement genererEvenementInitial() {
		return new EvenementInitial(null);
	}
	
	/**
	 * Genere et renvoie l'evenement final du systeme
	 * 
	 * @param evenementInitiateur	l'evenement initiateur
	 * @return 						l'evenement final du systeme
	 */
	public Evenement genererEvenementFinal(Evenement evenementInitiateur) {
		return new EvenementFinal(evenementInitiateur);
	}
	
	/**
	 * Evenement initial du systeme, debutant la simulation
	 * 
	 * L'evenement genere un evenement d'emission de lemme depuis le premier individu du systeme
	 * et declenche ce dernier
	 * 
	 * @author Charles MECHERIKI Yongda LIN
	 *
	 */
	private static class EvenementInitial extends Evenement {

		public EvenementInitial(Evenement evenementInitiateur) {
			super(evenementInitiateur);
		}

		@Override
		public void declencher() throws Exception {
			Individu individu = Systeme.obtenirIndividus().get(0);
			
			//Lemme lemmeEnEmission = StrategieSelection.executerImplEmission(individu);
			Evenement evenementEmission = individu.obtenirExecuteurEvenements().genererEvenementEmission(this);
			
			/*Systeme.ajouterLemmeCache(evenementEmission.lireID(), lemmeEnEmission);
			Systeme.ajouterOccurrenceCache(evenementEmission.lireID(), null);*/
			
			Systeme.ajouterPremierEvenementEnDate(
				evenementEmission,
				Date.dateSuivante(Systeme.lireDateHorloge())
			);

			Systeme.declencherProchainEvenement();
		}
	}
	
	/**
	 * Evenement final du systeme, concluant la simulation
	 * 
	 * Le declenchement de l'evenement ne fait rien
	 * 
	 * @author Charles MECHERIKI Yongda LIN
	 *
	 */
	private static class EvenementFinal extends Evenement {

		public EvenementFinal(Evenement evenementInitiateur) {
			super(evenementInitiateur);
		}

		@Override
		public void declencher() {
			// Fin de la simulation
		}
	}
}

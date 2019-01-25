package simulation.systeme.evenement.executeur;

import simulation.systeme.Individu;
import simulation.systeme.Systeme;
import simulation.systeme.Voisin;
import simulation.systeme.condition.modele.Condition;
import simulation.systeme.enumeration.TypeCritereArret;
import simulation.systeme.evenement.enumeration.IssueEvenement;
import simulation.systeme.evenement.enumeration.TypeEvenement;
import simulation.systeme.evenement.modele.Evenement;
import simulation.systeme.lexique.Lemme;
import simulation.systeme.lexique.OccurrenceLemme;
import simulation.systeme.strategie.modele.StrategieSelection;
import simulation.systeme.strategie.modele.StrategieSuccession;
import simulation.systeme.temps.Date;

/**
 * Classe responsable d'executer les evenements relatifs a un individu, 
 * i.e. emission / reception / memorisation / elimination de lemme
 * 
 * L'utilite de cette classe est avant tout de separer les problematiques de gestion des evenements 
 * de la definition d'un individu
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class ExecuteurEvenementsIndividu {
	private Individu individu;
	
	/**
	 * Creer un executeur en l'associant a l'individu passe en parametre
	 * 
	 * @param individu	l'individu a associer a l'executeur
	 */
	public ExecuteurEvenementsIndividu(Individu individu) {
		this.individu = individu;
	}
	
	/**
	 * Genere et renvoie un evenement d'emission
	 * 
	 * @param evenementInitiateur	l'evenement initiateur
	 * @return 						l'evenement d'emission genere
	 */
	public Evenement genererEvenementEmission(Evenement evenementInitiateur) {
		return new EvenementEmission(evenementInitiateur);
	}
	
	/**
	 * Genere et renvoie un evenement de reception
	 * 
	 * @param evenementInitiateur	l'evenement initiateur
	 * @return 						l'evenement de reception genere
	 */
	public Evenement genererEvenementReception(EvenementEmission evenementInitiateur) {
		return new EvenementReception(evenementInitiateur);
	}
	
	/**
	 * Genere et renvoie un evenement de memorisation
	 * 
	 * @param evenementInitiateur	l'evenement initiateur
	 * @return 						l'evenement de memorisation genere
	 */
	public Evenement genererEvenementMemorisation(EvenementReception evenementInitiateur) {
		return new EvenementMemorisation(evenementInitiateur);
	}
	
	/**
	 * Genere et renvoie un evenement d'elimination
	 * 
	 * @param evenementInitiateur	l'evenement initiateur
	 * @return 						l'evenement d'elimination genere
	 */
	public Evenement genererEvenementElimination(EvenementMemorisation evenementInitiateur) {
		return new EvenementElimination(evenementInitiateur);
	}
	
	/**
	 * Implementation de la classe Evenement responsable de l'emission d'un lemme par l'individu (de l'executeur)
	 * 
	 * Le deroulement de l'evenement consiste en :
	 * 		- la selection d'un lemme pour emission;
	 * 		- la verification de la condition d'emission de l'individu
	 * 		- la generation et l'ajout a l'echeancier des evenements de reception pour ses voisins
	 * 		- la determination d'un successeur
	 * 		- la generation et l'ajout a l'echeancier d'un evenement d'emission de ce dernier
	 * 
	 * C'est egalement ici que les occurrences de lemmes sont generees et que les caches du systeme 
	 * sont exploites
	 * 
	 * A la fin de l'evenement, le prochain evenement de l'echeancier est declenche
	 *  
	 * @author Charles MECHERIKI Yongda LIN
	 *
	 */
	private class EvenementEmission extends Evenement {

		public EvenementEmission(Evenement evenementInitiateur) {
			super(evenementInitiateur);
		}

		@Override
		public void declencher() throws Exception {
			Date date = Systeme.lireDateHorloge();
			
			OccurrenceLemme occurrenceInitiatrice = Systeme.consommerOccurrenceCache(lireID());

			Lemme lemmeEnEmission = StrategieSelection.executerImplEmission(individu);

			OccurrenceLemme occurrenceEmission = null;
			if (Condition.executerImplEmission(individu, lemmeEnEmission)) {
				occurrenceEmission = individu.nouvelleOccurrenceLemme(occurrenceInitiatrice, lemmeEnEmission, TypeEvenement.EMISSION, IssueEvenement.SUCCES, date);

				for (Voisin voisin : individu.obtenirVoisins()) {
					Evenement evenementReception = voisin.obtenirIndividu().obtenirExecuteurEvenements().genererEvenementReception(this);
					
					Systeme.ajouterLemmeCache(evenementReception.lireID(), lemmeEnEmission);
					Systeme.ajouterOccurrenceCache(evenementReception.lireID(), occurrenceEmission);
					
					Systeme.ajouterDernierEvenementEnDate(
						evenementReception,
						Systeme.lireDateHorloge().plusDelais(voisin.lireDelais())
					);
				}
			}
			else {
				occurrenceEmission = individu.nouvelleOccurrenceLemme(occurrenceInitiatrice, lemmeEnEmission, TypeEvenement.EMISSION, IssueEvenement.ECHEC, date);
			}
			Voisin successeur = StrategieSuccession.executerImpl(individu);
			
			Evenement evenementEmission = successeur.obtenirIndividu().obtenirExecuteurEvenements().genererEvenementEmission(this);
			
			Systeme.ajouterOccurrenceCache(evenementEmission.lireID(), occurrenceEmission);
			
			Systeme.ajouterDernierEvenementEnDate(
				evenementEmission,
				Systeme.lireDateHorloge().plusDelais(successeur.lireDelais())
			);
			
			Systeme.declencherProchainEvenement();
		}
	}
	
	/**
	 * Implementation de la classe Evenement responsable de la reception d'un lemme par l'individu (de l'executeur)
	 * 
	 * Le deroulement de l'evenement consiste en la verification de la condition de reception, 
	 * et si celle-ci est verifiee, la generation d'un evenement de memorisation
	 * 
	 * C'est egalement ici que les occurrences de lemmes sont generees et que les caches du systeme 
	 * sont exploites
	 * 
	 * A la fin de l'evenement, le prochain evenement de l'echeancier est declenche
	 *  
	 * @author Charles MECHERIKI Yongda LIN
	 *
	 */
	public class EvenementReception extends Evenement {

		public EvenementReception(EvenementEmission evenementInitiateur) {
			super(evenementInitiateur);
		}

		@Override
		public void declencher() throws Exception {
			Date date = Systeme.lireDateHorloge();
			Lemme lemmeEnReception = Systeme.consommerLemmeCache(lireID());
			OccurrenceLemme occurrenceInitiatrice = Systeme.consommerOccurrenceCache(lireID());
			OccurrenceLemme occurrenceReception = null;
			
			if (Condition.executerImplReception(individu, lemmeEnReception)) {
				occurrenceReception = individu.nouvelleOccurrenceLemme(occurrenceInitiatrice, lemmeEnReception, TypeEvenement.RECEPTION, IssueEvenement.SUCCES, date);

				if (!individu.connaitLemme(lemmeEnReception)) {
					Evenement evenementMemorisation = genererEvenementMemorisation(this);
					
					Systeme.ajouterLemmeCache(evenementMemorisation.lireID(), lemmeEnReception);
					Systeme.ajouterOccurrenceCache(evenementMemorisation.lireID(), occurrenceReception);
					
					Systeme.ajouterPremierEvenementEnDate(
						evenementMemorisation,
						Systeme.lireDateHorloge()
					);
				}
			}
			else {
				occurrenceReception = individu.nouvelleOccurrenceLemme(occurrenceInitiatrice, lemmeEnReception, TypeEvenement.RECEPTION, IssueEvenement.ECHEC, date);
			}
			Systeme.declencherProchainEvenement();
		}
	}
	
	/**
	 * Implementation de la classe Evenement responsable de la memorisation d'un lemme par l'individu (de l'executeur)
	 * 
	 * Le deroulement de l'evenement consiste en la verification de la condition de memorisation, 
	 * et si celle-ci est verifiee et que le lexique est plein, la generation d'un evenement d'elimination
	 * 
	 * C'est egalement ici que les occurrences de lemmes sont generees et que les caches du systeme 
	 * sont exploites
	 * 
	 * A la fin de l'evenement, le prochain evenement de l'echeancier est declenche
	 *  
	 * @author Charles MECHERIKI Yongda LIN
	 *
	 */
	public class EvenementMemorisation extends Evenement {

		public EvenementMemorisation(EvenementReception evenementInitiateur) {
			super(evenementInitiateur);
		}

		@Override
		public void declencher() throws Exception {
			Date date = Systeme.lireDateHorloge();
			Lemme lemmeEnMemorisation = Systeme.consommerLemmeCache(lireID());
			OccurrenceLemme occurrenceInitiatrice = Systeme.consommerOccurrenceCache(lireID());
			OccurrenceLemme occurrenceMemorisation = null;
			
			if (Condition.executerImplMemorisation(individu, lemmeEnMemorisation)) {
				
				occurrenceMemorisation = individu.nouvelleOccurrenceLemme(occurrenceInitiatrice, lemmeEnMemorisation, TypeEvenement.MEMORISATION, IssueEvenement.SUCCES, date);

				if (!individu.aLexiquePlein()) {
					individu.memoriserLemme(lemmeEnMemorisation);
					
					if (individu.aLexiquePlein()) {
						if (Systeme.lireTypeCritereArret() == TypeCritereArret.PREMIER_LEXIQUE_PLEIN) {
							Systeme.declencherEvenementFinal(this, Systeme.lireTypeCritereArret().toString());
						}
						else if (Systeme.lireTypeCritereArret() == TypeCritereArret.TOUS_LEXIQUES_PLEINS) {
							boolean tousLexiquesPleins = true;
							for (Individu individu : Systeme.obtenirIndividus()) {
								tousLexiquesPleins = tousLexiquesPleins && individu.aLexiquePlein();
							}
							if (tousLexiquesPleins) {
								Systeme.declencherEvenementFinal(this, Systeme.lireTypeCritereArret().toString());
							}
						}
					}
				}
				else {
					Evenement evenementElimination = genererEvenementElimination(this);

					Systeme.ajouterLemmeCache(evenementElimination.lireID(), lemmeEnMemorisation);
					Systeme.ajouterOccurrenceCache(evenementElimination.lireID(), occurrenceMemorisation);
					
					Systeme.ajouterPremierEvenementEnDate(
						evenementElimination,
						Systeme.lireDateHorloge()
					);
				}
			}
			else {
				occurrenceMemorisation = individu.nouvelleOccurrenceLemme(occurrenceInitiatrice, lemmeEnMemorisation, TypeEvenement.MEMORISATION, IssueEvenement.ECHEC, date);
			}
			
			Systeme.declencherProchainEvenement();
		}
	}
	
	/**
	 * Implementation de la classe Evenement responsable de l'elimination d'un lemme par l'individu (de l'executeur)
	 * 
	 * Le deroulement de l'evenement consiste en la selection d'un lemme pour elimination,
	 * et l'elimination de celui-ci du lexique de l'individu
	 * 
	 * C'est egalement ici que les occurrences de lemmes sont generees et que les caches du systeme 
	 * sont exploites
	 * 
	 * A la fin de l'evenement, le prochain evenement de l'echeancier est declenche
	 *  
	 * @author Charles MECHERIKI Yongda LIN
	 *
	 */
	private class EvenementElimination extends Evenement {

		public EvenementElimination(EvenementMemorisation evenementInitiateur) {
			super(evenementInitiateur);
		}

		@Override
		public void declencher() throws Exception {
			Date date = Systeme.lireDateHorloge();
			Lemme lemmeEnMemorisation = Systeme.consommerLemmeCache(lireID());
			OccurrenceLemme occurrenceInitiatrice = Systeme.consommerOccurrenceCache(lireID());
			
			Lemme lemmeEnRemplacement = StrategieSelection.executerImplElimination(individu, lemmeEnMemorisation);
			
			individu.nouvelleOccurrenceLemme(occurrenceInitiatrice, lemmeEnRemplacement, TypeEvenement.ELIMINATION, IssueEvenement.SUCCES, date);
			individu.remplacerLemme(lemmeEnRemplacement, lemmeEnMemorisation);

			Systeme.declencherProchainEvenement();
		}
	}
}

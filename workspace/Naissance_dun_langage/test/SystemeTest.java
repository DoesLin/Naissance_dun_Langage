import java.util.ArrayList;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import simulation.systeme.ConfigurationSysteme;
import simulation.systeme.Individu;
import simulation.systeme.Systeme;
import simulation.systeme.Voisin;
import simulation.systeme.condition.enumeration.ImplementationCondition;
import simulation.systeme.evenement.enumeration.IssueEvenement;
import simulation.systeme.evenement.enumeration.TypeEvenement;
import simulation.systeme.lecture.LecteurConfigurationSysteme;
import simulation.systeme.lexique.Lemme;
import simulation.systeme.lexique.OccurrenceLemme;
import simulation.systeme.strategie.enumeration.ImplementationStrategieSelection;
import simulation.systeme.temps.Date;

/**
 * Fichier de tests JUnit verifiant la coherence de l'etat du systeme en fonction de la
 * configuration et du deroulement de la simulation
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class SystemeTest {
	
	private static final String urlConfig = "test/config/config.xml";
	private static ConfigurationSysteme configurationSysteme;
	
	/**
	 * Initialise le systeme selon le fichier de configuration dont l'url est mentionnee en attribut
	 */
	@BeforeClass
	public static void initialisationClasse() {
		try {
			LecteurConfigurationSysteme lecteurConfigSysteme = new LecteurConfigurationSysteme();
			configurationSysteme = lecteurConfigSysteme.lireConfigSystemeDepuisFichier(urlConfig);
			Systeme.configurer(configurationSysteme);
			Systeme.lancer();
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Teste le nombre d'individu du systeme
	 */
	@Test
	public void nombreIndividusTest() {
		Assert.assertEquals(4, Systeme.obtenirIndividus().size());
	}
	
	/**
	 * Teste la configuration par defaut pour les individus du systeme
	 */
	@Test
	public void configurationParDefautIndividusTest() {
		boolean test = true;
		for (Individu individu : Systeme.obtenirIndividus()) {
			// Parce que le premier individu a des parametres personnalises
			if (individu.lireID() == 1) {
				test = test && (individu.lireImplStrategieSelectionElimination() == configurationSysteme.lireImplStrategieSelectionEliminationParDefaut())
						&& (individu.lireImplStrategieSuccession() == configurationSysteme.lireImplStrategieSuccessionParDefaut());
			}
			else {
				test = test && (individu.obtenirLexique().lireTailleInitiale() == configurationSysteme.lireTailleInitialeLexiqueParDefaut())
					&& (individu.obtenirLexique().lireTailleMaximale() == configurationSysteme.lireTailleMaximaleLexiqueParDefaut())
					&& (individu.lireImplConditionEmission() == configurationSysteme.lireImplConditionEmissionParDefaut())
					&& (individu.lireImplConditionReception() == configurationSysteme.lireImplConditionReceptionParDefaut())
					&& (individu.lireImplConditionMemorisation() == configurationSysteme.lireImplConditionMemorisationParDefaut())
					&& (individu.lireImplStrategieSelectionEmission() == configurationSysteme.lireImplStrategieSelectionEmissionParDefaut())
					&& (individu.lireImplStrategieSelectionElimination() == configurationSysteme.lireImplStrategieSelectionEliminationParDefaut())
					&& (individu.lireImplStrategieSuccession() == configurationSysteme.lireImplStrategieSuccessionParDefaut());
			}
		}
		Assert.assertTrue(test);
	}
	
	/**
	 * Teste la configuration pour le premier individu
	 */
	@Test
	public void configurationPremierIndividuTest() {
		Individu premierIndividu = Systeme.obtenirIndividus().get(0);
		
		boolean test = true;
		
		test = test && (premierIndividu.obtenirLexique().lireTailleInitiale() == 2)
			&& (premierIndividu.obtenirLexique().lireTailleMaximale() == 5)
			&& (premierIndividu.lireImplConditionEmission() == ImplementationCondition.CONDITION_TOUJOURS_SATISFAITE)
			&& (premierIndividu.lireImplConditionReception() == ImplementationCondition.CONDITION_TOUJOURS_SATISFAITE)
			&& (premierIndividu.lireImplConditionMemorisation() == ImplementationCondition.CONDITION_TOUJOURS_SATISFAITE)
			&& (premierIndividu.lireImplStrategieSelectionEmission() == ImplementationStrategieSelection.SELECTION_PREMIER_LEMME);
		
		Assert.assertTrue(test);
	}
	
	/**
	 * Teste la coherence des occurrences de lemmes produites lors de la simulation
	 * ainsi que le reconstruction de lexique
	 * 
	 * Attention, dans des cas tres particuliers de croisements de memorisation / elimination ce test 
	 * peut echouer
	 */
	@Test
	public void coherenceOccurrencesLemmesTest() {
		ArrayList<OccurrenceLemme> listeOccurrences = new ArrayList<OccurrenceLemme>();

		for (Individu individu : Systeme.obtenirIndividus()) {
			listeOccurrences.addAll(
				individu.obtenirTableOccurrencesLemmes().obtenirListeOccurrencesLemmes(
					Lemme.QUELCONQUE, TypeEvenement.QUELCONQUE, IssueEvenement.QUELCONQUE
				)
			);
		}
		
		// Verifie la chaine Memorisation < Reception < Emission
		boolean test = true;
		for (OccurrenceLemme occurrence : listeOccurrences) {
			if (occurrence.getTypeEvenement() == TypeEvenement.RECEPTION) {
				test = test && (occurrence.getOccurrenceInitiatrice().getTypeEvenement() == TypeEvenement.EMISSION)
					&& (occurrence.getOccurrenceInitiatrice().getIssueEvenement() == IssueEvenement.SUCCES)
					&& (occurrence.getOccurrenceInitiatrice().getLemme() == occurrence.getLemme())
					&& (occurrence.getOccurrenceInitiatrice().getDate().estAvant(occurrence.getDate()));
			}
			else if (occurrence.getTypeEvenement() == TypeEvenement.MEMORISATION) {
				test = test && (occurrence.getOccurrenceInitiatrice().getTypeEvenement() == TypeEvenement.RECEPTION)
					&& (occurrence.getOccurrenceInitiatrice().getIssueEvenement() == IssueEvenement.SUCCES)
					&& (occurrence.getOccurrenceInitiatrice().getLemme() == occurrence.getLemme())
					&& (occurrence.getOccurrenceInitiatrice().getDate().estAvant(occurrence.getDate()))
					&& (occurrence.getOccurrenceInitiatrice().getOccurrenceInitiatrice().getTypeEvenement() == TypeEvenement.EMISSION)
					&& (occurrence.getOccurrenceInitiatrice().getOccurrenceInitiatrice().getIssueEvenement() == IssueEvenement.SUCCES)
					&& (occurrence.getOccurrenceInitiatrice().getOccurrenceInitiatrice().getLemme() == occurrence.getLemme())
					&& (occurrence.getOccurrenceInitiatrice().getOccurrenceInitiatrice().getDate().estAvant(occurrence.getOccurrenceInitiatrice().getDate()));
					if (occurrence.getIssueEvenement() == IssueEvenement.SUCCES) {
						test = test && (occurrence.getIndividu().retrouverLexique(occurrence.getDate()).contains(occurrence.getLemme()))
							&& (!occurrence.getIndividu().retrouverLexique(occurrence.getOccurrenceInitiatrice().getOccurrenceInitiatrice().getDate()).contains(occurrence.getLemme()));
					}
			}
			else if (occurrence.getTypeEvenement() == TypeEvenement.ELIMINATION) {
				test = test && (occurrence.getOccurrenceInitiatrice().getTypeEvenement() == TypeEvenement.MEMORISATION)
					&& (occurrence.getOccurrenceInitiatrice().getIssueEvenement() == IssueEvenement.SUCCES)
					&& (!occurrence.getIndividu().retrouverLexique(occurrence.getDate()).contains(occurrence.getLemme()));
			}
		}
		Assert.assertTrue(test);
	}

	/**
	 * Teste si les delais de reception sont effectifs
	 */
	@Test
	public void delaisReceptionTest() {
		ArrayList<OccurrenceLemme> listeOccurrencesReception = new ArrayList<OccurrenceLemme>();

		for (Individu individu : Systeme.obtenirIndividus()) {
			listeOccurrencesReception.addAll(
				individu.obtenirTableOccurrencesLemmes().obtenirListeOccurrencesLemmes(
					Lemme.QUELCONQUE, TypeEvenement.RECEPTION, IssueEvenement.QUELCONQUE
				)
			);
		}
		
		boolean test = true;
		for (OccurrenceLemme occurrence : listeOccurrencesReception) {
			for (Voisin voisin : occurrence.getOccurrenceInitiatrice().getIndividu().obtenirVoisins()) {
				if (voisin.obtenirIndividu() == occurrence.getIndividu()) {
					test = test && occurrence.getDate().equals(Date.depuisValeur(
						occurrence.getOccurrenceInitiatrice().getDate().lireValeur() + voisin.lireDelais().lireValeur()
					));
				}
			}
		}
		Assert.assertTrue(test);
	}
	
	/**
	 * Teste si les delais de succession sont effectifs
	 */
	@Test
	public void delaisSuccessionTest() {
		ArrayList<OccurrenceLemme> listeOccurrencesReception = new ArrayList<OccurrenceLemme>();

		for (Individu individu : Systeme.obtenirIndividus()) {
			listeOccurrencesReception.addAll(
				individu.obtenirTableOccurrencesLemmes().obtenirListeOccurrencesLemmes(
					Lemme.QUELCONQUE, TypeEvenement.EMISSION, IssueEvenement.QUELCONQUE
				)
			);
		}
		
		boolean test = true;
		for (OccurrenceLemme occurrence : listeOccurrencesReception) {
			if (occurrence.getOccurrenceInitiatrice() != null) {
				for (Voisin voisin : occurrence.getOccurrenceInitiatrice().getIndividu().obtenirVoisins()) {
					if (voisin.obtenirIndividu() == occurrence.getIndividu()) {
						test = test && occurrence.getDate().equals(Date.depuisValeur(
							occurrence.getOccurrenceInitiatrice().getDate().lireValeur() + voisin.lireDelais().lireValeur()
						));
					}
				}
			}
		}
		Assert.assertTrue(test);
	}
	
	/**
	 * Teste le critere d'arret TOUS_LEXIQUES_PLEINS
	 */
	@Test
	public void critereArretTousLexiquesPleinsTest() {
		int lexiquesPleins = 0;
		
		for (Individu individu : Systeme.obtenirIndividus()) {
			if (individu.obtenirLexique().size() == individu.obtenirLexique().lireTailleMaximale()) {
				lexiquesPleins++; 
			}
		}
		
		Assert.assertEquals(Systeme.obtenirIndividus().size(), lexiquesPleins);
	}
}

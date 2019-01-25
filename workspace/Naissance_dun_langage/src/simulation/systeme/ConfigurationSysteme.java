package simulation.systeme;

import java.util.ArrayList;

import simulation.systeme.condition.enumeration.ImplementationCondition;
import simulation.systeme.strategie.enumeration.ImplementationStrategieSelection;
import simulation.systeme.strategie.enumeration.ImplentationStrategieSuccession;

/**
 * La configuration du systeme, provenant directement de la lecture des fichiers de configuration
 * Cette classe sert avant tout de conteneur a disposition du systeme
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class ConfigurationSysteme {
	private int nombreIndividus;
	private int tailleInitialeLexiqueParDefaut;
	private int tailleMaximaleLexiqueParDefaut;
	
	private ImplementationStrategieSelection implStrategieSelectionEmissionParDefaut;
	private ImplementationStrategieSelection implStrategieSelectionEliminationParDefaut;
	private ImplentationStrategieSuccession implStrategieSuccessionParDefaut;
	private ImplementationCondition implConditionEmissionParDefaut;
	private ImplementationCondition implConditionReceptionParDefaut;
	private ImplementationCondition implConditionMemorisationParDefaut;
	
	private CritereArret critereArret;
	private ArrayList<Individu> individus;
	private int[][] matriceVoisinage;
	/**
	 * Cree une configuration systeme avec tous ses composants - sauf les individus, a ajouter au fur et a mesure de leur creation
	 * 
	 * @param nombreIndividus								le nombre d'individus du systeme
	 * @param tailleInitialeLexiqueParDefaut				la taille initiale de lexique par defaut pour les individus
	 * @param tailleMaximaleLexiqueParDefaut				la taille maximale de lexique par defaut pour les individus
	 * @param implConditionEmissionParDefaut				la condition d'emission par defaut pour les individus
	 * @param implConditionReceptionParDefaut				la condition de reception par defaut pour les individus
	 * @param implConditionMemorisationParDefaut			la condition de memorisation par defaut pour les individus
	 * @param implStrategieSelectionEmissionParDefaut		la strategie de selection pour emission par defaut pour les individus
	 * @param implStrategieSelectionEliminationParDefaut	la strategie de selection pour elimination par defaut pour les individus
	 * @param implStrategieSuccessionParDefaut				la strategie de succession par defaut pour les individus
	 * @param critereArret									le critere d'arret de la simulation
	 */
	public ConfigurationSysteme(
			int nombreIndividus, int tailleInitialeLexiqueParDefaut, int tailleMaximaleLexiqueParDefaut,
			ImplementationCondition implConditionEmissionParDefaut,
			ImplementationCondition implConditionReceptionParDefaut,
			ImplementationCondition implConditionMemorisationParDefaut,
			ImplementationStrategieSelection implStrategieSelectionEmissionParDefaut, 
			ImplementationStrategieSelection implStrategieSelectionEliminationParDefaut,
			ImplentationStrategieSuccession implStrategieSuccessionParDefaut,
			CritereArret critereArret, ArrayList<Individu> individus, int[][] matriceVoisinage) {
		this.nombreIndividus = nombreIndividus;
		this.tailleInitialeLexiqueParDefaut = tailleInitialeLexiqueParDefaut;
		this.tailleMaximaleLexiqueParDefaut = tailleMaximaleLexiqueParDefaut;
		this.implConditionEmissionParDefaut = implConditionEmissionParDefaut;
		this.implConditionReceptionParDefaut = implConditionReceptionParDefaut;
		this.implConditionMemorisationParDefaut = implConditionMemorisationParDefaut;
		this.implStrategieSelectionEmissionParDefaut = implStrategieSelectionEmissionParDefaut;
		this.implStrategieSelectionEliminationParDefaut = implStrategieSelectionEliminationParDefaut;
		this.implStrategieSuccessionParDefaut = implStrategieSuccessionParDefaut;
		this.critereArret = critereArret;
		this.individus = individus;
		this.matriceVoisinage = matriceVoisinage;
	}

	 /**
	 * Renvoie le nombre d'individus du systeme
	 * 
	 * @return le nombre d'individus du systeme
	 */
	public int lireNombreIndividus() {
		return nombreIndividus;
	}

	/**
	 * Renvoie la taille initiale de lexique par defaut pour les individus
	 * 
	 * @return la taille initiale de lexique par defaut pour les individus
	 */
	public int lireTailleInitialeLexiqueParDefaut() {
		return tailleInitialeLexiqueParDefaut;
	}

	/**
	 * Renvoie la taille maximale de lexique par defaut pour les individus
	 * 
	 * @return la taille maximale de lexique par defaut pour les individus
	 */
	public int lireTailleMaximaleLexiqueParDefaut() {
		return tailleMaximaleLexiqueParDefaut;
	}

	/**
	 * Renvoie la condition d'emission par defaut pour les individus
	 * 
	 * @return la condition d'emission par defaut pour les individus
	 */
	public ImplementationCondition lireImplConditionEmissionParDefaut() {
		return implConditionEmissionParDefaut;
	}

	/**
	 * Renvoie la condition de reception par defaut pour les individus
	 * 
	 * @return la condition de reception par defaut pour les individus
	 */
	public ImplementationCondition lireImplConditionReceptionParDefaut() {
		return implConditionReceptionParDefaut;
	}

	/**
	 * Renvoie la condition de memorisation par defaut pour les individus
	 * 
	 * @return la condition de memorisation par defaut pour les individus
	 */
	public ImplementationCondition lireImplConditionMemorisationParDefaut() {
		return implConditionMemorisationParDefaut;
	}
	
	/**
	 * Renvoie la strategie de selection pour emission par defaut pour les individus
	 * 
	 * @return la strategie de selection pour emission par defaut pour les individus
	 */
	public ImplementationStrategieSelection lireImplStrategieSelectionEmissionParDefaut() {
		return implStrategieSelectionEmissionParDefaut;
	}

	/**
	 * Renvoie la strategie de selection pour elimination par defaut pour les individus
	 * 
	 * @return la strategie de selection pour elimination par defaut pour les individus
	 */
	public ImplementationStrategieSelection lireImplStrategieSelectionEliminationParDefaut() {
		return implStrategieSelectionEliminationParDefaut;
	}

	/**
	 * Renvoie la strategie de succession par defaut pour les individus
	 * 
	 * @return la strategie de succession par defaut pour les individus
	 */
	public ImplentationStrategieSuccession lireImplStrategieSuccessionParDefaut() {
		return implStrategieSuccessionParDefaut;
	}

	/**
	 * Renvoie le critere d'arret de la simulation
	 * 
	 * @return le critere d'arret de la simulation
	 */
	public CritereArret lireCritereArret() {
		return critereArret;
	}
	
	/**
	 * Renvoie les individus du systeme
	 * 
	 * @return les individus du systeme
	 */
	public ArrayList<Individu> obtenirIndividus() {
		return individus;
	}
	
	/**
	 * Renvoie la matrice de voisinage
	 * 
	 * @return la matrice de voisinage
	 */
	public int[][] lireMatriceVoisinage() {
		return matriceVoisinage;
	}
}

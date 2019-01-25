package simulation.systeme;

import java.util.ArrayList;

import simulation.systeme.condition.enumeration.ImplementationCondition;
import simulation.systeme.evenement.enumeration.IssueEvenement;
import simulation.systeme.evenement.enumeration.TypeEvenement;
import simulation.systeme.evenement.executeur.ExecuteurEvenementsIndividu;
import simulation.systeme.lexique.Lemme;
import simulation.systeme.lexique.Lexique;
import simulation.systeme.lexique.OccurrenceLemme;
import simulation.systeme.lexique.TableOccurrencesLemmes;
import simulation.systeme.strategie.enumeration.ImplementationStrategieSelection;
import simulation.systeme.strategie.enumeration.ImplentationStrategieSuccession;
import simulation.systeme.temps.Date;

/**
 * Un individu du systeme
 * 
 * Chaque individu est identifie par une lettre, possede un lexique, des voisins et ses propres 
 * implementations de conditions et strategies
 * 
 * Les evenements relatifs a un individu (emission / reception / memorisation / elimination de lemme) sont geres 
 * par un executeur, qui se charge egalement de remplir une table d'occurrences de lemmes
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class Individu {
	private static int compteur = 0;
	private int ID;
	private char lettre;
	
	private Lexique lexique;
	private TableOccurrencesLemmes tableOccurrencesLemmes;
	private ArrayList<Voisin> voisins;
	
	private ExecuteurEvenementsIndividu executeurEvenements;
	
	private ImplementationCondition implConditionEmission;
	private ImplementationCondition implConditionReception;
	private ImplementationCondition implConditionMemorisation;
	
	private ImplementationStrategieSelection implStrategieSelectionEmission;
	private ImplementationStrategieSelection implStrategieSelectionElimination;
	private ImplentationStrategieSuccession implStrategieSuccession;
	
	/**
	 * Cree un individu et initialise ses implementations de condition et strategies
	 * 
	 * @param implConditionEmission					la condition d'emission de l'individu
	 * @param implConditionReception				la condition de reception de l'individu
	 * @param implConditionMemorisation				la condition de memorisation de l'individu
	 * @param implStrategieSelectionEmission		la strategie de selection de lemme pour emission de l'individu
	 * @param implStrategieSelectionElimination		la strategie de selection de lemme pour elimination de l'individu
	 * @param implStrategieSuccession				la strategie de succession de l'individu
	 */
	public Individu(ImplementationCondition implConditionEmission,
			ImplementationCondition implConditionReception,
			ImplementationCondition implConditionMemorisation,
			ImplementationStrategieSelection implStrategieSelectionEmission,
			ImplementationStrategieSelection implStrategieSelectionElimination,
			ImplentationStrategieSuccession implStrategieSuccession) {
		this.ID = ++compteur;
		this.lettre = (char)(64 + ID);

		this.lexique = new Lexique();
		this.tableOccurrencesLemmes = new TableOccurrencesLemmes();
		this.voisins = new ArrayList<Voisin>();

		this.executeurEvenements = new ExecuteurEvenementsIndividu(this);
		
		this.implConditionEmission = implConditionEmission;
		this.implConditionReception = implConditionReception;
		this.implConditionMemorisation = implConditionMemorisation;
		this.implStrategieSelectionEmission = implStrategieSelectionEmission;
		this.implStrategieSelectionElimination = implStrategieSelectionElimination;
		this.implStrategieSuccession = implStrategieSuccession;
	}

	@Override
	public String toString() {
		return "Individu " + lettre;
	}
	
	/**
	 * Renvoie le nom de classe pour l'individu
	 * 
	 * @return le nom de classe pour l'individu
	 */
	public String lireNomClasse() {
		return toString().replace(' ', '-').toLowerCase();
	}

	/**
	 * Renvoie l'ID de l'individu
	 * 
	 * @return l'ID de l'individu
	 */
	public int lireID() {
		return ID;
	}

	/**
	 * Renvoie la lettre de l'individu
	 * 
	 * @return la lettre de l'individu
	 */
	public char lireLettre() {
		return lettre;
	}

	/**
	 * Renvoie la table des coccurrences de lemmes de l'individu
	 * 
	 * @return la table des coccurrences de lemmes de l'individu
	 */
	public TableOccurrencesLemmes obtenirTableOccurrencesLemmes() {
		return tableOccurrencesLemmes;
	}

	/**
	 * Genere une nouvelle occurrence de lemme selon le contexte (type / issue d'evenement, date, etc.)
	 * 
	 * @param occurrenceInitiatrice	l'occurrence	l'occurrence initiatrice de l'occurrence courante (typiquement, celle de l'ev. initiateur)
	 * @param lemme									le lemme a l'origine de l'occurrence
	 * @param typeEvenement							le type de l'evenement a l'origine de l'occurrence
	 * @param issueEvenement						l'issue de l'evenement a l'origine de l'occurrence
	 * @param date									la date de l'occurrence
	 * @return										l'occurrence creee
	 */
	public OccurrenceLemme nouvelleOccurrenceLemme(OccurrenceLemme occurrenceInitiatrice, Lemme lemme, TypeEvenement typeEvenement, IssueEvenement issueEvenement, Date date) {
		return tableOccurrencesLemmes.nouvelleOccurenceLemme(occurrenceInitiatrice, this, lemme, typeEvenement, issueEvenement, date);
	}
	
	/**
	 * Genere le lexique de l'individu - cf. la classe Lexique pour l'implementation - 
	 * selon une taille initiale et maximale
	 * 
	 * @param tailleInitiale	la taille initiale pour le lexique
	 * @param tailleMaximale	la taille maximale pour le lexique
	 */
	public void genererLexique(int tailleInitiale, int tailleMaximale) {
		lexique.generer(tailleInitiale, tailleMaximale, this);
	}
	
	/**
	 * Renvoie le lexique de l'individu
	 * 
	 * @return le lexique de l'individu
	 */
	public Lexique obtenirLexique() {
		return lexique;
	}
	
	/**
	 * Recree le lexique de l'individu comme il se trouvait a la date donnee
	 * 
	 * @param date	la date du lexique a recreer
	 * @return		le lexique de l'individu comme il se trouvait a la date donnee
	 */
	public Lexique retrouverLexique(Date date) {
		Lexique lexiqueRetrouve = new Lexique();
		lexiqueRetrouve.generer(obtenirLexique().lireTailleInitiale(), obtenirLexique().lireTailleMaximale(), this);
		
		ArrayList<OccurrenceLemme> occurrencesLemmes = tableOccurrencesLemmes.obtenirListeOccurrencesLemmesOrdonnee(
			Lemme.QUELCONQUE, TypeEvenement.QUELCONQUE, IssueEvenement.SUCCES, Date.valeurInitiale, date
		);

		for (OccurrenceLemme occurrenceLemme : occurrencesLemmes) {
			if (occurrenceLemme.getTypeEvenement() == TypeEvenement.MEMORISATION) {
				lexiqueRetrouve.add(occurrenceLemme.getLemme());
			}
			else if (occurrenceLemme.getTypeEvenement() == TypeEvenement.ELIMINATION) {
				lexiqueRetrouve.remove(occurrenceLemme.getLemme());
			}
		}

		return lexiqueRetrouve;
	}

	/**
	 * Renvoie true si le lexique de l'individu est plein, sinon false
	 * 
	 * @return true si le lexique de l'individu est plein, sinon false
	 */
	public boolean aLexiquePlein() {
		return lexique.estPlein();
	}
	
	/**
	 * Renvoie true si l'individu possede le lemme donne dans son lexique, sinon false
	 * 
	 * @param lemme		le lemme a verifier
	 * @return			true si l'individu possede le lemme donne dans son lexique, sinon false
	 */
	public boolean connaitLemme(Lemme lemme) {
		return lexique.contains(lemme);
	}
	
	/**
	 * Ajoute le lemme donne au lexique de l'individu
	 * 
	 * @param lemme		le lemme a ajouter au lexique de l'indivdu
	 */
	public void memoriserLemme(Lemme lemme) {
		lexique.add(lemme);
	}
	
	/**
	 * Remplace le lemme lemmeARemplacer du lexique de l'individu par le lemme nouveauLemme
	 * 
	 * @param lemmeARemplacer	lemme a remplacer dans le lexique de l'individu
	 * @param nouveauLemme		lemme prenant place dans le lexique de l'individu
	 */
	public void remplacerLemme(Lemme lemmeARemplacer, Lemme nouveauLemme) {
		if (lexique.remove(lemmeARemplacer)) {
			lexique.add(nouveauLemme);
		}
	}
	
	/**
	 * Renvoie l'executeur d'evenements de l'individu
	 * 
	 * @return l'executeur d'evenements de l'individu
	 */
	public ExecuteurEvenementsIndividu obtenirExecuteurEvenements() {
		return executeurEvenements;
	}

	/**
	 * Renvoie l'implementation de condition d'emission de l'individu
	 * 
	 * @return l'implementation de condition d'emission de l'individu
	 */
	public ImplementationCondition lireImplConditionEmission() {
		return implConditionEmission;
	}
	
	/**
	 * Renvoie l'implementation de condition de reception de l'individu
	 * 
	 * @return l'implementation de condition de reception de l'individu
	 */
	public ImplementationCondition lireImplConditionReception() {
		return implConditionReception;
	}
	
	/**
	 * Renvoie l'implementation de condition de memorisation de l'individu
	 * 
	 * @return l'implementation de condition de memorisation de l'individu
	 */
	public ImplementationCondition lireImplConditionMemorisation() {
		return implConditionMemorisation;
	}
	
	/**
	 * Renvoie l'implementation de strategie de selection de lemme pour emission de l'individu
	 * 
	 * @return l'implementation de strategie de selection de lemme pour emission de l'individu
	 */
	public ImplementationStrategieSelection lireImplStrategieSelectionEmission() {
		return implStrategieSelectionEmission;
	}
	
	/**
	 * Renvoie l'implementation de strategie de selection de lemme pour elimination de l'individu
	 * 
	 * @return l'implementation de strategie de selection de lemme pour elimination de l'individu
	 */
	public ImplementationStrategieSelection lireImplStrategieSelectionElimination() {
		return implStrategieSelectionElimination;
	}
	
	/**
	 * Renvoie la strategie de succession de l'individu
	 * 
	 * @return la strategie de succession de l'individu
	 */
	public ImplentationStrategieSuccession lireImplStrategieSuccession() {
		return implStrategieSuccession;
	}

	/**
	 * Ajoute le voisin donne comme voisin de l'individu
	 * 
	 * @param voisin	le voisin a ajouter a l'individu
	 */
	public void ajouterVoisin(Voisin voisin) {
		voisins.add(voisin);
	}
	
	/**
	 * Renvoie les voisins de l'individu
	 * 
	 * @return les voisins de l'individu
	 */
	public ArrayList<Voisin> obtenirVoisins() {
		return voisins;
	}
}

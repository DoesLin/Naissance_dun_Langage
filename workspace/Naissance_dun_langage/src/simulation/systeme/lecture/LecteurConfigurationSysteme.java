package simulation.systeme.lecture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import simulation.systeme.ConfigurationSysteme;
import simulation.systeme.CritereArret;
import simulation.systeme.Individu;
import simulation.systeme.Voisin;
import simulation.systeme.condition.enumeration.ImplementationCondition;
import simulation.systeme.enumeration.TypeCritereArret;
import simulation.systeme.exception.ConfigurationException;
import simulation.systeme.exception.ConfigurationException.Objet;
import simulation.systeme.exception.ConfigurationException.Cause;
import simulation.systeme.strategie.enumeration.ImplementationStrategieSelection;
import simulation.systeme.strategie.enumeration.ImplentationStrategieSuccession;
import simulation.systeme.temps.Delais;

/**
 * Classe permettant la lecture de la configuration du systeme
 * 
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class LecteurConfigurationSysteme {
	private final String strConfig = "config";
	private final String strSysteme = "systeme";
	private final String strIndividu = "individu";
	private final String strNombreIndividus = "nombreIndividus";
	private final String strURLFichierMatriceVoisinage = "urlFichierMatriceVoisinage";
	private final String strTailleInitialeLexique = "tailleInitialeLexique";
	private final String strTailleMaximaleLexique = "tailleMaximaleLexique";
	private final String strImplConditionEmission = "implConditionEmission";
	private final String strImplConditionReception = "implConditionReception";
	private final String strImplConditionMemorisation = "implConditionMemorisation";
	private final String strImplStrategieSelectionEmission = "implStrategieSelectionEmission";
	private final String strImplStrategieSelectionElimination = "implStrategieSelectionElimination";
	private final String strImplStrategieSuccession = "implStrategieSuccession";
	private final String strTypeCritereArret = "typeCritereArret";
	private final String strObjectifCritereArret = "objectif";
	private final String strID = "id";
	private final String strParDefaut = "ParDefaut";

	private Element elemSysteme;
	
	private ConfigurationSysteme configSysteme;

	/**
	 * Lit la configuration du systeme depuis le fichier XML passe en parametre
	 * 
	 * @param nomFichier	le nom du fichier de configuration a lire
	 * @return				la configuration du systeme associee
	 * @throws ConfigurationException 	si le fichier passe en parametre n'est pas conforme
	 * @throws IOException				si une erreur survient lors de la lecture du fichier
	 */
	public ConfigurationSysteme lireConfigSystemeDepuisFichier(String nomFichier) throws ConfigurationException, IOException {
		File fichierConfiguration = new File(nomFichier);

		if (!fichierConfiguration.exists() || fichierConfiguration.isDirectory()) {
			throw new ConfigurationException(
				Objet.FICHIER_CONFIGURATION, Cause.ABSENT,
				"Fichier de configuration '" + nomFichier + "' introuvable" 
			);
		}
		
		SAXBuilder saxBuilder = new SAXBuilder();
		
		Document document = null;
		try {
			document = saxBuilder.build(fichierConfiguration);
		}
		catch (Exception exception) {
			throw new ConfigurationException(
				Objet.FICHIER_CONFIGURATION, Cause.INVALIDE_INTERVALLE,
				"Structure du fichier '" + nomFichier + "', fichier XML valide attendu"
			);
		}
		
		Element racineDocument = null;
		try {
			racineDocument = document.getRootElement();
		}
		catch (Exception exception) {
			throw new ConfigurationException(
				Objet.ELEMENT_CONFIG, Cause.ABSENT,
				messageErreurElemAbsent(strConfig)
			);
		}
		
		if (racineDocument == null || !racineDocument.getName().equals(strConfig)) {
			throw new ConfigurationException(
				Objet.ELEMENT_CONFIG, Cause.ABSENT,
				messageErreurElemAbsent(strConfig)
			);
		}
		if ((elemSysteme = racineDocument.getChild(strSysteme)) == null) {
			throw new ConfigurationException(
				Objet.ELEMENT_SYSTEME, Cause.ABSENT,
				messageErreurElemAbsent(strSysteme)
			);
		}

		// Lecture de la configuration du systeme (element 'systeme')
		int nombreIndividus = lireNombreIndividus();
		String strURLFichierMatriceVoisinage = lireURLFichierMatriceVoisinage();
		int tailleInitialeLexiqueParDefaut = lireTailleInitialeLexiqueParDefaut();
		int tailleMaximaleLexiqueParDefaut = lireTailleMaximaleLexiqueParDefaut();
		ImplementationCondition implConditionEmissionParDefaut = lireImplConditionEmissionParDefaut();
		ImplementationCondition implConditionReceptionParDefaut = lireImplConditionReceptionParDefaut();
		ImplementationCondition implConditionMemorisationParDefaut = lireImplConditionMemorisationParDefaut();;
		ImplementationStrategieSelection implStrategieSelectionEmissionParDefaut = lireImplStrategieSelectionEmissionParDefaut();
		ImplementationStrategieSelection implStrategieSelectionEliminationParDefaut = lireImplStrategieSelectionEliminationParDefaut(); 
		ImplentationStrategieSuccession implStrategieSuccessionParDefaut = lireImplStrategieSuccessionParDefaut();
		CritereArret critereArret = lireCritereArret();

		// Lecture des configurations individuelles (element(s) 'individu')
		List<Element> listeElementsIndividus = racineDocument.getChildren(strIndividu);
		ArrayList<Integer> listeIndividusIDs = new ArrayList<Integer>();
		ArrayList<Individu> individus = new ArrayList<Individu>();
		Individu individu;
		boolean individuParticulier = false;

		int tailleInitialeLexique, tailleMaximaleLexique;
		ImplementationCondition implConditionEmission;
		ImplementationCondition implConditionReception;
		ImplementationCondition implConditionMemorisation;
		ImplementationStrategieSelection implStrategieSelectionEmission;
		ImplementationStrategieSelection implStrategieSelectionElimination; 
		ImplentationStrategieSuccession implStrategieSuccession;
		
		for (int individuID = 1; individuID <= lireNombreIndividus(); individuID++) {
			individuParticulier = false;
			
			for (Element elementIndividu : listeElementsIndividus) {
				int elementIndividuID = lireID(elementIndividu);

				if (listeIndividusIDs.contains(elementIndividuID)) {
					throw new ConfigurationException(
						Objet.ATTRIBUT_ID, Cause.INVALIDE_UNIQUE,
						"Attribut '" + strID  + "' invalide : cet attribut doit etre unique et est deja existant"
					);
				}
				
				if (elementIndividuID == individuID) {
					individuParticulier = true;
					listeIndividusIDs.add(individuID);
					
					tailleInitialeLexique = lireTailleInitialeLexique(elementIndividu);
					tailleMaximaleLexique = lireTailleMaximaleLexique(elementIndividu);
					implConditionEmission = lireImplConditionEmission(elementIndividu);
					implConditionReception = lireImplConditionReception(elementIndividu);
					implConditionMemorisation = lireImplConditionMemorisation(elementIndividu);
					implStrategieSelectionEmission = lireImplStrategieSelectionEmission(elementIndividu);
					implStrategieSelectionElimination = lireImplStrategieSelectionElimination(elementIndividu);
					implStrategieSuccession = lireImplStrategieSuccession(elementIndividu);
					
					individu = new Individu(
						implConditionEmission, implConditionReception, implConditionMemorisation,
						implStrategieSelectionEmission, implStrategieSelectionElimination, implStrategieSuccession
					);
					individu.genererLexique(tailleInitialeLexique, tailleMaximaleLexique);	
					individus.add(individu);
				}
			}
			listeIndividusIDs.clear();
			if (!individuParticulier) {
				tailleInitialeLexique = tailleInitialeLexiqueParDefaut;
				tailleMaximaleLexique = tailleMaximaleLexiqueParDefaut;
				implConditionEmission = implConditionEmissionParDefaut;
				implConditionReception = implConditionReceptionParDefaut;
				implConditionMemorisation = implConditionMemorisationParDefaut;
				implStrategieSelectionEmission = implStrategieSelectionEmissionParDefaut;
				implStrategieSelectionElimination = implStrategieSelectionEliminationParDefaut;
				implStrategieSuccession = implStrategieSuccessionParDefaut;
				
				individu = new Individu(
					implConditionEmission, implConditionReception, implConditionMemorisation,
					implStrategieSelectionEmission, implStrategieSelectionElimination, implStrategieSuccession
				);
				individu.genererLexique(tailleInitialeLexique, tailleMaximaleLexique);	
				individus.add(individu);
			}
		}
		
		// Lecture de la matrice de voisinage et generation du voisinage
		int[][] matriceVoisinage = lireMatriceVoisinage(strURLFichierMatriceVoisinage);
		for (int indiceIndividu = 0; indiceIndividu < matriceVoisinage.length; indiceIndividu++) {
			for (int indiceVoisin = 0; indiceVoisin < matriceVoisinage.length; indiceVoisin++) {
				if (matriceVoisinage[indiceIndividu][indiceVoisin] > 0) {
					individus.get(indiceIndividu).ajouterVoisin(
						new Voisin(individus.get(indiceVoisin),
						Delais.depuisValeur((matriceVoisinage[indiceIndividu][indiceVoisin])))
					);
				}
			}
		}
		
		configSysteme = new ConfigurationSysteme(
			nombreIndividus, tailleInitialeLexiqueParDefaut, tailleMaximaleLexiqueParDefaut,
			implConditionEmissionParDefaut, implConditionReceptionParDefaut, implConditionMemorisationParDefaut,
			implStrategieSelectionEmissionParDefaut, implStrategieSelectionEliminationParDefaut, implStrategieSuccessionParDefaut,
			critereArret, individus, matriceVoisinage
		);
		
		return configSysteme;
	}

	/**
	 * Lit la matrice de voisinage contenue dans le fichier passe en parametre
	 * 
	 * @param urlFichierGraphe	l'URL du fichier a lire
	 * @return					la matrice de voisnage associee
	 * @throws ConfigurationException		si le fichier passe en parametre n'est pas conforme
	 * @throws IOException					si une erreur survient lors de la lecture du fichier
	 */
	public int[][] lireMatriceVoisinage(String urlFichierGraphe) throws ConfigurationException, IOException {
		int nombreIndividus = lireNombreIndividus();
		
		int[][] matriceVoisinage = new int[nombreIndividus][nombreIndividus];
		FileReader lecteurFichier = null;
		try {
			 lecteurFichier = new FileReader(urlFichierGraphe);
		}
		catch (FileNotFoundException exception) {
			throw new ConfigurationException(
				Objet.MATRICE_VOISINAGE, Cause.ABSENT,
				"Matrice de voisinage attendue dans le repertoire " + urlFichierGraphe
			);
		}

		BufferedReader lecteurATampon = new BufferedReader(lecteurFichier);
		
		try {
			String ligne = lecteurATampon.readLine();
			while (ligne.startsWith("#")) {
				ligne = lecteurATampon.readLine();
			}
			
			for (int indiceIndividu = 0; indiceIndividu < nombreIndividus; indiceIndividu++) {
				int sommeDelaisMemeIndividu = 0;
				String[] stringDelaisVoisinsMemeIndividu = ligne.split(" ");
				
				if (stringDelaisVoisinsMemeIndividu.length != nombreIndividus) {
					throw new ConfigurationException(
						Objet.MATRICE_VOISINAGE, Cause.INVALIDE_INTERVALLE,
						"Matrice de voisinage invalide : la matrice doit etre carree de taille '" + strNombreIndividus + "'"
					);
				}
				
				int valeurDelais;
				for (int indiceVoisin = 0; indiceVoisin < nombreIndividus; indiceVoisin++) {
					valeurDelais = Integer.parseUnsignedInt(stringDelaisVoisinsMemeIndividu[indiceVoisin]);
					matriceVoisinage[indiceIndividu][indiceVoisin] = valeurDelais;
					sommeDelaisMemeIndividu += valeurDelais;
				}

				if (sommeDelaisMemeIndividu < 1) {
					throw new ConfigurationException(
						Objet.MATRICE_VOISINAGE, Cause.INVALIDE_INTERVALLE,
						"Matrice de voisinage invalide : une ligne ne peut pas etre entierement nulle"
					);
				}
				if (matriceVoisinage[indiceIndividu][indiceIndividu] > 0) {
					throw new ConfigurationException(
						Objet.MATRICE_VOISINAGE, Cause.INVALIDE_INTERVALLE,
						"Matrice de voisinage invalide : la diagonale doit etre nulle"
					);
				}
				
				if ((ligne = lecteurATampon.readLine()) == null ) {
					if (indiceIndividu < nombreIndividus - 1) {
						throw new ConfigurationException(
							Objet.MATRICE_VOISINAGE, Cause.INVALIDE_INTERVALLE,
							"Matrice de voisinage invalide : la matrice doit etre carree de taille '" + strNombreIndividus + "'"
						);
					}
				}
			}
		}
		catch (NumberFormatException exception) {
			throw new ConfigurationException(
				Objet.MATRICE_VOISINAGE, Cause.INVALIDE_INTERVALLE,
				"Matrice de voisinage invalide : la matrice doit contenir exclusivement des unsigned int"
			);
		}
		finally {
			lecteurATampon.close();
			lecteurFichier.close();
		}

		return matriceVoisinage;
	}
	
	/**
	 * Lit la valeur de l'attribut urlFichierMatriceVoisinage de l'element systeme
	 * 
	 * @return							la valeur de l'attribut
	 * @throws ConfigurationException	si jamais l'attribut est absent
	 */
	public String lireURLFichierMatriceVoisinage() throws ConfigurationException {
		if (elemSysteme.getAttribute(strURLFichierMatriceVoisinage) != null) {
			return elemSysteme.getAttribute(strURLFichierMatriceVoisinage).getValue();
		}
		throw new ConfigurationException(
			Objet.ATTRIBUT_URL_FICHIER_MATRICE_VOISINAGE, Cause.ABSENT,
			messageErreurAttrAbsent(strURLFichierMatriceVoisinage)
		);
	}
	
	/**
	 * Lit la valeur de l'attribut nombreIndividus de l'element systeme
	 * 
	 * @return							la valeur de l'attribut
	 * @throws ConfigurationException	si jamais l'attribut est absent ou invalide
	 */
	public int lireNombreIndividus() throws ConfigurationException {
		int nombreIndividus, nombreMin = 2, nombreMax = 10;
		if (elemSysteme.getAttribute(strNombreIndividus) == null) {
			throw new ConfigurationException(
				Objet.ATTRIBUT_NOMBRE_INDIVIDUS, Cause.ABSENT,
				messageErreurElemAbsent(strNombreIndividus)
			);
		}
		try {
			nombreIndividus = elemSysteme.getAttribute(strNombreIndividus).getIntValue();
			if (nombreIndividus < nombreMin || nombreIndividus > nombreMax) {
				throw new ConfigurationException();
			}
		}
		catch (DataConversionException exception) {
			throw new ConfigurationException(
				Objet.ATTRIBUT_NOMBRE_INDIVIDUS, Cause.INVALIDE_INTERVALLE,
				messageErreurAttrAbsentOuInvalide(
						strNombreIndividus, String.valueOf(nombreMin), String.valueOf(nombreMax)
				)
			);
		}
		return nombreIndividus;
	}

	/**
	 * Lit la valeur de l'attribut tailleInitialeLexiqueParDefaut de l'element systeme
	 * 
	 * @return							la valeur de l'attribut
	 * @throws ConfigurationException	si jamais l'attribut est absent ou invalide
	 */
	public int lireTailleInitialeLexiqueParDefaut() throws ConfigurationException {
		int tailleInitialeLexique, tailleMin = 1, tailleMax = 20;
		try {
			tailleInitialeLexique = elemSysteme.getAttribute(strTailleInitialeLexique + strParDefaut).getIntValue();
		}
		catch (Exception exception) {
			throw new ConfigurationException(
				Objet.ATTRIBUT_TAILLE_INITIALE_LEXIQUE_PAR_DEFAUT, Cause.ABSENT,
				messageErreurAttrAbsent(
					strTailleInitialeLexique + strParDefaut
				)
			);
		}
		if (tailleInitialeLexique < 1 || tailleInitialeLexique > 20) {
			throw new ConfigurationException(
				Objet.ATTRIBUT_TAILLE_INITIALE_LEXIQUE_PAR_DEFAUT, Cause.INVALIDE_INTERVALLE,
				messageErreurAttrInvalide(
					strTailleInitialeLexique + strParDefaut, String.valueOf(tailleMin), String.valueOf(tailleMax)
				)
			);
		}
		return tailleInitialeLexique;
	}
	
	/**
	 * Lit la valeur de l'attribut tailleInitialeLexique de l'element passe en parametre
	 * ou la valeur de la configuration systeme si celui-ci est absent
	 * 
	 * @return							la valeur de l'attribut
	 * @throws ConfigurationException	si jamais l'attribut est invalide
	 */
	public int lireTailleInitialeLexique(Element element) throws ConfigurationException {
		int tailleInitialeLexique, tailleMax, tailleMin = 1;
		if (element.getAttribute(strTailleInitialeLexique) != null) {
			try {
				tailleInitialeLexique = element.getAttribute(strTailleInitialeLexique).getIntValue();
				tailleMax = lireTailleMaximaleLexiqueParDefaut();
				if (tailleInitialeLexique < tailleMin || tailleInitialeLexique > tailleMax) {
					throw new ConfigurationException();
				}
			}
			catch (Exception exception) {
				throw new ConfigurationException(
					Objet.ATTRIBUT_TAILLE_INITIALE_LEXIQUE, Cause.INVALIDE_INTERVALLE,
					messageErreurAttrInvalide(
							strTailleInitialeLexique, String.valueOf(tailleMin), strTailleMaximaleLexique + strParDefaut
					)
				);
			}
			return tailleInitialeLexique;
		}
		return lireTailleInitialeLexiqueParDefaut();
	}
	
	/**
	 * Lit la valeur de l'attribut tailleMaximaleLexiqueParDefaut de l'element systeme
	 * 
	 * @return							la valeur de l'attribut
	 * @throws ConfigurationException	si jamais l'attribut est absent ou invalide
	 */
	public int lireTailleMaximaleLexiqueParDefaut() throws ConfigurationException {
		int tailleMaximaleLexique, tailleMin, tailleMax = 20;
		try {
			tailleMaximaleLexique = elemSysteme.getAttribute(strTailleMaximaleLexique + strParDefaut).getIntValue();
			tailleMin = lireTailleInitialeLexiqueParDefaut();
		}
		catch (Exception exception) {
			throw new ConfigurationException(
				Objet.ATTRIBUT_TAILLE_MAXIMALE_LEXIQUE_PAR_DEFAUT, Cause.ABSENT,
				messageErreurAttrAbsent(
						strTailleMaximaleLexique + strParDefaut
				)
			);
		}
		if (tailleMaximaleLexique < tailleMin || tailleMaximaleLexique > tailleMax) {
			throw new ConfigurationException(
				Objet.ATTRIBUT_TAILLE_MAXIMALE_LEXIQUE_PAR_DEFAUT, Cause.INVALIDE_INTERVALLE,
				messageErreurAttrInvalide(
						strTailleMaximaleLexique + strParDefaut, strTailleInitialeLexique + strParDefaut, String.valueOf(tailleMax)
				)
			);
		}
		return tailleMaximaleLexique;
	}
	
	/**
	 * Lit la valeur de l'attribut tailleMaximaleLexique de l'element passe en parametre 
	 * ou la valeur de la configuration systeme si celui-ci est absent
	 * 
	 * @return							la valeur de l'attribut
	 * @throws ConfigurationException	si jamais l'attribut est invalide
	 */
	public int lireTailleMaximaleLexique(Element element) throws ConfigurationException {
		int tailleMaximaleLexique, tailleMin, tailleMax = 20;
		if (element.getAttribute(strTailleMaximaleLexique) != null) {
			try {
				try {
					tailleMin = lireTailleInitialeLexique(element);
				}
				catch (Exception exception) {
					tailleMin = lireTailleInitialeLexiqueParDefaut();
				}
				tailleMaximaleLexique = element.getAttribute(strTailleMaximaleLexique).getIntValue();
				if (tailleMaximaleLexique < tailleMin || tailleMaximaleLexique > tailleMax) {
					throw new ConfigurationException();
				}
			}
			catch (Exception exception) {
				throw new ConfigurationException(
					Objet.ATTRIBUT_TAILLE_MAXIMALE_LEXIQUE, Cause.INVALIDE_INTERVALLE,
					messageErreurAttrInvalide(
						strTailleMaximaleLexique, strTailleInitialeLexique + "(" + strParDefaut + ")", String.valueOf(tailleMax)
					)
				);
			}
			return tailleMaximaleLexique;
		}
		return lireTailleMaximaleLexiqueParDefaut();
	}
	
	/**
	 * Lit la valeur de l'element implConditionEmissionParDefaut de l'element systeme
	 * 
	 * @return							la valeur de l'element
	 * @throws ConfigurationException	si jamais l'element est absent ou invalide
	 */
	public ImplementationCondition lireImplConditionEmissionParDefaut() throws ConfigurationException {
		if (elemSysteme.getChild(strImplConditionEmission + strParDefaut) == null) {
			throw new ConfigurationException(
				Objet.ELEMENT_IMPL_CONDITION_EMISSION_PAR_DEFAUT, Cause.ABSENT,
				messageErreurElemAbsent(
					strImplConditionEmission + strParDefaut
				)
			);
		}
		try {
			return ImplementationCondition.valueOf(elemSysteme.getChild(strImplConditionEmission + strParDefaut).getValue());
		}
		catch (Exception exception) {
			throw new ConfigurationException(
				Objet.ELEMENT_IMPL_CONDITION_EMISSION_PAR_DEFAUT, Cause.INVALIDE_ENUM,
				messageErreurElemInvalide(
					strImplConditionEmission + strParDefaut, ImplementationCondition.class.getSimpleName()
				)
			);
		}
	}
	
	/**
	 * Lit la valeur de l'element implConditionEmission de l'element passe en parametre
	 * ou la valeur de la configuration systeme si celui-ci est absent
	 * 
	 * @return							la valeur de l'element
	 * @throws ConfigurationException	si jamais l'element invalide
	 */
	public ImplementationCondition lireImplConditionEmission(Element element) throws ConfigurationException {
		if (element.getChild(strImplConditionEmission) != null) {
			try {
				return ImplementationCondition.valueOf(element.getChild(strImplConditionEmission).getValue());
			}
			catch (Exception exception) {
				throw new ConfigurationException(
					Objet.ELEMENT_IMPL_CONDITION_EMISSION, Cause.INVALIDE_ENUM,
					messageErreurElemInvalide(
						strImplConditionEmission, ImplementationCondition.class.getSimpleName()
					)
				);
			}
		}
		return lireImplConditionEmissionParDefaut();
	}

	/**
	 * Lit la valeur de l'element implConditionReceptionParDefaut de l'element systeme
	 * 
	 * @return							la valeur de l'element
	 * @throws ConfigurationException	si jamais l'element est absent ou invalide
	 */
	public ImplementationCondition lireImplConditionReceptionParDefaut() throws ConfigurationException {
		if (elemSysteme.getChild(strImplConditionReception + strParDefaut) == null) {
			throw new ConfigurationException(
				Objet.ELEMENT_IMPL_CONDITION_RECEPTION_PAR_DEFAUT, Cause.ABSENT,
				messageErreurElemAbsent(
					strImplConditionReception + strParDefaut
				)
			);
		}
		try {
			return ImplementationCondition.valueOf(elemSysteme.getChild(strImplConditionReception + strParDefaut).getValue());
		}
		catch (Exception exception) {
			throw new ConfigurationException(
				Objet.ELEMENT_IMPL_CONDITION_RECEPTION_PAR_DEFAUT, Cause.INVALIDE_ENUM,
				messageErreurElemInvalide(
					strImplConditionReception + strParDefaut, ImplementationCondition.class.getSimpleName()
				)
			);
		}
	}
	
	/**
	 * Lit la valeur de l'element implConditionReception de l'element passe en parametre
	 * ou la valeur de la configuration systeme si celui-ci est absent
	 * 
	 * @return							la valeur de l'element
	 * @throws ConfigurationException	si jamais l'element est invalide
	 */
	public ImplementationCondition lireImplConditionReception(Element element) throws ConfigurationException {
		if (element.getChild(strImplConditionReception) != null) {
			try {
				return ImplementationCondition.valueOf(element.getChild(strImplConditionReception).getValue());
			}
			catch (Exception exception) {
				throw new ConfigurationException(
					Objet.ELEMENT_IMPL_CONDITION_RECEPTION, Cause.INVALIDE_ENUM,
					messageErreurElemInvalide(
						strImplConditionReception, ImplementationCondition.class.getSimpleName()
					)
				);
			}
		}
		return lireImplConditionReceptionParDefaut();
	}
	
	/**
	 * Lit la valeur de l'element implConditionMemorisationParDefaut de l'element systeme
	 * 
	 * @return							la valeur de l'element
	 * @throws ConfigurationException	si jamais l'element est absent ou invalide
	 */
	public ImplementationCondition lireImplConditionMemorisationParDefaut() throws ConfigurationException {
		if (elemSysteme.getChild(strImplConditionMemorisation + strParDefaut) == null) {
			throw new ConfigurationException(
				Objet.ELEMENT_IMPL_CONDITION_MEMORISATION_PAR_DEFAUT, Cause.ABSENT,
				messageErreurElemAbsent(
					strImplConditionMemorisation + strParDefaut
				)
			);
		}
		try {
			return ImplementationCondition.valueOf(elemSysteme.getChild(strImplConditionMemorisation + strParDefaut).getValue());
		}
		catch (Exception exception) {
			throw new ConfigurationException(
				Objet.ELEMENT_IMPL_CONDITION_MEMORISATION_PAR_DEFAUT, Cause.INVALIDE_ENUM,
				messageErreurElemInvalide(
					strImplConditionMemorisation + strParDefaut, ImplementationCondition.class.getSimpleName()
				)
			);
		}
	}
	
	/**
	 * Lit la valeur de l'element implConditionMemorisation de l'element passe en parametre
	 * ou la valeur de la configuration systeme si celui-ci est absent
	 * 
	 * @return							la valeur de l'element
	 * @throws ConfigurationException	si jamais l'element est invalide
	 */
	public ImplementationCondition lireImplConditionMemorisation(Element element) throws ConfigurationException {
		if (element.getChild(strImplConditionMemorisation) != null) {
			try {
				return ImplementationCondition.valueOf(element.getChild(strImplConditionMemorisation).getValue());
			}
			catch (Exception exception) {
				throw new ConfigurationException(
					Objet.ELEMENT_IMPL_CONDITION_MEMORISATION, Cause.INVALIDE_ENUM,
					messageErreurElemInvalide(
						strImplConditionMemorisation, ImplementationCondition.class.getSimpleName()
					)
				);
			}
		}
		return lireImplConditionMemorisationParDefaut();
	}
	
	/**
	 * Lit la valeur de l'element implStrategieSelectionEmissionParDefaut de l'element systeme
	 * 
	 * @return							la valeur de l'element
	 * @throws ConfigurationException	si jamais l'element est absent ou invalide
	 */
	public ImplementationStrategieSelection lireImplStrategieSelectionEmissionParDefaut() throws ConfigurationException {
		if (elemSysteme.getChild(strImplStrategieSelectionEmission + strParDefaut) == null) {
			throw new ConfigurationException(
				Objet.ELEMENT_IMPL_STRATEGIE_SELECTION_EMISSION_PAR_DEFAUT, Cause.ABSENT,
				messageErreurElemAbsent(
					strImplStrategieSelectionEmission + strParDefaut
				)
			);
		}
		try {
			return ImplementationStrategieSelection.valueOf(elemSysteme.getChild(strImplStrategieSelectionEmission + strParDefaut).getValue());
		}
		catch (Exception exception) {
			throw new ConfigurationException(
				Objet.ELEMENT_IMPL_STRATEGIE_SELECTION_EMISSION_PAR_DEFAUT, Cause.INVALIDE_ENUM,
				messageErreurElemAbsentOuInvalide(
					strImplStrategieSelectionEmission + strParDefaut, ImplementationStrategieSelection.class.getSimpleName()
				)
			);
		}
	}
	
	/**
	 * Lit la valeur de l'element implStrategieSelectionEmission de l'element passe en parametre
	 * ou la valeur de la configuration systeme si celui-ci est absent
	 * 
	 * @return							la valeur de l'element
	 * @throws ConfigurationException	si jamais l'element est invalide
	 */
	public ImplementationStrategieSelection lireImplStrategieSelectionEmission(Element element) throws ConfigurationException {
		if (element.getChild(strImplStrategieSelectionEmission) != null) {
			try {	
				return ImplementationStrategieSelection.valueOf(element.getChild(strImplStrategieSelectionEmission).getValue());
			}
			catch (Exception exception) {
				throw new ConfigurationException(
					Objet.ELEMENT_IMPL_STRATEGIE_SELECTION_EMISSION, Cause.INVALIDE_ENUM,
					messageErreurElemInvalide(
						strImplStrategieSelectionEmission, ImplementationStrategieSelection.class.getSimpleName()
					)
				);
			}
		}
		return lireImplStrategieSelectionEmissionParDefaut();
	}
	
	/**
	 * Lit la valeur de l'element implStrategieSelectionEliminationParDefaut de l'element systeme
	 * 
	 * @return							la valeur de l'element
	 * @throws ConfigurationException	si jamais l'element est absent ou invalide
	 */
	public ImplementationStrategieSelection lireImplStrategieSelectionEliminationParDefaut() throws ConfigurationException {
		if (elemSysteme.getChild(strImplStrategieSelectionElimination + strParDefaut) == null) {
			throw new ConfigurationException(
				Objet.ELEMENT_IMPL_STRATEGIE_SELECTION_ELIMINATION_PAR_DEFAUT, Cause.INVALIDE_ENUM,
				messageErreurElemAbsent(
					strImplStrategieSelectionElimination + strParDefaut
				)
			);
		}
		try {
			return ImplementationStrategieSelection.valueOf(elemSysteme.getChild(strImplStrategieSelectionElimination + strParDefaut).getValue());
		}
		catch (Exception exception) {
			throw new ConfigurationException(
				Objet.ELEMENT_IMPL_STRATEGIE_SELECTION_ELIMINATION_PAR_DEFAUT, Cause.INVALIDE_ENUM,
				messageErreurElemAbsentOuInvalide(
					strImplStrategieSelectionElimination + strParDefaut, ImplementationStrategieSelection.class.getSimpleName()
				)
			);
		}
	}
	
	/**
	 * Lit la valeur de l'element implStrategieSelectionElimination de l'element passe en parametre
	 * ou la valeur de la configuration systeme si celui-ci est absent
	 * 
	 * @return							la valeur de l'element
	 * @throws ConfigurationException	si jamais l'element est invalide
	 */
	public ImplementationStrategieSelection lireImplStrategieSelectionElimination(Element element) throws ConfigurationException {
		if (element.getChild(strImplStrategieSelectionElimination) != null) {
			try {
				return ImplementationStrategieSelection.valueOf(element.getChild(strImplStrategieSelectionElimination).getValue());
			}
			catch (Exception exception) {
				throw new ConfigurationException(
					Objet.ELEMENT_IMPL_STRATEGIE_SELECTION_ELIMINATION, Cause.INVALIDE_ENUM,
					messageErreurElemInvalide(
						strImplStrategieSelectionElimination, ImplementationStrategieSelection.class.getSimpleName()
					)
				);
			}
		}
		return lireImplStrategieSelectionEliminationParDefaut();
	}
	
	/**
	 * Lit la valeur de l'element implStrategieSuccessionParDefaut de l'element systeme
	 * 
	 * @return							la valeur de l'element
	 * @throws ConfigurationException	si jamais l'element est absent ou invalide
	 */
	public ImplentationStrategieSuccession lireImplStrategieSuccessionParDefaut() throws ConfigurationException {
		if (elemSysteme.getChild(strImplStrategieSuccession + strParDefaut) == null) {
			throw new ConfigurationException(
				Objet.ELEMENT_IMPL_STRATEGIE_SUCCESSION_PAR_DEFAUT, Cause.ABSENT,
				messageErreurElemAbsent(
					strImplStrategieSuccession + strParDefaut
				)
			);
		}
		try {
			return ImplentationStrategieSuccession.valueOf(elemSysteme.getChild(strImplStrategieSuccession + strParDefaut).getValue());
		}
		catch (Exception exception) {
			throw new ConfigurationException(
				Objet.ELEMENT_IMPL_STRATEGIE_SUCCESSION_PAR_DEFAUT, Cause.INVALIDE_ENUM,
				messageErreurElemInvalide(
					strImplStrategieSuccession + strParDefaut, ImplentationStrategieSuccession.class.getSimpleName()
				)
			);
		}
	}
	
	/**
	 * Lit la valeur de l'element implStrategieSuccession de l'element passe en parametre
	 * ou la valeur de la configuration systeme si celui-ci est absent
	 * 
	 * @return							la valeur de l'element
	 * @throws ConfigurationException	si jamais l'element est invalide
	 */
	public ImplentationStrategieSuccession lireImplStrategieSuccession(Element element) throws ConfigurationException {
		if (element.getChild(strImplStrategieSuccession) != null) {
			try {
				return ImplentationStrategieSuccession.valueOf(element.getChild(strImplStrategieSuccession).getValue());
			}
			catch (Exception exception) {
				throw new ConfigurationException(
					Objet.ELEMENT_IMPL_STRATEGIE_SUCCESSION, Cause.INVALIDE_ENUM,
					messageErreurElemInvalide(
						strImplStrategieSuccession, ImplentationStrategieSuccession.class.getSimpleName()
					)
				);
			}
		}
		return lireImplStrategieSuccessionParDefaut();
	}

	/**
	 * Lit le contenu de l'element critereArret de l'element systeme
	 * 
	 * @return							le critere d'arret lu
	 * @throws ConfigurationException	si jamais l'attribut est absent ou invalide
	 */
	public CritereArret lireCritereArret() throws ConfigurationException {
		TypeCritereArret type; int objectif = 0, minObjectif = 1, maxObjectif = 1000;
		Element elemCritereArret;
		if (elemSysteme.getChild(strTypeCritereArret) == null) {
			throw new ConfigurationException(
				Objet.ELEMENT_TYPE_CRITERE_ARRET, Cause.ABSENT,
				messageErreurElemAbsent(
						strTypeCritereArret
				)
			);
		}
		try {
			elemCritereArret = elemSysteme.getChild(strTypeCritereArret);
			type = TypeCritereArret.valueOf(elemCritereArret.getValue());
		}
		catch (Exception exception) {
			throw new ConfigurationException(
				Objet.ELEMENT_TYPE_CRITERE_ARRET, Cause.INVALIDE_ENUM,
				messageErreurElemInvalide(
					strTypeCritereArret, TypeCritereArret.class.getSimpleName()
				)
			);
		}
		if (type.estAObjectif()) {
			if (elemCritereArret.getAttribute(strObjectifCritereArret) == null) {
				throw new ConfigurationException(
					Objet.ATTRIBUT_OBJECTIF_CRITERE_ARRET, Cause.ABSENT,
					messageErreurAttrAbsent(
						strObjectifCritereArret
					)
				);
			}
			try {
				objectif = elemCritereArret.getAttribute(strObjectifCritereArret).getIntValue();
				if (objectif < minObjectif || objectif > maxObjectif) {
					throw new ConfigurationException();
				}
			}
			catch (Exception exception) {
				throw new ConfigurationException(
				Objet.ATTRIBUT_OBJECTIF_CRITERE_ARRET, Cause.INVALIDE_INTERVALLE,
					messageErreurAttrInvalide(
						strObjectifCritereArret, String.valueOf(minObjectif), String.valueOf(maxObjectif)
					)
				);
			}
		}
		return new CritereArret(type, objectif);
	}
	
	/**
	 * Lit la valeur de l'attribut id de l'element passe en parametre 
	 * 
	 * @return							la valeur de l'attribut
	 * @throws ConfigurationException	si jamais l'attribut est invalide
	 */
	public int lireID(Element element) throws ConfigurationException {
		int ID, minID = 1, maxID = lireNombreIndividus();
		if (element.getAttribute(strID) == null) {
			throw new ConfigurationException(
				Objet.ATTRIBUT_ID, Cause.ABSENT,
				messageErreurAttrAbsent(
					strID
				)
			);
		}
		try {
			ID = element.getAttribute(strID).getIntValue();
			if (ID < minID || ID > maxID) {
				throw new ConfigurationException();
			}
			return ID;
		}
		catch (Exception exception) {
			throw new ConfigurationException(
				Objet.ATTRIBUT_ID, Cause.INVALIDE_INTERVALLE,
				messageErreurAttrInvalide(
					strID, String.valueOf(minID), strNombreIndividus
				)
			);
		}
	}
	
	/**
	 * Compose un message d'erreur indiquant l'absence de l'element de nom passe en parametre
	 * 
	 * @param nomElement	le nom de l'element absent
	 * @return				le message d'erreur compose
	 */
	public String messageErreurElemAbsent(String nomElement) {
		return "Element '" + nomElement + "' absent";
	}
	
	/**
	 * Compose un message d'erreur indiquant l'invalidite de l'element de nom passe en parametre
	 * 
	 * @param nomElement	le nom de l'element invalide
	 * @return				le message d'erreur compose
	 */
	public String messageErreurElemInvalide(String nomElement, String typeElement) {
		return "Element '" + nomElement + "' invalide : " + typeElement + " attendu";
	}
	
	/**
	 * Compose un message d'erreur indiquant l'absence ou l'invalidite de l'element de nom passe en parametre
	 * 
	 * @param nomElement	le nom de l'element absent ou invalide
	 * @return				le message d'erreur compose
	 */
	public String messageErreurElemAbsentOuInvalide(String nomElement, String typeElement) {
		return "Element '" + nomElement + "' absent ou invalide : " + typeElement + " attendu";
	}
	
	/**
	 * Compose un message d'erreur indiquant l'absence de l'attribut de nom passe en parametre
	 * 
	 * @param nomElement	le nom de l'attribut absent
	 * @return				le message d'erreur compose
	 */
	public String messageErreurAttrAbsent(String nomAttribut) {
		return "Attribut '" + nomAttribut + "' absent";
	}
	
	/**
	 * Compose un message d'erreur indiquant l'invalidite de l'attribut de nom passe en parametre
	 * 
	 * @param nomElement	le nom de l'attribut invalide
	 * @return				le message d'erreur compose
	 */
	public String messageErreurAttrInvalide(String nomAttribut, String valeurMin, String valeurMax) {
		return "Attribut '" + nomAttribut + "' invalide : int dans [" + valeurMin + ", " + valeurMax + "] attendu";
	}
	
	/**
	 * Compose un message d'erreur indiquant l'absence ou l'invalidite de l'attribut de nom passe en parametre
	 * 
	 * @param nomElement	le nom de l'attribut absent ou invalide
	 * @return				le message d'erreur compose
	 */
	public String messageErreurAttrAbsentOuInvalide(String nomAttribut, String valeurMin, String valeurMax) {
		return "Attribut '" + nomAttribut + "' absent ou invalide : int dans [" + valeurMin + ", " + valeurMax + "] attendu";
	}
}
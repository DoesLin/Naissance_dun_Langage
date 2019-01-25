import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import simulation.systeme.exception.ConfigurationException;
import simulation.systeme.exception.ConfigurationException.Cause;
import simulation.systeme.exception.ConfigurationException.Objet;
import simulation.systeme.lecture.LecteurConfigurationSysteme;

/**
 * Fichier de tests JUnit verifiant que la classe LecteurConfigurationSysteme 
 * detecte bien une configuration anormale du systeme et les bonnes exceptions
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
@RunWith(Parameterized.class)
public class LecteurConfigurationSystemeTest {

	/**
	 * Genere une liste de parametres pour les tests, de la forme :
	 * 		[urlFichierConfig, objetException, causeException]
	 * 
	 * @return	les parametres pour les tests
	 */
    @Parameters(name = "{0} | {1} | {2}")
    public static List<Object[]> obtenirParametres() {
        return Arrays.asList(
        	new Object[][] {
        		{"test/config/config_1.xml", "FICHIER_CONFIGURATION", "ABSENT"},
        		{"test/config/config_2.xml", "ATTRIBUT_NOMBRE_INDIVIDUS", "ABSENT"},
        		{"test/config/config_3.xml", "ELEMENT_IMPL_CONDITION_EMISSION_PAR_DEFAUT", "INVALIDE_ENUM"},
        		{"test/config/config_4.xml", "ATTRIBUT_TAILLE_INITIALE_LEXIQUE", "INVALIDE_INTERVALLE"},
        		{"test/config/config_5.xml", "ATTRIBUT_ID", "INVALIDE_INTERVALLE"},
        		{"test/config/config_6.xml", "ELEMENT_IMPL_STRATEGIE_SUCCESSION_PAR_DEFAUT", "ABSENT"},
        		{"test/config/config_7.xml", "ATTRIBUT_ID", "INVALIDE_UNIQUE"}
        	}
        );
    }
    
    private String urlFichierConfig;
    private Objet objetException;
    private Cause causeException;
    
    private final LecteurConfigurationSysteme lecteurConfigSysteme = new LecteurConfigurationSysteme();
    
    /**
     * Applique les parametres donnees pour le test courant
     * 
     * @param urlFichierConfig	url du fichier de configuration
     * @param objetException	objet de l'exception attendue
     * @param causeException	cause de l'exception attendue
     */
    public LecteurConfigurationSystemeTest(String urlFichierConfig, String objetException, String causeException) {
       this.urlFichierConfig = urlFichierConfig;
       this.objetException = Objet.valueOf(Objet.class, objetException);
       this.causeException = Cause.valueOf(Cause.class, causeException);
    }
    
    /**
     * Teste la configuration donnee en parametre et verifie qu'elle provoque bien l'exception 
	 * d'objet et de cause donnees
     */
	@Test
	public void lireConfigSystemeTest() {
		try {
			lecteurConfigSysteme.lireConfigSystemeDepuisFichier(urlFichierConfig);
			Assert.fail("Une exception est attendue");
		}
		catch (ConfigurationException exception) {
			if (exception.lireObjet() == objetException && exception.lireCause() == causeException) {
				Assert.assertTrue(true);
			}
			else {
				Assert.fail("Mauvaise exception levee");
			}
		}
		catch (IOException exception) {
			Assert.fail("Une exception inatendue a ete levee, reessayer");
		}
	}
}

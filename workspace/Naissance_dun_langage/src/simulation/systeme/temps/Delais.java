package simulation.systeme.temps;

/**
 * Classe representant un delais a partir d'une valeur numerique
 * 
 * L'interet de cette classe est de dissocier les notions temporelles de la 
 * simulation du reste du modele
 * 
 * L'unite de la valeur d'un delais est equivalent a l'unite de la valeur d'une date,
 * i.e. :
 * 		Date.depuisValeur(1).plusDelais(Delais.depuisValeur(1)).equals(Date.depuisValeur(2)) == true
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class Delais {
	private int valeur;

	private Delais(int valeur) {
		this.valeur = valeur;
	}
	
	/**
	 * Renvoie la valeur numerique du delais
	 * 
	 * @return la valeur numerique du delais
	 */
	public int lireValeur() {
		return valeur;
	}

	/**
	 * Cree un delais a partir de la valeur numerique donnee
	 * 
	 * @param valeur	la valeur numerique du delais
	 */
	public static Delais depuisValeur(int valeur) {
		return new Delais(valeur);
	}
	
	@Override
	public String toString() {
		return String.valueOf(valeur);
	}
}

package simulation.systeme.temps;

/**
 * Classe representant une date a partir d'une valeur numerique
 * 
 * L'interet de cette classe est de dissocier les notions temporelles de la 
 * simulation du reste du modele
 * 
 * L'unite de la valeur d'une date est equivalent a l'unite de la valeur d'un delais,
 * i.e. :
 * 		Date.depuisValeur(1).plusDelais(Delais.depuisValeur(1)).equals(Date.depuisValeur(2)) == true
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class Date implements Comparable<Date> {
	public static final Date valeurInitiale = new Date(0);
	
	private int valeur;

	private Date(int valeur) {
		this.valeur = valeur;
	}
	
	/**
	 * Renvoie la valeur numerique de la date
	 * 
	 * @return la valeur numerique de la date
	 */
	public int lireValeur() {
		return valeur;
	}
	
	/**
	 * Cree une date a partir de la valeur numerique donnee
	 * 
	 * @param valeur	la valeur numerique de la date
	 * @return			la date associee a la valeur numerique donnee
	 */
	public static Date depuisValeur(int valeur) {
		return new Date(valeur);
	}
	
	/**
	 * Renvoie la date suivant celle passee en parametre, i.e. la date
	 * dont la valeur est incrementee de 1
	 * 
	 * @param date	la date dont on veut la date suivante 
	 * @return		la date suivant celle passee en parametre
	 */
	public static Date dateSuivante(Date date) {
		return new Date(date.valeur + 1);
	}
	
	/**
	 * Renvoie la date courante plus le delais passe en parametre
	 * 
	 * @param delais	le delais a ajouter a la date courante
	 * @return la date courante plus le delais passe en parametre
	 */
	public Date plusDelais(Delais delais) {
		return new Date(valeur + delais.lireValeur());
	}
	
	/**
	 * Renvoie la date correspondante a la chaine de caractere passee en parametre
	 * 
	 * @param chaineDate	la chaine de caractere representant la date
	 * @return				la date correspondante a la chaine de caractere passee en parametre
	 * @throws NumberFormatException	si la chaine de caractere ne represente pas un entier naturel
	 */
	public static Date depuisChaine(String chaineDate) throws NumberFormatException {
		int valeurDate = Integer.parseInt(chaineDate);
		
		return new Date(valeurDate);
	}

	/**
	 * Renvoie true si la date actuelle est apres la date passee en parametre, false sinon
	 * 
	 * @param date	la date a comparer a la date actuelle
	 * @return 		true si la date actuelle est apres la date passee en parametre, false sinon
	 */
	public boolean estApres(Date date) {
		return valeur >= date.lireValeur();
	}
	
	/**
	 * Renvoie true si la date actuelle est avant la date passee en parametre, false sinon
	 * 
	 * @param date	la date a comparer a la date actuelle
	 * @return 		true si la date actuelle est avant la date passee en parametre, false sinon
	 */
	public boolean estAvant(Date date) {
		return valeur <= date.lireValeur();
	}
	
	@Override
	public String toString() {
		return String.valueOf(valeur);
	}

	@Override
	public boolean equals(Object objet){
		if (objet == null)
			return false;
		if (objet == this)
			return true;
		if (!(objet instanceof Date))
			return false;
		
		Date date = (Date)objet;
		return lireValeur() == date.lireValeur();
	}

	@Override
	public int compareTo(Date date) {
		return lireValeur() - date.lireValeur();
	}
}

package simulation.systeme.enumeration;

/**
 * Enumeration des differents types de critere d'arret
 * 
 * Un critere d'arret necessitant une valeur d'objectif (typiquement, un nombre d'evenements declenches)
 * doit etre precise dans la methode estAObjectif() 
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public enum TypeCritereArret {
	EVENEMENTS_DECLENCHES,
	DATE_ATTEINTE,
	PREMIER_LEXIQUE_PLEIN,
	TOUS_LEXIQUES_PLEINS;
	
	/**
	 * Renvoie true si le type de critere d'arret necessite une valeur d'objectif, sinon false
	 * 
	 * @return true si le type de critere d'arret necessite une valeur d'objectif, sinon false
	 */
	public boolean estAObjectif() {
		switch (this) {
			case EVENEMENTS_DECLENCHES:
			case DATE_ATTEINTE:
				return true;
			default :
				return false;
		}
	}
}
package simulation.systeme;

import simulation.systeme.enumeration.TypeCritereArret;

/**
 * Classe representant un critere d'arret pour la simulation
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class CritereArret {
	private TypeCritereArret type;
	private int objectif;

	/**
	 * Cree un critere d'arret depuis son type et son objectif
	 * 
	 * @param type			type du critere d'arret
	 * @param objectif		l'objectif du critere d'arret (typiquement, la valeur numerique a atteindre selon le type donne)
	 */
	public CritereArret(TypeCritereArret type, int objectif) {
		this.type = type;
		this.objectif = objectif;
	}

	/**
	 * Renvoie le type du critere d'arret
	 * 
	 * @return le type du critere d'arret
	 */
	public TypeCritereArret lireTypeCritere() {
		return type;
	}
	
	/**
	 * Renvoie l'objectif du critere d'arret
	 * 
	 * @return l'objectif du critere d'arret
	 */
	public int lireObjectif() {
		return objectif;
	}
}

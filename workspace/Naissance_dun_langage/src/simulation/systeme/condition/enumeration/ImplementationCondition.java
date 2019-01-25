package simulation.systeme.condition.enumeration;

/**
 * Enumeration des differentes implementations de conditions disponibles pour la simulation
 * Chaque valeur doit correspondre a exactement une implementation de la classe abstraite Condition
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public enum ImplementationCondition {
	CONDITION_PROBABILITE_UNIFORME,
	CONDITION_TOUJOURS_SATISFAITE,
	CONDITION_JAMAIS_SATISFAITE;
}
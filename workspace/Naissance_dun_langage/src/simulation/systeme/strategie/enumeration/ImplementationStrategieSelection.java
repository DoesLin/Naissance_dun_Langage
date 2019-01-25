package simulation.systeme.strategie.enumeration;

/**
 * Enumeration des differentes implementations de strategies de selection disponibles pour la simulation
 * Chaque valeur doit correspondre a exactement une implementation de la classe abstraite StrategieSelection
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public enum ImplementationStrategieSelection {
	SELECTION_PREMIER_LEMME,
	SELECTION_LEMME_ALEATOIRE,
	SELECTION_LEMME_MOINS_EMIS;
}

package simulation.systeme.lexique;

import java.util.Comparator;

/**
 * Comparateur de la classe OccurrenceLemme selon l'attribut ID
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class ComparateurOccurrenceLemmeID implements Comparator<OccurrenceLemme> {

	@Override
    public int compare(OccurrenceLemme occurrence1, OccurrenceLemme occurrence2) {
        return Integer.compare(occurrence1.getID(), occurrence2.getID());
    }
}
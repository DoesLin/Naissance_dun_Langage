package simulation.ihm.element;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * Une classe representant une fleche en JavaFX, trouvee sur GitHub
 * 
 * https://gist.github.com/kn0412/2086581e98a32c8dfa1f69772f14bca4
 * 
 * @author kn0412 (presume)
 *
 */
public class Arrow extends Path {
    private static final double defaultArrowHeadSize = 8d;

    /**
     * Cree une fleche de coordonnees initiales (startX, startY) et de coordonnees finales (endX, endY)
     * 
     * @param startX			coordonnee initiale X de la fleche
     * @param startY			coordonnee initiale Y de la fleche
     * @param endX				coordonnee finale X de la fleche
     * @param endY				coordonnee finale Y de la fleche
     */
    public Arrow(double startX, double startY, double endX, double endY) {
        super();

        strokeProperty().bind(fillProperty());
        setFill(Color.GRAY);

        getElements().add(new MoveTo(startX, startY));
        getElements().add(new LineTo(endX, endY));

        double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * defaultArrowHeadSize + endX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * defaultArrowHeadSize + endY;

        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * defaultArrowHeadSize + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * defaultArrowHeadSize + endY;
        
        getElements().add(new LineTo(x1, y1));
        getElements().add(new LineTo(x2, y2));
        getElements().add(new LineTo(endX, endY));
        
        getStrokeDashArray().addAll(1d, 8d);
    }
}
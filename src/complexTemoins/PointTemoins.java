package complexTemoins;

import Jcg.geometry.Point_2;
import Jcg.geometry.Vector_2;

/**
 * En fait je veux bien créer une classe pour les points qu'on va utiliser pour
 * notre pb, parce que cette nouvelle structure va contenir, pour chaque point,
 * adresses de deux points de P qui sont les plus proches, ça sert au fonctionnement
 * de l'algo complexe de témoins.
 * @author Khanh
 *
 */

public class PointTemoins extends Point_2 {
	/**
	 * Le premier point dans P le plus proche.
	 */
    public PointTemoins first;
    
    /**
     * Le deuxième point dans P le plus proche (sauf first).
     */
    public PointTemoins second;
    
    /**
     * La distance à l'ensemble P.
     */
    double distanceToP;
    
    /**
     * Simple construction.
     */
    public PointTemoins() {
    	super();
    	first=null;
    	second=null;
    	distanceToP=-1;
    }
    
    /**
     * Construction à partie d'un point 2D. On fait simplement la copie.
     * @param p Point 2D.
     */
    public PointTemoins (Point_2 p) {
    	super(p);
    	first=null;
    	second=null;
    	distanceToP=-1;
    }
    
    /**
     * Fonction géo pour mesurer la distance à un autre point.
     * @param q Point auquel on veut calculer la distance.
     * @return Distance en double entre le point au courant et q.
     */
    public double distanceTo (PointTemoins q) {
    	Vector_2 vector=new Vector_2(this, q);
    	return Math.sqrt((Double)vector.squaredLength());
    }
}

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
    private PointTemoins first;
    
    /**
     * Le deuxième point dans P le plus proche (sauf first).
     */
    private PointTemoins second;
    
    /**
     * La distance à l'ensemble P.
     */
    private double distanceToP;
    private double secondDistanceToP;
    private int index;
    
    /**
     * Simple construction.
     */
    public PointTemoins(int index) {
    	super();
    	first=null;
    	second=null;
    	distanceToP=-1;
    	this.index=index;
    }
    
    /**
     * Construction à partie d'un point 2D. On fait simplement la copie.
     * @param p Point 2D.
     */
    public PointTemoins (Point_2 p, int index) {
    	super(p);
    	first=null;
    	second=null;
    	distanceToP=-1;
    	this.index=index;
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
    
    public PointTemoins getFirstNearestPointToP() {
    	return this.first;
    }
    
    public PointTemoins getSecondNearestPointToP() {
    	return this.second;
    }
    
    public double getFirstDistanceToP() {
    	return this.distanceToP;
    }
    
    public double getSecondDistanceToP() {
    	return this.secondDistanceToP;
    }
    
    public void setFirstNearestPoint(PointTemoins p) {
    	this.first=p;
    }
    
    public void setSecondNearestPoint(PointTemoins p) {
    	this.second=p;
    }
    
    public void setFirstDistanceToP(double d) {
    	this.distanceToP=d;
    }
    
    public void setSecondDistanceToP(double d) {
    	this.secondDistanceToP=d;
    }
    
    public int getIndex() {
    	return this.index;
    }
    
    public void setIndex(int in) {
    	this.index=in;
    }
}

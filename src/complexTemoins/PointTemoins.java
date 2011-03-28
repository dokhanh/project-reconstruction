package complexTemoins;

import java.util.ArrayList;

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
	Simplex simplex;
	
	SimplexFace face;
	
	PointTemoins nearestPoint;
    
    /**
     * La distance à l'ensemble P.
     */
    private double distanceToP;
    private int index;
    
    private boolean added;
    
    /**
     * Simple construction.
     */
    public PointTemoins(int index) {
    	super();
    	this.simplex=new Simplex();
    	this.face=new SimplexFace();
    	this.nearestPoint=null;
    	distanceToP=-1;
    	this.index=index;
    	this.added=false;
    }
    
    /**
     * Construction à partie d'un point 2D. On fait simplement la copie.
     * @param p Point 2D.
     */
    public PointTemoins (Point_2 p, int index) {
    	super(p);
     	this.simplex=new Simplex();
    	this.face=new SimplexFace();
    	this.nearestPoint=null;
    	distanceToP=-1;
    	this.index=index;
    	this.added=false;
    }
    
    public boolean added() {
    	return this.added;
    }
    
    public void addToP() {
    	this.added=true;
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
    
    public double getFirstDistanceToP() {
    	return this.distanceToP;
    }
    
    public void setFirstDistanceToP(double d) {
    	this.distanceToP=d;
    }
    
    public int getIndex() {
    	return this.index;
    }
    
    public void setIndex(int in) {
    	this.index=in;
    }
    
    public void insert(PointTemoins point) {
    	this.simplex.insert(point, this);
    	this.face.insert(point, this);
    	this.distanceToP=this.distanceTo(this.nearestPoint);
    }
}

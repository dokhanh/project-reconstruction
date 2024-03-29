package complexTemoins;

import Jcg.geometry.Point_2;
import Jcg.geometry.Vector_2;

/**
 * Une classe h�rit�e de {@link Point_2} pour l'impl�mentation de notre algorithme
 * de complexe de t�moins.
 * @author Khanh
 *
 */

public class PointTemoins extends Point_2 {
	/**
	 * Le simplexe dont le point this est un t�moins.
	 */
	Simplex simplex;
	
	/**
	 * La face dont le point this est un t�moins.
	 */
	SimplexFace face;
	
	/**
	 * Le point dans P le plus proche par rapport au point this.
	 */
	PointTemoins nearestPoint;
    
    /**
     * La distance � l'ensemble P.
     */
    private double distanceToP;
    
    /**
     * Index du point.
     */
    private int index;
    
    /**
     * La variable bool�enne indiquant si le point est d�j� dans P ou non.
     */
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
     * Construction � partie d'un point 2D. On fait simplement la copie.
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
     * Fonction g�o pour mesurer la distance � un autre point.
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

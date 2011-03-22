package complexTemoins3D;

import Jcg.geometry.Point_3;

/**
 * Paraille que le cas 2D
 * @author Jinze
 *
 */

public class PointTemoins3D extends Point_3 {
	/**
	 * Le premier point dans P le plus proche.
	 */
	public PointTemoins3D first;
	
	/**
	 * La seconde point dans P le plus proche.
	 */
	public PointTemoins3D second;
	
	/**
	 * La troisieme point dans P le plus proche.
	 */
	public PointTemoins3D third;
	
	/**
	 * La distance a l'ensemble P.
	 */
	double distanceToP;
	double secondDistanceToP;
	double thirdDistanceToP;
	
	private int index;
	
	private boolean added;
	
	/**
	 * simple construction.
	 */
	public PointTemoins3D(int index) {
		super();
		first = null;
		second = null;
		third = null;
		distanceToP = -1;
		secondDistanceToP = -1;
		thirdDistanceToP = -1;
		this.index = index;
		this.added = false;
	}
	
	/**
	 * Construction a partie d'un point 3D. On fait simplement la copie.
	 * @param p Point 3D.
	 */
	public PointTemoins3D(Point_3 p, int index) {
		super(p);
		first = null;
		second = null;
		third = null;
		distanceToP = -1;
		secondDistanceToP = -1;
		thirdDistanceToP = -1;
		this.index = index;
		this.added = false;
	}
	
	public boolean added() {
		return this.added;
	}
	
	public void addToP() {
		this.added = true;
	}
	
	/**
	 * Fonction geo pour mesurer la distance a un autre point.
	 * @param q Point auquele on veut calculer la distance.
	 * @return Distance en double entre le point au courant et q.
	 */
	public double distanceTo(PointTemoins3D q) {
		return this.distanceFrom(q).doubleValue();
	}
	
	public PointTemoins3D getFirstNearestPointToP() {
    	return this.first;
    }
    
    public PointTemoins3D getSecondNearestPointToP() {
    	return this.second;
    }
    
    public double getFirstDistanceToP() {
    	return this.distanceToP;
    }
    
    public double getSecondDistanceToP() {
    	return this.secondDistanceToP;
    }
    
    public void setFirstNearestPoint(PointTemoins3D p) {
    	this.first=p;
    }
    
    public void setSecondNearestPoint(PointTemoins3D p) {
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
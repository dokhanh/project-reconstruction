package complexTemoins3D;

import java.util.ArrayList;

import complexTemoins.PointTemoins;

import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;

/**
 * Paraille que le cas 2D
 * @author Khanh & Jinze
 *
 */

public class PointTemoins3D extends Point_3 {
	private ArrayList<PointTemoins3D> fourNearestPoints;
	
	/**
	 * La distance a l'ensemble P.
	 */
	double distanceToP;
	
	private int index;
	
	private boolean added;
	
	/**
	 * simple construction.
	 */
	public PointTemoins3D(int index) {
		super();
    	this.fourNearestPoints=new ArrayList<PointTemoins3D>();
		distanceToP = -1;
		this.index = index;
		this.added = false;
	}
	
	/**
	 * Construction a partie d'un point 3D. On fait simplement la copie.
	 * @param p Point 3D.
	 */
	public PointTemoins3D(Point_3 p, int index) {
		super(p);
    	this.fourNearestPoints=new ArrayList<PointTemoins3D>();
		distanceToP = -1;
		this.index = index;
		this.added = false;
	}
	
	public ArrayList<PointTemoins3D> getFourNearestPoints() {
    	return this.fourNearestPoints;
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
	public double distanceTo (PointTemoins3D q) {
    	Vector_3 vector=new Vector_3(this, q);
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
}
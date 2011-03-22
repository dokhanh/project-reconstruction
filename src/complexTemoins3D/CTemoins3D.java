package complexTemoins3D;

/**
 * similaire au cas 3D
 * @author Khanh & Jinye
 */

import java.util.ArrayList;
import java.util.Collection;

import Jcg.geometry.Point_3;

public class CTemoins3D {	
	/**
	 * La liste de points W;
	 */
	ArrayList<PointTemoins3D> W;
	
	public ArrayList<PointTemoins3D> getCloud3D() {
		return this.W;
	}
	
	/**
	 * Une simple construction.
	 */
	public CTemoins3D() {
		super();
		W = new ArrayList<PointTemoins3D>();
	}
	
	/**
	 * Une construction plus complexe. On se donne une liste W des points.
	 * @param W nuage de points dans l'espace 3D.
	 */
	public CTemoins3D(Collection<PointTemoins3D> W) {
		super();
		this.W = new ArrayList<PointTemoins3D>(W);
	}
	
	/**
	 * Inserer un point p (qui est deja dans W) dans la liste de temoins. On
	 * s'inspire de la methode de triangulation Delauney_3, mais en fait finalement
	 * ca sert a rien.
	 */
	
	void exchange(PointTemoins3D p1, PointTemoins3D p2) {
		PointTemoins3D temp = p2;
		p2 = p1;
		p1 = temp;
	}
	
	public void insert(Point_3 p) {
		if(!W.contains(p)) throw new Error("Le point" + p + "n'est pas contenu dans l'ensemble de points de depart");
		PointTemoins3D point = (PointTemoins3D)p;
		point.addToP();
		for (PointTemoins3D pTemoins:this.W) {
			if(pTemoins.getFirstNearestPointToP() == null) {
				pTemoins.setFirstNearestPoint(point);
        		pTemoins.setFirstDistanceToP(pTemoins.distanceTo(point));
			}
			else if(pTemoins.getSecondNearestPointToP() == null) {
				pTemoins.setSecondNearestPoint(point);
				if(pTemoins.distanceTo(pTemoins.first)>pTemoins.distanceTo(pTemoins.second)) {
					exchange(pTemoins.first, pTemoins.second);
					pTemoins.distanceToP = pTemoins.distanceTo(pTemoins.first);
				}
			}
			else if(pTemoins.third == null) {
				pTemoins.third = point;
				if(pTemoins.distanceTo(pTemoins.first)>pTemoins.distanceTo(pTemoins.third)) {
					exchange(pTemoins.first, pTemoins.third);
					exchange(pTemoins.second,pTemoins.third);
					pTemoins.distanceToP = pTemoins.distanceTo(pTemoins.first);
				}
				if(pTemoins.distanceTo(pTemoins.second)>pTemoins.distanceTo(pTemoins.third)) {
					exchange(pTemoins.second,pTemoins.third);
					pTemoins.distanceToP = pTemoins.distanceTo(pTemoins.first);
				}
			}
			else if(pTemoins.distanceTo(point)<pTemoins.distanceToP) {
				pTemoins.third = pTemoins.second;
				pTemoins.second = pTemoins.first;
				pTemoins.first = point;
				pTemoins.distanceToP = pTemoins.distanceTo(point);
			}
			else if(pTemoins.distanceTo(point)<pTemoins.distanceTo(pTemoins.second)) {
				pTemoins.third = pTemoins.second;
				pTemoins.second = point;
			}
			else if(pTemoins.distanceTo(point)<pTemoins.distanceTo(pTemoins.third)) {
				pTemoins.third = point;
			}
		}
	}
	
    /**
     * Reconstruction de forme suivant l'algorithme de complexe de témoins, en
     * commençant par un point de départ et le nombre d'itérations.
     * @param startingPoint Point de départ.
     * @param nbDeTemoins Nombre d'itérations.
     */
    public void reconstruction (PointTemoins3D startingPoint, int nbDeTemoins) {
    	if (nbDeTemoins<=1) {
    		return;
    	}
    	insert(startingPoint);
    	for (int i=1;i<nbDeTemoins;i++) {
    		PointTemoins3D pointChoisi=null;
    		for (PointTemoins3D pointT:W) {
    			if (pointChoisi==null || pointChoisi.distanceToP<pointT.distanceToP) pointChoisi=pointT;
    		}
    		insert(pointChoisi);
    	}
    	return;
    }
}

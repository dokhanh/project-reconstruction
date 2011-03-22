package complexTemoins;

import java.util.ArrayList;
import java.util.Collection;

import Jcg.geometry.Point_2;

/**
 * Cette classe a pour but de repr�senter de mani�re math�matique et g�om�trique
 * l'algorithme de complexe de t�moins. Il n'y a donc aucune m�thode qui sert �
 * l'affichage dans cette classe. Pour l'affichage, voir plut�t la classe
 * GraphOfTemoins.
 * @author Khanh & Jinye
 *
 */

public class CTemoins {
    /**
     * La liste de points W.
     */
    private ArrayList<PointTemoins> W;
    
    public ArrayList<PointTemoins> getCloud() {
    	return this.W;
    }
    
    /**
     * Une simple construction.
     */
    public CTemoins() {
    	super();
    	W=new ArrayList<PointTemoins>();
    }
    
    /**
     * Une construction plus complexe. On se donne une liste W des points.
     * @param W nuage de points dans le plan 2D.
     */
    public CTemoins(Collection<PointTemoins> W) {
    	super();
    	this.W=new ArrayList<PointTemoins>(W);
    }
    
    /**
     * Ins�rer un point p (qui est d�j� dans W) dans la liste de t�moins. On
     * s'inspire de la m�thode de triangulation Delauney_2, mais en fait finalement
     * �a sert � rien.
     */
    public void insert(Point_2 p) {
    	if (!W.contains(p)) throw new Error("Le point "+p+" n'est pas contenu dans l'ensemble de points de depart");
        PointTemoins point=(PointTemoins)p;
    	point.addToP();
        for (PointTemoins pTemoins:this.W) {
        	if (pTemoins.getFirstNearestPointToP()==null) {
        		pTemoins.setFirstNearestPoint(point);
        		pTemoins.setFirstDistanceToP(pTemoins.distanceTo(point));
        	}
        	else {
        		if (pTemoins.getSecondNearestPointToP()==null) {
        			pTemoins.setSecondNearestPoint(point);
        			if (pTemoins.distanceTo(pTemoins.getFirstNearestPointToP())>pTemoins.distanceTo(pTemoins.getSecondNearestPointToP())) {
        				PointTemoins temp=pTemoins.getSecondNearestPointToP();
        				pTemoins.setSecondNearestPoint(pTemoins.getFirstNearestPointToP());
        				pTemoins.setFirstNearestPoint(temp);
        			}
        			pTemoins.setFirstDistanceToP(pTemoins.distanceTo(pTemoins.getFirstNearestPointToP()));
        			pTemoins.setSecondDistanceToP(pTemoins.distanceTo(pTemoins.getSecondNearestPointToP()));
        		}
        		else {
        			if (pTemoins.distanceTo(point)<pTemoins.getFirstDistanceToP()) {
        				pTemoins.setSecondNearestPoint(pTemoins.getFirstNearestPointToP());
        				pTemoins.setFirstNearestPoint(point);
        				pTemoins.setSecondDistanceToP(pTemoins.getFirstDistanceToP());
        				pTemoins.setFirstDistanceToP(pTemoins.distanceTo(point));
        			}
        			else if (pTemoins.distanceTo(point)<pTemoins.getSecondDistanceToP()) {
        				pTemoins.setSecondNearestPoint(point);
        				pTemoins.setSecondDistanceToP(pTemoins.distanceTo(point));
        			}
        		}
        	}
        }
    }
    
    /**
     * Reconstruction de forme suivant l'algorithme de complexe de t�moins, en
     * commen�ant par un point de d�part et le nombre d'it�rations.
     * @param startingPoint Point de d�part.
     * @param nbDeTemoins Nombre d'it�rations.
     */
    public void reconstruction (PointTemoins startingPoint, int nbDeTemoins) {
    	if (nbDeTemoins<=1) {
    		return;
    	}
    	insert(startingPoint);
    	for (int i=2;i<=nbDeTemoins;i++) {
    		PointTemoins pointChoisi=null;
    		for (PointTemoins pointT:W) {
    			if (pointChoisi==null || pointChoisi.getFirstDistanceToP()<pointT.getFirstDistanceToP()) pointChoisi=pointT;
    		}
    		insert(pointChoisi);
    	}
    	return;
    }
}

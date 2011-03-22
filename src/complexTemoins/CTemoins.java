package complexTemoins;

import java.util.ArrayList;
import java.util.Collection;

import Jcg.geometry.Point_2;

/**
 * Cette classe a pour but de représenter de manière mathématique et géométrique
 * l'algorithme de complexe de témoins. Il n'y a donc aucune méthode qui sert à
 * l'affichage dans cette classe. Pour l'affichage, voir plutôt la classe
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
     * Insérer un point p (qui est déjà dans W) dans la liste de témoins. On
     * s'inspire de la méthode de triangulation Delauney_2, mais en fait finalement
     * ça sert à rien.
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
     * Reconstruction de forme suivant l'algorithme de complexe de témoins, en
     * commençant par un point de départ et le nombre d'itérations.
     * @param startingPoint Point de départ.
     * @param nbDeTemoins Nombre d'itérations.
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

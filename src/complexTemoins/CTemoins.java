package complexTemoins;

import java.util.ArrayList;
import java.util.Collection;

import Jcg.geometry.Point_2;
import Jcg.triangulations2D.Delaunay_2;
import Jcg.triangulations2D.TriangulationDSVertex_2;

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
	 * La liste de points P.
	 */
    Collection<PointTemoins> temoins;
    
    /**
     * La liste de points W.
     */
    ArrayList<PointTemoins> W;
    
    /**
     * Une simple construction.
     */
    public CTemoins() {
    	super();
    	temoins=new ArrayList<PointTemoins>();
    	W=new ArrayList<PointTemoins>();
    }
    
    /**
     * Une construction plus complexe. On se donne une liste W des points.
     * @param W nuage de points dans le plan 2D.
     */
    public CTemoins(Collection<PointTemoins> W) {
    	super();
    	this.temoins=new ArrayList<PointTemoins>();
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
    	temoins.add(point);
        for (PointTemoins pTemoins:this.W) {
        	if (pTemoins.first==null) {
        		pTemoins.first=point;
        		pTemoins.distanceToP=pTemoins.distanceTo(point);
        	}
        	else {
        		if (pTemoins.second==null) {
        			pTemoins.second=point;
        			if (pTemoins.distanceTo(pTemoins.first)>pTemoins.distanceTo(pTemoins.second)) {
        				PointTemoins temp=pTemoins.second;
        				pTemoins.second=pTemoins.first;
        				pTemoins.first=temp;
        			}
        			pTemoins.distanceToP=pTemoins.distanceTo(pTemoins.first);
        		}
        		else {
        			if (pTemoins.distanceTo(point)<pTemoins.distanceToP) {
        				pTemoins.second=pTemoins.first;
        				pTemoins.first=point;
        				pTemoins.distanceToP=pTemoins.distanceTo(point);
        			}
        			else if (pTemoins.distanceTo(point)<pTemoins.distanceTo(pTemoins.second)) {
        				pTemoins.second=point;
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
    	for (int i=1;i<nbDeTemoins;i++) {
    		PointTemoins pointChoisi=null;
    		for (PointTemoins pointT:W) {
    			if (pointChoisi==null || pointChoisi.distanceToP<pointT.distanceToP) pointChoisi=pointT;
    		}
    		insert(pointChoisi);
    	}
    	return;
    }
}

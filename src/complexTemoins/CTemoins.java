package complexTemoins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import Jcg.geometry.Point_2;
import Jcg.triangulations2D.Delaunay_2;
import Jcg.triangulations2D.TriangulationDSFace_2;
import Jcg.triangulations2D.TriangulationDSVertex_2;

/**
 * Cette classe a pour but de représenter de manière mathématique et géométrique
 * l'algorithme de complexe de témoins. Il n'y a donc aucune méthode qui sert à
 * l'affichage dans cette classe. Pour l'affichage, voir plutôt la classe
 * GraphOfTemoins.
 * @author Khanh & Jinye
 *
 */

public class CTemoins extends Delaunay_2 {
    /**
     * La liste de points W.
     */
    private ArrayList<PointTemoins> W;
    
    HashMap<TriangulationDSVertex_2<Point_2>, ArrayList<PointTemoins>> pointsOfVoronoi;
    
    public ArrayList<PointTemoins> getCloud() {
    	return this.W;
    }
    
    /**
     * Une simple construction.
     */
    public CTemoins() {
    	super();
    	W=new ArrayList<PointTemoins>();
    	this.pointsOfVoronoi=new HashMap<TriangulationDSVertex_2<Point_2>, ArrayList<PointTemoins>>();
    }
    
    /**
     * Une construction plus complexe. On se donne une liste W des points.
     * @param W nuage de points dans le plan 2D.
     */
    public CTemoins(Collection<PointTemoins> W) {
    	super();
    	this.W=new ArrayList<PointTemoins>(W);
    	this.pointsOfVoronoi=new HashMap<TriangulationDSVertex_2<Point_2>, ArrayList<PointTemoins>>();
    }
    
    /**
     * Insérer un point p (qui est déjà dans W) dans la liste de témoins. On
     * s'inspire de la méthode de triangulation Delauney_2, mais en fait finalement
     * ça sert à rien.
     */
    public void insertTemoins(PointTemoins point) {
    	if (!W.contains(point)) throw new Error("Le point "+point+" n'est pas contenu dans l'ensemble de points de depart");
    	point.addToP();
    	//for test
    	//System.out.println(point.getIndex());
        for (PointTemoins pTemoins:this.W) {
        	//for test
        	//System.out.println(pTemoins.getThreeNearestPoints().size());
        	if (pTemoins.getThreeNearestPoints().size()<3) {
        		pTemoins.getThreeNearestPoints().add(point);
        		if (pTemoins.getFirstDistanceToP()==-1 || pTemoins.getFirstDistanceToP()>pTemoins.distanceTo(point)) {
        			pTemoins.setFirstDistanceToP(pTemoins.distanceTo(point));
        		}
        		//for test
            	//if (pTemoins.getThreeNearestPoints().size()==2) System.out.println(pTemoins.getThreeNearestPoints().get(1).getIndex());
        	}
        	else {
        		boolean cc=false;
        		for (PointTemoins pt:pTemoins.getThreeNearestPoints()) {
        			if (pTemoins.distanceTo(pt)>pTemoins.distanceTo(point)) {
        				cc=true;
        				break;
        			}
        		}
        		if (cc=true) {
        			pTemoins.getThreeNearestPoints().add(point);
        			if (pTemoins.getFirstDistanceToP()>pTemoins.distanceTo(point)) {
            			pTemoins.setFirstDistanceToP(pTemoins.distanceTo(point));
            		}
        			PointTemoins pRemoved=null;
        			for (PointTemoins pt: pTemoins.getThreeNearestPoints()) {
        				if (pRemoved==null || pTemoins.distanceTo(pt)>pTemoins.distanceTo(pRemoved)) pRemoved=pt;
        			}
        			pTemoins.getThreeNearestPoints().remove(pRemoved);
        		}
        	}
        }
    }
    
    public void insertTemoinsAdvanced(PointTemoins point) {
    	//for test
    	//System.out.println("add "+point.getIndex()+" "+point.getFirstDistanceToP());
    	if (!W.contains(point)) throw new Error("Le point "+point+" n'est pas contenu dans l'ensemble de points de depart");
    	point.addToP();
    	if (this.finiteVertices().size()==0) {
    		this.pointsOfVoronoi.put(this.insert(point), new ArrayList<PointTemoins>(W));
    		for (PointTemoins pt:W) {
    			pt.getThreeNearestPoints().add(point);
    			pt.setFirstDistanceToP(pt.distanceTo(point));
    		}
    		return;
    	}
    	ArrayList<PointTemoins> pointsToCheck=new ArrayList<PointTemoins>();
    	ArrayList<PointTemoins> pointsToChange=new ArrayList<PointTemoins>();
    	if (this.finiteVertices().size()<=2) {
    		pointsToCheck=W;
    		for (TriangulationDSVertex_2<Point_2> v:this.finiteVertices()) {
    			ArrayList<PointTemoins> pointsToRemove=new ArrayList<PointTemoins>();
				for (PointTemoins pt:this.pointsOfVoronoi.get(v)) {
					if ((Double)v.getPoint().distanceFrom(pt)>point.distanceTo(pt)) {
						pointsToRemove.add(pt);
						pointsToChange.add(pt);
					}
				}
				this.pointsOfVoronoi.get(v).removeAll(pointsToRemove);
    		}
    	}
    	else {
    		pointsToCheck.add(point);
    		TriangulationDSFace_2<Point_2> vp=this.locate(point);
    		if (vp==null) pointsToCheck=W;
    		else {
    			for (int i=0;i<vp.verticesPoints().length;i++) {
    				if (this.finiteVertices().contains(vp.vertex(i))) {
    					pointsToCheck.addAll(this.pointsOfVoronoi.get(vp.vertex(i)));
    					ArrayList<PointTemoins> pointsToRemove=new ArrayList<PointTemoins>();
    					for (PointTemoins pt:this.pointsOfVoronoi.get(vp.vertex(i))) {
    						if ((Double)vp.vertex(i).getPoint().distanceFrom(pt)>point.distanceTo(pt)) {
    							pointsToRemove.add(pt);
    							pointsToChange.add(pt);
    						}
    					}
    					this.pointsOfVoronoi.get(vp.vertex(i)).removeAll(pointsToRemove);
    				}
    			}
    		}
    	}
    	for (PointTemoins pTemoins:pointsToCheck) {
    		if (pTemoins.getThreeNearestPoints().size()<3) {
        		pTemoins.getThreeNearestPoints().add(point);
        		if (pTemoins.getFirstDistanceToP()==-1 || pTemoins.getFirstDistanceToP()>pTemoins.distanceTo(point)) {
        			pTemoins.setFirstDistanceToP(pTemoins.distanceTo(point));
        		}
        		//for test
            	//if (pTemoins.getThreeNearestPoints().size()==2) System.out.println(pTemoins.getThreeNearestPoints().get(1).getIndex());
        	}
        	else {
        		boolean cc=false;
        		for (PointTemoins pt:pTemoins.getThreeNearestPoints()) {
        			if (pTemoins.distanceTo(pt)>pTemoins.distanceTo(point)) {
        				cc=true;
        				break;
        			}
        		}
        		if (cc=true) {
        			pTemoins.getThreeNearestPoints().add(point);
        			if (pTemoins.getFirstDistanceToP()>pTemoins.distanceTo(point)) {
            			pTemoins.setFirstDistanceToP(pTemoins.distanceTo(point));
            		}
        			PointTemoins pRemoved=null;
        			for (PointTemoins pt: pTemoins.getThreeNearestPoints()) {
        				if (pRemoved==null || pTemoins.distanceTo(pt)>pTemoins.distanceTo(pRemoved)) pRemoved=pt;
        			}
        			pTemoins.getThreeNearestPoints().remove(pRemoved);
        		}
        	}
    	}
    	this.pointsOfVoronoi.put(this.insert(point), pointsToChange);
    }
    
    public static int nextIndex(int a) {
    	if (a==2) return 0;
    	else return a+1;
    }
    
    public static int previousIndex(int a) {
    	if (a==0) return 2;
    	else return a-1;
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
    	insertTemoins(startingPoint);
    	for (int i=2;i<=nbDeTemoins;i++) {
    		PointTemoins pointChoisi=null;
    		for (PointTemoins pointT:W) {
    			if (pointChoisi==null || pointChoisi.getFirstDistanceToP()<pointT.getFirstDistanceToP()) pointChoisi=pointT;
    		}
    		
    		insertTemoins(pointChoisi);
    	}
    	return;
    }
    
    public void reconstructionAdvanced(PointTemoins startingPoint, int nbDeTemoins) {
    	if (nbDeTemoins<=1) {
    		return;
    	}
    	insertTemoinsAdvanced(startingPoint);
    	for (int i=2;i<=nbDeTemoins;i++) {
    		PointTemoins pointChoisi=null;
    		for (PointTemoins pointT:W) {
    			if (pointChoisi==null || pointChoisi.getFirstDistanceToP()<pointT.getFirstDistanceToP()) pointChoisi=pointT;
    		}
    		insertTemoinsAdvanced(pointChoisi);
    	}
    	return;
    }
}

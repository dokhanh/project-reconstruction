package complexTemoins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import Jcg.geometry.Point_2;
import Jcg.graph.AdjacencyGraph;
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
    
    ArrayList<PointTemoins> pointsTemoins;
    ArrayList<SimplexFace> faces;
    ArrayList<Simplex> simplex;
    
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
    	this.pointsTemoins=new ArrayList<PointTemoins>();
    	this.faces=new ArrayList<SimplexFace>();
    	this.simplex=new ArrayList<Simplex>();
    	this.pointsOfVoronoi=new HashMap<TriangulationDSVertex_2<Point_2>, ArrayList<PointTemoins>>();
    }
    
    /**
     * Une construction plus complexe. On se donne une liste W des points.
     * @param W nuage de points dans le plan 2D.
     */
    public CTemoins(Collection<PointTemoins> W) {
    	super();
    	this.W=new ArrayList<PointTemoins>(W);
    	this.pointsTemoins=new ArrayList<PointTemoins>();
    	this.faces=new ArrayList<SimplexFace>();
    	this.simplex=new ArrayList<Simplex>();
    	this.pointsOfVoronoi=new HashMap<TriangulationDSVertex_2<Point_2>, ArrayList<PointTemoins>>();
    }
    
    /**
     * Insérer un point p (qui est déjà dans W) dans la liste de témoins. On
     * s'inspire de la méthode de triangulation Delauney_2, mais en fait finalement
     * ça sert à rien.
     */
    public void insertTemoins(PointTemoins point) {
    	System.out.println("add "+point.getIndex()+" "+point.getFirstDistanceToP());
    	if (!W.contains(point)) throw new Error("Le point "+point+" n'est pas contenu dans l'ensemble de points de depart");
    	point.addToP();
        for (PointTemoins pTemoins:this.W) {
        	pTemoins.insert(point);
        }
    }
    
    public void insertTemoinsAdvanced(PointTemoins point) {
    	System.out.println("add "+point.getIndex()+" "+point.getFirstDistanceToP());
    	if (!W.contains(point)) throw new Error("Le point "+point+" n'est pas contenu dans l'ensemble de points de depart");
    	point.addToP();
    	TriangulationDSVertex_2<Point_2> vAdded=this.insert(point);
    	ArrayList<TriangulationDSVertex_2<Point_2>> vAround=new ArrayList<TriangulationDSVertex_2<Point_2>>();
    	ArrayList<PointTemoins> pointsToCheck=new ArrayList<PointTemoins>();
    	ArrayList<PointTemoins> pointsToAdd=new ArrayList<PointTemoins>();
    	if (this.finiteVertices().size()==1) {
    		this.pointsOfVoronoi.put(vAdded, new ArrayList<PointTemoins>(W));
    		for (PointTemoins pt:W) {
    			pt.insert(point);
    		}
    		return;
    	}
    	else {
    		TriangulationDSFace_2<Point_2> vpStart=vAdded.getFace();
    		TriangulationDSFace_2<Point_2> faceCurrent=vpStart;
    		if (this.finiteVertices().size()==2) {
    			for (TriangulationDSVertex_2<Point_2> vv:this.finiteVertices()) {
    				if (vv!=vAdded) {
    					vAround.add(vv);
    					pointsToCheck.addAll(this.pointsOfVoronoi.get(vv));
    					ArrayList<PointTemoins> toRemove=new ArrayList<PointTemoins>();
        				for (PointTemoins pt:this.pointsOfVoronoi.get(vv)) {
        					if (pt.distanceTo(point)<(Double)pt.distanceFrom(vv.getPoint())) {
        						toRemove.add(pt);
        						pointsToAdd.add(pt);
        					}
        				}
        				this.pointsOfVoronoi.get(vv).removeAll(toRemove);
        				this.pointsOfVoronoi.put(vAdded, pointsToAdd);
        	    		for (PointTemoins pt:pointsToCheck) {
        	    			pt.insert(point);
        	    		}
    				}
    				return;
    			}
    		}
    		do {
    			TriangulationDSVertex_2<Point_2> v=faceCurrent.vertex((faceCurrent.index(vAdded)+1)%3);
    			if (this.pointsOfVoronoi.keySet().contains(v)) {
    				vAround.add(v);
    				pointsToCheck.addAll(this.pointsOfVoronoi.get(v));
    				ArrayList<PointTemoins> toRemove=new ArrayList<PointTemoins>();
    				for (PointTemoins pt:this.pointsOfVoronoi.get(v)) {
    					if (pt.distanceTo(point)<(Double)pt.distanceFrom(v.getPoint())) {
    						toRemove.add(pt);
    						pointsToAdd.add(pt);
    					}
    				}
    				this.pointsOfVoronoi.get(v).removeAll(toRemove);
    			}
    			faceCurrent=faceCurrent.neighbor((faceCurrent.index(vAdded)+1)%3);
    		} while (faceCurrent!=null && faceCurrent!=vpStart);
    		this.pointsOfVoronoi.put(vAdded, pointsToAdd);
    		for (PointTemoins pt:pointsToCheck) {
    			pt.insert(point);
    		}
    	}
    }
    
    public static int nextIndex(int a) {
    	if (a==2) return 0;
    	else return a+1;
    }
    
    public static int previousIndex(int a) {
    	if (a==0) return 2;
    	else return a-1;
    }
    
    public void updateGraph(GraphOfTemoins gtm) {
    	gtm.graph=new AdjacencyGraph(this.getCloud().size());
    	this.pointsTemoins=new ArrayList<PointTemoins>();
    	this.faces=new ArrayList<SimplexFace>();
    	this.simplex=new ArrayList<Simplex>();
    	ArrayList<PointTemoins> collOfPoints=new ArrayList<PointTemoins>();
    	for (PointTemoins pt:this.getCloud()) {
    		collOfPoints.add(pt.nearestPoint);
    	}
    	for (PointTemoins pt:this.getCloud()) {
    		if (pt.face.isValid(collOfPoints)) {
    			pt.face.constructGraph(gtm);
    		}
    	}
    	ArrayList<SimplexFace> collOfFaces=new ArrayList<SimplexFace>();
    	for (PointTemoins pt:this.getCloud()) {
    		collOfFaces.add(pt.face);
    	}
    	for (PointTemoins pt:this.getCloud()) {
    		if (pt.simplex.isValid(collOfFaces)) {
    			pt.simplex.constructGraph(gtm);
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

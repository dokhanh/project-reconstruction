package complexTemoins3D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import Jcg.geometry.Point_3;
import Jcg.triangulations3D.Delaunay_3;
import Jcg.triangulations3D.TriangulationDSCell_3;
import Jcg.triangulations3D.TriangulationDSVertex_3;

/**
 * Cette classe a pour but de représenter de manière mathématique et géométrique
 * l'algorithme de complexe de témoins. Il n'y a donc aucune méthode qui sert à
 * l'affichage dans cette classe. Pour l'affichage, voir plutôt la classe
 * GraphOfTemoins.
 * @author Khanh & Jinye
 *
 */

public class CTemoins3D extends Delaunay_3 {
    /**
     * La liste de points W.
     */
    private ArrayList<PointTemoins3D> W;
    
    HashMap<TriangulationDSVertex_3<Point_3>, ArrayList<PointTemoins3D>> pointsOfVoronoi;
    
    public ArrayList<PointTemoins3D> getCloud() {
    	return this.W;
    }
    
    /**
     * Une simple construction.
     */
    public CTemoins3D() {
    	super();
    	W=new ArrayList<PointTemoins3D>();
    	this.pointsOfVoronoi=new HashMap<TriangulationDSVertex_3<Point_3>, ArrayList<PointTemoins3D>>();
    }
    
    /**
     * Une construction plus complexe. On se donne une liste W des points.
     * @param W nuage de points dans le plan 3D.
     */
    public CTemoins3D(Collection<PointTemoins3D> W) {
    	super();
    	this.W=new ArrayList<PointTemoins3D>(W);
    	this.pointsOfVoronoi=new HashMap<TriangulationDSVertex_3<Point_3>, ArrayList<PointTemoins3D>>();
    }
    
    /**
     * Insérer un point p (qui est déjà dans W) dans la liste de témoins. On
     * s'inspire de la méthode de triangulation Delauney_3, mais en fait finalement
     * ça sert à rien.
     */
    public void insertTemoins(PointTemoins3D point) {
    	if (!W.contains(point)) throw new Error("Le point "+point+" n'est pas contenu dans l'ensemble de points de depart");
    	point.addToP();
    	//for test
    	//System.out.println(point.getIndex());
        for (PointTemoins3D pTemoins:this.W) {
        	//for test
        	//System.out.println(pTemoins.getThreeNearestPoints().size());
        	if (pTemoins.getFourNearestPoints().size()<4) {
        		pTemoins.getFourNearestPoints().add(point);
        		if (pTemoins.getFirstDistanceToP()==-1 || pTemoins.getFirstDistanceToP()>pTemoins.distanceTo(point)) {
        			pTemoins.setFirstDistanceToP(pTemoins.distanceTo(point));
        		}
        		//for test
            	//if (pTemoins.getThreeNearestPoints().size()==2) System.out.println(pTemoins.getThreeNearestPoints().get(1).getIndex());
        	}
        	else {
        		boolean cc=false;
        		for (PointTemoins3D pt:pTemoins.getFourNearestPoints()) {
        			if (pTemoins.distanceTo(pt)>pTemoins.distanceTo(point)) {
        				cc=true;
        				break;
        			}
        		}
        		if (cc=true) {
        			pTemoins.getFourNearestPoints().add(point);
        			if (pTemoins.getFirstDistanceToP()>pTemoins.distanceTo(point)) {
            			pTemoins.setFirstDistanceToP(pTemoins.distanceTo(point));
            		}
        			PointTemoins3D pRemoved=null;
        			for (PointTemoins3D pt: pTemoins.getFourNearestPoints()) {
        				if (pRemoved==null || pTemoins.distanceTo(pt)>pTemoins.distanceTo(pRemoved)) pRemoved=pt;
        			}
        			pTemoins.getFourNearestPoints().remove(pRemoved);
        		}
        	}
        }
    }
    
    public void insertTemoinsAdvanced(PointTemoins3D point) {
    	//for test
    	//System.out.println("add "+point.getIndex()+" "+point.getFirstDistanceToP());
    	if (!W.contains(point)) throw new Error("Le point "+point+" n'est pas contenu dans l'ensemble de points de depart");
    	point.addToP();
    	if (this.finiteVertices().size()==0) {
    		this.pointsOfVoronoi.put(this.insert(point), new ArrayList<PointTemoins3D>(W));
    		for (PointTemoins3D pt:W) {
    			pt.getFourNearestPoints().add(point);
    			pt.setFirstDistanceToP(pt.distanceTo(point));
    		}
    		return;
    	}
    	ArrayList<PointTemoins3D> pointsToCheck=new ArrayList<PointTemoins3D>();
    	ArrayList<PointTemoins3D> pointsToChange=new ArrayList<PointTemoins3D>();
    	if (this.finiteVertices().size()<=3) {
    		pointsToCheck=W;
    		for (TriangulationDSVertex_3<Point_3> v:this.finiteVertices()) {
    			ArrayList<PointTemoins3D> pointsToRemove=new ArrayList<PointTemoins3D>();
				for (PointTemoins3D pt:this.pointsOfVoronoi.get(v)) {
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
    		TriangulationDSCell_3<Point_3> vp=this.locate(point);
    		if (vp==null) pointsToCheck=W;
    		else {
    			for (int i=0;i<vp.verticesPoints().length;i++) {
    				if (this.finiteVertices().contains(vp.vertex(i))) {
    					pointsToCheck.addAll(this.pointsOfVoronoi.get(vp.vertex(i)));
    					ArrayList<PointTemoins3D> pointsToRemove=new ArrayList<PointTemoins3D>();
    					for (PointTemoins3D pt:this.pointsOfVoronoi.get(vp.vertex(i))) {
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
    	for (PointTemoins3D pTemoins:pointsToCheck) {
    		if (pTemoins.getFourNearestPoints().size()<3) {
        		pTemoins.getFourNearestPoints().add(point);
        		if (pTemoins.getFirstDistanceToP()==-1 || pTemoins.getFirstDistanceToP()>pTemoins.distanceTo(point)) {
        			pTemoins.setFirstDistanceToP(pTemoins.distanceTo(point));
        		}
        		//for test
            	//if (pTemoins.getThreeNearestPoints().size()==2) System.out.println(pTemoins.getThreeNearestPoints().get(1).getIndex());
        	}
        	else {
        		boolean cc=false;
        		for (PointTemoins3D pt:pTemoins.getFourNearestPoints()) {
        			if (pTemoins.distanceTo(pt)>pTemoins.distanceTo(point)) {
        				cc=true;
        				break;
        			}
        		}
        		if (cc=true) {
        			pTemoins.getFourNearestPoints().add(point);
        			if (pTemoins.getFirstDistanceToP()>pTemoins.distanceTo(point)) {
            			pTemoins.setFirstDistanceToP(pTemoins.distanceTo(point));
            		}
        			PointTemoins3D pRemoved=null;
        			for (PointTemoins3D pt: pTemoins.getFourNearestPoints()) {
        				if (pRemoved==null || pTemoins.distanceTo(pt)>pTemoins.distanceTo(pRemoved)) pRemoved=pt;
        			}
        			pTemoins.getFourNearestPoints().remove(pRemoved);
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
    public void reconstruction (PointTemoins3D startingPoint, int nbDeTemoins) {
    	if (nbDeTemoins<=1) {
    		return;
    	}
    	insertTemoins(startingPoint);
    	for (int i=2;i<=nbDeTemoins;i++) {
    		PointTemoins3D pointChoisi=null;
    		for (PointTemoins3D pointT:W) {
    			if (pointChoisi==null || pointChoisi.getFirstDistanceToP()<pointT.getFirstDistanceToP()) pointChoisi=pointT;
    		}
    		
    		insertTemoins(pointChoisi);
    	}
    	return;
    }
    
    public void reconstructionAdvanced(PointTemoins3D startingPoint, int nbDeTemoins) {
    	if (nbDeTemoins<=1) {
    		return;
    	}
    	insertTemoinsAdvanced(startingPoint);
    	for (int i=2;i<=nbDeTemoins;i++) {
    		PointTemoins3D pointChoisi=null;
    		for (PointTemoins3D pointT:W) {
    			if (pointChoisi==null || pointChoisi.getFirstDistanceToP()<pointT.getFirstDistanceToP()) pointChoisi=pointT;
    		}
    		insertTemoinsAdvanced(pointChoisi);
    	}
    	return;
    }
}

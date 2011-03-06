package complexTemoins;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import Jcg.geometry.Point_2;
import Jcg.geometry.Segment_2;
import Jcg.geometry.Vector_2;
import Jcg.graph.GeometricGraph_2;
import Jcg.triangulations2D.Delaunay_2;
import Jcg.triangulations2D.HalfedgeHandle;
import Jcg.triangulations2D.TriangulationDSVertex_2;

public class CTemoins extends Delaunay_2 {
    Collection<PointTemoins> temoins;
    ArrayList<PointTemoins> W;
    
    public CTemoins() {
    	super();
    	temoins=new ArrayList<PointTemoins>();
    	W=new ArrayList<PointTemoins>();
    }
    
    public CTemoins(Collection<PointTemoins> W) {
    	super();
    	this.temoins=new ArrayList<PointTemoins>();
    	this.W=new ArrayList<PointTemoins>(W);
    }
    
    public TriangulationDSVertex_2<Point_2> insert(Point_2 p) {
    	if (!W.contains(p)) throw new Error("Le point "+p+" n'est pas contenu dans l'ensemble de points de depart");
        PointTemoins point=(PointTemoins)p;
    	temoins.add(point);
        for (PointTemoins pTemoins:this.W) {
        	if (pTemoins.first==null) {
        		pTemoins.first=point;
        		pTemoins.distanceToP=pTemoins.distanceTo(point);
        	}
        	else {
        		if (pTemoins.distanceToP>pTemoins.distanceTo(point)) pTemoins.distanceToP=pTemoins.distanceTo(point);
        		if (pTemoins.second==null) {
        			pTemoins.second=point;
        			pTemoins.arete=new Segment_2(pTemoins.first, pTemoins.second);
        		}
        		else {
        			if (pTemoins.distanceTo(point)<pTemoins.distanceTo(pTemoins.first)) {
        				pTemoins.second=pTemoins.first;
        				pTemoins.first=point;
        				pTemoins.distanceToP=pTemoins.distanceTo(point);
        				pTemoins.arete=new Segment_2(pTemoins.first, pTemoins.second);
        			}
        			if (pTemoins.distanceTo(point)>=pTemoins.distanceTo(pTemoins.first) && pTemoins.distanceTo(point)<pTemoins.distanceTo(pTemoins.second)) {
        				pTemoins.second=point;
        				pTemoins.arete=new Segment_2(pTemoins.first, pTemoins.second);
        			}
        		}
        	}
        }
        return new TriangulationDSVertex_2<Point_2>(p);
    }
    
    public void reconstruction (PointTemoins startingPoint, int nbDeTemoins) {
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

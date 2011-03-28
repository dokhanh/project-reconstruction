package complexTemoins;

import java.util.ArrayList;
import java.util.Collection;

import Jcg.graph.AdjacencyGraph;

public class SimplexFace {
    ArrayList<PointTemoins> listPoints;
    
    public SimplexFace() {
    	this.listPoints=new ArrayList<PointTemoins>();
    }
    
    public SimplexFace(Collection<PointTemoins> list) {
    	this.listPoints=new ArrayList<PointTemoins>(list);
    	if (this.listPoints.size()>2) {
    		throw new Error ("Simplex face construction error");
    	}
    }
    
    public void insert (PointTemoins point, PointTemoins possesseur) {
    	if (possesseur.face!=this) {
    		throw new Error("Error while inserting a point into SimplexFace");
    	}
    	if (possesseur.nearestPoint==null) {
    		possesseur.nearestPoint=point;
    	}
    	else {
    		if (possesseur.getFirstDistanceToP()>possesseur.distanceTo(point)) possesseur.nearestPoint=point;
    	}
    	if (this.listPoints.size()<2) {
    		listPoints.add(point);
    	}
    	else {
    		listPoints.add(point);
    		PointTemoins pointChoisi=null;
    		for (PointTemoins pt:this.listPoints) {
    			if (pointChoisi==null || possesseur.distanceTo(pointChoisi)<possesseur.distanceTo(pt)) pointChoisi=pt;
    		}
    		listPoints.remove(pointChoisi);
    	}
    }
    
    public boolean equals (Object o) {
    	if (o instanceof SimplexFace) {
    		SimplexFace o1=(SimplexFace)o;
    		return (this.listPoints.containsAll(o1.listPoints) && o1.listPoints.containsAll(this.listPoints));
    	}
    	else return false;
    }
    
    public void constructGraph (GraphOfTemoins gtm) {
    	if (this.listPoints.size()==2) {
    	    gtm.graph.addEdge(this.listPoints.get(0).getIndex(), this.listPoints.get(1).getIndex());
        }
    	if (!gtm.temoins.faces.contains(this)) gtm.temoins.faces.add(this);
    	if (!gtm.temoins.pointsTemoins.contains(this.listPoints.get(0))) gtm.temoins.pointsTemoins.add(this.listPoints.get(0));
    	if (!gtm.temoins.pointsTemoins.contains(this.listPoints.get(1))) gtm.temoins.pointsTemoins.add(this.listPoints.get(1));
    }
    
    public boolean isValid(Collection<PointTemoins> list) {
    	return list.containsAll(this.listPoints);
    }
    
    public String toString() {
    	return this.listPoints.toString();
    }
}

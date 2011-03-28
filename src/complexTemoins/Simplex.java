package complexTemoins;

import java.util.ArrayList;
import java.util.Collection;

import Jcg.graph.AdjacencyGraph;

public class Simplex {
ArrayList<PointTemoins> listPoints;
    
    public Simplex() {
    	this.listPoints=new ArrayList<PointTemoins>();
    }
    
    public Simplex(Collection<PointTemoins> list) {
    	this.listPoints=new ArrayList<PointTemoins>(list);
    	if (this.listPoints.size()>3) {
    		throw new Error ("Simplex construction error");
    	}
    }
    
    public void insert (PointTemoins point, PointTemoins possesseur) {
    	if (possesseur.simplex!=this) {
    		throw new Error("Error while inserting a point into Simplex");
    	}
    	if (this.listPoints.size()<3) {
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
    	if (o instanceof Simplex) {
    		Simplex o1=(Simplex)o;
    		return (this.listPoints.containsAll(o1.listPoints) && o1.listPoints.containsAll(this.listPoints));
    	}
    	else return false;
    }
    
    public boolean isValid(Collection<SimplexFace> collOfFaces) {
    	boolean res=true;
    	for (int i=0;i<this.listPoints.size()-1;i++) {
    		ArrayList<PointTemoins> array=new ArrayList<PointTemoins>();
    		array.add(listPoints.get(i));
    		array.add(listPoints.get(i+1));
    		SimplexFace face=new SimplexFace(array);
    		if (!collOfFaces.contains(face)) {
    			res=false;
    			break;
    		}
    	}
    	ArrayList<PointTemoins> array1=new ArrayList<PointTemoins>();
		array1.add(listPoints.get(0));
		array1.add(listPoints.get(listPoints.size()-1));
		SimplexFace face=new SimplexFace(array1);
		if (!collOfFaces.contains(face)) {
			res=false;
		}
		return res;
    }
    
    public void constructGraph (GraphOfTemoins gtm) {
    	if (gtm.temoins.simplex.contains(this)==false && this.listPoints.size()==3) {
    		//for test
    		//System.out.println("da");
    		gtm.temoins.simplex.add(this);
    	}
    }
    
    public String toString() {
    	return this.listPoints.toString();
    }
}

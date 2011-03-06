package Jcg.graphDrawing;

import java.util.ArrayList;

import Jcg.Fenetre;
import Jcg.geometry.*;
import Jcg.viewer.*;
import Jcg.graph.*;

/**
 * Provides methods for drawing graphs using spectral based methods
 *
 * @author Luca Castelli Aleardi
 */
public abstract class GraphDrawing<X extends Point_>{

	Graph g;
	public ArrayList<X> points;

	/**
	 * returns the position of a vertex in the space (2D or 3D)
	 */
	public X getPoint(int i){
		if(i<0 || i>this.points.size())
			throw new Error("vertex index error");
		return this.points.get(i);
	}

	/**
	 * return the dimension of the representation: 2 or 3
	 */
	public int dimension(){
		X p=this.points.get(0);
		if(p==null) throw new Error("error: no first point computed");
		return p.dimension();
	}

	/**
	 * (abstract method) compute the geometric representation of the graph
	 */
	public abstract void computeDrawing();

	/**
	 * draw the graph in a 2D frame
	 */
	public void draw2D() {
		if(this.dimension()!=2) throw new Error("error: not 2D space");
		int[][] edges=this.g.getEdges();
		//for test
		//System.out.println(edges.length);
		Fenetre f=new Fenetre();
		for(int i=0;i<edges.length;i++) {
			//for test
			//System.out.println((Point_2)this.getPoint(edges[i][0]));
			f.addSegment((Point_2)this.getPoint(edges[i][0]), (Point_2)this.getPoint(edges[i][1]));
		}
	}

	/**
	 * draw the graph in 3D (using class MeshViewer)
	 */
	public void draw3D() {
		if(this.dimension()!=3) throw new Error("error: not 3D space");
		int[][] edges=this.g.getEdges();
		Point_3[] vertices=new Point_3[this.points.size()];
		
		int i=0;
		for(X p: this.points) {
			vertices[i]=(Point_3)p;
			i++;
		}
		
		new MeshViewer(vertices, edges);
	}

}

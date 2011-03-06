package Jcg.graphDrawing;

import java.util.ArrayList;

import Jama.Matrix;
import Jcg.geometry.*;
import Jcg.graph.*;

/**
 * Provides methods for drawing graphs in 2D using iterative Tutte barycentric method
 *
 * @author Luca Castelli Aleardi
 */
public class IterativeTutteDrawing<X extends Point_> extends GraphDrawing<X>{

	Matrix laplacian;
	public static int nIterations=100;
	Point_[] exteriorPoints;

	public IterativeTutteDrawing() {}

	public IterativeTutteDrawing(Graph g, Point_2[] exteriorPoints) {
		this.g=g;
    	this.points=new ArrayList<X>(g.sizeVertices());
    	this.exteriorPoints=exteriorPoints;
	}

	/**
	 * compute the Tutte drawing of a planar graph iteratively
	 * using the Force-Directed paradigm
	 * The first k vertices are assumed to be fixed on the outer face
	 */	
	public void computeDrawing() {
		int k=this.exteriorPoints.length;
		if(k<3) throw new Error("error exterior points");
		for(int i=0;i<k;i++) {
			this.points.add((X)exteriorPoints[i]);
		}
		for(int i=k;i<g.sizeVertices();i++) {
			Point_2 p=new Point_2();
			p.setOrigin();
			this.points.add((X)p);
		}
		
		// computes planar coordinates iteratively
		for(int i=0;i<nIterations;i++) {
			//System.out.println("iteration "+i);
			double[] x=new double[g.sizeVertices()];
			double[] y=new double[g.sizeVertices()];
			for(int j=k;j<g.sizeVertices();j++) {
				int[] neighbors=g.neighbors(j);
				int degree=g.degree(j);
				for(int l=0;l<degree;l++) {
					Point_ p=this.points.get(neighbors[l]);
					double weight=1.;
					//System.out.print("weight "+weight);
					x[j]=x[j]+p.getCartesian(0).doubleValue();
					y[j]=y[j]+p.getCartesian(1).doubleValue();
				}
				//System.out.println("");
				this.points.get(j).setCartesian(0, x[j]/(double)degree);
				this.points.get(j).setCartesian(1, y[j]/(double)degree);					
			}
		}
	}

	/**
	 * Test TutteDrawing methods
	 */
	public static void main(String[] args) {
		System.out.println("Tutte Drawing iteratively");
		Graph g=AdjacencyGraph.constructDodecahedron();

		System.out.println("graph initialized");
		
		Point_2[] exteriorPoints=new Point_2[4];
		exteriorPoints[0]=new Point_2(-10,-10.);
		exteriorPoints[1]=new Point_2(10.,-10.);
		exteriorPoints[2]=new Point_2(10.,10.);
		exteriorPoints[3]=new Point_2(-10.,10.);
		IterativeTutteDrawing<Point_2> d= new IterativeTutteDrawing<Point_2>(g, exteriorPoints);
		d.computeDrawing();
		System.out.println("planar representation computed");
		d.draw2D();
		
	}

}

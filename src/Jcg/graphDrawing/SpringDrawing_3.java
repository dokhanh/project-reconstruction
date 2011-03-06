package Jcg.graphDrawing;

import java.util.ArrayList;

import Jcg.geometry.*;
import Jcg.graph.*;

public class SpringDrawing_3<X extends Point_> extends GraphDrawing<X>{

	static double c1=2., c2=1., c3=1., c4=0.01;
	static int nIterations=15;
	
	public SpringDrawing_3(Graph g) {
		this.g=g;
    	this.points=new ArrayList<X>(g.sizeVertices());
	}
	
	public double attractiveForce(double distance) {
		return c1*(Math.log(distance/c2));
	}
	
	public double repulsiveForce(double distance) {
		return c3/Math.sqrt(distance);
	}
	
	public void setRandomPoints(int n) {
		Point_3 p;
		for (int i=0; i<n;) {
		    p = new Point_3 (1-2*Math.random(), 1-2*Math.random(), 1-2*Math.random());
		    double distance=p.squareDistance(new Point_3(0.,0.,0.)).doubleValue();
		    if ( distance <= 1.) {
		    	this.points.add((X)p);
		    	i++;
		    }
		}		
	}

	/**
	 * compute the drawing of a planar graph iteratively
	 * using the Force-Directed paradigm.
	 * Positions of vertices are updated according to their
	 * mutual attractive and repulsive forces.
	 */	
	public void computeDrawing() {
		this.setRandomPoints(this.g.sizeVertices());
		
		Vector_[] attractiveDisp=new Vector_3[this.g.sizeVertices()];
		Vector_[] repulsiveDisp=new Vector_3[this.g.sizeVertices()];
		for(int it=0;it<nIterations;it++) {
			for(int i=0;i<this.g.sizeVertices();i++) {
				attractiveDisp[i]=computeAttractiveForce(i);
				repulsiveDisp[i]=computeRepulsiveForce(i);
			}
			for(int i=0;i<this.g.sizeVertices();i++) {
				this.points.get(i).translateOf(attractiveDisp[i].multiplyByScalar(c4));
				this.points.get(i).translateOf(repulsiveDisp[i].multiplyByScalar(c4));
			}
			//normalize();
		}
	}
	
	public void normalize(){
		double norm;
		Point_ origin=new Point_3(0., 0., 0.);
		for(X p: this.points) {
			Vector_ v=new Vector_3((Point_3)origin,(Point_3)p);
			norm=Math.sqrt(v.squaredLength().doubleValue());
			if(norm!=0) {
				p.setCartesian(0, p.getCartesian(0).doubleValue()/norm);
				p.setCartesian(1, p.getCartesian(1).doubleValue()/norm);
				p.setCartesian(2, p.getCartesian(2).doubleValue()/norm);
			}
		}
	}
	
	public Vector_ computeRepulsiveForce(int vertex) {
		Vector_ displacement=new Vector_3(0., 0., 0.);
		Vector_3 delta;
		X p=this.points.get(vertex);
		for(int i=0;i<this.g.sizeVertices();i++) {
			if(i!=vertex) {
				delta=new Vector_3((Point_3)this.points.get(i),(Point_3)p);
				double norm=Math.sqrt(delta.squaredLength().doubleValue());
				displacement=displacement.sum(delta.multiplyByScalar(repulsiveForce(norm)));
			}
		}
		return displacement;
	}

	public Vector_ computeAttractiveForce(int vertex) {
		Vector_ displacement=new Vector_3(0., 0., 0.);
		Vector_3 delta;
		X p=this.points.get(vertex);
		for(int i=0;i<this.g.sizeVertices();i++) {
			if(i!=vertex && this.g.adjacent(i, vertex)==true) {
				delta=new Vector_3((Point_3)p, (Point_3)this.points.get(i));
				double norm=Math.sqrt(delta.squaredLength().doubleValue());
				displacement=displacement.sum(delta.multiplyByScalar(attractiveForce(norm)));
			}
		}
		return displacement;
	}

	/**
	 * Test SpringDrawing methods in 3D
	 */
	public static void main(String[] args) {
		System.out.println("Force directed methods, spring spring model in 3D");
		Graph g=AdjacencyGraph.constructDodecahedron();
		System.out.println("graph initialized");
		
		SpringDrawing_3<Point_3> d= new SpringDrawing_3<Point_3>(g);
		d.computeDrawing();
		System.out.println("3D representation computed");
		System.out.println("points"+d.points);
		d.draw3D();
		
	}
}

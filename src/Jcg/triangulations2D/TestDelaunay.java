package Jcg.triangulations2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Jcg.geometry.*;
import Jcg.*;

/**
 * A class for testing the construction of a Delaunay triangulation.
 *
 * @author Luca Castelli Aleardi
 *
 */public class TestDelaunay {

    public static void main (String[] args) {
 
	/* create data structure (empty Delaunay triangulation) */
	Delaunay_2 delaunay = new Delaunay_2();
 
	/* create and insert vertices, updating Delaunay incrementally */
	List<Point_2> vertices = new ArrayList<Point_2>();
	for (int i=0; i<100;) {
	    Point_2 p = new Point_2 (1-2*Math.random(), 1-2*Math.random());
	    if (p.squareDistance(new Point_2(0.,0.)).doubleValue() <= 1.) {
		vertices.add (p);
		delaunay.insert( vertices.get(i) );
		//System.out.print ("\r                                                  \r" + i + ": " + vertices.get(i));
	    i++;
	    }
	}
	Point_2 p1=new Point_2(-2.5,-1.2);
	Point_2 p2=new Point_2(2.5,-1.2);
	Point_2 p3=new Point_2(0.0,2.2);
	delaunay.insert(p1);
	delaunay.insert(p2);
	delaunay.insert(p3);
	vertices.add(p1);
	vertices.add(p2);
	vertices.add(p3);
	System.out.println("vertices: "+vertices.size());

	/* Get list of Delaunay edges */
	Collection<HalfedgeHandle<Point_2>> edges = delaunay.finiteEdges();

	/* Get list of convex hull edges */
	Collection<HalfedgeHandle<Point_2>> convEdges = delaunay.convexHullEdges();
	
	/* Get list of all Delaunay triangles */
	ArrayList<Point_2[]> triangles = (ArrayList<Point_2[]>)delaunay.finiteTriangles();
	System.out.println("triangles "+triangles.size());
	
	/* Get list of boundaries of the Voronoi regions */
	List<Point_2[]> voronoi = (List<Point_2[]>)delaunay.voronoiEdges();
	System.out.println("Voronoi computed");

	/* Output triangulation to OFF file */
	
	IO.writeToFile (vertices, triangles, "res.off");
	System.out.println("off file created");

	/* Output convex hull (CH) edges to OFF file */
	// LCA: a' modifier signature
	//IO.writeToFile (vertices, convEdges, "conv.off");

	/* Compute intersections of CH edges' bisectors with unit circle */
	ArrayList<Point_2> l = new ArrayList<Point_2>();
	ArrayList<Point_2[]> lp = new ArrayList<Point_2[]>();
	// LCA: a' modifier type
	//for (Point_2[] e : convEdges) {
	//    l.add (MesherOperations.midpointOnUnitCircle (e));
	//    lp.add (new Point_2[]
	//	{MesherOperations.midpointOnUnitCircle (e)});
	//}
	//IO.writeToFile (l, lp, "circle.off");
    }
}

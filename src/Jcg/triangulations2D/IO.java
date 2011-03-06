package Jcg.triangulations2D;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

import Jcg.geometry.*;
import Jcg.polyhedron.*;
import Jcg.viewer.MeshViewer;

public class IO {

    /**
     * Output triangulation (edges or triangles) to OFF file
     * @param List<Point> vertices: list of geometric points corresponding
     *        to the vertices of the triangulation
     * @param List<Point[]> faces: list of arrays of vertices corresponding
     *        to the faces (edges or triangles) of the triangulation
     * @param String filename: name of the output OFF file 
     *        (overwritten if already exists)
     */
    public static void writeToFile
	(Collection<Point_2> vertices, Collection<Point_2[]> faces, String filename) {
	// store vertex indices in map 
	HashMap <Point_2,Integer> vert = new HashMap<Point_2,Integer> ();
	try {
	 BufferedWriter out = new BufferedWriter
	     (new FileWriter(filename));
	 out.write ("OFF\n");
	 out.write (vertices.size() + " " + faces.size() + " 0\n");
	 int i=0;
	 for (Point_2 p : vertices) {
	     out.write(p.x + " " + p.y + " 0\n");
	     vert.put(p, new Integer(i++));
	 }    
	 
	 for (Point_2[] f : faces) {
	     Integer[] ind = new Integer [f.length];
	     for (int j=0; j<f.length; j++) {
		 ind[j] =  vert.get(f[j]);
		 if (ind[j] == null)
			 throw new Error ("Index pb in faces set in writeToFile().");
	     }
	     out.write("" + f.length);
	     for (int j=0; j<f.length; j++)
		 out.write (" " + ind[j].intValue());
	     out.write ("\n"); 
	 }
	 out.close();
	} catch (IOException e) {}
    }

    /**
     * Output triangulation edges to OFF file
     */
    public static void writeToFile(Collection<QuadEdge> qEdges, String filename) {
    	Collection<Point_2> vertices = new HashSet<Point_2>();
    	Collection<Point_2[]> edges = new HashSet<Point_2[]>();
    	for (QuadEdge q : qEdges) {
    		vertices.add(q.orig());
    		vertices.add(q.dest());
    		edges.add(new Point_2[]{q.orig(), q.dest()});
    	}
    	writeToFile(vertices, edges, filename);
    }


    /**
     * Return the (geometric) triangles of a 2D triangulation (useful for 2D rendering)
     */
    public static Collection<Point_2[]> getTriangles(TriangulationDS_2<Point_2> mesh) {
    	Collection<Point_2[]> triangles = new ArrayList<Point_2[]>();
    	for (TriangulationDSFace_2<Point_2> t: mesh.faces) {
    		Point_2[] vertices=new Point_2[3];
    		vertices[0]=t.vertex(0).getPoint();
    		vertices[1]=t.vertex(1).getPoint();
    		vertices[2]=t.vertex(2).getPoint();
    		triangles.add(vertices);
    	}
    	return triangles;
    }

    /**
     * Create a Triangle Data Structure for a given 3D surface mesh (from a file).
     * The mesh connectivity and geometry are stored in a OFF file.
     */
    public static TriangulationDS_2<Point_3> getTriangleMeshFromFile(String filename) {
    	System.out.println("Creating triangulated 3D mesh from OFF file");
		MeshRepresentation m=new MeshRepresentation();
    	m.readOffFile(filename);
    	
    	TriangulationDS_2<Point_3> result=new TriangulationDS_2<Point_3>(m.points, m.faces);
    	return result;
    }

    /**
     * Create a Triangle Data Structure for a given plane triangulation (from a file).
     * The graph connectivity and geometry are stored in a OFF file.
     */
    public static TriangulationDS_2<Point_2> getPlaneTriangulationFromFile(String filename) {
    	System.out.println("Creating triangulated 3D mesh from OFF file");
		MeshRepresentation m=new MeshRepresentation();
    	m.readOffFile(filename);
    	
    	Point_2[] planarPoints=LoadMesh.Point3DToPoint2D(m.points);
    	TriangulationDS_2<Point_2> result=new TriangulationDS_2<Point_2>(planarPoints, m.faces);
    	return result;
    }

}

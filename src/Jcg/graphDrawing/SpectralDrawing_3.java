package Jcg.graphDrawing;

import java.util.ArrayList;
import java.util.List;

import Jcg.Fenetre;
import Jcg.geometry.*;
import Jcg.graph.*;

/**
 * Provides methods for drawing graphs in 3D using spectral based methods
 *
 * @author Luca Castelli Aleardi
 */
public class SpectralDrawing_3<X extends Point_> extends SpectralDrawing_2<X> {

	public SpectralDrawing_3() {}

	public SpectralDrawing_3(int n) {
    	this.points=new ArrayList<X>(n);
	}

	public SpectralDrawing_3(Graph g) {
		this.g=g;
    	this.points=new ArrayList<X>(g.sizeVertices());
	}

	public Point_3 computeCoordinates_3(int i,double[] eigenvalues, double[][] eigenvectors) {
		double x,y,z;
		x=eigenvectors[i][1]/Math.sqrt(eigenvalues[1]);
		y=eigenvectors[i][2]/Math.sqrt(eigenvalues[2]);
		z=eigenvectors[i][3]/Math.sqrt(eigenvalues[3]);
		return new Point_3(x,y,z);
	}
	
	public void computeDrawing() {
		this.laplacian=this.LaplacianMatrix();
		System.out.println("Laplacian Matrix computed");

		double[] eValues=this.Eigenvalues(laplacian);
		double[][] eVectors=this.Eigenvectors(laplacian);
		
		X p;
		for(int i=0;i<this.g.sizeVertices();i++) {
			p=(X)computeCoordinates_3(i, eValues, eVectors);
			this.points.add(p);
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Spectral Drawing 3D");
		Graph g=AdjacencyGraph.constructDodecahedron();
		System.out.println("graph initialized");
		
		SpectralDrawing_3<Point_3> d= new SpectralDrawing_3<Point_3>(g);
		d.computeDrawing();
		d.draw3D();
		
	}
}

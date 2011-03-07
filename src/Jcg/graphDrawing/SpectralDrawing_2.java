package Jcg.graphDrawing;

import Jcg.geometry.*;
import Jcg.graph.*;

import Jama.*;
import java.util.*;

/**
 * Provides methods for drawing graphs in 2D using spectral based methods
 *
 * @author Luca Castelli Aleardi
 */
public class SpectralDrawing_2<X extends Point_> extends GraphDrawing<X>{

	Matrix laplacian;

	public SpectralDrawing_2() {}

	public SpectralDrawing_2(int n) {
    	this.points=new ArrayList<X>(n);
	}

	public SpectralDrawing_2(Graph g) {
		this.g=g;
    	this.points=new ArrayList<X>(g.sizeVertices());
	}
	
	/**
	 * computes and returns the adjacency matrix of a graph
	 */
	public Matrix AdjacencyMatrix() {
		double[][] m=new double[this.g.sizeVertices()][this.g.sizeVertices()];
		for(int i=0;i<this.g.sizeVertices();i++) {
			for(int j=0;j<this.g.sizeVertices();j++)
				if(this.g.adjacent(i,j)==true)
					m[i][j]=1.;
				else m[i][j]=0.;
	   	}
	   	return new Matrix(m);
	}

	/**
	 * computes and returns the laplacian matrix of a graph
	 */	
	public Matrix LaplacianMatrix() {
		double[][] m=new double[this.g.sizeVertices()][this.g.sizeVertices()];
		for(int i=0;i<this.g.sizeVertices();i++) {
			for(int j=0;j<this.g.sizeVertices();j++)
				if(i==j) m[i][j]=this.g.degree(i);
				else if(this.g.adjacent(i,j)==true)
					m[i][j]=-1.;
				else m[i][j]=0.;
	   	}
	   	return new Matrix(m);
	}
	
	public double[] Eigenvalues(Matrix m){
		EigenvalueDecomposition decomposition=m.eig();   	  					
		double[] eigenvalues=decomposition.getRealEigenvalues();

//		System.out.println("eigenvalues: ");
//		for(int i=0;i<eigenvalues.length;i++)
//			System.out.println(""+eigenvalues[i]);
	   	  		
		return eigenvalues;
	}

	public double[][] Eigenvectors(Matrix m){
		EigenvalueDecomposition decomposition=m.eig();   	  					
	   	Matrix eigenVectorsMatrix=decomposition.getV();
	   	double[][] eigenvectors=eigenVectorsMatrix.getArray();
	   	
//	   	System.out.println("eigenvectors matrix");
//	   	eigenVectorsMatrix.print(2,2);
	   	
	   	return eigenvectors;
	}
	
	public Point_2 computeCoordinates_2(int i,double[] eigenvalues, double[][] eigenvectors) {
		double x,y;
		x=eigenvectors[i][1]/Math.sqrt(eigenvalues[1]);
		y=eigenvectors[i][2]/Math.sqrt(eigenvalues[2]);
		return new Point_2(x,y);
	}
	
	public void computeDrawing() {
		this.laplacian=this.LaplacianMatrix();
		//System.out.println("Laplacian Matrix computed");

		double[] eValues=this.Eigenvalues(laplacian);
		//for test
		//System.out.println("ok");
		double[][] eVectors=this.Eigenvectors(laplacian);
		
		X p;
		for(int i=0;i<this.g.sizeVertices();i++) {
			p=(X)computeCoordinates_2(i, eValues, eVectors);
			this.points.add(p);
		}
		//for test
		//System.out.println("ok");
	}

	/**
	 * Test SpectralDrawing methods
	 */
	public static void main(String[] args) {
		System.out.println("Spectral Drawing 2D");		
		Graph g=AdjacencyGraph.constructCube();
		System.out.println("graph initialized");
		
		SpectralDrawing_2<Point_2> d= new SpectralDrawing_2<Point_2>(g);
		d.computeDrawing();
		d.draw2D();
	}

}

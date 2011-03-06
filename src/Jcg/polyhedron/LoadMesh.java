package Jcg.polyhedron;

import java.util.*;
import java.awt.Color;

import Jcg.geometry.*;

public class LoadMesh<X extends Point_> {
	
	public Polyhedron_3<X> createPolyhedron(X[] points, int[] faceDegrees, int[][] faces, int sizeHalfedges)
    {   
    	int i,j;
    	Polyhedron_3<X> result=new Polyhedron_3<X>(points.length,sizeHalfedges,faceDegrees.length);
        
        //System.out.println("Creating a new polyhedron");

        for(i=0;i<points.length;i++){
            result.vertices.add(new Vertex<X>(points[i]));
        }
        
        // auxiliary array containing the index of halfedges
        int[][] incidence=new int[result.sizeOfVertices()][result.sizeOfVertices()];
        for(i=0;i<result.sizeOfVertices();i++) // initialization
            for(j=0;j<result.sizeOfVertices();j++) incidence[i][j]=-1;
        
        int cont=0; // counts the number of inserted halfedges
        
        Face<X> _face;
        for(i=0;i<faceDegrees.length;i++) {         
            _face=new Face<X>(); // creating a face
            result.facets.add(_face);
            
            Halfedge<X> _edge, _prev=null, _begin=null;
            for(j=0;j<faceDegrees[i]-1;j++) {
                _edge=new Halfedge<X>();
                _edge.setVertex(result.vertices.get(faces[i][j+1]));
                _edge.setFace(_face);
                if(incidence[faces[i][j+1]][faces[i][j]]!=-1) {
                    _edge.setOpposite(result.halfedges.get(incidence[faces[i][j+1]][faces[i][j]]));
                    result.halfedges.get(incidence[faces[i][j+1]][faces[i][j]]).setOpposite(_edge);
                }
                if(j>0) { _edge.setPrev(_prev); _prev.setNext(_edge); }
                else _begin=_edge;
                incidence[faces[i][j]][faces[i][j+1]]=cont;
                cont++;
                _prev=_edge;
                result.halfedges.add(_edge);
            }
            // creating the last halfedge relative to the current face
            _edge=new Halfedge<X>();
            _edge.setVertex(result.vertices.get(faces[i][0])); // the target vertex is the first vertex in ccw order incident to the current face
            _edge.setFace(_face);
            if(incidence[faces[i][0]][faces[i][j]]!=-1) {
                _edge.setOpposite(result.halfedges.get(incidence[faces[i][0]][faces[i][j]]));
                result.halfedges.get(incidence[faces[i][0]][faces[i][j]]).setOpposite(_edge);
            }
            _edge.setPrev(_prev);
            _prev.setNext(_edge);
            _edge.setNext(_begin);
            _begin.setPrev(_edge);
            incidence[faces[i][j]][faces[i][0]]=cont;
            cont++;
            result.halfedges.add(_edge);
            
            _face.setEdge(_edge); // the current face points to the last incidence halfedge
        }
        
        for(i=0;i<result.halfedges.size();i++) {
        	Halfedge<X> pedge=(Halfedge<X>)result.halfedges.get(i);
        	Vertex<X> pvertex=pedge.vertex;
        	pvertex.halfedge=pedge;
        }
        
        return result;        
    }
    
    public static Point_2[] Point3DToPoint2D(Point_3[] points) {
    	Point_2[] result=new Point_2[points.length];
    	for(int i=0;i<points.length;i++) {
    		result[i]=new Point_2(points[i].getX().doubleValue(), points[i].getY().doubleValue());
    	}
    	return result;
    }
        
    public static void main(String[] args) {
    }
    
}

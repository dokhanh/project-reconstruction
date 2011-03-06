package complexTemoins;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import Jcg.geometry.Point_2;
import Jcg.geometry.Segment_2;
import Jcg.geometry.Vector_2;
import Jcg.graph.AdjacencyGraph;
import Jcg.graph.GeometricGraph;
import Jcg.graph.GeometricGraph_2;
import Jcg.graph.GraphNode;
import Jcg.graphDrawing.SpectralDrawing_2;

public class GraphOfTemoins {
    CTemoins temoins;
    AdjacencyGraph graph;
    double taille;
    HashMap<PointTemoins, Integer> mapping;
    
    public GraphOfTemoins(CTemoins temoins) {
    	this.temoins=temoins;
    	mapping=new HashMap<PointTemoins, Integer>();
    }
    
    public void readFromAFile(String filename) {
    	BufferedReader br=null;
    	try {
    		br=new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}
    	try {
    		String line=br.readLine();
    		String[] mots=line.split(" ");
    		taille=Double.parseDouble(mots[0])/2;
    		while (br.ready()) {
    			line=br.readLine();
    			mots=line.split(" ");
    			double x=Double.parseDouble(mots[0]);
    			double y=Double.parseDouble(mots[1]);
    			PointTemoins point=new PointTemoins(new Point_2(x,y));
    			this.temoins.W.add(point);
    		}
    		br.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public GraphOfTemoins(String filename) {
    	this.temoins=new CTemoins();
    	mapping=new HashMap<PointTemoins, Integer>();
    	readFromAFile(filename);
    }
    
    public void constructGraph() {
    	int nb=0;
    	for (PointTemoins pointT:temoins.W) {
    		mapping.put(pointT, nb);
    		nb+=1;
    	}
    	graph=new AdjacencyGraph(nb);
    	for (PointTemoins pointT:temoins.W) {
    		Segment_2 segment=pointT.arete;
    		//for test
    		//System.out.println(mapping.get((PointTemoins)(segment.target())));
    		graph.addEdge(mapping.get((PointTemoins)(segment.source())), mapping.get((PointTemoins)(segment.target())));
    		//for test
    		System.out.println("da them vao "+mapping.get((PointTemoins)(segment.source()))+" "+mapping.get((PointTemoins)(segment.target())));
    	}
    }
    
    public void updateGraph() {
    	int nb=graph.sizeVertices();
    	for (int i=0;i<nb;i++) {
    		for (int j=0;j<nb;j++) {
    			if (graph.adjacent(i, j)) graph.removeEdge(i, j);
    		}
    	}
    	for (PointTemoins pointT:temoins.W) {
    		Segment_2 segment=pointT.arete;
    		graph.addEdge(mapping.get((PointTemoins)(segment.source())), mapping.get((PointTemoins)(segment.target())));
    	}
    }
    
    public void draw() {
    	SpectralDrawing_2<PointTemoins> spd=new SpectralDrawing_2<PointTemoins>(this.graph);
    	//for test
    	//System.out.println(spd.points.size());
    	spd.computeDrawing();
    	spd.draw2D();
    }
    
    public static void main (String[] args) {
    	GraphOfTemoins gtm=new GraphOfTemoins(args[0]);
    	int nb=gtm.temoins.W.size();
    	gtm.temoins.reconstruction(gtm.temoins.W.get(0), nb/2);
    	gtm.constructGraph();
    	gtm.draw();
    }
}

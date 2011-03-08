package complexTemoins;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import Jcg.Fenetre;
import Jcg.geometry.Point_2;
import Jcg.graph.AdjacencyGraph;
import Jcg.graphDrawing.SpectralDrawing_2;

/**
 * Cette classe est pour effectuer la reconstruction et l'affichage de résultats.
 * @author Khanh & Jinye
 *
 */

public class GraphOfTemoins {
	/**
	 * Contient W et P, pas plus.
	 */
    CTemoins temoins;
    
    /**
     * La structure de graphe. Il faut quand même savoir enfin quels sont les points
     * qui sont reliés.
     */
    AdjacencyGraph graph;
    
    /**
     * C'est la valeur absolue maximale de coordonnées de points dans le plan.
     * Jusqu'à mtn ça sert à rien, tous sont automatiquement réglés.
     */
    double taille;
    
    /**
     * Correspond chaque point à son ordre. On l'utilise à cause de l'utilisation
     * de la classe AdjacencyGraph.
     */
    HashMap<PointTemoins, Integer> mapping;
    
    /**
     * Construction, en sachant W et P.
     * @param temoins
     */
    public GraphOfTemoins(CTemoins temoins) {
    	this.temoins=temoins;
    	mapping=new HashMap<PointTemoins, Integer>();
    	int nb=0;
    	for (PointTemoins pointT:temoins.W) {
    		mapping.put(pointT, nb);
    		nb+=1;
    	}
    	graph=new AdjacencyGraph(nb);
    }
    
    /**
     * Lire les coordonnée des points W à partir d'un fichier. C'est ce qui sert.
     * @param filename Nom du fichier.
     */
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
    	int nb=0;
    	for (PointTemoins pointT:temoins.W) {
    		mapping.put(pointT, nb);
    		nb+=1;
    	}
    	graph=new AdjacencyGraph(nb);
    }
    
    /**
     * Construction à partir d'un fichier qui contient les doordonnées de tous
     * les points de W.
     * @param filename Nom du fichier.
     */
    public GraphOfTemoins(String filename) {
    	this.temoins=new CTemoins();
    	mapping=new HashMap<PointTemoins, Integer>();
    	readFromAFile(filename);
    }
    
    /**
     * Après la reconstruction de formes d'après l'algo de complexe de témoins,
     * c'est le moment où on doit construire la structure de graphe pour
     * l'affichage.
     */
    public void constructGraph() {
    	for (PointTemoins pointT:temoins.W) {
    		if (pointT.first!=null && pointT.second!=null) {
    			graph.addEdge(mapping.get(pointT.first), mapping.get(pointT.second));
    		}
    	}
    }
    
    /**
     * Similaire à la méthode constructGraph, mais plus léger, car on n'a pas besoin
     * de rajouter les sommets.
     */
    public void updateGraph() {
    	int nb=graph.sizeVertices();
    	for (int i=0;i<nb;i++) {
    		for (int j=0;j<nb;j++) {
    			if (graph.adjacent(i, j)) graph.removeEdge(i, j);
    		}
    	}
    	for (PointTemoins pointT:temoins.W) {
    		graph.addEdge(mapping.get(pointT.first), mapping.get(pointT.second));
    	}
    }
    
    /**
     * Pour l'affichage du graphe, on utilise la classe SpectralDrawing_2.<p>Attention:
     * afin de pouvoir bien afficher, il faut d'abord ajouter en ordre les sommets du graphe
     * au SpectralDrawing_2. C'est essentiel.
     */
    public void draw() {
    	SpectralDrawing_2<PointTemoins> spd=new SpectralDrawing_2<PointTemoins>(this.graph);
    	spd.points=new ArrayList<PointTemoins>();
    	PointTemoins[] arrayP=new PointTemoins[graph.sizeVertices()];
    	for (PointTemoins point:mapping.keySet()) {
    		arrayP[mapping.get(point)]=point;
    	}
    	for (int i=0;i<graph.sizeVertices();i++) spd.points.add(arrayP[i]);
    	spd.computeDrawing();
    	spd.draw2D();
    }
    
    /**
     * Pour afficher tous les points donnés sans arête entre eux, mais la méthode
     * n'est pas encore complète.
     */
    public void showPoints() {
    	Fenetre f=new Fenetre();
		for (PointTemoins p:this.temoins.W) f.addPoint(p);
    }
    
    /**
     * Reconstruction pas à pas.
     * @param nbIterations Nombre d'itérations.
     */
    public void reconstructAndView (int nbIterations) {
    	if (nbIterations<=1) {
    		showPoints();
    	}
    	else {
    		temoins.reconstruction(temoins.W.get((int)(Math.random()*temoins.W.size())), 2);
    		constructGraph();
    		draw();
    		//for test
    		System.out.println(2);
    		for (int i=3;i<=nbIterations;i++) {
        		temoins.reconstruction(temoins.W.get(0), i);
        		updateGraph();
        		draw();
        		//for test
        		System.out.println(i);
        	}
    	}
    }
    
    /**
     * Créer une instance de GraphOfTemoins à partir d'un fichier, puis afficher
     * pas à pas la reconstruction.
     * @param args args[0] nom du fichier.
     */
    public static void main (String[] args) {
    	GraphOfTemoins gtm=new GraphOfTemoins(args[0]);
    	gtm.reconstructAndView(100);
    }
}

package complexTemoins3D;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import javax.media.j3d.TransformGroup;
import javax.media.j3d.Appearance;
import java.awt.Color;

import complexTemoins.PointTemoins;

import Jcg.GraphData;
import Jcg.geometry.Point_3;
import Jcg.geometry.Triangle_3;
import Jcg.graph.AdjacencyGraph;
import Jcg.graphDrawing.SpectralDrawing_3;
import Jcg.polyhedron.MeshRepresentation;
import Jcg.viewer.MeshViewer;

/**
 * Cette classe est pour effectuer la reconstruction et l'affichage de résultats.
 * @author Khanh & Jinye
 *
 */

public class GraphOfTemoins3D {
	/**
	 * Contient W et P, pas plus.
	 */
    private CTemoins3D temoins;
    private Collection<Triangle_3> listT;
    
    /**
     * La structure de graphe. Il faut quand même savoir enfin quels sont les points
     * qui sont reliés.
     */
    private AdjacencyGraph graph;
    
    /**
     * Construction, en sachant W et P.
     * @param temoins
     */
    public GraphOfTemoins3D(CTemoins3D temoins) {
    	this.temoins=temoins;
    	int nb=0;
    	for (PointTemoins3D pointT:temoins.getCloud()) {
    		pointT.setIndex(nb);
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
    		while (br.ready()) {
    			line=br.readLine();
    			mots=line.split(" ");
    			double x=Double.parseDouble(mots[0]);
    			double y=Double.parseDouble(mots[1]);
    			double z=Double.parseDouble(mots[2]);
    			PointTemoins3D point=new PointTemoins3D(new Point_3(x,y,z), 0);
    			this.temoins.getCloud().add(point);
    		}
    		br.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	int nb=0;
    	for (PointTemoins3D pointT:temoins.getCloud()) {
    		pointT.setIndex(nb);
    		//for test
    		System.out.println(pointT.getIndex());
    		nb+=1;
    	}
    }
    
    /**
     * Construction à partir d'un fichier qui contient les doordonnées de tous
     * les points de W.
     * @param filename Nom du fichier.
     */
    public GraphOfTemoins3D(String filename) {
    	this.temoins=new CTemoins3D();
    	readFromAFile(filename);
    }
    
    /**
     * Après la reconstruction de formes d'après l'algo de complexe de témoins,
     * c'est le moment où on doit construire la structure de graphe pour
     * l'affichage.
     */
    public void constructGraph() {
    	graph=new AdjacencyGraph(temoins.getCloud().size());
    	for (PointTemoins3D pointT:temoins.getCloud()) {
    		//for test
    		//System.out.println("da");
    		for (int i=0;i<pointT.getFourNearestPoints().size()-1;i++) {
    			graph.addEdge(pointT.getFourNearestPoints().get(i).getIndex(), pointT.getFourNearestPoints().get(i+1).getIndex());
    			//for test
    			//System.out.println(i);
    		}
    		if (pointT.getFourNearestPoints().size()==4) {
    			graph.addEdge(pointT.getFourNearestPoints().get(0).getIndex(), pointT.getFourNearestPoints().get(3).getIndex());
    		}
    	}
    }
    
    public void constructListOfTriangles() {
    	listT = new ArrayList<Triangle_3>();
    	Triangle_3 tempT;
    	for (PointTemoins3D pointT:temoins.getCloud()) {
    		//for test
    		//System.out.println("da");
    		ArrayList<PointTemoins3D> tempL = pointT.getFourNearestPoints();
    		for (int i=0;i<pointT.getFourNearestPoints().size();i++) {
    			tempT = new Triangle_3(tempL.get(i%4),tempL.get((i+1)%4),tempL.get((i+2)%4));
    			//for test
    			//System.out.println(i);
    			listT.add(tempT);
    		}
    	}
    }
    
    /**
     * Calculer le nb de composantes connexes du graphe créé lors du fonctionnement de la
     * méthode reconstruction de las classe CTemoins.<p>Attention: car cette fonction
     * est basée sur le graphe de GraphOfTemoins, il faut construire ce graph (
     * res. mettre à jour) par la méthode constructGraph (res. updateGraph) avant
     * d'appeler cette fonction.
     * @return un int nombre de composantes connexes.
     */
    public int nbOfComposants() {
    	int nbOfComposants=0;
    	int nbIndex=0;
    	int[] etiquette=new int[this.graph.sizeVertices()];
    	for (int i=0;i<graph.sizeVertices();i++) {
    		etiquette[i]=-1;
    	}
    	int[][] listOfEdges=this.graph.getEdges();
    	for (int i=0;i<listOfEdges.length;i++) {
    		int a=listOfEdges[i][0];
    		int b=listOfEdges[i][1];
    		if (etiquette[a]==-1 && etiquette[b]==-1) {
    			etiquette[a]=nbIndex;
    			etiquette[b]=nbIndex;
    			nbOfComposants+=1;
    			nbIndex+=1;
    		}
    		else {
    			if (etiquette[a]==-1 || etiquette[b]==-1) {
    				if (etiquette[a]==-1) etiquette[a]=etiquette[b];
    				if (etiquette[b]==-1) etiquette[b]=etiquette[a];
    			}
    			else if (etiquette[a]!=etiquette[b]) {
    				nbOfComposants-=1;
    				int l=etiquette[b];
        			for (int k=0;k<this.graph.sizeVertices();k++) {
        				if (etiquette[k]==l) etiquette[k]=etiquette[a];
        			}
    			}
    		}
    	}
    	return nbOfComposants;
    }
    
    /**
     * Similaire to la méthode nbOfComposants, mais calculer le nb de cycles du
     * graphe crée.<p>Attention: car cette fonction
     * est basée sur le graphe de GraphOfTemoins, il faut construire ce graph (
     * res. mettre à jour) par la méthode constructGraph (res. updateGraph) avant
     * d'appeler cette fonction.
     * @return un int nb de cycles.
     */
    public int nbDeCycles() {
    	LinkedList<Integer> pointsPasser=new LinkedList<Integer>();
    	HashMap<Integer, LinkedList<Integer>> voisins=new HashMap<Integer, LinkedList<Integer>>();
    	//for test
    	//System.out.println(graph.sizeVertices());
    	for (int i=0;i<graph.sizeVertices();i++) {
    		voisins.put(i, new LinkedList<Integer>());
    		pointsPasser.add(i);
    	}
    	int[][] edges=graph.getEdges();
    	//for test
    	//System.out.println(edges.length);
    	for (int i=0;i<edges.length;i++) {
    		voisins.get(edges[i][0]).add(edges[i][1]);
    		voisins.get(edges[i][1]).add(edges[i][0]);
    	}
    	int nbDeCycles=0;
    	LinkedList<Integer> queue=new LinkedList<Integer>();
    	while (!pointsPasser.isEmpty()) {
    		if (queue.isEmpty()) {
    			int cur=pointsPasser.removeLast();
    			queue.add(cur);
    		}
    		int cur=queue.removeLast();
    		LinkedList<Integer> voisinsOfCur=voisins.get(cur);
    		for (int voisin:voisinsOfCur) {
    			if (!pointsPasser.contains(voisin)) {
    				nbDeCycles+=1;
    				voisins.get(voisin).remove((Integer)cur);
    			}
    			else {
    				voisins.get(voisin).remove((Integer)cur);
    				pointsPasser.remove((Integer)voisin);
    				queue.addLast(voisin);
    			}
    		}
    		voisins.put(cur, new LinkedList<Integer>());
    	}
    	return nbDeCycles;
    }
    
    /**
     * Pour l'affichage du graphe, on utilise la classe SpectralDrawing_2.<p>Attention:
     * afin de pouvoir bien afficher, il faut d'abord ajouter en ordre les sommets du graphe
     * au SpectralDrawing_2. C'est essentiel.
     */
    /*
    public void draw() {
    	SpectralDrawing_2<PointTemoins> spd=new SpectralDrawing_2<PointTemoins>(this.graph);
    	spd.points=new ArrayList<PointTemoins>();
    	PointTemoins[] arrayP=new PointTemoins[graph.sizeVertices()];
    	for (PointTemoins point:temoins.getCloud()) {
    		arrayP[point.getIndex()]=point;
    	}
    	for (int i=0;i<graph.sizeVertices();i++) spd.points.add(arrayP[i]);
    	spd.draw2D();
    }
    */
    
    public void draw3() {
    	Collection<Point_3> listP = new ArrayList<Point_3>();
    	for(PointTemoins3D pointT:temoins.getCloud()) {
    		listP.add(pointT);
    	}
    	
    	TransformGroup objTrans = new TransformGroup();
    	Appearance ap = new Appearance();
    	Color color = new Color(0);
    	MeshRepresentation mp = new MeshRepresentation();
    	mp.getFromTriangleSoup(listT);
    	MeshViewer mesh = new MeshViewer(mp);
    }
    
    /**
     * Pour afficher tous les points donnés sans arête entre eux, mais la méthode
     * n'est pas encore complète.
     */
    public void showPoints() {
    	
    }
    
    /**
     * Reconstruction pas à pas.
     * @param nbIterations Nombre d'itérations.
     */
    public void reconstructAndView (PointTemoins3D startingPoint, int nbDeTemoins) {
    	if (nbDeTemoins<=1) {
    		showPoints();
    	}
    	else {
    		LinkedList<Integer> nbOfComposants=new LinkedList<Integer>();
    		LinkedList<Integer> nbOfCycles=new LinkedList<Integer>();
    		ArrayList<Double> abcis=new ArrayList<Double>();
    		this.temoins.insertTemoins(startingPoint);
    		for (int i=2;i<=nbDeTemoins;i++) {
    			double temps=System.currentTimeMillis();
        		PointTemoins3D pointChoisi=null;
        		for (PointTemoins3D pointT:temoins.getCloud()) {
        			if (pointChoisi==null || pointChoisi.getFirstDistanceToP()<pointT.getFirstDistanceToP()) pointChoisi=pointT;
        		}
        		abcis.add((Double)(1/pointChoisi.getFirstDistanceToP()));
        		temoins.insertTemoins(pointChoisi);
        		temps=System.currentTimeMillis()-temps;
        		this.constructGraph();
        		//draw();
        		int nbComposants=this.nbOfComposants();
        		int nbCycles=this.nbDeCycles();
        		System.out.println(i+" "+nbComposants+" "+nbCycles+" in "+temps+" mls");
        		nbOfComposants.add(nbComposants);
        		nbOfCycles.add(nbCycles);
        		GraphData.showData(nbOfComposants, nbOfCycles, abcis);
        		double temps1=System.currentTimeMillis();
        		while (System.currentTimeMillis()-temps1<1000);
        	}
    	}
    }
    
    public void reconstructAndViewAdvanced (PointTemoins3D startingPoint, int nbDeTemoins) {
    	if (nbDeTemoins<=1) {
    		showPoints();
    	}
    	else {
    		LinkedList<Integer> nbOfComposants=new LinkedList<Integer>();
    		LinkedList<Integer> nbOfCycles=new LinkedList<Integer>();
    		ArrayList<Double> abcis=new ArrayList<Double>();
    		this.temoins.insertTemoinsAdvanced(startingPoint);
    		for (int i=2;i<=nbDeTemoins;i++) {
    			double temps=System.currentTimeMillis();
        		PointTemoins3D pointChoisi=null;
        		for (PointTemoins3D pointT:temoins.getCloud()) {
        			if (pointChoisi==null || pointChoisi.getFirstDistanceToP()<pointT.getFirstDistanceToP()) pointChoisi=pointT;
        		}
        		abcis.add((Double)(1/pointChoisi.getFirstDistanceToP()));
        		temoins.insertTemoinsAdvanced(pointChoisi);
        		temps=System.currentTimeMillis()-temps;
        		this.constructGraph();
        		//draw();
        		int nbComposants=this.nbOfComposants();
        		int nbCycles=this.nbDeCycles();
        		System.out.println(i+" "+nbComposants+" "+nbCycles+" in "+temps+" mls");
        		nbOfComposants.add(nbComposants);
        		nbOfCycles.add(nbCycles);
        		GraphData.showData(nbOfComposants, nbOfCycles, abcis);
        		double temps1=System.currentTimeMillis();
        		while (System.currentTimeMillis()-temps1<1000);
        	}
    	}
    }
    
    public void reconstruction (PointTemoins3D startingPoint, int nbDeTemoins) {
    	double temps=System.currentTimeMillis();
    	temoins.reconstruction(startingPoint, nbDeTemoins);
    	temps=System.currentTimeMillis()-temps;
    	constructGraph();
		//draw();
		int nbComposants=this.nbOfComposants();
		int nbCycles=this.nbDeCycles();
		System.out.println(nbDeTemoins+" "+nbComposants+" "+nbCycles+" in "+temps+" mls");
    }
    
    public void reconstructionAdvanced (PointTemoins3D startingPoint, int nbDeTemoins) {
    	double temps=System.currentTimeMillis();
    	temoins.reconstructionAdvanced(startingPoint, nbDeTemoins);
    	temps=System.currentTimeMillis()-temps;
    	constructGraph();
		//draw();
		int nbComposants=this.nbOfComposants();
		int nbCycles=this.nbDeCycles();
		System.out.println(nbDeTemoins+" "+nbComposants+" "+nbCycles+" in "+temps+" mls");
    }
    
    public void reconstruction3D(PointTemoins3D startingPoint) {
    	
    }
    
    /**
     * Créer une instance de GraphOfTemoins à partir d'un fichier, puis afficher
     * pas à pas la reconstruction.
     * @param args args[0] nom du fichier.
     */
    public static void main (String[] args) {
    	GraphOfTemoins3D gtm=new GraphOfTemoins3D(args[0]);
    	//gtm.reconstructAndViewAdvanced(gtm.temoins.getCloud().get(0), 150);
    	//gtm.reconstructionAdvanced(gtm.temoins.getCloud().get(0), 160);
    	gtm.constructListOfTriangles();
    	gtm.draw3();
    }
}

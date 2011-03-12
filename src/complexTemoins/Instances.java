package complexTemoins;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Instances extends JFrame {
	DrawingCanvas canvas;
	int nbOfPoints;
	public String filename;
	
	public static int SIZE=600;
	public static int SIZEOFREC=15;
	
	public Instances(int nb, String name) {
		super();
		nbOfPoints=nb;
	    Container container = getContentPane();
	    this.filename=name;

	    canvas = new DrawingCanvas();
	    container.add(canvas);
	    
	    addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	        	printFile(nbOfPoints, filename);
	        	System.exit(0);
	        }
	    });
	    setSize(SIZE, SIZE);
	    setVisible(true);
	}
	
	public void printFile(int nbOfPoints, String filename) {
		FileWriter fw=null;
		PrintWriter out=null;
		try {
			fw=new FileWriter(filename);
			out=new PrintWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println("2");
		for (int i=0;i<nbOfPoints;i++) {
			int indexOfRec=(int)(Math.random()*this.canvas.listOfRec.size());
			Rectangle2D rec=canvas.listOfRec.get(indexOfRec);
			double x=rec.getMinX()+Math.random()*(rec.getMaxX()-rec.getMinX());
			double y=rec.getMinY()+Math.random()*(rec.getMaxY()-rec.getMinY());
			double x1=(x-300)/300;
			double y1=(300-y)/300;
			out.println(x1+" "+y1);
		}
		out.close();
	}
	
	public static void main(String args[]) {
	    new Instances(Integer.parseInt(args[0]), args[1]);
	}
	
	class DrawingCanvas extends JPanel {
		double x, y, w, h;
	    int x1, y1, x2, y2;
	    Rectangle2D rec;
	    Cursor curCursor;
	    
	    public LinkedList<Rectangle2D> listOfRec;
	    
	    public DrawingCanvas() {
	    	listOfRec=new LinkedList<Rectangle2D>();
	        w = SIZEOFREC;
	        h = SIZEOFREC;
	        setBackground(Color.white);
	        addMouseListener(new MyMouseListener());
	        addMouseMotionListener(new MyMouseMotionListener());
	    }
	    
	    public void paint(Graphics g) {
	        Graphics2D g2D = (Graphics2D) g;
	        if (x!=0 && y!=0) rec = new Rectangle2D.Double(x, y, w, h);
	        if (rec!=null) g2D.draw(rec);
	    }
	    
	    class MyMouseListener extends MouseAdapter {
	    	public void mousePressed(MouseEvent e) {
	    		if (rec==null) {
	    			x=e.getX();
	    			y=e.getY();
	    			x1=e.getX();
	    			y1=e.getY();
	    			rec=new Rectangle2D.Double(x, y, w, h);
	    		}
	    		else if (rec.contains(e.getX(), e.getY())) {
	    			canvas.repaint();
	    	        x1 = e.getX();
	    	        y1 = e.getY();
	    		}
	    		//for test
		    	//System.out.println("he");
	    	}
	    	
	    	public void mouseReleased(MouseEvent e) {
	    		canvas.repaint();
	    		//for test
	    		//System.out.println(listOfRec);
	    	}
	    	
	    	public void mouseClicked(MouseEvent e) {
	    		if (rec==null) {
	    			x=e.getX();
	    			y=e.getY();
	    			x1=e.getX();
	    			y1=e.getY();
	    			rec=new Rectangle2D.Double(x, y, w, h);
	    		}
		    	canvas.repaint();
		    	//for test
		    	//System.out.println("he");
		    }
	    }
	    
	    class MyMouseMotionListener extends MouseMotionAdapter {
			public void mouseDragged(MouseEvent e) {
				if (rec==null) {
	    			x=e.getX();
	    			y=e.getY();
	    			x1=e.getX();
	    			y1=e.getY();
	    			rec=new Rectangle2D.Double(x, y, w, h);
	    			//for test
	    			//System.out.println("h");
	    		}
				else {
					if (rec.contains(e.getX(), e.getY())) {
						x2 = e.getX();
						y2 = e.getY();
						x = x + x2 - x1;
						y = y + y2 - y1;
						x1 = x2;
						y1 = y2;
					}
				}
				//for test
				//System.out.println("he");
				listOfRec.add(new Rectangle2D.Double(x, y, w, h));
				canvas.repaint();
			}
			public void mouseMoved(MouseEvent e) {
				if (rec != null) {
					if (rec.contains(e.getX(), e.getY())) {
						curCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
					}
					else {
			            curCursor = Cursor.getDefaultCursor();
			        }
				}
				//for test
		    	//System.out.println("he");
				canvas.repaint();
			}
		}
	}
}
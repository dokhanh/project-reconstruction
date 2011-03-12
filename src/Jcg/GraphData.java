package Jcg;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphData extends JPanel {
	int[] data1;
	int[] data2;
	int PAD;
	
	public static JFrame frame=new JFrame("data");
	
	public GraphData (int[] array1, int[] array2) {
		this.data1=array1;
		this.data2=array2;
		PAD=data1.length;
	}
	
	public GraphData (LinkedList<Integer> list1, LinkedList<Integer> list2) {
		data1=new int[list1.size()];
		data2=new int[list1.size()];
		for (int i=0;i<data1.length;i++) {
			data1[i]=list1.get(i);
			data2[i]=list2.get(i);
		}
		PAD=data1.length;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        // Draw ordinate.
        g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
        // Draw abcissa.
        g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
        // Draw labels.
        Font font = g2.getFont();
        FontRenderContext frc = g2.getFontRenderContext();
        LineMetrics lm = font.getLineMetrics("0", frc);
        float sh = lm.getAscent() + lm.getDescent();
        // Ordinate label.
        String s = "Cycles & Composantes";
        float sy = PAD + ((h - 2*PAD) - s.length()*sh)/2 + lm.getAscent();
        for(int i = 0; i < s.length(); i++) {
            String letter = String.valueOf(s.charAt(i));
            float sw = (float)font.getStringBounds(letter, frc).getWidth();
            float sx = (PAD - sw)/2;
            g2.drawString(letter, sx, sy);
            sy += sh;
        }
        // Abcissa label.
        s = "Nb of iterations";
        sy = h - PAD + (PAD - sh)/2 + lm.getAscent();
        float sw = (float)font.getStringBounds(s, frc).getWidth();
        float sx = (w - sw)/2;
        g2.drawString(s, sx, sy);
        // Draw lines.
        double xInc = (double)(w - 2*PAD)/(data1.length-1);
        double scale = (double)(h - 2*PAD)/getMax();
        for(int i = 0; i < data1.length-1; i++) {
            double x1 = PAD + i*xInc;
            double y1 = h - PAD - scale*data1[i];
            double z1 = h - PAD - scale*data2[i];
            double x2 = PAD + (i+1)*xInc;
            double y2 = h - PAD - scale*data1[i+1];
            double z2 = h - PAD - scale*data2[i+1];
            g2.setPaint(Color.green.darker());
            g2.draw(new Line2D.Double(x1, y1, x2, y2));
            g2.setPaint(Color.orange.brighter());
            g2.draw(new Line2D.Double(x1, z1, x2, z2));
        }
        // Mark data points.
        g2.setPaint(Color.red);
        for(int i = 0; i < data1.length; i++) {
            double x = PAD + i*xInc;
            double y = h - PAD - scale*data1[i];
            g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
            double z = h - PAD - scale*data2[i];
            g2.fill(new Ellipse2D.Double(x-2, z-2, 4, 4));
        }
	}
	
	private int getMax() {
        int max = -Integer.MAX_VALUE;
        for(int i = 0; i < data1.length; i++) {
            if(data1[i] > max) max = data1[i];
            if(data2[i] > max) max = data2[i];
        }
        return max;
    }
	
	public static void showData (LinkedList<Integer> list1, LinkedList<Integer> list2) {
		GraphData gdata=new GraphData(list1, list2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().removeAll();
        frame.add(gdata);
        frame.setSize(400,400);
        frame.setVisible(true);
	}
}

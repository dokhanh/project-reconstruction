package complexTemoins;

import Jcg.geometry.Point_2;
import Jcg.geometry.Segment_2;
import Jcg.geometry.Vector_2;
import Jcg.triangulations2D.HalfedgeHandle;

public class PointTemoins extends Point_2 {
    public PointTemoins first;
    public PointTemoins second;
    public Segment_2 arete;
    double distanceToP;
    
    public PointTemoins() {
    	super();
    	first=null;
    	second=null;
    	arete=null;
    	distanceToP=-1;
    }
    
    public PointTemoins (Point_2 p) {
    	super(p);
    	first=null;
    	second=null;
    	arete=null;
    	distanceToP=-1;
    }
    
    public double distanceTo (PointTemoins q) {
    	Vector_2 vector=new Vector_2(this, q);
    	return Math.sqrt((Double)vector.squaredLength());
    }
}

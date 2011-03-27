package complexTemoins;

import Jcg.geometry.Point_2;

public class SegmentTemoins {
    private PointTemoins p1;
    private PointTemoins p2;
    
    SegmentTemoins (PointTemoins p1, PointTemoins p2) {
    	this.p1=p1;
    	this.p2=p2;
    }
    
    public PointTemoins get1() {
    	return p1;
    }
    
    public PointTemoins get2() {
    	return p2;
    }
    
    public boolean equals(Object o) {
    	if (o instanceof SegmentTemoins) {
    		SegmentTemoins o1=(SegmentTemoins)o;
    		return (p1==o1.p1 && p2==o1.p2)||(p1==o1.p2 && p2==o1.p1);
    	}
    	return false;
    }
}

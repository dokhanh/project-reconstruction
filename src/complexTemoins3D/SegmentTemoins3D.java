package complexTemoins3D;

import Jcg.geometry.Point_3;

public class SegmentTemoins3D {
    private PointTemoins3D p1;
    private PointTemoins3D p2;
    
    SegmentTemoins3D (PointTemoins3D p1, PointTemoins3D p2) {
    	this.p1=p1;
    	this.p2=p2;
    }
    
    public PointTemoins3D get1() {
    	return p1;
    }
    
    public PointTemoins3D get2() {
    	return p2;
    }
    
    public boolean equals(Object o) {
    	if (o instanceof SegmentTemoins3D) {
    		SegmentTemoins3D o1=(SegmentTemoins3D)o;
    		return (p1==o1.p1 && p2==o1.p2)||(p1==o1.p2 && p2==o1.p1);
    	}
    	return false;
    }
}

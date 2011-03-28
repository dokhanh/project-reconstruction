package complexTemoins3D;

public class TriangleTemoins {
    private PointTemoins3D p1;
    private PointTemoins3D p2;
    private PointTemoins3D p3;
    
    TriangleTemoins (PointTemoins3D p1, PointTemoins3D p2, PointTemoins3D p3) {
    	this.p1=p1;
    	this.p2=p2;
    	this.p3=p3;
    }
    
    public PointTemoins3D get1() {
    	return p1;
    }
    
    public PointTemoins3D get2() {
    	return p2;
    }
    
    public PointTemoins3D get3() {
    	return p3;
    }
    
    public boolean equals(Object o) {
    	if (o instanceof TriangleTemoins) {
    		TriangleTemoins o1=(TriangleTemoins)o;
    		return (p1==o1.p1 && p2==o1.p2 && p3==o1.p3)||(p1==o1.p2 && p2==o1.p1 && p3==o1.p3);   		
    	}
    	return false;
    }
}

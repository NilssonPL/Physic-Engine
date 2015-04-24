package Structs;

public class Edge {
	public Vec2 point1;
	public Vec2 point2;
	public Vec2 normal;
	
	public Edge(Vec2 p1, Vec2 p2)
	{
		point1 = p1;
		point2 = p2;
		
		Vec2 tmp = p2.sub(p1);
		normal = new Vec2(-tmp.y, tmp.x);
		normal.normalize();
	}
	
}

package Objects;

import java.util.ArrayList;

import Structs.Edge;
import Structs.Vec2;

public class RigidBody extends PhysicObject{
	
	private ArrayList<Vec2> m_points;
	
	public RigidBody(Vec2 position, ArrayList<Vec2> points, PhysicObject po)
	{
		super(po.staticFriction, po.dynamicFriction, po.massData, po.material);
		
		this.m_points = points;
		this.position = position;
	}
	
	public ArrayList<Vec2> GetPoints(boolean applyMatrix)
	{
		if(!applyMatrix)
			return m_points;
		
		ArrayList<Vec2> transformed = new ArrayList<Vec2>();
		
		for(int i = 0; i < m_points.size(); i++)
		{
			Vec2 p = new Vec2(m_points.get(i));
			float sin = (float) Math.sin(orientation);
			float cos = (float) Math.cos(orientation);
			
			p.x = p.x * cos - p.y * sin;
			p.y = p.x * sin + p.y * cos;
			
			transformed.add(m_points.get(i).add(position));
		}
		return transformed;
	}	
}

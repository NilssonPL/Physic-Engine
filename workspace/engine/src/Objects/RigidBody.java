package Objects;

import java.util.ArrayList;

import Structs.Edge;
import Structs.Vec2;

public class RigidBody extends PhysicObject{
	
	private ArrayList<Vec2> m_points;
	private ArrayList<Edge> m_edgeList;
	
	public RigidBody(ArrayList<Vec2> points, PhysicObject po)
	{
		super(po.staticFriction, po.dynamicFriction, po.massData, po.material);
		
		this.m_points = points;
		this.position = new Vec2(0.0f, 0.0f);
		m_edgeList = new ArrayList<Edge>();
		
		for(int i = 0; i < m_points.size(); i++)
		{
			Vec2 point = m_points.get(i);
			m_edgeList.add(new Edge(point, m_points.get((i+1) % m_points.size())));
			this.position = this.position.add(point);
		}
		
		this.position = this.position.mult(1f/m_points.size());
	}
	
	public ArrayList<Edge> GetEdges()
	{
		return m_edgeList;
	}
	
	public ArrayList<Vec2> GetPoints()
	{
		return m_points;
	}
	
	public void Move(float deltaTime)
	{
		if(massData.mass != 0.0f)
		{
			force = force.mult(deltaTime * 0.5f * massData.inverseMass);
			velocity = velocity.add(force);
			
			Vec2 translation = new Vec2(velocity.x * deltaTime, velocity.y * deltaTime);
			position = position.add(translation);
			for(int i = 0; i < m_points.size(); i++)
			{
				m_points.set(i, m_points.get(i).add(translation));
				int k = 0;
				if(Float.isInfinite(m_points.get(i).x))
					k++;
			}
			velocity = velocity.add(force);
		}
		
		force.x = 0.0f;
		force.y = 0.0f;
	}
	
	public void Move(Vec2 move)
	{
		position = position.add(move);
		for(int i = 0; i < m_points.size(); i++)
		{
			m_points.set(i, m_points.get(i).add(move));
			int k = 0;
			if(Float.isInfinite(m_points.get(i).x))
				k++;
				
		}
	}
}

package Logic;

import java.util.ArrayList;

import Objects.AABB;
import Objects.PhysicObject;
import Objects.RigidBody;
import Structs.Edge;
import Structs.Manifold;
import Structs.Vec2;

public class CollisionDetection {

	public static boolean AABBvsAABB(AABB a, AABB b, Manifold m)
	{
		 m.A = a;
		 m.B = b;
		 Vec2 n = new Vec2(b.position.x - a.position.x, b.position.y - a.position.y);
		 
		 float halfExtendA = (a.max.x - a.min.x) * 0.5f;
		 float halfExtendB = (b.max.x - b.min.x) * 0.5f; 
		 
		 float xOverlap = halfExtendA  + halfExtendB- Math.abs(n.x); 
		
		 if(xOverlap > 0)
		 {
			 halfExtendA = (a.max.y - a.min.y) * 0.5f;
			 halfExtendB = (b.max.y - b.min.y) * 0.5f;
			 
			 float yOverlap = halfExtendA + halfExtendB - Math.abs(n.y);
			 
			 if(yOverlap > 0)
			 {
				 if(xOverlap < yOverlap)
				 {
					 if(n.x < 0)
						 m.normal = new Vec2( -1, 0);
					 else
						 m.normal = new Vec2( 1, 0);
					 
					 m.penetration = xOverlap;
					 return true;
				 }
				 else
				 {
					 if(n.y < 0)
						 m.normal = new Vec2(0, -1);
					 else
						 m.normal = new Vec2(0, 1);
					 
					 m.penetration = yOverlap;
					 return true;
				 }
			 }
		 }
		 return false;
		
	}
	
	public static boolean RigidBodyVsRigidBody(RigidBody a, RigidBody b, Manifold m)
	{
		float minDistance = Float.MAX_VALUE;
	
		ArrayList<Edge> edges1 = a.GetEdges();
		ArrayList<Edge> edges2 = b.GetEdges();
		
		m.A = a;
		m.B = b;
	
		for(int i = 0; i < edges1.size() + edges2.size(); i++)
		{
			Edge e;
			if(i < edges1.size())
				e = edges1.get(i);
			else
				e = edges2.get(i - edges1.size());
			
			float minMax1[] = ProjectRigidBody(e.normal, a);
			float minMax2[] = ProjectRigidBody(e.normal, b);
			
			float distance;
			if(minMax1[0] <= minMax2[0])
				distance = minMax2[0] - minMax1[1];
			else
				distance = minMax1[0] - minMax2[1];
			
			
			if(distance >= 0.0f)
				return false;
			
			if(Math.abs(distance) <= minDistance)
			{
				minDistance = Math.abs(distance);
				m.normal = e.normal;
				m.penetration = Math.abs(distance);
			}
			
		}
		Vec2 d = a.position.sub(b.position);
		if(d.dot(m.normal) > 0)
			m.normal = m.normal.mult(-1f);
		a.Move(m.normal.mult(m.penetration).mult(-1f));

		
		return true;
	}
	
	private static float[] ProjectRigidBody(Vec2 axis, RigidBody r)
	{
		float result[] = new float[2];
		ArrayList<Vec2> points = r.GetPoints();
		result[0] = Float.MAX_VALUE;
		result[1] = -Float.MAX_VALUE;
		
		for(int i = 0; i < points.size(); i++)
		{
			float dot = points.get(i).dot(axis);
			result[0] = Math.min(dot, result[0]);		
			result[1] = Math.max(dot, result[1]);
		}
		

		
		return result;
	}
	
	
	public static void SolveCollision(PhysicObject a, PhysicObject b, Manifold m)
	{
		Vec2 rv = b.velocity.sub(a.velocity);
		
		float velocityResponse = rv.dot(m.normal);
		
		if(velocityResponse > 0f)
			return;
		
		float e = Math.min(a.material.restitution, b.material.restitution);
		
		float j = 0;
		float tmp = (a.massData.inverseMass + b.massData.inverseMass);
		if(tmp != 0)
			j = (-(1 + e) * velocityResponse) / tmp;
		else
			return;
		
		Vec2 impulse = m.normal.mult(j);
		
		float massSum = a.massData.mass + b.massData.mass;
		
		float ratio = a.massData.mass / massSum;
		a.velocity = a.velocity.sub(impulse.mult(a.massData.inverseMass * ratio));
		
		ratio = b.massData.mass / massSum;
		b.velocity = b.velocity.add(impulse.mult(b.massData.inverseMass * ratio));
		
		
		rv = b.velocity.sub(a.velocity);
		
		Vec2 tangent = rv.sub(m.normal.mult(rv.dot(m.normal)));
		tangent.normalize();
		
		float jt = -rv.dot(tangent) / tmp;
		
		float mu = (float) Math.sqrt(a.staticFriction * a.staticFriction + b.staticFriction * b.staticFriction);
		
		Vec2 frictionImpulse;
		if(Math.abs(jt) < j * mu)
			frictionImpulse = tangent.mult(jt);
		else
		{
			float dynamicFriction = (float) Math.sqrt(a.dynamicFriction * a.dynamicFriction + b.dynamicFriction * b.dynamicFriction);
			frictionImpulse = tangent.mult(-j * dynamicFriction);
		}
		
		ratio = a.massData.mass / massSum;
		a.velocity = a.velocity.sub(frictionImpulse.mult(a.massData.inverseMass * ratio));
		
		ratio = b.massData.mass / massSum;
		b.velocity = b.velocity.add(frictionImpulse.mult(b.massData.inverseMass * ratio));
	}
	
	public static void PositionalCorrection(PhysicObject a, PhysicObject b, Manifold m)
	{
		  float percent = 0.8f; // usually 20% to 80%
		  float slop = 0.01f; // usually 0.01 to 0.1
		  float correction = (float) Math.max(m.penetration - slop, 0.0f ) / (a.massData.inverseMass + b.massData.inverseMass) * percent;
		  a.Move(m.normal.mult(a.massData.inverseMass * correction).mult(-1f));
		  b.Move(m.normal.mult(b.massData.inverseMass * correction));
		  //a.position = a.position.sub();
		  //b.position = b.position.add(m.normal.mult(b.massData.inverseMass * correction));
	}
}

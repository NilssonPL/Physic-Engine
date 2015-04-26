package Objects;

import Structs.MassData;
import Structs.Material;
import Structs.Vec2;


public class PhysicObject {
	
	public Vec2 velocity;
	public Vec2 force;
	public Vec2 position;
	
	public MassData massData;
	public Material material;
	
	public float staticFriction;
	public float dynamicFriction;
	
	public float orientation;
	public float angularVelocity;
	public float torque;
	
	public PhysicObject(float staticFriction, float dynamiceFriction, MassData massData, Material material) {
		// TODO Auto-generated constructor stub
		this.velocity = new Vec2(0.0f, 0.0f);
		this.force = new Vec2(0.0f, 0.0f);
		
		this.massData = massData;
		this.material = material;
		
		this.staticFriction = staticFriction;
		this.dynamicFriction = dynamiceFriction;
	}
	
	public void AddForce(Vec2 force)
	{
		this.force = this.force.add(force);
	}
	
	public void Move(float deltaTime)
	{
		
		if(massData.mass != 0.0f)
		{
			force = force.mult(deltaTime * 0.5f * massData.inverseMass);
			velocity = velocity.add(force);
			
			position = position.add(new Vec2(velocity.x * deltaTime, velocity.y * deltaTime));
			velocity = velocity.add(force);
		}
		
		force.x = 0.0f;
		force.y = 0.0f;
	}
}

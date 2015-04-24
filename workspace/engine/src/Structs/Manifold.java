package Structs;

import Objects.PhysicObject;

public class Manifold {
	public PhysicObject A;
	public PhysicObject B;
	public float penetration;
	public Vec2 normal;
	
	public Manifold()
	{
		A = null;
		B = null;
		penetration = 20.0f;
	}
}

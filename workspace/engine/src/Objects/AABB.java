package Objects;

import Structs.Vec2;

public class AABB extends PhysicObject
{
	public float width;
	public float height;
	public Vec2 min;
	public Vec2 max;
	
	private float m_halfWidth;
	private float m_halfHeight;
	
	public AABB(Vec2 position, float width, float height, PhysicObject object)
	{
		super(object.staticFriction, object.dynamicFriction, object.massData, object.material);
		this.width = width;
		this.height = height;
		this.position =  position;
		
		m_halfWidth = width * 0.5f;
		m_halfHeight = height * 0.5f;
		this.min = new Vec2(position.x - m_halfWidth, position.y - m_halfHeight);
		this.max = new Vec2(position.x + m_halfWidth, position.y + m_halfHeight);
	}
	
	public void Move(float dt)
	{
		super.Move(dt);
		this.min.x = position.x - m_halfWidth;
		this.min.y = position.y - m_halfHeight;
		this.max.x = position.x + m_halfWidth;
		this.max.y = position.y + m_halfHeight;
	}
}

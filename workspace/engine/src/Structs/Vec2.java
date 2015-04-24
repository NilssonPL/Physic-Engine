package Structs;

public class Vec2 {
	public float x;
	public float y;
	
	public Vec2()
	{
		this.x = 0f;
		this.y = 0f;
	}
	
	public Vec2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vec2(Vec2 vec)
	{
		this.x = vec.x;
		this.y = vec.y;
	}
	
	public Vec2 add(Vec2 vec)
	{
		return new Vec2(x + vec.x, y + vec.y);
	}
	
	public Vec2 sub(Vec2 vec)
	{
		return new Vec2(x - vec.x, y - vec.y);
	}
	
	public Vec2 mult(float mult)
	{
		return new Vec2(this.x * mult, this.y * mult);
	}
	
	
	public void normalize()
	{
		float tmp = x * x + y * y;
		if(tmp == 0.0f)
			return;
		
		float length = (float) Math.sqrt(tmp);
		
		x /= length;
		y /= length;
	}
	
	public float dot(Vec2 vec)
	{
		return x * vec.x + y * vec.y;
	}
	
	
}

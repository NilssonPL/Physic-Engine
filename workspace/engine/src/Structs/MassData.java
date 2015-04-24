package Structs;

public class MassData {

	public float mass;
	public float inverseMass;
	
	public float invertia;
	public float inverse_invertia;
	
	public MassData(float mass, float invertia)
	{
		this.mass = mass;
		this.inverseMass = 0.0f;
		if(mass != 0.0f)
			this.inverseMass = 1f / mass;
			
		this.invertia = invertia;
		this.inverse_invertia = 1 / invertia;
	}
}

package Logic;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Vector;

import Objects.AABB;
import Objects.PhysicObject;
import Objects.RigidBody;
import Structs.Manifold;
import Structs.MassData;
import Structs.Material;
import Structs.Vec2;


public class GameScreen implements Runnable{

	private boolean m_running;
	private Thread m_screen;
	private BufferStrategy m_bufferStrategy;
	private Dimension m_screenDimension;
	private float m_fps;
	private int m_frames;
	private Vector<RigidBody> m_aabbVector;
	
	
	public GameScreen()
	{
		m_screen = null;
		m_bufferStrategy = null;
		m_screenDimension = new Dimension(600, 1000);
		m_fps = 60;
		
		m_aabbVector = new Vector<RigidBody>();
		
		PhysicObject po = new PhysicObject(0.0f, 0.0f, new MassData( 0.0f, (float) Math.random()), new Material((float) Math.random(),  (float) Math.random()));
		int y = 40;
		

		//m_aabbVector.add(new AABB(new Vec2(m_screenDimension.width * 0.5f, m_screenDimension.height), m_screenDimension.width, 0, po));
		//m_aabbVector.add(new AABB(new Vec2(m_screenDimension.width, m_screenDimension.height * 0.5f), 0.0f, m_screenDimension.height, po));
		
		
		for(int i = 0; i < 4; i++)
		{
			int x = 40;
			po = new PhysicObject(1.0f, 1.0f, new MassData( 3-i, (float) Math.random()), new Material((float) Math.random(),  (float) Math.random()));
			for(int j = 0; j < 10; j++)
			{
				ArrayList<Vec2> points = new ArrayList<Vec2>();
				points.add(new Vec2(x, y));
				//points.add(new Vec2(x + 10, y-10));
				points.add(new Vec2(x + 20, y));
				points.add(new Vec2(x + 20, y + 20));
				//points.add(new Vec2(x + 10, y + 30));
				points.add(new Vec2(x, y + 20));
				RigidBody r = new RigidBody(points, po);
				//AABB aabb = new AABB(new Vec2(x, y), 20, 20, po);
				//m_aabbVector.add(aabb);
				//aabb.AddForce(new Vec2((float) Math.random() * 10.0f, (float) Math.random() * 10.0f));
				r.AddForce(new Vec2((float) Math.random() * 10.0f, (float) Math.random() * 10.0f));
				m_aabbVector.add(r);
				x += 40;
			}
			y += 100;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		float time = (1000f / m_fps);
		long nanoTime = System.nanoTime();
		long difference = 0;
		int frames = 0;
		while(m_running)
		{
			long currentTime = System.currentTimeMillis();
			Graphics2D g = (Graphics2D) m_bufferStrategy.getDrawGraphics();
			g.clearRect(0, 0, m_screenDimension.width, m_screenDimension.height);
			
			UpdatePhysics(time * 0.01f);
			
			Render(g);
			g.dispose();
			m_bufferStrategy.show();
			long currentNanoTime = System.nanoTime();
			
			
			//float sleep = (time - (currentTime - System.currentTimeMillis()));
			frames++;
			
			if((currentNanoTime - nanoTime + difference) >= 1000000000L)
			{
				m_frames = frames;
				frames = 0;
				difference = (currentNanoTime - nanoTime) - 1000000000L;
				nanoTime = currentNanoTime;
			}
			
			float timeDifference = System.currentTimeMillis() - currentTime;
			while(timeDifference < time)
			{
				timeDifference = System.currentTimeMillis() - currentTime;
			}
		}
	}
	
	public void UpdatePhysics(float dt)
	{
		for(int k = 0; k < 3; k++)
			for(int i = 0; i < m_aabbVector.size(); i++)
				for(int j = i + 1; j < m_aabbVector.size(); j++)
				{
					//AABB a = m_aabbVector.get(i);
					//AABB b = m_aabbVector.get(j);
					
					RigidBody a = m_aabbVector.get(i);
					RigidBody b = m_aabbVector.get(j);
					
					Manifold m = new Manifold();
					//if(CollisionDetection.AABBvsAABB(a, b, m))
					if(CollisionDetection.RigidBodyVsRigidBody(a, b, m))
					{
						if(k== 0)
							CollisionDetection.SolveCollision(a, b, m);
						CollisionDetection.PositionalCorrection(a, b, m);
					}
				}
		
		for(int i = 0; i < m_aabbVector.size(); i++)
		{
		
			//CollisionDetection.ApplyGravity(m_aabbVector.get(i), new Vec2(0.1f, 0.5f), dt * 0.01f);
			m_aabbVector.get(i).AddForce(new Vec2(0.0f, 9.81f * dt));
			m_aabbVector.get(i).Move(dt);
		}
		
		
	}
	
	public void Render(Graphics2D graphics)
	{
		graphics.setColor(Color.RED);
		graphics.setFont(new Font("Dialog", Font.BOLD, 18));
		graphics.drawString("" + m_frames, 20, 20);
		
		for(int i = 0; i < m_aabbVector.size(); i++)
		{
			graphics.setColor(Color.BLACK);
			//AABB rect = m_aabbVector.get(i);
			//graphics.drawRect((int) rect.min.x, (int) rect.min.y, (int) rect.max.x - (int) rect.min.x, (int) rect.max.x - (int) rect.min.x);
			
			RigidBody r = m_aabbVector.get(i);
			ArrayList<Vec2> points = r.GetPoints();
			Polygon p = new Polygon();
			
			for(int j = 0; j < points.size(); j++)
			{
				Vec2 point = points.get(j);
				p.addPoint((int) Math.round(point.x), (int) Math.round(point.y));
			}
			
			graphics.drawPolygon(p);
		}
	}
	
	public void SetBufferStrategy(BufferStrategy bufferStrategy)
	{
		m_bufferStrategy = bufferStrategy;
	}
	
	public Dimension GetDimension()
	{
		return m_screenDimension;
	}
	
	public void start()
	{
		if(m_running)
			return;
		
		if(m_bufferStrategy == null)
			return;
		
		m_running = true;
		m_screen = new Thread(this);
		m_screen.start();
	}
	
	public void stop()
	{
		if(!m_running)
			return;
		if(m_screen == null)
			return;
		
		m_running = false;
		try {
			m_screen.join();
		} catch (InterruptedException e) {
			System.out.println("Screen Thread can't joined");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

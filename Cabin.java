import java.awt.*;
import java.awt.geom.*;
import java.util.*;

class Cabin implements MyShape
{	
	private Polygon roof;
	private Polygon house;
	private Rectangle chimney;
	private Rectangle door;

	// X, Y and size instance variables
	private int X, Y;
	private int size;

	private boolean isHighlighted;

	public Cabin(int startX, int startY, int sz)
	{
		X = startX;
		Y = startY;
		size = sz;

		setUp();
	}
	
	private void setUp()
	{
		roof = new Polygon();
		roof.addPoint(X,Y);
		roof.addPoint(X - size * 2, Y);
		roof.addPoint(X - size, Y - size);
		house = new Polygon();
		house.addPoint(X, Y);
        house.addPoint(X, Y + size * 2);
		house.addPoint(X - size * 2, Y + size * 2);
        house.addPoint(X - size * 2, Y);

        chimney = new Rectangle(X - size / 2, Y - size, size / 2, size);
		door = new Rectangle(X - size, Y + size, size / 2, size);
	}

	public void highlight(boolean b)
	{
		isHighlighted = b;
	}

	public void draw(Graphics2D g)
	{
		g.setColor(new Color(127, 120, 120));
		if (isHighlighted)
		{
			g.draw(roof);
		}
		else
		{
			g.fill(roof);
		}
		
		g.setColor(new Color(102, 51, 0));
		if (isHighlighted)
		{
			g.draw(house);
		}
		else
		{
			g.fill(house);
		}

		g.setColor(new Color(121, 15, 24));
		if (isHighlighted)
		{
			g.draw(chimney);
		}
		else
		{
			g.fill(chimney);
		}
		
		g.setColor(new Color(0, 0, 0));
		if (isHighlighted)
		{
			g.draw(door);
		}
		else
		{
			g.fill(door);
		}

	}

	public void move(int x, int y)
	{
		int deltaX = x - X;
		int deltaY = y - Y;
		roof.translate(deltaX, deltaY);
		house.translate(deltaX, deltaY);
		chimney.translate(deltaX, deltaY);
		door.translate(deltaX, deltaY);
		X = x;
		Y = y;
	}

	public boolean contains(double x, double y)
	{
		if (roof.contains(x, y))
			return true;
		if (house.contains(x, y))
			return true;
		if (chimney.contains(x, y))
			return true;
		if (door.contains(x, y))
			return true;
		return false;
	}

	public void resize(int newsize)
	{
		size = newsize;
		setUp();
	}

	public String saveData()
	{
		return ("Cabin:" + X + ":" + Y + ":" + size);
	}
}
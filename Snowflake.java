// CS 401 Fall 2014

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

class Snowflake implements MyShape
{
	// Build a Snowflake using composition.
	private Line2D line1, line2, line3, line4;

	private Rectangle2D perimeter;
	private int X, Y;
	private int size;
	private boolean isHighlighted;

	// Create a new Snowflake.
	public Snowflake(int startX, int startY, int sz)
	{
		X = startX;
		Y = startY;
		size = sz;
		line1 = new Line2D.Double(X, Y, X - size, Y - size);
		line2 = new Line2D.Double(X - size, Y, X, Y - size);
		line3 = new Line2D.Double(X - size/ 2, Y + size / 2, X - size / 2, Y - 3 * size / 2);
		line4 = new Line2D.Double(X + size/ 2, Y - size / 2, X - 3 * size / 2, Y - size / 2);
		perimeter = new Rectangle2D.Double(X-size,Y-size,size,size);
		isHighlighted = false;
	}

	public void highlight(boolean b)
	{
		isHighlighted = b;
	}

	// Draw the Snowflake "onto" the Graphics2D parameter that is passed
	// in. This method will be called from a JFrame or JPanel, which
	// is where the Graphics2D object originates.
	public void draw(Graphics2D g)
	{
		if (!isHighlighted)
		{
			g.setColor(Color.white);
		}
		else
		{
			g.setColor(Color.red);
		}
		g.draw(line1);
		g.draw(line2);
		g.draw(line3);
		g.draw(line4);
	}

	// Reset the X and Y coordinates, and update the lines and rectangle to 
	// reflect the new location.
	public void move(int x, int y)
	{
		X = x;
		Y = y;
		line1.setLine(X, Y, X - size, Y - size);
        line2.setLine(X - size, Y, X, Y - size);
        line3.setLine(X - size / 2, Y + size / 2, X - size / 2, Y - 3 * size / 2);
        line4.setLine(X + size / 2, Y - size / 2, X - 3 * size / 2, Y - size / 2);
		perimeter.setFrame(X-size,Y-size,size,size);
	}

	public void resize(int newsize)
	{
		size = newsize;
		move(X, Y);
	}
	
	// Returns whether or not an xy cordinate is in this shape
	public boolean contains(double x, double y)
	{
		return perimeter.contains(x,y);
	}

	// Method to save object to text file
	public String saveData()
	{
		return ("Snowflake:" + X + ":" + Y + ":" + size);
	}
}
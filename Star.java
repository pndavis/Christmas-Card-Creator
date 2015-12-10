// CS 401 Fall 2014

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

class Star implements MyShape
{
    private Polygon up;
    private Polygon down;

    private Line2D line1, line2, line3, line4;

    // X, Y and size instance variables
    private int X, Y;
    private int size;

    private boolean isHighlighted;

    // Create a new Star.
    public Star(int startX, int startY, int sz)
    {
        X = startX;
        Y = startY;
        size = sz;

        setUp();
    }

    private void setUp()
    {
        up = new Polygon();
        up.addPoint(X, Y);
        up.addPoint(X - size, Y);
        up.addPoint(X - size / 2, Y - size);
        down = new Polygon();
        down.addPoint(X, Y);
        down.addPoint(X - size, Y);
        down.addPoint(X - size / 2, Y + size);

        int nX = X-size/2;
        line1 = new Line2D.Double(nX, Y, nX - size, Y - size);
        line2 = new Line2D.Double(nX, Y, nX - size, Y + size);
        line3 = new Line2D.Double(nX, Y, nX + size, Y - size);
        line4 = new Line2D.Double(nX, Y, nX + size, Y + size);
    }

    public void highlight(boolean b)
    {
        isHighlighted = b;
    }

    public void draw(Graphics2D g)
    {
        g.setColor(new Color(255, 227, 65));
        if (isHighlighted)
        {
            g.draw(up);
        }
        else
        {
            g.fill(up);
        }
        if (isHighlighted)
        {
            g.draw(down);
        }
        else
        {
            g.fill(down);
        }

        g.draw(line1);
        g.draw(line2);
        g.draw(line3);
        g.draw(line4);
    }

    public void move(int x, int y)
    {
        int deltaX = x - X;
        int deltaY = y - Y;
        up.translate(deltaX, deltaY);
        down.translate(deltaX, deltaY);
        X = x;
        Y = y;

        int nX = X-size/2;
        line1.setLine(nX, Y, nX - size, Y - size);
        line2.setLine(nX, Y, nX - size, Y + size);
        line3.setLine(nX, Y, nX + size, Y - size);
        line4.setLine(nX, Y, nX + size, Y + size);
    }

    public void resize(int newsize)
    {
        size = newsize;
        setUp();
    }

    // Returns whether or not an xy cordinate is in this shape
    public boolean contains(double x, double y)
    {
        if (up.contains(x,y))
            return true;
        if (down.contains(x,y))
            return true;
        return false;
    }  

    // Method to save object to text file
    public String saveData()
    {
        return ("Star:" + X + ":" + Y + ":" + size);
    }
}
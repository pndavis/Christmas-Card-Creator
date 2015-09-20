// CS 401 Fall 2014
// Tree class as another implementation of the MyShape interface.
// This class also uses composition, with 2 Polygons being the primary
// components of a Tree object.  For more information on Polygons, see
// the Java API.

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

class Star implements MyShape
{
    // Represent a Tree in two parts -- a Polygon for the top part
    // (the branches) and another Polygon for the trunk.  Since the
    // trunk is rectangular, a Rectangle2D could have been used, but
    // to keep consistent (especially with the move() method) I used
    // Polygon objects for both.
    private Polygon up;
    private Polygon down;

    private Line2D line1, line2, line3, line4;

    // X, Y and size instance variables
    private int X, Y;
    private int size;

    private boolean isHighlighted;

    // Create a new Tree object.  Note how the Polygons are built,
    // adding one point at a time to each.  If you plot the points out
    // on paper you will see how the shapes are formed.  This method
    // uses a setUp method as shown below to allow for the Tree to
    // be regenerated internally (i.e. outside the constructor)
    public Star(int startX, int startY, int sz)
    {
        X = startX;
        Y = startY;
        size = sz;

        setUp();
    }

    // Create the actual parts of the Tree.  For your shapes you
    // will likely use trial and error to get nice looking results
    // (I used a LOT of trial and error for mine).
    private void setUp()
    {
        up = new Polygon();
        up.addPoint(X,Y);
        up.addPoint(X-size,Y);
        up.addPoint(X-size/2,Y-5*size/5);
        down = new Polygon();
        down.addPoint(X,Y);
        down.addPoint(X-size,Y);
        down.addPoint(X-size/2,Y+5*size/5);

        int nX = X-size/2;
        line1 = new Line2D.Double(nX,Y,nX-size,Y-size);
        line2 = new Line2D.Double(nX,Y,nX-size,Y+size);
        line3 = new Line2D.Double(nX,Y,nX+size,Y-size);
        line4 = new Line2D.Double(nX,Y,nX+size,Y+size);




    }

    public void highlight(boolean b)
    {
        isHighlighted = b;
    }

    // The Polygon class can also be drawn with a predefined method in
    // the Graphics2D class.  There are two versions of this method:
    // 1) draw() which only draws the outline of the shape
    // 2) fill() which draws a solid shape
    // In this class the draw() method is used when the object is
    // highlighted.
    // The colors chosen are RGB colors I looked up on the Web.  Take a
    // look and use colors you like for your shapes.
    public void draw(Graphics2D g)
    {
        g.setColor(new Color(255, 227, 65));
        if (isHighlighted)
            g.draw(up);
        else
            g.fill(up);
        if (isHighlighted)
            g.draw(down);
        else
            g.fill(down);
        g.draw(line1);
        g.draw(line2);
        g.draw(line3);
        g.draw(line4);
    }

    // Looking at the API, we see that Polygon has a translate() method
    // which can be useful to us.  All we have to do is calculate the
    // difference of the new (x,y) and the old (X,Y) and then call
    // translate() for both parts of the tree.
    public void move(int x, int y)
    {
        int deltaX = x - X;
        int deltaY = y - Y;
        up.translate(deltaX, deltaY);
        down.translate(deltaX, deltaY);
        X = x;
        Y = y;

        int nX = X-size/2;
        line1.setLine(nX,Y,nX-size,Y-size);
        line2.setLine(nX,Y,nX-size,Y+size);
        line3.setLine(nX,Y,nX+size,Y-size);
        line4.setLine(nX,Y,nX+size,Y+size);
    }

    // Polygon also has a contains() method, so this method is also
    // simple
    public boolean contains(double x, double y)
    {
        if (up.contains(x,y))
            return true;
        if (down.contains(x,y))
            return true;
        return false;
    }

    // The move() method for the Polygons that are in Tree are not
    // reconfigured like in Snowflake, so we can't use the trick used
    // there.  Instead, we just change the size and call setUp() to
    // regenerate all of the objects.
    public void resize(int newsize)
    {
        size = newsize;
        setUp();
    }

    // Note again the format
    public String saveData()
    {
        return ("Star:" + X + ":" + Y + ":" + size);
    }
}
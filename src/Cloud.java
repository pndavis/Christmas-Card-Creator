//Cloud.java

//import javafx.scene.shape.Circle;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

class Cloud implements MyShape
{
    private Ellipse2D.Double c1;
    private Ellipse2D.Double c2;
    private Ellipse2D.Double c3;
    private Ellipse2D.Double c4;
    private Ellipse2D.Double c5;

    // X, Y and size instance variables
    private int X, Y;
    private int size;

    private boolean isHighlighted;

    // Create a new Tree object.  Note how the Polygons are built,
    // adding one point at a time to each.  If you plot the points out
    // on paper you will see how the shapes are formed.  This method
    // uses a setUp method as shown below to allow for the Tree to
    // be regenerated internally (i.e. outside the constructor)
    public Cloud(int startX, int startY, int sz)
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
        c1 = new Ellipse2D.Double(X,Y,size,size);
        c2 = new Ellipse2D.Double(X-(size/4),Y+(size/4),size,size/3);
        c3 = new Ellipse2D.Double(X+(size/4),Y-(size/3),size,size);
        c4 = new Ellipse2D.Double(X-(size/4),Y+(size/4),size/2,size/2);
        c5 = new Ellipse2D.Double(X+(size/4),Y+(size/4),size/3,size/3);
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
        g.setColor(new Color(255, 255, 255));
        if (isHighlighted)
            g.draw(c1);
        else
            g.fill(c1);
        if (isHighlighted)
            g.draw(c2);
        else
            g.fill(c2);
        if (isHighlighted)
            g.draw(c3);
        else
            g.fill(c3);
        if (isHighlighted)
            g.draw(c4);
        else
            g.fill(c4);
        if (isHighlighted)
            g.draw(c5);
        else
            g.fill(c5);
    }

    // Looking at the API, we see that Polygon has a translate() method
    // which can be useful to us.  All we have to do is calculate the
    // difference of the new (x,y) and the old (X,Y) and then call
    // translate() for both parts of the tree.
    public void move(int x, int y)
    {
        X = x;
        Y = y;
        c1.setFrame(X,Y,size,size);
        c2.setFrame(X-(size/4),Y+(size/4),size,size/3);
        c3.setFrame(X+(size/4),Y-(size/3),size,size);
        c4.setFrame(X-(size/4),Y+(size/4),size/2,size/2);
        c5.setFrame(X+(size/4),Y+(size/4),size/3,size/3);
    }

    // Polygon also has a contains() method, so this method is also
    // simple
    public boolean contains(double x, double y)
    {
        if (c1.contains(x,y))
            return true;
        if (c2.contains(x,y))
            return true;
        if (c3.contains(x,y))
            return true;
        if (c4.contains(x,y))
            return true;
        if (c5.contains(x,y))
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
        return ("Cloud:" + X + ":" + Y + ":" + size);
    }
}
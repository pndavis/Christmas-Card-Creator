//Cloud.java
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

    public Cloud(int startX, int startY, int sz)
    {
        X = startX;
        Y = startY;
        size = sz;

        setUp();
    }

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

    public void resize(int newsize)
    {
        size = newsize;
        setUp();
    }

    public String saveData()
    {
        return ("Cloud:" + X + ":" + Y + ":" + size);
    }
}
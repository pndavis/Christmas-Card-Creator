// CS 401 Fall 2014
// Paul Davis
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.awt.print.*;

// Create enum types that will be useful in the program
enum Figures {TREE,SNOWFLAKE,GREETING,CABIN,CLOUD,STAR}
enum Mode {NONE,DRAW,SELECTED,MOVING}

// Code extracted from Oracle Java Example programs.  See link below for full code:
// http://docs.oracle.com/javase/tutorial/2d/printing/examples/PrintUIWindow.java
class thePrintPanel implements Printable
{
	JPanel panelToPrint;

	public int print(Graphics g, PageFormat pf, int page) throws
			PrinterException
	{
		if (page > 0) { /* We have only one page, and 'page' is zero-based */
			return NO_SUCH_PAGE;
		}

        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform t = new AffineTransform();
		t.scale(0.9, 0.9);
		g2d.transform(t);
		g2d.translate(pf.getImageableX(), pf.getImageableY());

        /* Now print the window and its visible contents */
		panelToPrint.printAll(g);

        /* tell the caller that this page is part of the printed document */
		return PAGE_EXISTS;
	}

	public thePrintPanel(JPanel p)
	{
		panelToPrint = p;
	}
}

public class assig5
{
	private ShapePanel drawPanel;
	private JPanel buttonPanel;
	private JButton makeShape;
	private JRadioButton makeTree, makeFlake, makeGreet, makeCabin, makeCloud, makeStar;
	private ButtonGroup shapeGroup;
	private Figures currShape;
	private JLabel msg;
	private JMenuBar theBar;
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenuItem newFile, openFile, saveFile, saveAs,endProgram, printScene, printJpg;
	private JMenuItem eCut, eCopy, ePaste;
	private JPopupMenu popper;
	private JMenuItem delete, resize;
	private JFrame theWindow;

	// This ArrayList is used to store the shapes in the program.
	// It is specified to be of type MyShape, so objects of any class
	// that implements the MyShape interface can be stored in here.
	// See Section 7.13 in your text for more info on ArrayList.
	private ArrayList<MyShape> shapeList;
    private ArrayList<MyShape> containsList;
    private int containsIndex;
	private MyShape newShape;

	private String fileName = "";
	private boolean saved = false;
    private String cutCopy;
    private boolean fromcut = false;

	public assig5() throws IOException
	{
		drawPanel = new ShapePanel(640, 480);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 2));

		makeShape = new JButton("Click to Draw");

		ButtonHandler bhandler = new ButtonHandler();
		makeShape.addActionListener(bhandler);

		buttonPanel.add(makeShape);
		msg = new JLabel("");
		buttonPanel.add(msg);

		makeTree = new JRadioButton("Tree", false);
		makeFlake = new JRadioButton("Snowflake", true);
		makeGreet = new JRadioButton("Greeting", false);
		makeCabin = new JRadioButton("Cabin", false);
		makeCloud = new JRadioButton("Cloud", false);
        makeStar = new JRadioButton("Star", false);

		RadioHandler rhandler = new RadioHandler();
		makeTree.addItemListener(rhandler);
		makeFlake.addItemListener(rhandler);
		makeGreet.addItemListener(rhandler);
		makeCabin.addItemListener(rhandler);
		makeCloud.addItemListener(rhandler);
        makeStar.addItemListener(rhandler);

		buttonPanel.add(makeFlake);
		buttonPanel.add(makeTree);
		buttonPanel.add(makeGreet);
		buttonPanel.add(makeCabin);
		buttonPanel.add(makeCloud);
        buttonPanel.add(makeStar);

		// A ButtonGroup allows a set of JRadioButtons to be associated
		// together such that only one can be selected at a time
		shapeGroup = new ButtonGroup();
		shapeGroup.add(makeFlake);
		shapeGroup.add(makeTree);
		shapeGroup.add(makeGreet);
		shapeGroup.add(makeCabin);
		shapeGroup.add(makeCloud);
        shapeGroup.add(makeStar);

		currShape = Figures.SNOWFLAKE;
		drawPanel.setMode(Mode.NONE);

		theWindow = new JFrame("Assignment 5");
		Container c = theWindow.getContentPane();
		drawPanel.setBackground(Color.lightGray);
		c.add(drawPanel, BorderLayout.NORTH);
		c.add(buttonPanel, BorderLayout.SOUTH);

		// Note how the menu is created.  First we make a JMenuBar, then
		// we put a JMenu in it, then we put JMenuItems in the JMenu.  We
		// can have multiple JMenus if we like.  JMenuItems generate
		// ActionEvents, just like JButtons, so we just have to link an
		// ActionListener to them.
		theBar = new JMenuBar();
		theWindow.setJMenuBar(theBar);
		fileMenu = new JMenu("File");
		theBar.add(fileMenu);
		newFile = new JMenuItem("New");
		openFile = new JMenuItem("Open");
		saveFile = new JMenuItem("Save");
		saveAs = new JMenuItem("Save As");
        printJpg = new JMenuItem("Save as JPG");
		printScene = new JMenuItem("Print");
		endProgram = new JMenuItem("Exit");
		fileMenu.add(newFile);
		fileMenu.add(openFile);
		fileMenu.add(saveFile);
		fileMenu.add(saveAs);
        fileMenu.add(printJpg);
		fileMenu.add(printScene);
		fileMenu.add(endProgram);
		newFile.addActionListener(bhandler);
		openFile.addActionListener(bhandler);
		saveFile.addActionListener(bhandler);
		saveAs.addActionListener(bhandler);
        printJpg.addActionListener(bhandler);
		printScene.addActionListener(bhandler);
		endProgram.addActionListener(bhandler);


		editMenu = new JMenu("Edit");
		theBar.add(editMenu);
		eCut = new JMenuItem("Cut");
		eCopy = new JMenuItem("Copy");
		ePaste = new JMenuItem("Paste");
		editMenu.add(eCut);
		editMenu.add(eCopy);
		editMenu.add(ePaste);
		eCut.addActionListener(bhandler);
		eCopy.addActionListener(bhandler);
		ePaste.addActionListener(bhandler);
        eCut.setEnabled(false);
        eCopy.setEnabled(false);
		ePaste.setEnabled(false);

		// JPopupMenu() also holds JMenuItems.  To see how it is actually
		// brought out, see the mouseReleased() method in the ShapePanel class
		// below.
		popper = new JPopupMenu();
		delete = new JMenuItem("Delete");
		resize = new JMenuItem("Resize");
		delete.addActionListener(bhandler);
		resize.addActionListener(bhandler);
		popper.add(delete);
		popper.add(resize);

		theWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		theWindow.pack();
		theWindow.setVisible(true);
        theWindow.setLocationRelativeTo(null);
        saved = true;
	}

	public static void main(String [] args) throws IOException
	{
		new assig5();
	}

	// See Section 12.4 for information on JRadioButtons.  Note that the
	// text uses ActionListeners to handle JRadioButtons.  Clicking on
	// a JRadioButton actually generates both an ActionEvent and an
	// ItemEvent.  I am using the ItemEvent here.  To handle the event,
	// all I am doing is changing a state variable that will affect the
	// MouseListener in the ShapePanel.
	private class RadioHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			if (e.getSource() == makeTree)
				currShape = Figures.TREE;
			else if (e.getSource() == makeFlake)
				currShape = Figures.SNOWFLAKE;
			else if (e.getSource() == makeGreet)
				currShape = Figures.GREETING;
			else if (e.getSource() == makeCabin)
				currShape = Figures.CABIN;
			else if (e.getSource() == makeCloud)
				currShape = Figures.CLOUD;
            else if (e.getSource() == makeStar)
                currShape = Figures.STAR;
		}
	}

	// Note how the makeShape button and moveIt menu item are handled 
	// -- we again simply set the state in the panel so that the mouse will 
	// actually do the work.  The state needs to be set back in the mouse
	// listener.
	private class ButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == makeShape)
			{
				if (makeShape.getText().equals("Click to Draw"))
				{
					drawPanel.setMode(Mode.DRAW);
					msg.setText("Position new shapes with mouse");
					makeShape.setText("Click to Edit");
                    eCut.setEnabled(false);
                    eCopy.setEnabled(false);
				}
				else
				{
					drawPanel.setMode(Mode.NONE);
					msg.setText("Edit shapes with mouse ");
					makeShape.setText("Click to Draw");
                    eCut.setEnabled(false);
                    eCopy.setEnabled(false);
				}
			}
			else if (e.getSource() == delete)
			{
				boolean ans = drawPanel.deleteSelected();
				if (ans)
				{
					msg.setText("Shape deleted");
					drawPanel.repaint();
				}
			}
			else if (e.getSource() == resize) {
                String newSize = null;
                boolean ans = false;
                try
                {
                    newSize = JOptionPane.showInputDialog(theWindow, "Enter new size [Cancel for no change]");
                    ans = drawPanel.resizeSelected(Integer.parseInt(newSize));
                }
				catch (NumberFormatException f)
                {}
                if (ans)
                {
                    msg.setText("Shape resized");
                    drawPanel.repaint();
                }
			}

			else if (e.getSource() == newFile)
			{
                if(!saved)
                {
                    int no = JOptionPane.showConfirmDialog(null, "Save Data?", "Save?", JOptionPane.YES_NO_OPTION);
                    if (no == JOptionPane.YES_OPTION) {
                        saveData();
                    }
                }
                fileName = "";
				shapeList = new ArrayList<MyShape>();
                drawPanel.newFile();
				drawPanel.repaint();
                saved = true;
			}
			else if (e.getSource() == openFile)
			{
                if(!saved)
                {
                    int no = JOptionPane.showConfirmDialog(null, "Save Data?", "Save?", JOptionPane.YES_NO_OPTION);
                    if (no == JOptionPane.YES_OPTION) {
                        saveData();
                    }
                }
                boolean opening = false;
                Scanner dataIn = null;
                String newFileName = "";
                while (!opening) {
                    try
                    {
                        newFileName = JOptionPane.showInputDialog(theWindow, "What file do you want to open?");
                        File inData = new File(newFileName);
                        dataIn = new Scanner(inData);
                        opening = true;
                    }
                    catch (NullPointerException f)
                    {
                        return;
                    }
                    catch (IOException z) {
                        opening = false;
                    }
                }
                fileName = newFileName;

                shapeList = new ArrayList<MyShape>();
                int saveLength = Integer.parseInt(dataIn.nextLine());
                String n;
                int x;
                int y;
                int size;
                String text;

                for(int count = 0; count < saveLength; count++)
                {
                    String total = dataIn.nextLine();
                    total = total + ":a";
                    String delims = ":";
                    String[] tokens = total.split(delims);
                    n = tokens[0];
                    x = Integer.parseInt(tokens[1]);
                    y =  Integer.parseInt(tokens[2]);
                    size = Integer.parseInt(tokens[3]);
                    text = tokens[4];
                    if(n.equals("Snowflake"))
                    {
                        shapeList.add(new Snowflake(x, y, size));
                    }
                    else if(n.equals("Tree"))
                    {
                        shapeList.add(new Tree(x, y, size));
                    }
                    else if(n.equals("Greeting"))
                    {
                        shapeList.add(new Greeting(x, y, size, text));
                    }
                    else if(n.equals("Cabin"))
                    {
                        shapeList.add(new Cabin(x, y, size));
                    }
                    else if(n.equals("Cloud"))
                    {
                        shapeList.add(new Cloud(x, y, size));
                    }
                    else if(n.equals("Star"))
                    {
                        shapeList.add(new Star(x, y, size));
                    }
                }
                dataIn.close();
                drawPanel.repaint();
                saved = true;
			}
			else if (e.getSource() == saveFile)
			{
                saveData();
			}
			else if (e.getSource() == saveAs)
			{
				String newFileName = JOptionPane.showInputDialog(theWindow, "What is the name of this file?");
				PrintWriter fileOut = null;
				try
				{
					fileOut = new PrintWriter(new FileOutputStream(fileName,false));
				}
				catch (IOException q)
				{}
                catch (NullPointerException z)
                {
                    return;
                }
                fileName = newFileName;
                saved = true;


				fileOut.println(shapeList.size());
				for(int i = 0; i < shapeList.size(); i++)
				{
					fileOut.println(shapeList.get(i).saveData());
				}
				fileOut.close();

			}
            else if (e.getSource() == printJpg)
            {
                StringBuilder a = new StringBuilder();

                BufferedImage img = getScreenShot(drawPanel);
                a.append(JOptionPane.showInputDialog(theWindow, "What is the name of this file?"));
                if(!a.toString().contains(".jpg"))
                {
                    a.append(".jpg");
                }
                try
                {
                    ImageIO.write(img, "png", new File(a.toString()));
                }
                catch (IOException f)
                {
                    System.out.print("Didn't print jpg");
                }
            }
			else if (e.getSource() == printScene)
			{
				Printable thePPanel = new thePrintPanel(drawPanel);
				PrinterJob job = PrinterJob.getPrinterJob();
				job.setPrintable(thePPanel);
				boolean ok = job.printDialog();
				if (ok)
				{
					try {
						job.print();
					}
					catch (PrinterException ex) {
             		/* The job did not successfully complete */
					}
				}
			}
			else if (e.getSource() == endProgram)
			{
                String output;
                if(fileName.equals(""))
                {
                    output = "Save current file?";
                }
                else
                {
                    output = "Save file " + fileName + "?";
                }
                if(!saved)
                {
                    int no = JOptionPane.showConfirmDialog(null, output, "Save?", JOptionPane.YES_NO_OPTION);
                    if (no == JOptionPane.YES_OPTION) {
                        saveData();
                    }
                }
				System.exit(0);
			}


			else if (e.getSource() == eCut)
			{
                String temp = drawPanel.cutSelected();
                if(temp != null) {
                    cutCopy = temp;
                    fromcut = true;
                    msg.setText("Shape cut");
                    drawPanel.repaint();
                }
                eCut.setEnabled(false);
                eCopy.setEnabled(false);
			}
			else if (e.getSource() == eCopy)
			{
                String temp = drawPanel.copySelected();
                if(temp != null) {
                    cutCopy = temp;
                    fromcut = false;
                    msg.setText("Shape copied");
                    drawPanel.repaint();
                }
			}
			else if (e.getSource() == ePaste)
			{
                drawPanel.pasteSelected(cutCopy);
                msg.setText("Shape pasted");
                drawPanel.repaint();
                eCut.setEnabled(false);
                eCopy.setEnabled(false);
			}
		}
	}
    public static BufferedImage getScreenShot(Component component) {

        BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
        component.paint(image.getGraphics());
        return image;
    }
    public void saveData()
    {
        if(fileName.equals(""))
        {
            fileName = JOptionPane.showInputDialog(theWindow, "What is the name of this file?");
        }
        PrintWriter fileOut = null;
        try
        {
            fileOut = new PrintWriter(new FileOutputStream(fileName, false));
        }
        catch (IOException q)
        {
            System.out.println("IO Exception thrown");
        }

        //System.out.println(shapeList.size());;
        int size = shapeList.size();
        fileOut.println(size);
        for(int i = 0; i < shapeList.size(); i++)
        {
            fileOut.println(shapeList.get(i).saveData());
        }
        fileOut.close();
        saved = true;
    }

	// Here we are extending JPanel.  This way we can use all of the
	// properties of JPanel (including generating MouseEvents) and also
	// add new instance data and methods, as shown below.  Since this is
	// an inner class, it can access instance variables from the A5Help
	// class if necessary.
	private class ShapePanel extends JPanel
	{

		// These instance variables are used to store the desired size
		// of the panel.  See method getPreferredSize() below.
		private int prefwid, prefht;

		// Store index of the selected MyShape.  This allows the Shape
		// to be moved and updated.
		private int selindex;

		// Keep track of positions where mouse is moved on the display.
		// This is used by mouse event handlers when moving the shapes.
		private int x1, y1, x2, y2;

		private boolean popped; // has popup menu been activated?

		private Mode mode;   // Keep track of the current Mode

		public ShapePanel (int pwid, int pht)
		{
			shapeList = new ArrayList<MyShape>(); // create empty ArrayList
			selindex = -1;

			prefwid = pwid;	// values used by getPreferredSize method below
			prefht = pht;   // (which is called implicitly).  This enables
			// the JPanel to request the room that it needs.
			// However, the JFrame is not required to honor
			// that request.

			setOpaque(true);// Paint all pixels here (See API)

			setBackground(Color.lightGray);

			addMouseListener(new MyMouseListener());
			addMouseMotionListener(new MyMover());
			popped = false;
		}  // end of constructor


		// This class is extending MouseAdapter.  MouseAdapter is a predefined
		// class that implements MouseListener in a trivial way (i.e. none of
		// the methods actually do anything).  Extending MouseAdapter allows
		// a programmer to implement only the MouseListener methods that
		// he/she needs but still satisfy the interface (recall that to
		// implement an interface one must implement ALL of the methods in the
		// interface -- in this case I do not need 3 of the 5 MouseListener
		// methods)

		// Note that there is a lot of logic in this class to test for the
		// different state conditions of the program.  The idea is that clicking
		// on and releasing the mouse will do different things at different 
		// times in the program execution.  As an alternative, you could in fact
		// have MouseListeners for different circumstances (ex: for being
		// in DRAW mode vs. being in NONE mode).  In this case, you could
		// actually swap the listeners in and out as appropriate using the 
		// removeMouseListener method in addition to the addMouseListener method
		// for the JPanel.

		private class MyMouseListener extends MouseAdapter
		{
			public void mousePressed(MouseEvent e)
			{
				x1 = e.getX();  // store where mouse is when clicked
				y1 = e.getY();

				if (!e.isPopupTrigger() && (mode == Mode.NONE || mode == Mode.SELECTED)) // left click and
				{												    // either NONE or
					if (selindex >= 0)								// SELECTED mode
					{
						unSelect();			// unselect previous shape
						mode = Mode.NONE;
                        eCut.setEnabled(false);
                        eCopy.setEnabled(false);
					}
					selindex = getSelected(x1, y1);  // find shape mouse is

                    /*
                    for(MyShape x : shapeList)
                    {
                        if(x.contains(x1, y1))
                        {

                        }
                    }*/



					// clicked on
					if (selindex >= 0)
					{
                        eCut.setEnabled(true);
                        eCopy.setEnabled(true);
						mode = Mode.SELECTED;  	// Now in SELECTED mode for shape

						// Check for double-click.  If so, show dialog to update text of
						// the current text shape (will do nothing if shape is not a MyText)
						MyShape curr = shapeList.get(selindex);
						if (curr instanceof MyText && e.getClickCount() == 2)
						{
							String newText = JOptionPane.showInputDialog(theWindow,
									"Enter new text [Cancel for no change]");
							if (newText != null)
								((MyText) curr).setText(newText);
						}
					}
					repaint();	//  Make sure updates are redrawn
				}
				else if (e.isPopupTrigger() && selindex >= 0)  // if button is
				{								               // the popup menu
					popper.show(ShapePanel.this, x1, y1);      // trigger, show
					popped = true;							   // popup menu
				}
			}

			public void mouseReleased(MouseEvent e)
			{
				if (mode == Mode.DRAW) // in DRAW mode, create the new Shape
				{					   // and add it to the list of Shapes.  In this
					// case we need to distinguish between the
					// shapes since we are calling constructors
					if (currShape == Figures.TREE)
					{
						newShape = new Tree(x1,y1,50);
					}
					else if (currShape == Figures.SNOWFLAKE)
					{
						newShape = new Snowflake(x1,y1,10);
					}
					else if (currShape == Figures.GREETING)
					{
						newShape = new Greeting(x1,y1,30);
					}
					else if (currShape == Figures.CABIN)
					{
						newShape = new Cabin(x1,y1,30);
					}
					else if (currShape == Figures.CLOUD)
					{
						newShape = new Cloud(x1,y1,30);
					}
                    else if (currShape == Figures.STAR)
                    {
                        newShape = new Star(x1,y1,30);
                    }
					addShape(newShape);
                    saved = false;
				}
				// In MOVING mode, set mode back to NONE and unselect shape (since 
				// the move is finished when we release the mouse).
				else if (mode == Mode.MOVING)
				{
					mode = Mode.NONE;
					unSelect();
					makeShape.setEnabled(true);
					repaint();
                    saved = false;
                    eCut.setEnabled(false);
                    eCopy.setEnabled(false);
				}
				else if (e.isPopupTrigger() && selindex >= 0) // if button is
				{							// the popup menu trigger, show the
					popper.show(ShapePanel.this, x1, y1); // popup menu
				}
				popped = false;  // unset popped since mouse is being released
			}
		}

		// the MouseMotionAdapter has the same idea as the MouseAdapter
		// above, but with only 2 methods.  The method not implemented
		// here is mouseMoved.  The difference between the two is whether or
		// not the mouse is pressed at the time.  Since we require a "click and
		// hold" to move our objects, we are using mouseDragged and not
		// mouseMoved.
		private class MyMover extends MouseMotionAdapter
		{
			public void mouseDragged(MouseEvent e)
			{
				x2 = e.getX();   // store where mouse is now
				y2 = e.getY();

				// Note how easy moving the shapes is, since the "work"
				// is done within the various shape classes.  All we do
				// here is call the appropriate method.  However, we don't 
				// want to accidentally move the selected shape with a right click
				// so we make sure the popup menu has not been activated.
				if ((mode == Mode.SELECTED || mode == Mode.MOVING) && !popped)
				{
					MyShape s = shapeList.get(selindex);
					mode = Mode.MOVING;
					s.move(x2, y2);
                    saved = false;
                    eCut.setEnabled(false);
                    eCopy.setEnabled(false);
				}
				repaint();	// Repaint screen to show updates
                saved = false;
			}
		}

		// Check to see if point (x,y) is within any of the shapes.  If
		// so, select that shape and highlight it so user can see.  Again,
		// note that we do not care which shape we are selecting here --
		// it only matters that it has the MyShape interface methods.
		// This version of getSelected() always considers the shapes from
		// beginning to end of the ArrayList.  Thus, if a shape is "under"
		// or "within" a shape that was previously created, it will not
		// be possible to select the "inner" shape.  In your assignment you
		// must redo this method so that it allows all shapes to be selected.
		// Think about how you would do this.
		private int getSelected(double x, double y)
		{
			for (int i = 0; i < shapeList.size(); i++)
			{
				if (shapeList.get(i).contains(x, y))
				{
					shapeList.get(i).highlight(true);
					return i;
				}
			}
			return -1;
		}

		public void unSelect()
		{
			if (selindex >= 0)
			{
				shapeList.get(selindex).highlight(false);
				selindex = -1;
			}
		}
        public void newFile()
        {
            selindex = -1;
        }

		public boolean deleteSelected()
		{
			if (selindex >= 0)
			{
				shapeList.remove(selindex);
				selindex = -1;
                saved = false;
				return true;
			}
			else return false;
		}
        public boolean resizeSelected(int x)
        {
            if (selindex >= 0)
            {
                shapeList.get(selindex).resize(x);
                selindex = -1;
                saved = false;
                return true;
            }
            else return false;
        }
        public String cutSelected()
        {
            if (selindex >= 0)
            {
                ePaste.setEnabled(true);
                String send = shapeList.get(selindex).saveData();
                shapeList.remove(selindex);
                selindex = -1;
                saved = false;
                return send;
            }
            else
                return null;

        }

        public String copySelected()
        {
            if (selindex >= 0)
            {
                ePaste.setEnabled(true);
                String send = shapeList.get(selindex).saveData();
                return send;
            }
            else return null;
        }
        public void pasteSelected(String total)
        {
            total = total + ":a";
            String delims = ":";
            String[] tokens = total.split(delims);
            String n = tokens[0];
            int x = Integer.parseInt(tokens[1]);
            int y =  Integer.parseInt(tokens[2]);
            int size = Integer.parseInt(tokens[3]);
            String text = tokens[4];
            if(fromcut)
            {
                x = x1;
                y = y1;
            }

            if(n.equals("Snowflake"))
            {
                shapeList.add(new Snowflake(x, y, size));
            }
            else if(n.equals("Tree"))
            {
                shapeList.add(new Tree(x, y, size));
            }
            if(n.equals("Greeting"))
            {
                shapeList.add(new Greeting(x, y, size, text));
            }
            if(n.equals("Cabin"))
            {
                shapeList.add(new Cabin(x, y, size));
            }
            if(n.equals("Cloud"))
            {
                shapeList.add(new Cloud(x, y, size));
            }
            if(n.equals("Star"))
            {
                shapeList.add(new Star(x, y, size));
            }
        }

		public void setMode(Mode newMode)            // set Mode
		{
			mode = newMode;
		}

		private void addShape(MyShape newshape)      // Add shape
		{
			shapeList.add(newshape);
			repaint();	// repaint so we can see new shape
		}

		// Method called implicitly by the JFrame to determine how much
		// space this JPanel wants.  Be sure to include this method in
		// your program so that your panel will be sized correctly.
		public Dimension getPreferredSize()
		{
			return new Dimension(prefwid, prefht);
		}

		// This method enables the shapes to be seen.  Note the parameter,
		// which is implicitly passed.  To draw the shapes, we in turn
		// call the draw() method for each shape.  The real work is in the draw()
		// method for each MyShape
		public void paintComponent (Graphics g)
		{
			super.paintComponent(g);         // don't forget this line!
			Graphics2D g2d = (Graphics2D) g;
			for (int i = 0; i < shapeList.size(); i++)
			{
				shapeList.get(i).draw(g2d);
			}
		}
	} // end of ShapePanel
}
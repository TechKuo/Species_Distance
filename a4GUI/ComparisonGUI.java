package a4GUI;

import java.awt.Container; 

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;

/** Base class for implementing the species comparison GUI.
 * <p>
 * You will subclass ComparisonGUI to make your comparison GUI.
 * <p>
 * The GUI consists of a table with a picture of each species and two larger pictures
 * that are intended to show the currently selected species and its closest relative,
 * as computed by your species distance algorithm.
 * <p>
 *  When the user clicks on a cell in the table, method onSelectCell is called.
 *  Your subclass should override <i>onSelectCell</i> to exhibit the behavior described
 *  in the assignment. 
 * <p>
 * You will refer to cells by their number, 0 through n-1, where n is the number of species
 * given to the ComparisonGUI's constructor.
 * <p>
 * Read the documentation for each method to learn what you can do with this class.
 * @author Lonnie Princehouse. Gries repalaced Grid by Box and simplified in a number of
 * ways, including eliminating two anonymous classes.
 */
public class ComparisonGUI {
    /** Number of species. */
    private int n;

    // Species table dimensions
    protected final int columns= 5;  // Number of columns: Hard-coded
    protected int rows;  // Depends on n and columns

    protected Cell cells[]; // One cell object for each cell in the table

    // image and label for selected species
    protected JLabel selectedLabel= new JLabel("Selected: ");
    protected JLabel closestRelatedLabel= new JLabel("Closest relative: ");

    // image and label for closest related species
    protected JLabel selectedImage= new JLabel();
    protected JLabel closestRelatedImage= new JLabel();

    protected final static int CELL_WIDTH= 90;
    protected final static int CELL_HEIGHT= 90;

    protected JFrame frame= new JFrame("Species Comparison Interface");  // Top-level window
    protected Container topPane= frame.getContentPane(); // Content pane of frame
    protected Box boxArray= new Box(BoxLayout.Y_AXIS); // Array of images, as a Y-axis Box. Goes in frame west
    protected Box comparisonBox= new Box(BoxLayout.Y_AXIS); // Selected and closest info. Goes in frame east

    /** Constructor: an instance with an expected table of n cells.
     * <p>
     * After constructing an instance of ComparisonGUI,
     * call setCellImage for each cell (numbered 0 through n-1) to place
     * images in the cells.
     * Throw a RuntimeException if n <= 0.
     */
    public ComparisonGUI(int n) {   
        try {
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { }
        if (n <= 0)
            throw new RuntimeException("ComparisonGUI constructor must have n > 0");
        this.n= n;

        setup();
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    /** Configure the GUI */
    private void setup() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fixComparisonBox();  // Fill the right-side panel of components
        topPane.add(comparisonBox, BorderLayout.EAST);

        topPane.add(new JLabel("   "), BorderLayout.CENTER);  // Place the space to separate west/east

        fixBoxArray();   // Fill the box for the array of images
        topPane.add(boxArray, BorderLayout.WEST);
    }

    /** Make up BoxArray with cells and listeners.
        The n images appear as an array with column columns.
        Each is a ScalingImageButtons, and they are placed in cells[0..n-1] */
    public void fixBoxArray() {

        rows= (int) Math.ceil(n / (double) columns);  // number of rows in GUI for the images
        // Create the row boxes and add them to boxArray
        Box[] brows= new Box[rows];
        for (int i= 0; i < rows; i++) {
            brows[i]= new Box(BoxLayout.X_AXIS);
            boxArray.add(brows[i]);
        }

        // Initialize the n cells with imageButtons, place them in the GUI, and
        // register action listeners and component listeners for them
        ImageClick cellListener= new ImageClick(); // A listener for the images in cells
        ComponentListenerClass componentListener= new ComponentListenerClass(); // A component listener for the images in cells
        Dimension dim= new Dimension(CELL_WIDTH, CELL_HEIGHT);
        cells = new Cell[n];       // Array of cells
        for (int i= 0; i < n; i++) {
            // Create the imageButton and set its size preferred size
            ScalingImageButton imageButton= new ScalingImageButton();
            imageButton.setMinimumSize(dim);
            imageButton.setPreferredSize(dim);
            imageButton.setMaximumSize(dim);

            // Register the imageButton as an action listener and a component listener
            imageButton.addActionListener(cellListener);
            imageButton.addComponentListener(componentListener);

            // Add the imageButton to row r --depends on the number of columns
            brows[i/columns].add(imageButton);

            // Finally create array element cells[i]
            cells[i]= new Cell(imageButton, i);
        }

        // Complete the last row with rigid areas
        int lastCol= (n-1) % columns;
        // System.out.println("row " + rows + " lastCol " + lastCol);
        for (int h= lastCol + 1; h < columns; h++) {
            brows[rows-1].add(Box.createRigidArea(new Dimension(CELL_WIDTH, CELL_HEIGHT)));
        }
    }

    /** Optional: For extending the user interface.
     *  Returns the top-level frame (A.K.A. top-level window). 
     *  The frame's content panel (getFrame().getContentPanel()) uses a BorderLayout manager
     *  Components added will appear below the existing GUI.
     * @return The top-level frame.
     */
    public JFrame getFrame() {
        return frame;
    }

    /** Return the number of columns in the species table */
    public int getColumns() {
        return columns;
    }

    /** Return the number of rows in the species table */ 
    public int getRows() {
        return rows;
    }

    /** Add to field comparisonBox the stuff that goes into the right panel, i.e.
        the label and image for the selected species and the label and image for
        its closest species.
        (Override this method to accomplish this task.) */
    public void fixComparisonBox() {
    }

    /** Return the cell at index i. */
    private Cell getCell(int i) {
        return cells[i];
    }

    /** Place the image for species number i and the image for its closest relative
        in the east panel. Change the background colors of the species to
        indicate distance from species number i.
        (Override this method to accomplish that task.)
     */
    public void onSelectCell(int  i) { 
        System.out.println("onSelectCell: User clicked cell number " + i);
    }

    /** Load an image from file named f into cell number i. 
     * @param cell Cell number
     * @param filename Path to the image file
     * @throws InvalidCellNumberException 
     */
    public void setCellImage(int i, String f) throws InvalidCellNumberException {
        checkCell(i);
        try {
            getCell(i).setImageFile(f);
        } catch (IOException e) {
            System.err.println("Image not found: " + f);
        }
    }

    /** Throw InvalidCellNumberException if i is not in range 0..n-1. */
    private void checkCell(int i) throws InvalidCellNumberException {
        if (i < 0 || i >= n) {
            throw new InvalidCellNumberException();
        }
    }
    /**
     * Set the background color of cell number i.
     * Colors are specified as (r,g,b) triples between zero and one.
     * @param i Cell number
     * @param red Red component. Must be between 0 and 1.
     * @param green Green component. Must be between 0 and 1.
     * @param blue Blue component. Must be between 0 and 1.
     * @throws InvalidColorException 
     * @throws InvalidCellNumberException 
     */
    public void setCellColor(int i, double red, double green, double blue)
            throws InvalidColorException, InvalidCellNumberException {
        checkCell(i);
        if (red < 0 || red > 1) 
            throw new InvalidColorException();
        if (green < 0 || green > 1) 
            throw new InvalidColorException();
        if (blue < 0 || blue > 1) 
            throw new InvalidColorException();

        getCell(i).setColor( (int) (red * 255), (int) (255 * green), (int) (255 * blue));
    }

    /** Set the background color of cell i to color c.
     * Throw and InvalidCellNumberException if i is not the number of a cell.
     */
    public void setCellColor(int i, java.awt.Color c) throws InvalidCellNumberException {
        checkCell(i);
        getCell(i).setColor(new Color(c));
    }

    /**
     *  Set the name and image file of the currently selected species to n
     *  and f, respectively.  
     *  These will appear under "Selected:" at the right of the window.
     */
    public void setSelectedInfo(String n, String f) {
        selectedImage.setIcon(new ImageIcon(f));
        selectedLabel.setText("Selected: " + n + "  ");
        frame.repaint();
    }

    /** Set the name and image file of the current most closely related species
     *  to n and f, respectively.
     *  These will appear under "Closest relative:" at the right of the window.
     */
    public void setClosestRelativeInfo(String n, String f) {
        closestRelatedImage.setIcon(new ImageIcon(f));
        closestRelatedLabel.setText("Closest relative: " + n  + "  ");
    }

    private ColorThread colorthread;

    /** Start the GUI's main event loop. Call this after instantiating your GUI. */
    public void run() {
        colorthread= new ColorThread(cells);
        colorthread.start();  
    }

    /** One instance of this class is used to process clicks on an image
     * in the array of Cells in the GUI. Clicking selects the image as
     * the one to appear in the right panel, as the Selected on. Do what
     * is necessary to have that happen.
     */
    public class ImageClick implements ActionListener {
        /** Process click of button on a Cell */
        public void actionPerformed(ActionEvent e) {
            Object ob= e.getSource();
            if (!(ob instanceof ScalingImageButton)) {
                throw new RuntimeException("Big error in ImageClick.actionPerformed." +
                        " Method should not have been called.");
            }

            // Determine cells[i] in which this object appears and
            // call onSelectedCell(i) to process it. Then pack and repaint the frame.
            ScalingImageButton sib= (ScalingImageButton) ob;
            for (int i= 0; i < n; i= i+1) {
                if (sib == cells[i].image) {
                    onSelectCell(i);
                    frame.pack();
                    frame.repaint();
                }
            }
        }
    }

    /** One instance of this class is used to process clicks on an image
     * in the array of Cells in the GUI. Clicking selects the image as
     * the one to appear in the right panel, as the Selected on. Do what
     * is necessary to have that happen.
     */
    public class ComponentListenerClass implements ComponentListener {
        public void componentHidden(ComponentEvent arg0) {}
        public void componentMoved(ComponentEvent arg0) {}
        public void componentResized(ComponentEvent arg0) {
            int width= (columns + 1) * CELL_WIDTH; 
            int height= rows * CELL_HEIGHT + 100;
            ScalingImageButton imageButton= (ScalingImageButton) (arg0.getSource());
            imageButton.setScaledImage(width/columns,height/rows);
            frame.repaint();
            frame.pack();
        }
        public void componentShown(ComponentEvent arg0) {}
    }

}

package a4GUI;

import java.io.IOException;

/** Used internally by ComparisonGUI. 
    Manages the contents of a cell in the species grid. */
public class Cell {

    protected int i;   // The cell's number in the image array
    protected Color currentColor;  // The current color
    protected Color targetColor;   // The color that it is being changed to
    protected ScalingImageButton image; // The cell's image
    
    // seconds for color change
    private final static double COLOR_CHANGE_TIME= 0.2;

    /** Constructor: an instance with button imp and initially white.
        It is cell number i in the array of Cells. */
    public Cell(ScalingImageButton imp, int i) {
        image= imp;
        this.i= i;
        currentColor= new Color(1,1,1);
        targetColor= currentColor;
    }
    
    /** Return the number of this cell in the array of Cells. */
    public int getI() {
        return i;
    }

    /** Change this instance's image to filename --load it from the file. */
    public void setImageFile(String filename) throws IOException {
        image.loadImage(filename);
    }

    /** Set the background color to c. */
    private void setColorImmediate(Color c) {
        currentColor = c;
        image.setBackground(c.toAwt());
    }

    /** Set this color to (red, blue, green), all in the range 0..255. */
    public void setColor(int red, int green, int blue) {
        setColor(new Color(red,green,blue));
    }

    /** Set the color to white. */
    protected void clearColor() {
        setColor(null);
    }
    
    /** Set color of cell to c --to white if c is null */
    public void setColor(Color c) {
        if(c == null) 
            c= new Color(255,255,255);
        targetColor= c;
    }

    /** Advance this instance's color toward the target color. */
    protected void advanceColor(double dt) {
        if (currentColor.equals(targetColor)) 
            return;
        setColorImmediate(currentColor.blend(targetColor, dt / COLOR_CHANGE_TIME));
    }
}

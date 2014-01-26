package a4GUI;

/** Used internally by ComparisonGUI for color fading, etc. A Color object
    represents a color with three components, red, green, and blue, each
    a double value between 0.0 and 1.0 inclusive. Black is with all
    components 0.0; white with call components 1.10.
    
    A constructor allows red, green, blue components in the range 0.255,
    A third constructor has as parameter a value of type java.awt.Color.
 */
public class Color {
    private double r;
    private double g;
    private double b;

    /** Constructor: An instance with red, green, and blue values,
        each a double in the range 0..1.   */
    public Color(double red, double green, double blue) {
        r= red;
        g= green;
        b= blue;
    }

    /** Constructor: An instance with red, green, and blue values,
        each an int in the range 0..255.   */
    public Color(int red, int green, int blue) {
        r= (double) red / 255.0;
        g= (double) green / 255.0;
        b= (double) blue / 255.0;
    }

    /** Constructor: An instance with color given by color.   */
    public Color(java.awt.Color color) {
        r= (double) color.getRed() / 255.0;
        g= (double) color.getGreen() / 255.0;
        b= (double) color.getBlue() / 255.0;
    }

    /** Return true iff this color and other have the same r g b components. */
    public boolean equals(Color other) {
        return r == other.r  &&  g == other.g  &&  b == other.b;
    }
    
    /** Return the red component of this color, in the range 0..255. */
    public int getRedInt() {
        return (int) (255 * r);
    }

    /** Return the green component of this color, in the range 0..255. */
    public int getGreenInt() {
        return (int) (255 * g);
    }

    /** Return the blue component of this color, in the range 0..255. */
    public int getBlueInt() {
        return (int) (255 * b);
    }

    /** Return a blend of this color and color other. 
        If d < 0, 0.0 is used for d; if d > 1, 1.0 is used for d.
        The blend is a linear scale, with d = 0 giving this color and 1 giving other.
     */
    protected Color blend(Color other, double d) {
        d = Math.max(Math.min(1.0, d), 0.0);
        return new Color(r * (1-d) + other.r * d, 
                g * (1-d) + other.g * d,
                b * (1-d) + other.b * d);  
    }
    
    /** Return this color as a java.awt.Color object. */
    public java.awt.Color toAwt() {
        return new java.awt.Color((float)r, (float) g, (float) b);
    }

}

package a4GUI;

/** Thrown when an invalid cell number is given to one of the GUI's cell methods. */
public class InvalidCellNumberException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /** Constructor: an instance with empty message */
    public InvalidCellNumberException() {
    }

    /** Constructor: aan instance with message m */
    public InvalidCellNumberException(String m) {
        super(m);
    }

    public String toString() {
        return "Cell index is outside the number of species allocated";
    }
}

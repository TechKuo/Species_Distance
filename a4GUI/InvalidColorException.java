package a4GUI;

/** Thrown when a color component is outside of the valid range (zero to one, inclusive) */
public class InvalidColorException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidColorException() {
    }

    public InvalidColorException(String m) {
        super(m);
    }

    public String toString() {
        return "Illegal color component value. Color components must be between 0 and 1 inclusive.";
    }
}
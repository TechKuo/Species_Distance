/** An instance represents a gene.
 *         An object wraps a String containing the gene. We use a new class
 *         instead of class String so that methods can provide validation.<br><br>
 * 
 *         Throughout your program, if you know that a condition should
 *         be true, check it. If it is false but you are expecting it
 *         to be true, either you have bugs or don't understand your program.<br><br>
 * 
 *         Conditions to check in this class:<br>
 *         (1) Genes contain only the acceptable characters (C T G A).<br>
 *         (2) Genes have at least one character.<br>
 *         (3) Genes may not contain the end delimiter TGC (A gene may
 *             contain the beginning delimiter CAG).<br><br>
 *
 *         If these conditions are not met, the method is expected to
 *         throw an IllegalArgumentException<br>
 * 
 *         It makes the most sense to put these checks in one place, probably
 *         procedure setDNA.
 *         Resist the temptation to put these checks in other places,
 *         like SpeciesReader or DNAParser. See
 *         http://en.wikipedia.org/wiki/Single_responsibility_principle.<br><br>
 * 
 *         Having this class brings the benefit of type safety to the rest of
 *         your program. You're less likely to pass an attribute ("LatinName")
 *         or filename ("biscuit.dat") to a method expecting a Gene if you have
 *         a dedicated Gene class.<br>
 *
 * @author CS2110 Course Staff
 */
public abstract class Gene {

    /** Constructor: uninitialized Gene */
    public Gene() {}

    /** Set the DNA associated with this gene to dna.
	    Throw an IllegalArgumentException if the dna is not well-formed
	    (see comment on the class declaration itself). */
    public abstract void setDNA(String dna) throws IllegalArgumentException;

    /** Get the DNA associated with this gene */
    public abstract String getDNA();

    /** return the dna associated with this gene */
    public @Override abstract String toString();

    /** return true iff ob is a Gene and wraps the same gene as this one. */
    public @Override boolean equals(Object ob) {
        return ob instanceof Gene   &&
                getDNA().equals(((Gene) ob).getDNA());
    }

    /**Return a hash based on the DNA, but don't hash to exactly the same value as
	   the string DNA to help avoid colliding with an object of a different type.
     */
    public @Override int hashCode() {
        return getDNA().hashCode() + 1;
    }
}

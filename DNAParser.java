import java.util.List;

/** Interface contains a method to extract genes from a raw DNA string
 * <pre>
 *         Parse a DNA string, ignoring (discarding) junk DNA.
 * 
Recall that a DNA string is a list of characters from the
 *         set {C,T,G,A}. A gene is a substring that starts with the
 *         3-character sequence CAG and ends with TGC. CAG could
 *         occur inside the gene, and this is perfectly legal. Similarly,
 *         TGC could occur between genes (in the junk), and this is also legal.
 *         
 *         A typical DNA string will contain between 5 and 15 genes, and a
 *         typical gene will contain 150 to 500 characters. No gene
 *         will  be longer than 999 characters, but we recommend that
 *         you write your code so that violations of these rough limits
 *         won't cause  problems or buggy behavior.
 * 
 *         The ideal tool for parsing genes from DNA is regular expressions. For
 *         this assignment, you don't have to know how to use them, but in the
 *         real world a regex is the fastest and most secure solution.
 *         www.Rubular.com is a great tool for developing regular expressions.
 * </pre>
 * 
 * @author CS2110 Course Staff
 */
public interface DNAParser {

	/** Set the DNA to be parsed to dna 
	 *  @param DNA: A string of amino acids C,T,G,A  */
	public void setDNA(String dna);

	/**
     * Return the list of genes that are in the DNA string previously by setDNA().
     * <pre>
     * Rules for parsing the DNA string to find the genes are as follows:
     * 
     * 1. Parse from left to right.
     * 2. In the DNA sequence, a gene is delimited by the start sequence CAG
     *    and end sequence TGC. Therefore, when the start sequence CAG is
     *    encountered, begin a gene, and the next occurrence of the end
     *    sequence TGC terminates the gene.
     * 3. Inside a gene, CAG does not start another gene.
     * 4. The start and end sequences are NOT part of the gene; they
     *       just delimit it.
     * 5. A gene may be empty. If an empty gene is encountered, ignore it --do not add it to the list
     * 6. If there is a start of a gene (CAG) but no following end delimiter (TGC),
     *    the substring beginning at the start is NOT considered to be a gene and
     *    is discarded. In this case, there are no more genes to extract.
     * 7. The DNA string may contain duplicate genes. Function parse
     *    does not remove duplicates from the list it constructs.
     * </pre>
     * @return A list of genes found in the DNA string, in the order in which they appear.
     */
	public List<Gene> parse();
}

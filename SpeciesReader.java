import java.io.File;
import java.io.IOException;

/** Contains methods to read a CS2110 species data file.
 * See the A0 handout for details. Here is an example data file:
 * <pre>
 * Name="Strats Squirrel" 
 * LatinName="Leapus Longus" 
 * Image="Strats_Squirrel.png"
 * Weight=2400
 * Collected-by="Barbara"
 * DNA="CTTAAATTGAGGTGAGCAACTGACGCGAATTGATTGGTTGTGA"
 * </pre>
 * The ideal tool for parsing a line of a data file is regular expressions.
 * For this assignment, you don't have to know how to use them,
 * but in the real world a regex is the fastest and most secure solution.
 * www.Rubular.com is a great tool for developing regular expressions.
 * 
 * @author CS2110 Course Staff
 * 
 */
public abstract class SpeciesReader {

    /** Return a Species object for file named f.
        Throw an IO Exception if problems are encountered reading the file. */
	public abstract Species readSpecies(String f) throws IOException;

	/** Return a Species object for file f.
    Throw an IO Exception if problems are encountered reading the file. */
	public abstract Species readSpecies(File f) throws IOException;

}

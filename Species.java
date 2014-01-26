import java.util.Collection;

/**
 * Class Species represents a species record read from a data file.
 * The attributes maintained in a Species object are discussed in the
 * assignment A0 handout. They are: given Name, LatinName, ImageFilename,
 * Weight, Collected-by, and DNA.
 * 
 * @author CS2110 Course Staff
 * 
 */
public abstract class Species {

	/** Default constructor. Does nothing. */
	public Species() {
	}

	/** Set the Species' common name to n. */
	public abstract void setName(String n);

	/** Get the Species' common name. */
	public abstract String getName();

	/** Set the Species' scientific name to n. */
	public abstract void setLatinName(String n);

	/** Get the Species' scientific name. */
	public abstract String getLatinName();
	
	/** Set the Species' weight to w.
        Precondition: w > 0 */
	public abstract void setWeight(int weight);
	
	/** Get the Species' weight. */
	public abstract int getWeight();
	
	/** Set the Species' collector to c. */
	public abstract void setCollector(String c);
	
	/** Get the Species' collector. */
	public abstract String getCollector();
	

	/** Set the Species' DNA to dna.
	 * Note: For our purposes, a Species DNA will be set only once.  */
	public abstract void setDNA(String dna);

	/**Get the Species' DNA.  */
	public abstract String getDNA();

	/** Set the filename to point to imageFilename. */
	public abstract void setImageFilename(String imageFilename);

	/** Get the filename pointing to the Species' image. */
	public abstract String getImageFilename();

	/**
	 * Get the Species' genome. A genome is the set of genes parsed from raw
	 * DNA. The return value of getGenome SHOULD NOT contain duplicate genes,
	 * even if genes are duplicated in the raw DNA.
	 * 
	 * You can parse the genome when setDNA is called ("eager"), or when
	 * getGenome is called ("lazy").
	 * 
	 * If the latter, avoid parsing the genome every time getGenome is called.
	 * 
	 * @return A duplicate-free set of genes found in this Species' DNA.
	 */
	public abstract Collection<Gene> getGenome();
        
}

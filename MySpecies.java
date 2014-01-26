import java.util.*;

/** An instance maintains information about a species. */
public class MySpecies extends Species implements Comparable<MySpecies> {

    private String name; // The species' common name
    private String latinName; // The species scientific name
    private int weight; // The species weight. > 0
    private String collector; // The collector --first person to collect the species
    private String dna;      // The species' dna
    private String imageFilename; // Name of the file that contains the image
    private HashSet<Gene> speciesGenes; // If not null, contains the set of genes for dna
    private Gene[] allGenesOfAllSpecies; // List of all genes in all species, in sorted order
    public int[] speciesGenesIndexes; // sorted indexes (in allGenesOfAllSpecies) of this species' genes
    private int[][] geneDistanceMatrix;

    /** g is a list of all genes in all species. geneDistanceMatrix is the corresponding
       matrix of distances between genes. Save information about them to
       make later use of gene-distance matrix efficient. */
    public void saveGeneInformation(Gene[] g, int[][] geneDistanceMatrix) {
        getGenome();
        allGenesOfAllSpecies= g;
        this.geneDistanceMatrix= geneDistanceMatrix;
        Object[] garray= speciesGenes.toArray(); 
        Arrays.sort(garray);
        speciesGenesIndexes= new int[speciesGenes.size()];
        for (int k= 0; k < garray.length; k= k+1) {
            speciesGenesIndexes[k]= indexOfGene(garray[k]);
        }
    }

    /** Return the sorted array of indexes of this species' genes. */
    public int[] getSpeciesGenesIndexes() {
        return speciesGenesIndexes;
    }

    /** Return index of g in field allGenesOfAllSpecies.
        Throw RuntimeException if not in. */
    private int indexOfGene(Object g) {
        for (int k= 0; k < allGenesOfAllSpecies.length; k= k+1) {
            if (g.equals(allGenesOfAllSpecies[k])) return k;
        }
        throw new RuntimeException("Gene " + g + " is not in allGenesOfAllSpecies");
    }

    /** Constructor: an instance with default values in fields. */
    public MySpecies() {
    }

    /** Set the Species' common name to n. */
    public void setName(String n) {
        name= n;
    }

    /** Return the Species' common name. */
    public  String getName() {
        return name;
    }

    /** Set the Species' scientific name to n. */
    public void setLatinName(String n) {
        latinName= n;
    }

    /** Return the Species' scientific name. */
    public String getLatinName() {
        return latinName;
    }

    /** Set the Species' weight to w.
      Precondition: w > 0 */
    public  void setWeight(int w) {
        assert w > 0;
        weight= w;
    }

    /** Return the Species' weight. */
    public int getWeight() {
        return weight;
    }

    /** Set the Species' collector to c. */
    public void setCollector(String c) {
        collector= c;
    }

    /** Return the Species' collector. */
    public String getCollector() {
        return collector;
    }

    /** Set the Species' DNA to dna. */
    public void setDNA(String dna) {
        this.dna= dna;
        speciesGenes= null;
    }

    /** Return the Species' DNA. */
    public String getDNA() {
        return dna;
    }

    /** Set the filename to point to imageFilename. */
    public void setImageFilename(String imageFilename) {
        this.imageFilename= imageFilename;
    }

    /** Return the filename pointing to the Species' image. */
    public String getImageFilename() {
        return imageFilename;
    }

    /** Return the distance between this species and sp, acc.
        to algorithm described in the A3 handout. */
    public int getDistance(Species sp) {
        return (distance(this, sp) + distance(sp, this)) / 2;
    }
    
    /** Return the distance between this species and sp, acc.
    to algorithm described in the A3 handout. */
public int getDistanceSlow(Species sp) {
    return (distanceSlow(this, sp) + distanceSlow(sp, this)) / 2;
}
    
    /** Return the distance from Species A to Species B */
    public static int distanceSlow(Species A, Species B) {
        MySpecies mA= (MySpecies) A;
        MySpecies mB= (MySpecies) B;
        int total= 0;
        for (Gene a :  mA.speciesGenes) {
            // Add to total the min distance from a to genes in mB
            int min= Integer.MAX_VALUE;
            for (Gene b : mB.speciesGenes) {
                min= Math.min(min, ((MyGene)a).getDistance(b));
            }
            total= total + min;
        }
        return total;
    }

    /** Return the distance from Species A to Species B */
    public static int distance(Species A, Species B) {
        MySpecies mA= (MySpecies) A;
        MySpecies mB= (MySpecies) B;
        int total= 0;
        for (int a= 0; a < mA.speciesGenesIndexes.length; a= a+1) {

            // Add to total the min distance from a to genes in mB
            int min= Integer.MAX_VALUE;
            int ia= mA.speciesGenesIndexes[a];
            for (int b= 0; b < mB.speciesGenesIndexes.length; b= b+1) {
                int ib= mB.speciesGenesIndexes[b];
                min= Math.min(min, mA.geneDistanceMatrix[ia][ib]);
            }
            total= total + min;
        }
        return total;
    }

    /** Return a negative int, 0, or a positive int depending on whether
        this species' name comes before, is the same as, or comes after
        sp's name in dictionary ordering. */
    public @Override int compareTo(MySpecies sp) {
        return getName().compareTo(sp.getName());
    }

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
    public Collection<Gene> getGenome() {
        if (speciesGenes == null) {
            MyDNAParser p= new MyDNAParser();
            p.setDNA(dna);
            speciesGenes= new HashSet<Gene>(p.parse());
        }
        return speciesGenes;
    }

    /** return representation of this instance. Contains all
      attributes --but for DNA, only the first 30 characters */
    public @Override String  toString()  {
        String d= getDNA();
        if (d != null) {
            d= d.substring(0, Math.min(d.length(),30));
        } 
        return "Name=\"" + getName() + "\"" +
        "\nLatinName=\"" + getLatinName() + "\"" +
        "\nImageFilename=\"" + getImageFilename() + "\"" +
        "\nWeight=" + getWeight() +
        "\nCollected-by=\"" + getCollector() + "\"" +
        "\nDNA=\"" + d + "\"";
    }
}

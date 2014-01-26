
import java.awt.Color;
import java.io.*;
import java.util.*;
import java.io.IOException;
import java.util.*;

/** Class to run assignment A3.  */
public class Main {

    /** Do what is required in the A3 handout, but restrict it to two species.
     */
    public static void main(String[] args) throws IOException {
        Species[] species1= getSpecies();
        for (int h= 0; h < species1.length; h= h+1) {
            System.out.println("species1[" + h + "]: " + species1[h].getName());
        }
        
        Species[] species= species1;
        
        Date timeStart= new Date();  // We calculate the time for everything except reading in species.
        Arrays.sort(species);

        Gene[] genes= getGenes(species);
        Arrays.sort(genes);
        //printHeader(genes);

        int[][] geneDistance= distanceMatrix(genes);
        //print(geneDistance, 'G');
        
        // Precalculate for later distance calculation
        for (Species sp : species) {
            MySpecies msp= (MySpecies) sp;
            msp.saveGeneInformation(genes, geneDistance);
            //System.out.println(msp.getName() + " " + toString(msp.getSpeciesGenesIndexes()));
        }
        Date timeEnd= new Date();
        System.out.println("time for everything except species distance matrix: " + (timeEnd.getTime() - timeStart.getTime()));
        
        
        // Calculate a distance matrix using no precalculation
        //int[][] speciesDistance= distanceMatrixSlow(species);
        
        timeStart= new Date();
        int[][] speciesDistance= distanceMatrix(species);
        timeEnd= new Date();
        System.out.println("species distanceMatrix time: " + (timeEnd.getTime() - timeStart.getTime()));
        
        print(speciesDistance, 'S');
    }

    /** Return an array of all Species in SpeciesData */
    public static Species[] getSpecies() throws IOException {
        MySpeciesReader msr= new MySpeciesReader();
        Vector<Species> species= new Vector<Species>();

        File f= new File("SpeciesData");
        File[] files= f.listFiles(); 
        for (File p : files) {
            String name= p.getName();
            if (name.endsWith(".dat")) {
                species.add(msr.readSpecies("SpeciesData/" + name));
            }
        }

        Species[] a= new Species[species.size()];
        for (int k= 0; k < a.length; k= k+1) {
            a[k]= species.get(k);
        }

        return a;
    }

    /** Return an array of all genes (with no duplicates) in species */
    public static Gene[] getGenes(Species[] species) {
        HashSet<Gene> geneSet= new HashSet<Gene>();
        for (Species sp : species) {
            MyDNAParser p= new MyDNAParser(sp.getDNA());
            List<Gene> geneList= p.parse();
            for (Gene g : geneList) {
                geneSet.add(g);
            }
        }

        Gene[] gArray= new Gene[geneSet.size()];
        int i= 0;
        for (Gene ge : geneSet) {
            gArray[i]= ge;
            i= i+1;
        }

        return gArray;
    }

    /** Return a distance matrix for species */
    public static int[][] distanceMatrix(Species[] species) {
        int[][] matrix= new int[species.length][species.length];

        for (int h= 0; h < species.length; h= h+1) {
            // Computer matrix[h..][h..]
            for (int k= h; k < species.length; k= k+1) {
                int dist= ((MySpecies)species[h]).getDistance(species[k]);
                matrix[h][k]= dist;
                matrix[k][h]= dist;
            }
        }

        return matrix;
    }
    
    /** Return a distance matrix for species */
    public static int[][] distanceMatrixSlow(Species[] species) {
        int[][] matrix= new int[species.length][species.length];

        for (int h= 0; h < species.length; h= h+1) {
            // Computer matrix[h..][h..]
            for (int k= h; k < species.length; k= k+1) {
                int dist= ((MySpecies)species[h]).getDistanceSlow(species[k]);
                matrix[h][k]= dist;
                matrix[k][h]= dist;
            }
        }

        return matrix;
    }

    /** Compute and return a distance matrix for genes */
    public static int[][] distanceMatrix(Gene[] genes) {
        int[][] matrix= new int[genes.length][genes.length];

        for (int r= 0; r < matrix.length; r= r+1) {
            // Compute the distances for matrix[r..][r..]
            for (int c= r; c < matrix.length; c= c+1) {
                int d= ((MyGene)(genes[r])).getDistance(genes[c]);
                matrix[r][c]= d;
                matrix[c][r]= d;
            }
        }
        return matrix;
    }



    /** Read the data files in args, create Species files for
     * them in species, and add their genes to genes */
    private static void processFiles(String[] args,
            Vector<Species> species, List<Gene> allGenes) throws IOException {

        SpeciesReader reader = new MySpeciesReader();
        for(String filename : args) {
            //System.out.printf("Reading %s...\n", filename);

            Species sp = reader.readSpecies(filename);
            species.add(sp);

            MyDNAParser p= new MyDNAParser(sp.getDNA());
            List<Gene> g= p.parse();
            allGenes.addAll(g);
        }


    }

    /** Print the genes in g, one per line, as described in A2 handout. */
    public static void printHeader(Gene[] genes) {
        for (int r= 0; r < genes.length; r= r+1) {
            System.out.println("G" + r + "=" + genes[r]);
        }

    }

    /** Print the species in s, one per line, as described in A3 handout,
        giving only its name and its list of genes */
    public static void printHeader(Species[] species, Gene[] genes) {
        for (int k= 0; k < species.length; k= k+1) {
            Collection<Gene> genome= species[k].getGenome();
            Gene[] array= new Gene[genome.size()]; 
            int h= 0;
            for (Gene g : genome) {
                array[h]= g;
                h= h+1;
            }
            Arrays.sort(array);  

            String res= "S" + k + "=" + species[k].getName() + ": Genes [";

            for (int i= 0; i < array.length; i= i+1) {
                if (i > 0) res= res + ", ";
                res= res + getGene(array[i], genes);
            }

            System.out.println(res + "]");
        }
    }

    /** = index of gene g in genes (-1 if none) */
    public static int getGene(Gene g, Gene[] genes) {
        for (int k= 0; k < genes.length; k= k+1) {
            if (g.equals(genes[k])) return k;
        }
        return -1;
    }

    /** Print matrix m, with a header row, as per the A0 handout, with
        GS being 'G' or 'S' for gene or species */
    public static void print(int[][] m, char GS) {
        // Print the matrix header
        String row= "";
        for (int r= 0; r < m.length; r= r+1) {
            if (r < 10)
                row= row + "    " + GS + r;
            else if (r < 100) row= row + "   " + GS + r;
            else row= row + "  " + GS + r;
        }
        System.out.println(row);

        // Print the matrix
        for (int r= 0; r < m.length; r= r+1) {
            row= "";
            for (int c= 0; c < m[r].length; c= c+1) {
                row= row + String.format("%6d", m[r][c]);
            }
            row= row + "     // " + GS + r;
            System.out.println(row);
        }
    }

    /** Return a sorted list genes in hs */
    public static Gene[] getGenes(HashSet<Gene> hs) {
        int n= hs.size();      // The number of genes
        Gene[] geneArray= new Gene[n];
        hs.toArray(geneArray);
        Arrays.sort(geneArray);
        return geneArray;
    }

    /** Print diagnostic information about Species s */
    private static void printSpecies(Species s) {
        System.out.printf("    Name: %s\n", s.getName());
        System.out.printf("    LatinName: %s\n", s.getLatinName());
        System.out.printf("    ImageFilename: %s\n", s.getImageFilename());
        System.out.printf("    DNA length: %d characters\n", s.getDNA().length());
        printGenome(s.getGenome());
        System.out.println();
    }

    /** Print the genes genome g */
    private static void printGenome(Collection<Gene> g) {
        System.out.printf("    %d genes\n", g.size());
        System.out.println("   --------------------------");
        int i = 0;

        for(Gene gene : g) {
            System.out.printf("       %d: %s\n", i++, gene.getDNA());
        }
    }
    
    /** Return a list of elements of b, separated by ", " and delimited by [ and ].*/
    public static String toString(int[] b) {
        String res= "[";
        for (int k= 0; k < b.length; k= k+1) {
            if (k > 0) res= res + ", ";
            res= res + b[k];
        }
        return res + "]";
    }

    /** Print matrix m, with a header row of just numbers. */
    public static void print1(int[][] m) {
        // Print matrix header
        String row= "";
        for (int r= 0; r < m.length; r= r+1) {
            row= row + String.format("%5d",  r);
        }
        System.out.println(row);

        // Print the matrix
        for (int r= 0; r < m.length; r= r+1) {
            row= "";
            for (int c= 0; c < m[r].length; c= c+1) {
                row= row + String.format("%5d", m[r][c]);
            }
            row= row + "     // S" + r;
            System.out.println(row);
        }
    }
}


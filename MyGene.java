

/** An instance maintains the dna string of a gene. */
public class MyGene extends Gene implements Comparable<MyGene> {

    /** Score for each common instance of a character. */
    public static final int COMMON_COST= 15;

    /** Score for difference in number of instances. */
    public static final int DIFFERENCE_COST= 8;

    /** Score for characters appearing in only one gene fragment. */
    public static final int ONLY_IN_ONE_COST= 25;

    /** Sliding window minimum match length. */
    public static final int MIN_MATCH= 4; 

    /** The gene. It is not empty, does not contain
        TGC, and contains only C T G A. */
    private String dna; // The gene. 

    /** Constructor: uninitialized Gene */
    public MyGene() {

    }

    /** Constructor: an instance with gene dna. 
    Throw an IllegalArgumentException if the dna is not well-formed, i.e. if it
    is empty, contains end delimiter TGC, or contains a char other than C T G A. */
    public MyGene(String dna) throws IllegalArgumentException {
        setDNA(dna);
    }

    /** return a number < 0 if this gene is smaller than gene g, 0 if same,
        and a number > 0 if greater.
        MyGene g1 is smaller than gene g2 iff g1's dna is shorter or
        (the dna's are the same length and g1's dna is lexically smaller) */
    public @Override int compareTo(MyGene g) {
        String s1= getDNA();
        String s2= g.getDNA();
        if (s1.length() == s2.length())
            return s1.compareTo(s2);
        return s1.length() - s2.length();
    }

    /** Set the DNA associated with this gene to dna.
    Throw an IllegalArgumentException if the dna is not well-formed, i.e. if it is null,
    is empty, contains end delimiter TGC, or contains a char other than C T G A. */
    public @Override void setDNA(String dna) throws IllegalArgumentException {
        if (dna == null || dna.length() == 0)
            throw new IllegalArgumentException("argument null or empty");
        if (dna.contains("TGC"))
            throw new IllegalArgumentException("argument contains TGC");
        for (int i= 0; i < dna.length(); i= i+1) {
            char c= dna.charAt(i);
            if ("CTGA".indexOf(c) < 0) {
                throw new IllegalArgumentException("argument contains illegal character '" + c + "'");
            }
        }
        this.dna= dna;
    }

    /** Get the DNA associated with this gene */
    public @Override String getDNA() {
        return dna;
    }

    /** return the DNA associated with this gene */
    public @Override String toString() {
        return dna;
    }

    /** Trim common prefix and suffix from s0 and s1, as per the
     * A2 handout, and return them in [0] and [1] of the result array. */
    public static String[] trim(String s0, String s1) {
        // Find the common prefix s.s0[0..h+1] and s.s[0.k+1]
        int h= 0;
        while (h < s0.length()  &&  h < s1.length()  &&
                s0.charAt(h) == s1.charAt(h)) {
            h= h+1;
        }
        s0= s0.substring(h);
        s1= s1.substring(h);

        // Find the common suffix s.s0[h+1..] and s.s1[k+1..]
        h= s0.length()-1;
        int k= s1.length()-1;
        while (0 <= h  && 0 <= k  &&
                s0.charAt(h) == s1.charAt(k)) {
            h= h-1;  k= k-1;
        }
        return new String[] {s0.substring(0, h+1), s1.substring(0, k+1)};
    }

    /** Return the minimum distance between this gene and gene g, calculate the
        distance according to the rules given in the A3 and A2 handouta. */
    public int getDistance(Gene g) {
        return Math.min(distanceRecursive(getDNA(), g.getDNA()),
                        distanceRecursive(g.getDNA(), getDNA()));
    }


    /** If String s0 matches s1 acc. to the A2 window-matching
     *  algorithm, return an array p with
     *  p[0] the match
     *  s0 = p[1] + p[0] + p[2]
     *  s1 = p[3] + p[0] + p[4]
     *  If the strings don't match, return null. */
    public static String[] match(String s0, String s1) {
        // invariant: No match occurs with beginning char in s0[0..h-1]
        for (int h= 0; h <= s0.length() - MIN_MATCH; h= h+1) {
            String match= s0.substring(h, h+MIN_MATCH);
            int k= s1.indexOf(match);
            if (k >= 0) {
                // s0[h..h+MIN_MATCH-1] matches s1[k..k+MIN_MATCH-1].
                // Set n to the length of the longest match
                int n= MIN_MATCH;
                while (h+n < s0.length()  &&  k+n < s1.length() &&
                        s0.charAt(h+n) == s1.charAt(k+n)) {
                    n= n+1;
                }
                
                return new String[] {s0.substring(h, h+n),
                        s0.substring(0,h), s0.substring(h+n),
                        s1.substring(0,k), s1.substring(k+n)};
            }
        }

        return null;
    }

    /** Return the basic distance between genes s0 and s1, as
       defined in the A2 handout. */
    public static int basicDistance(String s0, String s1) {
        String[] s= trim(s0, s1);

        int[] a0= numberOf(s[0]); // number of Cs, Ts, Gs, As in s0
        int[] a1= numberOf(s[1]); // number of Cs, Ts, Gs, As in s1
        int cost= 0;
        for (int k= 0; k < a0.length; k= k+1) {
            cost= cost + cost(a0[k], a1[k]);
        }
        return cost;   
    }

    /** Return the cost for chars ints n0 and n1.
        Each is the number of times a particular char appears in a string */
    public static int cost(int n0, int n1) {
        if (n0 == 0) return n1 * ONLY_IN_ONE_COST;
        if (n1 == 0) return n0 * ONLY_IN_ONE_COST;
        if (n0 <= n1) return n0 * COMMON_COST + (n1 - n0) * DIFFERENCE_COST;
        return n1 * COMMON_COST + (n0 - n1) * DIFFERENCE_COST;
    }

    /** Return an array a[0..3] containing the number of occurrences of
        C, A, T, G in s*/
    public static int[] numberOf(String s) {
        int[] a= new int[4];
        for (int k= 0; k < s.length(); k= k+1) {
            switch (s.charAt(k)) {
                case 'C': a[0]= a[0] + 1; break;
                case 'A': a[1]= a[1] + 1; break;
                case 'T': a[2]= a[2] + 1; break;
                case 'G': a[3]= a[3] + 1; break;
                default:
            }
        }
        return a;
    }

    /** Return the distance between genes s0 and s1, as
    defined in the A3 handout. */
    public static int distanceRecursive(String s0, String s1) {
        String[] s= trim(s0, s1);
        s0= s[0];
        s1= s[1];

        String[] m= match(s0, s1);
        if (m == null) {
            return basicDistance(s0, s1);
        }

        return basicDistance(m[1], m[3]) +
                Math.min(distanceRecursive(m[2], m[4]), distanceRecursive(m[4], m[2]));   
    }
}


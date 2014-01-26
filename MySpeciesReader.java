import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 */

/**
 * @author gries
 *
 */
public class MySpeciesReader extends SpeciesReader {

    /** Constructor: an instance with uninitialized fields. */
    public MySpeciesReader() {
    }

    /* Return a Species object for file named f.
       Throw an IO Exception if problems are encountered reading the file.
     */
    public @Override Species readSpecies(String f) throws IOException {
        return readSpecies(new File(f));
    }

    /** Return a Species object for f.
    Throw an IO Exception if problems are encountered reading the file */
    public @Override Species readSpecies(File f) throws IOException {
        FileReader fr= new FileReader(f);
        BufferedReader br= new BufferedReader(fr);

        Species s= new MySpecies();

        // Read one line at a time from br and put its data into s
        String line= br.readLine();
        while (line != null) {
            parseLine(line, s);
            line= br.readLine();
        }
        br.close(); 
        return s;
    }

    /** Parse line and add its attribute-value pair, if it has one and it
    is possible, to s.
    Precondition: line contains either just spaces or an attribute-
    value pair, as specified in section "Specifed File Format" of
    the A0 handout.
     */
    public static void parseLine(String line, Species s) {
        line= line.trim();
        int k= line.indexOf('=');
        if (k < 0)
            return;

        String attribute= line.substring(0,k).trim();
        String value= line.substring(k+1).trim();

        if (attribute.equals("Name")) {
            s.setName(removeQ(value));
        }
        if (attribute.equals("LatinName")) {
            s.setLatinName(removeQ(value));
        }
        if (attribute.equals("ImageFilename")) {
            s.setImageFilename(removeQ(value));
        }
        if (attribute.equals("Weight")) {
            s.setWeight(Integer.parseInt(value));
        }
        if (attribute.equals("Collected-by")) {
            s.setCollector(removeQ(value));
        }
        if (attribute.equals("DNA")) {
            s.setDNA(removeQ(value));
        }       
    }
    
    /** return s but with surrounding double quotes removed.
       Precondition: s end and begins with a double quote */
    public static String removeQ(String s) {
        return s.substring(1, s.length()-1);
    }

}

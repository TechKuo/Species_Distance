import java.awt.Color;
import java.io.IOException;
import java.util.*;

import javax.swing.JLabel;

public class A4GUI extends a4GUI.ComparisonGUI {
	
	private Species[] speciesArray;				//array of all species in SpeciesData
	private int[][] distanceMatrix;				//distance matrix for all species in SpeciesData
	private int[] speciesDistances; 			//array containing the selected species' distances from other species
	private int c;								//holds the index (of the array of species) of the species closet to the selected species 
	private ArrayList<Integer> closelyRelated;	//holds the indices of the species somewhat related(see InitializeSomeWhatRelated) to the selected species
	private ArrayList<Integer> distantlyRelated;//holds the indices of the species distantly related(see InitializeDistantlyRelated to the selected species

	 /** Constructor: an instance with an expected table of n cells.
     * <p>
     * After constructing an instance of ComparisonGUI,
     * call setCellImage for each cell (numbered 0 through n-1) to place
     * images in the cells.
     * Throw a RuntimeException if n <= 0.
     */
	public A4GUI(int n){
		super(n);
	}
	
	 /** Constructor: an instance with an expected table of s.length cells where
	  * s is the array of all species. Stores s and corresponding distance matrix 
	  * in appropriate fields.
     * <p>
     * After constructing an instance of ComparisonGUI,
     * call setCellImage for each cell (numbered 0 through s.length-1) to place
     * images in the cells.
     * 
     */
	public A4GUI(Species[] s, int[][] dm){
		super(s.length);
		speciesArray = s;
		distanceMatrix = dm;
		for (int i = 0; i < s.length; i++){
			setCellImage(i, "SpeciesData/"+s[i].getImageFilename());
		}
		super.run();
	}
	
	/** Add to field comparisonBox the stuff that goes into the right panel, i.e.
    the label and image for the selected species and the label and image for
    its closest species as well as a color key. */
	public @Override void fixComparisonBox() {
		comparisonBox.add(selectedLabel);
		comparisonBox.add(selectedImage);
		comparisonBox.add(closestRelatedLabel);
		comparisonBox.add(closestRelatedImage);
		JLabel divider = new JLabel("________________________");
		comparisonBox.add(divider);
		JLabel colorKey= new JLabel("Color Key");
		comparisonBox.add(colorKey);
		JLabel selectedSpecies= new JLabel("Selected Species: Red");
		comparisonBox.add(selectedSpecies);
		JLabel closestRelative= new JLabel("Closest Relative: Orange");
		comparisonBox.add(closestRelative);
		JLabel closelyRelatedSpecies= new JLabel("Closely Related Species: Yellow");
		comparisonBox.add(closelyRelatedSpecies);
		JLabel distantlyRelatedSpecies= new JLabel("Distantly Related Species: Green");
		comparisonBox.add(distantlyRelatedSpecies);
		
    }
	
	 /** Place the image for species number i and the image for its closest relative
    in the east panel. Change the background colors of the species to
    indicate distance from species number i. */
	public @Override void onSelectCell(int  i) { 
    setSelectedInfo(speciesArray[i].getName(),"SpeciesData/"+speciesArray[i].getImageFilename());
    setCellColor(i,new Color(255,0,0));										//set the color of the selected species to red
    int c = findClosestRelative(i);
    setClosestRelativeInfo(speciesArray[c].getName(),"SpeciesData/"+speciesArray[c].getImageFilename());
    setCellColor(c,new Color(238,113,25));									//set the color of the closest relative to the selected species to orange
    intializeSpeciesDistances(i);
    initializeCloselyRelated(i);
    initializeDistantlyRelated(i);
    for (int j : closelyRelated) setCellColor(j, new Color(255,200,8));	//set the color of the somewhat related species to yellow
    for (int k : distantlyRelated) setCellColor(k, new Color(170,197,27));	//set the color of the distantly related species to green
	}
	
	/** Returns the index in allSpecies of the species 
	 * closest to the selected species(at i of allSpecies).  */
	public int findClosestRelative(int i){
		c = 0;			
		if (i == 0) c = 1;	//to make sure the smallest distance is not 0 
		int minDistance = distanceMatrix[i][c]; //holds the smallest distance between species
		for (int r = 0; r < distanceMatrix.length; r++) {
           if (distanceMatrix[i][r] != 0 && distanceMatrix[i][r] < minDistance){
        	   minDistance = distanceMatrix[i][r];
        	   c = r;
           }
		}
		return c;
	}
	
	/**Initializes speciesDistances, an array containing the distances of species from the selected species(at i)*/
	private void intializeSpeciesDistances(int i){
		speciesDistances = new int[distanceMatrix.length];
		for (int r = 0; r < distanceMatrix.length; r++) speciesDistances[r] = distanceMatrix[i][r];
	}
	
	/**Returns the max distance from the selected species (at i) */
	private int computeMaxDistance (int i){
		int max = 0;
		for (int r = 0; r<speciesDistances.length; r++){
			if (speciesDistances[r] > max) max = speciesDistances[r];
		}
		return max;
	}
	
	/**Initializes closelyRelated, an array containing the indices of species 
	 * whose distances are less than the max distance from the selected species divided by 2
	 * not including the selected species itself and its closest relative */
	private void initializeCloselyRelated (int i){
		closelyRelated = new ArrayList<Integer>();
		int max = computeMaxDistance(i);
		for (int r = 0; r < speciesDistances.length; r++) {
	        if (r != c && speciesDistances[r] != 0 &&speciesDistances[r] < max/2 )  {
	        	closelyRelated.add(r);
	        }
	     }
	}
	/**Initializes distantlyRelated, an array containing the indices of species
	 * whose distances are greater than max distance from the selected species divided by 2*/
	private void initializeDistantlyRelated(int i){
		distantlyRelated = new ArrayList<Integer>();
		int max = computeMaxDistance(i);
		for (int r = 0; r < speciesDistances.length; r++) {
	        if (speciesDistances[r] > max/2 )  distantlyRelated.add(r);
		}
	}
	
	/**Do what is required in the A4 handout
	 * @throws IOException */
	public static void main(String[] args) throws IOException {
		Species[] allSpecies = Main.getSpecies(); 	//store all species in SpeciesData in an array
		Arrays.sort(allSpecies);					//sort the species array	
		
		Gene[] genes= Main.getGenes(allSpecies);	//get genes of all the species and store them in an array
	    Arrays.sort(genes);							//sort the gene array
	    int[][] geneDistance= Main.distanceMatrix(genes);	//pre-calculate gene distances for later species distance matrix
	   
	    for (Species sp : allSpecies) {						//store the gene distances in the species objects
	            MySpecies msp= (MySpecies) sp;
	            msp.saveGeneInformation(genes, geneDistance);
	        }
	    int[][] distances= Main.distanceMatrix(allSpecies);	//calculate the distance matrix for all Species
		A4GUI a4gui= new A4GUI(allSpecies, distances);
	}

}

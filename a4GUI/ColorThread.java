package a4GUI;

import javax.swing.SwingUtilities;

/** Used internally to create the color fading effect.
 * Runs a thread in the background that updates cell colors
 * in small steps on a timer.
 */
public class ColorThread extends Thread {
    protected boolean running= false; // Stop thread by setting this to false

    private Cell[] cells; // From the ComparisonGUI

    private int ms= 50; // update interval in milliseconds
    private double dt= (double) ms / 1000.0; // update interval in seconds

    /** Constructor: an instance with cell array c. */
    public ColorThread(Cell[] c) {
        super();
        cells= c;
    }

    /** Called to start this thread running.
     *
     */
    public void run() {
        running= true;
        while (running) {
            try {
                sleep(ms);
            } catch (InterruptedException e) {
                break;
            }
            if(running)
                try {
                    // invokeAndWait is needed here because Swing components can't be 
                    // safely updated from any thread other than the event dispatch
                    //  thread. invokeAndWait passes this along.
                    SwingUtilities.invokeAndWait(new Runnable() { 
                        public void run() {
                            if (cells != null)
                                for (Cell c : cells) {
                                    if (c != null) {
                                        c.advanceColor(dt);
                                        c.image.updateUI();
                                    }
                                }
                        }
                    });
                } catch (Exception e) {
                    // Suppress exceptions
                }
        }
    }
}

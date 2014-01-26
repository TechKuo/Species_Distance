package a4GUI;

/*
 * Based on Scott Carpenter's ImagePanel.java example, which
 * scales images to fit a panel.
 *
 * Source: http://www.movingtofreedom.org/2007/11/11/displaying-a-scaled-image-in-java/
 * ---------------------------------------------------------------------------------------------------------------
 * ImagePanel.java
 *
 * Copyright (C) 2007  Scott Carpenter (scottc at movingtofreedom dot org)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Created on November 9, 2007, 4:07 PM
 *
 */

import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.imageio.*;

public class ScalingImageButton extends JButton {

    private static final long serialVersionUID = 1L;
    private Image image;
    private Image scaledImage;
    private int imageWidth= 0;
    private int imageHeight= 0;

    /** Constructor: an opaque image. */
    public ScalingImageButton() {
        super();
        setOpaque(true);
        setText("image");
    }

    /** Load an image from file with name (or path) f. */
    public void loadImage(String f) throws IOException {
        image= ImageIO.read(new File(f));
        // Might be a situation where image isn't fully loaded, and
        // should check for that before setting¦
        imageWidth= image.getWidth(this);
        imageHeight= image.getHeight(this);
        setScaledImage();
    }

    /** Scale the image: e.g. containing frame might call this from formComponentResized*/
    public void scaleImage() {
        setScaledImage();
    }

    /** Set the width and height of the scaled image to the buttons width and height. */
    protected void setScaledImage() {
        setScaledImage(getWidth(), getHeight());
    }

    /** Set the width and height of the scaled image to s and h. */
    public void setScaledImage(int w, int h) {
        if (w <= 0 || h <= 0) return;

        if (image != null) {
            //use floats so division below won't round
            float iw= imageWidth;
            float ih= imageHeight;
            float pw= w;
            float ph= h;

            if (pw == 0 || ph == 0 || iw == 0 || ih == 0) 
                return;

            if (pw/ph >= iw/ih) {
                iw= iw * ph / ih;
                ih= ph;
            } else {
                ih= ih * pw / iw;
                iw= pw;
            }
            scaledImage= image.getScaledInstance(
                    new Float(iw).intValue(), new Float(ih).intValue(), Image.SCALE_DEFAULT);

            setIcon(new ImageIcon(scaledImage));
        }
    }

}


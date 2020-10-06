import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import java.util.ArrayList;



public class Scrach extends PApplet
{
    public static void main(String[] passedArgs)
    {
        String[] appletArgs = new String[] { "Scrach" };

        if (passedArgs != null)
        {
            PApplet.main(concat(appletArgs, passedArgs));
        }
        else
        {
            PApplet.main(appletArgs);
        }
    }


    public void settings()
    {
        // size(640, 640);
        size(640, 640, P3D);
    }


    public void setup()
    {
        ortho(-320, 320, 320, -320); // hard coded, 640x640 canvas, RHS
        resetMatrix();
        colorMode(RGB, 1.0f);
        background(0);
    }
    int count = 0;

    public void draw()
    {
        clear();
        strokeWeight(3);
        
        stroke(255, 255, 255);
        triangle(0.0f, 134.13733f, 0.0f, 75.40885f, 125.307594f, 69.268326f);
        
        stroke(255, 0, 0);
        point(0.0f, 76.0f);
    }
}

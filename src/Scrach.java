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
        stroke(255, 255, 255);
        strokeWeight(3);
        int divisions = 10;
        int radius = 300;
        float unitPi = PI / divisions;
        float unit2PI = unitPi * 2;

        float theta = 0f; // from 0 to 2Pi
        float phi = 0f; // from 0 to Pi

        // int innerCount = 0;

        for (int i = 0; i < divisions; i++)
        {
            for (int j = 0; j < divisions; j++)
            {
                // if (innerCount > count)
                // {
                //     continue;
                // }
                // else innerCount++;

                float[] t = new float[] {
                    (float) radius * (float) Math.sin(phi) * (float) Math.sin(theta),
                    (float) radius * (float) Math.cos(phi),
                    (float) radius * (float) Math.sin(phi) * (float) Math.cos(theta)
                };
                theta += unit2PI;
                // System.out.println("(" + 
                //     (float) radius * (float) Math.sin(phi) * (float) Math.sin(theta) + ", " +
                //     (float) radius * (float) Math.cos(phi) + ", " +
                //     (float) radius * (float) Math.sin(phi) * (float) Math.cos(theta) + ")"
                // );
                // if (t[2] > 0)
                // {
                //     stroke(0, 255, 0);
                // }
                // else
                // {
                //     stroke(255, 255, 255);
                // }
                // delay(20);
                
                t = project(t);
                point(t[0], t[1]);
            }
            phi += unitPi;
        }

        // if (count < divisions * divisions)
        //     count++;
    }


    final float[] EYE = { 0, 0, 600 };
    final float PERSPECTIVE = 0.002f; // 1/500, don't bother changing


    public float[] project(float[] v)
    {
        float adjZ = v[Z] - EYE[Z]; // RHS, Z into screen
        if (adjZ > 0)
            return null; // clipping plane
        adjZ *= -1;
        float px = v[X] / (adjZ * PERSPECTIVE);
        float py = v[Y] / (adjZ * PERSPECTIVE);
        return new float[] { px, py };
    }
}

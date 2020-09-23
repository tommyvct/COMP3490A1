import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import java.util.Arrays;



public class Assign1Handout extends PApplet 
{
    ////////////////////// MAIN PROGRAM
    // creates a sphere made of triangles, centered on 0,0,0, with given radius
    //
    // also -
    // calculates the 3 edge vectors for each triangle
    // calculates the face normal (unit length)
    //
    // HINT: first setup a loop to calculate all the points around the sphere,
    // store in an array then loop over those points and setup your triangles.
    public Triangle[] makeSphere(int radius, int divisions) 
    {
        return new Triangle[0];
    }



    // takes a new triangle, and calculates it's normals and edge vectors
    public Triangle setupTriangle(Triangle t) 
    {
        // TODO: 意味不明
        return t;
    }



    // This function draws the 2D, already projected triangle, on the raster
    // - it culls degenerate or back-facing triangles
    //
    // - it calls fillTriangle to do the actual filling, and bresLine to
    // make the triangle outline.
    //
    // - implements the specified lighting model (using the global enum type)
    // to calculate the vertex colors before calling fill triangle. Doesn't do
    // shading
    //
    // - if needed, it draws the outline and normals (check global variables)
    //
    // HINT: write it first using the gl LINES/TRIANGLES calls, then replace
    // those with your versions once it works.
    public void draw2DTriangle(Triangle t, Lighting lighting, Shading shading) 
    {
        // TODO: didn't learn yet
    }



    // uses a scanline algorithm to fill the 2D on-raster triangle
    // - implements the specified shading algorithm to set color as specified
    // in the global variable shading. Note that for NONE, this function simply
    // returns without doing anything
    // - uses POINTS to draw on the raster
    public void fillTriangle(Triangle t, Shading shading) 
    {
        // TODO: didn't learn yet
    }



    // given point p, normal n, eye location, light location, calculates phong
    // - material represents ambient, diffuse, specular as defined at the top of the
    // file
    // - calculates the diffuse, specular, and multiplies it by the material and
    // - fillcolor values
    public float[] phong(float[] p, float[] n, float[] eye, float[] light, float[] material, float[] fillColor, float s) 
    {
        // TODO: didn't learn yet
        return new float[] { 0, 0, 0 };
    }



    /**
     * implements Bresenham's line algorithm
     * 
     * @param fromX X-coordinate of origin point
     * @param fromY Y-coordinate of origin point
     * @param toX X-coordinate of destination point
     * @param toY Y-coordinate of destination point
     */
    public void bresLine(int fromX, int fromY, int toX, int toY) 
    {
        //////// FAILSAFE \\\\\\\\\\\\\
        // beginShape(LINES);

        // vertex(fromX, fromY);
        // vertex(toX, toY);

        // endShape();
        ///////////////////////////////

        boolean flip = false;
        float error = 0.499f;
        int deltaX = toX - fromX;
        int deltaY = toY - fromY;
        float gradient = (float) deltaY / (float) deltaX;
        
        if (deltaX == 0)
        {
            beginShape(POINTS);
            
            for (int i = min(fromY, toY); i <= max(fromY, toY); i++)
            {
                vertex(fromX, i);
            }
            
            endShape();
            return;
        }
        else if (abs(gradient) > 1.0f)
        {
            int swap;
            flip = true;

            swap = fromX;
            fromX = fromY;
            fromY = swap;

            swap = toX;
            toX = toY;
            toY = swap;

            deltaX = toX - fromX;
            deltaY = toY - fromY;
            gradient = (float) deltaY / (float) deltaX;
        }

        int x = fromX;
        int y = fromY;

        int[][] points = new int[Math.abs(deltaX)][2];

        for (int i = 0; i < Math.abs(deltaX); i++)
        {
            points[i][0] = x;
            points[i][1] = y;

            x += (deltaX < 0) ? -1 : +1;  // 👈 = -1, 👉 = +1
            error += gradient;
            if (Math.abs(error) >= 0.5f)
            {
                y += (deltaY < 0) ? -1 : +1;  // 👇 = -1, 👆 = +1
                error += (error < 0 ) ? +1 : -1;
            }
        }

        beginShape(POINTS);

        for (int[] point : points) 
        {
            if (flip)
            {
                vertex(point[1], point[0]);
            }
            else
            {
                vertex(point[0], point[1]);
            }
        }

        endShape();
    }



    /**
     * 2D & 3D vector cross product
     * 
     * @param vector1 lvalue
     * @param vector2 rvalue
     * @return numerical cross product when 2D(at index 0), a vector of cross product when 3D
     * @throws Exception when dimension mismatch or not 2D or not 3D.
     */
    public float[] cross(float[] vector1, float[] vector2) throws Exception 
    {
        if (vector1.length == 2 && vector2.length == 2)
        {
            return new float[]{ vector1[0] * vector2[1] - vector1[1] * vector2[2] };
        }
        else if (vector1.length == 3 && vector2.length == 3)
        {
            return new float[]
            {
                vector1[1] * vector2[2] - vector1[2] * vector2[1],
                vector1[2] * vector2[0] - vector1[0] * vector2[2],
                vector1[0] * vector2[1] - vector1[1] * vector2[0]
            };
        }
        else
        {
            throw new Exception("Invalid dimension");
        }
    }



    /**
     * Vector normalization
     * // TODO: test pending
     * @param vector to normalize
     * @return a zero vector if length of given vector is 0, otherwise a normalized vector
     */
    public float[] normalize(float[] vector) 
    {
        float unit = 0;
        float[] ret = new float[vector.length];

        for (float f : vector) 
        {
            unit += f * f;
        }

        if (unit == 0.0f)
        {
            return ret;
        }
        
        unit = sqrt(unit);

        for (int i = 0; i < ret.length; i++) 
        {
            ret[i] = vector[i] / unit;
        }

        return ret;
    }



    /**
     * 2D & 3D vector dot product
     * // TODO: test pending
     * @param vector1 lvalue
     * @param vector2 rvalue
     * @return numerical dot product of vector1 and vector2
     * @throws Exception when dimension mismatch or not 2D or not 3D.
     */
    public float dot(float[] vector1, float[] vector2) throws Exception 
    {
        if (vector1.length == 2 && vector2.length == 2)
        {
            return vector1[0] * vector2[0] + vector1[1] * vector2[1];
        }
        else if (vector1.length == 3 && vector2.length == 3)
        {
            return vector1[0] * vector2[0] + vector1[1] * vector2[1] + vector1[2] * vector2[2];
        }
        else
        {
            throw new Exception("Invalid dimension");
        }
    }



    /**
     * 2D & 3D vector substraction.
     * // TODO: test pending
     * @param vector1 lvalue
     * @param vector2 rvalue
     * @return a new vector representing vector1-vector2
     * @throws Exception when dimension mismatch or not 2D or not 3D.
     */
    public float[] subtract(float[] vector1, float vector2[]) throws Exception 
    {
        if (vector1.length == 2 && vector2.length == 2)
        {
            return new float[] { vector2[0] - vector1[0], vector2[1] - vector1[1] };
        }
        else if (vector1.length == 3 && vector2.length == 3)
        {
            return new float[] { vector2[0] - vector1[0], vector2[1] - vector1[1], vector2[2] - vector1[2] };
        }
        else
        {
            throw new Exception("Invalid dimension");
        }
    }
    


    static public void main(String[] passedArgs) 
    ////////////////////////////////          \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    /////////////////////////////// BLACK  BOX \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    //////////////////////////////  ¯\_(ツ)_/¯  \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    /////////////////////////////                \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    //#region Black Box
    {
        String[] appletArgs = new String[] { "Assign1Handout" };
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }

    /**
     * a test function that checks all the drawing octants and compars against the built-in line function
     */
    public void lineTest() 
    {
        int dsmall = 90;
        int dlarge = 180;
        int offset = 10;

        stroke(1.0f, 1.0f, 1.0f);
        beginShape(LINES);
        vertex(0, 0);
        vertex(dlarge, dsmall);
        vertex(0, 0);
        vertex(-dlarge, dsmall);
        vertex(0, 0);
        vertex(dlarge, -dsmall);
        vertex(0, 0);
        vertex(-dlarge, -dsmall);

        vertex(0, 0);
        vertex(dsmall, dlarge);
        vertex(0, 0);
        vertex(-dsmall, dlarge);
        vertex(0, 0);
        vertex(dsmall, -dlarge);
        vertex(0, 0);
        vertex(-dsmall, -dlarge);

        vertex(-dlarge, 0);
        vertex(dlarge, 0);
        vertex(0, -dlarge);
        vertex(0, dlarge);
        endShape(LINES);

        stroke(1.0f, 0f, 0.0f);
        bresLine(0 + offset, 0 + offset, dlarge + offset, dsmall + offset);
        bresLine(0 + offset, 0 + offset, -dlarge + offset, dsmall + offset);
        bresLine(0 + offset, 0 + offset, dlarge + offset, -dsmall + offset);
        bresLine(0 + offset, 0 + offset, -dlarge + offset, -dsmall + offset);

        bresLine(0 + offset, 0 + offset, dsmall + offset, dlarge + offset);
        bresLine(0 + offset, 0 + offset, -dsmall + offset, dlarge + offset);
        bresLine(0 + offset, 0 + offset, dsmall + offset, -dlarge + offset);
        bresLine(0 + offset, 0 + offset, -dsmall + offset, -dlarge + offset);

        bresLine(-dlarge, 0 + offset, dlarge, 0 + offset);
        bresLine(0 + offset, -dlarge, 0 + offset, dlarge);
    }

    class Triangle {
        Triangle(float[] V1, float[] V2, float[] V3) { // does DEEP copy!!
            v1 = Arrays.copyOf(V1, V1.length);
            v2 = Arrays.copyOf(V2, V2.length);
            v3 = Arrays.copyOf(V3, V3.length);
        }

        // position data. in 3D space
        float[] v1; // 3 triangle vertices
        float[] v2;
        float[] v3;

        // projected data. On the screen raster
        float[] pv1; // (p)rojected vertices
        float[] pv2;
        float[] pv3;

        // add other things as needed, like normals (face, vectors), edge vectors,
        // colors, etc.
    }

    Triangle[] sphereList;
    Triangle[] rotatedList;

    public void setup() {
        ortho(-320, 320, 320, -320); // hard coded, 640x640 canvas, RHS
        resetMatrix();
        colorMode(RGB, 1.0f);

        sphereList = makeSphere(SPHERE_SIZE, 10);
        rotatedList = new Triangle[sphereList.length];
        announceSettings();
    }

    public void settings() {
        size(640, 640, P3D); // hard coded 640x640 canvas
    }

    float theta = 0.0f;
    float delta = 0.01f;

    public void draw() {
        clear();

        if (rotate) {
            theta += delta;
            while (theta > PI * 2)
                theta -= PI * 2;
        }

        if (lineTest)
            lineTest();
        else {
            rotateSphere(sphereList, rotatedList, theta);
            drawSphere(rotatedList, lighting, shading);
        }
    }



    final char KEY_LIGHTING = 'l';
    final char KEY_SHADING = 's';
    final char KEY_OUTLINE = 'o';
    final char KEY_ROTATE = 'r';
    final char KEY_NORMALS = 'n';
    final char KEY_ACCELERATED = '!';
    final char KEY_LINE_TEST = 't';

    enum Lighting {
        FLAT, PHONG_FACE, PHONG_VERTEX
    }

    Lighting lighting = Lighting.PHONG_FACE;

    enum Shading {
        NONE, BARYCENTRIC, FLAT, GOURAUD, PHONG
    }

    Shading shading = Shading.NONE;

    boolean doOutline = false; // to draw, or not to draw (outline).. is the question
    boolean rotate = false;
    boolean normals = false;
    boolean accelerated = false;
    boolean lineTest = true;

    public void keyPressed() {
        if (key == KEY_SHADING) {
            int next = (shading.ordinal() + 1) % Shading.values().length;
            shading = Shading.values()[next];
        }
        if (key == KEY_LIGHTING) {
            int next = (lighting.ordinal() + 1) % Lighting.values().length;
            lighting = Lighting.values()[next];
        }
        if (key == KEY_OUTLINE)
            doOutline = !doOutline;

        if (key == KEY_ROTATE)
            rotate = !rotate;

        if (key == KEY_NORMALS)
            normals = !normals;

        if (key == KEY_ACCELERATED)
            accelerated = !accelerated;

        if (key == KEY_LINE_TEST)
            lineTest = !lineTest;

        announceSettings();
    }

    public void announceSettings() {
        String msg = "";
        if (lineTest)
            msg += "Line Test";
        else
            msg += "Shading: " + shading + " Lighting: " + lighting;

        if (accelerated)
            msg += "(accelerated) ";
        println(msg);
    }

    // GLOBAL CONSTANTS
    // -- use float[] for vectors, indexed as below
    final int X = 0;
    final int Y = 1;
    final int Z = 2;
    final int R = 0;
    final int G = 1;
    final int B = 2;

    // resonable settings for size, projection, lighting. Only change after you have
    // everything working
    final int SPHERE_SIZE = 200;

    final float[] LIGHT = { 200, 200, 350 }; // location of
    final float[] EYE = { 0, 0, 600 };

    final float[] OUTLINE_COLOR = { 1.0f, .2f, .5f };
    final float[] FILL_COLOR = { 1.0f, 1.0f, 1.0f };

    // reasonable constants for PHONG
    final float[] MATERIAL = { 0.1f, 0.8f, 0.8f }; // ambient, diffuse, specular %
    final int M_AMBIENT = 0;
    final int M_DIFFUSE = 1;
    final int M_SPECULAR = 2;
    final float PHONG_SPECULAR = 30;

    // returns null of the point is too close to the user (and thus not drawn).
    // check for that
    //
    // uhm... don't worry about how this works :D
    final float PERSPECTIVE = 0.002f; // 1/500, don't bother changing

    public float[] project(float[] v) {
        float adjZ = v[Z] - EYE[Z]; // RHS, Z into screen
        if (adjZ > 0)
            return null; // clipping plane
        adjZ *= -1;
        float px = v[X] / (adjZ * PERSPECTIVE);
        float py = v[Y] / (adjZ * PERSPECTIVE);
        return new float[] { px, py };
    }

    // and don't worry about this, either
    public void rotateVertex(float[] v, float theta) {
        float rx = v[X] * cos(theta) - v[Z] * sin(theta);
        float rz = v[X] * sin(theta) + v[Z] * cos(theta);
        v[X] = rx;
        v[Z] = rz;
    }

    // Projects all the triangles from 3D space (x,y,z) to screen raster coordinates
    // then draws each triangle.
    // - this is provided so you don't have to mess with projection
    public void drawSphere(Triangle[] sphere, Lighting lighting, Shading shading) {
        for (Triangle t : sphere) {
            if (t == null)
                continue;

            // has to be re-projected each time because of animation
            t.pv1 = project(t.v1);
            t.pv2 = project(t.v2);
            t.pv3 = project(t.v3);

            // null is clipped vertex
            if (!(t.pv1 == null || t.pv2 == null || t.pv3 == null))
                draw2DTriangle(t, lighting, shading);
        }
    }

    // rotates all the sphere values by the angle given
    public void rotateSphere(Triangle[] original, Triangle[] rotated, float theta) {
        for (int i = 0; i < original.length; i++) {
            if (rotated[i] == null)
                rotated[i] = setupTriangle(new Triangle(original[i].v1, original[i].v2, original[i].v3));
            else {
                System.arraycopy(original[i].v1, 0, rotated[i].v1, 0, original[i].v1.length);
                System.arraycopy(original[i].v2, 0, rotated[i].v2, 0, original[i].v2.length);
                System.arraycopy(original[i].v3, 0, rotated[i].v3, 0, original[i].v3.length);

                rotateVertex(rotated[i].v1, theta);
                rotateVertex(rotated[i].v2, theta);
                rotateVertex(rotated[i].v3, theta);
                setupTriangle(rotated[i]);
            }
        }
    }
    //#endregion
}

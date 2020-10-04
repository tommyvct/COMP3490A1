import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import java.util.ArrayList;
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
        float unitTheta = 2 * PI / divisions; // from 0 to 2Pi
        float unitPhi = PI / divisions;   // from 0 to Pi

        Vector[][] points = new Vector[divisions + 1][divisions];

        for (int i = 0; i < divisions + 1; i++) 
        {
            for (int j = 0; j < divisions; j++)
            {
                points[i][j] = new Vector(
                    (float) radius * (float) Math.sin(unitPhi * i) * (float) Math.sin(unitTheta * j),
                    (float) radius * (float) Math.cos(unitPhi * i),
                    (float) radius * (float) Math.sin(unitPhi * i) * (float) Math.cos(unitTheta * j)
                );
            }    
        }

        ArrayList<Triangle> triangleList = new ArrayList<Triangle>();

        for (int i = 0; i < divisions; i++) 
        {
            for (int j = 0; j < divisions - 1; j++)
            {
                Triangle t1 = new Triangle(
                    points[i][j],
                    points[i][j + 1],
                    points[i + 1][j + 1]
                );

                Triangle t2 = new Triangle(
                    points[i][j],
                    points[i + 1][j],
                    points[i + 1][j + 1]
                );

                triangleList.add(t1);
                triangleList.add(t2);
            }

            Triangle last1 = new Triangle(
                points[i][divisions - 1],
                points[i][0],
                points[i + 1][0]
            );

            Triangle last2 = new Triangle(
                points[i][divisions - 1],
                points[i + 1][divisions - 1],
                points[i + 1][0]
            );

            triangleList.add(last1);
            triangleList.add(last2);
        }

        return triangleList.toArray(new Triangle[0]);
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
        // TODO: Culling didn't work! 
        // if (t.degenerate || t.projectedDegenerate || !t.projectedCounterClockwiseWinding)
        // {
        //     // println(t.degenerate || t.projectedDegenerate ? "degenerate" : "CW Winding");
        //     return;
        // }

        fillTriangle(t, shading);

        if (doOutline)
        {
            stroke(256, 0, 0);
            bresLine(t.projectedVertex1, t.projectedVertex2);
            bresLine(t.projectedVertex2, t.projectedVertex3);
            bresLine(t.projectedVertex3, t.projectedVertex1);
            // try
            // {
            //     bresLine(t.projectedCentre, t.projectedCentre.add(t.projectedNormal));  // normal
            // }
            // catch (Exception e)
            // {
            //     e.printStackTrace();
            // }
        }
    }


    // uses a scanline algorithm to fill the 2D on-raster triangle
    // - implements the specified shading algorithm to set color as specified
    // in the global variable shading. Note that for NONE, this function simply
    // returns without doing anything
    // - uses POINTS to draw on the raster
    public void fillTriangle(Triangle t, Shading shading)
    {
        if (shading == Shading.NONE)
        {
            return;
        }

        beginShape(POINTS);
        for (int y = (int) t.projectedBoxBottom; y < (int) t.projectedBoxTop; y++)
        {
            for (int x = (int) t.projectedBoxLeft; x < (int) t.projectedBoxRight; x++)
            {
                if (t.isContainsPoint(new Vector((float) x, (float) y)))
                {
                    stroke(0, 255, 0);  // TODO: shading??
                    vertex(x, y);
                }
            }
        }
        endShape();
    }


    // given point p, normal n, eye location, light location, calculates phong
    // - material represents ambient, diffuse, specular as defined at the top of the
    // file
    // - calculates the diffuse, specular, and multiplies it by the material and
    // - fillcolor values
    public float[] phong(
        float[] p, float[] n, float[] eye, float[] light, float[] material, float[] fillColor,
        float s
    )
    {
        // TODO: Phong and Gouraud
        return new float[] { 0, 0, 0 };
    }


    /**
     * implements Bresenham's line algorithm
     * 
     * @param fromX X-coordinate of origin point
     * @param fromY Y-coordinate of origin point
     * @param toX   X-coordinate of destination point
     * @param toY   Y-coordinate of destination point
     */
    public void bresLine(int fromX, int fromY, int toX, int toY)
    {
        //////// FAILSAFE \\\\\\\\\\\\\
        beginShape(LINES);

        vertex(fromX, fromY);
        vertex(toX, toY);

        endShape();
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

        for (int i = 0; i < points.length; i++)
        {
            points[i][0] = x;
            points[i][1] = y;

            x += (deltaX < 0) ? -1 : +1; // 👈 = -1, 👉 = +1
            error += gradient;

            if (Math.abs(error) >= 0.5f)
            {
                y += (deltaY < 0) ? -1 : +1; // 👇 = -1, 👆 = +1
                error += (error < 0) ? +1 : -1;
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


    public void bresLine(Vector from, Vector to)
    {
        bresLine(
            (int) from.payload[Vector.X], (int) from.payload[Vector.Y],
            (int) to.payload[Vector.X], (int) to.payload[Vector.Y]
        );
    }


    class Triangle
    {
        Triangle(float[] V1, float[] V2, float[] V3)
        { // does DEEP copy!!
            vertex1 = new Vector(V1);
            vertex2 = new Vector(V2);
            vertex3 = new Vector(V3);

            notifyComponentChange();
        }


        public Triangle(Vector v1, Vector v2, Vector v3)
        {
            this.vertex1 = v1;
            this.vertex2 = v2;
            this.vertex3 = v3;

            notifyComponentChange();
        }


        public String toString()
        {
            return "{ " + vertex1.toString() + ", " + vertex2.toString() + ", " + vertex3.toString() + " }" + 
                " ->  \n{ " + projectedVertex1.toString() + ", " + projectedVertex2.toString() + projectedVertex3.toString() + " }";
        }


        public void notifyComponentChange()
        {
            float area = 0;
            float projectedArea = 0;

            try
            {
                edge1 = vertex2.subtract(vertex1); // AB
                edge2 = vertex3.subtract(vertex2); // BC
                edge3 = vertex1.subtract(vertex3); // CA
                
                normal = edge1.cross(edge2).normalize();
                area = edge1.cross(edge2).norm() / 2;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
            centre = new Vector(
                (vertex1.payload[Vector.X] + vertex2.payload[Vector.X] + vertex3.payload[Vector.X]) / 3,
                (vertex1.payload[Vector.Y] + vertex2.payload[Vector.Y] + vertex3.payload[Vector.Y]) / 3,
                (vertex1.payload[Vector.Z] + vertex2.payload[Vector.Z] + vertex3.payload[Vector.Z]) / 3
            );
            
            projectedVertex1 = new Vector(project(vertex1.payload));
            projectedVertex2 = new Vector(project(vertex2.payload));
            projectedVertex3 = new Vector(project(vertex3.payload));

            
            try
            {
                projectedEdge1 = projectedVertex2.subtract(projectedVertex1); // AB
                projectedEdge2 = projectedVertex3.subtract(projectedVertex2); // BC
                projectedEdge3 = projectedVertex1.subtract(projectedVertex3); // CA
                projectedArea = projectedEdge1.cross2d(projectedEdge2);  // to fix
                
                degenerate = (area < 0f) ? true : false; // area == 0
                projectedDegenerate = projectedArea < 0f ? true : false;
                projectedCounterClockwiseWinding = (projectedEdge1.cross2d(projectedVertex3.subtract(projectedVertex1)) > 0f) ? true : false;  // TODO: fuck windings!!!!!
                // println("" + (projectedEdge1.cross2d(projectedVertex3.subtract(projectedVertex1)) == projectedArea));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            projectedNormal = new Vector(project(normal.payload)).normalize();
            projectedNormal.scale(30f);
            projectedCentre = new Vector(project(centre.payload));

            projectedBoxLeft = min(
                projectedVertex1.payload[Vector.X], 
                projectedVertex2.payload[Vector.X],
                projectedVertex3.payload[Vector.X]
            );
            projectedBoxRight = max(
                projectedVertex1.payload[Vector.X], 
                projectedVertex2.payload[Vector.X],
                projectedVertex3.payload[Vector.X]
            );
            projectedBoxBottom = min(
                projectedVertex1.payload[Vector.Y], 
                projectedVertex2.payload[Vector.Y],
                projectedVertex3.payload[Vector.Y]
            );
            projectedBoxTop = max(
                projectedVertex1.payload[Vector.Y], 
                projectedVertex2.payload[Vector.Y],
                projectedVertex3.payload[Vector.Y]
            );
        }


        public boolean isContainsPoint(Vector p)
        {
            if (p.length != 2)
            {
                return false;
            }
            else 
            {
                try 
                {
                    boolean condition1 = (projectedEdge1.cross2d(p.subtract(projectedVertex1)) > 0f);
                    boolean condition2 = (projectedEdge2.cross2d(p.subtract(projectedVertex2)) > 0f);
                    boolean condition3 = (projectedEdge3.cross2d(p.subtract(projectedVertex3)) > 0f);
                    return condition1 == condition2 && condition1 ==  condition3 && condition2 == condition3;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return false;
                }   
            }
        }


        // position data. in 3D space
        Vector vertex1; // 3 triangle vertices
        Vector vertex2;
        Vector vertex3;
        Vector edge1;
        Vector edge2;
        Vector edge3;
        Vector normal;
        Vector centre;
        boolean degenerate;

        // projected data. On the screen raster
        Vector projectedVertex1; // (p)rojected vertices
        Vector projectedVertex2;
        Vector projectedVertex3;
        Vector projectedEdge1;
        Vector projectedEdge2;
        Vector projectedEdge3;
        Vector projectedNormal;
        Vector projectedCentre;

        boolean projectedDegenerate;
        boolean projectedCounterClockwiseWinding;

        float projectedBoxLeft;
        float projectedBoxRight;
        float projectedBoxTop;
        float projectedBoxBottom;
    }


    class Vector
    {
        public static final int X = 0;
        public static final int Y = 1;
        public static final int Z = 2;
        public final int length;
        private float[] payload;

        public String toString()
        {
            String ret = "(" + payload[0];

            for (int i = 1; i < payload.length; i++) {
                ret += "f, ";
                ret += payload[i];
            }

            ret += "f)";

            return ret;
        }

        public Vector(float... components)
        {
            this.payload = new float[components.length];
            this.length = this.payload.length;

            for (int i = 0; i < components.length; i++)
            {
                payload[i] = components[i];
            }
        }


        public float[] toFloatArray()
        {
            return this.payload;
        }


        public Vector add(Vector rvalue) throws Exception
        {
            if (rvalue.length != this.length)
            {
                throw new Exception("Invalid dimension");
            }
            else
            {
                var ret = new Vector(new float[this.length]);

                for (int i = 0; i < this.length; i++)
                {
                    ret.payload[i] = this.payload[i] + rvalue.payload[i];
                }
                return ret;
            }
        }


        /**
         * 2D & 3D vector substraction.
         * 
         * @param rvalue rvalue
         * @return a new vector representing vector1-vector2
         * @throws Exception when dimension mismatch or not 2D or not 3D.
         */
        public Vector subtract(float... rvalue) throws Exception
        {
            if (rvalue.length != this.length)
            {
                throw new Exception("Invalid dimension");
            }
            else
            {
                var ret = new float[this.length];

                for (int i = 0; i < ret.length; i++)
                {
                    ret[i] = this.payload[i] - rvalue[i];
                }
                return new Vector(ret);
            }
        }


        /**
         * 2D & 3D vector substraction.
         * 
         * @param rvalue rvalue
         * @return a new vector representing vector1-vector2
         * @throws Exception when dimension mismatch or not 2D or not 3D.
         */
        public Vector subtract(Vector rvalue) throws Exception
        {
            return this.subtract(rvalue.payload);
        }


        /**
         * In-place 2D & 3D vector substraction.
         * 
         * @param rvalue rvalue
         * @throws Exception when dimension mismatch or not 2D or not 3D.
         */
        public void subtractInPlace(float... rvalue) throws Exception
        {
            if (rvalue.length != this.length)
            {
                throw new Exception("Invalid dimension");
            }
            else
            {
                var ret = new float[this.length];

                for (int i = 0; i < ret.length; i++)
                {
                    this.payload[i] -= rvalue[i];
                }
            }
        }


        /**
         * In-place 2D & 3D vector substraction.
         * 
         * @param rvalue rvalue
         * @throws Exception when dimension mismatch or not 2D or not 3D.
         */
        public void subtractInPlace(Vector rvalue) throws Exception
        {
            this.subtractInPlace(rvalue.payload);
        }


        /**
         * 2D & 3D vector dot product
         * 
         * @param rvalue rvalue
         * @return numerical dot product of vector1 and vector2
         * @throws Exception when dimension mismatch or not 2D or not 3D.
         */
        public float dot(float... rvalue) throws Exception
        {
            if (this.length == 2 && rvalue.length == 2)
            {
                return this.payload[Vector.X] * rvalue[Vector.X] + this.payload[Vector.Y] * rvalue[Vector.Y];
            }
            else if (this.length == 3 && rvalue.length == 3)
            {
                return this.payload[Vector.X] * rvalue[Vector.X] + this.payload[Vector.Y] * rvalue[Vector.Y] +
                    this.payload[Vector.Z] * rvalue[Vector.Z];
            }
            else
            {
                throw new Exception("Invalid dimension");
            }
        }


        /**
         * Vector normalization
         * 
         * @return a zero vector if length of given vector is 0, otherwise a normalized
         *         vector
         */
        public Vector normalize()
        {
            float unit = 0;
            float[] ret = Arrays.copyOf(this.payload, this.payload.length);

            for (float f : this.payload)
            {
                unit += f * f;
            }
            if (unit == 0.0f)
            {
                return new Vector(new float[ret.length]);
            }
            unit = sqrt(unit);

            for (int i = 0; i < ret.length; i++)
            {
                ret[i] /= unit;
            }
            return new Vector(ret);
        }


        /**
         * In-place Vector normalization.
         * <p>
         * 
         * If the length of the subject vector is 0, nothing will happen.
         */
        public void normalizeInPlace()
        {
            float unit = 0;

            for (float f : this.payload)
            {
                unit += f * f;
            }
            if (unit == 0.0f)
            {
                return;
            }
            unit = sqrt(unit);

            for (int i = 0; i < this.payload.length; i++)
            {
                this.payload[i] /= unit;
            }
        }


        /**
         * 2D vector cross product
         * 
         * @param rvalue rvalue
         * @return numerical cross product
         * @throws Exception when dimension mismatch or not 2D.
         */
        public float cross2d(Vector rvalue) throws Exception
        {
            if (this.length == 2 && rvalue.length == 2)
            {
                return this.payload[Vector.X] * rvalue.payload[Vector.Y] - this.payload[Vector.Y] * rvalue.payload[Vector.X]; 
            }
            else
            {
                throw new Exception("Invalid dimension");
            }
        }


        /**
         * 3D vector cross product
         * 
         * @param rvalue rvalue
         * @return a vector of cross product when 3D
         * @throws Exception when dimension mismatch or not 3D.
         */
        public Vector cross(float... rvalue) throws Exception
        {
            if (this.length == 3 && rvalue.length == 3)
            {
                float[] components = new float[] {
                    this.payload[Vector.Y] * rvalue[Vector.Z] - this.payload[Vector.Z] * rvalue[Vector.Y],
                    this.payload[Vector.Z] * rvalue[Vector.X] - this.payload[Vector.X] * rvalue[Vector.Z],
                    this.payload[Vector.X] * rvalue[Vector.Y] - this.payload[Vector.Y] * rvalue[Vector.X] };
                return new Vector(
                    components
                );
            }
            else
            {
                throw new Exception("Invalid dimension");
            }
        }


        /**
         * 2D & 3D vector cross product
         * 
         * @param rvalue rvalue
         * @return numerical cross product when 2D(the only component in the Vector
         *         object), a vector of cross product when 3D
         * @throws Exception when dimension mismatch or not 2D or not 3D.
         */
        public Vector cross(Vector rvalue) throws Exception
        {
            return this.cross(rvalue.payload);
        }


        /**
         * 2D & 3D vector in-place cross product
         * 
         * @param rvalue rvalue
         * @throws Exception when dimension mismatch or not 3D.
         */
        public void crossInPlace(float... rvalue) throws Exception
        {
            if (this.length == 2 && rvalue.length == 2)
            {
                throw new Exception("Cannot do cross product in place on 2D vector.");
            }
            else if (this.length == 3 && rvalue.length == 3)
            {
                this.payload = new float[] {
                    this.payload[Vector.Y] * rvalue[Vector.Z] - this.payload[Vector.Z] * rvalue[Vector.Y],
                    this.payload[Vector.Z] * rvalue[Vector.X] - this.payload[Vector.X] * rvalue[Vector.Z],
                    this.payload[Vector.X] * rvalue[Vector.Y] - this.payload[Vector.Y] * rvalue[Vector.X] };
            }
            else
            {
                throw new Exception("Invalid dimension");
            }
        }


        /**
         * 2D & 3D vector in-place cross product
         * 
         * @param rvalue rvalue
         * @throws Exception when dimension mismatch or not 3D.
         */
        public void crossInPlace(Vector rvalue) throws Exception
        {
            this.crossInPlace(rvalue.payload);
        }


        public float norm()
        {
            float ret = 0;

            for (float f : this.payload)
            {
                ret += f * f;
            }

            return sqrt(ret);
        }

        public void scale(float factor)
        {
            for (int i = 0; i < this.payload.length; i++)
            {
                this.payload[i] *= factor;
            }
        }
    }


    public void triangleTest()
    {
        stroke(1.0f, 1.0f, 1.0f);

        Triangle t;

        do 
        {
            t = new Triangle(
            new Vector(random(-160, 160), random(-160, 160), random(-160, 160)),
            new Vector(random(-160, 160), random(-160, 160), random(-160, 160)),
            new Vector(random(-160, 160), random(-160, 160), random(-160, 160))
            );
        } while (t.projectedDegenerate || !t.projectedCounterClockwiseWinding);
        
        // t = new Triangle(
        //     new Vector(-141.22969f, -71.76317f, -46.007675f),
        //     new Vector(95.48776f, 66.87723f, 145.24771f),
        //     new Vector(32.29593f, 45.46768f, -47.11788f)
        // );

        println(t.toString());
        draw2DTriangle(t, Lighting.FLAT, Shading.BARYCENTRIC);

    }


    static public void main(String[] passedArgs)
    //////////////////////////////// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    /////////////////////////////// BLACK BOX \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    ////////////////////////////// ¯\_(ツ)_/¯ \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    ///////////////////////////// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    // #region Black Box
    {
        String[] appletArgs = new String[] { "Assign1Handout" };

        if (passedArgs != null)
        {
            PApplet.main(concat(appletArgs, passedArgs));
        }
        else
        {
            PApplet.main(appletArgs);
        }
    }


    /**
     * a test function that checks all the drawing octants and compars against the
     * built-in line function
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

    // put class Triangle back here


    Triangle[] sphereList;
    Triangle[] rotatedList;


    public void setup()
    {
        ortho(-320, 320, 320, -320); // hard coded, 640x640 canvas, RHS
        resetMatrix();
        colorMode(RGB, 1.0f);

        // frameRate(1);
        sphereList = makeSphere(SPHERE_SIZE, 10);
        rotatedList = new Triangle[sphereList.length];
        announceSettings();
    }


    public void settings()
    {
        size(640, 640, P3D); // hard coded 640x640 canvas
    }


    float theta = 0.0f;
    float delta = 0.01f;


    public void draw()
    {
        clear();

        if (rotate)
        {
            theta += delta;
            while (theta > PI * 2)
            theta -= PI * 2;
        }

        if (lineTest)
            lineTest();
        else
        {
            rotateSphere(sphereList, rotatedList, theta);
            drawSphere(rotatedList, lighting, shading);
        }

        if (mousePressed)
        {
            println("(" + (mouseX-320f) + ", " + (320f-(mouseY)) + ")");
        }
    }


    // public void draw()
    // {
    //     clear();

    //     triangleTest();

    //     if (mousePressed)
    //     {
    //         println("(" + (mouseX-320f) + ", " + (320f-(mouseY)) + ")");
    //     }
    // }


    final char KEY_LIGHTING = 'l';
    final char KEY_SHADING = 's';
    final char KEY_OUTLINE = 'o';
    final char KEY_ROTATE = 'r';
    final char KEY_NORMALS = 'n';
    final char KEY_ACCELERATED = '!';
    final char KEY_LINE_TEST = 't';
    final char KEY_FPS_1 = '1';
    final char KEY_FPS_05 = '5';
    final char KEY_FPS_NORMAL = '6';


    enum Lighting
    {
        FLAT, PHONG_FACE, PHONG_VERTEX
    }


    Lighting lighting = Lighting.PHONG_FACE;


    enum Shading
    {
        NONE, BARYCENTRIC, FLAT, GOURAUD, PHONG
    }


    Shading shading = Shading.NONE;
    boolean doOutline = false; // to draw, or not to draw (outline).. is the question
    boolean rotate = false;
    boolean normals = false;
    boolean accelerated = false;
    boolean lineTest = false;



    public void keyPressed()
    {
        if (key == KEY_SHADING)
        {
            int next = (shading.ordinal() + 1) % Shading.values().length;
            shading = Shading.values()[next];
        }
        if (key == KEY_LIGHTING)
        {
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

        if (key == KEY_FPS_05)
        {
            frameRate(0.5f);
            println("FPS: 0.5");
        }
        if (key == KEY_FPS_1)
        {
            frameRate(1);
            println("FPS: 1");
        }
        if (key == KEY_FPS_NORMAL)
        {
            frameRate(60);
            println("FPS: 60");
        }
        announceSettings();
    }


    public void announceSettings()
    {
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


    // and don't worry about this, either
    public void rotateVertex(float[] v, float theta)
    {
        float rx = v[X] * cos(theta) - v[Z] * sin(theta);
        float rz = v[X] * sin(theta) + v[Z] * cos(theta);
        v[X] = rx;
        v[Z] = rz;
    }


    // Projects all the triangles from 3D space (x,y,z) to screen raster coordinates
    // then draws each triangle.
    // - this is provided so you don't have to mess with projection
    public void drawSphere(Triangle[] sphere, Lighting lighting, Shading shading)
    {
        for (Triangle t : sphere)
        {
            if (t == null)
                continue;

            // has to be re-projected each time because of animation
            // unnecessary, projected vector now calculated on construction
            // t.pv1 = project(t.v1);
            // t.pv2 = project(t.v2);
            // t.pv3 = project(t.v3);

            // null is clipped vertex
            if (!(t.projectedVertex1 == null || t.projectedVertex2 == null || t.projectedVertex3 == null))
                draw2DTriangle(t, lighting, shading);
        }
    }


    // rotates all the sphere values by the angle given
    public void rotateSphere(Triangle[] original, Triangle[] rotated, float theta)
    {
        for (int i = 0; i < original.length; i++)
        {

            if (rotated[i] == null)
                rotated[i] = new Triangle(original[i].vertex1, original[i].vertex2, original[i].vertex3);
            else
            {
                System.arraycopy(
                    original[i].vertex1.payload, 0,
                    rotated[i].vertex1.payload, 0,
                    original[i].vertex1.length
                );
                System.arraycopy(
                    original[i].vertex2.payload, 0,
                    rotated[i].vertex2.payload, 0,
                    original[i].vertex2.length
                );
                System.arraycopy(
                    original[i].vertex3.payload, 0,
                    rotated[i].vertex3.payload, 0,
                    original[i].vertex3.length
                );

                rotateVertex(rotated[i].vertex1.payload, theta);
                rotateVertex(rotated[i].vertex2.payload, theta);
                rotateVertex(rotated[i].vertex3.payload, theta);
                rotated[i].notifyComponentChange();
            }
        }
    }
    // #endregion
}

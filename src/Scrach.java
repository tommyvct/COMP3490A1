public class Scrach {
    public static void main(String[] args) {
        float[] a = new float[] { 564, 626, 2 };
        float[] b = new float[] { 55, -51, 44 };

        try {
            var ans = normalize(v3d(566.7468f,4813.86f, 843.8777f));
            for (float f : ans) {
                System.out.println(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static float[] v3d(float x, float y, float z)
    {
        return new float[] {x, y, z};
    }

    public static float[] v2d(float x, float y)
    {
        return new float[] {x, y};
    }
    

    /**
     * Vector normalization
     * // TODO: test pending
     * @param vector to normalize
     * @return a zero vector if length of given vector is 0, otherwise a normalized vector
     */
    public static float[] normalize(float[] vector) 
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
        
        unit = (float) Math.sqrt(unit);

        for (int i = 0; i < ret.length; i++) 
        {
            ret[i] = vector[i] / unit;
        }

        return ret;
    }

}

public class Scrach
{
    public static void main(String[] args)
    {
        a aa = new a();

        aa.print();

        aa.ia[0] = 6;

        aa.print();

        aa.getIa()[1] = 6;
        aa.print();
    }
}



class a
{
    public int[] ia;


    public a()
    {
        ia = new int[] { 1, 2, 3 };
    }


    public int[] getIa()
    {
        return ia;
    }


    public void print()
    {
        for (int i : ia)
        {
            System.out.println(i);
        }
    }
}

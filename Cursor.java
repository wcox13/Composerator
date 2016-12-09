import java.util.*;

public class Cursor<T extends Chainable> implements Comparable<Cursor<T>>
{
    // Instance var
    private ArrayList<T> entries;

    // Basic constructor
    public Cursor()
    {
        entries = new ArrayList<T>();
    }

    // Constructor that also takes an input arraylist
    public Cursor(ArrayList<T> list)
    {
        entries = list;
    }

    // Get ith entry
    public Chainable get(int i)
    {
        return entries.get(i);
    }

    public int getLen()
    {
        return entries.size();
    }

    public ArrayList<T> getEntries()
    {
        return entries;
    }

    // Set ith entry
    public void set(int i, T obj)
    {
        entries.set(i, obj);
    }

    // Get last entry
    public T getLast()
    {
        return entries.get((entries.size() - 1));
    }

    // Return new chainable with last element removed
    public Cursor stripLast()
    {
        ArrayList<T> newEntries = new ArrayList<T>();

        for (int i = 0, n = entries.size() - 1; i < n; i++)
        {
            newEntries.add(entries.get(i));
        }

        return new Cursor(newEntries);
    }

    // implementing compare method. ASSUMES same length
    public int compareTo(Cursor cur)
    {
        for (int i = 0, len = entries.size(); i < len; i++)
        {
            // compare corresponding entries
            int comp = entries.get(i).compareTo(cur.get(i));

            if (comp < 0)
                return -1;

            else if (comp > 0)
                return 1;
        }
        return 0;
    }

    @Override public String toString()
    {
        String s = "[";

        for (Chainable c : entries)
        {
            s += c.toString() + "  ";
        }

        return s + "]:   ";
    }
}

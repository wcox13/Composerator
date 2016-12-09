import java.util.ArrayList;

public class Chain<T extends Chainable> {

    // Instance vars
    private ArrayList<T> list;
    private Cursor cursor;

    // This is the position of the LAST element of the cursor
    private int cursorPos;

    // Create new chain
    public Chain()
    {
        list = new ArrayList<T>();
    }

    // Constructor that takes an array
    public Chain(ArrayList<T> l)
    {
        list = l;
    }

    public Cursor getCursor()
    {
        return cursor;
    }

    // Add an object to the chain
    public void addToChain(T obj)
    {
        list.add(obj);
    }

    // Initialize the cursor (length = order + 1) to the first elements
    public boolean initCursor(int order)
    {
        if (list.size() >= order)
        {
            // copy first elements of list
            ArrayList<Chainable> cur = new ArrayList<Chainable>();

            for (int i = 0; i < order + 1; i++)
            {
                cur.add(list.get(i));
            }

            cursor = new Cursor(cur);
            cursorPos = order;

            return true;
        }

        return false;
    }

    // Advance cursor to next note, returns true if successful
    public boolean advanceCursor()
    {
        if (list.size() > cursorPos + 1)
        {
            for (int i = 0, l = cursor.getLen(); i < l; i++)
            {
                cursor.set(i, list.get(cursorPos - l + i + 2));
            }

            cursorPos++;

            return true;
        }

        return false;
    }

    // Print method to be implemented by inheritors
    // NOTE type T must have classToString method
    public void printChain()
    {
        System.out.print(T.classToString() + " (" + list.size() + "): [");
        for (T c : list)
        {
            System.out.print(" " + c.toString() + " ");
        }
        System.out.println("]");
    }

    // method to quantize the chain
    public void quantize()
    {
        for (Chainable c : list)
        {
            c.round();
        }
    }

    // returns list of objects (used mainly for decoding)
    public ArrayList<T> getList()
    {
        return list;
    }

    // appends another chain to the rear of this chain
    public void appendChain(Chain c)
    {
        list.addAll(c.getList());
    }

}

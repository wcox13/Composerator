import java.util.ArrayList;

public class SortedArrayList<T extends Comparable> extends ArrayList<T>
{
    // ASSUMES input list is sorted, adds a new element in correct place
    // ASSUMES comparable T, else throws cast exception
    public void insertSorted(T c)
    {
        for(int i = 0, len = this.size(); i < len; i++)
        {
            // if less than element, insert
            if (c.compareTo(this.get(i)) == -1)
            {
                this.add(i, c);
                return;
            }
        }

        // if greater than all elements, append
        this.add((T) c);
    }

    // binary search ASSUMES sorted
    // returns index of c, -1 if not found
    public static int binSearch(ArrayList<? extends Comparable> list, Comparable c, int minIndex, int maxIndex)
    {
        // value will be found at low index -- otherwise not found
        if (maxIndex - minIndex <= 0)
        {
            return -1;
        }

        // get mid
        int midIndex = (maxIndex + minIndex) / 2;

        Comparable midValue = list.get(midIndex);

        if (c.compareTo(midValue) == -1)
        {
            return binSearch(list, c, minIndex, midIndex);
        }

        else if (c.compareTo(midValue) == 1)
        {
            return binSearch(list, c, midIndex + 1, maxIndex);
        }
        else
        {
            return midIndex;
        }
    }
}

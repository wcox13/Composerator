import java.util.ArrayList;

public class MarkovRow<T extends Chainable> implements Comparable<MarkovRow<T>>
{
    // identifier, i.e. note(s) that we are finding the probability
    // of certain notes following
    private Cursor<T> id;

    // sorted list of probabilities
    // note is only added once it appears
    private SortedArrayList<MarkovEntry<T>> entries;

    // number of chainables that have been added to row
    private int numAdded;

    // keep track of normalized
    // also keeps track of whether the matrix is "done" (fully constructed)
    private Boolean normalized = false;

    // keep track of prepped for composition
    private Boolean prepared = false;

    public MarkovRow (Cursor c)
    {
        id = c;
        numAdded = 0;
        entries = new SortedArrayList<MarkovEntry<T>>();
    }

    // getters
    public Cursor getId()
    {
        return id;
    }

    public ArrayList<MarkovEntry<T>> getEntries()
    {
        return entries;
    }

    // setter
    public void setId(Cursor<T> id)
    {
        this.id = id;
    }

    // Method to divide each element of the row by the number of entries that are filled
    public void normalize()
    {
        for (MarkovEntry e : entries)
        {
            e.divide(numAdded);
        }

        normalized = true;
    }

    // prepares the row for probabilistic entry selection
    // makes it so that a random number generator can be used to select an element
    public void prepareForComposition()
    {
        if (normalized)
        {
            double sum = 0.;

            for (MarkovEntry e : entries) {
                double tmp = e.getProb();
                e.add(sum);
                sum += tmp;
            }

            prepared = true;
        }
    }

    // adds a chainable to the list of markov entries
    // if it already exists, updates counter
    public void add(T c)
    {
        MarkovEntry<T> newEntry = new MarkovEntry<T>(c);

        int index = SortedArrayList.binSearch(entries, newEntry, 0, entries.size());

        // if not found, insert new
        if (index == -1)
        {
            entries.insertSorted(newEntry);
        }

        // otherwise update
        else
        {
            entries.get(index).increment();
        }

        numAdded++;
    }

    // TODO
    // randomly pick a note from the row based on the probabilities
    // return null if not normalized and prepped (shouldn't happen anyway)
    public T choose()
    {
        if (normalized && prepared)
        {
            double rand = Math.random();

            for (MarkovEntry entry : entries)
            {
                if (rand < entry.getProb())
                {
                    return (T) entry.getValue();
                }
            }

            // should never happen
            return null;
        }

        else
        {
            return null;
        }
    }

    // Compare two rows by ID
    public int compareTo(MarkovRow r)
    {
        return id.compareTo(r.getId());
    }

    // increment the probability of a certain chainable
    public void increment(T c)
    {
        MarkovEntry<T> entry = new MarkovEntry<T>(c);

        int index = SortedArrayList.binSearch(entries, entry, 0, entries.size());

        if (index == -1)
        {
            entries.insertSorted(entry);
        }

        else
        {
            entries.get(index).increment();
        }

        numAdded++;
    }

    @Override public String toString()
    {
        String s = id.toString();

        if (entries.size() != 0)
        {
            for (MarkovEntry e : entries)
            {
                s += e.toString() + "  ";
            }
            return s + "\n";
        }
        else
        {
            return s + "<empty>";
        }
    }
}

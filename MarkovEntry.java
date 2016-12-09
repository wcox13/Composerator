public class MarkovEntry<T extends Chainable> implements Comparable<MarkovEntry<T>>
{
    private T value;
    private double prob;

    public MarkovEntry(T val)
    {
        value = val;
        prob = 1.;
    }

    public void setValue(T value)
    {
        this.value = value;
    }

    public void setProb(double prob)
    {
        this.prob = prob;
    }

    public T getValue()
    {
        return value;
    }

    public double getProb()
    {
        return prob;
    }

    // compare by value, ignoring probability
    public int compareTo(MarkovEntry<T> e)
    {
        return value.compareTo(e.getValue());
    }

    public void increment()
    {
        prob += 1.;
    }

    public void add(double d)
    {
        prob += d;
    }

    public void divide(double divisor)
    {
        prob /= divisor;
    }

    @Override public String toString()
    {
        return "(" + value.toString() + ", " + String.format("%1$,.2f", prob) + ")";
    }
}

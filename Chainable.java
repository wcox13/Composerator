// abstract class for elements in a markov chain (i.e. pitch, duration)
public abstract class Chainable implements Comparable<Chainable>
{

    // returns a description of the value
    public static String classToString()
    {
        return "";
    }

    // rounds a chainable based on its quantization constants
    // does nothing if the chainable is already discrete (i.e., pitch)
    public abstract void round();
}


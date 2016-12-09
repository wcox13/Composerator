import java.util.Random;

public class MarkovMatrix<T extends Chainable>
{

    // actual matrix of rows
    private SortedArrayList<MarkovRow<T>> matrix;

    // order of markov chain
    private int order;

    // builds a markovmatrix from a chain
    // contains probabilities
    public MarkovMatrix(Chain<T> chain, int order)
    {
        // set order
        this.order = order;

        // instantiate matrix
        matrix = new SortedArrayList<MarkovRow<T>>();

        // quantize chain
        chain.quantize();

        // set cursor to beginning
        chain.initCursor(order);

        // loop until end is reached
        do
        {
            // get current cursor
            Cursor<T> cursor = chain.getCursor();

            // get current note
            T c = cursor.getLast();

            // make new row with current id (cursor minus last element)
            MarkovRow<T> row = new MarkovRow<T>(cursor.stripLast());

            // search for row
            int rowIndex = SortedArrayList.binSearch(matrix, row, 0, matrix.size());

            // add new row if not found
            if (rowIndex == -1)
            {
                row.add(c);
                matrix.insertSorted(row);
            }

            // otherwise update row
            else
            {
                matrix.get(rowIndex).increment(c);
            }

        } while (chain.advanceCursor());

        // normalize matrix
        normalize();
    }

    public SortedArrayList<MarkovRow<T>> getMatrix()
    {
        return matrix;
    }

    // use the matrix to compose a piece of a given length
    public Chain<T> compose(int length)
    {
        prepareForComposition();

        Chain<T> chain = new Chain<T>(chooseStart().getEntries());

        chain.initCursor(order - 1);

        MarkovRow<T> dummyRow = new MarkovRow<T>(chain.getCursor());

        T nextNote;

        for (int i = 0; i < length - 2; i++)
        {
            int index = SortedArrayList.binSearch(matrix, dummyRow, 0, matrix.size());

            // happens very rarely -- if the end of the piece is a "dead end"
            if (index == -1)
            {
                index = (new Random()).nextInt(matrix.size());
            }

            nextNote = matrix.get(index).choose();

            chain.addToChain(nextNote);
            chain.advanceCursor();
            dummyRow.setId(chain.getCursor());
        }

        return chain;
    }

    // chooses a random starting sequence from the row identifiers
    private Cursor<T> chooseStart()
    {
        Random rand = new Random();

        int randIndex = rand.nextInt(matrix.size());

        return matrix.get(randIndex).getId();
    }

    // normalize all entries
    private void normalize()
    {
        for (MarkovRow<T> row : matrix)
        {
            row.normalize();
        }
    }

    // prepare each row (see MarkovRow prepareForComposition)
    private void prepareForComposition()
    {
        for (MarkovRow<T> row : matrix)
        {
            row.prepareForComposition();
        }
    }

    @Override public String toString()
    {
        String s = "";

        for (int i = 0, len = matrix.size(); i < len; i++)
        {
            s += "(" + i + ") " + matrix.get(i).toString();
        }

        return s;
    }
}

import java.util.ArrayList;

public class Pitch extends Chainable {

    // i.e. "C" or "B"
    private String pitchClass;

    // midi identifier
    private int midiId;

    /*
     * midi_id corresponds to the following midi code taken from:
     * http://www.midimountain.com/midi/midi_note_numbers.html
     *
     * Octave |   C |  C# |   D |  D# |   E |   F |  F# |   G |  G# |   A |  A# |   B
     *      0 |   0 |   1 |   2 |   3 |   4 |   5 |   6 |   7 |   8 |   9 |  10 |  11
     *      1 |  12 |  13 |  14 |  15 |  16 |  17 |  18 |  19 |  20 |  21 |  22 |  23
     *      2 |  24 |  25 |  26 |  27 |  28 |  29 |  30 |  31 |  32 |  33 |  34 |  35
     *      3 |  36 |  37 |  38 |  39 |  40 |  41 |  42 |  43 |  44 |  45 |  46 |  47
     *      4 |  48 |  49 |  50 |  51 |  52 |  53 |  54 |  55 |  56 |  57 |  58 |  59
     *      5 |  60 |  61 |  62 |  63 |  64 |  65 |  66 |  67 |  68 |  69 |  70 |  71
     *      6 |  72 |  73 |  74 |  75 |  76 |  77 |  78 |  79 |  80 |  81 |  82 |  83
     *      7 |  84 |  85 |  86 |  87 |  88 |  89 |  90 |  91 |  92 |  93 |  94 |  95
     *      8 |  96 |  97 |  98 |  99 | 100 | 101 | 102 | 103 | 104 | 105 | 106 | 107
     *      9 | 108 | 109 | 110 | 111 | 112 | 113 | 114 | 115 | 116 | 117 | 118 | 119
     *     10 | 120 | 121 | 122 | 123 | 124 | 125 | 126 | 127
     *
     * Rest is given the midi value of 128 for the purpose of this program
     */

    // integer representing the pitch's octave on the piano (i.e. 4)
    private int octave;

    // int corresponding to the frequency of the pitch in Hertz
    private int frequency;

    // true/false if flat or sharp
    private boolean flat;
    private boolean sharp;

    // default constructor for rest pitches
    public Pitch()
    {
        // rest values
        pitchClass = "R";
        octave = 0;
        midiId = 128;
    }

    // Pitch note constructor
    public Pitch(String note, int oct, int id)
    {
        octave = oct;
        pitchClass = note;
        midiId = id;

        if (note.contains("#")) { sharp = true; }
        if (note.contains("b")) { flat = true; }
    }

    // description method to print
    public String toString() { return pitchClass + octave; }

    // get class name
    public static String classToString() { return "Pitch"; }

    // getter methods for instance variables
    public String getPitchClass()
    {
        return pitchClass;
    }

    public int getOctave()
    {
        return octave;
    }

    public boolean getFlat()
    {
        return flat;
    }

    public boolean getSharp()
    {
        return sharp;
    }

    public int getMidiId()
    {
        return midiId;
    }

    public int compareTo(Chainable p) {
        String pPitchClass = ((Pitch) p).getPitchClass();
        int pMidiId = ((Pitch) p).getMidiId();

        if (pitchClass.equals("R") && pPitchClass.equals("R"))
        {
            return 0;
        }
        else if (pPitchClass.equals("R"))
        {
            return 1;
        }
        else if (pitchClass.equals("R"))
        {
            return -1;
        }
        else if (midiId < pMidiId)
        {
            return -1;
        }
        else if (midiId > pMidiId)
        {
            return 1;
        }

        return 0;
    }


    // compare to method (not used)
//    public int compareTo(Chainable p)
//    {
//        String p_pitch_class = ((Pitch) p).getPitch_class();
//
//        // check octave first
//        if (octave < ((Pitch) p).getOctave())
//        {
//            return -1;
//        }
//
//        else if (octave > ((Pitch) p).getOctave())
//        {
//            return 1;
//        }
//
//        // check same note/rest
//        else if (p_pitch_class.equals(pitch_class))
//        {
//            return 0;
//        }
//
//        // check rest; rest is always lower
//        else if (pitch_class.equals("R") && p_pitch_class.equals("R"))
//        {
//            return 0;
//        }
//
//        else if (p_pitch_class.equals("R"))
//        {
//            return 1;
//        }
//
//        else if (pitch_class.equals("R"))
//        {
//            return -1;
//        }
//
//        // compare notes. even if note sounds the same, note with lower letter is lower
//        else
//        {
//            int comp = pitch_class.compareTo(p_pitch_class);
//
//            if (comp == 1 || comp == -1)
//                return comp;
//
//            // check sharp/flat
//            else if (sharp)
//            {
//                if (((Pitch) p).getSharp())
//                {
//                    return 0;
//                }
//                else
//                {
//                    return 1;
//                }
//            }
//
//            else if (flat)
//            {
//                if (((Pitch) p).getFlat())
//                {
//                    return 0;
//                }
//                else
//                {
//                    return -1;
//                }
//            }
//
//            else
//            {
//                if (((Pitch) p).getSharp())
//                {
//                    return -1;
//                }
//                else if (((Pitch) p).getFlat())
//                {
//                    return 1;
//                }
//                else
//                {
//                    return 0;
//                }
//            }
//        }
//    }

    // dummy method -- pitch is already quantized
    public void round() {}
}
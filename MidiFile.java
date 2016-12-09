import java.io.*;
import javax.sound.midi.*;

public class MidiFile {

    // variable used to decode the midi file
    private static final int NOTE_ON = 0x90;
    private static final int NOTE_OFF = 0x80;
    private static final String[] NOTE_CLASSES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    private static final int NOTES_PER_OCTAVE = 12;

    // midi sequence
    private Sequence midiSequence;

    // timing resolution
    private float timingResolution;

    // default constructor
    public MidiFile()
    {
        midiSequence = null;
    }

    // constructor for a MidiFile object
    public MidiFile(Sequence s)
    {
        // Import sequence from file path (catch exceptions)
        // set sequence
        midiSequence = s;

        // extract timing resolution
        timingResolution = (float) midiSequence.getResolution();
    }

    // decodes the midi file to a song object
    public Song toSong()
    {
        // initialize all the chain variables
        Chain pitchChain = new Chain<Pitch>();
        Chain volumeChain = new Chain<Volume>();
        Chain durationChain = new Chain<Duration>();
        Chain noteChain = new Chain<Note>();

        // iterate through MIDI tracks
        int trackNumber = 0;

        // iterate through available tracks
        for (Track track :  midiSequence.getTracks())
        {
            // for labeling track number
            trackNumber++;

            // info tracks usually have a small track size (around 5 - 10 pieces of info)
            // actual music tracks are generally on the scale of hundreds
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();

            // starting and ending ticks of a MIDI note
            long noteTickStart = 0;
            long noteTickEnd;

            // starting and ending ticks of a MIDI rest
            long restTickStart = 0;
            long restTickEnd;

            // starting and ending velocities of MIDI note
            int velocityStart = 0;
            int velocityEnd;

            // iterate through each message in the track
            for (int i = 0; i < track.size(); i++)
            {
                // midi events occur at a specific 'tick rate' which is
                // analogous to sampling rate for an analog signal
                MidiEvent event = track.get(i);

                // tick is the clock value of the midi event
                long tick = event.getTick();

                // Java's midi library encodes midi events as 'messages'
                MidiMessage message = event.getMessage();

                if (message instanceof ShortMessage)
                {
                    ShortMessage sm = (ShortMessage) message;

                    // key pressed
                    int key = sm.getData1();
                    System.out.println(key);

                    // note's octave
                    int octave = (key / NOTES_PER_OCTAVE) - 1;

                    // note class
                    int note = key % NOTES_PER_OCTAVE;
                    String noteName = NOTE_CLASSES[note];

                    // note's velocity (eventually mapped to volume)
                    int velocity = sm.getData2();

                    // NOTE STATES
                    if (sm.getCommand() == NOTE_ON)
                    {
                        // note has begun to be registered, rest has ended
                        noteTickStart = tick;
                        velocityStart = velocity;
                        restTickEnd = tick;

                        // insert a rest object into all 4 chains with rest-specific values for each

                        // initialize new rest with just a duration (default values of
                        // pitch and volume are encapsulated in the rest constructor)
                        Duration restDuration = new Duration(restTickStart, restTickEnd, timingResolution);

                        // only generate and add the rests if they last for a usable amount of time
                        // i.e. notes aren't directly after one another
                        if (restDuration.getTime() > 0.0)
                        {
                            System.out.println("Rested for: " + restDuration.getTime());

                            // create a new rest object (using duration constructor)
                            Rest currentRest = new Rest(restDuration);

                            // add rest versions of each object to their respective chains
                            pitchChain.addToChain(new Pitch());
                            volumeChain.addToChain(new Volume());
                            durationChain.addToChain(restDuration);

                            // add raw rest (since it's a subclass of Note) to the note chain
                            noteChain.addToChain(currentRest);
                        }

                        System.out.println("Note begun: " + noteName);

                    }
                    else if (sm.getCommand() == NOTE_OFF)
                    {
                        // note has registered, rest has begun
                        noteTickEnd = tick;
                        velocityEnd = velocity;
                        restTickStart = tick;

                        // create a new pitch object
                        Pitch currentPitch = new Pitch(noteName, octave, key);

                        // create a new volume object
                        Volume currentVolume = new Volume(velocityStart, velocityEnd);

                        // create a new duration object (passing timing resolution)
                        Duration currentDuration = new Duration(noteTickStart, noteTickEnd, timingResolution);

                        // encapsulate a new note object
                        Note currentNote = new Note(currentPitch, currentVolume, currentDuration);

                        // print out current note description
                        System.out.println("Note ended: " + currentNote.toString());

                        // add current values to chains
                        pitchChain.addToChain(currentPitch);
                        volumeChain.addToChain(currentVolume);
                        durationChain.addToChain(currentDuration);
                        noteChain.addToChain(currentNote);

                    }
                    else
                    {
                        System.out.println("Other command: " + sm.getCommand());
                    }
                }
                else if (message instanceof MetaMessage)
                {
                    MetaMessage mm = (MetaMessage) message;
                    System.out.println("Meta Message: " + mm.toString());
                }
                else
                {
                    System.out.println("Other message: " + message.getClass());
                }
            }
            System.out.println();
        }

        // return the compiled song (also print song) (pass in timing resolution for output reasons)
        return new Song(pitchChain, durationChain, volumeChain, noteChain, timingResolution);
    }

    // getter method for timing resolution
    public float getTimingResolution()
    {
        return timingResolution;
    }

    // creates an actual midi file and writes it to the directory specified
    public void writeToFilePath(String filePath)
    {
        // write midi sequence to file
        File f = new File(filePath);
        try
        {
            MidiSystem.write(midiSequence, 1, f);
        }
        // file wasn't successfully written
        catch (IOException e)
        {
            System.out.println(e.toString());
        }
    }
}


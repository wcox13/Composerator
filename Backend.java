import java.util.ArrayList;
import javax.sound.midi.*;
import java.io.File;

// class to encapsulate the operation of taking input songs and
public class Backend
{
    // constructor for a composeration
    public static void composerate(ArrayList<String> filePaths, int order, int outputLength, String outFilePath)
    {
        // filePaths: an array list of filepaths of input files
        // order: the desired order of the markov matrix
        // output length: the desired number of notes in the output file
        // outfilepath: path destination to which to write the otuput midi file

        // load the sequence from the file path
        try
        {
            // an array list to hold the multiple song objects from
            // each of the input midi files
            ArrayList<Song> songs = new ArrayList<Song>();

            // for each file passed as input -> create a midi sequence
            for (String path : filePaths)
            {
                // strip out the midi sequence
                Sequence ms = MidiSystem.getSequence(new File(path));

                // initialize MIDI file with the midi sequence
                MidiFile midi = new MidiFile(ms);

                // decode midi file to song
                Song midiSong = midi.toSong();

                System.out.println("\nMIDI TO SONG SUCCESSFUL");

                // add to array list
                songs.add(midiSong);
            }

            System.out.println("\nALL SONGS SUCCESSFULLY ADDED");

            // combine all the songs into one new one
            Song combination = songs.get(0);
            for (int i = 1, len = songs.size(); i < len; i++)
            {
                combination.appendSong(songs.get(i));
            }

            System.out.println("SONGS APPENDED");

            Song output = MarkovProcess(combination, order, outputLength);

            System.out.println("MARKOV DONE");

            // create a midi file object from the song
            MidiFile midiOut = output.toMidiFile();

            // write the midi file out to path
            midiOut.writeToFilePath(outFilePath);

        }
        catch (Exception e)
        {
            // file doesn't exist
            System.out.println(e.toString());
        }
    }

    private static Song MarkovProcess(Song input, int order, int len)
    {
        // do Markov analysis on each attribute
        MarkovMatrix<Pitch> pitchMatrix = new MarkovMatrix<Pitch>(input.getPitchChain(), order);
        MarkovMatrix<Duration> durationMatrix = new MarkovMatrix<Duration>(input.getDurationChain(), order);
        MarkovMatrix<Volume> volumeMatrix = new MarkovMatrix<Volume>(input.getVolumeChain(), order);

        // compose new chain for each attribute
        Chain<Pitch> pitchChain = pitchMatrix.compose(len);
        Chain<Duration> durationChain = durationMatrix.compose(len);
        Chain<Volume> volumeChain = volumeMatrix.compose(len);

        // create note chain
        Chain<Note> noteChain = new Chain<Note>();

        pitchChain.printChain();
        durationChain.printChain();
        volumeChain.printChain();

        for (int i = 0; i < len; i++)
        {
            Note n = new Note(pitchChain.getList().get(i), volumeChain.getList().get(i),
                    durationChain.getList().get(i));

            noteChain.addToChain(n);
        }

        // create output song
        return new Song(pitchChain, durationChain, volumeChain, noteChain, input.getTimebase());
    }
}

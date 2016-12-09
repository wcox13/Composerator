import javax.sound.midi.*;
import java.nio.ByteBuffer;

public class Song {

    // midi sequence
    private Sequence midiSequence;

    // current midi track
    Track currentTrack;

    // chains
    private Chain pitchChain;
    private Chain durationChain;
    private Chain volumeChain;
    private Chain noteChain;

    // pulses per quarter note
    private float timebase;

    // constructor that takes four chains and a timebase
    public Song(Chain pc, Chain dc, Chain vc, Chain nc, float ppqn) {
        pitchChain = pc;
        durationChain = dc;
        volumeChain = vc;
        noteChain = nc;
        timebase = ppqn;

        printSong();
    }

    // prints out song to console (prints individual chains)
    public void printSong() {
        pitchChain.printChain();;
        volumeChain.printChain();
        durationChain.printChain();
        noteChain.printChain();
    }

    // getters
    public Chain getVolumeChain()
    {
        return volumeChain;
    }

    public Chain getDurationChain()
    {
        return durationChain;
    }

    public Chain getPitchChain()
    {
        return pitchChain;
    }

    public float getTimebase()
    {
        return timebase;
    }


    /*
        Information on MIDI file formats adapted from:
        - http://www.fileformat.info/format/midi/corion.htm
        - http://www.midi.org/techspecs/midimessages.php
        - http://www.digitalpreservation.gov/formats/fdd/fdd000119.shtml
        - http://docs.oracle.com/javase/8/docs/api/javax/sound/midi/spi/MidiFileWriter.html
        - http://www.automatic-pilot.com/midifile.html

        GENERAL FORM OF A MIDI FILE

        - File header (14 constant bytes)
        - Track header (4 constant four bytes)
        - 4 bytes to indicate amount of track data (including footer) NOTE: big-endian
        - Track Data
            - Metadata events (tempo, key signature, time signature)
            - Performance events (notes, controller changes etc.)
        - Track footer (4 constant bytes)

        TIMEBASES

        - tempo of track is expressed in microseconds per quarter note
            - manually choose a multiplier: 16
                - longest note: 64 ticks
                - shortest note: 1 tick
            - set the timebase fairly low (fewer bits to represent each value)

        DELTAS

        - expressed in number of ticks
        - "delta = 0" means "do this as close as possible to previous event"
        - rest: turn note off - wait for N clicks - turn next note on again
        - have three notes playing at same time: turn 1st off at 16, then turn others off at 0 (since it's
          measured with respect to the last note)

        EVENT FORMAT

        0x90 --> (byte representing note) --> (byte representing strike velocity)

     */

    // static bits and values
    private static final int START_TICK = 0;
    private static final int NOTE_ON = 0x90;
    private static final int NOTE_OFF = 0x80;
    private static final int SET_TEMPO = 0x51;
    private static final int SET_TRACK_NAME = 0x03;
    private static final int SET_END_OF_TRACK = 0x2F;

    // tick counter for going through song
    int currentTick = START_TICK;

    // desired output tempo (maybe have the user specify this) ?
    float outputTempo = 120;

    // converts the song back to a midi file
    public MidiFile toMidiFile() {
        try {
            // new sequence with song's num of ticks per beat  ****
            midiSequence = new Sequence(javax.sound.midi.Sequence.PPQ, (int) timebase);

            // create first track
            currentTrack = midiSequence.createTrack();

            ////////////////////////////////////////////////////////
            /////////////////////// HEADER /////////////////////////
            ////////////////////////////////////////////////////////

            // general MIDI configuration
            // have to do (byte) casts because Java has unsigned int problems
            byte[] b = {(byte) 0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte) 0xF7};
            SysexMessage sm = new SysexMessage();
            sm.setMessage(b, 6);

            MidiEvent me = new MidiEvent(sm, START_TICK);
            currentTrack.add(me);

            // calculate tempo in bytes
            float microPerMinute = 60000000;
            int microPerPulse = (int) (microPerMinute / outputTempo);
            byte[] bytes = ByteBuffer.allocate(4).putInt(microPerPulse).array();

            // three bytes represent number of microseconds per pulse
            byte[] bt = {bytes[1], bytes[2], bytes[3]};
            writeMetaEvent(SET_TEMPO, bt, 3, START_TICK);

            // set track name (meta event)
            String TrackName = "Composerator Track 1";
            writeMetaEvent(SET_TRACK_NAME, TrackName.getBytes(), TrackName.length(), START_TICK);

            // set omni on
            writeShortEvent(0xB0, 0x7D, 0x00, START_TICK);

            // set poly on
            writeShortEvent(0xB0, 0x7F, 0x00, START_TICK);

            // set instrument to Piano
            writeShortEvent(0xC0, 0x00, 0x00, START_TICK);

            ////////////////////////////////////////////////////////
            //////////////////////// BODY //////////////////////////
            ////////////////////////////////////////////////////////

            // iterate through note chain and call note events on each note
            for (Object c : noteChain.getList())
            {
                // cast object to Note class
                noteEvent((Note) c);
            }

            ////////////////////////////////////////////////////////
            ////////////////////// FOOTER //////////////////////////
            ////////////////////////////////////////////////////////

            // set end of track
            byte[] bet = {}; // empty array
            writeMetaEvent(SET_END_OF_TRACK, bet, 0, currentTick);

        }
        catch (Exception e)
        {
            System.out.println("Exception caught " + e.toString());
        }

        // return a new MIDI file object with the constructed midi sequence
        return new MidiFile(midiSequence);
    }

    ////////////////////////////////////////////////////////
    //////////////// MIDI HELPER METHODS ///////////////////
    ////////////////////////////////////////////////////////

    static final int rest = 128;

    // encapsulation method to map note to midi event
    private void noteEvent(Note n) {
        // maps string to a byte value
        Pitch p = n.getPitch();

        Volume v = n.getVolume();
        Duration d = n.getDuration();

        int endingTick = currentTick + (int) d.getTickLength();

        int note = p.getMidiId();
        if (note != rest)
        {
            int vel = v.getMidiVelocity();
            writeShortEvent(NOTE_ON, note, vel, currentTick);
            writeShortEvent(NOTE_OFF, note, vel, endingTick);

            // set current tick to latest tick val
            currentTick = endingTick;
        }
    }

    // write a meta event to current track at certain tick
    private void writeMetaEvent(int id, byte[] val, int b3, int tick) {
        MetaMessage mt = new MetaMessage();
        try {
            mt.setMessage(id, val, b3);
        } catch (InvalidMidiDataException e) {
            System.out.println(e.toString());
        }
        MidiEvent me = new MidiEvent(mt, (long) tick);
        currentTrack.add(me);
    }

    // write a short message to current track at certain tick
    private void writeShortEvent(int id, int val, int vel, int tick) {
        ShortMessage sm = new ShortMessage();
        try {
            sm.setMessage(id, val, vel);
        } catch (InvalidMidiDataException e) {
            System.out.println(e.toString());
        }
        MidiEvent me = new MidiEvent(sm, (long) tick);
        currentTrack.add(me);
    }

    // appends a song to this current song
    public void appendSong(Song s)
    {
        // append all the individual chains within the song
        noteChain.appendChain(s.noteChain);
        pitchChain.appendChain(s.pitchChain);
        volumeChain.appendChain(s.volumeChain);
        durationChain.appendChain(s.durationChain);
    }
}
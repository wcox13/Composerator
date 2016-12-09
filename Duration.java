import java.util.ArrayList;

public class Duration extends Chainable {

    // length of the duration
    private double time_s;
    private long tickLength;

    // tolerance for comparison
    private final double tolerance = 1e-6;

    // quantization step size
    private final double stepSize = 0.07;

    // tick rate
    // MIDI files were generated at 960 PPQN (pulses per quarter note)
    private final double PPQN;

    // THIS IS EMPIRICALLY DETERMINED FROM THE INPUT FILE BUT NEED TO FIND
    // A WAY OF DEDUCING IT FROM THE MIDI FILE

    // in quarter / min
    private static final double tempo = 100.0;

    // pulses per second (1600 @ 100 bpm)
    private final double tick_rate;

    // main constructor that takes starting and ending tick values
    public Duration(long tick1, long tick2, float ppqn)
    {
        // set timebase variables according to input
        PPQN = ppqn;
        tick_rate = (tempo / 60.0) * PPQN;

        tickLength = tick2 - tick1;
        // do math to determine actual duration in seconds
        time_s = (tickLength / tick_rate);
    }

    public double getTime()
    {
        return time_s;
    }

    public long getTickLength()
    {
        return tickLength;
    }

    @Override
    public String toString()
    {
        return String.format("%1$,.2f", time_s);
    }

    // ASSUMES chainable is a duration
    public int compareTo(Chainable d)
    {
        double diff = time_s - ((Duration) d).getTime();

        if (Math.abs(diff) < tolerance) return 0;

        else if (diff < 0) return -1;

        else return 1;
    }

    // return the tick rate --> used in the output of the midi file
    public double getTick_rate()
    {
        return tick_rate;
    }

    // return the ppqn --> used in determining other time-related constants in other classes
    public double getPPQN()
    {
        return PPQN;
    }

    // round up to nearest quantization step
    public void round()
    {
        time_s = time_s - (time_s % stepSize) + stepSize;
    }

}

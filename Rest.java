// subclass of note
public class Rest extends Note {

    // define instance variables as pre-determined rest value
    public Rest(Duration d)
    {
        this.setPitch(new Pitch());
        this.setVolume(new Volume());
        this.setDuration(d);
    }
}

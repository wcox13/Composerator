# Composerator
Higher order Markov chain algorithm to automatically compose music.

## Background
This project (developed with Garrett Parrish) analyzes music probabilistically using higher 
order Markov chains.  Using the data learned from this process, the machine can then 
probabilistically compose a new piece of music.  The result is a new, unique composition 
that is similar in character to the input piece.  In addition, the project supports adding 
multiple input pieces, in which case the style of the output will be a combination of the styles of the inputs.

See `Report.pdf` for a detailed description of the project.

## Usage
Note that you will need some program to play back the MIDI file (GarageBand, Sibelius, or Finale will work fine).  
To try it out, download or clone the directory and navigate to it in your terminal. Type

`make`

then type

`java Main`

You will see a UI pop up.  Click the "File 1" button and select "Bach Sample File.mid" in the project directory;
This is a MIDI file of one of Bach's famous Cello Suites.  You may tinker with the Markov chain order and the output
length, but the default values (3 and 500) will work well for this test file. Click save destination and select a folder
in which you would like the output file to save, the click Composerate!

Once finished, you can open the resulting MIDI file in one of the programs listed above to listen to it. You should hear
a natural, Bach-like, machine-composed melody!

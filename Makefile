JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java
	
CLASSES = \
	Backend.java \
	Chain.java \
	Chainable.java \
	Cursor.java \
	Duration.java \
	MainUIFrame.java \
	MarkovEntry.java \
	MarkovMatrix.java \
	MarkovRow.java \
	MidiFile.java \
	Note.java \
	Pitch.java \
	Rest.java \
	Song.java \
	SortedArrayList.java \
	Volume.java \
	Main.java
default: classes
classes: $(CLASSES:.java=.class)
clean:
	$(RM) *.class
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainUIFrame extends JFrame
{
    // buttons
    JButton chooseFile1, chooseFile2, chooseFile3;
    JButton cancelFile1, cancelFile2, cancelFile3;
    JButton composerateButton;

    // interfaces for displaying status of file uploads
    JTextArea file1Log, file2Log, file3Log;
    JScrollPane log1ScrollPane, log2ScrollPane, log3ScrollPane;
    private static final String NOFILE = "NOFILE";
    private static final String FILE_NOT_SELECTED = "No File Selected.";

    // file paths
    String file1Path = NOFILE, file2Path = NOFILE, file3Path = NOFILE;
    String destinationPath = NOFILE;
    ArrayList<String> filePaths = new ArrayList<String>();

    // input text fields
    JTextField chainOrder;
    JTextField outputLength;

    // create a grid layout with 4 rows and two columns
    GridLayout experimentLayout = new GridLayout(4,1);

    // file chooser for picking files and directories
    JFileChooser fc = new JFileChooser();

    // main constructor
    public MainUIFrame(String name)
    {
        super(name);
        setResizable(false);
    }

    public void addComponentsToPane(final Container pane)
    {

/* MEANT FOR BETTER DESIGN OF THIS SECTION
        // array lists for iterating through relevant variables when initializing the UI
        ArrayList<JButton> chooseFileButtons = new ArrayList<JButton>(){{add(chooseFile1); add(chooseFile2); add(chooseFile3);}};
        ArrayList<String> chooseFileButtonDescriptions = new ArrayList<String>(){{add("File 1"); add("File 2"); add("File 3");}};
        String removeButtonDescription = "Remove";
        ArrayList<JButton> cancelFileButtons = new ArrayList<JButton>(){{add(cancelFile1); add(cancelFile2); add(cancelFile3);}};
        ArrayList<JTextArea> fileLogs = new ArrayList<JTextArea>(){{add(file1Log); add(file2Log); add(file3Log);}};
        ArrayList<JScrollPane> logScrollPanes = new ArrayList<JScrollPane>(){{add(log1ScrollPane); add(log2ScrollPane); add(log3ScrollPane);}};
*/

        // create main panel
        final JPanel fileInput = new JPanel();

        // set layout to grid
        fileInput.setLayout(experimentLayout);

        // instructions panel
        JTextArea instructions = new JTextArea(1,20);
        instructions.setMargin(new Insets(5, 5, 5, 5));
        instructions.setEditable(false);
        instructions.setText("Welcome to Composerator! Please select up to three MIDI files that you wish to Composerate!");

        JPanel instructionsPanel = new JPanel();
        instructionsPanel.add(instructions);
        instructionsPanel.setPreferredSize(new Dimension(50,10));
        fileInput.add(instructionsPanel);

        // set frame size based on description label
        Dimension buttonSize = instructions.getPreferredSize();
        fileInput.setPreferredSize(new Dimension((int)(buttonSize.getWidth()),
                (int)(buttonSize.getHeight()) * 6 ));

        ///////////////////////////////////////////////////////////////

        // create three choose file buttons (one for each optional midi file)
        chooseFile1 = new JButton("File 1");
        cancelFile1 = new JButton("Remove");

        // text box to print status updates
        file1Log = new JTextArea(1,20);
        formatTextPanel(file1Log);
        log1ScrollPane = new JScrollPane(file1Log);

        // create panel and add buttons and log
        final JPanel chooseFile1Panel = new JPanel();
        chooseFile1Panel.add(chooseFile1, BorderLayout.WEST);
        chooseFile1Panel.add(log1ScrollPane, BorderLayout.CENTER);
        chooseFile1Panel.add(cancelFile1, BorderLayout.EAST);
        fileInput.add(chooseFile1Panel);

        ///////////////////////////////////////////////////////////////

        // create three choose file buttons (one for each optional midi file)
        chooseFile2 = new JButton("File 2");
        cancelFile2 = new JButton("Remove");

        // text box to print status updates
        file2Log = new JTextArea(1,20);
        formatTextPanel(file2Log);
        log2ScrollPane = new JScrollPane(file2Log);

        // create panel and add buttons and log
        JPanel chooseFile2Panel = new JPanel();
        chooseFile2Panel.add(chooseFile2, BorderLayout.WEST);
        chooseFile2Panel.add(log2ScrollPane, BorderLayout.CENTER);
        chooseFile2Panel.add(cancelFile2, BorderLayout.EAST);
        fileInput.add(chooseFile2Panel);

        ///////////////////////////////////////////////////////////////

        // create three choose file buttons (one for each optional midi file)
        chooseFile3 = new JButton("File 3");
        cancelFile3 = new JButton("Remove");

        // text box to print status updates
        file3Log = new JTextArea(1,20);
        formatTextPanel(file3Log);
        log3ScrollPane = new JScrollPane(file3Log);

        // create panel and add buttons and log
        JPanel chooseFile3Panel = new JPanel();
        chooseFile3Panel.add(chooseFile3, BorderLayout.WEST);
        chooseFile3Panel.add(log3ScrollPane, BorderLayout.CENTER);
        chooseFile3Panel.add(cancelFile3, BorderLayout.EAST);
        fileInput.add(chooseFile3Panel);

        ///////////////////////////////////////////////////////////////
        ///////////////// FILE SELECTION HANDLERS /////////////////////
        ///////////////////////////////////////////////////////////////

        // file 1 select handler
        chooseFile1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                    int returnVal = fc.showOpenDialog(MainUIFrame.this);

                    if (returnVal == JFileChooser.APPROVE_OPTION)
                    {
                        File file = fc.getSelectedFile();
                        System.out.println("File 1: " + file.getAbsolutePath());
                        file1Log.setText("File Selected: " + file.getName());
                        file1Path = file.getAbsolutePath();
                        filePaths.add(file1Path);
                    }
                    else
                    {
                        file1Log.setText(FILE_NOT_SELECTED);
                    }
            }
        });

        // file 2 select handler
        chooseFile2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int returnVal = fc.showOpenDialog(MainUIFrame.this);

                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    File file = fc.getSelectedFile();
                    System.out.println("File 2: " + file.getAbsolutePath());
                    file2Log.setText("File Selected: " + file.getName() );
                    file2Path = file.getAbsolutePath();
                    filePaths.add(file2Path);
                }
                else
                {
                    file2Log.setText(FILE_NOT_SELECTED);
                }
            }
        });

        // file 3 select handler
        chooseFile3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int returnVal = fc.showOpenDialog(MainUIFrame.this);

                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    File file = fc.getSelectedFile();
                    System.out.println("File 3: " + file.getAbsolutePath());
                    file3Log.setText("File Selected: " + file.getName() );
                    file3Path = file.getAbsolutePath();
                    filePaths.add(file3Path);
                }
                else
                {
                    file3Log.setText(FILE_NOT_SELECTED);
                }
            }
        });

        ///////////////////////////////////////////////////////////////
        ///////////////// CANCEL BUTTON HANDLERS //////////////////////
        ///////////////////////////////////////////////////////////////

        // cancel 1 handler
        cancelFile1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.out.println("Removed file 1");
                file1Log.setText(FILE_NOT_SELECTED);
                file1Path = NOFILE;

                // remove that object from file paths array
                filePaths.remove(filePaths.indexOf(file1Path));
            }
        });

        // cancel 2 handler
        cancelFile2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.out.println("Removed file 2");
                file2Log.setText(FILE_NOT_SELECTED);
                file2Path = NOFILE;

                // remove that object from file paths array
                filePaths.remove(filePaths.indexOf(file2Path));
            }
        });

        // cancel 3 handler
        cancelFile3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.out.println("Removed file 3");
                file3Log.setText(FILE_NOT_SELECTED);
                file3Path = NOFILE;

                // remove that object from file paths array
                filePaths.remove(filePaths.indexOf(file3Path));
            }
        });

        ///////////////////////////////////////////////////////////////
        //////////////////// CONTROL PANEL INITS //////////////////////
        ///////////////////////////////////////////////////////////////

        // panel objects
        JPanel controls = new JPanel();
        controls.setLayout(new GridLayout(3,1));

        // two labels for buttons
        controls.add(new Label("Markov Chain Order:"));
        controls.add(new Label("Output Length (Notes):"));

        // create and add textfields for order and length
        chainOrder = new JTextField("3");
        outputLength = new JTextField("500");
        controls.add(chainOrder);
        controls.add(outputLength);

        // output file path
        final JButton outFilePath = new JButton("Save Destination");
        controls.add(outFilePath);

        // destination path handler
        outFilePath.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                // start a file chooser and set
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showOpenDialog(MainUIFrame.this) == JFileChooser.APPROVE_OPTION)
                {
                    destinationPath = chooser.getCurrentDirectory().getPath();
                    System.out.print(destinationPath);
                    outFilePath.setText(destinationPath.substring(destinationPath.lastIndexOf('/') + 1));
                }
                else
                {
                    destinationPath = NOFILE;
                }
            }
        });

        // create, add, and tie an action listener to a composerate button
        // pressing this button when you have uploaded at least one file
        // and specified a destination path will begin the markov analysis
        composerateButton = new JButton("Composerate");
        controls.add(composerateButton);

        // composerate -- trigger actual events of stuff
        composerateButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int len = Integer.parseInt(outputLength.getText());
                int order = Integer.parseInt(chainOrder.getText());
                System.out.println("Destination: "  + destinationPath);

                // if there is at least one file and there is a destination path
                if ((filePaths.size() > 0) && (!destinationPath.equals(NOFILE)))
                {
                    // get current date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date today = new Date();
                    String dateStr = sdf.format(today);

                    // set filename
                    destinationPath += "/Composerator-" + dateStr + ".mid";
                    Backend.composerate(filePaths, order, len, destinationPath);
                }
            }
        });

            // add file input on the top
            pane.add(fileInput, BorderLayout.NORTH);

            // separate controls and file input
            pane.add(new JSeparator(), BorderLayout.CENTER);

            // add control panel to the bottom
            pane.add(controls,BorderLayout.SOUTH);

            // initialize file states
            file1Log.setText(FILE_NOT_SELECTED);
            file2Log.setText(FILE_NOT_SELECTED);
            file3Log.setText(FILE_NOT_SELECTED);
        }

    // create UI and make it visible
    private static void createAndShowGUI()
    {
        // create and set up the main window
        MainUIFrame frame = new MainUIFrame("Composerator");

        // set close method
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // initialize all content within the window
        frame.addComponentsToPane(frame.getContentPane());

        // make window visible
        frame.pack();
        frame.setVisible(true);
    }

    // helper method for formatting text areas (for standardization)
    private void formatTextPanel(JTextArea t)
    {
        t.setMargin(new Insets(5,5,5,5));
        t.setEditable(false);
    }

    // main method for running this class's initialization
    public void start()
    {
        // schedule a job for the event dispatch thread:
        // creating and showing GUI
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
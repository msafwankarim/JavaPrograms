package app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class StaffForm extends JFrame implements ActionListener {
    JTable table;       //This will hold the consumer records
    JPanel leftPan;     //panel to hold buttons on the left
    DefaultTableModel tableModel;   //Table model to be used with tabel
    JScrollPane jsp;        // We will add table to this scroll pane to make table scrollable
    JButton addRecordBtn, generateBtn, calculateBtn, searchBtn, viewRecordBtn;  //reference for different buttons
    JTextField searchBoxTF;     //reference to searchBox TextField
    JLabel error;       //label to display error messages

    public StaffForm() {
        setUpGUI(); //setup Basic GUI
        //this.setSize(1000,700);
        this.setVisible(true);  //make form visible
    }

    public void setUpGUI() {

        GridLayout gridLayout = new GridLayout(6, 1);
        gridLayout.setVgap(20); //creates grid layout with 6 rows and 1 column set vertical gap to 20pixels

        tableModel = new DefaultTableModel() { //This is actually an anonymous class extending DefaultTableModel
            //create object of defaultTableModel but overriding cell editable
            @Override                       //method tom make cells uneditable
            public boolean isCellEditable(int row, int column) {
                return false;       //this will make the cells uneditable
            }
        };
        table = new JTable(tableModel); //create a new table object that will follow table model
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);    //Only one row can be selected at a time
        tableModel.setColumnIdentifiers(new String[]{"ID", "Name", "Address", "ConnectionType"});// set the title of
        //columns.. modifying table model will indirectly effect table

        if (!DataBase.isConnected())
            DataBase.setUpConnection(); //make sure that we are connected

        DataBase.fillConsumerTable(tableModel); //fill table with consumer records from database

        error = new JLabel("This is error"); //create a label named This is error and store it in error
        error.setVisible(false);    //hide the label
        //error.setVisible(false);

        leftPan = new JPanel(gridLayout);   // panel to hold buttons on left
                                            //elements in panel will follow grid layout
        addRecordBtn = new JButton("Add new Record");   //creates new button add new record
        generateBtn = new JButton("Generate Bill");     //new button generate bill
        calculateBtn = new JButton("Bill Calculator");  //new button bill calculator
        searchBoxTF = new JTextField();             //create search box
        searchBtn = new JButton("Search");      //create search button
        viewRecordBtn = new JButton("View Bill Records"); //create view record button
        JButton resetButton = new JButton("Reset");    //create resetButton
        viewRecordBtn.setBounds(800, 200, 150, 40); //set view record button on right of screen
        resetButton.setBounds(650, 10, 100, 40);    //set rset Button to top mid-right

        addRecordBtn.addActionListener(this);   // add event listener for buttons
                                                    //action performed of this class will be executed on click
        generateBtn.addActionListener(this);
        calculateBtn.addActionListener(this);
        searchBtn.addActionListener(this);
        viewRecordBtn.addActionListener(this);
        resetButton.addActionListener(this);

        leftPan.setBounds(20, 150, 150, 400);  //set left button panel to left of screen
        leftPan.add(addRecordBtn);  // add Add new Record button to panel
        leftPan.add(generateBtn);   // add Generate Bill button to panel
        leftPan.add(calculateBtn);  // add Calculate button to panel

        searchBoxTF.setToolTipText("Enter Consumer ID to search");  //set Tooltip for search box
        //searchBoxTF.setText("");    //clear search box
        searchBoxTF.setBounds(300, 10, 200, 40); //place search box on top
        searchBtn.setBounds(530, 10, 100, 40);  //place search button next to search box

        jsp = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);   //create scroll pane by adding table
                                                        //other two options will specify the visibilty
                                                        //of scrollbars
        jsp.setBounds(250, 100, 500, 500); //place table scroll pan in center

        this.setLayout(null);   //set frame layout to null
        this.add(jsp);          //add scroll pane to frame
        this.setTitle("Billing System");    //set Title to billing system
        this.add(viewRecordBtn);    //add view record button to frame
        this.add(searchBtn);        //add search button
        this.add(resetButton);      //add reset button
        this.add(searchBoxTF);      //add search box
        this.add(leftPan);          //add button panel on the left
        this.setSize(1024, 768);    //set window size 1024 x 768
        this.add(error);        //add error label
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //application will be terminated if cross button
        //is clicked
    }

    /**
     * This method will setUp color, font etc. for error message
     * @param str message to be displayed
     */
    public void displayError(String str) {
        error.setText(str);     //set Error text to str
        error.setBounds(400, 600, 250, 40); //place it in the bottom of table
        error.setForeground(Color.RED);     //set font color to red
        error.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));    //set font to SansSerif, Bold, 13(size)
        error.setVisible(true); //make error visible

        /**
         * Following statements will create a new Timer Object with delay of
         * 3000 milliseconds or 3 seconds
         * after 3 seconds ActionListener will be invoked
         * In action listener we will hide the error so whenever an error occurs it will appear for 2seconds only
         */
        new Timer(3000, new ActionListener() { //create a new timer object with paramter 3000m milliseconds
            @Override   //second parameter is actually anonymous class implementing ActionListener interface
            public void actionPerformed(ActionEvent e) {
                error.setVisible(false);
            }
        }).start(); //start the timer
    }

    /**
     * delete all the data from the table
     */
    public void clearTable() {
        int rowCount = tableModel.getRowCount();    //get total number of rows
        //System.out.println("Row Count: "+rowCount);
        //then delete rows from down to top order
        for (int i = rowCount - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
    }

    /**
     * Clears the table and reload records from DataBase
     */
    public void refreshTable() {
        clearTable();
        DataBase.fillConsumerTable(tableModel);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        //getActionCommand will return the text of the button that generated event
        //System.out.println("Event occurred "+ae.getSource());
        switch (ae.getActionCommand()) {

            case "Add new Record":
                //if Add new record was pressed then create new dialog to add consumer
                //dialog owner is this frame, title is New Consumer Dialog
                //dialog will be modal(means you cannot interact with frame util you
                //close the dialog
                new NewConsumerDialog(this, "New ConsumerDialog", true);
                refreshTable();
                break;
            case "Generate Bill":
                //create bill generator dialog
                new BillGeneratorDialog(this, "Bill Generator", true);
                break;
            case "Bill Calculator":
                //create bill calculator dialog and make it visible
                //System.out.println("Bill Calculator Clicked");
                new BillCalculator(this, "Bill Calculator", true).setVisible(true);
                break;
            case "View Bill Records": {
                //if viewRecord button is clicked then
                //System.out.println("View Record Clicked");
                int row = table.getSelectedRow(); //get selected row from table this will return
                //System.out.println(row);  //-1 if no row is selected

                if (row != -1) { //means if any row is selected
                    int id = (int) tableModel.getValueAt(row, 0); //getValue from first column of selected row
                    //System.out.println(id);
                    //then create a consumer dialog modal with owner this
                    new ViewConsumerDialog(this, "Consumer Bill Records", true, id);
                } else {
                    //if no row is selected
                    displayError("Please Select a row");
                    //System.out.println("ERROR:");
                }
            }
            break;
            case "Search":
                //if search button is clicked
                try {
                    clearTable();   //clear data from table
                    //fill the table with records that have consumer ID mentioned in search Box
                    DataBase.fillConsumerTable(tableModel, Integer.parseInt(searchBoxTF.getText()));
                } catch (NumberFormatException | SQLException exception) {
                    /**
                     * NumberFormatException might be thrown if we try to convert value in search box from
                     * text to integer
                     * SQLException might be thrown if no record was found
                     */
                    displayError("Invalid Value Entered");
                    searchBoxTF.setText("");
                    //clear searchBox
                }
                break;
            case "Reset":
                //if reset button is clicked
                refreshTable(); //refresh data in table
                searchBoxTF.setText("");    //clear searchBox
                break;
        }
    }
}

package app;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class Inherits from Bill Calculator and Modifies GUI to reuse the functionality
 */
public class BillGeneratorDialog extends BillCalculator implements ActionListener
{
    int consumerCount; //to store the number of consumer records available in database
    Consumer data;      //hold data for active consumer(consumer being displayed(

    JLabel name = new JLabel(),     //label to hold Consumer name
            address = new JLabel(), //address
            id = new JLabel(),      //Consumer ID
            consumerType = new JLabel();    //And Consumer TYpe
    JTextField currentDateTf = new JTextField(),dueDateTf = new JTextField(); //labels to hold currentDate
    //and Due date
    private int currentIndex = 1;   // ID of the active consumer

    public BillGeneratorDialog(Frame owner, String title, boolean modal) {
        super(owner,title,modal);
        modifyUI();     //Modify GUI created by super Class
        if(!DataBase.isConnected())
            DataBase.setUpConnection(); //make sure we are connected to database
        consumerCount = DataBase.getConsumerCount();    //load the total number of consumers
        load();     //load data of Consumer with ID currentIndex to GUI
        setSize(1024,768);  //set dialog size to 1024x768
        setVisible(true);   //make dialog visible on screen
    }
    public void modifyUI()
    {
        calculateBtn.addActionListener(this); //change the calculateBtn's (from super class)
        //action listener to this class;
        this.remove(consumerTypeBox);   //Remove consumerTypeBox we will use Label so user cann't modify it
        JButton generateBtn = new JButton("Generate Bill"); //This button will save records to database when pressed
        JButton cancelBtn = new JButton("Cancel"); //This will simply destroy dialog
        JButton nextButton = new JButton("Next");   //Load Next Record
        JButton previousButton = new JButton("Previous");   //Load Previous Dialog

        textFields[1].setEditable(false);   //make the previous reading field uneditable because
        //we will load it from database
        textFields[1].setBorder(BorderFactory.createLineBorder(Color.BLACK));//create border for prev reading box
        textFields[1].setBackground(Color.WHITE);   //set Background color to white

        consumerType.setBounds(250,200,150,30); // add consumer Type label in place of comboBox
        consumerType.setBorder(BorderFactory.createLineBorder(Color.BLACK));    //set Border to black

        JLabel currentDate = new JLabel("Issue Date "), dueDate = new JLabel("Due Date");
        //Labels to store current and Issue data

        GridLayout layout = new GridLayout(1,5);    //create layout for buttons at bottom 1 row, 5 columns
        GridLayout sideLayout=new GridLayout(3,2);  //layout for details panel
        sideLayout.setVgap(50);     //set button gap to 50 pixels
        layout.setHgap(50);         //set gap between Name ,Address and ID labels to 50pixels
        JPanel btnPan = new JPanel(layout);     //Panel to hold buttons at bottom
                                                //This will follow layout to arrange items
        JPanel detailsPan = new JPanel(sideLayout);     //Panel to Hold name, address and Id labels
                                                        //this will follow sideLayout
        detailsPan.setBounds(500,100,500,300);  //set details panel to right

        previousButton.addActionListener(this);     //add button listener to buttons
        nextButton.addActionListener(this);         //our class will handle all the events
        generateBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        btnPan.setBounds(20,650,750,30);    //set Button panel at the bottom
        btnPan.add(previousButton);    //at row 1 column 1 //adding buttons to button panel
        btnPan.add(generateBtn);        //at row 1 col 2
        btnPan.add(cancelBtn);          //at row 1 col 3
        btnPan.add(nextButton);         //at row 1 col4

        //adding name,address and iD to details panel;
        detailsPan.add(new JLabel("Name "));    //at row 1 col 1
        detailsPan.add(name);       //row 1 col 2
        detailsPan.add(new JLabel("Address"));  //row 2 col 1
        detailsPan.add(address);            // row 2 col 2
        detailsPan.add(new JLabel("Consumer ID ")); // row 3 col 1
        detailsPan.add(id);         //row 3 col 2
        //consumerTypeBox.hidePopup();

        //creating Lined black borders around the labels
        name.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        id.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        address.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        detailsPan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),"Consumer Details",
                TitledBorder.CENTER, TitledBorder.CENTER)); //creates Titled border around details panel
            //with title Consumer details
        //BorderFactory.cre
        //detailsPan.add(new JTextField("Test"));

        currentDate.setBounds(60,50*11,150,30); //place the current date label at bottom
        dueDate.setBounds(60,50*12,150,30);     //place due date label at bottom
        currentDateTf.setBounds(250,50*11,150,30);  //place textFields next to labels respectively
        currentDateTf.setEditable(false);   //make the current date TextField uneditable
        dueDateTf.setBounds(250,50*12,150,30);

        //Formatting dates to simple Day/Month/Year Format
        /**
         * SimpleDataFormat class formats the data from date object to the format given in parameter
         */
        currentDateTf.setText((new SimpleDateFormat("dd/MM/yyyy")).format(new Date()));
        Date date = new Date();
        date.setDate(date.getDay()+15); //set the due date from 15 days after now
        dueDateTf.setText((new SimpleDateFormat("dd/MM/yyyy")).format(date));
        dueDateTf.setEditable(false);   //make dueDate uneditable

        //Adding components to Dialog
        add(dueDate);
        add(dueDateTf);
        add(currentDateTf);
        add(currentDate);
        this.add(detailsPan);
        this.add(btnPan);
        this.add(consumerType);
    }

    /**
     * This function will increment the currentIndex by 1
     * if it is less than total consumer count
     * else set the currentIndex to first Record
     * then call load()
     */
    public void loadNext() {
        if(currentIndex < consumerCount)
            currentIndex++;
        else
            currentIndex = 1;
        load();

    }

    /**
     * This function will call ClearFields
     * getThe Consumer record with id currentIndex
     * if consumerExist then set details to specific labels
     */
    public void load() {

        clearFields();  //Clear name,address and ID fields
        data = DataBase.getConsumerAt(currentIndex);    //get Record with ID currentIndex
        if(data != null) {  //if record was found
            name.setText(String.format("  %s", data.getName()));    //set name label to consumer name
            address.setText(String.format("  %s", data.getAddress()));  //set address label to consumer address
            id.setText(String.format("  %d", data.getID()));    //set id to consumer ID
            consumerType.setText(data.getConnectionTypeString());   //set consumerType label to connectionType
            consumerTypeBox.setSelectedIndex(data.getConnectionType()); //set setComboBox selected index to
                //connection type because it will be used for calculations
            data.loadBill();    //Load Latest Bill for consumer
            if(data.getBill()!=null)    //If bill was loaded
                textFields[1].setText(String.valueOf(data.getBill().getReading())); //set Previous reading to readings
            // of that bill
            else
                textFields[1].setText("");//else keep them empty
        }
    }

    /**
     * check if currentIndex( current loaded record id) is greater then 1 decrease currentIndex by 1
     * else set currentIndex to last record in database
     * load() that record
     */
    public void loadPrevious() {
        if(currentIndex > 1)
            currentIndex--;
        else
            currentIndex = consumerCount;

        load();
    }

    /**
     * This function will be called if any of the above buttons are pressed
     * @param e event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Next":                //if next button was pressed
                loadNext();             //loadNext() record
                break;
            case "Previous":            //if previous button was pressed
                loadPrevious();         //loadPrevious record
                break;
            case "Cancel":              //if cancel was pressed
                dispose();              //destroy dialog
                break;
            case "Calculate":           //if calculate was pressed
                calculateBill();        //calculateBill()
                break;
            case "Generate Bill": {             //if generate button was pressed
                Bill bill = calculateBill();    // calculateBill and have a reference to the result
                if (bill == null)           //if bill is empty (in case of error)
                    return;             //return
                data.setBill(bill);     //set this as a Bill for current Consumer
                DataBase.saveBill(data);    //save the bill of Consumer
                //display dialog success
                JOptionPane.showMessageDialog(this, "Record Saved", "Success", 1);
                clearFields();  //clear all fields on GUI
                load();    //reload() data of current consumer
                break;
            }

        }
    }
    public void clearFields() {     //clear text from each field by setting text to ""
        for(int i =0 ;i<textFields.length;i++)
            textFields[i].setText("");
    }
    public Bill calculateBill() {
        Bill bill;
        if(data.getBill() == null)  //if consumer Bill is not present (which means no previous reading)
            textFields[1].setText("0"); //then set previous reading to 0
        bill = super.calculateBill();   //call to calculate method of super class which will return resultant bill
        if(bill != null) {      //if resultant bill is present (means no error occurred)
            bill.setDueDate(dueDateTf.getText());      //set due Date of bill to due date of textfield
            bill.setIssueDate(currentDateTf.getText());    //same with issue date
            bill.setBillId(DataBase.getBillCount() + 1);    //setBillId to number of bills in database + 1
            //System.out.println("ID: ");
            //System.out.println(bill);
        }
        return bill;
    }
}
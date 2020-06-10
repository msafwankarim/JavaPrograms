package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewConsumerDialog extends JDialog {
    private JLabel[] labels = {new JLabel("Name"), new JLabel("Address"), new JLabel("Consumer ID")};
    private JLabel errorLbl, consumerLbl;   //Error Label, consumerType
    private JTextField[] textFields = new JTextField[3];    //textFields for name address and id
    private String[] consumerTypes = {"Domestic", "Commercial", "General Industry"};    //consumer Types
    private JComboBox<String> consumerType; //Combo box representing consumer types
    private JButton save, cancel;   //buttons to save record and cancel

    public NewConsumerDialog(Frame owner,String title, boolean modal ) {
        super(owner,title,modal);   //calls superclass(JDialog) constructor
        this.setSize(500,500);  //setWindow size 500x500
        DataBase.setUpConnection();         //Connect with DataBase
        consumerType = new JComboBox<String>(consumerTypes);    //creates a consumerType combo box object
        errorLbl = new JLabel();        //creates error label
        save = new JButton("Save"); //creates save button
        cancel = new JButton("Cancel"); //creates cancel button
        consumerLbl = new JLabel("Consumer Type "); //consumerLabel type box

        setUpGUI(); // set up gui elements
        //addEventListeners();
    }

    /**
     * This function will set errorLbl text to error string (str)
     * changes text color to red
     * set font to Dialog,Bold, size=14
     * place the error label on the top
     * and make it visible
     * @param str error string
     */
    public void displayError(String str) {

        errorLbl.setText("ERROR: "+str);
        errorLbl.setForeground(Color.RED);
        errorLbl.setFont(new Font(Font.DIALOG,Font.BOLD,14));
        errorLbl.setBounds(40,20,250,50);
        //errorLbl.setBackground(Color.RED);
        //System.out.println("In displayError");
        errorLbl.setVisible(true);
    }

    public void setUpGUI() {

        for(int i = 0; i<labels.length; i++) {
            //This function will set labels at x = 50 and y will be 80px down than previous one
            //
            labels[i].setBounds(50,80*(i+1),130,40);
            labels[i].setFont(new Font(Font.SANS_SERIF,Font.ITALIC, 16));
            textFields[i] = new JTextField(); //create Text Fields at each index corresponding to label
            textFields[i].setBounds(200,80*(i+1),200,40); //place textfield in front of label
            textFields[i].setFont(new Font(Font.SANS_SERIF,Font.BOLD, 16)); //set Font of textFields t
            //SansSerif,Bold,size=16
            add(labels[i]); //add label[i] to frame
            add(textFields[i]); //add textfield[i] to frame
        }
        textFields[2].setEditable(false);   //make the consumer ID text Field uneditable
        textFields[2].setText(String.valueOf(DataBase.getConsumerCount()+1));   //get The total count of consumers from
        //database and then place it in consumerID field by incrementing it by 1
        consumerLbl.setFont(new Font(Font.SANS_SERIF,Font.ITALIC, 16)); //set Consumer label font
        consumerLbl.setBounds(50,320,130,40);   //place consumerLbl to the bottom
        consumerType.setBounds(200,320,130,40); //place consumerType comboBox next to consumerLbl
        save.setBounds(250,410,80,30);      //set Save button at the bottom
        cancel.setBounds(350,410,80,30);    //set cancel button next to save button

        save.addActionListener(new ActionListener() {   //add anonymous class listener to save button
            @Override
            public void actionPerformed(ActionEvent e) { //this method will be called when save button is clicked
                //System.out.println("Save Clicked");
                if(textFields[0].getText().isEmpty() || textFields[1].getText().isEmpty()) {
                    //if any text field is empty display error
                    displayError("All Fields are required");
                    return;
                }
                Consumer con;
                try {
                    //creates consumer object and
                    con = new Consumer(Integer.parseInt(textFields[2].getText()), //set ID to text from ID field
                            textFields[0].getText(),    //set Name
                            textFields[1].getText(),    //set Address
                            consumerType.getSelectedIndex() //set the consumer Type
                            );
                    DataBase.addConsumerRecord(con);    //save consumer record to database
                }
                catch (NumberFormatException exception) {   // if Value in id field is incorrect
                    displayError("Invalid or Incomplete Values");
                }

                dispose();  //close and destroy this dialog
            }
        });
        cancel.addActionListener(new ActionListener() { //this button will simply destroy the dialog
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); ////close and destroy this dialog
            }
        });

        add(save);  //add save button to dialog
        add(cancel);//add cancel button to dialog
        add(errorLbl);  //add(error label to dialog
        add(consumerLbl);   //add consumer label to dialog
        add(consumerType);  //add consumerType box to dialog
        setLayout(null);    //set layout manager to null
        setVisible(true);   //make dialog visible
    }

}

package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BillCalculator extends JDialog {

    protected JLabel error = new JLabel();  //Label to hold error
    protected JLabel[] labels = {       //array of labels with specific text
            new JLabel("Latest Reading"),   //self explanatory
            new JLabel("Previous Reading"),
            new JLabel("GCV"),
            new JLabel("Consumer Type"),
            new JLabel("Hecto Meters Used"),
            new JLabel("MMBTU used"),
            new JLabel("Minimum Bill"),
            new JLabel("Original Bill"),
            new JLabel("Tax"),
            new JLabel("Total Bill")
    };
    protected JComboBox<String> consumerTypeBox;    //ComboBox to hold the consumerTypes
    protected String[] consumers = {"Domestic", "Commercial", "General Industry"}; //consumerTypes
    protected JTextField[] textFields;  //array of textFields
    protected JButton calculateBtn;     //CalculateButton

    public BillCalculator(Frame owner, String title, boolean modal) {
        super(owner,title,modal);   //Call super class Constructor
        setSize(800,600);   //set Window size 800x600
        setUpGUI();     //setUp gui in form
        setLayout(null);    //set layout manager to null
        //this.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
    }

    public JLabel getError() {
        return error;
    }   //getter for error

    public void setError(JLabel error) {
        this.error = error;
    }   //getter for setError

    public JLabel[] getLabels() {
        return labels;
    }   //getter for lables

    public void setLabels(JLabel[] labels) {
        this.labels = labels;
    }   //getter for labels

    public JComboBox<String> getConsumerTypeBox() {
        return consumerTypeBox;
    }   //getter for consumerTypeBox

    public void setConsumerTypeBox(JComboBox<String> consumerTypeBox) {
        this.consumerTypeBox = consumerTypeBox;
    }
    //setter for consumerTypeBox
    public String[] getConsumers() {
        return consumers;
    }
    //getter for consumerType list
    public void setConsumers(String[] consumers) {
        this.consumers = consumers;
    } //setter for consumerTypes
    public JTextField[] getTextFields() {
        return textFields;
    }   //getter for textfields

    public void setTextFields(JTextField[] textFields) {
        this.textFields = textFields;
    } //getter for textFields

    public JButton getCalculateBtn() {
        return calculateBtn;
    } //getter for calculateBtn
    public void setCalculateBtn(JButton calculateBtn) {
        this.calculateBtn = calculateBtn;
    }
    //setter for calculate Button
    public void setUpGUI() {
        consumerTypeBox = new JComboBox<>(consumers); //creates a combo box with values specified in combo box
        textFields = new JTextField[labels.length -1];  //create textFields array
                                        //array Size is less than 1 of label length because one typefield is replaced
                                        //by combo box
        /**
         * Following loop will set labels at x =60 and y will be 50pixels down than previous one
         * and add Labels to frame
         */
        for (int i = 0; i < labels.length; i++) {
            labels[i].setBounds(60, 50 * (i+1), 150, 30);
            this.add(labels[i]);
            if(i<labels.length -1){  //if i is less than labels length -1
                //this condition will skip one index because textFields array is smaller
                textFields[i] = new JTextField();   //new textField object and store it in array[i]
                if(i >= 3) {
                    //if we are adding textField after combo box then its 50 must be
                    //multiplied by 2
                    textFields[i].setBounds(250, 50 * (i + 2), 150, 30);
                    textFields[i].setEditable(false); //make textfields uneditable
                    textFields[i].setBackground(Color.WHITE);   //set there foreground to white
                    textFields[i].setFont(new Font(Font.DIALOG_INPUT,Font.BOLD,12));    //set Font
                    textFields[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));   //create lined border
                }
                else
                    textFields[i].setBounds(250,50 * (i+1),150,30);
                //if we are adding textFields before combox box then multiply 50 with 1
                this.add(textFields[i]); //add TextFields to frame
            }
        }
        error.setBounds(250,5,300,30);  //set Error postion to the top of dialog
        error.setForeground(Color.RED);     //set color to red
        error.setVisible(false);            //hide the error label
        consumerTypeBox.setBounds(250,200,150,30);  //set combo box after 3rd text field
        add(consumerTypeBox);   //add it to frame
        add(error);     //add error to frame
        calculateBtn = new JButton("Calculate");    //add calculate button
        calculateBtn.setBounds(500,500,100,30); //set it to the right of every thing
        calculateBtn.addActionListener(new ActionListener() {   //anonymous class implementing action listener
            //to handle button clicks
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateBill();
            }});
        add(calculateBtn); //calculate Button
    }


    /**
     * This function will read textFields and Calculate Total bill and then
     * @return bill if every thing is fine
     * @return null in case of error
     */
    public Bill calculateBill() {
        try {
            if (textFields[0].getText().isEmpty() || textFields[1].getText().isEmpty() || textFields[2].getText().isEmpty()) {
                //if previous reading field or current reading field or gcv field is empty then display error and
                //return
                showError("All fields are required");
                return null;
            }
            Bill bill = new Bill(Long.parseLong(textFields[0].getText()), //create new bill object and setLatestReading
                    Long.parseLong(textFields[1].getText()),        //set previous reading
                    Double.parseDouble(textFields[2].getText())     //set gcv
            );
            if (error.isVisible())  //if error is visible hide error
                error.setVisible(false);
            bill.setMinBill(consumerTypeBox.getSelectedIndex()); //set minimum bill according to consumerTypeBox


            textFields[3].setText(String.valueOf(bill.getHectoMeters())); //set Hectometers used
            textFields[4].setText(String.format(" %.4f", bill.getMmbtu())); //set MMBTU used
            textFields[5].setText(String.valueOf(bill.getMinBill()));       ///set minimumbill
            textFields[6].setText(String.format(" %.4f", bill.calculateBill()));//calculate orignal bill
            textFields[7].setText((String.valueOf(bill.getTax()))); //set Tax value
            textFields[8].setText((String.format(" %.4f", bill.getTax() + bill.calculateBill())));  //total bill
            return bill; // return bill object if every thin goes well
        } catch (NumberFormatException exception) {
            //Show error in case of invalid values
            showError("Invalid value entered");
        }
        //This statement will be unreachable if no error happens
        return null;
    }
    public void showError(String str)
    {
        error.setText("ERROR: "+str);
        error.setFont(new Font(Font.SANS_SERIF,Font.BOLD,14)); //set font for error
        error.setVisible(true);     //make error visible

    }

}

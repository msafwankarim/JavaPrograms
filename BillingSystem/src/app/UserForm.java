package app;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class represents app.UserForm window
 * It will handle click events by it self
 */
public class UserForm extends JFrame implements ActionListener {
    final private int consumerId; //consumer ID this will represent the logged in user
    public UserForm(int id) {
        consumerId = id;
        setLayout(null);    //set Layout manager to null
        setUpGUI();         //setUpGui elements
        /**
         * Loads data of Consumer by matching consumerID
         * display error in case of false result
         */
        if(!loadData()){
            JOptionPane.showMessageDialog(this,"An error occurred while loading data","Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        setSize(600,600);   //set window size to 600x600
        this.setVisible(true);      //make form visible
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //Application will be terminated if
        //user clicks X button on top-right
    }

    /**
     * This function will setUp some gui elements on form
     */
    public void setUpGUI() {

        JLabel image = new JLabel(new ImageIcon("incon.png")); //load logo
        image.setBounds(5,5,500,100);   //set Size of logo
        add(image);     //add logo to window

        JButton billHistory = new JButton("Bill History"),  //Button for BillHistory
                billCalculator = new JButton("Bill Calculator");    //Button for bull calculator

        billHistory.setBounds(120,450,150,30);  //billHistory and bill calculator buttons to the bottom
        billCalculator.setBounds(300,450,150,30);

        billHistory.addActionListener(this);    //actionPerformed function of this class will be called when user
        //clicks the button
        billCalculator.addActionListener(this);

        add(billCalculator);    //add BillCalculator button to frame
        add(billHistory);       //add BillHistory to frame
    }

    /**
     * This function will load data from dataBase by matching consumer ID
     * Creates different lables to display loaded details
     * @return true on success
     * @return false on error
     */
    public boolean loadData() {
        Consumer con;   //Consumer object to hold the details of consumer
        JLabel name = new JLabel(), id = new JLabel(), address = new JLabel(); //labels to display consumer's name,id
                                                    //and address
        GridLayout sideLayout=new GridLayout(3,2); //Creates a new grid layout manager with 3 rows and
                                            // 2 columns
        sideLayout.setVgap(50);     //vertical gap between each cell will be 50pixels
        JPanel detailsPan = new JPanel(sideLayout);     //creates panel that will follow the layout sideLayout
                                            //panel is used to group the items
        detailsPan.setBounds(50,100,500,300);   //set panel position
        detailsPan.add(new JLabel("Name "));    //create and add the label at grid position row = 1,column =1

        detailsPan.add(name);           ////add the label name at grid position row = 1,column =2

        detailsPan.add(new JLabel("Address"));//create and add the label at grid position row = 2,column =1

        detailsPan.add(address);//add the label address at grid position row = 2,column =2

        detailsPan.add(new JLabel("Consumer ID "));//create and add the label at grid position row = 3,column =1

        detailsPan.add(id); //add the label id at grid position row = 3,column =1
        //consumerTypeBox.hidePopup();

        name.setBorder(BorderFactory.createLineBorder(Color.BLACK));    //Creates new lined border with black color
                                //BorderFactory classes is used to produce different kind of borders
                                // it will return object compatible with Border interface
        id.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        address.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        detailsPan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),"Consumer Details",
                TitledBorder.CENTER, TitledBorder.CENTER));
        //Creates a titled Border with title Consumers details
        con = DataBase.getConsumerAt(consumerId);   //get Consumer with logged in consumerId
        if(con == null)     //incase of error
            return false;

        this.setTitle("Consumer Details: "+con.getName()); //set Window title to Consumer Detals: {consumerName}
        name.setText(con.getName());        //put consumer name in name Label
        address.setText(con.getAddress());  // put consumer address in address lable
        id.setText(String.valueOf(con.getID()));//put consumer id in id label... we have to convert it to string
        add(detailsPan);    //add details pan to frame
        return true;    //return true if every thing goes well

    }
    @Override
    public void actionPerformed(ActionEvent ae) {
        //getActionCommand will return the text on button pressed
        switch(ae.getActionCommand()) {
            case "Bill History":
                //if BillHistory was pressed then create bill history dialog with owner frame as this frame and
                // title to "Bill History", and dialog will be modal (which means you cannot interact with this frame
                // until you close the dialog box
                //forth parameter will indicate whose bill history will be displayed
                new ViewConsumerDialog(this,"Bill History",true,consumerId);
                break;
            case "Bill Calculator":
                //if BillCalculator was pressed then create bill history dialog with owner frame as this frame and
                // title to "Bill Calculator", and dialog will be modal (which means you cannot interact with this frame
                // until you close the dialog box
                // and make it visible
                new BillCalculator(this,"Bill Calculator",true).setVisible(true);
                break;
        }
    }

}
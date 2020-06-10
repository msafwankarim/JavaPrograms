package app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ViewConsumerDialog extends JDialog {

    public ViewConsumerDialog(Frame frame, String title, boolean modal, int id) {
        super(frame,title,modal);   //call to superclass constructor
        DefaultTableModel model = new DefaultTableModel();  //layout model for our table
        JTable table = new JTable(model);   //create a new table which will follow DefaultTableModel
        model.setColumnIdentifiers(new String[]{"BillID","Reading","Issue Data","Due Date","GCV","Bill Type",
                "MMBTU","Tax","Minimum Bill","Original Bill","Total"}); //Set Titles to columns
        this.setLayout(new BorderLayout()); //set borderLayout manager
        /**
         * Border layout manager arranges the items in North, South, East, West and Center format
         * by default every item will be set to center
         */
        JScrollPane jScrollPane = new JScrollPane(table);   //creates scrollable panel for our table
        add(jScrollPane);       //add scroll panel to Dialog
        DataBase.fillBillTable(model,id);   //fillTable with bills with consumerID == id

        this.setSize(1000,400); // set Size to 1000x400
        this.setVisible(true);          //make dialog visible
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

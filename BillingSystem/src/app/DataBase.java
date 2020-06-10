package app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class DataBase {
    private static Connection conn = null; //Static field to hold connection to JDBC
    private static Statement stat = null;  //Statement on which we will execute queries

    /**
     * Statement interface is used to execute SQL commands
     */

    public static void setUpConnection() {
        /**
         * This function will connect to SQLITE JDBC driver
         * then connects to dataBase
         * then creates a statement
         */
        try {
            Class.forName("org.sqlite.JDBC");   //This will load JDBC SQLite driver dynamically at run time
            //Above statement Might throw ClassNotFoundException
            conn = DriverManager.getConnection("jdbc:sqlite:File.DB");  //get a connection to database
            //Above statement Might throw SQLiteException
            stat = conn.createStatement();  //get reference to Statement object from which we will be able to execute sql
            //Above statement Might throw SQLiteException
        } catch (Exception e) {
            //System.out.println(e);
            JOptionPane.showMessageDialog(null,"DataBase ERROR","Error",
                    JOptionPane.ERROR_MESSAGE); //display error dialog
        }
    }

    /**
     *
     * @return number of consumer records
     */
    public static int getConsumerCount() {
        try {   //Following SQL command will return number of records from consumers table
            return ((stat.executeQuery("SELECT COUNT(*) FROM Consumers")).getInt(1)); //get result at
            //column 1
            /**
             * SQLite commands return results in columns
             */
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }

    /**
     * This function will add Consumer record con to database
     * @param con recor to be added
     */
    public static void addConsumerRecord(Consumer con) {
        String query;
        query = String.format("INSERT INTO Consumers(ConsumerID,Name,Address,ConnectionType) VALUES (%d,\"%s\",\"%s\",%d)",
                con.getID(), con.getName(), con.getAddress(), con.getConnectionType());
        try {
            stat.executeQuery(query);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * This function will find Consumer with ID id
     * @param id id to search
     * @return if found true else false
     */
    public static boolean verifyUser(final int id) {
        ResultSet rs;
        try {
            rs = stat.executeQuery("SELECT ConsumerId FROM Consumers WHERE ConsumerId==" + id);
            System.out.println(rs.getInt(1));
            return true;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    /**
     * This function will read records from database and loads them to table one by one
     * @param tableModel in which records will be added
     */
    public static void fillConsumerTable(DefaultTableModel tableModel) {
        ResultSet rs;
        try {
            rs = stat.executeQuery("SELECT * FROM Consumers");
            while (rs.next()) {
                Consumer consumer = new Consumer(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getInt(4));

                tableModel.addRow(new Object[]{consumer.getID(), consumer.getName(),
                        consumer.getAddress(), consumer.getConnectionTypeString()});
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * This function find load the record of consumer with id idToSearch to table
     * @param tableModel table in which record will be added
     * @param idToSearch ID to search
     * @throws SQLException In case of error
     */
    public static void fillConsumerTable(DefaultTableModel tableModel, int idToSearch) throws SQLException {
        ResultSet rs;

       // System.out.println(idToSearch);
        rs = stat.executeQuery("SELECT * FROM Consumers WHERE ConsumerID==" + idToSearch);
        System.out.println(rs);
        while (rs.next()) {
            Consumer consumer = new Consumer(rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getInt(4));
           // System.out.println(consumer);
            tableModel.addRow(new Object[]{consumer.getID(), consumer.getName(),
                    consumer.getAddress(), consumer.getConnectionTypeString()});

        }
    }

    /**
     *
     * @return true if connected; false otherwise
     */
    public static boolean isConnected() {
        return !(conn==null);
    }

    /**
     *
     * @return true if stat not equals null; false otherwise
     */
    public static boolean isReady() {
        return !(stat==null);
    }

    /**
     * This function will return the Consumer with Consumer ID index
     * @param index  consumer id to search
     * @return Consumer object or null
     */
    public static Consumer getConsumerAt(final int index) {
        ResultSet rs = null;
        try {
            rs = stat.executeQuery("SELECT * FROM Consumers WHERE ConsumerId=="+index);
            return (new Consumer(rs.getInt(1),rs.getString(2),rs.getString(3),
                    rs.getInt(4)));
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * This function will find the bill with consumerID given in parameter in descending order
     * @param consumerID id to search bills for
     * @return Bill or null
     */
    public static Bill getBillFor(final int consumerID) {
        ResultSet rs = null;
        try {
            rs = stat.executeQuery("SELECT * FROM Bills WHERE ConsumerId=="+consumerID+" ORDER BY BillID DESC");
            Bill bill = new Bill();
            bill.setBillId(rs.getInt(1));
            bill.setReading(rs.getInt(3));
            bill.setIssueDate(rs.getString(4));
            bill.setDueDate(rs.getString(5));
            bill.setGcv(rs.getDouble(6));
            bill.setMinBill(rs.getInt(7));
            return bill;
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     *
     * @return returns the total number of bill records
     */
    public static int getBillCount() {
        try {
            return ((stat.executeQuery("SELECT COUNT(*) FROM Bills")).getInt(1));
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }

    /**
     * Fills the table {model} with the bills with id {consumerID}
     * @param model table in which data is added
     * @param consumerID id to find bills for
     */
    public static void fillBillTable(DefaultTableModel model, int consumerID) {
        long previousReading = 0;
        int counter = 0;
        try {
            ResultSet rs = stat.executeQuery("SELECT * FROM Bills WHERE ConsumerId==" + consumerID +
                    " ORDER BY BillID ASC");
            Bill bill;
            while(rs.next()) {

                if(counter == 0) {
                    bill = new Bill(rs.getLong(3), 0, rs.getDouble(6));
                }
                else {
                    bill = new Bill(rs.getLong(3),previousReading, rs.getDouble(6));

                }
                //System.out.println(previousReading);
                bill.setBillId(rs.getInt(1));
                bill.setIssueDate(rs.getString(4));
                bill.setDueDate(rs.getString(5));
                bill.setMinBill(rs.getInt(7));

                model.addRow(new Object[] {
                        bill.getBillId(),
                        bill.getReading(),
                        bill.getIssueDate(),
                        bill.getDueDate(),
                        bill.getGcv(),
                        bill.getBillTypeString(rs.getInt(7)),
                        bill.getMmbtu(),
                        bill.getTax(),
                        bill.getMinBill(),
                        bill.calculateBill(),
                        bill.calculateBill()+bill.getTax()
                });

//                System.out.println("Type: "+rs.getInt(7)+"\nBill Type: "
//                        +bill.getBillTypeString(rs.getInt(7)));
//                System.out.println(previousReading);
                previousReading = rs.getLong(3);
                counter++;
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Add the bill of Consumer {consumer} to the data base
     * @param consumer
     */
    public static void saveBill(Consumer consumer) {
        String query = "INSERT INTO Bills(BillID,ConsumerID,Reading,IssueDate,DueDate,gcv,BillType)" +
                "VALUES ("+consumer.getBill().getBillId()+"," +
                consumer.getID()+"," +
                consumer.getBill().getReading()+",\""+
                consumer.getBill().getIssueDate()+"\",\""+
                consumer.getBill().getDueDate()+"\","+
                consumer.getBill().getGcv()+","+
                consumer.getConnectionType()+")";
        try {
            stat.executeQuery(query);
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
    }
}

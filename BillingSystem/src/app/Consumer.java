package app;

public class Consumer {
    private int ID;     //to hold consumer ID
    private String name, address;   // to hold name and address
    private int connectionType;     // to hold type of connection given by a comboBox
    private Bill bill;      //to store latest bill from database

    public Consumer(int ID, String name, String address, int connectionType) {
        this.ID = ID;
        this.name = name;
        this.address = address;
        this.connectionType = connectionType;

    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public int getID() {
        return ID;
    }

    /**
     *
     * @return the connectionType value in string based on condition
     */
    public String getConnectionTypeString() {

        switch (connectionType) {
            case 0:
                return "Domestic";
            case 1:
                return "Commercial";
            case 2:
                return "General Industry";
            default:
                return "NIL";
        }
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(int connectionType) {
        this.connectionType = connectionType;
    }

    @Override
    public String toString() {
        String str = this.getID() +", "+this.name+", " + this.address+", "+this.getConnectionTypeString();
        return str;
    }
    public void loadBill() {
        //load latest Bill from database
        if(!DataBase.isConnected())
            DataBase.setUpConnection();     //make sure we are connected
        bill = DataBase.getBillFor(ID);
    }
}

package app;

public class Bill {
    private int billId;     //to store bill id
    private long reading;   //store meter reading
    private String issueDate, dueDate;  //store issue and due date
    //private boolean ;
    private double hectoMeters; //store hectometers
    /**
     * hectometers are used to calculate MMBTU, and MMBTU value is used to calculate bill price
     * According to formula by Sui Northern gas
     */
    private double tax, mmbtu;  //tax value and mmbtu value
    private double minBill, gcv;    //minimum bill and gcv, GCV is Gas Calorific Value and is given on bill

    public Bill(final long lastReading, final long prevReading, final double gcv) {
        //setPreviousReading(prevReading);
        setReading(lastReading);
        setGcv(gcv);
        setHectoMeters((this.reading - prevReading)/1.0E5); //set Hectometers according to formula
        setTax(300.0);
    }

    public Bill(){}

    public Bill(int billId, long reading, String issueDate, String dueDate, double gcv) {
        this.billId = billId;
        this.reading = reading;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        //this.hectoMeters = hectoMeters;
        //this.tax = tax;
        //this.mmbtu = mmbtu;
        //this.minBill = minBill;
        this.gcv = gcv;
    }

    public int getBillId() {
        return billId;
    }

    /**
     * this function will return String based on the value given in billType
     * @param billType
     * @return
     */
    public String getBillTypeString(int billType) {

        switch (billType) {
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

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public double getGcv() {
        return gcv;
    }

    public void setGcv(double gcv) {
        this.gcv = gcv;
    }

    public long getReading() {
        return reading;
    }

    public void setReading(long reading) {
        if(reading >= 0)
            this.reading = reading;
        else this.reading = 0;
    }

    public double getHectoMeters() {
        return hectoMeters;
    }

    public void setHectoMeters(double hectoMeters)
    {
        this.hectoMeters = hectoMeters;
    }

    public double getTax()
    {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getMinBill()
    {
        return minBill;
    }

    /**
     * This function will set the minimum bill amount based on value in parameter billType
     * @param billType
     */
    public void setMinBill(final int billType) {
        switch (billType) {
            case 0:
                this.minBill = 300.0;
                break;
            case 1:
                this.minBill = 500.0;
                break;
            case 2:
                this.minBill = 1000.0;
                break;
            default:
                this.minBill = 250.0;
        }
    }
    public double getMmbtu()
    {
        this.mmbtu = ((hectoMeters * gcv)/281.7385);    //calculating MMBTU using formula
        return mmbtu;
    }
    public double calculateBill() {
        double totalBill;
        float price = getPriceFactor();
        totalBill = minBill + (mmbtu * price); //formula by sui northern gas ltd.
        return totalBill;
    }

    /**
     * This function will return the pricefactor based on HM3 used
     * This factor measurement is given on every bill
     * @return
     */
    public float getPriceFactor() {
        //float priceFactor = 0.0F;
        if(hectoMeters <= 0.50)
            return  121.0F;
        else if (hectoMeters <= 1.00)
            return  300.0F;
        else  if(hectoMeters <= 2.00)
            return  553.0F;
        else if(hectoMeters<= 3.00)
            return  738.0F;
        else
            return  1107.0F;
    }

    @Override
    public String toString() {      //converts the data to string
        return "{" + billId +
                ", " + reading +
                ", '" + issueDate + '\'' +
                ", '" + dueDate + '\'' +
                ", " + hectoMeters +
                ", " + tax +
                ", " + mmbtu +
                ", " + minBill +
                ", " + gcv +
                '}';
    }
}

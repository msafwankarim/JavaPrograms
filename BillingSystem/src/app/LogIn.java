package app;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LogIn extends JFrame{
    private JButton logInBtn;       //LogIn Button;

    //private  user, staff;
    //private CheckboxGroup checkboxGroup;
    private JTextField tf;      //ConsumerID textField
    private JPasswordField passField;   //Password Field In case of Employee Log In
    public JRadioButton user, staff;    //Radio Buttons to switch between user and staff
    private JLabel logInLabel;      //to modify logIn label
    private JLabel passLabel;       //TO modify the Password Label later
    public LogIn() {
        setUpGUI();         //This function will set-up(initialize) gui elements
        displayUserGUI();   //This function will display controls used by Consumer
        setVisible(true);   //To make the screen Visible
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JPasswordField getPassField() {
        return passField;
    }   //Returns passField Object, This will be used in
                                                                //LogInButtonListener Class

    public JTextField getConsumerIDField() {
        return tf;
    }   //Returns the id Text Field

    public void displayError(String str)
    {
        /**
         * function displayError(String) : void
         * This function will be used to display error on form when required
         */
        JLabel error = new JLabel(str); //creates label named error
        this.add(error);            //Add the Error to Form(frame)
        error.setForeground(Color.RED);     //Set TextColor to Red
        error.setBounds(
                150,        //X-position
                400,        //Y-Position
                300,    //Width
                100     //Height
        ); //Set the position of this label to bottom of form
        error.setVisible(true);     //make the error label visible on form
        /**
         * Following statements will create a new Timer Object with delay of
         * 2000 milliseconds or 2 seconds
         * after 2 seconds ActionListener will be invoked
         * In action listener we will hide the error so whenever an error occurs it will appear for 2seconds only
         */
        new Timer(2000,
                new ActionListener() {      //Creates an anonymous class implementing action listen
            @Override
            public void actionPerformed(ActionEvent e) {    //Override the actionPerformed method from super class
                error.setVisible(false);            //This statement will hide the error
            }
        }).start();     //Starts the timer. Without the start() function called timer wont start and ultimately it
                        // will have no effect
    }
    public void setUpGUI() {
    /**
    * Following statement will create label to display Title image of sui northern gas
     * This will load the icon named incon.png from filesystem and justify it to center
    */
        JLabel image = new JLabel(
                new ImageIcon("incon.png"), //Image to load
                JLabel.CENTER               //Image justification on the label
        );

        image.setBounds(0,20,600,100);  //sets image location the image on the top of frame


        logInBtn = new JButton("Log In");   //initializing LogIn Button
        tf = new JTextField();                  // Initializes ID textField
        tf.setToolTipText("Your Consumer ID here"); //Set Tool Tip on ID textField
                    //Tool Tip will be displayed when you hover mouse on textField for few seconds
        logInLabel = new JLabel("Consumer ID");     //Initializes ID label
        logInLabel.setBounds(70, 200, 80, 40);  //Sets the position of ID label to almost center
        tf.setBounds(180,200,200,30);       //positions the ID textfield to the right of ID label
        logInBtn.setBounds(220,300,80,30);  //positions the LogIn button down

        /**
         * This will add an action Listener to logInBtn so whenever LogIn button is clicked the actionPerformed method
         * of LogInButtonListener class is called
         * whenever user clicks logInBtn it will check whether Consumer or Staff radio button is clicked
         * in case of consumer this will take the values from Consumer ID field and verifies that record with this
         * id exist in dataBase
         * in case of Staff this will take values from ID field and password field and verifies it
         */
        logInBtn.addActionListener(
                new LogInButtonListener(this)   //creates a new LogInButtonListener Object and passes it as a
                                                //logIn button listener
                                                //Passing login form to the object because listener will need it to
                                            //get ID and password(in case of staff radio button checked)
        );

        passLabel = new JLabel("Password: ");   //Creats new label to indicate password field on right
        passLabel.setBounds(70,250, 100,50);    //set position down to the LogIn label
        passField = new JPasswordField();           //Creates password field
        passField.setBounds(180,250,200,30);//set position right to the password label


        user = new JRadioButton(
                "Consumer", //Creates a new radio button With text Consumer
                true    //checkbox will be selected by default
        );
        staff = new JRadioButton("Staff", false);   //Same as above but check box will not be selected
        user.setBounds(180, 150, 100, 40);    //Set radio button on top of the labels and down to the
                                                                //image

        /**
         * add radio button listener to user radio button so whenever user selects it
         * then this itemStateChanged function will hide Password Field
         */
        user.addItemListener(new RadioButtonListener(this));

        staff.setBounds(300,150, 80, 40);//set staff Radio button next to consumer radiobutton

        staff.addItemListener(new RadioButtonListener(this)); //add radio button listener to staff radiobutton
            //detailed description is given above

        ButtonGroup buttonGroup = new ButtonGroup();    //Creates new button group
        buttonGroup.add(user);  //add consumer and staff radiobutton to group
        buttonGroup.add(staff);
        //With the help of button group now only one radio button canbe selected at a time
        Image icon = Toolkit.getDefaultToolkit().getImage("icon_title.png"); //Loads the icon from filesystem
        this.setIconImage(icon);    //set image as title image... image on to left of window
        JLabel developer = new JLabel("Developed by Safwan");
        developer.setBounds(450,400,150,100);
        this.add(developer);
        this.setTitle("Sui Gas Billing System");    //Set title of window
        this.add(image);        //add Sui Northern gas logo to window
        this.setSize(600,500);  //set window size 600x500
        this.setLayout(null);       //set layout manager as null. Layout managers help in setting up elements in a Component
        this.add(user);         //add user radio button
        this.add(staff);        //add staff radio button
        this.add(passLabel);    //add password label to Frame
        this.add(passField);    //add password field to frame
        this.add(tf);           //add ID textfield to frame
        this.add(logInBtn);     //add logIn button to frame
        this.add(logInLabel);   // add logIn label to frame
        //  this.setVisible(true);
    }

    /**
     * Following function will be called by RadioButtonListener.itemStateChanged():void function
     * this function will hide password field and password label
     * and change logIn label from Employee ID: to Consumer ID
     */
    public void displayUserGUI() {
        logInLabel.setText("Consumer ID: ");
        passField.setVisible(false);
        passLabel.setVisible(false);

    }

    /**
     * Following function will be called by RadioButtonListener.itemStateChanged():void function
     * when the staff button is selected
     * this function will un-hide password field and password label
     * and change logIn label from Consumer ID to Employee ID:
     */

    public void displayStaffGUI() {
        logInLabel.setText("Employee ID: ");
        passField.setVisible(true);
        passLabel.setVisible(true);

    }
}
package app;

import javax.swing.*;
import java.awt.event.*;

/**
 * This class is an event listener for LogIn button from LogIn class
 */
public class LogInButtonListener implements ActionListener {
    LogIn form;     //This will be used to store the reference to the source form


    public LogInButtonListener(final LogIn lg)
    {
        //System.out.println("In listener");
        form = lg;      //Assign the parameter
    }


    /**
     * This function will take parameters and verify if id is 1 Password is "password"
     * @param id Employee ID
     * @param pass Password
     * @return true if id is 1 and Password is "password"
     * @return false otherwise
     */
    public boolean verifyLogIn(int id, String pass) {
        return (id == 1 && pass.equals("password"));
    }

    /**
     * This function will be called when user clicks log In button
     * @param ae Action event
     * @throws NumberFormatException if Employee ID is an Invalid integer
     */
    @Override
    public void actionPerformed(ActionEvent ae) throws NumberFormatException{
        //System.out.println("Action Performed");
        JTextField tf = form.getConsumerIDField();  //get reference to consumer id text field
        JPasswordField pass;    //to store reference to password field of LogIN form
        int consumerID = 0;     // consumer id we will get it from consumerID field
        //System.out.println("In Try Block()");
        try {
            consumerID = Integer.parseInt(tf.getText()); /*
             tf.getText() will return content as String
            so we will convert it to Integer using parseInt(String) function
            This will throw NumberFormatException if value inside tf cannot be converted to int
            in that case catch block will show error
            */

            if(!DataBase.isConnected())
                DataBase.setUpConnection(); //Verifies that we are connected to DataBase
            /**
             * if staff Radio button is selected then we must get password from passwordField
             * then we pass password and ID to verifyLogIn function
             */
            if(form.staff.isSelected()) {
                pass = form.getPassField();
                if(verifyLogIn(consumerID, pass.getText())) {
                    //if logIn credentials are correct then create new app.StaffForm
                    new StaffForm();
                    form.dispose(); //destroy logIn Form
                }
                else
                    form.displayError("Error: Wrong ID or Password entered"); //in case of wrong id or password
            }
            else
            {
                /**
                 * If Staff Radio button was not selected then it means Consumer button was selected so
                 * getConsumerID and check from database if user with this id exist or not
                 */
                if(DataBase.verifyUser(consumerID)) {
                    new UserForm(consumerID);   //Create a new app.UserForm for specific user
                    form.dispose();     //Distroy LogIn form
                    //form.setLogInSuccess(true);
                }
                else
                    form.displayError("No Such Consumer Found");    //error if user not verified
            }
        }//System.out.println(consumerID);
        catch(Exception e) {
            /**
             * In-case of any exception(NumberFormatException) clear text in TextField
             * displayError on form
             */
            //System.out.println(e);
            tf.setText("");
            form.displayError("Invalid ID entered");
        }

    }
}

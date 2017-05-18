package com.example.shaju.letzchat.registration_page.view;



public interface RegistrationPageDisplayer {

    interface RegistrationPageClickActionListener {

        //Submit button click to Register
        void registerButtonClick(String emailIN, String passwordIN, String confirm);

        //For dialog
        void onAlertDialogDismissed();

        //Go to login panel
        void loginButtonSelected();

    }

    //Show the error from string.xml file
    void showRegistrationAlertDialog(int stringID);

    void showErrorFromResourcesString(int stringID);

    //Attach the action listener
    void attach(RegistrationPageClickActionListener actionListener);
    //remove listeners
    void detach(RegistrationPageClickActionListener actionListener);


}

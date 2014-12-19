package uk.ac.gcu.nbrown201.taylorswift;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;


// This class will be used to create and return the about dialog so we can show the user
// more information about my applications.
public class AboutDialog extends DialogFragment {

    // This creates and returns the about dialog.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Creates a dialog that we can build upon.
        AlertDialog.Builder aboutDialog = new AlertDialog.Builder(getActivity());

        // This will set the title of our dialog.
        aboutDialog.setTitle("About");

        //This will set the about text that we want the user to see so they can understand the
        //application better
        aboutDialog.setMessage("This application is all about Taylor Swift. There is two buttons" +
                "you can press the first being 'News' which is some news from an RSS feed. The second" +
                "button is tour dates and venues of Taylor Swifts last tour");

        // Will return the dialog so that the application can use it.
        return aboutDialog.create();
    }

}

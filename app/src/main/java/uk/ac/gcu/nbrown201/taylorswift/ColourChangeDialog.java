package uk.ac.gcu.nbrown201.taylorswift;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

// this class is a dialog that allows the user to add a colour they like into the
// shared preferences so that it can be used throughout the application.
public class ColourChangeDialog extends DialogFragment implements DialogInterface.OnClickListener {

    public static final CharSequence[] colors_list={"Pink","Blue","Yellow","Green"};
    public static final CharSequence[] colour_ref_names = {"hot_pink", "baby_blue", "yellow", "green"};

    // this method will create the dialog box by
    // display a list of colours
    // http://www.javabeat.net/lists-checkboxes-radio-buttons-alertdialog-android/
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // create the dialog.
        AlertDialog.Builder colours = new AlertDialog.Builder(getActivity());

        // set the title
        colours.setTitle("Choose a colour");
        // set the items that have been defined above.
        colours.setItems(colors_list, this);

        // return the dialog object.
        return colours.create();
    }

    // when a user clicks on an item it will find the referenced colours
    // id and save this into the users shared prefs to be used by the
    // application.
    @Override
    public void onClick(DialogInterface dialogInterface, int position) {

        // get the name of the colour.
        CharSequence name = colour_ref_names[position];

        // need to cast the name into a string so that it can be passed to the next method.
        String colourName = (String)name;

        // find the colour in the resources. (like the drawable but with images)
        int appColour = getResources().getIdentifier(colourName, "color", "uk.ac.gcu.nbrown201.taylorswift");

        // get the editor for the preferenaces.
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit();
        // put the preference into a key called "app_colour"
        editor.putInt("app_colour", appColour);
        // this is so that the map can display the appropriate colour based on the name. (cant put a id colour on the marker.. has to be a float)
        editor.putString("app_colour_string", colourName);
        // commit the change so that it saves it.
        editor.commit();

        // tell the user what colour was clicked.
        Toast.makeText(getActivity().getApplicationContext(), "You clicked colour: " + name + " which is " + appColour, Toast.LENGTH_SHORT).show();
    }
}

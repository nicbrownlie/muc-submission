package uk.ac.gcu.nbrown201.taylorswift;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// This activity is the first class that is shown to a user when they launch the application
// it implement the View.OnClickListener so that it can get notified when a user clicks a button.
public class MainActivity extends Activity implements View.OnClickListener {

    Button newsButton;
    Button tourButton;

    //Application Start Up.
    //Everytime the this activity is loaded it will
    //pick a random image and then display it in the background.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets up all the images into an array.
        String[] swiftImages = {
                "drawable/ts1", "drawable/ts2", "drawable/ts3", "drawable/ts4", "drawable/ts5",
                "drawable/ts6", "drawable/ts7", "drawable/ts8", "drawable/ts9", "drawable/ts10"
        };

        //get a random path from the array (http://stackoverflow.com/questions/9055287/select-a-random-value-from-an-array)
        String randomImagePath = swiftImages[new Random().nextInt(swiftImages.length)];

        // Get the images id reference so it can be used in an image view.
        int imgResID = getApplicationContext().getResources().getIdentifier(randomImagePath, "drawable", "uk.ac.gcu.nbrown201.taylorswift");

        //Find the image by id.
        ImageView imageView = (ImageView)findViewById(R.id.backgroundImageView);

        //set the random image to the img id.
        imageView.setImageResource(imgResID);

        ColourChangeDialog change = new ColourChangeDialog();
        change.show(getFragmentManager(), "colours");


        //find and store the buttons in the class properties so we can
        //launch the correct activity depending on what button was pressed.
        newsButton = (Button)findViewById(R.id.newsButton);
        tourButton = (Button)findViewById(R.id.tourButton);
        newsButton.setOnClickListener(this);
        tourButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // In this method I check to see if the id is the about action
    // if it is then I create the AboutDialog class and then show it.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "Settings button pressed", Toast.LENGTH_SHORT).show();
            return true;
        } else if(id == R.id.action_about) {
            DialogFragment aboutDialog = new AboutDialog();
            aboutDialog.show(getFragmentManager(), "about");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Called when a button is clicked.
    @Override
    public void onClick(View view) {

        //Compares the view to check what button was pressed
        //Depending on the button pressed an intent is created
        //and then a new activity is launched from that intent.
        if(view == newsButton) {
            Toast.makeText(getApplicationContext(), "News button pressed", Toast.LENGTH_SHORT).show();
            Intent newsIntent = new Intent(getApplicationContext(), NewsActivity.class);
            startActivity(newsIntent);

        } else if(view == tourButton) {
            Toast.makeText(getApplicationContext(), "Tour button pressed", Toast.LENGTH_SHORT).show();
            Intent tourIntent = new Intent(getApplicationContext(), TourActivity.class);
            startActivity(tourIntent);
        }
    }



}

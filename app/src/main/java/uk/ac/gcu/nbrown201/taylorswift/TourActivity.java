package uk.ac.gcu.nbrown201.taylorswift;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;

// this activity uses the Google Maps API and the Database Manager to display the tour dates
// on a map.
public class TourActivity extends FragmentActivity {


    // Holds the markers that will be placed on the map.
    private Marker[] mapMarkers;
    // The Map that will be used
    private GoogleMap mapTourDates;

    // The array of tour dates.
    private ArrayList<TourDateInfo> tourDates;

    // The lat long of glasgow so the map can be centered there.
    private LatLng latLongGlasgow = new LatLng(55.861138, -4.250144);

    // object for the shared preferences.
    SharedPreferences sharedPreferences;

    BitmapDescriptor markerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // finds the map fragment view instead of the activity_tour file.
        setContentView(R.layout.map_view);

        //load the prefs
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // images for each of the preferences colours were created as markers
        // this is so that we can display a custom marker image.
        // https://developers.google.com/maps/documentation/android/marker#customize_the_marker_image
        // but get the colour from the prefs
        String colourString = sharedPreferences.getString("app_colour_string", "hot_pink");

        int markerImgRes = 0;
        if(colourString.equalsIgnoreCase("hot_pink")) {
            markerImgRes = getResources().getIdentifier("pink_marker", "drawable", "uk.ac.gcu.nbrown201.taylorswift");
        } else if(colourString.equalsIgnoreCase("baby_blue")) {
            markerImgRes = getResources().getIdentifier("blue_marker", "drawable", "uk.ac.gcu.nbrown201.taylorswift");
        } else if(colourString.equalsIgnoreCase("green")) {
            markerImgRes = getResources().getIdentifier("green_marker", "drawable", "uk.ac.gcu.nbrown201.taylorswift");
        } else if(colourString.equalsIgnoreCase("yellow")) {
            markerImgRes = getResources().getIdentifier("yellow_marker", "drawable", "uk.ac.gcu.nbrown201.taylorswift");
        }

        // set the marker image to be used when adding markers to the map
        markerImage = BitmapDescriptorFactory.fromResource(markerImgRes);

        // creates the database manager. this will either find the exisiting database
        // or create a new one.
        TourDateInfoDBMgr tourDateInfoDBMgr = new TourDateInfoDBMgr(this, "tourdates.sqlite", null, 6);


        try {
            //creates the database.
            tourDateInfoDBMgr.dbCreate();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // gets all the tour dates.
        tourDates = tourDateInfoDBMgr.getAllTourDates();

        // sets up the map and adds markers to it.
        setUpMap();
        addMarkers();


    }


    public void setUpMap() {
        // gets the map from the fragment.
        mapTourDates = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        if(mapTourDates != null) {
            // if the map is there it will position it over the UK with glasgow as the centre.
            mapTourDates.moveCamera(CameraUpdateFactory.newLatLngZoom(latLongGlasgow, 5));
        }
    }

    // adds the markers to the map
    public void addMarkers() {

        // a marker options object
        MarkerOptions marker;
        // the current tour date that it going to be placed on the map
        TourDateInfo tourDate;
        // the title of the marker
        String title;
        // the date of the tour event.
        String text;


        // loop over all the tourdates that were found in the SQL database.
        for(int i = 0; i< tourDates.size(); i++) {
            // get all the tour date information for each of the found items
            tourDate = tourDates.get(i);
            title = tourDate.getVenueName();
            text = tourDate.getDate();

            // create a marker and set its data and position on the map.
            marker = SetMarker(title, "Performance on: " + text, new LatLng(tourDate.getLatitude(), tourDate.getLongitude()), 210.0f, true);
            //add the marker to the map.
            mapTourDates.addMarker(marker);
        }

    }

    // will return a marker options object to be added to the database.
    public MarkerOptions SetMarker(String title, String snippet, LatLng position, float markerColour, boolean centreAnchor) {
        float anchorX = 0.5f;
        float anchorY = centreAnchor ? 0.5f : 1f;
        return new MarkerOptions().title(title).snippet(snippet)
                .icon(markerImage)
                .anchor(anchorX, anchorY).position(position);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tour, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

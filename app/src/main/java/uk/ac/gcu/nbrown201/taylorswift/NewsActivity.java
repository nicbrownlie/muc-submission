package uk.ac.gcu.nbrown201.taylorswift;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

//This class is the first page I created, takes uses the RSSParser that we used in the labs
//but the the addition of being able to return the whole list of RSS Items in a feed. I revisited
//my previous coursework (Introduction to Mobile Device Programming) to amend the code to get
//the list and then display in in the ListView.
public class NewsActivity extends Activity {

    // url of the RSS feed to read from.
    String URL = "http://musicfeeds.com.au/feeds/taylor-swift/feed/";
    // the list view to display the news in.
    ListView listView;
    // the array of items from the news feed.
    ArrayList<mcRSSDataItem>  latestTSNews;

    // object for the shared preferences.
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        //Find the list view
        listView = (ListView)findViewById(R.id.listItems);

        // set up the parser with the context and the URL that is to be parsed
        AsyncTSRSSParser parser = new AsyncTSRSSParser(getApplicationContext(), URL);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        try {
            // execute the parser. this will be done in the background.
            latestTSNews = parser.execute("").get();
            // create the adapater to hold the parsed data items.
            NewsListAdapter adapter = new NewsListAdapter(getApplicationContext(), latestTSNews);
            // add the adapter to the listview so it can display the data.
            listView.setAdapter(adapter);

        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch(ExecutionException e) {
            e.printStackTrace();
        }

    }

    // This is an Adapter to display a list of data items.
    // This allows the listview to display many items.
    private class NewsListAdapter extends BaseAdapter {

        //xml layout for corresponding view items
        private LayoutInflater li;

        //initialise roadworks ArrayList
        private ArrayList<mcRSSDataItem> dataitems = new ArrayList<mcRSSDataItem>();

        // constructor
        public NewsListAdapter(Context context, ArrayList<mcRSSDataItem> items)
        {
            li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(items != null)
                dataitems = items;
        }

        //methods to traverse ArrayList roadworks
        public int getCount() {
            return dataitems.size();
        }

        public Object getItem(int position) {
            return dataitems.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        // this method gets a view with the data item in it.
        public View getView(int position, View convertView, ViewGroup parent) {

            //If there is a view this will not be null and it can be recycled.
            View v = convertView;

            //Gets the dataitem that should be displayed in this view.
            final mcRSSDataItem roadwork = dataitems.get(position);
            if (v == null) {
                // inflates view if there is no view to be recycled.
                v = li.inflate(R.layout.news_item, null);
            }
            // text view for the title in the rss feed defined in the
            final TextView title = (TextView) v.findViewById(R.id.title);

            // gets the selected colour id from the start of the application.
            int appColour = sharedPreferences.getInt("app_colour", 0);
            // sets this colour to each background.
            title.setBackgroundResource(appColour);
            // sets the text of the title.
            title.setText(roadwork.getItemTitle());
            // text view for the description in the rss feed
            final TextView desc = (TextView)v.findViewById(R.id.desc);
            // sets the description
            desc.setText(Html.fromHtml(roadwork.getItemDesc()));
            //return the view.
            return v;
        }
    }
}

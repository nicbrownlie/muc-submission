package uk.ac.gcu.nbrown201.taylorswift;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.PriorityQueue;

// This is a class that allows us to fetch and parse the RSS in the background so that
// the applications 'main' UI is not blocked.
public class AsyncTSRSSParser extends AsyncTask<String, Integer, ArrayList<mcRSSDataItem>> {

    // the context in which the task is running.
    private Context appContext;
    // the url to parse the rss data.
    private String urlRSSToParse;

    // Constructor function sets the variables.
    public AsyncTSRSSParser(Context appC, String urlRSS) {
        appContext = appC;
        urlRSSToParse = urlRSS;
    }

    // This is the method that will do the work in the background and then
    // return the results so the UI can use them.
    @Override
    protected ArrayList<mcRSSDataItem> doInBackground(String... strings) {
        //A local variable to hold the parsed data items
        ArrayList<mcRSSDataItem> parsedData;
        // Creates the parser.
        mcRSSParser rssParser = new mcRSSParser();
        try {
            //Passes the URL to parse into the parser and then it will read and parse the data
            // into mcDataItem objects.
            rssParser.parseRSSData(urlRSSToParse);
        } catch(MalformedURLException e) {
            e.printStackTrace();
        }
        // the data has been parsed and put into objects
        // this will get the data and then return it.
        parsedData = rssParser.getRSSDataItems();
        return parsedData;
    }

    // This uses the context to show the toast.
    @Override
    protected void onPreExecute() {
        Toast.makeText(appContext, "Parsing started!", Toast.LENGTH_SHORT).show();
    }

    // Uses the context to show the toast.
    @Override
    protected void onPostExecute(ArrayList<mcRSSDataItem> mcRSSDataItem) {
        Toast.makeText(appContext, "Parsing finished!", Toast.LENGTH_SHORT).show();
    }
}

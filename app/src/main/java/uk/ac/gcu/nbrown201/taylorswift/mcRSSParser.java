package uk.ac.gcu.nbrown201.taylorswift;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

// reused from the labs.
// class can parse any XMl document with xml tags 'item' and within
// them should be a 'title', 'description' and 'link' tag
public class mcRSSParser {

    // holds many items instead of one.
    private ArrayList<mcRSSDataItem>  rssDataItems;

    // returns all the items in an list
    public ArrayList<mcRSSDataItem> getRSSDataItems() {
        return this.rssDataItems;
    }

    // this now loops over all the items and adds each of them to an array
    // instead of just returning the last item in the list.
    // this is show it can be shown in a list view.
    public void parseRSSDataItem(XmlPullParser parser, int theEventType)
    {
        try
        {
            //get the event type.
            int eventType = parser.getEventType();

            //create a variable for the current item being read from.
            mcRSSDataItem currentDataItem = null;

            //do the following until the event type is the end of the document.
            while (eventType != XmlPullParser.END_DOCUMENT){

                // will store the name of the tag that the parser has found.
                String name = null;

                // checks to see what the event type is and depending on that
                // will perform something.
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        // there is a xml document so create the array to store the items.
                        rssDataItems = new ArrayList<mcRSSDataItem>();
                        break;
                    case XmlPullParser.START_TAG:
                        //get the current tag name
                        name = parser.getName();

                        //searches for tag 'item'
                        if (name.equalsIgnoreCase("item")){
                            // create a new item object.
                            currentDataItem = new mcRSSDataItem();
                        } else if (currentDataItem != null){

                            if (name.equalsIgnoreCase("link")){
                                //searches for tag link within  item
                                // then adds it to the data item.
                                currentDataItem.setItemLink(parser.nextText());
                            } else if (name.equalsIgnoreCase("description")){

                                //searches for tag description within  item
                                // then adds it to the data item.
                                currentDataItem.setItemDesc(parser.nextText());
                            } 
                            else if (name.equalsIgnoreCase("title")){
                                //searches for tag title within  item
                                // then adds it to the data item.
                                currentDataItem.setItemTitle(parser.nextText());
                            }
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        // gets the name of the tag
                        name = parser.getName();
                        //ensures its an item end tag and it doesnt equal null.
                        if (name.equalsIgnoreCase("item") && currentDataItem != null){
                            // will add the current road work to the list.
                            rssDataItems.add(currentDataItem);
                        }
                        break;
                }
                // gets the next event type to look at.
                eventType = parser.next();
            }
        }
        catch (XmlPullParserException parserExp1)
        {
            Log.e("MyTag","Parsing error" + parserExp1.toString());
        }

        catch (IOException parserExp1)
        {
            Log.e("MyTag","IO error during parsing");
        }

    }

    // will parse the data from a URL.
    public void parseRSSData(String RSSItemsToParse) throws MalformedURLException {
        URL rssURL = new URL(RSSItemsToParse);
        InputStream rssInputStream;
        try
        {
            XmlPullParserFactory parseRSSfactory = XmlPullParserFactory.newInstance();
            parseRSSfactory.setNamespaceAware(true);
            XmlPullParser RSSxmlPP = parseRSSfactory.newPullParser();
            String xmlRSS = getStringFromInputStream(getInputStream(rssURL), "UTF-8");
            RSSxmlPP.setInput(new StringReader(xmlRSS));
            int eventType = RSSxmlPP.getEventType();

            parseRSSDataItem(RSSxmlPP,eventType);

        }
        catch (XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("MyTag","IO error during parsing");
        }
        Log.e("MyTag","End document");
    }

    // uses the URL to get the stream of data.
    public InputStream getInputStream(URL url) throws IOException
    {
        return url.openConnection().getInputStream();
    }

    // get a readable string from the input stream.
    public static String getStringFromInputStream(InputStream stream, String charsetName) throws IOException
    {
        int n = 0;
        char[] buffer = new char[1024 * 4];
        InputStreamReader reader = new InputStreamReader(stream, charsetName);
        StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
        return writer.toString();
    }
}
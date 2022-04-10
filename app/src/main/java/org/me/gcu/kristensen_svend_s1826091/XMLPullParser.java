package org.me.gcu.kristensen_svend_s1826091;
//NAME SVEND KRISTENSEN
//STUDENTID : S1820691
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;

public class XMLPullParser {
    static final String KEY_CHANNEL= "channel";
    static final String KEY_ITEM = "item";
    static final String KEY_TITLE = "title";
    static final String KEY_DESC = "description";
    static final String KEY_LINK = "link";
    static final String KEY_GEORSS = "georss:point";
    static final String KEY_PUBDATE = "pubDate";

    public static List<TrafficScotlandModel> getFromFile(Context ctx,String fileToUse) {

        // List of roadworks/incidents/ planned roadworks that we will return
        List<TrafficScotlandModel> TrafficScotlandList;
        TrafficScotlandList = new ArrayList<TrafficScotlandModel>();

        TrafficScotlandModel curTSM = null;

        String curText = "";

        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            FormatDateDescription formatDate = new FormatDateDescription();
            FileInputStream fis = ctx.openFileInput(fileToUse);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));


            xpp.setInput(reader);

            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                String tagname = xpp.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase(KEY_CHANNEL)) {
                            // do nothing... who needs the channel tag!
                        }
                        if (tagname.equalsIgnoreCase(KEY_TITLE)) {

                            curTSM = new TrafficScotlandModel();

                        }
                        break;

                    case XmlPullParser.TEXT:
                        curText = xpp.getText();
                        break;


                    case XmlPullParser.END_TAG:

                        if (tagname.equalsIgnoreCase(KEY_ITEM))  {
                            TrafficScotlandList.add(curTSM);
                        } else if (tagname.equalsIgnoreCase(KEY_TITLE)){
                            curTSM.setTitle(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_DESC)) {
                            curTSM.setDescription(formatDate.descriptionChopper(curText)); // send the entire description to the description chopper to remove the date information, leaving just desc txt
                            String[] datesArr = formatDate.dateChopper(curText); //send the description to the date chopper to remove the description and keep the dates
                            if(datesArr != null) {
                                curTSM.setStartDate(formatDate.convertDate(datesArr[0]));
                                curTSM.setEndDate(formatDate.convertDate(datesArr[1]));
                                long res;
                                res = formatDate.identifyDaysBetweenDates(curTSM.getStartDate(), curTSM.getEndDate());
                                curTSM.setDays(res);
                            }
                        } else if (tagname.equalsIgnoreCase(KEY_LINK)) {
                            curTSM.setLink(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_GEORSS)) {
                            curTSM.setGeorss(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_PUBDATE)) {
                            curTSM.setPubDate(curText);
                        }
                        break;

                    default:
                        break;
                }
                //move on to next iteration
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return the populated list.
        return TrafficScotlandList;
    }
}
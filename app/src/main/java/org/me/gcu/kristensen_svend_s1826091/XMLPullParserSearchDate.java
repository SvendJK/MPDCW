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
import android.util.Log;

public class XMLPullParserSearchDate {
    static final String KEY_CHANNEL= "channel";
    static final String KEY_TITLE = "title";
    static final String KEY_DESC = "description";
    static final String KEY_LINK = "link";
    static final String KEY_GEORSS = "georss";
    static final String KEY_PUBDATE = "pubDate";

    public static List<TrafficScotlandModel> getFromFile(Context ctx, String textInput,String fileToUse) {

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
                        //still nothing

                        }
                        if (tagname.equalsIgnoreCase(KEY_TITLE)) {
                            curTSM = new TrafficScotlandModel();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        curText = xpp.getText();
                        break;


                    case XmlPullParser.END_TAG:


                        if (tagname.equalsIgnoreCase(KEY_TITLE)) {
                            curTSM.setTitle(curText);

                        } if (tagname.equalsIgnoreCase(KEY_DESC))  {
                            curTSM.setDescription(formatDate.descriptionChopper(curText));
                            String[] datesArr = formatDate.dateChopper(curText);
                            if(datesArr != null) {
                                curTSM.setStartDate(formatDate.convertDate(datesArr[0]));
                                curTSM.setEndDate(formatDate.convertDate(datesArr[1]));
                                long res;
                                res = formatDate.identifyDaysBetweenDates(curTSM.getStartDate(), curTSM.getEndDate());
                                curTSM.setDays(res);
                               // Log.i("TESTINGGGG","the current getstartdate is " +curTSM.getStartDate());
                        if (curTSM.getStartDate().equals(textInput) || curTSM.getEndDate().equals(textInput)) {
                                    TrafficScotlandList.add(curTSM);
                                }

                        } else if (tagname.equalsIgnoreCase(KEY_TITLE)){
                            curTSM.setTitle(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_DESC)) {
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
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return the populated list.
        return TrafficScotlandList;
    }
}

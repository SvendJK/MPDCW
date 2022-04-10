package org.me.gcu.kristensen_svend_s1826091;
//NAME SVEND KRISTENSEN
//STUDENTID : S1820691

import android.icu.util.TimeUnit;
import android.icu.util.ULocale;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.graphics.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

public class FormatDateDescription {


    public String descriptionChopper(String description) { // takes description and removes the date information from it
        if (description.contains("<")) {
            return description.substring(description.lastIndexOf("<") +6);
        } else {
            return description;
        }

    }
    public String[] dateChopper(String description) {

        if (description.contains("Start Date: ")){ //if there is is dates present, chop off extra to return just the value . otherwise return null
            String startDate = description.substring(12, description.indexOf('<'));
            String endDate = description.substring(description.indexOf('>'));
            if (description.contains("<")) { //if theres still another br tag in there, get rid
                endDate = endDate.substring(6, endDate.indexOf('<'));
            } else {
                endDate = endDate.substring(11);
            }
            String[] startAndEnd = new String[2];
            startAndEnd[0] = startDate;
            startAndEnd[1] = endDate;
            return startAndEnd;

        } else {
            return null;
        }

    }

    public String convertDate(String jumbledString) throws ParseException { //converts messy date layout to dd/mm/yyyy
        jumbledString = jumbledString.substring(0,jumbledString.length()-8);
        String[] separated = jumbledString.split(",");
        jumbledString = separated[1].substring(1);
        SimpleDateFormat fromParser = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        String reformattedStr = myFormat.format(fromParser.parse(jumbledString));
        return reformattedStr;
    }


    public long identifyDaysBetweenDates(String startDate, String endDate) {
        try {
            Date start = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH) // convert start date to SDF
                    .parse(startDate);
            Date end = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH)
                    .parse(endDate);
            long days =  (end.getTime()-start.getTime())/86400000;

           // System.out.println(days); #testing stuff
            //System.out.println(start);
           // System.out.println(end);
            return days;
//                if(days <7){
//                    return R.color.purple_200;
//                }


        } catch (ParseException e) {
            e.printStackTrace();
            return 0;

        }



    }


}

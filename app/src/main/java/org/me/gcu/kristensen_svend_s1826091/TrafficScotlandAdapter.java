package org.me.gcu.kristensen_svend_s1826091;
//NAME SVEND KRISTENSEN
//STUDENTID : S1820691
import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

// holds list of traffic scotland data after being parsed out of XML and builds the row views to display on screen

public class TrafficScotlandAdapter extends ArrayAdapter<TrafficScotlandModel> {


    public TrafficScotlandAdapter(Context ctx, int textViewResourceId, List<TrafficScotlandModel> sites) {
        super(ctx, textViewResourceId, sites);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        RelativeLayout row = (RelativeLayout)convertView;
        Log.i("Svend CW Adapter", "getView pos = " + pos);
        if(null == row){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (RelativeLayout)inflater.inflate(R.layout.row_site, null);
        }

        //Get our View References
        TextView titleTxt = (TextView)row.findViewById(R.id.titleTxt);
        TextView descTxt = (TextView)row.findViewById(R.id.descriptionTxt);
//        TextView linkTxt = (TextView)row.findViewById(R.id.linkTxt);
//        TextView pubDateTxt = (TextView)row.findViewById(R.id.pubDateTxt);
        TextView startDateTxt = (TextView)row.findViewById(R.id.startDateTxt);
//        //TextView georssTxt = (TextView)row.findViewById(R.id.georssTxt);
       TextView endDateTxt = (TextView)row.findViewById(R.id.endDateTxt);

        final ProgressBar indicator = (ProgressBar)row.findViewById(R.id.progress);


        indicator.setVisibility(View.VISIBLE);
//        iconImg.setVisibility(View.INVISIBLE);


        //georssTxt.setText((getItem(pos).getGeorss()));
        titleTxt.setText(getItem(pos).getTitle());
        descTxt.setText(getItem(pos).getDescription());
//        linkTxt.setText(getItem(pos).getLink());
//        pubDateTxt.setText(getItem(pos).getPubDate());
       startDateTxt.setText(getItem(pos).getStartDate());
       endDateTxt.setText(getItem(pos).getEndDate());
        indicator.setVisibility(View.INVISIBLE);


        long res;
        res = getItem(pos).getDays(); //set title to a specific colour based on the amount of days the roadworks will take. green <= week, yellow <= 2 weeks, red >= two weeks
        if(res <=7){
            titleTxt.setTextColor(Color.parseColor("#34472b"));
        } else if(res <=20 && res > 7) {
            titleTxt.setTextColor(Color.parseColor("#cbd62b"));
        } else if (res >=21 ) {
            titleTxt.setTextColor(Color.parseColor("#61101e"));
        }
        else {
            titleTxt.setTextColor(Color.parseColor("#000000"));
        }

        return row;


    }


}

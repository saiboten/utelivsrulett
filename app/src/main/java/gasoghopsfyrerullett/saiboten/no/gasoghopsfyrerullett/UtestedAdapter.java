package gasoghopsfyrerullett.saiboten.no.gasoghopsfyrerullett;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tobias on 11.09.2016.
 */
public class UtestedAdapter extends ArrayAdapter<Club> {

     public UtestedAdapter(Context context, List<Club> club) {
            super(context, 0, club);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Club club = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_utested, parent, false);
            }
            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
            TextView tvDrinks = (TextView) convertView.findViewById(R.id.tvDrinks);
            TextView tvGuiness = (TextView) convertView.findViewById(R.id.tvGuieness);

            // Populate the data into the template view using the data object
            tvName.setText(club.getNavn());
            tvDrinks.setText(club.isDrinker() ? "Ja" : "Nei");
            tvGuiness.setText(club.isGuiness() ? "Ja" : "Nei");

            // Return the completed view to render on screen
            return convertView;
        }
    }

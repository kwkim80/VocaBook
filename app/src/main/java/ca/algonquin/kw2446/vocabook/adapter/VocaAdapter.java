package ca.algonquin.kw2446.vocabook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import ca.algonquin.kw2446.vocabook.R;
import ca.algonquin.kw2446.vocabook.model.Voca;

public class VocaAdapter extends ArrayAdapter<Voca> {

    private Context context;
    private ArrayList<Voca> list;

    private static class ViewHolder {
        TextView tvWord;
        TextView tvMean;

    }

    public VocaAdapter(@NonNull Context context, ArrayList<Voca> list) {
        super(context, R.layout.voca_items, list);
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int i, @Nullable View convertView, @NonNull ViewGroup parent) {


         Voca voca=list.get(i);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.voca_items,parent ,false);
            viewHolder.tvMean = convertView.findViewById(R.id.tvMean);
            viewHolder.tvWord = convertView.findViewById(R.id.tvWord);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.tvWord.setText(voca.getWord());
        viewHolder.tvMean.setText(voca.getMean());


        // Return the completed view to render on screen
        return convertView;



    }




}

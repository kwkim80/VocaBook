package ca.algonquin.kw2446.vocabook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import ca.algonquin.kw2446.vocabook.R;
import ca.algonquin.kw2446.vocabook.model.Voca;
import ca.algonquin.kw2446.vocabook.model.WordSet;

public class WordEditAdapter extends ArrayAdapter<WordSet> {

    private Context context;
    private ArrayList<WordSet> list;

    private static class ViewHolder {
        TextView tvTitle, tvDate;
        ImageView ivCate;

    }

    public WordEditAdapter(@NonNull Context context, ArrayList<WordSet> list) {
        super(context, R.layout.list_items, list);
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int i, @Nullable View convertView, @NonNull ViewGroup parent) {


        final WordSet wordSet=list.get(i);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent ,false);
            viewHolder.tvTitle=convertView.findViewById(R.id.tvTitle);
            viewHolder.tvDate=convertView.findViewById(R.id.tvDate);
            viewHolder.ivCate=convertView.findViewById(R.id.ivCate);

            // Cache the viewHolder object inside the fresh view


            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.tvTitle.setText(wordSet.getTitle());
        viewHolder.tvDate.setText(wordSet.getRegDate());

        if(wordSet.getCategory().equalsIgnoreCase("english")){
            viewHolder.ivCate.setImageResource(R.drawable.english);
        }else if(wordSet.getCategory().equalsIgnoreCase("french")){
            viewHolder.ivCate.setImageResource(R.drawable.french);
        }else{
            viewHolder.ivCate.setImageResource(R.drawable.korean);
        }



        // Return the completed view to render on screen
        return convertView;


    }



}

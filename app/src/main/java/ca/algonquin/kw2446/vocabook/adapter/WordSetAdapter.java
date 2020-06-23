package ca.algonquin.kw2446.vocabook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.algonquin.kw2446.vocabook.R;
import ca.algonquin.kw2446.vocabook.model.WordSet;

public class WordSetAdapter extends RecyclerView.Adapter<WordSetAdapter.ViewHolder> {
    ArrayList<WordSet> list;

    WordSetItemClicked mainAct;

    public interface WordSetItemClicked{
        void onWordSetItemClicked(WordSet wordSet);
        void onWordSetItemLongClicked(WordSet wordSet);
    }
    public WordSetAdapter(Context context, ArrayList<WordSet> list) {
        this.list = list;
        mainAct=(WordSetItemClicked) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvDate;
        ImageView ivCate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvDate=itemView.findViewById(R.id.tvDate);
            ivCate=itemView.findViewById(R.id.ivCate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   WordSet wordSet=(WordSet)v.getTag();  //by using Tag
                    mainAct.onWordSetItemClicked(wordSet);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    WordSet wordSet=list.get(getAdapterPosition()); //by using getAdapterPosition()
                    mainAct.onWordSetItemLongClicked(wordSet);
                    return false;
                }
            });
        }

        public  void setItem(WordSet item){
            tvTitle.setText(item.getTitle());
            tvDate.setText(item.getRegDate());

            if(item.getCategory().equalsIgnoreCase("english")){
                ivCate.setImageResource(R.drawable.english);
            }else if(item.getCategory().equalsIgnoreCase("french")){
                ivCate.setImageResource(R.drawable.french);
            }else{
                ivCate.setImageResource(R.drawable.korean);
            }

        }
    }

    @NonNull
    @Override
    public WordSetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new WordSetAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WordSetAdapter.ViewHolder holder, int i) {
        WordSet wordSet= list.get(i);
        holder.itemView.setTag(wordSet);
        holder.setItem(wordSet);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

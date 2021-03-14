package ie.lit.ardictionary.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

import ie.lit.ardictionary.MainActivity;
import ie.lit.ardictionary.R;
import ie.lit.ardictionary.model.Notebook;
import ie.lit.ardictionary.model.Word;
import ie.lit.ardictionary.ui.word.WordFragment;

public class NotebookAdapter extends RecyclerView.Adapter<NotebookAdapter.NotebookViewHolder> {
    private Context context;
    private List<Notebook> notebooks;

    class NotebookViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView, dateTextView;

        public NotebookViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.notebookNameTextView);
            dateTextView = itemView.findViewById(R.id.notebookDateTextView);

        }
    }

    public NotebookAdapter(Context context, List<Notebook> notebooks) {
        this.context = context;
        this.notebooks = notebooks;
    }

    @NonNull
    @Override
    public NotebookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notebook_card, parent, false);

        return new NotebookAdapter.NotebookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotebookViewHolder holder, int position) {
        final Notebook notebook = notebooks.get(position);

        holder.dateTextView.setText(notebook.getDate());
        holder.nameTextView.setText(notebook.getName());

        CardView card = holder.itemView.findViewById(R.id.card_view_notebook);

        switch(notebook.getStyle()){
            case "green":
                holder.itemView.setBackgroundResource(R.drawable.card_background_green);
                card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green_light));
                break;
            case "blue":
                holder.itemView.setBackgroundResource(R.drawable.card_background_blue);
                card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.blue_light));
                break;
            case "red":
                holder.itemView.setBackgroundResource(R.drawable.card_background_red);
                card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red_light));
                break;
            case "purple":
                holder.itemView.setBackgroundResource(R.drawable.card_background_purple);
                card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.purple_light));
                break;
            case "yellow":
                holder.itemView.setBackgroundResource(R.drawable.card_background_yellow);
                card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.yellow_light));
                break;
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("notebookId", notebook.getUid());

                Fragment wordFragment = new WordFragment();
                wordFragment.setArguments(bundle);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.notebook_layout, wordFragment, "Word Fragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return notebooks.size();
    }

}

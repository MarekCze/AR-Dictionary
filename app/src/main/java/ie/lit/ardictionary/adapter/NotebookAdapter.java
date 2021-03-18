package ie.lit.ardictionary.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ie.lit.ardictionary.MainActivity;
import ie.lit.ardictionary.R;
import ie.lit.ardictionary.model.Notebook;
import ie.lit.ardictionary.ui.word.WordFragment;

public class NotebookAdapter extends RecyclerView.Adapter<NotebookAdapter.NotebookViewHolder> {
    private Context context;
    private List<Notebook> notebooks;

    class NotebookViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView, dateTextView;
        ImageView overflow;

        public NotebookViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.notebookNameTextView);
            dateTextView = itemView.findViewById(R.id.notebookDateTextView);
            overflow = itemView.findViewById(R.id.overflow);

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

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.overflow);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("notebookId", notebook.getUid());

                String fragmentTag = "Word Fragment";
                Fragment wordFragment = new WordFragment();
                wordFragment.setArguments(bundle);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction()
                        .add(R.id.notebook_layout, wordFragment, fragmentTag)
                        .addToBackStack(fragmentTag)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return notebooks.size();
    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_notebook, popup.getMenu());
        popup.setOnMenuItemClickListener(new NotebookMenuItemClickListener());
        popup.show();
    }

    class NotebookMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public NotebookMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_notebook_change_name:
                    Toast.makeText(context, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_notebook_delete:
                    Toast.makeText(context, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

}

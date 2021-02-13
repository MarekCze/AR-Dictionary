package ie.lit.ardictionary.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import ie.lit.ardictionary.R;
import ie.lit.ardictionary.model.Word;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private Context context;
    private List<Word> words;

    public class WordViewHolder extends RecyclerView.ViewHolder {
        public TextView wordTextView, pronunciationTextView, definitionTextView, synonymTextView;
        public ImageButton audioBtn;

        public WordViewHolder(View view) {
            super(view);
            wordTextView = (TextView) view.findViewById(R.id.wordTextView);
            pronunciationTextView = (TextView) view.findViewById(R.id.pronunciationTextView);
            definitionTextView = (TextView) view.findViewById(R.id.definitionTextView);
            synonymTextView = (TextView) view.findViewById(R.id.synonymTextView);
            audioBtn = (ImageButton) view.findViewById(R.id.audioBtn);
        }
    }

    public WordAdapter(Context context, List<Word> words) {
        this.context = context;
        this.words = words;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_card, parent, false);

        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        final Word word = words.get(position);
        holder.wordTextView.setText(word.getWord());
        holder.pronunciationTextView.setText(word.getPronunciation());
        holder.definitionTextView.setText(buildDefinitions(word.getShortDefs()));
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    private SpannableString buildDefinitions(List<String> definitions){

        StringBuilder sb = new StringBuilder();

        for(String s : definitions){
            sb.append("\n");
            sb.append(s);
        }
        SpannableString string = new SpannableString(sb);
        string.setSpan(new BulletSpan(), 10, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }



}

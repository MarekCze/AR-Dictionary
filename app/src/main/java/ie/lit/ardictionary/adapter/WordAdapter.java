package ie.lit.ardictionary.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.util.Log;
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

import java.io.IOException;
import java.util.List;

import ie.lit.ardictionary.R;
import ie.lit.ardictionary.model.Word;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private Context context;
    private List<Word> words;
    private boolean isPlaying = false;

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

        holder.audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mp = new MediaPlayer();
                if(!isPlaying){
                    isPlaying = true;
                    try {
                        mp.setDataSource(word.getAudio());
                        mp.prepare();
                        mp.start();
                    } catch (IOException e) {
                        Log.e("Err Tag", "prepare() failed");
                    }
                } else {
                    isPlaying = false;
                    mp.release();
                    mp = null;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    private SpannableStringBuilder buildDefinitions(List<String> definitions){

        SpannableStringBuilder sb = new SpannableStringBuilder();
        int charCount = 0;
        for(String s : definitions){
            CharSequence ch = s;
            sb.append(s);
            sb.setSpan(new BulletSpan(), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sb.append("\n");
            charCount += ch.length() + 1;
        }

        return sb;
    }



}

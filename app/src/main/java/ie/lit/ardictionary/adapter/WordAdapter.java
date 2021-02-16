package ie.lit.ardictionary.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
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
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
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
        public RecyclerView bulletRecyclerView;

        public WordViewHolder(View view) {
            super(view);
            wordTextView = (TextView) view.findViewById(R.id.wordTextView);
            pronunciationTextView = (TextView) view.findViewById(R.id.pronunciationTextView);
            definitionTextView = (TextView) view.findViewById(R.id.definitionTextView);
            synonymTextView = (TextView) view.findViewById(R.id.synonymTextView);
            audioBtn = (ImageButton) view.findViewById(R.id.audioBtn);
            bulletRecyclerView = (RecyclerView) view.findViewById(R.id.bullet_recycler_view);
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

    @RequiresApi(api = Build.VERSION_CODES.P)
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

                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        isPlaying = false;
                        mp.release();
                        mp = null;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private SpannableStringBuilder buildDefinitions(List<String> definitions){

        SpannableStringBuilder ssb = new SpannableStringBuilder();

        for(int i = 0; i < definitions.size(); i++){
            String s = definitions.get(i);
            if(i + 1 < definitions.size()){
                s += "\n";
            }
            CharSequence ch = s;
            SpannableString ss = new SpannableString(s);
            ss.setSpan(new BulletSpan(12, Color.parseColor("#000000"), 7), 0, ch.length(), 0);
            ssb.append(ss);
        }

        return ssb;
    }

}

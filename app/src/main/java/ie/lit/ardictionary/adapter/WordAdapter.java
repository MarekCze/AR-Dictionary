package ie.lit.ardictionary.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import ie.lit.ardictionary.R;
import ie.lit.ardictionary.model.Word;
import ie.lit.ardictionary.ui.word.WordViewModel;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private Context context;
    private List<Word> words;
    private boolean isPlaying = false;

    public class WordViewHolder extends RecyclerView.ViewHolder {
        public TextView wordTextView, pronunciationTextView, definitionTextView, synonymTextView, sentencesTextView, sentencesHeadingTextView, synonymHeadingTextView;
        public View hrLineExampleSentences, hrLineSynonyms;
        public ImageButton audioBtn;

        public WordViewHolder(View view) {
            super(view);
            wordTextView = (TextView) view.findViewById(R.id.wordTextView);
            pronunciationTextView = (TextView) view.findViewById(R.id.pronunciationTextView);
            definitionTextView = (TextView) view.findViewById(R.id.definitionTextView);
            synonymTextView = (TextView) view.findViewById(R.id.synonymTextView);
            sentencesTextView = (TextView) view.findViewById(R.id.sentencesTextView);
            sentencesHeadingTextView = (TextView) view.findViewById(R.id.sentencesHeadingTextView);
            synonymHeadingTextView = (TextView) view.findViewById(R.id.synonymHeadingTextView);
            audioBtn = (ImageButton) view.findViewById(R.id.audioBtn);
            hrLineExampleSentences = (View) view.findViewById(R.id.hrLineExampleSentences);
            hrLineSynonyms = (View) view.findViewById(R.id.hrLineSynonyms);
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
        holder.definitionTextView.setText(buildBulletPointList(word.getDefinitions()));
        holder.synonymTextView.setText(buildBulletPointList(word.getSynonyms()));
        holder.sentencesTextView.setText(buildBulletPointList(word.getSentences()));
        holder.itemView.setBackgroundResource(R.drawable.btn_background_white);

        setStyle(holder, word);

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

    @RequiresApi(api = Build.VERSION_CODES.P)
    private SpannableStringBuilder buildBulletPointList(List<String> bulletPoints){

        SpannableStringBuilder ssb = new SpannableStringBuilder();

        if(bulletPoints != null){
            for(int i = 0; i < bulletPoints.size(); i++){
                String s = bulletPoints.get(i);
                if(i + 1 < bulletPoints.size()){
                    s += "\n";
                }
                CharSequence ch = s;
                SpannableString ss = new SpannableString(s);
                ss.setSpan(new BulletSpan(12, Color.parseColor("#000000"), 7), 0, ch.length(), 0);
                ssb.append(ss);
            }
        } else {
            ssb.append("");
        }
        return ssb;
    }

    private void setStyle(WordViewHolder holder, Word word){
        Drawable drawableWrap = DrawableCompat.wrap(holder.audioBtn.getDrawable()).mutate();


        switch(word.getStyle()) {
            case "green":
                holder.wordTextView.setTextAppearance(context, R.style.CardView_GreenTheme);
                holder.sentencesHeadingTextView.setTextAppearance(context, R.style.CardView_GreenTheme);
                holder.synonymHeadingTextView.setTextAppearance(context, R.style.CardView_GreenTheme);
                holder.hrLineExampleSentences.setBackgroundColor(ContextCompat.getColor(context, R.color.green_light));
                holder.hrLineSynonyms.setBackgroundColor(ContextCompat.getColor(context, R.color.green_light));

                DrawableCompat.setTint(drawableWrap, ContextCompat.getColor(context, R.color.green_dark));

                break;
            case "blue":
                holder.wordTextView.setTextAppearance(context, R.style.CardView_BlueTheme);
                holder.sentencesHeadingTextView.setTextAppearance(context, R.style.CardView_BlueTheme);
                holder.synonymHeadingTextView.setTextAppearance(context, R.style.CardView_BlueTheme);
                holder.hrLineExampleSentences.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_light));
                holder.hrLineSynonyms.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_light));

                DrawableCompat.setTint(drawableWrap, ContextCompat.getColor(context, R.color.blue_dark));

                break;
            case "red":
                holder.wordTextView.setTextAppearance(context, R.style.CardView_RedTheme);
                holder.sentencesHeadingTextView.setTextAppearance(context, R.style.CardView_RedTheme);
                holder.synonymHeadingTextView.setTextAppearance(context, R.style.CardView_RedTheme);
                holder.hrLineExampleSentences.setBackgroundColor(ContextCompat.getColor(context, R.color.red_light));
                holder.hrLineSynonyms.setBackgroundColor(ContextCompat.getColor(context, R.color.red_light));

                DrawableCompat.setTint(drawableWrap, ContextCompat.getColor(context, R.color.red_dark));

                break;
            case "purple":
                holder.wordTextView.setTextAppearance(context, R.style.CardView_PurpleTheme);
                holder.sentencesHeadingTextView.setTextAppearance(context, R.style.CardView_PurpleTheme);
                holder.synonymHeadingTextView.setTextAppearance(context, R.style.CardView_PurpleTheme);
                holder.hrLineExampleSentences.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_light));
                holder.hrLineSynonyms.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_light));

                DrawableCompat.setTint(drawableWrap, ContextCompat.getColor(context, R.color.purple_dark));

                break;
            case "yellow":
                holder.wordTextView.setTextAppearance(context, R.style.CardView_YellowTheme);
                holder.sentencesHeadingTextView.setTextAppearance(context, R.style.CardView_YellowTheme);
                holder.synonymHeadingTextView.setTextAppearance(context, R.style.CardView_YellowTheme);
                holder.hrLineExampleSentences.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow_light));
                holder.hrLineSynonyms.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow_light));

                DrawableCompat.setTint(drawableWrap, ContextCompat.getColor(context, R.color.yellow_dark));

                break;
        }
    }

}

package ie.lit.ardictionary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ie.lit.ardictionary.R;
import ie.lit.ardictionary.model.Word;

public class BulletAdapter extends RecyclerView.Adapter<BulletAdapter.BulletViewHolder>{

    private Context context;
    private List<String> defs;

    public class BulletViewHolder extends RecyclerView.ViewHolder {
        public TextView bulletTextView;

        public BulletViewHolder(View view) {
            super(view);
            bulletTextView = (TextView) view.findViewById(R.id.bulletTextView);
        }
    }

    public BulletAdapter(Context context, List<String> defs) {
        this.context = context;
        this.defs = defs;
    }
    @NonNull
    @Override
    public BulletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bullet_card, parent, false);

        return new BulletViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(@NonNull BulletViewHolder holder, int position) {
        final String s = defs.get(position);

        CharSequence ch = s;
        SpannableString ss = new SpannableString(s);
        ss.setSpan(new BulletSpan(12, Color.parseColor("#000000"), 7), 0, ch.length(), 0);

        holder.bulletTextView.setText(s);
    }

    @Override
    public int getItemCount() {
        return defs.size();
    }

}

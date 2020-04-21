package com.projetTec.imageStudio.controller.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projetTec.imageStudio.R;
import com.projetTec.imageStudio.controller.StudioActivity;
import com.projetTec.imageStudio.controller.fragments.Studio_fragment;

import java.lang.reflect.Array;
import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.PhotoEditor;

public class EmojiRecyclerViewAdapter extends RecyclerView.Adapter<EmojiRecyclerViewAdapter.ViewHolder> {

    ArrayList<String> emojiList;

    private OnEmojiClickListener onEmojiClickListener;

    public EmojiRecyclerViewAdapter() {
        emojiList = PhotoEditor.getEmojis(StudioActivity.getContextOfApplication());
    }

    public interface OnEmojiClickListener {
        void onEmojiClickListener(String emojiCode);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(StudioActivity.getContextOfApplication()).inflate(R.layout.emoji_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.emojiText.setText(emojiList.get(position));
    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView emojiText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emojiText = itemView.findViewById(R.id.text_view_emoji_row);

            emojiText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEmojiClickListener.onEmojiClickListener(emojiList.get(getLayoutPosition()));
                }
            });
        }
    }

}

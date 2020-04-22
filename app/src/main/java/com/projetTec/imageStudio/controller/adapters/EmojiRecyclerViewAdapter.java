package com.projetTec.imageStudio.controller.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projetTec.imageStudio.R;
import com.projetTec.imageStudio.controller.StudioActivity;

import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.PhotoEditor;

/**
 * <p>
 * The class is an adapter of the EmojiRecyclerView, it generates a list of emoji that will be displayed and sets up a listener.
 * </p>
 *
 * @author Kamel.H
 * @see com.projetTec.imageStudio.controller.bottomDialogFragment.EmojiBottomDialogFragment
 */
public class EmojiRecyclerViewAdapter extends RecyclerView.Adapter<EmojiRecyclerViewAdapter.ViewHolder> {

    //The list which will contain the different emoji
    private final ArrayList<String> emojiList;

    //The onEmojiClickListener method which facilitates the management of listener
    private OnEmojiClickListener onEmojiClickListener;

    /**
     * <p>
     *     The constructor takes nothing in parameter, ie fills the list with the different emoji.
     * </p>
     *
     * @see com.projetTec.imageStudio.controller.bottomDialogFragment.EmojiBottomDialogFragment
     */
    public EmojiRecyclerViewAdapter() {
        emojiList = PhotoEditor.getEmojis(StudioActivity.getContextOfApplication());
    }

    /**
     * <p>
     * The interface that will be implemented in the class where the {@link EmojiRecyclerViewAdapter}
     * will be instantiated and used.
     * </p>
     *
     * @author Kamel.H
     * @see com.projetTec.imageStudio.controller.bottomDialogFragment.EmojiBottomDialogFragment
     */
    public interface OnEmojiClickListener {

        /**
         * <p>
         * The listener which will be called when a emoji is selected in the Emoji RecyclerView class.
         * </p>
         *
         * @param emojiCode The Emoji code
         */
        void onEmojiClickListener(String emojiCode);

    }

    /**
     * <p>
     * The setter of onEmojiClickListener.
     * </p>
     *
     * @param onEmojiClickListener The new listener
     */
    public void setOnEmojiClickListener(OnEmojiClickListener onEmojiClickListener) {
        this.onEmojiClickListener = onEmojiClickListener;
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

    /**
     * <p>
     * The class allows us to extract the elements of the exemplary layout.
     * </p>
     *
     * @author Kamel.H
     * @see RecyclerView.ViewHolder
     * @see EmojiRecyclerViewAdapter.OnEmojiClickListener
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        //TextView on which we put the emoji each time
        final TextView emojiText;

        /**
         * <p>
         * Constructor in which the views and listener were initialized.
         * </p>
         *
         * @param itemView The layout view
         */
        ViewHolder(@NonNull View itemView) {
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

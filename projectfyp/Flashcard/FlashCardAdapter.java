package com.example.projectfyp.Flashcard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfyp.R;

import java.util.List;
import java.util.function.Consumer;

public class FlashCardAdapter extends RecyclerView.Adapter<FlashCardAdapter.FlashCardViewHolder> {
    private Context context;
    private List<FlashCard> flashCards;
    private Consumer<FlashCard> onDeleteClick;
    private Consumer<FlashCard> onPracticeClick;

    public FlashCardAdapter(Context context, List<FlashCard> flashCards,
                            Consumer<FlashCard> onDeleteClick,
                            Consumer<FlashCard> onPracticeClick) {
        this.context = context;
        this.flashCards = flashCards;
        this.onDeleteClick = onDeleteClick;
        this.onPracticeClick = onPracticeClick;
    }

    @NonNull
    @Override
    public FlashCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flashcard, parent, false);
        return new FlashCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashCardViewHolder holder, int position) {
        FlashCard card = flashCards.get(position);
        holder.bind(card);

        // Make the card square by setting the height equal to the width
        holder.itemView.post(() -> {
            int screenWidth = holder.itemView.getContext().getResources().getDisplayMetrics().widthPixels;
            int cardWidth = (screenWidth / 2) - 32; // Divide by 2 for 2 columns, subtract margins

            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.width = cardWidth;
            params.height = cardWidth;
            holder.itemView.setLayoutParams(params);
        });
    }
    @Override
    public int getItemCount() {
        return flashCards.size();
    }

    class FlashCardViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewQuestion;
        private TextView textViewAnswer;
        private Button buttonPractice;
        private Button buttonDelete;
        private boolean isFlipped = false;

        FlashCardViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewQuestion = itemView.findViewById(R.id.textViewQuestion);
            textViewAnswer = itemView.findViewById(R.id.textViewAnswer);
            buttonPractice = itemView.findViewById(R.id.buttonPractice);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

            // Add click listener to the whole card for flipping
            itemView.setOnClickListener(v -> flipCard());
        }

        void bind(FlashCard card) {
            textViewQuestion.setText(card.getQuestion());
            textViewAnswer.setText(card.getAnswer());

            buttonDelete.setOnClickListener(v -> onDeleteClick.accept(card));
            buttonPractice.setOnClickListener(v -> onPracticeClick.accept(card));
        }

        private void flipCard() {
            isFlipped = !isFlipped;

            float scale = context.getResources().getDisplayMetrics().density;
            itemView.setCameraDistance(8000 * scale);

            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator rotation = ObjectAnimator.ofFloat(itemView, "rotationX",
                    isFlipped ? 0f : 180f, isFlipped ? 180f : 360f);
            rotation.setDuration(300);

            rotation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (isFlipped) {
                        textViewAnswer.setVisibility(View.VISIBLE);
                        textViewQuestion.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!isFlipped) {
                        textViewAnswer.setVisibility(View.GONE);
                        textViewQuestion.setVisibility(View.VISIBLE);
                    }
                    // Reset rotation to prevent issues with recycling
                    itemView.setRotationX(0f);
                }
            });

            animatorSet.play(rotation);
            animatorSet.start();
        }
    }
}
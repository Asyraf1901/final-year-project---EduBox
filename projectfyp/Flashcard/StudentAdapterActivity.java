package com.example.projectfyp.Flashcard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectfyp.R;
import java.util.List;

public class StudentAdapterActivity extends RecyclerView.Adapter<StudentAdapterActivity.FlashCardViewHolder> {
    private List<FlashCard> flashCards;
    private Context context;
    private OnDeleteClickListener deleteListener;
    private OnPracticeClickListener practiceListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(FlashCard card);
    }

    public interface OnPracticeClickListener {
        void onPracticeClick(FlashCard card);
    }

    public StudentAdapterActivity(Context context, List<FlashCard> flashCards,
                                  OnDeleteClickListener deleteListener,
                                  OnPracticeClickListener practiceListener) {
        this.context = context;
        this.flashCards = flashCards;
        this.deleteListener = deleteListener;
        this.practiceListener = practiceListener;
    }

    @NonNull
    @Override
    public FlashCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_student_practise, parent, false);
        return new FlashCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashCardViewHolder holder, int position) {
        FlashCard card = flashCards.get(position);

        // Set progress text
        holder.textViewProgress.setText(String.format("Card %d of %d",
                position + 1, flashCards.size()));

        // Set question and prepare answer field
        holder.textViewQuestion.setText(card.getQuestion());
        holder.editTextUserAnswer.setText("");

        // Reset card state
        holder.cardFront.setVisibility(View.VISIBLE);
        holder.cardBack.setVisibility(View.INVISIBLE);
        holder.buttonSubmit.setVisibility(View.VISIBLE);
        holder.buttonNext.setVisibility(View.GONE);
        holder.textViewResult.setVisibility(View.GONE);
        holder.editTextUserAnswer.setEnabled(true);

        // Setup card flip
        setupCardFlip(holder);

        // Setup button listeners
        holder.buttonSubmit.setOnClickListener(v -> checkAnswer(holder, card.getAnswer()));
        holder.buttonNext.setOnClickListener(v -> {
            if (position < flashCards.size() - 1) {
                notifyItemChanged(position + 1);
            } else {
                notifyItemChanged(0);
            }
        });

        holder.buttonDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(card);
            }
        });

        holder.buttonPractice.setOnClickListener(v -> {
            if (practiceListener != null) {
                practiceListener.onPracticeClick(card);
            }
        });
    }

    private void setupCardFlip(FlashCardViewHolder holder) {
        float scale = context.getResources().getDisplayMetrics().density;
        holder.cardFront.setCameraDistance(8000 * scale);
        holder.cardBack.setCameraDistance(8000 * scale);

        View.OnClickListener flipListener = v -> {
            ObjectAnimator anim1;
            ObjectAnimator anim2;

            if (holder.cardBack.getVisibility() == View.INVISIBLE) {
                // Flip to back
                anim1 = ObjectAnimator.ofFloat(holder.cardFront, "rotationY", 0f, 180f);
                anim2 = ObjectAnimator.ofFloat(holder.cardBack, "rotationY", -180f, 0f);
                holder.cardBack.setVisibility(View.VISIBLE);
            } else {
                // Flip to front
                anim1 = ObjectAnimator.ofFloat(holder.cardBack, "rotationY", 0f, 180f);
                anim2 = ObjectAnimator.ofFloat(holder.cardFront, "rotationY", -180f, 0f);
            }

            AnimatorSet animSet = new AnimatorSet();
            animSet.playTogether(anim1, anim2);
            animSet.setDuration(300);

            animSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (holder.cardBack.getVisibility() == View.VISIBLE &&
                            holder.cardBack.getRotationY() >= 90) {
                        holder.cardBack.setVisibility(View.INVISIBLE);
                    }
                }
            });

            animSet.start();
        };

        holder.cardFront.setOnClickListener(flipListener);
        holder.cardBack.setOnClickListener(flipListener);
    }

    private void checkAnswer(FlashCardViewHolder holder, String correctAnswer) {
        String userAnswer = holder.editTextUserAnswer.getText().toString().trim();

        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
            holder.textViewResult.setTextColor(Color.GREEN);
            holder.textViewResult.setText("Correct! 🎉");
        } else {
            holder.textViewResult.setTextColor(Color.RED);
            holder.textViewResult.setText("Incorrect. The answer is: " + correctAnswer);
        }

        holder.textViewResult.setVisibility(View.VISIBLE);
        holder.buttonSubmit.setVisibility(View.GONE);
        holder.buttonNext.setVisibility(View.VISIBLE);
        holder.editTextUserAnswer.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return flashCards.size();
    }

    public static class FlashCardViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProgress;
        TextView textViewQuestion;
        TextView textViewResult;
        EditText editTextUserAnswer;
        Button buttonSubmit;
        Button buttonNext;
        Button buttonDelete;
        Button buttonPractice;
        CardView cardFront;
        CardView cardBack;

        public FlashCardViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProgress = itemView.findViewById(R.id.textViewProgress);
            textViewQuestion = itemView.findViewById(R.id.textViewQuestion);
            textViewResult = itemView.findViewById(R.id.textViewResult);
            editTextUserAnswer = itemView.findViewById(R.id.editTextUserAnswer);
            buttonSubmit = itemView.findViewById(R.id.buttonSubmit);
            buttonNext = itemView.findViewById(R.id.buttonNext);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonPractice = itemView.findViewById(R.id.buttonPractice);
            cardFront = itemView.findViewById(R.id.cardFront);
            cardBack = itemView.findViewById(R.id.cardBack);
        }
    }
}
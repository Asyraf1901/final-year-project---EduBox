package com.example.projectfyp.Activities;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfyp.Models.QuestionModel;
import com.example.projectfyp.R;
import com.example.projectfyp.databinding.ActivityQuestion2Binding;
import com.example.projectfyp.databinding.ActivityQuestionBinding;

import java.util.ArrayList;

public class QuestionActivity2 extends AppCompatActivity {

    ArrayList<QuestionModel> list = new ArrayList<>();
    private int count = 0;
    private int position = 0;
    private int score = 0;
    CountDownTimer timer;

    ActivityQuestion2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestion2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar(); // Pastikan anda menggunakan AppCompatActivity
        if (actionBar != null) {
            actionBar.hide();
        } else {
            Log.e("QuestionActivity", "ActionBar is null");
        }

        restartTimer();
        timer.start();

        String setName = getIntent().getStringExtra("set");

        if (setName != null) {
            if (setName.equals("SET-1")) {
                setOne();
            } else if (setName.equals("SET-2")) {
                setTwo();
            } else if (setName.equals("SET-3")) {
                setThree();
            } else if (setName.equals("SET-4")) {
                setFour();
            } else if (setName.equals("SET-5")) {
                setFive();
            }
        }



        for (int i = 0; i < 4; i++) {
            binding.optionContainer.getChildAt(i).setOnClickListener(view -> checkAnswer((Button) view));
        }

        // Menggantikan `getQuestion()` dengan `get(position).getQuestion()`
        playAnimation(binding.question, 0, list.get(position).getQuestion());

        binding.btnNext.setOnClickListener(view -> {

            if (timer != null) {
                timer.cancel();
            }

            binding.btnNext.setEnabled(false);
            binding.btnNext.setAlpha(0.3f);
            enableOptions(true);
            position++;

            if (position < list.size()) {
                count = 0;
                playAnimation(binding.question, 0, list.get(position).getQuestion());
                restartTimer();  // Mulakan semula timer untuk soalan seterusnya
                timer.start();   // Mulakan timer baru
            } else {
                Intent intent = new Intent(QuestionActivity2.this, ScoreActivity.class);
                intent.putExtra("score", score);
                intent.putExtra("total", list.size());
                startActivity(intent);
                finish();
            }
        });
    }

    private void restartTimer() {

        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {
                binding.timer.setText(String.valueOf(l / 1000));
            }

            @Override
            public void onFinish() {
                Dialog dialog = new Dialog(QuestionActivity2.this);  // Dibaiki dialog initialization
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.timeout_dialog);
                dialog.findViewById(R.id.tryAgain).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {  // Dibaiki nama kaedah onClick
                        Intent intent = new Intent(QuestionActivity2.this, SetsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                dialog.show();

            }
        };
    }

    private void playAnimation(View view, int value, String data) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {
                        if (value == 0 && count < 4) {
                            String option = "";
                            switch (count) {
                                case 0:
                                    option = list.get(position).getOptionA();
                                    break;
                                case 1:
                                    option = list.get(position).getOptionB();
                                    break;
                                case 2:
                                    option = list.get(position).getOptionC();
                                    break;
                                case 3:
                                    option = list.get(position).getOptionD();
                                    break;
                            }
                            playAnimation(binding.optionContainer.getChildAt(count), 0, option);
                            count++;
                        }
                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        try {
                            if (value == 0) {
                                ((TextView) view).setText(data);
                                binding.totalQuestion.setText((position + 1) + "/" + list.size());
                            } else {
                                ((Button) view).setText(data);
                            }
                            view.setTag(data);
                            playAnimation(view, 1, data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animator) {}

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animator) {}
                });
    }

    private void enableOptions(boolean enabled) {

        for (int i = 0; i < 4 ; i++) {

            binding.optionContainer.getChildAt(i).setEnabled(enabled);

            if (enabled) {

                binding.optionContainer.getChildAt(i).setBackgroundResource(R.drawable.btn_opt);
            }
        }
    }

    private void checkAnswer(Button selectedOption) {

        if (timer !=null){
            timer.cancel();
        }

        binding.btnNext.setEnabled(true);
        binding.btnNext.setAlpha(1);

        if(selectedOption.getText().toString().equals(list.get(position).getCorrectAnswer())){

            score++;
            selectedOption.setBackgroundResource(R.drawable.right_answer);
        }else {

            selectedOption.setBackgroundResource(R.drawable.btn_wrong);

            Button correctOption = (Button)binding.optionContainer.findViewWithTag(list.get(position).getCorrectAnswer());
            correctOption.setBackgroundResource(R.drawable.right_answer);
        }
    }

    private void setTwo() {

        list.add(new QuestionModel("6.Which of the following is true about a stack?",
                "A. FIFO (First In First Out)",
                "B. LIFO (Last In First Out)",
                "C. Random access",
                "D. None of the above",
                "B. LIFO (Last In First Out)"));

        list.add(new QuestionModel("7.What is the worst-case time complexity for searching in a binary search tree (BST)?",
                "A. O(1)",
                "B. O(n)",
                "C. O(log n)",
                "D. O(n log n)",
                "B. O(n)"));

        list.add(new QuestionModel("8.Which of the following operations are performed more efficiently by a doubly linked list than a singly linked list?",
                "A. Inserting a node after a given node",
                "B. Deleting a given node",
                "C. Searching for a node",
                "D. Traversing the list",
                "B. Deleting a given node"));

        list.add(new QuestionModel("9.Which data structure can be used to convert infix notation to postfix notation?",
                "A. Queue",
                "B. Stack",
                "C. Tree",
                "D. Linked List",
                "B. Stack"));

        list.add(new QuestionModel("10.In which tree structure does every internal node have exactly two children, and all leaves are at the same level?",
                "A. AVL tree",
                "B. Binary Search Tree",
                "C. Complete Binary Tree",
                "D. Full Binary Tree",
                "D. Full Binary Tree"));

    }

    private void setOne() {


        list.add(new QuestionModel("1.Which data structure allows inserting and deleting elements from both ends but not in the middle?",
                "A. Stack",
                "B. Queue",
                "C. Deque",
                "D. Linked List",
                "C. Deque"));

        list.add(new QuestionModel("2.What is the time complexity of accessing an element in an array?",
                "A. O(1)",
                "B. O(n)",
                "C. O(log n)",
                "D. O(n log n)",
                "A. O(1)"));

        list.add(new QuestionModel("3.Which data structure is used in a breadth-first search (BFS) algorithm?",
                "A. Stack",
                "B. Queue",
                "C. Linked List",
                "D. Tree",
                "B. Queue"));

        list.add(new QuestionModel("4.In a binary search tree (BST), for every node, the left subtree contains elements that are:",
                "A. Greater than the node",
                "B. Smaller than the node",
                "C. Equal to the node",
                "D. Unrelated to the node",
                "B. Smaller than the node"));

        list.add(new QuestionModel("5.Which of the following data structures is not linear?",
                "A. Stack",
                "B. Queue",
                "C. Linked List",
                "D. Tree",
                "D. Tree"));


    }

    private void setThree() {


        list.add(new QuestionModel("11.Which of the following data structures uses pointers to connect nodes?",
                "A. Stack",
                "B. Array",
                "C. Linked List",
                "D. Queue",
                "C. Linked List"));

        list.add(new QuestionModel("12.What is the best data structure to check for balanced parentheses in an expression?",
                "A. Array",
                "B. Stack",
                "C. Queue",
                "D. Linked List",
                "B. Stack"));

        list.add(new QuestionModel("13.In a min-heap, the root node is always:",
                "A. The smallest element",
                "B. The largest element",
                "C. The middle element",
                "D. None of the above",
                "A. The smallest element"));

        list.add(new QuestionModel("14.What is the maximum number of children a binary tree node can have?",
                "A. 1",
                "B. 2",
                "C. 3",
                "D. 4",
                "B. 2"));

        list.add(new QuestionModel("15.Which traversal method visits the root node first, then the left subtree, and finally the right subtree?",
                "A. In-order",
                "B. Pre-order",
                "C. Post-order",
                "D. Level-order",
                "B. Pre-order"));


    }

    private void setFour() {


        list.add(new QuestionModel("16.A circular queue is also known as:",
                "A. Ring buffer",
                "B. Stack",
                "C. Deque",
                "D. Priority queue",
                "A. Ring buffer"));

        list.add(new QuestionModel("17.What is the height of a binary tree with only one node?",
                "A. 0",
                "B. 1",
                "C. 2",
                "D. -1",
                "A. 0"));

        list.add(new QuestionModel("18.Which of the following algorithms is used to sort elements in a heap?",
                "A. Quick sort",
                "B. Merge sort",
                "C. Heap sort",
                "D. Bubble sort",
                "C. Heap sort"));

        list.add(new QuestionModel("19.In a hash table, collisions can be resolved by:",
                "A. Stacking",
                "B. Open addressing",
                "C. Tree traversal",
                "D. Recursion",
                "B. Open addressing"));

        list.add(new QuestionModel("20.Which of the following is the fastest algorithm for finding the shortest path in a graph?",
                "A. Dijkstra’s algorithm",
                "B. Kruskal’s algorithm",
                "C. Prim’s algorithm",
                "D. Depth-first search",
                "A. Dijkstra’s algorithm"));


    }

    private void setFive() {


        list.add(new QuestionModel("21.In a queue, elements are removed from:",
                "A. The front",
                "B. The back",
                "C. Random positions",
                "D. Both ends",
                "A. The front"));

        list.add(new QuestionModel("22.Which data structure is ideal for implementing a priority queue?",
                "A. Stack",
                "B. Heap",
                "C. Linked List",
                "D. Array",
                "B. Heap"));

        list.add(new QuestionModel("23.What is the worst-case time complexity for searching in a balanced binary search tree (BST)?",
                "A. O(n)",
                "B. O(log n)",
                "C. O(n log n)",
                "D. O(1)",
                "B. O(log n)"));

        list.add(new QuestionModel("24.What is the time complexity of merging two sorted linked lists?",
                "A. O(1)",
                "B. O(n)",
                "C. O(log n)",
                "D. O(n log n)",
                "B. O(n)"));

        list.add(new QuestionModel("25.Which sorting algorithm repeatedly selects the minimum element from the unsorted part of the list and places it at the beginning?",
                "A. Quick sort",
                "B. Merge sort",
                "C. Insertion sort",
                "D. Selection sort",
                "D. Selection sort"));


    }
}
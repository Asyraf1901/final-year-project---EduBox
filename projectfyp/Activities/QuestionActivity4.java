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
import com.example.projectfyp.databinding.ActivityQuestion4Binding;
import com.example.projectfyp.databinding.ActivityQuestionBinding;

import java.util.ArrayList;

public class QuestionActivity4 extends AppCompatActivity {

    ArrayList<QuestionModel> list = new ArrayList<>();
    private int count = 0;
    private int position = 0;
    private int score = 0;
    CountDownTimer timer;

    ActivityQuestion4Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestion4Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar(); // Pastikan anda menggunakan AppCompatActivity
        if (actionBar != null) {
            actionBar.hide();
        } else {
            Log.e("QuestionActivity4", "ActionBar is null");
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
                Intent intent = new Intent(QuestionActivity4.this, ScoreActivity.class);
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
                Dialog dialog = new Dialog(QuestionActivity4.this);  // Dibaiki dialog initialization
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.timeout_dialog);
                dialog.findViewById(R.id.tryAgain).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {  // Dibaiki nama kaedah onClick
                        Intent intent = new Intent(QuestionActivity4.this, SetsActivity.class);
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

        list.add(new QuestionModel("6. Which SQL statement is used to retrieve data from a database?",
                "A. INSERT",
                "B. UPDATE",
                "C. SELECT",
                "D. DELETE",
                "C. SELECT"));

        list.add(new QuestionModel("7. What is the function of a foreign key?",
                "A. A key used to encrypt data",
                "B. A key linking two tables together",
                "C. A key that uniquely identifies each record",
                "D. A key that duplicates data",
                "B. A key linking two tables together"));

        list.add(new QuestionModel("8. What is an index used for in a database?",
                "A. To improve the speed of data retrieval",
                "B. To increase redundancy",
                "C. To secure sensitive data",
                "D. To store backups",
                "A. To improve the speed of data retrieval"));

        list.add(new QuestionModel("9. Which ACID property ensures that transactions are completed fully or not at all?",
                "A. Atomicity",
                "B. Consistency",
                "C. Isolation",
                "D. Durability",
                "A. Atomicity"));

        list.add(new QuestionModel("10. Which type of database backup captures only the data that has changed since the last full backup?",
                "A. Full backup",
                "B. Differential backup",
                "C. Incremental backup",
                "D. Hot backup",
                "B. Differential backup"));

    }

    private void setOne() {


        list.add(new QuestionModel("1. What is the primary key in a database?",
                "A. A unique identifier for each record",
                "B. A password to access the database",
                "C. A duplicate field in a table",
                "D. A secondary key",
                "A. A unique identifier for each record"));

        list.add(new QuestionModel("2. What is the function of the 'JOIN' operation in SQL?",
                "A. Combines two or more tables based on a related column",
                "B. Deletes duplicate rows from a table",
                "C. Modifies existing records in a table",
                "D. Inserts new records into a table",
                "A. Combines two or more tables based on a related column"));

        list.add(new QuestionModel("3. What does SQL stand for?",
                "A. Structured Query Language",
                "B. Simple Query Language",
                "C. Standard Query Language",
                "D. Sequential Query Language",
                "A. Structured Query Language"));

        list.add(new QuestionModel("4. Which of the following is a NoSQL database?",
                "A. Oracle",
                "B. MySQL",
                "C. MongoDB",
                "D. PostgreSQL",
                "C. MongoDB"));

        list.add(new QuestionModel("5. What does normalization in a database achieve?",
                "A. Reduces data redundancy",
                "B. Increases data redundancy",
                "C. Enhances data security",
                "D. Speeds up data retrieval",
                "A. Reduces data redundancy"));



    }

    private void setThree() {


        list.add(new QuestionModel("11. What is the main feature of a relational database?",
                "A. Stores data in hierarchical format",
                "B. Stores data in tables",
                "C. Uses object-oriented programming",
                "D. Organizes data with graphs",
                "B. Stores data in tables"));

        list.add(new QuestionModel("12. Which of the following is used to ensure data integrity in a database?",
                "A. Data Redundancy",
                "B. Normalization",
                "C. Aggregation",
                "D. Replication",
                "B. Normalization"));

        list.add(new QuestionModel("13. Which command is used to create a new table in SQL?",
                "A. CREATE DATABASE",
                "B. CREATE TABLE",
                "C. ADD COLUMN",
                "D. INSERT INTO",
                "B. CREATE TABLE"));

        list.add(new QuestionModel("14. What is the purpose of the 'WHERE' clause in SQL?",
                "A. To filter records based on a condition",
                "B. To sort data in ascending order",
                "C. To group data",
                "D. To delete records",
                "A. To filter records based on a condition"));

        list.add(new QuestionModel("15. Which SQL function returns the total number of records?",
                "A. COUNT()",
                "B. SUM()",
                "C. AVG()",
                "D. MAX()",
                "A. COUNT()"));


    }

    private void setFour() {


        list.add(new QuestionModel("16. What is the role of a database schema?",
                "A. To store data",
                "B. To define the structure of a database",
                "C. To improve query performance",
                "D. To handle database backups",
                "B. To define the structure of a database"));

        list.add(new QuestionModel("17. What is a trigger in a database?",
                "A. A special kind of index",
                "B. A procedure that automatically executes in response to a database event",
                "C. A key used for encryption",
                "D. A method to normalize data",
                "B. A procedure that automatically executes in response to a database event"));

        list.add(new QuestionModel("18. What does the term 'denormalization' refer to?",
                "A. The process of creating redundant data",
                "B. The process of breaking tables into smaller parts",
                "C. The process of removing redundant data",
                "D. The process of encrypting data",
                "A. The process of creating redundant data"));

        list.add(new QuestionModel("19. What is a composite key in a database?",
                "A. A key that consists of more than one attribute",
                "B. A key used to enforce foreign key constraints",
                "C. A key that encrypts the database",
                "D. A key used to identify duplicate records",
                "A. A key that consists of more than one attribute"));

        list.add(new QuestionModel("20. Which SQL statement is used to update existing data?",
                "A. INSERT",
                "B. UPDATE",
                "C. SELECT",
                "D. DELETE",
                "B. UPDATE"));


    }

    private void setFive() {


        list.add(new QuestionModel("21.Which ACID property ensures that a transaction is completed fully or not at all?",
                "A. Atomicity",
                "B. Consistency",
                "C. Isolation",
                "D. Durability",
                "A. Atomicity"));

        list.add(new QuestionModel("22.Which type of database backup captures only the data that has changed since the last full backup?",
                "A. Full backup",
                "B. Differential backup",
                "C. Incremental backup",
                "D. Hot backup",
                "B. Differential backup"));

        list.add(new QuestionModel("23.Which of the following SQL commands is used to remove a table from the database?",
                "A. DELETE",
                "B. DROP",
                "C. REMOVE",
                "D. TRUNCATE",
                "B. DROP"));

        list.add(new QuestionModel("24.What is the role of an index in a database?",
                "A. To increase redundancy",
                "B. To improve the speed of data retrieval",
                "C. To secure sensitive data",
                "D. To normalize the data",
                "B. To improve the speed of data retrieval"));

        list.add(new QuestionModel("25.Which type of SQL statement is used to modify an existing record in a table?",
                "A. INSERT",
                "B. DELETE",
                "C. UPDATE",
                "D. ALTER",
                "C. UPDATE"));


    }
}
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
import com.example.projectfyp.databinding.ActivityQuestion3Binding;
import com.example.projectfyp.databinding.ActivityQuestion4Binding;
import com.example.projectfyp.databinding.ActivityQuestionBinding;

import java.util.ArrayList;

public class QuestionActivity3 extends AppCompatActivity {

    ArrayList<QuestionModel> list = new ArrayList<>();
    private int count = 0;
    private int position = 0;
    private int score = 0;
    CountDownTimer timer;

    ActivityQuestion3Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestion3Binding.inflate(getLayoutInflater());
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
                Intent intent = new Intent(QuestionActivity3.this, ScoreActivity.class);
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
                Dialog dialog = new Dialog(QuestionActivity3.this);  // Dibaiki dialog initialization
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.timeout_dialog);
                dialog.findViewById(R.id.tryAgain).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {  // Dibaiki nama kaedah onClick
                        Intent intent = new Intent(QuestionActivity3.this, SetsActivity.class);
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

        list.add(new QuestionModel("6.What does a system call allow a user program to do?",
                "A. Directly access the hardware",
                "B. Request a service from the operating system",
                "C. Terminate the operating system",
                "D. Create new hardware components",
                "B. Request a service from the operating system"));

        list.add(new QuestionModel("7.In which memory allocation method does each process get a fixed-sized partition of memory?",
                "A. Paging",
                "B. Segmentation",
                "C. Fixed partitioning",
                "D. Dynamic partitioning",
                "C. Fixed partitioning"));

        list.add(new QuestionModel("8.What is the term for temporarily transferring a program from RAM to disk storage to free up space?",
                "A. Paging",
                "B. Swapping",
                "C. Spooling",
                "D. Thrashing",
                "B. Swapping"));

        list.add(new QuestionModel("9.Which of the following memory management schemes does not suffer from external fragmentation?",
                "A. Fixed partitioning",
                "B. Segmentation",
                "C. Paging",
                "D. Dynamic partitioning",
                "C. Paging"));

        list.add(new QuestionModel("10.What is the main advantage of a multi-threaded process?",
                "A. Faster file access",
                "B. Simultaneous execution of multiple parts of a program",
                "C. Higher memory usage",
                "D. More complicated code",
                "B. Simultaneous execution of multiple parts of a program"));

    }

    private void setOne() {


        list.add(new QuestionModel("1.Which of the following is an example of a real-time operating system?",
                "A. Windows 10",
                "B. Linux",
                "C. RTOS",
                "D. macOS",
                "C. RTOS"));

        list.add(new QuestionModel("2.What is the main function of an operating system?",
                "A. Managing hardware resources",
                "B. Performing mathematical calculations",
                "C. Compiling programs",
                "D. Creating webpages",
                "A. Managing hardware resources"));

        list.add(new QuestionModel("3.In which of the following scheduling algorithms does the operating system allocate the CPU to the process with the shortest burst time first?",
                "A. Round-robin",
                "B. Priority scheduling",
                "C. Shortest job next (SJN)",
                "D. First-come, first-served",
                "C. Shortest job next (SJN)"));

        list.add(new QuestionModel("4.Which of the following is a non-preemptive scheduling algorithm?",
                "A. Round-robin",
                "B. First-come, first-served",
                "C. Shortest remaining time first",
                "D. Priority scheduling",
                "B. First-come, first-served"));

        list.add(new QuestionModel("5.Which of the following is NOT a function of the kernel in an operating system?",
                "A. Memory management",
                "B. Process management",
                "C. File management",
                "D. User interface management",
                "D. User interface management"));



    }

    private void setThree() {


        list.add(new QuestionModel("11.What is the main difference between a process and a thread?",
                "A. A process is part of a thread",
                "B. A thread is part of a process",
                "C. Threads are independent of processes",
                "D. Processes execute faster than threads",
                "B. A thread is part of a process"));

        list.add(new QuestionModel("12.In virtual memory systems, what is the term for the location where data is temporarily stored while it is not being used by the CPU?",
                "A. Swap space",
                "B. Cache",
                "C. Main memory",
                "D. Registers",
                "A. Swap space"));

        list.add(new QuestionModel("13.Which of the following is used for inter-process communication (IPC)?",
                "A. Mutex",
                "B. Semaphore",
                "C. Shared memory",
                "D. All of the above",
                "D. All of the above"));

        list.add(new QuestionModel("14.Which of the following is NOT a type of operating system?",
                "A. Batch operating system",
                "B. Time-sharing operating system",
                "C. Embedded operating system",
                "D. Programming operating system",
                "D. Programming operating system"));

        list.add(new QuestionModel("15.What is the term for when two or more processes are unable to proceed because each is waiting for the other to release a resource?",
                "A. Starvation",
                "B. Deadlock",
                "C. Mutual exclusion",
                "D. Blocking",
                "B. Deadlock"));


    }

    private void setFour() {


        list.add(new QuestionModel("16.Which of the following is an example of deadlock prevention?",
                "A. Allow circular wait",
                "B. Hold and wait",
                "C. Disable interrupts",
                "D. Resource allocation using priorities",
                "D. Resource allocation using priorities"));

        list.add(new QuestionModel("17.Which of the following is the correct sequence in the booting process?",
                "A. Loading OS, POST, BIOS",
                "B. POST, BIOS, Loading OS",
                "C. BIOS, POST, Loading OS",
                "D. POST, Loading OS, BIOS",
                "C. BIOS, POST, Loading OS"));

        list.add(new QuestionModel("18.Which algorithm is used for disk scheduling where the disk arm moves back and forth, servicing requests?",
                "A. FCFS",
                "B. SSTF",
                "C. SCAN",
                "D. C-SCAN",
                "C. SCAN"));

        list.add(new QuestionModel("19.What is a 'context switch' in an operating system?",
                "A. Switching between user programs",
                "B. Switching the operating system to a new version",
                "C. Saving and loading the state of a process",
                "D. Switching between different hardware devices",
                "C. Saving and loading the state of a process"));

        list.add(new QuestionModel("20.Which of the following is an example of a distributed operating system?",
                "A. Windows XP",
                "B. MS-DOS",
                "C. Unix",
                "D. Amoeba",
                "D. Amoeba"));


    }

    private void setFive() {


        list.add(new QuestionModel("21.Which of the following algorithms minimizes the number of page faults?",
                "A. FIFO",
                "B. LRU",
                "C. Round-robin",
                "D. SSTF",
                "B. LRU"));

        list.add(new QuestionModel("22.Which of the following is a characteristic of a microkernel operating system?",
                "A. Large size",
                "B. Monolithic structure",
                "C. Minimal core functions with services in user space",
                "D. Only real-time applications",
                "C. Minimal core functions with services in user space"));

        list.add(new QuestionModel("23.What is the maximum number of partitions that can exist in a process's address space in a segmented memory system?",
                "A. 1",
                "B. 2",
                "C. 4",
                "D. There is no fixed limit",
                "D. There is no fixed limit"));

        list.add(new QuestionModel("24.Which system utility provides information about the programs and services running on a computer?",
                "A. Task Scheduler",
                "B. Task Manager",
                "C. Disk Management",
                "D. System Monitor",
                "B. Task Manager"));

        list.add(new QuestionModel("25.Which of the following types of operating systems is designed to be used by a single user at a time?",
                "A. Time-sharing",
                "B. Multi-user",
                "C. Single-user",
                "D. Network operating system",
                "C. Single-user"));


    }
}
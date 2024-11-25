package com.example.projectfyp.Activities;

import android.animation.Animator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
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
import com.example.projectfyp.databinding.ActivityQuestionBinding;

import java.util.ArrayList;
import java.util.Optional;

public class QuestionActivity extends AppCompatActivity {

    ArrayList<QuestionModel> list = new ArrayList<>();
    private int count = 0;
    private int position = 0;
    private int score = 0;
    CountDownTimer timer;

    ActivityQuestionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
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
                Intent intent = new Intent(QuestionActivity.this, ScoreActivity.class);
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
                Dialog dialog = new Dialog(QuestionActivity.this);  // Dibaiki dialog initialization
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.timeout_dialog);
                dialog.findViewById(R.id.tryAgain).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {  // Dibaiki nama kaedah onClick
                        Intent intent = new Intent(QuestionActivity.this, SetsActivity.class);
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

        list.add(new QuestionModel("6. In computer systems, what does the 'bus' refer to?",
                "A. A set of connections for data transfer",
                "B. A processing unit",
                "C. A storage device",
                "D. An input device",
                "A. A set of connections for data transfer"));

        list.add(new QuestionModel("7. Which of the following describes Von Neumann architecture?",
                "A. Separate memory for data and instructions",
                "B. Single memory for both data and instructions",
                "C. No memory required",
                "D. Distributed memory",
                "B. Single memory for both data and instructions"));

        list.add(new QuestionModel("8. What is cache memory?",
                "A. Slow memory for storage",
                "B. Fast memory for frequently accessed data",
                "C. Non-volatile storage",
                "D. Main memory for CPU",
                "B. Fast memory for frequently accessed data"));

        list.add(new QuestionModel("9. What is a characteristic of CISC architecture?",
                "A. Simple instruction set",
                "B. Large number of instructions",
                "C. Few addressing modes",
                "D. Limited use of microprogramming",
                "B. Large number of instructions"));

        list.add(new QuestionModel("10. What is the function of the program counter (PC) in a CPU?",
                "A. Store data",
                "B. Control data flow",
                "C. Hold the address of the next instruction",
                "D. Perform arithmetic operations",
                "C. Hold the address of the next instruction"));

    }

    private void setOne() {


        list.add(new QuestionModel("1. What is the main purpose of the ALU (Arithmetic Logic Unit)?",
                "A. Store data",
                "B. Perform arithmetic and logical operations",
                "C. Control data flow",
                "D. Manage memory",
                "B. Perform arithmetic and logical operations"));

        list.add(new QuestionModel("2. Which memory is volatile and loses data when power is turned off?",
                "A. ROM",
                "B. RAM",
                "C. Cache",
                "D. Hard Disk",
                "B. RAM"));

        list.add(new QuestionModel("3. Which component of the CPU fetches and decodes instructions?",
                "A. ALU",
                "B. Control Unit",
                "C. Memory",
                "D. Register",
                "B. Control Unit"));

        list.add(new QuestionModel("4. What is the primary benefit of pipelining in CPU design?",
                "A. Increases clock speed",
                "B. Allows parallel execution of instructions",
                "C. Reduces the CPU size",
                "D. Simplifies instruction decoding",
                "B. Allows parallel execution of instructions"));

        list.add(new QuestionModel("5. Which of the following is NOT a feature of RISC architecture?",
                "A. Simple instruction set",
                "B. Fixed instruction length",
                "C. Complex addressing modes",
                "D. Pipeline processing",
                "C. Complex addressing modes"));



    }

    private void setThree() {


        list.add(new QuestionModel("11. Which type of memory is the fastest in access speed?",
                "A. RAM",
                "B. Cache",
                "C. Hard Disk",
                "D. Register",
                "D. Register"));

        list.add(new QuestionModel("12. What is the full form of DMA?",
                "A. Direct Memory Access",
                "B. Disk Management Algorithm",
                "C. Data Memory Allocation",
                "D. Device Management Access",
                "A. Direct Memory Access"));

        list.add(new QuestionModel("13. What is the function of virtual memory?",
                "A. Provides temporary storage",
                "B. Increases CPU speed",
                "C. Extends the apparent size of RAM",
                "D. Stores permanent data",
                "C. Extends the apparent size of RAM"));

        list.add(new QuestionModel("14. Which type of memory is non-volatile?",
                "A. RAM",
                "B. Cache",
                "C. ROM",
                "D. Register",
                "C. ROM"));

        list.add(new QuestionModel("15. What does pipelining achieve in CPU architecture?",
                "A. Divides instructions to speed up execution",
                "B. Stores data in multiple registers",
                "C. Executes two instructions simultaneously",
                "D. Increases memory bandwidth",
                "A. Divides instructions to speed up execution"));


    }

    private void setFour() {


        list.add(new QuestionModel("16. What is the role of the Memory Management Unit (MMU)?",
                "A. Perform arithmetic operations",
                "B. Control I/O devices",
                "C. Translate logical to physical addresses",
                "D. Manage data flow",
                "C. Translate logical to physical addresses"));

        list.add(new QuestionModel("17. What is an instruction set?",
                "A. A collection of software routines",
                "B. A list of instructions a processor can execute",
                "C. A type of memory",
                "D. A set of hardware connections",
                "B. A list of instructions a processor can execute"));

        list.add(new QuestionModel("18. What is the purpose of cache memory?",
                "A. To store permanent data",
                "B. To speed up frequently accessed instructions",
                "C. To reduce CPU clock speed",
                "D. To manage data flow",
                "B. To speed up frequently accessed instructions"));

        list.add(new QuestionModel("19. What is the role of the control unit in a CPU?",
                "A. Store data",
                "B. Manage memory",
                "C. Decode instructions and manage data flow",
                "D. Perform logical operations",
                "C. Decode instructions and manage data flow"));

        list.add(new QuestionModel("20. What type of memory stores BIOS settings?",
                "A. RAM",
                "B. ROM",
                "C. CMOS",
                "D. Cache",
                "C. CMOS"));


    }

    private void setFive() {


        list.add(new QuestionModel("21. Which addressing mode specifies the operand directly in the instruction?",
                "A. Direct addressing",
                "B. Immediate addressing",
                "C. Indirect addressing",
                "D. Indexed addressing",
                "B. Immediate addressing"));

        list.add(new QuestionModel("22. What does the data bus do?",
                "A. Carry control signals",
                "B. Transfer data between CPU and memory",
                "C. Connect external devices",
                "D. Store data permanently",
                "B. Transfer data between CPU and memory"));

        list.add(new QuestionModel("23. What does 'clock cycle' in a CPU refer to?",
                "A. Time taken to complete an instruction",
                "B. Time between two pulses of the oscillator",
                "C. Speed of data transfer",
                "D. Time taken to access memory",
                "B. Time between two pulses of the oscillator"));

        list.add(new QuestionModel("24. What is the function of the control bus?",
                "A. Control CPU speed",
                "B. Manage data transfer",
                "C. Carry control signals from CPU to other components",
                "D. Store control instructions",
                "C. Carry control signals from CPU to other components"));

        list.add(new QuestionModel("25. What is the advantage of a superscalar processor?",
                "A. Executes more than one instruction per clock cycle",
                "B. Has more memory",
                "C. Easier to design",
                "D. Uses less power",
                "A. Executes more than one instruction per clock cycle"));


    }
}
package com.example.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView countLabel;
    private TextView questionLabel;
    private Button answerBtn1;
    private Button answerBtn2;
    private Button answerBtn3;
    private Button answerBtn4;

    private String rightAnswer;
    private int rightAnswerCount = 0;
    private int quizCount = 1;
    static final private int QUIZ_COUNT = 5;

    ArrayList<ArrayList<String>> quizArray = new ArrayList<>();

    String quizData[][] = {
            // {"Country", "Right Answer", "Choice 1", "Choice 2", "Choise 3"}
            {"Vrolijkheid", "Goed", "Slecht", "Gaat wel", "Heel vrolijk"},
            {"Humeur", "Goed", "Slecht", "Gaat wel", "Heel slecht"},
            {"Energie", "Veel", "Weinig", "Gaat wel", "Heel weinig"},
            {"Positiviteit", "Vaak", "Weinig", "Gaat wel", "Heel soms"},
            {"Sociaal", "Vaak", "Weinig", "Gaat wel", "Heel soms"}
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countLabel = (TextView)findViewById(R.id.countLabel);
        questionLabel = (TextView)findViewById(R.id.questionLabel);
        answerBtn1 = (Button)findViewById(R.id.answerBtn1);
        answerBtn2 = (Button)findViewById(R.id.answerBtn2);
        answerBtn3 = (Button)findViewById(R.id.answerBtn3);
        answerBtn4 = (Button)findViewById(R.id.answerBtn4);

        //Receive quizCategory from StartActivity
        int quizCategory = getIntent().getIntExtra("QUIZ_CATEGORY", 0);
        Log.v("CATEGORY_TAG", quizCategory + "");

        // Create quizArray from quizData
        for (int i = 0; i < quizData.length; i++) {
            //Prepare array.
            ArrayList<String> tmpArray = new ArrayList<>();
            tmpArray.add(quizData[i][0]); //Onderwerp
            tmpArray.add(quizData[i][1]); //Juiste antwoord?
            tmpArray.add(quizData[i][2]); //A1
            tmpArray.add(quizData[i][3]); //A2
            tmpArray.add(quizData[i][4]); //A3

            //Add tmpArray to quizArray
            quizArray.add(tmpArray);
        }

        showNextQuestion();
    }

    public void showNextQuestion(){
        //Update quizCountLabel
        countLabel.setText("V" + quizCount);

        //Generate random number between 0 - (quizArray's size -1)
        Random random = new Random();
        int randomNum = random.nextInt(quizArray.size());

        //Pick quiz set
        ArrayList<String> quiz = quizArray.get(randomNum);

        //Set question and right answer.
        //Array format: {"Country", "Right Answer", "Choice 1", "Choice 2", "Choise 3"}

        questionLabel.setText(quiz.get(0));
        rightAnswer = quiz.get(1);

        //Remove quiz and shuffle choices
        quiz.remove(0);
        Collections.shuffle(quiz);

        //Set Choices.
        answerBtn1.setText(quiz.get(0));
        answerBtn2.setText(quiz.get(1));
        answerBtn3.setText(quiz.get(2));
        answerBtn4.setText(quiz.get(3));

        //Remove this quiz from quizArray
        quizArray.remove(randomNum);
    }

    public void checkAnswer(View view) {
        //Get pushed button
        Button answerBtn = (Button) findViewById(view.getId());
        String btnText = answerBtn.getText().toString();

        String alertTitle;

        if(btnText.equals(rightAnswer)) {
            //Correct
            alertTitle = "Correct!";
            rightAnswerCount++;
        } else {
            //Wrong
            alertTitle = "Wrong!";
        }

        //Create Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(alertTitle);
        builder.setMessage("Answer : " + rightAnswer);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(quizCount == QUIZ_COUNT){
                    //show result
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    intent.putExtra("RIGHT_ANSWER_COUNT", rightAnswerCount);
                    startActivity(intent);

                } else {
                    quizCount++;
                    showNextQuestion();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}

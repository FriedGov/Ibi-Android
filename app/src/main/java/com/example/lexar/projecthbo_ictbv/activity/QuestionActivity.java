package com.example.lexar.projecthbo_ictbv.activity;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lexar.projecthbo_ictbv.R;
import com.example.lexar.projecthbo_ictbv.error.Assert;
import com.example.lexar.projecthbo_ictbv.fragment.MultipleChoiceQuestionFragment;
import com.example.lexar.projecthbo_ictbv.fragment.OpenQuestionFragment;
import com.example.lexar.projecthbo_ictbv.fragment.QuestionFragment;
import com.example.lexar.projecthbo_ictbv.fragment.RatingQuestionFragment;
import com.example.lexar.projecthbo_ictbv.helper.NotificationManager;
import com.example.lexar.projecthbo_ictbv.model.Location;
import com.example.lexar.projecthbo_ictbv.model.Question;
import com.example.lexar.projecthbo_ictbv.helper.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity that show the question to the user.
 */
public class QuestionActivity extends AppCompatActivity
        implements QuestionFragment.QuestionCallbacks,
        RatingQuestionFragment.RatingCallBacks {

    private ArrayList<Question> questions;
    private Location location;
    private int questionIndex;
    @BindView(R.id.tv_QuestionText)
    TextView questionText;

    @BindView(R.id.btn_nextQuestion)
    Button nextQuestion;

    @BindView(R.id.btn_closeQuestion)
    Button close;

    @BindView(R.id.iv_questionImage)
    ImageView questionImage;

    @BindView(R.id.tv_placeValue)
    TextView placeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);

        this.location = getIntent().getParcelableExtra("location");
        this.questions = getIntent().getParcelableArrayListExtra("questions");
        nextQuestion(0);

        placeContainer.setText(location.getName());
        Picasso.with(this).load(location.getImage()).into(questionImage);


        NotificationManager.cancelNotification(this, Constants.NOTIFICATION_ID_QUESTIONS);

        setTranslucentStatusBar(getWindow());
    }


    /**
     * Open the screen with the next question.
     * @param index the index of the question
     */
    private void nextQuestion(int index) {
        Assert.that(index > -1, "index is under 0");
        Assert.that(index < questions.size(), "index is over the array length");
        for (int i = index; i < questions.size(); i++) {
            Question question = questions.get(i);
            Bundle b = new Bundle();
            Fragment fragment;
            
            questionText.setText(question.getDescription());
            b.putInt(Constants.QUESTION_ID_KEY, questions.get(i).getId());
            b.putInt(Constants.LOCATION_ID_KEY, location.getId());
            questionIndex = i;
            if (question.getQuestionType().equals(Constants.QUESTION_TYPE_OPEN)) {
                fragment = new OpenQuestionFragment();
                loadUpFragment(fragment, b);
                break;
            } else if (question.getQuestionType().equals(Constants.QUESTION_TYPE_MULTIPLE_CHOICE)) {
                fragment = new MultipleChoiceQuestionFragment();
                loadUpFragment(fragment, b);
                break;
            } else if (question.getQuestionType().equals(Constants.QUESTION_TYPE_RATING)) {
                fragment = new RatingQuestionFragment();
                loadUpFragment(fragment, b);
                break;
            }
            break;
        }
    }

    /**
     * Show a new fragment.
     * @param fragment the fragment that needs to be shown
     * @param b        the bundle containing arguments
     */
    private void loadUpFragment(Fragment fragment, Bundle b) {
        fragment.setArguments(b);
        getFragmentManager().beginTransaction().addToBackStack(
                Constants.FRAGMENT_TAG).replace(
                        R.id.placeholder, fragment, Constants.FRAGMENT_TAG).commit();
    }

    /**
     * Set the statusbar to a translucent color.
     * @param window the current window
     */
    private static void setTranslucentStatusBar(Window window) {
        if (window == null) {
            return;
        }
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatusBarLollipop(window);
        } else if (sdkInt >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatusBarKitKat(window);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setTranslucentStatusBarLollipop(Window window) {
        window.setStatusBarColor(
                window.getContext()
                        .getResources()
                        .getColor(R.color.statusBar));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void setTranslucentStatusBarKitKat(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    /**
     * Go to the next question.
     */
    @OnClick(R.id.btn_nextQuestion)
    public void next() {
        QuestionFragment fragment =
                (QuestionFragment) getFragmentManager().
                        findFragmentByTag(Constants.FRAGMENT_TAG);
        disableNextButton();
        fragment.addAnswer();
    }

    /**
     * Close the question screen.
     */
    @OnClick(R.id.btn_closeQuestion)
    public void close() {
        finish();
    }

    /**
     * Callback for when a question has been answered.
     */
    @Override
    public void answerCallback() {
        Fragment fragment = getFragmentManager().findFragmentByTag(Constants.FRAGMENT_TAG);
        if (fragment instanceof MultipleChoiceQuestionFragment) {
            Intent intent = new Intent(this, GraphActivity.class);
            Question question = questions.get(questionIndex);
            intent.putExtra(Constants.QUESTION_ID_KEY, question.getId());
            intent.putExtra(Constants.LOCATION_ID_KEY, location.getId());
            intent.putExtra(Constants.QUESTION_TEXT, question.getDescription());
            startActivity(intent);
        }
        questionIndex++;
        if (questionIndex == questions.size()) {
            showEndScreen(fragment);
        } else {
            //Enable the nextButton
            enableNextButton();
            nextQuestion(questionIndex);
        }
    }

    /**
     * Show the screen that tells the user he is done answering the questions.
     *
     * @param fragment the last fragment that was active
     */
    private void showEndScreen(Fragment fragment) {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.remove(fragment);
        trans.commit();
        TextView textView = findViewById(R.id.tv_prefixLabel);
        textView.setAlpha(0);
        placeContainer.setAlpha(0);
        nextQuestion.setAlpha(0);
        questionText.setText(R.string.thanks_for_entering_questions);
    }

    /**
     * Callback when an error occurs with sending an answer to the server.
     */
    @Override
    public void errorCallback() {
        Toast.makeText(getApplicationContext(),
                getString(R.string.error_giving_answer), Toast.LENGTH_LONG).show();
        enableNextButton();
    }

    /**
     * Enable the nextButton for clicking.
     */
    private void enableNextButton() {
        nextQuestion.setClickable(true);
        nextQuestion.setAlpha(1);
    }

    /**
     * Disable the nextButton for clicking.
     */
    private void disableNextButton() {
        nextQuestion.setClickable(false);
        nextQuestion.setAlpha(.5f);
    }


    /**
     * Call for disabling the nextButton when a rating answer has been answered.
     */
    @Override
    public void answerRating() {
        enableNextButton();
    }

    /**
     * Close the activity when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

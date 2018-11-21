package com.example.lexar.projecthbo_ictbv.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lexar.projecthbo_ictbv.R;
import com.example.lexar.projecthbo_ictbv.model.User;
import com.example.lexar.projecthbo_ictbv.task.RegisterTask;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity for registering a new user.
 */
public class RegisterActivity extends AppCompatActivity implements RegisterTask.Callback {

    private static final String UNDER18 = "UNDER18";
    private static final String BETWEEN18AND34 = "BETWEEN18AND34";
    private static final String BETWEEN35AND49 = "BETWEEN35AND49";
    private static final String BETWEEN50AND64 = "BETWEEN50AND64";
    private static final String ABOVE65 = "ABOVE65";

    @BindView(R.id.age_spinner)
    NiceSpinner spinner;

    @BindView(R.id.rg_genderButtons)
    RadioGroup genderButtons;


    @BindView(R.id.rg_relationButtons)
    RadioGroup relationButtons;

    @BindView(R.id.et_registerCity)
    EditText etRegisterCity;

    private Context context;
    private String age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        context = getApplicationContext();
        NiceSpinner niceSpinner = findViewById(R.id.age_spinner);
        List<String> dataset = new LinkedList<>(Arrays.asList(
                "18 jaar of jonger",
                "18 tot 34 jaar",
                "35 tot 49 jaar",
                "50 tot 64 jaar",
                "65 jaar of ouder"));
        niceSpinner.attachDataSource(dataset);
        spinner.setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * Method to register an user.
     */
    @OnClick(R.id.btn_confirmRegister)
    public void register() {
        if (allFieldsFilledIn()) {
            Toast.makeText(context,
                    getString(R.string.fields_not_filled),
                    Toast.LENGTH_SHORT).show();
        } else {
            RegisterTask task = new RegisterTask(this);
            String gender = ((RadioButton) findViewById(
                    genderButtons.getCheckedRadioButtonId())).
                    getText().toString();
            String relationship = ((RadioButton) findViewById(
                    relationButtons.getCheckedRadioButtonId()))
                    .getText().toString();
            boolean single = true;
            if (relationship.equals(getString(R.string.partner))) {
                single = false;
            }
            task.execute(gender, getAgeFromSpinner(), single, etRegisterCity.getText().toString());
        }
    }

    /**
     * Method to check if all fields are filled.
     * @return boolean if all fields are filled
     */
    private boolean allFieldsFilledIn() {
        if (genderButtons.getCheckedRadioButtonId() == -1 ||
                relationButtons.getCheckedRadioButtonId() == -1 ||
                etRegisterCity.getText().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Method to get an age from the spinner.
     * @return the age
     */
    public String getAgeFromSpinner() {
        int spinnerValue = spinner.getSelectedIndex() + 1;
        switch (spinnerValue) {
            case 1:
                age = UNDER18;
                break;
            case 2:
                age = BETWEEN18AND34;
                break;
            case 3:
                age = BETWEEN35AND49;
                break;
            case 4:
                age = BETWEEN50AND64;
                break;
            case 5:
                age = ABOVE65;
                break;
        }
        return age;
    }

    /**
     * Callback which is called when a new user is successfully registered.
     * @param id the id of the created user
     */
    @Override
    public void onUserRegistered(int id) {
        User.getInstance().saveUser(id);
        finish();
    }

    /**
     * Callback which is called when an error occurs when registering a new user.
     */
    @Override
    public void onRegisterError() {
        Toast.makeText(context, getString(R.string.error_registing), Toast.LENGTH_LONG).show();
    }
}












package com.example.lexar.projecthbo_ictbv.activity;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lexar.projecthbo_ictbv.R;
import com.example.lexar.projecthbo_ictbv.error.Assert;
import com.example.lexar.projecthbo_ictbv.model.Answer;
import com.example.lexar.projecthbo_ictbv.helper.Constants;
import com.example.lexar.projecthbo_ictbv.task.TotalAnswersTask;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Activity for showing a pie chart that shows the user how many people gave a certain answer.
 */
public class GraphActivity extends AppCompatActivity
        implements TotalAnswersTask.TotalAnswersTaskCallbacks {


    private PieChart pieChart;
    private String questionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        pieChart = findViewById(R.id.bar_chart);
        pieChart.setRotationEnabled(true);
       TotalAnswersTask totalAnswersTask = new TotalAnswersTask(this);
        int questionId = getIntent().getIntExtra(Constants.QUESTION_ID_KEY, 0);
        int locationId = getIntent().getIntExtra(Constants.LOCATION_ID_KEY, 0);
        questionText = getIntent().getStringExtra(Constants.QUESTION_TEXT);
       totalAnswersTask.execute(questionId, locationId);
    }

    /**
     * Fill the pie chart with the answer data.
     * @param answers the data
     */
    @Override
    public void onGetAnswers(ArrayList<Answer> answers) {
        Assert.that(answers != null, "Answers are null");
        ArrayList<PieEntry> yEntries = new ArrayList<>();
        for (int i = 0; i < answers.size(); i++) {
            yEntries.add(new PieEntry(answers.get(i).getAmount(), answers.get(i).getText()));
        }
        PieDataSet pieDataSet = new PieDataSet(yEntries, getString(R.string.answers));
        pieChart.setUsePercentValues(true);
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setDrawValues(true);
        pieChart.setDrawEntryLabels(false);
        pieChart.setCenterText(questionText);
        Legend legend = pieChart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setForm(Legend.LegendForm.LINE);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();
    }


}

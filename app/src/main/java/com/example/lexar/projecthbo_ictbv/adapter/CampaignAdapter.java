package com.example.lexar.projecthbo_ictbv.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lexar.projecthbo_ictbv.R;
import com.example.lexar.projecthbo_ictbv.error.Assert;
import com.example.lexar.projecthbo_ictbv.model.Campaign;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * Adapter for showing the campaigns.
 */
public class CampaignAdapter extends ArrayAdapter {
    private ArrayList<Campaign> campaigns;
    private ProgressBar pbCampaignProgress;
    private TextView campaignName;
    private TextView campaignDate;

    public CampaignAdapter(@NonNull Context context, int resource,
                           @NonNull ArrayList<Campaign> campaigns) {
        super(context, resource, campaigns);
        this.campaigns = campaigns;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate
                    (R.layout.campaign_item, parent, false);
            campaignName = convertView.findViewById(R.id.tv_campaignName);
            campaignDate = convertView.findViewById(R.id.tv_campaignDate);
            pbCampaignProgress = convertView.findViewById(R.id.pb_campaignProgress);
        }
        Campaign campaign = campaigns.get(position);
        campaignName.setText(campaign.getName());
        String campaignDateFormat = getContext().getResources().
                getString(R.string.campaigndatecontainer);
        campaignDate.setText(String.format(campaignDateFormat, campaign.getStartDateString(),
                campaign.getEndDateString()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            long daysBetweenStartAndEnd = ChronoUnit.DAYS.between(campaign.getStartDate(),
                    campaign.getEndDate());
            long daysBetweenNowAndEnd = ChronoUnit.DAYS.between(LocalDate.now(),
                    campaign.getEndDate());
            System.out.println("DIFF: " + daysBetweenNowAndEnd);

            pbCampaignProgress.setMax((int) daysBetweenStartAndEnd);
            pbCampaignProgress.setProgress((int)
                    (pbCampaignProgress.getMax() - daysBetweenNowAndEnd));
        } else {
            pbCampaignProgress.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    @Override
    public void setNotifyOnChange(boolean notifyOnChange) {
        super.setNotifyOnChange(notifyOnChange);
    }
}

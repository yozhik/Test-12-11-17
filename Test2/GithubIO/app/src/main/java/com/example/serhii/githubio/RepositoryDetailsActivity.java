package com.example.serhii.githubio;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RepositoryDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_details);

        RepositoryItemInfo repositoryDetails = (RepositoryItemInfo) getIntent()
                .getParcelableExtra(Constants.PARSEL_DATA);

        TextView textViewName = (TextView) findViewById(R.id.name);
        TextView textViewDescription = (TextView) findViewById(R.id.description);
        TextView textViewStars = (TextView) findViewById(R.id.stars);
        TextView textViewForks = (TextView) findViewById(R.id.forks);
        TextView textViewWatchers = (TextView) findViewById(R.id.watchers);
        TextView textViewIssues = (TextView) findViewById(R.id.issues);
        TextView textViewUpdatedDate = (TextView) findViewById(R.id.updatedDate);

        textViewName.setText(repositoryDetails.getName());
        textViewDescription.setText(repositoryDetails.getDescription());
        textViewStars.setText(Integer.toString(repositoryDetails.getStarsCount()));
        textViewForks.setText(Integer.toString(repositoryDetails.getForksCount()));
        textViewWatchers.setText(Integer.toString(repositoryDetails.getWatchersCount()));
        textViewIssues.setText(Integer.toString(repositoryDetails.getIssuesCount()));
        textViewUpdatedDate.setText(ClearGarbageSymbolsInDate(repositoryDetails.getUpdatedDate()));

        //hide textfields with null values
        //I know that it's bad to dublicate code (similar in Adapter), but I think it's also bad to
        //change parsed data objects and replace null values with empty strings.
        if(repositoryDetails.getName() == null)
            textViewName.setVisibility(View.GONE);

        if(repositoryDetails.getDescription() == null)
            textViewDescription.setVisibility(View.GONE);
    }

    private String ClearGarbageSymbolsInDate(String input)
    {
        String formattedDate = input.replace('T', ' ');
        formattedDate = formattedDate.replace('Z', ' ');
        return formattedDate;
    }
}
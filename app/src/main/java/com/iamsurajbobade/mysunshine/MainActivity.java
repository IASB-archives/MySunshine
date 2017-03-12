package com.iamsurajbobade.mysunshine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iamsurajbobade.mysunshine.Networking.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // field from activity.xml file
    private EditText mSearchBoxEditText;
    private TextView mURLTextView;
    private TextView mSearchResultsTextView;
    private TextView mErrorTextView;
    private ProgressBar mloadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set references to all view fields from activity.xml file
        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mURLTextView = (TextView) findViewById(R.id.tv_query_url);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_query_result);
        mErrorTextView = (TextView) findViewById(R.id.tv_error_message);
        mloadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
    }

    /**
     * This method is used to do initialise the primary task to make a github query
     * This method uses helper class GitHubQueryTask to do the work in background
     */
    private void makeGitHubQuery(){
        String query = mSearchBoxEditText.getText().toString();
        URL queryUrl = NetworkUtils.buildUrl(query);
        mURLTextView.setText(queryUrl.toString());
        new GitHubQueryTask().execute(queryUrl);
    }

    // following two methods are helper methods to class GitHubQueryTask
    private void showErrorMessage(){
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }
    private void showQueryResult(){
        mSearchResultsTextView.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);
    }

    /**
     * Class GitHubQueryTask does the background task to fetch the result of query
     */
    private class GitHubQueryTask extends AsyncTask<URL, Void, String>{
        // Method works on the main tread before Execute method.
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mloadingProgressBar.setVisibility(View.VISIBLE);
        }

        // Method runs after Execute method of main thread
        @Override
        protected void onPostExecute(String gitHubSearchResult) {
            mloadingProgressBar.setVisibility(View.INVISIBLE);
            if(gitHubSearchResult != null && !gitHubSearchResult.equals("")){
                showQueryResult();
                mSearchResultsTextView.setText(gitHubSearchResult);
            }else {
                showErrorMessage();
            }
        }

        /**
         * Overriden method which works on background thread
         * @param params    final url of a search wuery will be passed to it
         * @return  result of the query in string format
         */
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String githubSearchResult = null;
            try {
                githubSearchResult = NetworkUtils.getResponceFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResult;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int clickedItemId = item.getItemId();
        if(clickedItemId == R.id.menu_search){
            makeGitHubQuery();
        }else if(clickedItemId == R.id.menu_settings){
            Toast.makeText(this, "Settings tobe added soon", Toast.LENGTH_SHORT).show();
        }
        return  super.onOptionsItemSelected(item);
    }
}

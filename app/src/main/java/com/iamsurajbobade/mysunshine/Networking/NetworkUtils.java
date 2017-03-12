package com.iamsurajbobade.mysunshine.Networking;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Suraj.Bobade on 3/13/2017.
 */

public class NetworkUtils {

    // Base URL of a github search api
    private final static String GITHUB_BASE_URL = "https://api.github.com/search/repositories";
    // Parameters to use while building the URL
    private final static String PARAM_QUERY = "q";
    private final static String PARAM_SORT = "sort";
    private final static String sortBy = "stars";

    /**
     * This method builds the url to make the HTTP request to github
     *
     * @param githubSearchQuery is the String that will be searched on the github
     * @return a URL that will be used to fetch the result of @Param result
     */
    public static URL buildUrl(String githubSearchQuery){
        // build query url using android Class Uri
        Uri buildUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, githubSearchQuery)
                .appendQueryParameter(PARAM_SORT, sortBy)
                .build();

        // In order to use this built url in java, convert it to URL class of java
        URL queryUrl = null;
        try {
            queryUrl = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return queryUrl;
    }

    /**
     * This method returns the result of a query using HTTP responce
     * @param url is the URL to fetch the HTTP response from
     * @return  The result of HTTP response in String format
     * @throws IOException  Related to Nework and stream reading
     */
    public static String getResponceFromHttpUrl(URL url) throws IOException {
        // Create a http connection with the server using Java HttpURLConnection object
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try {
            // Get input stream tobe used by scanner
            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }else {
                return null;
            }
        }finally {
            httpURLConnection.disconnect();
        }
    }
}

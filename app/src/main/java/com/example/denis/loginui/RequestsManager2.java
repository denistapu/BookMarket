package com.example.denis.loginui;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RequestsManager2 extends AsyncTask<Void, Void, JSONObject>{

    private RequestCallBack listener;
    private HashMap<String,String> params = null;

    public RequestsManager2(RequestCallBack listener){
        this.listener = listener;
    }
    public void setParams(HashMap<String,String> params){
        this.params = params;
    }

    protected JSONObject doInBackground(Void... p){
        JSONObject jsonResponse = null;

        URL url;
        HttpURLConnection httpURLConnection = null;
        OutputStreamWriter outputStreamWriter = null;

        try {

            //open connection to the server
            String reqUrl = "http://transfertk.ddns.net:8063/api/Api.php?request="+params.get("request");
            for(Map.Entry<String,String> entry : params.entrySet())
                reqUrl += "&"+entry.getKey()+"="+entry.getValue();
            url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            //set request properties
            httpURLConnection.setDoOutput(true); //defaults request method to POST
            httpURLConnection.setDoInput(true);  //allow input to this HttpURLConnection
            httpURLConnection.setRequestMethod("GET");
            //open output stream and POST our JSON data to server
           /* outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
            outputStreamWriter.write(getPostDataString(params));
            outputStreamWriter.flush(); //flush the stream when we're finished writing to make sure all bytes get to their destination*/

            //prepare input buffer and get the http response from server
            StringBuilder stringBuilder = new StringBuilder();
            int responseCode = httpURLConnection.getResponseCode();

            //Check to make sure we got a valid status response from the server,
            //then get the server JSON response if we did.
            if(responseCode == HttpURLConnection.HTTP_OK) {

                //read in each line of the response to the input buffer
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                bufferedReader.close(); //close out the input stream

                try {
                    //Copy the JSON response to a local JSONObject
                    jsonResponse = new JSONObject(stringBuilder.toString());
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if(httpURLConnection != null) {
                httpURLConnection.disconnect(); //close out our http connection
            }

            if(outputStreamWriter != null) {
                try {
                    outputStreamWriter.close(); //close our output stream
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

//Return the JSON response from the server.
        return jsonResponse;
    }


    protected void onPostExecute(JSONObject result) {
        listener.onResultReceived(result);
    }
    public interface RequestCallBack{
        void onResultReceived(JSONObject result);
    }


    private String getPostDataString(HashMap<String,String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}

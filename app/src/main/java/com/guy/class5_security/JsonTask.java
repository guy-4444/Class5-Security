package com.guy.class5_security;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class JsonTask extends AsyncTask<String, Integer, String> {

    public interface CallBack_Response {
        void response(boolean success, String response);
    }

    private Context context;
    private ProgressDialog pd;
    private String url;
    private CallBack_Response callBack_response;

    public JsonTask(Context context, String url, CallBack_Response callBack_response) {
        this.context = context;
        this.url = url;
        this.callBack_response = callBack_response;
    }

    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(context);
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.show();
    }

    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        /*
        if you call like
        jsonTask.execute("https://pastebin.com/raw/RNQj7Add");
        params[0] will be "https://pastebin.com/raw/RNQj7Add"

         */
        try {
            URL urlClass = new URL(url);
            connection = (HttpURLConnection) urlClass.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            int index = 0;
            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);

                publishProgress(index++);
            }

            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d("JsonTask-Progress", "" + values[0]);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (pd.isShowing()){
            pd.dismiss();
        }

        Log.d("JsonTask", "result=" + result);
        if (result == null) {
            callBack_response.response(false, null);
        } else {
            callBack_response.response(true, result);
        }
    }
}

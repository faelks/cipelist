package com.mad.cipelist.test;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mad.cipelist.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Felix on 21/09/16.
 *
 * Test activity for a SQLite Database and the Yummly API
 * The activity is able to download and display an image from the API and
 * can save users to the database.
 */
public class Activity extends android.app.Activity {
    EditText userNameEt, passEt, passConEt;
    Button registerBtn, deleteUserBtn, downloadRecipeBtn;
    String usrString, passStr, passConStr;
    Context ctx = this;
    DatabaseOperations mDatabaseHelper;
    Cursor cr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        userNameEt = (EditText) findViewById(R.id.usernameET);
        passEt = (EditText) findViewById(R.id.passET);
        passConEt = (EditText) findViewById(R.id.passConEt);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        deleteUserBtn = (Button) findViewById(R.id.deleteUserBtn);
        downloadRecipeBtn = (Button) findViewById(R.id.downloadRecipeBtn);

        mDatabaseHelper = new DatabaseOperations(ctx);
        cr = mDatabaseHelper.getInformation(mDatabaseHelper);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usrString = userNameEt.getText().toString();
                passStr = passEt.getText().toString();
                passConStr = passConEt.getText().toString();

                cr.moveToFirst();

                boolean userExists = false;
                do {
                    if(usrString.equals(cr.getString(0))) {
                        Toast.makeText(getBaseContext(), "User already exists", Toast.LENGTH_SHORT).show();
                        userNameEt.setText("");
                        userExists = true;
                    }
                } while (cr.moveToNext());

                if (!userExists) {
                    if (!passStr.equals(passConStr)) {
                        Toast.makeText(getBaseContext(), "Passwords are not matching", Toast.LENGTH_SHORT).show();
                        passEt.setText("");
                        passConEt.setText("");
                    } else {
                        mDatabaseHelper.putInformation(mDatabaseHelper, usrString, passStr);
                        Toast.makeText(getBaseContext(), "User registered successfully to database", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });

        deleteUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usrString = userNameEt.getText().toString();
                mDatabaseHelper.deleteUser(mDatabaseHelper, usrString);
                Toast.makeText(getBaseContext(), "Deleted user " + usrString, Toast.LENGTH_SHORT).show();
            }
        });

        downloadRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadRecipesAsyncTask().execute();
            }
        });
    }

    /**
     * Downloads recipes from the Yummly API
     */
    private class DownloadRecipesAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                // This url uses the ID given by Yummly as well as a generated key.
                // The last part is a search query that should be based on user preference
                JSONObject jsonObject = getJSONObjectFromURL("http://api.yummly.com/v1/api/recipes?_app_id=772a7337&_app_key=c302ed36e3515ca025686c8c070b3739&q=onion+soup");

                //Parses the returned JSON object and sets the urlAtIndex to the
                //first url found.
                JSONArray matches = jsonObject.optJSONArray("matches");
                String urlAtIndex = "";
                if (matches != null) {
                    for (int i = 0; i < matches.length(); i++) {
                        JSONObject objAtIndex =  matches.optJSONObject(i);
                        if (objAtIndex != null) {
                            JSONArray smallImageUrls = objAtIndex.optJSONArray("smallImageUrls");
                            for (int j = 0; j < smallImageUrls.length(); j++) {
                                urlAtIndex = smallImageUrls.optString(j);
                            }
                        }
                    }

                }
                return urlAtIndex;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... params) {
            super.onProgressUpdate(params);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result!=null) {
                Toast.makeText(getApplicationContext(), "Dowloaded", Toast.LENGTH_SHORT).show();
                Log.d("JSON", result);

                // Removes the size parameter from the url so that the image is larger
                result = result.substring(0, result.lastIndexOf("=") );
                Log.d("JSON", result);

                // Uses the url to download the image for a recipe
                new DownloadImageTask((ImageView) findViewById(R.id.recipeImg))
                        .execute(result);
            }
        }
    }

    /**
     * Donwloads a JSON object from a url
     * @param urlString
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection = null;

        URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);

        urlConnection.setDoOutput(true);

        urlConnection.connect();

        BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));

        char[] buffer = new char[1024];

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();

        String jsonString = sb.toString();

        Log.d("JSON", "Downloaded JSON Object");

        return new JSONObject(jsonString);
    }

    /**
     * Dowloads a bitmap image from the input url string
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

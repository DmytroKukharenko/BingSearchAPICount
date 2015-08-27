package singwaichan.android.search;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import singwaichan.android.usatodayhttpsample.R;


public class MainActivity extends ActionBarActivity {

    private EditText text1;
    private EditText text2;
    private Button button;
    private TextView text3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text1 = (EditText) this.findViewById(R.id.textView2);
        text2 = (EditText) this.findViewById(R.id.textView);
        text3 = (TextView) this.findViewById(R.id.textView3);
        button = (Button) this.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tex = String.valueOf(text1.getText());
                BingAsyncTask bingAsyncTask = new BingAsyncTask(tex);
                bingAsyncTask.execute();
                String tex1 = String.valueOf(text2.getText());
                BingAsyncTask bingAsyncTask1 = new BingAsyncTask(tex1);
                bingAsyncTask1.execute();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class BingAsyncTask extends AsyncTask<Void, Void, Long> {

            private String search;
        private String APILink;
        private String API_KEY;

        public  BingAsyncTask(String search){
            this.search=search;
            this.search.replace(" ", "%20").trim();
        }

        @Override
        protected Long doInBackground(Void... params) {
            String result = "";

            APILink = "https://api.datamarket.azure.com/Data.ashx/Bing/Search/v1/Composite?Sources=%27web%27&Query=%27"+this.search +"%27&$top=1&$format=JSON";
            API_KEY = "OnU2Mxti1LltKV0xiBXh0wvlN3DbXwXngWfcHZ+1tME";


            HttpClient httpClient = new DefaultHttpClient();


            HttpGet httpget = new HttpGet(APILink);
            String auth = API_KEY + ":" + API_KEY;
            String encodedAuth = Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);
            Log.e("", encodedAuth);
            httpget.addHeader("Authorization", "Basic " + encodedAuth);


            //Execute and get the response.
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpget);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                try {
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Extract link from JSON
            //String to Json
            JSONObject jsonObject = null;
            if (JSONValue.isValidJson(result)) {
                jsonObject = (JSONObject) JSONValue.parse(result);
            }



            jsonObject = (JSONObject) jsonObject.get("d");

            jsonObject = (JSONObject) ((JSONArray) jsonObject.get("results")).get(0);

            long res = Long.parseLong(jsonObject.get("WebTotal").toString());




            return res;
        }



        @Override
        protected void onPostExecute(Long res) {
            super.onPostExecute(res);
            MainActivity.this.text3.setText(MainActivity.this.text3.getText()+"   "+(res).toString());
        }
    }
}




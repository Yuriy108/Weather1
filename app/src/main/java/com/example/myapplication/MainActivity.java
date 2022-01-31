package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String link="https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&lang=ua&appid=c4fa1894bf48a995a9f3f5e0dfd2c37b";
    EditText editText;
    TextView textView;
    String weather;
    String customerUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.editTextTextPersonName);
        textView=findViewById(R.id.textView);

    }

    public void sent(View view) {
        String town=editText.getText().toString();
        if(!town.isEmpty()){
            customerUrl=String.format(link,town);
            Download download=new Download();
            download.execute(customerUrl);

        }
    }

    class Download extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            StringBuffer stringBuffer=new StringBuffer();
            URL url=null;
            HttpURLConnection connection=null;
            try {
                url=new URL(strings[0]);
                connection=(HttpURLConnection) url.openConnection();
                InputStream inputStream=connection.getInputStream();
                InputStreamReader streamReader=new InputStreamReader(inputStream);
                BufferedReader reader=new BufferedReader(streamReader);
                String line=reader.readLine();
                while (line!=null){
                    stringBuffer.append(line);
                    line=reader.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                String city=jsonObject.getString("name");
                JSONObject temp= jsonObject.getJSONObject("main");
                String mytemp=temp.getString("temp");
                JSONArray array=jsonObject.getJSONArray("weather");
                String des=array.getJSONObject(0).getString("description");
                weather=String.format("В городе %s\n температура %s\n на небе у нас %s",city,mytemp,des);
                textView.setText(weather);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
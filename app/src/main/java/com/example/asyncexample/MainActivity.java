package com.example.asyncexample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;


public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button startTaskButton; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressbar);
        startTaskButton = findViewById(R.id.startAsyncTask); 
        
        startTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAsyncTasnk(v); 
            }
        });

    }

    private void startAsyncTasnk(View v) {
        ExampleAsyncTask task = new ExampleAsyncTask(this);
        task.execute(10);


    }

    private static class ExampleAsyncTask extends AsyncTask<Integer, Integer, String>{

        private WeakReference<MainActivity> mReference;

        ExampleAsyncTask(MainActivity activity){
            mReference = new WeakReference<MainActivity>(activity);
        }


        @Override
        protected String doInBackground(Integer... integers) {

            for(int i = 0 ; i < integers[0]; i++){
                publishProgress((i * 100) / integers[0]);
                try{
                    Thread.sleep(2000);
                }
                catch (InterruptedException e ){
                    e.printStackTrace();
                }
            }


            return "Finished!";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            MainActivity activity = mReference.get();
            if(activity == null || activity.isFinishing()){
                return;
            }
            activity.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            MainActivity activity = mReference.get();
            if(activity == null || activity.isFinishing()){
                return;
            }

            Toast.makeText( activity, s, Toast.LENGTH_SHORT).show();
            activity.progressBar.setProgress(0);
            activity.progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            MainActivity activity = mReference.get();
            if(activity == null || activity.isFinishing()){
                return;
            }

            activity.progressBar.setProgress(values[0]);


        }
    }




}

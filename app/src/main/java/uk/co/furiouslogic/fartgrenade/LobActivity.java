package uk.co.furiouslogic.fartgrenade;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.pollfish.main.PollFish;
import com.pollfish.constants.Position;


import java.io.InputStream;
import java.util.Random;

public class LobActivity extends ActionBarActivity {

    private int _fartDelay = 5;

    //todo: proper fart sounds
    //todo: admob
    //todo: pollfish
    //todo: volume up
    //todo: darken screen
    //todo: record result

    //Controls
    private TextView _txtCountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lob);

        //Admob
        AdView avTimerBanner = (AdView) findViewById(R.id.avMainBanner);
        avTimerBanner.loadAd(new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("A9C350CBED46BAC089E21096D5522BE6").build());

        //Get controls
        _txtCountDown = (TextView) findViewById(R.id.txtCountDown);

        showState(_fartDelay);
    }

    @Override
    protected void onResume(){
        super.onResume();

        PollFish.init(this, "7758ef7c-4cfb-4c16-97b3-17b91033fdfd", Position.TOP_LEFT, 50);
    }

    private void showState(Integer currentCountDownSecond) {
        if (currentCountDownSecond > 0)
            _txtCountDown.setText(String.valueOf(currentCountDownSecond));
        else {
            _txtCountDown.setText("OOPS!");
            makeRandomFart();
        }
    }

    private void makeRandomFart() {
        Context context = getApplicationContext();

        SoundPool beepPool;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            beepPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(aa)
                    .build();
        }else
            beepPool = new SoundPool(10, AudioManager.STREAM_ALARM, 1);

        //Get random number to choose fart sound
        Random rand = new Random();
        int max = 3;
        int min = 1;
        int randomNum = rand.nextInt((max - min) + 1) + min;

        String resourceName = "fart0" + randomNum;
        int resId = getResources().getIdentifier(resourceName , "raw", getPackageName());
        int noiseId = beepPool.load(context, resId, 1);

        beepPool.play(noiseId, 1, 1, 1, 0, 1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lob, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void btnArm_onClick(View view) {
//        TextView txtCountDown = (TextView) findViewById(R.id.txtCountDown);
//        txtCountDown.setText("Arse Biscuits!");
        new CountDown().execute(_fartDelay);
    }

    public void btnUp_onClick(View view) {
        if(_fartDelay>=300) return;
        _fartDelay+=5;
        showState(_fartDelay);
    }

    public void btnDown_onClick(View view) {
        if(_fartDelay<=5) return;
        _fartDelay-=5;
        showState(_fartDelay);
    }

    private class CountDown extends AsyncTask<Integer, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... params) {
            long startMillis = System.currentTimeMillis();
            int secondsGone = 0;

            while (secondsGone < _fartDelay) {
                if (startMillis + (secondsGone * 1000) + 1000 <= System.currentTimeMillis()) {
                    secondsGone++;
                    publishProgress(secondsGone);
                }
            }

            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... params) {
            super.onProgressUpdate(params);
            int currentSecond = params[0];

            showState(_fartDelay - currentSecond);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            showState(0);
        }
    }
}

package uk.co.furiouslogic.fartgrenade;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.InputStream;

//todo: Quick fart delay selector
//todo: Randomly choose from raw resources

public class LobActivity extends ActionBarActivity {

    private boolean _isRunning = false;
    private int _fartDelay = 10;

    //Controls
    private TextView _txtCountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lob);

        //Get controls
        _txtCountDown = (TextView) findViewById(R.id.txtCountDown);

        showState(_fartDelay);
    }

    private void showState(Integer currentCountDownSecond) {
        if (currentCountDownSecond > 0)
            _txtCountDown.setText(String.valueOf(currentCountDownSecond));
        else {
            _txtCountDown.setText("FART!");
            makeRandomFart();
        }
    }

    private void makeRandomFart() {
        Context context = getApplicationContext();

        SoundPool beepPool;
        int beepLowId;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            beepPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(aa)
                    .build();
            beepLowId = beepPool.load(context, R.raw.test_fart_01, 1);
//            beepHighId = beepPool.load(context, R.raw.beephigh, 1);
        }else{
            beepPool = new SoundPool(10, AudioManager.STREAM_ALARM, 1);
            beepLowId = beepPool.load(context, R.raw.test_fart_01, 1);
//            beepHighId = beepPool.load(context, R.raw.beephigh, 1);
        }

        beepPool.play(beepLowId, 1, 1, 1, 0, 1);
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

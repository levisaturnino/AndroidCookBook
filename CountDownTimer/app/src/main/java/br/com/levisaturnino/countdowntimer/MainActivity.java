package br.com.levisaturnino.countdowntimer;

import android.databinding.DataBindingUtil;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.levisaturnino.countdowntimer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MalibuCountDownTimer countDownTimer;
    private long timeElapsed;
    private boolean timerHasStarted = false;

    private final long startTime = 50 * 1000;
    private final long interval = 1 * 1000;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        binding.start.setOnClickListener(this);

        countDownTimer = new MalibuCountDownTimer(startTime,interval);
        binding.timer.setText(binding.timer.getText()+String.valueOf(startTime));

    }

    @Override
    public void onClick(View v) {

        if(!timerHasStarted){
            countDownTimer.start();
            timerHasStarted = true;
            binding.start.setText("Start");
        }else{
            countDownTimer.cancel();
            timerHasStarted = false;
            binding.start.setText("RESET");
        }

    }

    public class MalibuCountDownTimer extends CountDownTimer {
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public MalibuCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        binding.timer.setText("Time remain:"+millisUntilFinished);
        timeElapsed = startTime - millisUntilFinished;

        binding.timeElapsed.setText("Time Elapsed: "+String.valueOf(timeElapsed));
    }

    @Override
    public void onFinish() {

        binding.timer.setText("Time's up! ");
        binding.timeElapsed.setText("Time Elapsed: "+String.valueOf(startTime));
    }
    }

}
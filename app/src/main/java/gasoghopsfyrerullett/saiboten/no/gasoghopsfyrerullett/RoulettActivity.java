package gasoghopsfyrerullett.saiboten.no.gasoghopsfyrerullett;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RoulettActivity extends AppCompatActivity {

    private static final String TAG = "RoulettActivity";

    private Random randomGenerator = new Random();

    @BindView(R.id.result)
    TextView result;

    @BindView(R.id.utestedRoulettList)
    ListView clubs;

    ArrayList<String> inputClubs;

    ArrayAdapter<String> arrayAdapter;



    @BindView(R.id.roulett_back)
    Button backButton;

   @BindView(R.id.startRoulette)
           Button startButton;

    @BindView(R.id.RouletteContainer)
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulett);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String[] inputClubsArray = intent.getStringArrayExtra("eligableClubs");
        inputClubs = new ArrayList<String>();
        inputClubs.addAll(Arrays.asList(inputClubsArray));

        Log.d(TAG, "Clubs: " + inputClubs);
        int index = randomGenerator.nextInt(inputClubs.size());
        String club = inputClubs.get(index);
        Log.d(TAG, "Selected club: " + club);

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, inputClubs);
        clubs.setAdapter(arrayAdapter);
    }

    @OnClick(R.id.startRoulette)
    public void startRoulette() {
        startButton.setVisibility(View.GONE);
        testAnim();
    }

    public void testAnim() {
        if(inputClubs.size() == 1) {
            Log.d(TAG, "We are done");
            clubs.setVisibility(View.GONE);
            result.setText("Du skal til: " + inputClubs.get(0));
            backButton.setVisibility(View.VISIBLE);
            new ParticleSystem(this, 300, R.drawable.pixel, 10000)
                    .setSpeedRange(0.05f, 0.5f)
                    .oneShot( relativeLayout, 300);
            return;
        }

        final int index = randomGenerator.nextInt(inputClubs.size());
        String club = inputClubs.get(index);
        Animation anim = AnimationUtils.loadAnimation(
                RoulettActivity.this, android.R.anim.slide_out_right
        );

        anim.setDuration(2000);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, "onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "onAnimationEnd");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Observable.timer(anim.getDuration(), TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Log.d(TAG, "Removing child from index: " + index);
                clubs.getChildAt(index).clearAnimation();
                inputClubs.remove(index);
                arrayAdapter.notifyDataSetChanged();
                testAnim();
            }
        });

        Log.d(TAG, "Starting animation");
        clubs.getChildAt(index).setAnimation(anim);
        anim.start();

    }

    @OnClick(R.id.roulett_back)
    public void back() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

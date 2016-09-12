package gasoghopsfyrerullett.saiboten.no.gasoghopsfyrerullett;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    List<Club> items;

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.result)
    TextView result;

    @BindView(R.id.addUtestedText)
    EditText uteStedText;

    @BindView(R.id.checkBoxAddDrinks)
    CheckBox checkBoxAddDrinks;

    @BindView(R.id.checkBoxAddGuiness)
    CheckBox checkBoxAddGuiness;

    @BindView(R.id.checkBoxDrinks)
    CheckBox checkBoxDrinks;

    @BindView(R.id.checkBoxGuiness)
    CheckBox checkBoxGuiness;

    UtestedAdapter uteStedAdapter;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        populateUtesteder();

        uteStedAdapter = new UtestedAdapter(this, items);
        listView.setAdapter(uteStedAdapter);

        View view = View.inflate(this, R.layout.header, null);
        listView.addHeaderView(view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int finalI = i;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Slett")
                        .setMessage("Vil du slette utestedet?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(R.string.yes_norwegian, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                items.remove(finalI-1);
                                uteStedAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Slettet", Toast.LENGTH_SHORT).show();
                                storeClubs();
                            }})
                        .setNegativeButton(R.string.no_norwegian, null).show();
            }
        });
    }

    private void populateUtesteder() {
        items = new ArrayList<>();
        List<Club> storedClubs = getStoredClubs();
        if(storedClubs != null) {
            for(Club storedClub :storedClubs) {
                items.add(storedClub);
            }
        }
    }

    @OnClick(R.id.roulette_start)
    public void rouletteStart() {

        final boolean drinks = checkBoxDrinks.isChecked();
        final boolean guiness = checkBoxGuiness.isChecked();

        final List<String> finalUtestedList = new ArrayList<>();

        final Observable<Club> observableClub = Observable.from(items);
        observableClub.filter(new Func1<Club, Boolean>() {
            @Override
            public Boolean call(Club club) {
                Log.d(TAG, "Filtering club: " + club);
                 if(drinks && !club.isDrinker()) {
                    return false;
                 }
                if(guiness && !club.isGuiness()) {
                    return false;
                }
                if(guiness && drinks && (!club.isGuiness() || !club.isDrinker())) {
                    return false;
                }

                return true;

            }
        }).map(new Func1<Club, String>() {
            @Override
            public String call(Club club) {
                return club.getNavn();
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted. List: " + finalUtestedList);
                if(finalUtestedList.size() > 0) {
                    Intent startRoulette = new Intent(getApplicationContext(), RoulettActivity.class);
                    String[] utestedStringArray = new String[finalUtestedList.size()];
                    for(int i = 0 ; i < finalUtestedList.size(); i++) {
                        utestedStringArray[i] = finalUtestedList.get(i);
                    }
                    startRoulette.putExtra("eligableClubs", utestedStringArray);
                    startActivity(startRoulette);
                }
                else {
                    result.setText("Ingen utested matcher dine krav. Sorry borry");
                }

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String utested) {
                Log.d(TAG, "onNext: " + utested);
                finalUtestedList.add(utested);
            }
        });


    }

    @OnClick(R.id.addUteSted)
    public void addClub() {
        Club uteSted = new Club();
        uteSted.setNavn(uteStedText.getText().toString());
        uteSted.setDrinker(checkBoxAddDrinks.isChecked());
        uteSted.setGuiness(checkBoxAddGuiness.isChecked());
        items.add(uteSted);
        uteStedAdapter.notifyDataSetChanged();
        uteStedText.setText("");
        checkBoxAddDrinks.setChecked(false);
        checkBoxAddGuiness.setChecked(false);
        storeClubs();
    }

    public void storeClubs() {
        SharedPreferences sharedPereferences = getSharedPreferences("GasOgHopsFyreRulett",0);
        Gson gson = new Gson();
        String str = gson.toJson(items);
        Log.d(TAG, "Str: " + str);
        sharedPereferences.edit().putString("clubs",str).apply();
    }

    public List getStoredClubs() {
        List<Club> returnList = null;
        SharedPreferences sharedPereferences = getSharedPreferences("GasOgHopsFyreRulett",0);
        String storedUtesteder = sharedPereferences.getString("clubs", null);

        if(storedUtesteder != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Club>>(){}.getType();

            returnList = gson.fromJson(storedUtesteder, listType);
            Log.d(TAG, "Result " + returnList);
            Object obj = returnList.get(0);
            Club club = (Club) obj;

            Log.d(TAG, "Club: "+ club);
        }

        return returnList;

    }

}

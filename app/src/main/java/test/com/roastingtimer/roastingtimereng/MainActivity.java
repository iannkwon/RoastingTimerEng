package test.com.roastingtimer.roastingtimereng;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    Button btn_start;
    Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-9812429853692507/2462051357");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        final SQLiteDatabase mDatabase = openOrCreateDatabase("timer.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        //db 테이블 생성
        mDatabase.execSQL("create table if not exists roasting(" +
                "num integer primary key autoincrement," +
                "rdate text not null," +
                "origin text not null," +
                "temp text not null," +
                "humi text not null," +
                "record text not null," +
                "end text not null,"+
                "memo text not null);");

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                startActivity(intent);
                finish();



            }
        });

    }
}

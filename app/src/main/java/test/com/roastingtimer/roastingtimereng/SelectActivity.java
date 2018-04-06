package test.com.roastingtimer.roastingtimereng;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class SelectActivity extends AppCompatActivity {

    ListView list_select;
    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        MobileAds.initialize(this, "ca-app-pub-9812429853692507/2462051357");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final ArrayList<String> list = new ArrayList<>();

        mDatabase = openOrCreateDatabase("timer.db",0,null);

        //select * from roasting
        Cursor c = mDatabase.query("roasting",
                null,null,null,null,null,"num desc");
        c.moveToFirst(); //커서 포인터 위치를 첫번째 레코드 위치로
        while (!c.isAfterLast()){ //현재 커서포인터위치에서 다음레코드가 있는지 확인 , 마지막 레코드이면 true recode
            String data = c.getString(c.getColumnIndex("rdate"))+"\n"+
                    "Origin : "+c.getString(c.getColumnIndex("origin"))+"\n"+
                    "Temp : "+c.getString(c.getColumnIndex("temp"))+"\n"+
                    "Humi : "+c.getString(c.getColumnIndex("humi"))+"\n"+
                    "Record"+"\n"+c.getString(c.getColumnIndex("record"))+"\n"+
                    "Out : "+c.getString(c.getColumnIndex("end"))+"\n"+
                    "Memo"+"\n"+c.getString(c.getColumnIndex("memo"));
            list.add(data);
            c.moveToNext();//커서포인터위치를 다음 레코드 위치로
        }

        list_select = (ListView) findViewById(R.id.list_select);
        list_select.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1,list
        ));

        list_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("SelectActivity","row>>>"+parent.getItemAtPosition(position));
                Intent intent = new Intent(getApplicationContext(),
                        UpdateActivity.class);
                intent.putExtra("info", parent.getItemAtPosition(position).toString());  //부모뷰의 데이터 요구

                startActivity(intent);
                finish();


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

}


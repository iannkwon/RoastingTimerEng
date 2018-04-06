package test.com.roastingtimer.roastingtimereng;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class UpdateActivity extends AppCompatActivity {

    TextView tv_date;
    EditText tv_record;
    TextView tv_out;
    EditText et_originUp;
    EditText et_tempUp;
    EditText et_humiUp;
    EditText et_memo;

    SQLiteDatabase mDatabase;
    Button btn_update;
    Button btn_delete;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        MobileAds.initialize(this, "ca-app-pub-9812429853692507/2462051357");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        tv_date = (TextView) findViewById(R.id.tv_date);
        et_originUp = (EditText) findViewById(R.id.et_originUp);
        et_tempUp = (EditText) findViewById(R.id.et_tempUp);
        et_humiUp = (EditText) findViewById(R.id.et_humiUp);
        tv_record = (EditText) findViewById(R.id.tv_record);
        tv_out = (TextView) findViewById(R.id.tv_out);
        et_memo = (EditText)findViewById(R.id.et_memo);

        btn_update = (Button)findViewById(R.id.btn_update);
        btn_delete = (Button)findViewById(R.id.btn_delete);

        //스크롤바 추가
        tv_record.setMovementMethod(new ScrollingMovementMethod());

        LinearLayout layoutUpdate = (LinearLayout)findViewById(R.id.layoutUpdate);
        layoutUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_originUp.getWindowToken(), 0);
            }
        });

        String info = getIntent().getStringExtra("info");
        Log.i("UpdateActivity", info);

        String[] temp = info.split("\n"); //문자열 분리


        mDatabase=openOrCreateDatabase("timer.db",
                SQLiteDatabase.CREATE_IF_NECESSARY,null);
        Cursor c = mDatabase.query("roasting",
                new String[]{"rdate","origin","temp","humi","end","record","memo"}, //컬럼
                "rdate"+"=?",new String[]{String.valueOf(temp[0])}, //조건문
                null,null,null);
        c.getColumnName(0);
        c.moveToFirst(); //커서 포인터 위치를 첫번째 레코드 위치로
        while (!c.isAfterLast()){ //현재 커서포인터위치에서y 다음레코드가 있는지 확인 , 마지막 레코드이면 true recode
            tv_date.setText(c.getString(c.getColumnIndex("rdate")));
            et_originUp.setText(c.getString(c.getColumnIndex("origin")));
            et_tempUp.setText(c.getString(c.getColumnIndex("temp")));
            et_humiUp.setText(c.getString(c.getColumnIndex("humi")));
            tv_out.setText(c.getString(c.getColumnIndex("end")));
            tv_record.setText(c.getString(c.getColumnIndex("record")));
            et_memo.setText(c.getString(c.getColumnIndex("memo")));
            c.moveToNext();//커서포인터위치를 다음 레코드 위치로
        }






        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder ab = new AlertDialog.Builder(context);

                //제목셋팅
                ab.setTitle("Update");

                //AlertDialog 셋팅
                ab.setMessage("Are you update?")
                        .setCancelable(false)
                        .setPositiveButton("Update",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ContentValues values = new ContentValues();
                                        values.put("origin",et_originUp.getText().toString());
                                        values.put("temp",et_tempUp.getText().toString());
                                        values.put("humi",et_humiUp.getText().toString());
                                        values.put("memo",et_memo.getText().toString());

                                        int updateCount = mDatabase.update("roasting", values, "rdate=?",new String[]{tv_date.getText().toString()});

                                        Log.i("updateActivity","update count:"+updateCount);

                                        if (updateCount>0) {
                                            UpdateActivity.this.finish();
                                            Intent intent = new Intent(getApplicationContext(), SelectActivity.class);

                                            startActivity(intent);
                                        }
                                        Toast.makeText(getApplicationContext(), "Update Successed", Toast.LENGTH_SHORT).show();
                                    }
                                }


                        )
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                //다이얼로그 생성
                AlertDialog ad = ab.create();
                //다이얼로그 보여주기
                ad.show();



            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder ab = new AlertDialog.Builder(context);

                //제목셋팅
                ab.setTitle("Delete");

                //AlertDialog 셋팅
                ab.setMessage("Are you delete?")
                        .setCancelable(false)
                        .setPositiveButton("Delete",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        int deleteCount = mDatabase.delete("roasting","rdate=?",new String[]{tv_date.getText().toString()});

                                        Log.i("UpdateActivity","deleteCount: "+deleteCount);
                                        if(deleteCount>0) {

                                            Intent intent = new Intent(
                                                    getApplicationContext(), SelectActivity.class);
                                            startActivity(intent);
                                        }
                                        Toast.makeText(getApplicationContext(), "Delete Successed", Toast.LENGTH_SHORT).show();
                                        UpdateActivity.this.finish();

                                    }
                                }


                        )
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                //다이얼로그 생성
                AlertDialog ad = ab.create();
                //다이얼로그 보여주기
                ad.show();

            }

        });

    }//end create

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
        startActivity(intent);
    }
}

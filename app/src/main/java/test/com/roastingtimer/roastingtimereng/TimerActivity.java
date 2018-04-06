package test.com.roastingtimer.roastingtimereng;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
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

import java.util.Date;

public class TimerActivity extends AppCompatActivity {
    TextView text_timer;
    TextView text_list;
    TextView text_tem;
    TextView text_tem2;
    TextView text_humi;
    TextView text_humi2;

    EditText et_tem;
    EditText et_humi;
    EditText et_origin;

    Button btn_tStart;
    Button btn_1st;
    Button btn_2st;
    Button btn_check;
    Button btn_save;
    Button btn_cancel;

    final static int init = 0;
    final static int run = 1;
    final static int pause = 2;

    int status = init;
    int count = 1;
    long baseTime;
    long pauseTime;
    String pop1 = "1st POP!";
    String pop2 = "2st POP!";
    String nowTime;
    String memo;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        MobileAds.initialize(this, "ca-app-pub-9812429853692507/2462051357");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        text_timer = (TextView) findViewById(R.id.text_timer);
        text_list = (TextView) findViewById(R.id.text_list);
        text_tem = (TextView) findViewById(R.id.text_tem);
        text_tem2 = (TextView) findViewById(R.id.text_tem2);
        text_humi = (TextView) findViewById(R.id.text_humi);
        text_humi2 = (TextView) findViewById(R.id.text_humi2);

        et_tem = (EditText)findViewById(R.id.et_tem);
        et_humi = (EditText)findViewById(R.id.et_humi);
        et_origin = (EditText)findViewById(R.id.et_origin);

        btn_tStart = (Button)findViewById(R.id.btn_tStart);
        btn_1st = (Button)findViewById(R.id.btn_1st);
        btn_2st = (Button)findViewById(R.id.btn_2st);
        btn_check = (Button)findViewById(R.id.btn_check);
        btn_save = (Button)findViewById(R.id.btn_save);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);

        text_list.setMovementMethod(new ScrollingMovementMethod());

        LinearLayout layoutTimer = (LinearLayout)findViewById(R.id.layoutTimer);
        layoutTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_origin.getWindowToken(), 0);
            }
        });
    }//end create

    //뒤로가기 버튼 막기

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            text_timer.setText(getTimeOut());

            handler.sendEmptyMessage(0);
        }
    };

    String getTimeOut(){
        long now = SystemClock.elapsedRealtime();
        long outTime = now - baseTime;
        String easy_outTime = String.format("%02d:%02d:%02d", outTime/1000 /60, (outTime/1000)%60, (outTime%1000)/10);
        return easy_outTime;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }//end destroy


    public void onClick(View v){



        switch (v.getId()){
            case R.id.btn_tStart:
                switch (status){
                    case init:
                        baseTime = SystemClock.elapsedRealtime();
                        handler.sendEmptyMessage(0);
                        btn_tStart.setText("STOP");
                        btn_check.setEnabled(true);
                        status = run;
                        break;
                    case run:
                        handler.removeMessages(0);
                        pauseTime = SystemClock.elapsedRealtime();
                        btn_tStart.setText("START");
                        btn_check.setText("RESET");
                        status = pause;
                        break;
                    case pause:
                        long now = SystemClock.elapsedRealtime();
                        handler.sendEmptyMessage(0);
                        baseTime += (now - pauseTime);
                        btn_tStart.setText("STOP");
                        btn_check.setText("CHECK");
                        status = run;
                        break;
                }
                break;
            case R.id.btn_check:
                switch (status){
                    case run:
                        nowTime = getTimeOut();
                        // 다이얼로그
                        final EditText editText = new EditText(this);
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Memo");
                        builder.setMessage("No Memo Click input");
                        builder.setView(editText);

                        builder.setPositiveButton("Input",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(), editText.getText().toString(), Toast.LENGTH_SHORT);
                                        String str = text_list.getText().toString();
                                        memo = editText.getText().toString();
                                        str +=  String.format("%d. %s %s\n",count,nowTime,memo);
                                        text_list.setText(str);

                                    }
                                });
                        builder.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder.show();
                        count++;
                        break;
                    case pause:
                        //handler stop
                        handler.removeMessages(0);

                        btn_tStart.setText("START");
                        btn_check.setText("CHECK");
                        text_timer.setText("00:00:00");

                        status = init;
                        count = 1;
                        text_list.setText("");
                        btn_check.setEnabled(false);
                        break;
                }
                break;
            case R.id.btn_1st:
                switch (status){
                    case run:
                        nowTime = getTimeOut();
                        // 다이얼로그
                        final EditText editText = new EditText(this);
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Memo");
                        builder.setMessage("No Memo Click input");
                        builder.setView(editText);

                        builder.setPositiveButton("Input",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(), editText.getText().toString(), Toast.LENGTH_SHORT);
                                        String str = text_list.getText().toString();
                                        memo = editText.getText().toString();
                                        str +=  String.format("%d. %s %s %s\n",count,nowTime,pop1,memo);
                                        text_list.setText(str);

                                    }
                                });
                        builder.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder.show();
                        count++;
                        break;
                    case pause:

                }
                break;
            case R.id.btn_2st:
                switch (status){
                    case run:
                        nowTime = getTimeOut();
                        // 다이얼로그
                        final EditText editText = new EditText(this);
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Memo");
                        builder.setMessage("No Memo Click input");
                        builder.setView(editText);

                        builder.setPositiveButton("Input",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(), editText.getText().toString(), Toast.LENGTH_SHORT);
                                        String str = text_list.getText().toString();
                                        memo = editText.getText().toString();
                                        str +=  String.format("%d. %s %s %s\n",count,nowTime,pop2,memo);
                                        text_list.setText(str);

                                    }
                                });
                        builder.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder.show();
                        count++;
                        break;
                    case pause:
                }
                break;
            case R.id.btn_save:

                if (text_list.length()!=0) {
                    final SQLiteDatabase mDatabase = openOrCreateDatabase("timer.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

                    //테이블 삭제
//                mDatabase.execSQL("DROP TABLE IF EXISTS roasting;");
//                Log.i("delete successed","ok");


                    AlertDialog.Builder ab = new AlertDialog.Builder(context);

                    //제목셋팅
                    ab.setTitle("Timer Save");
                    //AlertDialog 셋팅
                    ab.setMessage("Are you save?")
                            .setCancelable(false)
                            .setPositiveButton("Save",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //프로그래 닫힘

                                            long rNow = System.currentTimeMillis();
                                            //현재 시간을 date변수에 저장한다.
                                            Date date = new Date(rNow);
                                            //시간을 나타낼 포맷을 정한다 (yyyy/MM/dd 같은 형태로 변형 가능)
                                            java.text.SimpleDateFormat sdfNow = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                            //nowDate 변수에 값을 저장한다
                                            String rdate = sdfNow.format(date);
                                            String memo = " ";

                                            ContentValues values = new ContentValues();
                                            values.put("origin", String.valueOf(et_origin.getText()));
                                            values.put("rdate", String.valueOf(rdate));
                                            values.put("temp", String.valueOf(et_tem.getText()));
                                            values.put("humi", String.valueOf(et_humi.getText()));
                                            values.put("record", String.valueOf(text_list.getText()));
                                            values.put("end", String.valueOf(text_timer.getText()));
                                            values.put("memo",String.valueOf(memo));
                                            long rowNum = mDatabase.insert("roasting", null, values);
                                            Log.i("testing", "insert rowNum>>>" + rowNum);
                                            Intent intent = new Intent(TimerActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            Toast.makeText(getApplicationContext(),"Save Successed.",Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    })
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


                    break;
                } else {
                    Toast.makeText(getApplicationContext(),"There is no values",Toast.LENGTH_LONG).show();
                    break;
                }
            case R.id.btn_cancel:

                AlertDialog.Builder ab = new AlertDialog.Builder(context);

                //제목셋팅
                ab.setTitle("Timer Finish");
                //AlertDialog 셋팅
                ab.setMessage("Are you finish?")
                        .setCancelable(false)
                        .setPositiveButton("Finish",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //프로그래 닫힘
                                        TimerActivity.this.finish();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                })
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


//                Intent intent = new Intent(TimerActivity.this, MainActivity.class);
//                startActivity(intent);
                break;


        } //end swich




    } //end onClick

}//end class

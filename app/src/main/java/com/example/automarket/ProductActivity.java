package com.example.automarket;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class ProductActivity extends AppCompatActivity {

    private Socket socket;
    PrintWriter out;

    private String ip = "70.12.225.218"; //server연결 ip
    private int port = 7848; //server연결 ip

    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd");
    String formatDate = sdfNow.format(date);

    TextView dateNow;
    String api_url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("ProductLog", "productAvtivity nocreate()");
        api_url = Helper.getMetaData(this, "api_url");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_product);

        dateNow = (TextView) findViewById(R.id.dateNow);
        dateNow.setText(formatDate);    // TextView 에 현재 시간 문자열 할당

        Intent i = getIntent();
        String searchKeyword = i.getStringExtra("searchKeyword");

        if (searchKeyword != null) {
            Intent send = new Intent();
            ComponentName cname = new ComponentName("com.example.automarket",
                    "com.example.automarket.SearchService");
            send.setComponent(cname);
            send.putExtra("searchKeyword", searchKeyword);
            send.putExtra("api_url",api_url);
            startService(send);
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    setSocket(ip,port);
                    String data = "T/10000000/1";
                    out.println(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run () {
                        String data = "T/10000104/1000007/1";
                        out.println(data);
                        String exit = "/EXIT/";
                        out.println(exit);
                    }
                });
                thread.start();


                Toast.makeText(getApplicationContext(),"감사합니다.", Toast.LENGTH_SHORT).show();


                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.example.automarket",
                        "com.example.automarket.MainActivity");
                i.setComponent(cname);
                startActivity(i);
            }
        });
    }

    public void setSocket(String ip, int port) throws IOException {
        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("ProductLog", "호출호출!!");
        ArrayList<ProductVO> result = intent.getParcelableArrayListExtra("resultData");
        Log.i("ProductLog", "데이터가 정상적으로 Activity에 도달!!");
        Log.i("resulttest", result.toString());

        final ListView productListView = (ListView)findViewById(R.id.product);

        ProductListViewAdapter adapter = new ProductListViewAdapter();
        for(ProductVO vo : result) {
            adapter.addlitem(vo);
        }
        productListView.setAdapter(adapter);
        // intent에서 데이터 추출해서 ListView에 출력하는 작업을 진행
        // 만약 그림까지 포함할려면 추가적인 작업이 더 들어가야 한다.
        // ListVoew에 도서 제목만 일단 먼저 출력해 보고
        // 성공하면 CustomListView를 이용해서 이미지, 제목, 저자, 가격 등의 데이터를 출력
    }
}

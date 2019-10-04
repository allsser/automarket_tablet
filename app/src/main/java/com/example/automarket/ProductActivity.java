package com.example.automarket;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class ProductActivity extends AppCompatActivity {

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    PrintWriter out;

    private String ip = "70.12.225.218";
    private int port = 7848;

    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd");
    String formatDate = sdfNow.format(date);

    TextView dateNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product);

        dateNow = (TextView) findViewById(R.id.dateNow);
        dateNow.setText(formatDate);    // TextView 에 현재 시간 문자열 할당

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    setSocket(ip,port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String data = "/10000104/1000007/1";
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
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}

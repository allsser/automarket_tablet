package com.example.automarket;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class SearchService extends Service {
    private String api_url =  "";//"70.12.115.50:7777/automarket";
    private class ProductSearchRunnable implements Runnable {
        private String keyword;
        ProductSearchRunnable(String keyword) {
            this.keyword = keyword;
        }
        @Override
        public void run(){
            String url = api_url+"/api/order/detail.do?rkey=";
            url = url + keyword;

           // Log.i("연습1",api_url.toString());
            Log.i("연습2",url.toString());

            try {
                URL urlObj = new URL(url);
                HttpURLConnection con = (HttpURLConnection)urlObj.openConnection();
                con.setRequestMethod("GET");

                BufferedReader br =
                        new BufferedReader(new InputStreamReader(con.getInputStream()));
                String input = null;
                StringBuffer sb = new StringBuffer();
                while ((input = br.readLine()) != null) {
                    sb.append(input);
                }
                br.close();
                Log.i("ProductLog", sb.toString());

                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> map =
                        mapper.readValue(sb.toString(), new TypeReference<Map<String, Object>>() {});

                Object obj = map.get("orderdetail");

                String resultJsonData = mapper.writeValueAsString(obj);

                ArrayList<ProductVO> myObject =
                        mapper.readValue(resultJsonData, new TypeReference<ArrayList<ProductVO>>(){});

                for(ProductVO vo : myObject) {
                    vo.byteFromURL(api_url);
                }

                Intent i = new Intent(getApplicationContext(), ProductActivity.class);

                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                Log.i("연습4",myObject.toString());
                String test = Integer.toString(myObject.size());
                Log.i("사이즈",test.toString());

                //i.putParcelableArrayListExtra("resultData",myObject);
                i.putParcelableArrayListExtra("resultData",myObject);
                startActivity(i);


            } catch (Exception e) {
                Log.i("DATAError", e.toString());
            }
        }
    }
    public SearchService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("productLog","onStartCommand 호출!!");

        String keyword = intent.getStringExtra("searchKeyword");

        api_url = intent.getExtras().getString("api_url");
        ProductSearchRunnable runnable = new ProductSearchRunnable(keyword);
        Thread t = new Thread(runnable);
        t.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
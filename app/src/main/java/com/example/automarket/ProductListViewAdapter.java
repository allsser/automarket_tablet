package com.example.automarket;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ProductListViewAdapter extends BaseAdapter {
    private List<ProductVO> list = new ArrayList<ProductVO>();

    public void addlitem(ProductVO vo) {list.add(vo);}

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i){
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {return i;}

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if(view == null) {
            LayoutInflater inflater =
                    (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            //view = inflater.inflate(R.layout.simple_list_item_1,viewGroup,false);
            view = inflater.inflate(R.layout.product_item, viewGroup, false);
        }


        ImageView iv = (ImageView)view.findViewById(R.id.customIv);
        TextView tv1 = (TextView)view.findViewById(R.id.customTv1);
        TextView tv2 = (TextView)view.findViewById(R.id.customTv2);

        final ProductVO vo = list.get(i);

        Log.i("출력 test",vo.getProdnm().toString());

//        iv.setImageDrawable(vo.getDrawable());
       // Glide.with(context).load(vo.getDrawable()).into(iv);
        tv1.setText("상품이름:"+vo.getProdnm());
        tv2.setText("상품가격:"+vo.getProdprice());

        if(vo.getThumbnailimg() !=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(vo.getThumbnailimg(), 0, vo.getThumbnailimg().length);
            iv.setImageBitmap(bitmap);
        }

        return view;
    }
}

package com.example.rx;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

public class MySimpleAdapter extends SimpleAdapter {
    public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    @Override
    public void setViewImage(ImageView v, String value) {
        super.setViewImage(v, value);

        v.setBackgroundColor(Color.GREEN);
    }
}

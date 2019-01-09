package com.myreevuuCoach.holder;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.myreevuuCoach.R;


public class ChatHolderHeader {
    public TextView txtHeaderText;

    public ChatHolderHeader(Context con, View view, int width) {
        // TODO Auto-generated constructor stub
        txtHeaderText = (TextView) view.findViewById(R.id.txtHeaderText);
        txtHeaderText.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (width * 0.04));
        txtHeaderText.setPadding(width / 28, width / 64, width / 28, width / 64);
    }

    public void bindHolder(String text) {
        txtHeaderText.setText(text);
    }
}

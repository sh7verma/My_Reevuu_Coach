//package com.myreevuuCoach.dialog;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.util.DisplayMetrics;
//import android.view.Gravity;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ListView;
//
//import com.myreevuuCoach.R;
//
//import java.util.ArrayList;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class OptionSelection extends Activity {
//
//    int mScreenwidth, mScreenheight;
//
//    @BindView(R.id.list_options)
//    ListView mListOptions;
//    ArrayList<String> mData;
//    String mSelected;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        WindowManager.LayoutParams wmlp = this.getWindow().getAttributes();
//
//        wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
//        this.setFinishOnTouchOutside(true);
//        setContentView(R.layout.dialog_optionselection);
//        getDefaults();
//        getWindow().setLayout((mScreenwidth), (int) (mScreenheight * 0.5));
//        ButterKnife.bind(this);
//
//        mData = new ArrayList<>();
//        mData = getIntent().getStringArrayListExtra("data");
//
//        mSelected = getIntent().getStringExtra("selected");
//
//        mListOptions.setAdapter(new BankOptionsAdapter(OptionSelection.this, mData, mSelected));
//
//        mListOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent in = new Intent();
//                in.putExtra("selected_data", mData.get(position));
//                setResult(RESULT_OK, in);
//                finish();
//            }
//        });
//    }
//
//    @OnClick(R.id.ll_options)
//    void outSideTouch() {
//        finish();
//    }
//
//    void getDefaults() {
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        mScreenwidth = dm.widthPixels;
//        mScreenheight = dm.heightPixels;
//    }
//}
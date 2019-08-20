package com.arisaid.calendarview;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.github.airsaid.calendarview.widget.CalendarView;
import com.loonggg.bottomsheetpopupdialoglib.ShareBottomPopupDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
public class MainActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();
    private CalendarView mCalendarView;
    private TextView mTxtDate;
    private PopupWindow popupWindow1;
    private View dialogView;
    private TextView textView;
    private RadioGroup select_group;
    private ShareBottomPopupDialog shareBottomPopupDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTxtDate = (TextView) findViewById(R.id.txt_date);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        // 设置已选的日期
        mCalendarView.setSelectDate(initData());
        // 指定显示的日期, 如当前月的下个月
        Calendar calendar = mCalendarView.getCalendar();
        calendar.add(Calendar.MONTH, 0);
        mCalendarView.setCalendar(calendar);
        // 设置字体
        mCalendarView.setTypeface(Typeface.SERIF);
        initDialog();
        //设置日期状态改变监听
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, boolean select, int year, int month, int day) {
                if (select == true) {
                    shareBottomPopupDialog.showPopup(dialogView);
                }
//                popup1();
//                View v = View.inflate(MainActivity.this,R.layout.alert_selectday,null);
//                popupWindow1.showAtLocation(v, Gravity.BOTTOM, 0, 0);
            }
        });
        // 设置是否能够改变日期状态
        mCalendarView.setChangeDateStatus(true);
        // 设置日期点击监听
        mCalendarView.setOnDataClickListener(new CalendarView.OnDataClickListener() {
            @Override
            public void onDataClick(@NonNull CalendarView view, int year, int month, int day) {
                textView.setText("您选择的时间是：" + year + "年" + (month + 1) + "月" + day + "日");
                initLinsenter();
            }
        });
        // 设置是否能够点击
        mCalendarView.setClickable(true);

        setCurDate();
    }

    private void initDialog() {
        dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_selectday, null);
        initView();
        shareBottomPopupDialog = new ShareBottomPopupDialog(MainActivity.this, dialogView);
    }

    private void initView() {
        textView = dialogView.findViewById(R.id.text_date);
        select_group = dialogView.findViewById(R.id.select_group);
    }
    private void initLinsenter() {

        select_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_morning:
                        mCalendarView.setCheckedStatus(0);
                        shareBottomPopupDialog.dismiss();
                        break;
                    case R.id.radio_allday:
                        mCalendarView.setCheckedStatus(1);
                        shareBottomPopupDialog.dismiss();
                        break;
                    case R.id.radio_afternoon:
                        shareBottomPopupDialog.dismiss();
                        mCalendarView.setCheckedStatus(2);
                        break;
                }
            }
        });
    }

    private void popup1() {
        View v = View.inflate(this, R.layout.alert_selectday, null);
//        ImageView imageView = v.findViewById(R.id.notice_pp_xl);
//        notice_rv = v.findViewById(R.id.notice_rv);
        popupWindow1 = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,210, true);
        popupWindow1.setFocusable(true);
        popupWindow1.setTouchable(true);
        popupWindow1.setBackgroundDrawable(new BitmapDrawable());

    }
    private List<String> initData() {
        List<String> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        SimpleDateFormat sdf = new SimpleDateFormat(mCalendarView.getDateFormatPattern(), Locale.CHINA);
        sdf.format(calendar.getTime());
        dates.add(sdf.format(calendar.getTime()));
        return dates;
    }

    public void next(View v){
        mCalendarView.nextMonth();
        setCurDate();
    }

    public void last(View v){
        mCalendarView.lastMonth();
        setCurDate();
    }

    private void setCurDate(){
        mTxtDate.setText(mCalendarView.getYear() + "年" + (mCalendarView.getMonth() + 1) + "月");
    }
}

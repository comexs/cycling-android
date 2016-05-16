package com.alex.cycling.ui.person;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alex.cycling.R;
import com.alex.cycling.base.BaseActivity;
import com.alex.cycling.ui.widget.PhotoChoice;
import com.alex.cycling.utils.BaiduLocationUtils;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/16.
 */
public class PersonActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, PhotoChoice.ChoiceListener {

    public static final String DATEPICKER_TAG = "选择日期";

    @Bind(R.id.avatar)
    SimpleDraweeView avatar;
    @Bind(R.id.username)
    TextView username;
    @Bind(R.id.sex)
    TextView sex;
    @Bind(R.id.sex_view)
    FrameLayout sexView;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.address_view)
    FrameLayout addressView;
    @Bind(R.id.birthday)
    TextView birthday;
    @Bind(R.id.birthday_view)
    FrameLayout birthdayView;

    PhotoChoice photoChoice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.avatar, R.id.sex_view, R.id.address_view, R.id.birthday_view})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.avatar:
                if (null == photoChoice) {
                    photoChoice = new PhotoChoice(this, this);
                }
                photoChoice.setAvatar();
                break;
            case R.id.sex_view:

                break;
            case R.id.address_view:
                BaiduLocationUtils.instance.setListener(new BDLocationListener() {
                    @Override
                    public void onReceiveLocation(final BDLocation bdLocation) {
                        if (bdLocation == null || address == null) {
                            return;
                        }
                        address.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (null != address)
                                    address.setText(String.format("%s.%s", bdLocation.getProvince(), bdLocation.getCity()));
                            }
                        }, 1000);
                    }
                });
                BaiduLocationUtils.instance.start(this);
                address.setText("定位中...");
                break;
            case R.id.birthday_view:
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCancelable(true);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        birthday.setText(String.format("%s.%s.%s", year, month, day));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != photoChoice) photoChoice.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void choiceSuccess(String uri) {
        avatar.setImageURI(Uri.parse("file://" + uri));
    }
}

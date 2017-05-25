package cn.edu.zucc.cql.myapplication;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

public class SettingActivity extends AppCompatActivity {
    private Switch mSwitch;
    private UiModeManager mUiModeManager = null;
    int flag=0;
    private LinearLayout lay;
    private RelativeLayout rel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar3);
        toolbar.setTitle("设置");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mUiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        mSwitch=(Switch) findViewById(R.id.sw1);
        rel=(RelativeLayout) findViewById(R.id.re2);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences preferences=getSharedPreferences("user",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                if (isChecked) {
                    mUiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
                    flag=1;
                } else{
                    mUiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
                    flag=0;
                }
                editor.putInt("flag", flag);
                editor.commit();
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        int flag=preferences.getInt("flag",2);
        if(flag==1){
            mSwitch.setChecked(true);
        }
        else
            mSwitch.setChecked(false);
    }
}

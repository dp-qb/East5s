package com.east5s;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.*;
import android.content.*;
import android.widget.*;

public class SettingsActivity extends PreferenceActivity
{
	private PreferenceManager d当前界面;
	//private SharedPreferences.Editor preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_layout);
		d当前界面 = getPreferenceManager();
		SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
		findPreference("壁纸前台音量").setSummary(sp.getString("壁纸前台音量", "默认0.01"));
		findPreference("壁纸后台音量").setSummary(sp.getString("壁纸后台音量", "默认0.001"));
		findPreference("视频宽高").setSummary(sp.getString("视频宽高", "默认自适应"));
		findPreference("时间格式").setSummary(sp.getString("时间格式", "默认HHmmss"));
		findPreference("时间文字大小").setSummary(sp.getString("时间文字大小", "默认120"));
		findPreference("时间文字颜色").setSummary(sp.getString("时间文字颜色", "请输入16进制颜色值，默认为“#66CCFF”"));
		findPreference("时间文字颜色混合模式").setSummary(sp.getString("时间文字颜色混合模式", "默认覆盖"));
		findPreference("FPS").setSummary(sp.getString("FPS", "默认每秒绘制30帧"));
		findPreference("视频路径").setSummary(sp.getString("视频路径", "如果不想给存储权限，不要设置并手动把视频复制到“/sdcard/Android/data/com.east5s/files/GIF”目录下"));
		findPreference("音乐路径").setSummary(sp.getString("音乐路径", "如果不想给存储权限，不要设置并手动把音乐复制到“/sdcard/Android/data/com.east5s/files/MP3”目录下"));
		//preferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).edit();
		d当前界面.findPreference("时间与视频").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					MyWallPaPer.s设置壁纸(getApplicationContext());
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		d当前界面.findPreference("壁纸前台音量").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					MyWallPaPer.s设置壁纸(getApplicationContext());
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		d当前界面.findPreference("壁纸后台音量").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					MyWallPaPer.s设置壁纸(getApplicationContext());
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		d当前界面.findPreference("视频宽高").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					MyWallPaPer.s设置壁纸(getApplicationContext());
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		d当前界面.findPreference("时间格式").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					MyWallPaPer.s设置壁纸(getApplicationContext());
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		d当前界面.findPreference("时间文字大小").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					MyWallPaPer.s设置壁纸(getApplicationContext());
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		d当前界面.findPreference("时间文字颜色").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					MyWallPaPer.s设置壁纸(getApplicationContext());
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		d当前界面.findPreference("时间文字颜色混合模式").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					MyWallPaPer.s设置壁纸(getApplicationContext());
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		d当前界面.findPreference("FPS").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					MyWallPaPer.s设置壁纸(getApplicationContext());
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		d当前界面.findPreference("背景色").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					MyWallPaPer.s设置壁纸(getApplicationContext());
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		d当前界面.findPreference("视频路径").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					MyWallPaPer.s设置壁纸(getApplicationContext());
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		d当前界面.findPreference("音乐路径").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					MyWallPaPer.s设置壁纸(getApplicationContext());
					被点击的控件.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
		d当前界面.findPreference("key_ok").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
				@Override
				public boolean onPreferenceClick(Preference 被点击的控件)
				{
					MyWallPaPer.g关闭壁纸(getApplicationContext());
					MyWallPaPer.s设置壁纸(getApplicationContext());
					return true;
				}
			});
		d当前界面.findPreference("key_del").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
				@Override
				public boolean onPreferenceClick(Preference 被点击的控件)
				{
					MyWallPaPer.g关闭壁纸(getApplicationContext());
					return true;
				}
			});
		d当前界面.findPreference("key_help").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				@Override
				public boolean onPreferenceChange(Preference 被点击的控件, Object objValue)
				{
					//preference.setSummary(String.valueOf(objValue));
					return true; // 保存更新值
				}
			});
			
    }

}

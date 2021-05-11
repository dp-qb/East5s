package com.east5s;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import java.io.IOException;
import java.io.*;
import java.util.*;
import android.content.pm.*;
import android.graphics.*;
import java.text.*;
import android.view.*;
import android.util.*;
import android.os.*;
import android.app.*;
import android.widget.*;
import android.widget.Gallery.*;
import android.graphics.Movie;
import java.time.format.*;
import java.time.*;
import android.media.*;
import android.media.AudioManager.*;
import android.text.*;
public class MyWallPaPer extends WallpaperService
{
	private static final String SERVICE_NAME = "com.east5s.MyWallPaPer";
	@Override
	public Engine onCreateEngine()
	{
		return new GifEngine();
	}
	public static void s设置壁纸(Context context)
	{//准备墙纸的视频
		WallpaperInfo info = WallpaperManager.getInstance(context).getWallpaperInfo();
		if (info != null && SERVICE_NAME.equals(info.getServiceName()))
		{//是更换视频
			Intent intent = new Intent();
			intent.setAction("action");
			intent.putExtra("broadcast_set_video_param", "");
			context.sendBroadcast(intent);//发送标准广播
		}
		else
		{//是第一次打开
			Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(context, MyWallPaPer.class));
			context.startActivity(intent);//启动另一个程序的界面
		}
	}
	public static void g关闭壁纸(Context context)
	{//关闭壁纸
		try
		{
			WallpaperManager.getInstance(context).clear();
		}
		catch (IOException e)
		{}
	}
	private static int random(int min, int max)
	{//随机数
		Random random = new Random();
		return random.nextInt(max) % (max - min + 1) + min;
	}
	private String getVideoPath(String url)
	{//获取路径下随机文件
		String fileAbsolutePath=null;
		try
		{
			File file = new File(url);
			if (file.exists())
			{
				String[] files = file.list();  
				fileAbsolutePath = url + "/" + files[random(0, files.length)];
				//Toast.makeText(MyWallPaPer.this, "正在加载视频："+fileAbsolutePath, Toast.LENGTH_LONG).show();
			}
			else
			{
				//Toast.makeText(MyWallPaPer.this, "视频加载失败，显示默认视频。", Toast.LENGTH_LONG).show();
			}
		}
		catch (Exception e)
		{
			Toast.makeText(MyWallPaPer.this, "加载文件路径时出现异常：" + url, Toast.LENGTH_LONG).show();
		}
		return fileAbsolutePath;
	}
	private class GifEngine extends Engine 
	{
		private boolean 时间与视频;//是否循环播放
		private String fit="适应屏幕";//视频如何适配屏幕
		private int p屏宽,p屏高;//屏幕宽高
		DateTimeFormatter s时间格式化;
		TextPaint s时间文字画笔 = new TextPaint();
		Paint b背景画笔=new Paint();
		Handler x线程管理器 = new Handler();
		private Movie GIF; //Gif对象
		private int mCurrentAnimationTime = 0; //Gif该播放第几帧
		private int Z帧间隔=100;//每次间隔多少毫秒绘制下一帧
		int FPS=5;
		private MediaPlayer mp;//背景音乐
		private String GIF文件夹,MP3文件夹;
		float 前台音量,后台音量;
        private final Runnable 绘制线程 = new Runnable() {
            @Override
            public void run()
			{
                一帧();
            }
        };
		void 初始化()
		{
			SharedPreferences s数据持久化 = PreferenceManager.getDefaultSharedPreferences(MyWallPaPer.this);
			s时间格式化 = DateTimeFormatter.ofPattern(s数据持久化.getString("时间格式", "Hmmss"));
			时间与视频 = s数据持久化.getBoolean("时间与视频", false);
			fit = s数据持久化.getString("视频宽高", "适应屏幕");
			GIF文件夹 = s数据持久化.getString("视频路径", "/sdcard/Android/data/com.east5s/files/GIF");
			MP3文件夹 = s数据持久化.getString("音乐路径", "/sdcard/Android/data/com.east5s/files/MP3");
			前台音量 = Float.parseFloat(s数据持久化.getString("壁纸前台音量", "0.01"));
			后台音量 = Float.parseFloat(s数据持久化.getString("壁纸后台音量", "0.001"));
			int 时间文字颜色=Color.parseColor(s数据持久化.getString("时间文字颜色", "#66ccff"));
			String 时间画笔混合模式=s数据持久化.getString("时间文字颜色混合模式", "叠加");
			int size = Integer.parseInt(s数据持久化.getString("时间文字大小", "120"));
			FPS = Integer.parseInt(s数据持久化.getString("FPS", "30"));
			Z帧间隔 = 1000 / FPS;
			String bgColor=s数据持久化.getString("背景色", "");
			if (bgColor.length() > 15)
			{
				b背景色 = bgColor.split("\n");
			}
			//  初始化画笔
			s时间文字画笔.setColor(时间文字颜色);
			s时间文字画笔.setFakeBoldText(true); 
			s时间文字画笔.setAntiAlias(true);//抗锯齿
			s时间文字画笔.setTextSize(size); //以px为单位
			b背景画笔.setColor(Color.RED);
			switch (时间画笔混合模式)
			{
				case "覆盖": {
						s时间文字画笔.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
						break;
					}
				case "加深": {
						s时间文字画笔.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
						break;
					}
				case "变亮": {
						s时间文字画笔.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
						break;
					}
				case "叠加": {
						s时间文字画笔.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
						break;
					}
				case "透明": {
						s时间文字画笔.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
						break;
					}
				case "删除颜色": {
						s时间文字画笔.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
						break;
					}
				default:{
						Toast.makeText(MyWallPaPer.this, "未知画笔模式!", Toast.LENGTH_LONG).show();
						break;
					}
			}
			// 初始化音乐资源
			if (null == mp)
			{
				if ((前台音量 <= 0) & (后台音量 <= 0))
				{
					Toast.makeText(MyWallPaPer.this, "前后台音量都为零，不再加载音乐。", Toast.LENGTH_LONG).show();
				}
				else
				{
					try
					{
						// 创建MediaPlayer对象
						mp = new MediaPlayer();
						mp.setDataSource(getVideoPath(MP3文件夹));
						// 在MediaPlayer取得播放资源与stop()之后要准备PlayBack的状态前一定要使用MediaPlayer.prepeare()
						mp.prepareAsync();//准备资源
					} 
					catch (IllegalStateException e)
					{} 
					catch (IOException e)
					{}
					// 装载完毕回调
					mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {                    
							@Override
							public void onPrepared(MediaPlayer mp)
							{
								mp.setVolume(后台音量, 后台音量);
								mp.start();
								//Toast.makeText(MyWallPaPer.this, "装载完成开始播放。", Toast.LENGTH_LONG).show();
							}
						});
					// 音乐播放完毕的事件处理
					mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							public void onCompletion(MediaPlayer mp)
							{
								String ui=getVideoPath(MP3文件夹);
								//Toast.makeText(MyWallPaPer.this, "播放完毕，新的视频："+ui, Toast.LENGTH_LONG).show();
								try
								{
									mp.reset();
									mp.setDataSource(ui);
									mp.prepareAsync();//准备资源
								} 
								catch (IllegalStateException e)
								{}
								catch (IOException e)
								{}
							}
						});
					// 播放音乐时发生错误的事件处理
					mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
							public boolean onError(MediaPlayer mp, int what, int extra)
							{
								String ui=getVideoPath(MP3文件夹);
								try
								{
									mp.reset();
									mp.setDataSource(ui);
									mp.prepareAsync();//准备资源
									Toast.makeText(MyWallPaPer.this, "播放错误，新的视频：" + ui, Toast.LENGTH_LONG).show();
								} 
								catch (IllegalStateException e)
								{}
								catch (IOException e)
								{}
								return false;
							}
						});
				}
			}
			else
			{
				if ((前台音量 <= 0) & (后台音量 <= 0))
				{
					mp.release();
					Toast.makeText(MyWallPaPer.this, "前后台音量都为零，释放音乐占用的内存。", Toast.LENGTH_LONG).show();
				}
				else
				{mp.setVolume(后台音量, 后台音量);}

			}
		}
		//接收广播
		private BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
			{
				初始化();
            }
        };

        @Override
        public void onCreate(SurfaceHolder surfaceHolder)
		{
			//创建引擎时调用
            super.onCreate(surfaceHolder);
            IntentFilter filter = new IntentFilter();
            filter.addAction("action");
            registerReceiver(mReceiver, filter);//注册动态广播
			初始化();
        }
        @Override
        public void onDestroy()
		{
			//销毁引擎时调用
            super.onDestroy();
            unregisterReceiver(mReceiver);//撤销动态广播
			if (mp != null)mp.release();// 释放资源
			//Toast.makeText(MyWallPaPer.this, "销毁视频壁纸引擎!", Toast.LENGTH_LONG).show();
        }
		@Override
		public void onVisibilityChanged(boolean visible)
		{//能否看到壁纸
			if (visible)
			{
				x线程管理器.postDelayed(绘制线程, Z帧间隔);//启动绘制线程
				if (null != mp)
				{
					if (前台音量 <= 0)
					{
						if (mp.isPlaying())
						{mp.pause();}
						else
						{}
					}
					else
					{
						mp.setVolume(前台音量, 前台音量);
						if (mp.isPlaying())
						{}
						else
						{mp.start();}
					}
				}
			}
			else
			{
				x线程管理器.removeCallbacks(绘制线程);//停止绘制时间
				if (null != mp)
				{
					if (后台音量 <= 0)
					{
						if (mp.isPlaying())
						{mp.pause();}
						else
						{}
					}
					else
					{
						mp.setVolume(后台音量, 后台音量);
						if (mp.isPlaying())
						{}
						else
						{mp.start();}
					}
				}
			}
		}	
		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height)
		{p屏宽 = width;p屏高 = height;}//屏幕大小变化

		public class j加载文件的线程 extends Thread
		{
			public void run()
			{
				File file = new File(getVideoPath(GIF文件夹));
				BufferedInputStream is = null;
				int size_ = (int)file.length();
				if (size_ <= 0)
				{throw new IllegalArgumentException("文件读取失败(大小为零)：" + file);}
				try
				{
					is = new BufferedInputStream(new FileInputStream(file), size_);
				}
				catch (FileNotFoundException e)
				{
					throw new IllegalArgumentException("没有读取到有效的文件：" + file);
				}
				is.mark(size_);
				GIF = Movie.decodeStream(is);
				加载中 = false;
			}
		}
		boolean 加载中=false,GIF就绪=false;
		long GIF开始播放第一帧的时间=0; //Gif啥时候开始播放的？
		int 变动的时间 ;
		String[] b背景色=null;
		String bg="#000000";
		int g过渡比例=1;
		String old背景色 ="#000000";
		int f方向=0;
        private void 一帧()
		{//进行一帧绘制
			long d当前时间毫秒 = System.currentTimeMillis();
			int s秒= LocalTime.now().getSecond();
			int ss=s秒 % 10;
			SurfaceHolder holder = getSurfaceHolder();
			Canvas c = holder.lockCanvas();
			c.save();  
			if (变动的时间 != ss)//下一秒
			{
				变动的时间 = ss;
				if (b背景色 != null)
				{
					b背景画笔.setColor(Color.parseColor(old背景色));
					old背景色 = bg;
					bg = b背景色[random(0, b背景色.length)];
				}
				else
				{
					b背景画笔.setColor(Color.parseColor(old背景色));
					old背景色 = bg;
					Random random = new Random();
					bg = String.format("#%06x", random.nextInt(16777215) % (16777216));
				}
				g过渡比例 = 1;
				f方向++;
				if (f方向 == 4)f方向 = 0;
			}
			if (GIF就绪)c.drawColor(Color.BLACK);//绘制背景
			else
			{
				c.drawColor(Color.parseColor(old背景色));//绘制背景
				g过渡比例++;
				//c.drawRect(new Rect(0,0,p屏宽, p屏高*(g过渡比例/FPS)), b背景画笔);  
				switch (f方向)//上左下右上
				{
					case 0: {
							c.drawRect(new Rect(0, 0, p屏宽, p屏高 - p屏高 * 2 / FPS * g过渡比例), b背景画笔);//上到下
							break;
						}
					case 1: {
							c.drawRect(new Rect(0, 0, p屏宽 - p屏宽 * 2 / FPS * g过渡比例, p屏高), b背景画笔);//左到右
							break;
						}
					case 2: {
							c.drawRect(new Rect(0, p屏高 * 2 / FPS * g过渡比例, p屏宽, p屏高), b背景画笔);//下到上
							break;
						}
					case 3: {
							c.drawRect(new Rect(p屏宽 * 2 / FPS * g过渡比例, 0, p屏宽, p屏高), b背景画笔);//右到左
							break;
						}
					default:{
							Toast.makeText(MyWallPaPer.this, "色彩过渡方向异常!", Toast.LENGTH_LONG).show();
							break;
						}
				}
			}
			if (GIF == null)
			{
				if (加载中 == false)
				{
					//创建加载文件线程
					new j加载文件的线程().start();
					加载中 = true;
				}
				else
				{
					time_show(c);
				}
			}
			else
			{
				if (GIF就绪)
				{
					// 如果第一帧，记录起始时间 
					if (GIF开始播放第一帧的时间 <= 0)
					{ GIF开始播放第一帧的时间 = d当前时间毫秒; } 
					// 取出动画的时长 
					int d动画总时长 = GIF.duration(); 
					//if (d动画总时长 <= 0){ d动画总时长 = 5000;} 
					// 算出需要显示第几帧 
					mCurrentAnimationTime = (int) ((d当前时间毫秒 - GIF开始播放第一帧的时间) % d动画总时长); 
					GIF.setTime(mCurrentAnimationTime); 
					if (d当前时间毫秒 < (GIF开始播放第一帧的时间 + (long)d动画总时长))
					{
						gifshow(fit, c);
					}
					else
					{
						GIF就绪 = false;
						GIF = null;
						GIF开始播放第一帧的时间 = 0;
					}
				}
				else
				{
					if (ss == 5)GIF就绪 = true;
					time_show(c);
				}
			}
			c.restore();  
			holder.unlockCanvasAndPost(c);
			//  调度下一次重绘
			x线程管理器.postDelayed(绘制线程, Z帧间隔);
		}
		public void time_show(Canvas can)
		{//进行一次时间绘制
			LocalDateTime now = LocalDateTime.now();
			String dataStr = s时间格式化.format(now);
			StaticLayout myStaticLayout = new StaticLayout(dataStr, s时间文字画笔, can.getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
			can.translate(0, (p屏高 - myStaticLayout.getHeight()) / 2);
			myStaticLayout.draw(can);
		}
		public void gifshow(String show, Canvas can)
		{//进行一次GIF绘制
			can.save();
			switch (show)
			{
				case "适应屏幕": {//根据屏幕大小计算缩放比例
						float saclex = (float) p屏宽 / (float) GIF.width() ;
						float sacley = (float) p屏高 / (float) GIF.height();
						if (Math.abs(saclex - sacley) > 0.5)//拉伸极限
						{
							if (saclex < sacley)
							{
								can.scale(saclex, saclex);
								float top=(p屏高 / saclex - GIF.height()) / 2;
								GIF.draw(can, 0, top);//画GIF
							}
							else
							{
								can.scale(sacley, sacley);
								float l=(p屏宽 / sacley - GIF.width()) / 2;
								GIF.draw(can, l, 0);//画GIF
							}
						}
						else
						{
							can.scale(saclex, sacley);//缩放画布
							GIF.draw(can, 0, 0);//画GIF
						}
						break;
					}
				case "原始大小": {//
						GIF.draw(can, p屏宽 / 2 - GIF.width() / 2, p屏高 / 2 - GIF.height() / 2); //画GIF
						break;
					}
				case "裁剪": {//
						//根据屏幕大小计算缩放比例
						float saclex = (float) p屏宽 / (float) GIF.width() ;
						float sacley = (float) p屏高 / (float) GIF.height();
						if (saclex > sacley)
						{
							can.scale(saclex, saclex);
							float top=(p屏高 / saclex - GIF.height()) / 2;
							GIF.draw(can, 0, top);//画GIF
						}
						else
						{
							can.scale(sacley, sacley);
							float l=(p屏宽 / sacley - GIF.width()) / 2;
							GIF.draw(can, l, 0);//画GIF
						}
						break;
					}
				case "拉伸": {//
						//根据屏幕大小计算缩放比例
						float saclex = (float) p屏宽 / (float) GIF.width() ;
						float sacley = (float) p屏高 / (float) GIF.height();
						can.scale(saclex, sacley);
						GIF.draw(can, 0, 0);
						break;
					}
				default:{
						Toast.makeText(MyWallPaPer.this, "未知模式!", Toast.LENGTH_LONG).show();
						break;
					}
			}
			if (时间与视频)//在GIF上面绘制时间
			{
				can.restore();
				time_show(can);
			}
		}
		//壁纸引擎类
	}
	//壁纸管理类
}

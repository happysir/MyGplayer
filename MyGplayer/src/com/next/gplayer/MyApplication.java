package com.next.gplayer;

import android.app.Application;
import android.os.Handler;
import android.view.WindowManager;

public class MyApplication extends Application {  
    
	private Handler  appHandler;
		
	public Handler getHandler() {
		return appHandler;
	}
 
 private WindowManager.LayoutParams wmParams=new WindowManager.LayoutParams();  
  
  
 public WindowManager.LayoutParams getMywmParams(){  
  return wmParams;  
 }  
  
}  
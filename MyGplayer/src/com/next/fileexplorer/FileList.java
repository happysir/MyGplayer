package com.next.fileexplorer;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.next.gplayer.GPlayer;


public class FileList extends Activity{
	
	private List<Map<String, String>> mVideoList;
	public FileUtils mFileUtils;
	public ListView fileListView;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		videoListAdapter();
		
		setContentView(0x7f030000);  
		fileListView =(ListView)findViewById(0x7f070003);
		
		SimpleAdapter adapter = new SimpleAdapter(this,mVideoList,
				android.R.layout.simple_list_item_1,new String[]{"videoname"},
				new int[]{android.R.id.text1});
		fileListView.setAdapter(adapter);

		fileListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String path = mVideoList.get(arg2).get("videopath");
				
				Intent intent = new Intent(FileList.this, GPlayer.class);
				intent.putExtra("playerpath", path);
				startActivity(intent);
				 
				
				 
			}
		});
			
		}
	private void videoListAdapter() {
		mFileUtils = new FileUtils();
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			File file = new File( "/sdcard");
			mFileUtils.searchFile(file);
			mVideoList=mFileUtils.getList();
		}
	}
	
	
	
	

}

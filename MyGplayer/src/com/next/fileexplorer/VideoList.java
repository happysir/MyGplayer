package com.next.fileexplorer;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.next.gplayer.GPlayer;
import com.next.gplayer.R;

public class VideoList extends Activity {  
    
    private Cursor cursor;  
      
    private ArrayList<VideoInfo> videoList = new ArrayList<VideoInfo>();
    
    private ListView videoListView;
    
    public void onCreate(Bundle savedInstanceState){  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.filelist);  
        
        videoListView=(ListView)findViewById(R.id.videofile);
        init();  
        
        videoListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent playIntent=new Intent(VideoList.this, GPlayer.class);
				String pathString=videoList.get(arg2).filePath;
				playIntent.putExtra("path", pathString);
				VideoList.this.startActivity(playIntent);
				Log.d("GPlayer", "VideoList"+System.currentTimeMillis());
			}
		});
    }  
      
      
    private void init(){  
        String[] thumbColumns = new String[]{  
                MediaStore.Video.Thumbnails.DATA,  
                MediaStore.Video.Thumbnails.VIDEO_ID  
        };  
          
        String[] mediaColumns = new String[]{  
                MediaStore.Video.Media.DATA,  
                MediaStore.Video.Media._ID,  
                MediaStore.Video.Media.TITLE,  
                MediaStore.Video.Media.MIME_TYPE  
        };  
          
           
        cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);  
          
       
          
        if(cursor.moveToFirst()){  
            do{  
                VideoInfo info = new VideoInfo();  
                  
                info.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));  
                info.mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));  
                info.title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));  
                  
                Log.d("-name debug-", info.title+"    "+info.filePath);
                
                  
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));  
                BitmapFactory.Options options = new BitmapFactory.Options();  
                options.inDither = false;  
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;  
                info.b = MediaStore.Video.Thumbnails.getThumbnail(getContentResolver(), id,  Images.Thumbnails.MICRO_KIND, options);                  
                  
                
                 
                videoList.add(info);  
                  
            }while(cursor.moveToNext());  
        }  
               
        VideoAdapter adapter = new VideoAdapter(this); 
        if(adapter==null){
        	Log.d("JMQ","(adapter==null");
        	return;
        }
        videoListView.setAdapter(adapter);  
    }  
      
    class VideoInfo{  
        String filePath;  
        String mimeType;  
        Bitmap b;  
        String title;  
    }  
      
    
    
    class ViewHolder{  
        ImageView thumbImage;  
        TextView titleText;  
    }  
 
    private class VideoAdapter extends BaseAdapter{  
        
        private Context mContext;  
        private LayoutInflater inflater;  
          
        public VideoAdapter(Context context){  
            this.mContext = context;  
            this.inflater = LayoutInflater.from(context);   
            this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);    
        }  
        @Override  
        public int getCount() {  
              
            return videoList.size();  
        }  
        @Override  
        public Object getItem(int p) {  
            
            return videoList.get(p);  
        }  
        @Override  
        public long getItemId(int p) {  
              
            return p;  
        }  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
            ViewHolder holder ;  
            if(convertView == null){  
                holder = new ViewHolder();  
                convertView = inflater.inflate(R.layout.row, null);  
                holder.thumbImage = (ImageView)convertView.findViewById(R.id.icon);  
                holder.titleText = (TextView)convertView.findViewById(R.id.videoName);  
                convertView.setTag(holder);  
            }else{  
                holder = (ViewHolder)convertView.getTag();  
            }  
              
           
            holder.titleText.setText(videoList.get(position).title+"."+(videoList.get(position).mimeType).substring(6));  
            if(videoList.get(position).b != null){  
                holder.thumbImage.setImageBitmap(videoList.get(position).b);  
            }  
              
            return convertView;  
        }  
          
    }  
}
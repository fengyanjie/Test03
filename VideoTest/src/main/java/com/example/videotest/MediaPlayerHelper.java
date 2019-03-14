package com.example.videotest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.example.videotest.CompressHelper.FileWriteDoneListener;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

public class MediaPlayerHelper {

	private String TAG = MediaPlayerHelper.class.getName();
	public static MediaPlayerHelper helper = null;
	private FileWriteDoneListener mListener = null;
	
	public static MediaPlayerHelper getIntance(){
		if(null == helper){
			synchronized (MediaPlayerHelper.class) {
				if(null == helper){
					helper = new MediaPlayerHelper();
				}
			}
		}
		return helper;
	}
	
	 /**
     * 保存视频截图.该方法只能支持本地视频文件
     * 
     * @param time视频当前位置
     */
    public void saveScreenShot(MediaPlayer mediaPlayer ,String videoPath,String savePath ,FileWriteDoneListener listener) {
        this.mListener = listener;
        long time = mediaPlayer.getCurrentPosition();
        if (time >= 0) {
            try {
                mediaPlayer.getCurrentPosition();
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(videoPath);
                // 获取视频的播放总时长单位为毫秒
                //String timeString = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                // 转换格式为微秒
                //long timelong = Long.parseLong(timeString) * 1000;
                //Log.e(TAG, "timeString:"+ timeString);
                Log.e(TAG, "mediaPlayer:"+ mediaPlayer.getDuration());
                long curPos1 = (mediaPlayer.getCurrentPosition()-3470);//2550(有些靠前)
                // 获取当前视频指定位置的截图,时间参数的单位是微秒,做了*1000处理
                // 第二个参数为指定位置，意思接近的位置截图
                Bitmap bitmap1= mediaMetadataRetriever.getFrameAtTime(curPos1 * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
                CompressHelper.getInstance().compressImageByPixel(savePath, bitmap1,mListener);
                // 释放资源
                mediaMetadataRetriever.release();
                listener.writeDone(true);
            } catch (Exception e) {
            	Log.e(TAG, e.getMessage());
            	listener.writeDone(false);
            }
        }
    }
}

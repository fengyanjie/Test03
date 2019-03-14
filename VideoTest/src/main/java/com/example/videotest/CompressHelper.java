package com.example.videotest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
/**
 * @Description: 处理Bitmap图片压缩(包括尺寸、质量的压缩)、保存 
 * @Author:zhangnan@voole.com
 * @Since:2015-11-6
 */
public class CompressHelper {
	private String TAG = CompressHelper.class.getName();
	public static CompressHelper helper = null;
	
	public static CompressHelper getInstance(){
		if(helper == null){
			synchronized (CompressHelper.class) {
				if(helper == null){
					helper = new CompressHelper();
				}
			}
		}
		return helper;
	}

	/**
	 * @Description: 多线程压缩图片的质量
	 * @Author:zhangnan@voole.com
	 * @Since:2015-11-5  
	 * @Version:1.1  
	 * @param bitmap 内存中的图片
	 * @param path 图片的保存路径
	 */
	public  void compressImageByQuality(final Bitmap bitmap,final String path){
		new Thread(new Runnable() {//开启多线程进行压缩处理
			@Override
			public void run() {
				try {
					//查看是否有原图,若有原图就将该图片删除,然后重新创建压缩过后的图片，进行保存
			    	if(isExistFile(path)){
			    		File oldFile = new File(path);
			    		oldFile.delete();
			    		Log.e(TAG, "源图片删除成功");
			    	}
			    	if(bitmap == null){
			    		mListener.writeDone(true);
			    		Log.e(TAG, "bitmap is null");
			    		return;
			    	}
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int options = 100;
					bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//质量压缩方法，把压缩后的数据存放到baos中 (100表示不压缩，0表示压缩到最小)
					Log.e(TAG, "bitmap size :"+baos.toByteArray().length / 1024);
					while (baos.toByteArray().length / 1024 > 400) {//循环判断如果压缩后图片是否大于100kb,大于继续压缩         
						baos.reset();//重置baos即让下一次的写入覆盖之前的内容 
						options -= 10;//图片质量每次减少10
						if(options<0)options=0;//如果图片质量小于10，则将图片的质量压缩到最小值
						bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//将压缩后的图片保存到baos中
						if(options==0)break;//如果图片的质量已降到最低则，不再进行压缩
					}
					//String path  = "/sdcard/11aa/testabc3.jpeg";
					FileOutputStream fos = new FileOutputStream(new File(path));//将压缩后的图片保存的本地上指定路径中
					fos.write(baos.toByteArray());
					fos.flush();
					fos.close();
					Log.e(TAG, "压缩图片成功");
					mListener.writeDone(true);
				} catch (Exception e) {
					mListener.writeDone(false);
				}
			}
		}).start();
	}

	/**
	 * @Description:  按比例缩小图片的像素以达到压缩的目的
	 * @Author:zhangnan@voole.com
	 * @Since:2015-11-5  
	 * @Version:1.1  
	 * @param imgPath
	 * @param listener
	 */
	public  void compressImageByPixel(String imgPath,FileWriteDoneListener listener) {
		try {
			
			this.mListener = listener;
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			newOpts.inJustDecodeBounds = true;//只读边,不读内容
			Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
			newOpts.inJustDecodeBounds = false;
			int width = newOpts.outWidth;
			int height = newOpts.outHeight;
			Log.e(TAG,"image bitmap width:"+width +" ,height:"+height);
//			float maxWSize = 480f;//默认1000px
//			float maxHSize = 800f;//默认1000px
//			int be = 1;
//			if (width > height && width > maxWSize) {//缩放比,用高或者宽其中较大的一个数据进行计算
//				be = (int) (newOpts.outWidth / maxWSize);
//			} else if (width < height && height > maxHSize) {
//				be = (int) (newOpts.outHeight / maxHSize);
//			}
			//be++;
			float maxWSize = 800f;
			int be = 1;
			while (width/be > maxWSize) {
				be++;
			}
			newOpts.inSampleSize = be;//设置采样率
			newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设
			newOpts.inPurgeable = true;// 同时设置才会有效
			newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收
			bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
			Log.e(TAG,"compress image size ");
			compressImageByQuality(bitmap,imgPath);//压缩好比例大小后再进行质量压缩  
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			mListener.writeDone(false);
		}
	}
	/**
	 * @Description:  按比例缩小图片的像素以达到压缩的目的
	 * @Author:zhangnan@voole.com
	 * @Since:2015-12-1  
	 * @Version:1.1  
	 * @param imgPath
	 * @param bitmap
	 * @param listener
	 */
	public  void compressImageByPixel(String imgPath,Bitmap bitmap,FileWriteDoneListener listener) {
		try {
			this.mListener = listener;
			float maxWSize = 800f;
			int bitmapWidth = bitmap.getWidth();  
            int bitmapHeight = bitmap.getHeight();  
			int be = 1;
			while (bitmapWidth/be > maxWSize) {
				be++;
			}
            Matrix matrix = new Matrix();  
            matrix.postScale(be, be);  
            // 产生缩放后的Bitmap对象  
            Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);  
			compressImageByQuality(resizeBitmap,imgPath);//压缩好比例大小后再进行质量压缩  
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			mListener.writeDone(false);
		}
	}
	
	public  FileWriteDoneListener mListener = null;
	public interface FileWriteDoneListener{
		void writeDone(boolean isSuccess);
	}
	/**
	 * @Description:  判断该文件是否存在
	 * @Author:zhangnan@voole.com
	 * @Since:2015-11-06  
	 * @Version:1.1  
	 * @param filePath
	 * @return
	 */
	public boolean isExistFile(String filePath){
		boolean isExist = false;
		File file = new File(filePath);
		if(file.exists()){
			isExist= true;
		}
		return isExist;
	}
}

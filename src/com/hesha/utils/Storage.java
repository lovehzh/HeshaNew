package com.hesha.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class Storage {
	
	private String externalStorageRoot;
	
	private static final String TAG = "Storage";
	private static String IMAGES_FOLDER = "images";
	
	private Context context;
	public final static int sStandardWidth=100,sStandarHeight=100,mStandarWidth=640,mStandarHeight=640;
	
	public Storage(Context context){
		this.context = context;
		//获取当前外部存储设备的目录
		externalStorageRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	}
	
	
	/**
	 * 得到SD Card的路径 [已测试]
	 * @return SD Card的路径
	 */
	public String getExternalStorageRoot() {
		return externalStorageRoot;
	}

	/**
	 * 创建内部存储器上的文件  [已测试]
	 * @param fileName 文件名
	 * @return File对象
	 */
	public File createIntenalFile(String fileName) {
		//File dir = context.getDir(fileName, 0);
		File dir = context.getFilesDir();
		File file = new File(dir, fileName);
		return file;
	}
	
	/**
	 * 以默认方式创建内部存储器上的文本文件，默认方式是指创建后不能追加内容 [已测试]
	 * @param fileName 文件名
	 * @param content 文本内容
	 * @return 创建成功返回true,否则返回false
	 */
	public boolean writeTextToInternalFile(String fileName, String text){
		FileOutputStream fos = null;
		try{
			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			fos.write(text.getBytes());
			fos.flush();
			fos.close();
			return true;
		}catch (Exception e){
		    if (fos != null){
		    	try {
					fos.close();
				} catch (IOException e1) {
					Log.e(TAG, e1.toString());
				}
		    }
		}
		return false;
	}
	
	/**
	 * 在内部存储器上创建Bitmap文件 [已测试]
	 * @param fileName 文件名
	 * @param mBitmap Bitmap对象 
	 * @return 创建成功返回true, 否则返回false
	 */
	public boolean writeBitMapToInternalFile(String fileName, Bitmap mBitmap){
		File file = this.createIntenalFile(fileName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
			return true;
		} catch(IOException e){
			Log.e(TAG, e.toString());
		}
		return false;
	}
	
	/**
	 * 在内部存储器上创建byte[]文件 [已测试]
	 * @param fileName 文件名
	 * @param mBytes byte[]
	 * @return 创建成功返回true, 否则返回false 
	 */
	public boolean writeBytesToInternalFile(String fileName, byte[] mBytes){
		File file = this.createIntenalFile(fileName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(mBytes);
			fos.flush();
			fos.close();
			return true;
		} catch (IOException e){
			Log.e(TAG, e.toString());
		}
		return false;
	}
	
	/**
	 * 读取内部存储器上的文本文件 [已测试]
	 * @param fileName 文件名
	 * @return 文本内容
	 */
	public String readTextFromInternalFile(String fileName){
		String result = "";
		FileInputStream fis = null;
		try{
			fis = context.openFileInput(fileName);
			byte[] buffer = new byte[1024];
			int length = 0;
			while((length = fis.read(buffer)) != -1){
				length += length;
				result = new String(buffer, 0, length);
			}
			fis.close();
		} catch (Exception e){
		    if (fis != null){
		    	try {
					fis.close();
				} catch (IOException e1) {
					Log.i(TAG, e1.toString());
				}
		    }
			Log.i(TAG, e.toString());
		}
		
		return result;
	}
	
	/**
	 * 从内部存储器上读取图片，返回Bitmap [已测试]
	 * @param fileName 
	 * @return Bitmap
	 */
	public Bitmap readBitmapFromInternalFile(String fileName){
		File dir = context.getFilesDir();
		File file = new File(dir, fileName);
		if(file.exists()){
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			return bitmap;
		}
		return null;
	}
	
	/**
	 * 从内部存储器上读取文件，返回byte[] [已测试]
	 * @param fileName 文件名
	 * @return byte[]
	 */
	public byte[] readBytesFromInternalFile(String fileName){
		File dir = context.getFilesDir();
		File file = new File(dir, fileName);
		byte[] content = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			
			byte[] temp = new byte[1024];
			int size = 0;
			while((size = bis.read(temp)) != -1){
				out.write(temp, 0, size);
			}
			bis.close();
			
			content = out.toByteArray();
		} catch (IOException e){
			Log.e(TAG, e.toString());
		}
		return content;
	}
	
	/**
	 * 判断SD Card是否存在 [已测试]
	 * @return 存在返回true,不存在返回false
	 */
	public boolean isExternalStorageExist(){
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_UNMOUNTED.equals(state)){
			return false;
		}
		return true;
	}
	
	/**
	 * 判断SD Card是否只是可读的 (read only) [已测试]
	 * @return 是只读的返回true,否则返回false
	 */
	public boolean isExternalStorageReadable(){
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断SD Card是否可读写的 [已测试]
	 * @return 可读写返回true,否则返回false
	 */
	public boolean isExternalStorageWriteable(){
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)){
			return true;
		}
		return false;
	}
	
	/**
	 * 在存储卡上创建一个文件 [已测试]
	 * @param path 路径
	 * @param fileName 文件名
	 * @return File
	 */
	public File createExternalFile(String path, String fileName){
		File file = new File(externalStorageRoot + path + File.separator + fileName); 
		return file;
	}
	
	/**
	 * 在SD卡上创建文本文件 [已测试]
	 * @param path 路径
	 * @param fileName 文件名
	 * @param text 文本内容
	 * @return 创建成功返回true, 否则返回false
	 */
	public boolean writeTextToExternalStorage(String path, String fileName, String text) {
		File file = this.createExternalFile(path, fileName);
		try{
			FileWriter fw = new FileWriter(file);
			fw.write(text);
			fw.flush();
			fw.close();
			return true;
		} catch(IOException e){
			Log.e(TAG, e.toString());
		}
		return false;
	}
	
	/**
	 * 在SD卡上创建Bitmap文件 [已测试]
	 * @param path 路径
	 * @param fileName 文件名,注意文件后缀为 ".png"
	 * @param mBitmap Bitmap对象
	 * @return 创建成功返回true, 否则返回false
	 */
	public boolean writeBitmapToExternalStorage(String path, String fileName, Bitmap mBitmap) {
		File file = this.createExternalFile(path, fileName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
			return true;
		} catch(IOException e){
			Log.e(TAG, e.toString());
		}
		return false;
	}
	
	/**
	 * 在SD卡上创建Bitmap文件 [已测试]
	 * @param path 路径
	 * @param fileName 文件名,注意文件后缀为 ".png"
	 * @param mBitmap Bitmap对象
	 * @return 创建成功返回true, 否则返回false
	 */
	public static boolean writeBitmapToExternalStorage(File imgFile,Bitmap mBitmap) {
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(imgFile);
			if(mBitmap!=null){
				mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			}
			fos.flush();
			fos.close();
			return true;
		} catch(IOException e){
			Log.e(TAG, e.toString());
		}
		return false;
	}
	/**
	 * 截取图片，保存一份大图和一份小图
	 * @param imgPath
	 * @param fileName
	 */
	public static boolean writeMSBitmapToExternalStorge(String imgPath, String fileName){	
		
		File dir = getImagesFolder();
		fileName=UUID.nameUUIDFromBytes(fileName.getBytes()).toString();
		fileName=fileName.replaceAll("-", "");
		File imgFile = new File(dir.getAbsoluteFile() + File.separator +fileName+".jpg");
		      
    	 BitmapFactory.Options op2 = new BitmapFactory.Options(); 
         
         op2.inJustDecodeBounds = true;  
         
         Bitmap pic2 = BitmapFactory.decodeFile(imgPath, op2);
         int wmRatio = (int) Math.ceil(op2.outWidth / (float) mStandarWidth); //计算宽度比例  
         int hmRatio = (int) Math.ceil(op2.outHeight / (float) mStandarHeight); //计算高度比例  
         Log.i("camera", "wmRatio1 = "+wmRatio+" , hmRatio1 = "+hmRatio);
         
         if (wmRatio > 1 && hmRatio > 1) {  
             if (wmRatio > hmRatio) {  
            	 if(wmRatio>=6){
            		 wmRatio=8;
            	 }
             	op2.inSampleSize = wmRatio;  
             } else {  
            	 if(hmRatio>=6){
            		 hmRatio=8;
            	 }
             	op2.inSampleSize = hmRatio;  
             }  
         }
         	                
         op2.inJustDecodeBounds = false; //注意这里，一定要设置为false，因为上面我们将其设置为true来获取图片尺寸了  
         pic2 = BitmapFactory.decodeFile(imgPath, op2);
    	
         boolean flag=writeBitmapToExternalStorage(imgFile, pic2);
                         
           if(flag){
        	   op2 = new BitmapFactory.Options(); 
               
               op2.inJustDecodeBounds = true;  
               
               pic2 = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), op2);
               
               int wRatio = (int) Math.ceil(op2.outWidth / (float) sStandardWidth); //计算宽度比例  
               int hRatio = (int) Math.ceil(op2.outHeight / (float) sStandarHeight); //计算高度比例  
               Log.i("camera", "wRatio = "+wRatio+" , hRatio = "+hRatio);                 
               if (wRatio > 1 && hRatio > 1) {  
                   if (wRatio > hRatio) {  
                	   if(wRatio>=6){
                		   wRatio=8;
                  	   }
                       op2.inSampleSize = wRatio;  
                   } else {  
                	   if(hRatio>=6){
                		   hRatio=8;
                  	   }
                       op2.inSampleSize = hRatio;  
                   }  
               }
                                  
               op2.inJustDecodeBounds = false; //注意这里，一定要设置为false，因为上面我们将其设置为true来获取图片尺寸了 
               
               pic2 = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), op2);
               
	       		if(dir!=null){
	       			dir = new File(dir.getAbsolutePath() + File.separator + "thumbnail");
	       			if(!dir.exists()) {
	       				dir.mkdirs();
	       			}
	       		}
               
               imgFile = new File(dir.getAbsoluteFile() + File.separator+fileName+".jpg");
               
               if(writeBitmapToExternalStorage(imgFile, pic2)){
            	   Log.i("camera", "writeBitmapToExternalStorage ="+true);     
            	   return true;
               };
              
           }else{
        	   return false;
           }
           return false;
           
	}
	/**
	 * 截图图片，保存一份大图
	 * @param imgPath
	 * @param fileName
	 * @return
	 */
	public static File writeMBitmapToExternalStorge(String imgPath, String fileName){	
		
		File dir = getImagesFolder();
		fileName=UUID.nameUUIDFromBytes(fileName.getBytes()).toString();
		fileName=fileName.replaceAll("-", "");
		File imgFile = new File(dir.getAbsoluteFile() + File.separator +fileName+".jpg");
		      
    	 BitmapFactory.Options op2 = new BitmapFactory.Options(); 
         
         op2.inJustDecodeBounds = true;  
         
         Bitmap pic2 = BitmapFactory.decodeFile(imgPath, op2);
         int wmRatio = (int) Math.ceil(op2.outWidth / (float) mStandarWidth); //计算宽度比例  
         int hmRatio = (int) Math.ceil(op2.outHeight / (float) mStandarHeight); //计算高度比例  
         Log.i("camera", "wmRatio = "+wmRatio+" , hmRatio = "+hmRatio);
         
         if (wmRatio > 1 && hmRatio > 1) {  
             if (wmRatio > hmRatio) {  
            	 if(wmRatio>=6){
            	    wmRatio=8;
            	   }
             	op2.inSampleSize = wmRatio;  
             } else {  
        	    if(hmRatio>=6){
        		 hmRatio=8;
         	    }
             	op2.inSampleSize = hmRatio;  
             }  
         }
         	                
         op2.inJustDecodeBounds = false; //注意这里，一定要设置为false，因为上面我们将其设置为true来获取图片尺寸了  
         pic2 = BitmapFactory.decodeFile(imgPath, op2);
    	
         boolean flag=writeBitmapToExternalStorage(imgFile, pic2);
          
         if(flag){
        	 return imgFile;
         }
         
         return null;
	}
	/**
	 * 截取图片，保存一份小图
	 * @param imgPath   the path of the original image
	 * @param fileName  the name of the image
	 * @return
	 */
	public static File writeSBitmapToExternalStorge(String imgPath, String fileName){	
		
		fileName=UUID.nameUUIDFromBytes(fileName.getBytes()).toString();
		fileName=fileName.replaceAll("-", "");
		
		File dir = getImagesFolder();
		if(dir!=null){
			dir = new File(dir.getAbsolutePath() + File.separator + "thumbnail");
			if(!dir.exists()) {
				dir.mkdirs();
			}
		}
		
		File imgFile = new File(dir.getAbsoluteFile() + File.separator +fileName+".jpg");
		          	
		BitmapFactory.Options op2 = new BitmapFactory.Options(); 
       
       op2.inJustDecodeBounds = true;  
       
       Bitmap  pic2 = BitmapFactory.decodeFile(imgPath, op2);
       
       int wRatio = (int) Math.ceil(op2.outWidth / (float) sStandardWidth); //计算宽度比例  
       int hRatio = (int) Math.ceil(op2.outHeight / (float) sStandarHeight); //计算高度比例  
                          
       if (wRatio > 1 && hRatio > 1) {  
           if (wRatio > hRatio) {  
        	   if(wRatio>=6){
        		   wRatio=8;
           	   }
               op2.inSampleSize = wRatio;  
           } else {  
        	   if(hRatio>=6){
        		   hRatio=8;
           	   }
               op2.inSampleSize = hRatio;  
           }  
       }
                          
       op2.inJustDecodeBounds = false; //注意这里，一定要设置为false，因为上面我们将其设置为true来获取图片尺寸了 
       
       pic2 = BitmapFactory.decodeFile(imgPath, op2);
       
       
       boolean flag=writeBitmapToExternalStorage(imgFile, pic2);
             
       if(flag){
      	 return imgFile;
       }
       
       return null;
           
	}
	
	/**
	 * 在SD卡上创建byte[]文件 [已测试]
	 * @param path
	 * @param fileName
	 * @param mBytes
	 * @return 创建成功返回true, 否则返回false
	 */
	public boolean writeBytesToExternalStorage(String path, String fileName, byte[] mBytes) {
		File file = this.createExternalFile(path, fileName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(mBytes);
			fos.flush();
			fos.close();
			return true;
		} catch (IOException e){
			Log.e(TAG, e.toString());
		}
		return false;
	}
	
	/**
	 * 从SD卡上读取文本,返回文本内容 [已测试]
	 * @param path 路径
	 * @param fileName 文件名
	 * @return String
	 */
	public String readTextFromExternalFile(String path, String fileName){
		File file = this.createExternalFile(path, fileName);
		String result = "";
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int length = 0;
			while((length = fis.read(buffer)) != -1){
				length += length;
				result = new String(buffer, 0, length);
			}
			fis.close();
		} catch (Exception e){
		    if (fis != null){
		    	try {
					fis.close();
				} catch (IOException e1) {
					Log.i(TAG, e1.toString());
				}
		    }
			Log.i(TAG, e.toString());
		}
		
		return result;
	}
	
	/**
	 * 从SD卡上读取图片，返回Bitmap [已测试]
	 * @param path 路径
	 * @param fileName 文件名
	 * @return 图片存在时返回Bitmap对象, 不存时在返回null
	 */
	public Bitmap readBitmapFromExternalFile(String path, String fileName){
		File file = this.createExternalFile(path, fileName);
		if(file.exists()){
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			return bitmap;
		}
		return null;
	}
	
	/**
	 * 从SD卡上读取文件，返回byte[] [已测试]
	 * @param path
	 * @param fileName
	 * @return byte[]
	 */
	public byte[] readBytesFromExternalFile(String path, String fileName){
		File file = this.createExternalFile(path, fileName);
		byte[] content = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			
			byte[] temp = new byte[1024];
			int size = 0;
			while((size = bis.read(temp)) != -1){
				out.write(temp, 0, size);
			}
			bis.close();
			
			content = out.toByteArray();
		} catch (IOException e){
			Log.e(TAG, e.toString());
		}
		return content;
	}
	
	
	/**
	 * 获取内部存储的文件列表 [已测试]
	 * @return ArrayList<File>
	 */
	public List<File> getFileListFromInternalStorage() {
		String[] names = context.fileList();
		ArrayList<File> files = new ArrayList<File>();
		for(int i=0; i<names.length; i++){
			File f = new File(names[i]);
			files.add(f);
		}
		return files;
	
	}
	
	/**
	 * 获取存储卡目录中的文件列表 [已测试]
	 * @param path 路径
	 * @return ArrayList<File>
	 */
	public List<File> getFileListFromExternalStorage(String path) {
		List<File> fileList = new ArrayList<File>();
		File dir = new File(externalStorageRoot + File.separator + path);
		File[] files = dir.listFiles();
		for(int i=0; i<files.length; i++){
			fileList.add(files[i]);
		}
		return fileList;
	}
	
	private static File getStorageFolder() {
		File root = Environment.getExternalStorageDirectory();
		
	    if (root.canWrite()){
	    	String uuid = UUID.nameUUIDFromBytes("SnapSomething".getBytes()).toString();
	    	uuid = uuid.replaceAll("-", "");
			File dir = new File(root.getAbsolutePath() + File.separator + "SnapSomething"+File.separator+uuid);
			if(!dir.exists()) {
				dir.mkdirs();
				Log.d(TAG, "Create directory " + dir.getAbsolutePath());
			}
			return dir;
	    }
	    return null;
	}
	
	private static File getSubFolder(String subFolder) {
		File root = getStorageFolder();
		if(root != null) {
			File dir = new File(root.getAbsoluteFile() + File.separator + subFolder);
			dir.mkdirs();
			return dir;
		}
		return null;
	}
	
	public static File getImagesFolder() {
		return getSubFolder(IMAGES_FOLDER);
	}
}

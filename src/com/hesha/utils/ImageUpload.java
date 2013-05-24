package com.hesha.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import android.util.Log;

public class ImageUpload
{
	private static final String TAG = "ImageUpload";

	/**
	 * 直接通过HTTP协议提交数据到服务器,实现表单提交功能
	 * 
	 * @param actionUrl
	 *            上传路径
	 * @param params
	 *            请求参数 key为参数名,value为参数值
	 * @param filename
	 *            上传文件
	 */
	public String postImg(String actionUrl, Map<String, String> params,
			String filename, String name)
	{
		String s = "";
		try
		{
			String BOUNDARY = "--------------et567z"; // 数据分隔线
			String MULTIPART_FORM_DATA = "Multipart/form-data";

			URL url = new URL(actionUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoInput(true);// 允许输入
			conn.setDoOutput(true);// 允许输出
			conn.setUseCaches(false);// 不使用Cache
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA
					+ ";boundary=" + BOUNDARY);

			StringBuilder sb = new StringBuilder();

			// 上传的表单参数部分，格式请参考文章
			for (Map.Entry<String, String> entry : params.entrySet())
			{// 构建表单字段内容
				sb.append("--");
				sb.append(BOUNDARY);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"\r\n\r\n");
				sb.append(entry.getValue());
				sb.append("\r\n");
			}
			// System.out.println(sb.toString());
			DataOutputStream outStream = new DataOutputStream(
					conn.getOutputStream());
			outStream.write(sb.toString().getBytes());// 发送表单字段数据
			
			if(null != filename) {
				byte[] content = readFileImage(filename);
				// 上传的文件部分，格式请参考文章
				// System.out.println("content:"+content.toString());
			
				StringBuilder split = new StringBuilder();
				split.append("--");
				split.append(BOUNDARY);
				split.append("\r\n");
				split.append("Content-Disposition: form-data;name=\"" + name + "\";filename=\""
						+ filename + "\r\n");
				split.append("Content-Type: image/jpg\r\n\r\n");
				System.out.println(split.toString());
				outStream.write(split.toString().getBytes());
				outStream.write(content, 0, content.length);
				outStream.write("\r\n".getBytes());
			}
			

			byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();// 数据结束标志
			outStream.write(end_data);
			outStream.flush();
			int cah = conn.getResponseCode();
			String serverResponseMessage = conn.getResponseMessage();
			s = inputStream2String(conn.getInputStream());
			if (Constants.D)
				Log.i(TAG, "s:" + s);
			if (Constants.D)
				Log.i(TAG, "code : " + cah + " message : "
						+ serverResponseMessage);
//			Log.e(TAG, "ooooooo"+serverResponseMessage);
			// if (cah != 200) throw new RuntimeException("请求url失败:"+cah);
			if (cah == 200)// 如果发布成功则提示成功
			{

			}
			else if (cah == 400)
			{

			}
			else
			{
				throw new RuntimeException("请求url失败:" + cah);
			}

			outStream.close();
			conn.disconnect();
		}
		catch (IOException e)
		{
			e.printStackTrace();

		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		
		return s;
	}

	public static byte[] readFileImage(String filename) throws IOException
	{
		BufferedInputStream bufferedInputStream = new BufferedInputStream(
				new FileInputStream(filename));
		int len = bufferedInputStream.available();
		byte[] bytes = new byte[len];
		int r = bufferedInputStream.read(bytes);
		if (len != r)
		{
			bytes = null;
			throw new IOException("读取文件不正确");
		}
		bufferedInputStream.close();
		return bytes;
	}

	public static String inputStream2String(InputStream is) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1)
		{
			baos.write(i);
		}
		return baos.toString();
	}
}

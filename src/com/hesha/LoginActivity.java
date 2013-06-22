package com.hesha;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.bean.personal.Code;
import com.hesha.bean.personal.GenSignIn;
import com.hesha.constants.Constants;
import com.hesha.utils.DeviceInfo;
import com.hesha.utils.DtoUtils;
import com.hesha.utils.HttpUrlConnectionUtils;
import com.hesha.utils.JsonUtils;
import com.hesha.utils.MyDialog;
import com.hesha.utils.NetState;
import com.hesha.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener, Constants{
	private static final String TAG = "LoginActivity";
	private Button btnBack, btnLogin, btnSignup, btnRetrievePassword;
	private EditText etUsername, etPassword;
	private InputMethodManager imm;
	
	private ProgressDialog dialog;
	private String username, userId;
	private SharedPreferences settings;
	private int intentCode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
		
		initData();
		initComponent();
	}
	
	private void initData() {
		if(D) Log.i(TAG, "initData()");
		Intent intent = getIntent();
		if(null != intent) {
			intentCode = intent.getIntExtra("code", 0);
		}
		
		settings = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
		username = settings.getString(Constants.USERNAME, "");
		userId = settings.getString(Constants.USER_ID, "0");
	}
	
	private void initComponent() {
		dialog = new ProgressDialog(this);
		
		btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		
		btnSignup = (Button)findViewById(R.id.btnSignup);
		btnSignup.setOnClickListener(this);
		
		btnRetrievePassword = (Button)findViewById(R.id.btnRetrievePassword);
		btnRetrievePassword.setOnClickListener(this);
		
		etUsername = (EditText)findViewById(R.id.etUsername);
		etPassword = (EditText)findViewById(R.id.etPassword);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;
			
		case R.id.btnLogin:
			if(NetState.isConnect(this)) {
				if(checkField()) {
					if (imm.isActive()) {//如果开启
						imm.hideSoftInputFromWindow(v.getWindowToken(),0);
					}
					//sign in task
					new SignInTask().execute((Void)null);
				}
			}else {
				Toast.makeText(this, R.string.no_net, Toast.LENGTH_SHORT).show();
			}
			
			break;
			
		case R.id.btnSignup:
			intent = new Intent(this, SignupActivity.class);
			startActivity(intent);
			break;
			
		case R.id.btnRetrievePassword:
			intent = new Intent(this, RetrievePasswordActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}
	
	private boolean checkField() {
		boolean result = false;
		String username = etUsername.getText().toString().trim();
		String password = etPassword.getText().toString().trim();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示");
		
		if(username.length() == 0) {
			builder.setMessage("用户名不能为空");
			result = false;
		} else if(password.length() == 0) {
			builder.setMessage("密码不能为空");
			result =  false;
		} else {
			result = true;
		}
		
		if(!result) {
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			
			builder.create().show();
		}
		
		return result;
	}
	
	private class SignInTask extends AsyncTask<Void, Void, Void> {
		private String response;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("正在登录，请稍后");
			dialog.show();
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			username = etUsername.getText().toString().trim();
			String password = etPassword.getText().toString().trim();
			GenSignIn genSignIn = new GenSignIn();
			genSignIn.setAc("userLogin");
			genSignIn.setDeviceId(DeviceInfo.getDeviceId(LoginActivity.this));
			genSignIn.setPassword(DtoUtils.toEncode(password));
			genSignIn.setUsername(DtoUtils.toEncode(username));
			
			String data = JsonUtils.genJson(genSignIn);
			if(Constants.D) Log.i(TAG, data);
			String url = Constants.SERVER_URL;
			try {
				response = HttpUrlConnectionUtils.post(url, data, Constants.CONTENT_TYPE_JSON);
				if(Constants.D) Log.i(TAG, "sign in response:" + response); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(dialog.isShowing()) {
				dialog.dismiss();
			}
			
			ObjectMapper mapper = new ObjectMapper();
			try {
				Code code = mapper.readValue(response, Code.class);
				String strCode = code.getCode();
				if(strCode.equals("GB0000000")) {//注册成功
					String token = code.getToken();
					int uid = code.getUid();
					
					Editor editor = settings.edit();//保存user id, username, and token
					editor.putString(Constants.USER_ID, String.valueOf(uid));
					editor.putString(Constants.USERNAME, username);
					editor.putString(Constants.TOKEN, token);
					editor.commit();
					
					//登录成功后设置显示的ViewFilper和UI上的文字
					//vf.setDisplayedChild(1);
					//tvUsername.setText(username);
					
					//取得用户信息
					int[] userIds = new int[]{uid};
//					new GetUserInfoTask(userIds).execute((Void)null);
//					
					if(intentCode == INTENT_CODE_COLLECTION) {
						setResult(INTENT_CODE_COLLECTION);
						finish();
					}
					else if(intentCode == INTENT_CODE_ITEM_DETAIL) {
						setResult(INTENT_CODE_ITEM_DETAIL);
						finish();
					}
					
					else if(intentCode == INTENT_CODE_ITEM_DETAIL_LIKE) {
						setResult(INTENT_CODE_ITEM_DETAIL_LIKE);
						finish();
					}
					
					else if(intentCode == INTENT_CODE_SUBJECT_UPLOAD_PHOTO) {
						setResult(INTENT_CODE_SUBJECT_UPLOAD_PHOTO);
						finish();
					}
					
				}else {
					String errorMessage = Utils.parseCode(LoginActivity.this, strCode);
					AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
					builder.setTitle("登录失败");
					builder.setMessage(errorMessage);
					builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					
					builder.create().show();
				}
				
				
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				MyDialog.showInfoDialog(LoginActivity.this);
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}

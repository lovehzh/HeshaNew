package com.hesha;


import com.hesha.utils.EmailUtils;
import com.hesha.utils.NetState;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends Activity implements OnClickListener{
	private Button btnBack, btnSignup;
	private EditText etEmail, etNickname, etPassword;
	private InputMethodManager imm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_activity);
		
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		initComponent();
	}
	
	private void initComponent() {
		btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		btnSignup = (Button)findViewById(R.id.btnSignup);
		btnSignup.setOnClickListener(this);
		
		etEmail = (EditText)findViewById(R.id.etEmail);
		etNickname = (EditText)findViewById(R.id.etNickname);
		etPassword = (EditText)findViewById(R.id.etPassword);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;
			
		case R.id.btnSignup:
			if(NetState.isConnect(this)) {
				if(checkField()) {
					if (imm.isActive()) {
						//如果开启
						imm.hideSoftInputFromWindow(v.getWindowToken(),0);
						}
				
					//signUpProcess();
				}
				break;
			}else {
				Toast.makeText(this, R.string.no_net, Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
		
	}
	
	private boolean checkField() {
		boolean result = false;
		String email = etEmail.getText().toString().trim();
		String username = etNickname.getText().toString().trim();
		String password = etPassword.getText().toString().trim();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
		builder.setTitle("提示");
		
		if(!EmailUtils.isNameAdressFormat(email)) {
			builder.setMessage("请输入正确的邮箱格式");
			result =  false;
		}else if(username.length() < 5 || username.length() > 16) {
			builder.setMessage("用户昵称必须由5-16位字符或数字组成");
			result = false;
		} else if(password.length() <6 || password.length() > 32) {
			builder.setMessage("密码必须由6-32位字母或数字组成");
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
}

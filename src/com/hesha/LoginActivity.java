package com.hesha;

import com.hesha.utils.NetState;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	private Button btnBack, btnLogin, btnSignup, btnRetrievePassword;
	private EditText etUsername, etPassword;
	private InputMethodManager imm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
		
		initComponent();
	}
	
	private void initComponent() {
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
}

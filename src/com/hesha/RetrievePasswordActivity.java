package com.hesha;

import com.hesha.utils.EmailUtils;
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

public class RetrievePasswordActivity extends Activity implements OnClickListener{
	private Button btnBack, btnSubmit;
	private EditText etEmail;
	private InputMethodManager imm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.retrieve_password_activity);
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
		
		initComponent();
	}
	
	private void initComponent() {
		btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		
		btnSubmit = (Button)findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		
		etEmail = (EditText)findViewById(R.id.etEmail);
		
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;
			
		case R.id.btnSubmit:
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
			
		default:
			break;
		}
		
	}
	
	private boolean checkField() {
		boolean result = false;
		String email = etEmail.getText().toString().trim();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示");
		
		if(!EmailUtils.isNameAdressFormat(email)) {
			builder.setMessage("请输入正确的邮箱格式");
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

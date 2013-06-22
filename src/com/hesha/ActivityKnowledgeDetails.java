package com.hesha;



import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.bean.knowledge.Article;
import com.hesha.bean.knowledge.ArticleDetailsData;
import com.hesha.constants.Constants;
import com.hesha.utils.DateUtils;
import com.hesha.utils.HttpUrlConnectionUtils;
import com.hesha.utils.TimeoutErrorDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;



public class ActivityKnowledgeDetails extends Activity implements OnClickListener{
	private static final String TAG = "ActivityKnowledgeDetails";
	private Button btnBack;
	private TextView tvName, tvUsername, tvDate;
	private TextView tvIntro;
	private WebView webView;
	private TextView tvContent;
	private Article article;
	
	private static final int WHAT_DID_LOAD_DATA = 0;
//	private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;
	private static final int CONNECTION_TIME_OUT = 3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_knowledge_detail);
		
		initData();
		initComponent();
	}
	
	private void initData() {
		Intent intent = getIntent();
		article = (Article)intent.getSerializableExtra("article");
		
		loadData();
	}
	
	private void initComponent() {
		btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		tvName = (TextView)findViewById(R.id.tvName);
		tvName.setText(article.getTitle());
		
		tvUsername = (TextView)findViewById(R.id.tvUsername);
		tvUsername.setText(article.getUser_info().getUser_name());
		
		tvDate = (TextView)findViewById(R.id.tvDate);
		tvDate.setText(DateUtils.getStringFromTimeMillis(article.getCreation_date() * 1000 + ""));
		
		tvIntro = (TextView)findViewById(R.id.tv_intro);
		tvIntro.setText(article.getIntro());
		

	}
	
	private void setView(Article article) {
		String imgTemplate = "";
		if(article.getImage().getImage().trim().length() != 0) {
			imgTemplate = "<center><img src=\"url\" width=\"304\" height=\"228\"></center><br/><p>";
			imgTemplate = imgTemplate.replace("url", Constants.IMAGE_BASE_URL + article.getImage().getImage());
		}
		
		Log.i("img", imgTemplate);
		
		String template = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><body>mycontent</body></html>";
		
		webView = (WebView)findViewById(R.id.webView);
		webView.setBackgroundColor(0);
		webView.loadDataWithBaseURL(null, template.replace("mycontent", imgTemplate + article.getContent()), "text/html", "utf-8", null);
		
		if(Constants.D) Log.i("ss", article.getContent());
		
		tvContent = (TextView)findViewById(R.id.tvContent);
		tvContent.setText(article.getContent());
		tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;

		default:
			break;
		}
		
	}
	
	private void loadData() {
		Log.i(TAG, "on loadData");
		new Thread(new Runnable() {
			@Override
			public void run() {
				Article obj = getArticleByIdFromServer(article.getId());
				
				Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				msg.obj = obj;
				msg.sendToTarget();
			}
		}).start();
	}
	
	
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA:
				
				if(null != msg.obj) {
					//更新UI
					Article article = (Article)msg.obj;
					setView(article);
				}else {
					
				}
				break;
				
			case CONNECTION_TIME_OUT:
				TimeoutErrorDialog.showTimeoutError(ActivityKnowledgeDetails.this);
				break;

			default:
				break;
			}
		};
	};
	
	private Article getArticleByIdFromServer(int id) {
		String response = "";
		Article article = new Article();
		
		String url = Constants.SERVER_URL + "?ac=getArticleById&article_id=" + id;
		if(Constants.D) Log.i(TAG, "url:" + url);
		
		try {
			response = HttpUrlConnectionUtils.get(url, "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(Constants.D) Log.i(TAG, "response:" + response);
		
		if(response.equals(Constants.CONNECTION_TIMED_OUT)) {
			Message msg = mUIHandler.obtainMessage(CONNECTION_TIME_OUT);
			msg.sendToTarget();
		}else {
			ObjectMapper mapper = new ObjectMapper();
			try {
				
				ArticleDetailsData struct = mapper.readValue(response, ArticleDetailsData.class);
				boolean success = Boolean.valueOf(struct.getSuccess());
				if(success) {
					//
					article = struct.getArticle();
				}else {
					AlertDialog.Builder builder = new AlertDialog.Builder(ActivityKnowledgeDetails.this);
					builder.setTitle("获取数据失败");
					builder.setMessage("错误：" + struct.getError_des());
					builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
					});
					
					builder.create().show();
				}
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		
		return article;
	}
}

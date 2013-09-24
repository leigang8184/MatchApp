package match.mt.matchapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

public class MoreAboutActivity extends Activity {

	private Button about_backbtn = null;
	private WebView m_webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_about_layout);

		about_backbtn = (Button) findViewById(R.id.about_button_back);
		m_webView = (WebView) findViewById(R.id.about_webView);

		m_webView.loadUrl("file:///android_asset/about.html");

		about_backbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						MoreActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK) && m_webView.canGoBack()) {
			m_webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
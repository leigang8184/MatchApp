package match.mt.matchapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MoreActivity extends Activity {

	private Button more_backbtn = null;
	private Button more_btn_about = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_layout);

		more_backbtn = (Button) findViewById(R.id.more_button_back);
		more_btn_about = (Button) findViewById(R.id.more_btn_about);
		
		more_btn_about.setOnClickListener(aboutbtn_Listener);
		more_backbtn.setOnClickListener(backbtn_Listener);
	}
	
	public Button.OnClickListener backbtn_Listener = new Button.OnClickListener() {

		public void onClick(View v) {

			Intent m_intent = new Intent();
			m_intent.setClass(getApplicationContext(), MainMatchActivity.class);
			startActivity(m_intent);
		}
	};

	
	public Button.OnClickListener aboutbtn_Listener = new Button.OnClickListener() {

		public void onClick(View v) {

			Intent m_intent = new Intent();
			m_intent.setClass(getApplicationContext(), MoreAboutActivity.class);
			startActivity(m_intent);
		}
	};

}
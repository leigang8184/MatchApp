package match.mt.matchapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ScanResultActivity extends Activity {

	private TextView scan_result_text = null;
	private ImageView scan_result_bitmap = null;
	private Button scan_result_backbtn = null;

	public String result_text = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.scan_result_layout);

		scan_result_text = (TextView) findViewById(R.id.scan_result_text);
		scan_result_bitmap = (ImageView) findViewById(R.id.scan_result_bitmap);
		scan_result_backbtn = (Button) findViewById(R.id.button_back);

		scan_result_backbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						MainMatchActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});

		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.containsKey("result")
				&& bundle.containsKey("bitmap")) {

			scan_result_text.setText(bundle.getString("result"));
			scan_result_bitmap.setImageBitmap((Bitmap) getIntent()
					.getParcelableExtra("bitmap"));

		}

	}

}
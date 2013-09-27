package match.mt.matchapp;

import java.io.IOException;

import match.mt.nfc4wifi.ISector;
import match.mt.nfc4wifi.MifareClassCard;
import match.mt.nfc4wifi.MifareSector;
import match.mt.nfc4wifi.util;
import match.mt.nfc4wifi.WifiAdmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NfcRWActivity extends Activity {

	// NFC parts
	private static NfcAdapter mAdapter;
	private static PendingIntent mPendingIntent;
	private static IntentFilter[] mFilters;
	private static String[][] mTechLists;
	// 77696669--wifi
	private static final byte[] wifiKey = { 'w', 'i', 'f', 'i', (byte) 0xFF,
			(byte) 0xFF };
	private static final int AUTH = 1;
	private static final int EMPTY_BLOCK_0 = 2;
	private static final int EMPTY_BLOCK_1 = 3;
	private static final int NETWORK = 4;
	private static final String TAG = "mifare";

	private Button connectBtn = null;
	private Button readBtn = null;
	private Button writeBtn = null;
	private WifiAdmin mWifiAdmin;
	NfcAdapter nfcAdapter;
	TextView promt;
	private Intent intentNow;
	private Intent intentNfc;
	private int type = 2;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nfc_layout);

		mWifiAdmin = new WifiAdmin(NfcRWActivity.this);

		readBtn = (Button) findViewById(R.id.button1);
		readBtn.setOnClickListener(new MyListener());

		writeBtn = (Button) findViewById(R.id.button2);
		writeBtn.setOnClickListener(new MyListener());

		connectBtn = (Button) findViewById(R.id.button3);
		connectBtn.setOnClickListener(new MyListener());

		promt = (TextView) findViewById(R.id.OpRslt);

		// 获取默认的NFC控制器
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (nfcAdapter == null) {
			promt.setText("设备不支持NFC！");

			Toast.makeText(NfcRWActivity.this, "设备不支持NFC！", Toast.LENGTH_SHORT)
					.show();

			finish();
			return;
		}
		if (!nfcAdapter.isEnabled()) {
			promt.setText("请在系统设置中先启用NFC功能！");

			Toast.makeText(NfcRWActivity.this, "请在系统设置中先启用NFC功能！",
					Toast.LENGTH_SHORT).show();

			finish();
			return;
		}

		mAdapter = NfcAdapter.getDefaultAdapter(this);
		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		mFilters = new IntentFilter[] { ndef, };

		// Setup a tech list for all NfcF tags
		mTechLists = new String[][] { new String[] { MifareClassic.class
				.getName() } };

		intentNow = getIntent();
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intentNow.getAction())
				|| NfcAdapter.ACTION_TAG_DISCOVERED.equals(intentNow
						.getAction())) {
			intentNfc = intentNow;
		}
		resolveIntent(intentNow);

	}

	private class MyListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			EditText rslt = (EditText) findViewById(R.id.OpRslt);
			switch (v.getId()) {
			case R.id.button1:// 读取
				// readNfc(intentNow);
				type = 0;
				break;
			case R.id.button2:// 写入
				// writeNfc(intentNow);
				type = 1;
				break;
			case R.id.button3:// 连接wifi
				type = 2;
				EditText nameText = (EditText) findViewById(R.id.editText1);
				EditText pwdText = (EditText) findViewById(R.id.editText2);
				if (nameText.getText() == null
						|| nameText.getText().toString().length() == 0) {
					rslt.setText("please input wifi name~");
				}
				rslt.setText(mWifiAdmin.openWifiByNamePwd(nameText.getText()
						.toString(), pwdText.getText().toString()));
				break;
			}
		}
	}

	private void writeNfc(Intent intent) {
		// mAdapter.disableForegroundDispatch(this);
		String action = intentNfc.getAction();
		EditText nameText = (EditText) findViewById(R.id.editText1);
		EditText pwdText = (EditText) findViewById(R.id.editText2);
		String wifiName = nameText.getText().toString();
		String wifiPwd = pwdText.getText().toString();
		// 2) Check if it was triggered by a tag discovered interruption.
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {

			Tag tagFromIntent = intentNfc
					.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			// 4) Get an instance of the Mifare classic card from this TAG
			// intent
			MifareClassic mfc = MifareClassic.get(tagFromIntent);
			MifareClassCard mifareClassCard = null;
			try { // 5.1) Connect to card
				mfc.connect();
				boolean auth = false;
				// 5.2) and get the number of sectors this card has..and loop
				// thru these sectors
				int secCount = mfc.getSectorCount();
				mifareClassCard = new MifareClassCard(secCount);
				int bCount = 0;
				int bIndex = 0;
				for (int j = 0; j < secCount; j++) {
					MifareSector mifareSector = new MifareSector();
					mifareSector.sectorIndex = j;
					// 6.1) authenticate the sector
					auth = mfc.authenticateSectorWithKeyA(j, wifiKey);
					mifareSector.authorized = auth;
					if (auth) {
						String tmp = "";
						int index = 0;
						// 6.2) In each sector - get the block count
						bCount = mfc.getBlockCountInSector(j);
						bCount = Math.min(bCount, ISector.BLOCKCOUNT);
						bIndex = mfc.sectorToBlock(j);
						for (int i = 0; i < bCount; i++) {
							if (j == 0 && i == 0) {
								continue;
								// 第一扇区第一块是厂商信息
							}

							if (index == 0) {
								index++;
								tmp = "name:" + wifiName;
							} else if (index == 1) {
								index++;
								tmp = "pwd:" + wifiPwd;
							} else {
								break;
							}

							// 写入
							mfc.writeBlock(++bIndex,
									util.string2AsciiBytes(tmp, 16));
						}
						break;
					} else { // Authentication failed - Handle it

					}
				}
				String rslt = "";
				rslt += "now write to nfc:";
				rslt += "\n";
				rslt += "name:" + wifiName;
				rslt += "\n";
				rslt += "pwd:" + wifiPwd;
				rslt += "\n";
				rslt += "write success~~";
				rslt += "\n";
				// wifi
				rslt += mWifiAdmin.openWifiByNamePwd(wifiName, wifiPwd);

				promt.setText(rslt);
			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage());
				showAlert(3);
			} finally {
				/*
				 * mAdapter.enableForegroundDispatch(this, mPendingIntent,
				 * mFilters, mTechLists);
				 */
				if (mifareClassCard != null) {
					mifareClassCard.debugPrint();
				}
			}
		}

	}
	
	void resolveIntent(Intent intent) {
		// 1) Parse the intent and get the action that triggered this intent
		String action = intent.getAction();
		String wifiName = "";
		String wifiPwd = "";
		// 2) Check if it was triggered by a tag discovered interruption.
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
			// 3) Get an instance of the TAG from the NfcAdapter
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			// 4) Get an instance of the Mifare classic card from this TAG
			// intent
			MifareClassic mfc = MifareClassic.get(tagFromIntent);
			MifareClassCard mifareClassCard = null;

			try { // 5.1) Connect to card

				if(tagFromIntent==null){
					Log.e("tagFromIntent!!!!!", "tagFromIntent is null");
					return ;
				}
				
				if(mfc==null){
					Log.e("mfc!!!!!", "mfc is null: "+tagFromIntent);
					return ;
				}
				mfc.connect();
				boolean auth = false;
				// 5.2) and get the number of sectors this card has..and loop
				// thru these sectors
				int secCount = mfc.getSectorCount();
				mifareClassCard = new MifareClassCard(secCount);
				int bCount = 0;
				int bIndex = 0;
				for (int j = 0; j < secCount; j++) {
					MifareSector mifareSector = new MifareSector();
					mifareSector.sectorIndex = j;
					// 6.1) authenticate the sector
					auth = mfc.authenticateSectorWithKeyA(j, wifiKey);
					mifareSector.authorized = auth;
					if (auth) {
						String tmp = "";
						int index = 0;
						// 6.2) In each sector - get the block count
						bCount = mfc.getBlockCountInSector(j);
						bCount = Math.min(bCount, ISector.BLOCKCOUNT);
						bIndex = mfc.sectorToBlock(j);
						for (int i = 0; i < bCount; i++) {
							if (j == 0 && i == 0) {
								continue;
								// 第一扇区第一块是厂商信息
							}
							// 6.3) Read the block
							byte[] data = mfc.readBlock(bIndex++);
							for (int k = 0; k < data.length; k++) {

								if (data[k] == (byte) 0xFF) {
									index = k;
									break;
								}
							}
							tmp = util.byte2AsciiString(data, 1, index);
							if (tmp.startsWith("name:")) {
								wifiName = tmp.split("name:")[1];
							} else if (tmp.startsWith("pwd:")) {
								wifiPwd = tmp.split("pwd:")[1];
							}
						}
					} else { // Authentication failed - Handle it

					}
				}
				String rslt = "";
				rslt += "now connect wifi:";
				rslt += "\n";
				rslt += "wifi name:" + wifiName;
				rslt += "\n";
				rslt += "wifi password:" + wifiPwd;
				rslt += "\n";
				rslt += "connect status:";
				rslt += "\n";
				// wifi
				rslt += mWifiAdmin.openWifiByNamePwd(wifiName, wifiPwd);

				promt.setText(rslt);
			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage());
				showAlert(3);
			} finally {

				if (mifareClassCard != null) {
					mifareClassCard.debugPrint();
				}
			}
		}// End of method
		else {
			promt.setText("no nfc tag find");
		}

	}
	
	void readNfc(Intent intent) {
		// 1) Parse the intent and get the action that triggered this intent
		// mAdapter.disableForegroundDispatch(this);
		String action = intentNfc.getAction();
		String wifiName = "";
		String wifiPwd = "";
		// 2) Check if it was triggered by a tag discovered interruption.
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
			// 3) Get an instance of the TAG from the NfcAdapter
			Tag tagFromIntent = intentNfc
					.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			// 4) Get an instance of the Mifare classic card from this TAG
			// intent
			MifareClassic mfc = MifareClassic.get(tagFromIntent);
			MifareClassCard mifareClassCard = null;

			try { // 5.1) Connect to card
				mfc.connect();
				boolean auth = false;
				// 5.2) and get the number of sectors this card has..and loop
				// thru these sectors
				int secCount = mfc.getSectorCount();
				mifareClassCard = new MifareClassCard(secCount);
				int bCount = 0;
				int bIndex = 0;
				for (int j = 0; j < secCount; j++) {
					MifareSector mifareSector = new MifareSector();
					mifareSector.sectorIndex = j;
					// 6.1) authenticate the sector
					auth = mfc.authenticateSectorWithKeyA(j, wifiKey);
					mifareSector.authorized = auth;
					if (auth) {
						String tmp = "";
						int index = 0;
						// 6.2) In each sector - get the block count
						bCount = mfc.getBlockCountInSector(j);
						bCount = Math.min(bCount, ISector.BLOCKCOUNT);
						bIndex = mfc.sectorToBlock(j);
						for (int i = 0; i < bCount; i++) {
							if (j == 0 && i == 0) {
								continue;
								// 第一扇区第一块是厂商信息
							}
							// 6.3) Read the block
							byte[] data = mfc.readBlock(bIndex++);
							for (int k = 0; k < data.length; k++) {

								if (data[k] == (byte) 0xFF) {
									index = k;
									break;
								}
							}
							tmp = util.byte2AsciiString(data, 1, index);
							if (tmp.startsWith("name:")) {
								wifiName = tmp.split("name:")[1];
							} else if (tmp.startsWith("pwd:")) {
								wifiPwd = tmp.split("pwd:")[1];
							}
						}
					} else { // Authentication failed - Handle it

					}
				}
				String rslt = "";
				rslt += "now read tag data:";
				rslt += "\n";
				rslt += "wifi name:" + wifiName;
				rslt += "\n";
				rslt += "wifi password:" + wifiPwd;
				rslt += "\n";
				// wifi
				rslt += mWifiAdmin.openWifiByNamePwd(wifiName, wifiPwd);

				promt.setText(rslt);
			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage());
				showAlert(3);
			} finally {
				/*
				 * mAdapter.enableForegroundDispatch(this, mPendingIntent,
				 * mFilters, mTechLists);
				 */
				if (mifareClassCard != null) {
					mifareClassCard.debugPrint();
				}
			}
		}// End of method
		else {
			promt.setText("no nfc tag find");
		}

	}
	
	private void showAlert(int alertCase) {
		// prepare the alert box
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		switch (alertCase) {

		case AUTH:// Card Authentication Error
			alertbox.setMessage("Authentication Failed ");
			break;
		case EMPTY_BLOCK_0: // Block 0 Empty
			alertbox.setMessage("Failed reading ");
			break;
		case EMPTY_BLOCK_1:// Block 1 Empty
			alertbox.setMessage("Failed reading 0");
			break;
		case NETWORK: // Communication Error
			alertbox.setMessage("Tag reading error");

			break;
		}
		// set a positive/yes button and create a listener
		alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			// Save the data from the UI to the database - already done
			public void onClick(DialogInterface arg0, int arg1) {
				clearFields();
			}
		});
		// display box
		alertbox.show();

	}
	
	private void clearFields() {
		promt.setText("");

	}
	
	@Override
	public void onResume() {
		super.onResume();
		mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
				mTechLists);
	}

	@Override
	public void onNewIntent(Intent intent) {
		Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);
		intentNow = intent;
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intentNow.getAction())
				|| NfcAdapter.ACTION_TAG_DISCOVERED.equals(intentNow
						.getAction())) {
			intentNfc = intentNow;
		}

		if (type == 0) {
			readNfc(intentNow);
		} else if (type == 1) {
			writeNfc(intentNow);
		} else {
			resolveIntent(intent);
		}

		// mText.setText("Discovered tag " + ++mCount + " with intent: " +
		// intent);
	}

	@Override
	public void onPause() {
		super.onPause();
		mAdapter.disableForegroundDispatch(this);
	}

	protected void onDestroy() {

		super.onDestroy();
	}

}
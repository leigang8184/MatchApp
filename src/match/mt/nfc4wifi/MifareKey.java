//------------------------------------------------------------------------------
//                         COPYRIGHT 2011 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 13SEP2011  James Shen                 	          Initial Creation
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package match.mt.nfc4wifi;

import android.annotation.SuppressLint;
//--------------------------------- IMPORTS ------------------------------------
import android.nfc.tech.MifareClassic;

//[------------------------------ MAIN CLASS ----------------------------------]
//--------------------------------- REVISIONS ----------------------------------
//Date       Name                 Tracking #         Description
//--------   -------------------  -------------      --------------------------
//13SEP2011  James Shen                 	         Initial Creation
////////////////////////////////////////////////////////////////////////////////
/**
* This class stands for mifare key A or key B.
* <hr>
* <b>&copy; Copyright 2011 Guidebee, Inc. All Rights Reserved.</b>
* 
* @version 1.00, 13/09/11
* @author Guidebee Pty Ltd.
*/
public class MifareKey implements ICardKey {

	////////////////////////////////////////////////////////////////////////////
	//--------------------------------- REVISIONS ------------------------------
	// Date       Name                 Tracking #         Description
	// ---------  -------------------  -------------      ----------------------
	// 13SEP2011  James Shen                 	          Initial Creation
	////////////////////////////////////////////////////////////////////////////
	/**
	 * Constructor.
	 * @param keyValue the key value.
	 */
	public MifareKey(byte[] keyValue) {
		if (keyValue == null || keyValue.length != 6) {
			throw new IllegalArgumentException("Invaid key");
		}
		key=new byte[6];
		System.arraycopy(keyValue, 0,key, 0, key.length);

	}
	
	
	////////////////////////////////////////////////////////////////////////////
	//--------------------------------- REVISIONS ------------------------------
	// Date       Name                 Tracking #         Description
	// ---------  -------------------  -------------      ----------------------
	// 13SEP2011  James Shen                 	          Initial Creation
	////////////////////////////////////////////////////////////////////////////
	/**
	 * Default constructor.
	 * 
	 */

	@SuppressLint("NewApi")
	public MifareKey() {
		key=MifareClassic.KEY_DEFAULT;

	}
	
	////////////////////////////////////////////////////////////////////////////
	//--------------------------------- REVISIONS ------------------------------
	// Date       Name                 Tracking #         Description
	// ---------  -------------------  -------------      ----------------------
	// 13SEP2011  James Shen                 	          Initial Creation
	////////////////////////////////////////////////////////////////////////////
	/* (non-Javadoc)
	 * @see com.example.nfc4wifi.ICardKey#getKey()
	 */ 
	public byte[] getKey(){
		return key;
	}
	
	////////////////////////////////////////////////////////////////////////////
	//--------------------------------- REVISIONS ------------------------------
	// Date       Name                 Tracking #         Description
	// ---------  -------------------  -------------      ----------------------
	// 13SEP2011  James Shen                 	          Initial Creation
	////////////////////////////////////////////////////////////////////////////
	/* (non-Javadoc)
	 * @see com.example.nfc4wifi.ICardKey#setKey(byte[])
	 */ 
	public void setKey(byte[] keyValue){
		if (keyValue == null || keyValue.length != 6) {
			throw new IllegalArgumentException("Invaid key");
		}
		key=new byte[6];
		System.arraycopy(keyValue, 0,key, 0, key.length);
	}

	/**
	 * this is the key values (6 bytes)
	 */
	private byte[] key;
}

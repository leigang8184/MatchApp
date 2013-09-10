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

//--------------------------------- IMPORTS ------------------------------------


import android.util.Log;


//[------------------------------ MAIN CLASS ----------------------------------]
//--------------------------------- REVISIONS ----------------------------------
//Date       Name                 Tracking #         Description
//--------   -------------------  -------------      --------------------------
//13SEP2011  James Shen                 	         Initial Creation
////////////////////////////////////////////////////////////////////////////////
/**
* This class stands for mifare class card.
* <hr>
* <b>&copy; Copyright 2011 Guidebee, Inc. All Rights Reserved.</b>
* 
* @version 1.00, 13/09/11
* @author Guidebee Pty Ltd.
*/
public class MifareClassCard implements ICard {
	
	////////////////////////////////////////////////////////////////////////////
	//--------------------------------- REVISIONS ------------------------------
	// Date       Name                 Tracking #         Description
	// ---------  -------------------  -------------      ----------------------
	// 13SEP2011  James Shen                 	          Initial Creation
	////////////////////////////////////////////////////////////////////////////
	/**
	 * Constructor.
	 * @param sectorSize size of the sectors.
	 */
	public MifareClassCard(int sectorSize){
		sectors=new MifareSector[sectorSize];
		SECTORCOUNT=sectorSize;
		initializeCard();
	}
	
	////////////////////////////////////////////////////////////////////////////
	//--------------------------------- REVISIONS ------------------------------
	// Date       Name                 Tracking #         Description
	// ---------  -------------------  -------------      ----------------------
	// 13SEP2011  James Shen                 	          Initial Creation
	////////////////////////////////////////////////////////////////////////////
	/* (non-Javadoc)
	 * @see com.example.nfc4wifi.ICard#initializeCard()
	 */
	@Override
	public void initializeCard(){
		for(int i=0;i<SECTORCOUNT;i++){
			sectors[i]=new MifareSector();
			sectors[i].sectorIndex=i;
			sectors[i].keyA=new MifareKey();
			sectors[i].keyB=new MifareKey();
			for(int j=0;j<4;j++){
				sectors[i].blocks[j]=new MifareBlock();
				sectors[i].blocks[j].blockIndex=i*4+j;
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////
	//--------------------------------- REVISIONS ------------------------------
	// Date       Name                 Tracking #         Description
	// ---------  -------------------  -------------      ----------------------
	// 13SEP2011  James Shen                 	          Initial Creation
	////////////////////////////////////////////////////////////////////////////
	/* (non-Javadoc)
	 * @see com.example.nfc4wifi.ICard#getSector(int)
	 */
	@Override
	public ISector getSector(int index){
		if(index>=SECTORCOUNT){
			throw new IllegalArgumentException("Invaid index for sector"); 
		}
		return sectors[index];
	}
	
	
	////////////////////////////////////////////////////////////////////////////
	//--------------------------------- REVISIONS ------------------------------
	// Date       Name                 Tracking #         Description
	// ---------  -------------------  -------------      ----------------------
	// 13SEP2011  James Shen                 	          Initial Creation
	////////////////////////////////////////////////////////////////////////////
	/* (non-Javadoc)
	 * @see com.example.nfc4wifi.ICard#setSector(int, com.example.nfc4wifi.MifareSector)
	 */
	@Override
	public void setSector(int index, MifareSector sector) {
		if (index >= SECTORCOUNT) {
			throw new IllegalArgumentException("Invaid index for sector");
		}
		sectors[index]=sector;
	}
	
	////////////////////////////////////////////////////////////////////////////
	//--------------------------------- REVISIONS ------------------------------
	// Date       Name                 Tracking #         Description
	// ---------  -------------------  -------------      ----------------------
	// 13SEP2011  James Shen                 	          Initial Creation
	////////////////////////////////////////////////////////////////////////////
	/* (non-Javadoc)
	 * @see com.example.nfc4wifi.ICard#getSectorCount()
	 */
	@Override
	public int getSectorCount(){
		return SECTORCOUNT;
		
	}
	
	////////////////////////////////////////////////////////////////////////////
	//--------------------------------- REVISIONS ------------------------------
	// Date       Name                 Tracking #         Description
	// ---------  -------------------  -------------      ----------------------
	// 13SEP2011  James Shen                 	          Initial Creation
	////////////////////////////////////////////////////////////////////////////
	/* (non-Javadoc)
	 * @see com.example.nfc4wifi.ICard#setSectorCount(int)
	 */
	@Override
	public void setSectorCount(int newCount){
		if(SECTORCOUNT<newCount){
			sectors=new MifareSector[newCount];
			initializeCard();
		}
		SECTORCOUNT=newCount;
		
	}

	////////////////////////////////////////////////////////////////////////////
	//--------------------------------- REVISIONS ------------------------------
	// Date       Name                 Tracking #         Description
	// ---------  -------------------  -------------      ----------------------
	// 13SEP2011  James Shen                 	          Initial Creation
	////////////////////////////////////////////////////////////////////////////
	/**
	 * debug print information.
	 */
	public void debugPrint() {
		int blockIndex=0;
		for (int i = 0; i < SECTORCOUNT; i++) {
			MifareSector sector = sectors[i];
			
			if (sector != null) {
				Log.i(TAG, "Sector " + i);
				for (int j = 0; j < ISector.BLOCKCOUNT; j++) {
					IBlock block = sector.blocks[j];
					if(block!=null){
						byte[] raw = block.getData();
						String hexString = "  Block "+ j +" "
								+Converter.getHexString(raw, raw.length)
								+"  ("+blockIndex+")";
						Log.i(TAG, hexString);
					}
					blockIndex++;

				}
			}
		}
	}
	
	/**
	 * the size of the sector.
	 */
	private int SECTORCOUNT=16;
	
	/**
	 * Log TAG.
	 */
	protected String TAG="MifareCardInfo";
	
	/**
	 * sectors.
	 */
	private MifareSector[] sectors;
}

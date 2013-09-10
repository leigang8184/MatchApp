package match.mt.nfc4wifi;

public interface IBlock {

	////////////////////////////////////////////////////////////////////////////
	//--------------------------------- REVISIONS ------------------------------
	// Date       Name                 Tracking #         Description
	// ---------  -------------------  -------------      ----------------------
	// 13SEP2011  James Shen                 	          Initial Creation
	////////////////////////////////////////////////////////////////////////////
	/**
	 * Get the data value.
	 * @return the byte array of the block.
	 */
	public abstract byte[] getData();

	////////////////////////////////////////////////////////////////////////////
	//--------------------------------- REVISIONS ------------------------------
	// Date       Name                 Tracking #         Description
	// ---------  -------------------  -------------      ----------------------
	// 13SEP2011  James Shen                 	          Initial Creation
	////////////////////////////////////////////////////////////////////////////
	/**
	 * Set block data 
	 * @param dataValue new block data
	 */
	public abstract void setData(byte[] dataValue);
	
	
	public int getBlockIndex() ;


	public void setBlockIndex(int blockIndex);


	public boolean isNeedRead() ;


	public void setNeedRead(boolean needRead);


	public boolean isNeedWrite() ;


	public void setNeedWrite(boolean needWrite);
	
	
	

}
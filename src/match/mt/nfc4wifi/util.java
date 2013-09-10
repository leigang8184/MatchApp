package match.mt.nfc4wifi;

import java.nio.charset.Charset;

public class util {
	/**
	 * input:输入的byte数组
	 * startIndex：截取开始的位置--从1开始
	 * endIndex  ：截取结束的位置--最大为input.length
	 * 返回的结果就是这段的byte数组，每个byte转化成的ascii码序列
	 * */
    public static String byte2AsciiString(byte[] input,int startIndex,int endIndex){
		if(startIndex>input.length||endIndex>input.length){
	    	return null;
		}
		int tmpIndex = 0;
		byte[] tmp = new byte[endIndex-startIndex+1];
		for(int i =startIndex-1;i<endIndex;i++){
			tmp[tmpIndex++]=input[i];
		}
		return new String(tmp);
    	
    }
    public static String byte2AsciiString(byte[] input){
		return byte2AsciiString(input,1,input.length);
    	
    }
    public static byte[] string2AsciiBytes(String input ,int toTal){
    	if(input==null||input.length()==0){
    		return null;
    	}
    	byte[] rslt  = new byte[toTal];
    	byte[] temp = input.getBytes(Charset.forName("US-ASCII"));
    	for(int i = 0 ;i<toTal;i++){
			if(i<temp.length){
				rslt[i]=temp[i];
			}else{
				rslt[i]=(byte) 0xFF;
			}
		}
    		
    	
    	return rslt;
    }
    
    public static void main(String[] args){
    	/*byte[] input = {110, 97, 109, 101, 58, 76, 71, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    	input[0]='L';
    	input[1]='G';
    	input[2]=':';*/
    	System.out.println("aa".getBytes());
    }
}

package match.mt.nfc4wifi;

import java.nio.charset.Charset;

public class util {
	/**
	 * input:�����byte����
	 * startIndex����ȡ��ʼ��λ��--��1��ʼ
	 * endIndex  ����ȡ������λ��--���Ϊinput.length
	 * ���صĽ��������ε�byte���飬ÿ��byteת���ɵ�ascii������
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

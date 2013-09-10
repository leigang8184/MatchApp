package match.mt.nfc4wifi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.GroupCipher;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiConfiguration.PairwiseCipher;
import android.net.wifi.WifiConfiguration.Protocol;
import android.net.wifi.WifiManager.WifiLock;

public class WifiAdmin {
    //����һ��WifiManager����
	public WifiManager mWifiManager;
	//����һ��WifiInfo����
	private WifiInfo mWifiInfo;
	//ɨ��������������б�
	private List<ScanResult> mWifiList;
	//���������б�
	private List<WifiConfiguration> mWifiConfigurations;
	WifiLock mWifiLock;
	public WifiAdmin(Context context){
		//ȡ��WifiManager����
		mWifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		//ȡ��WifiInfo����
		mWifiInfo=mWifiManager.getConnectionInfo();
	}
	
	public WifiInfo getConnectionInfo(){
		return mWifiManager.getConnectionInfo();
	}
	//��wifi
	public boolean OpenWifi()  
    {  
        boolean bRet = true;  
        if (!mWifiManager.isWifiEnabled())  
        {  
         bRet = mWifiManager.setWifiEnabled(true);    
        }  
        return bRet;  
    } 
	//�ر�wifi
	public void closeWifi(){
		if(!mWifiManager.isWifiEnabled()){
			mWifiManager.setWifiEnabled(false);
		}
	}
	 // ��鵱ǰwifi״̬  
    public int checkState() {  
        return mWifiManager.getWifiState();  
    }  
	//����wifiLock
	public void acquireWifiLock(){
		mWifiLock.acquire();
	}
	//����wifiLock
	public void releaseWifiLock(){
		//�ж��Ƿ�����
		if(mWifiLock.isHeld()){
			mWifiLock.acquire();
		}
	}
	//����һ��wifiLock
	public void createWifiLock(){
		mWifiLock=mWifiManager.createWifiLock("test");
	}
	//�õ����úõ�����
	public List<WifiConfiguration> getConfiguration(){
		return mWifiConfigurations;
	}
	public void setConfiguration(int index,WifiConfiguration cfg){
		mWifiConfigurations.set(index,cfg);
	}
	
	public void removeConfiguration(int index){
		mWifiManager.removeNetwork(index);
	}
	
	//ָ�����úõ������������
	public boolean connetionConfiguration(int index){
		if(index>mWifiConfigurations.size()){
			return false;
		}
		//�������ú�ָ��ID������
		int i = 0;
		while(mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId, true)&&i<3){
			if(mWifiManager.getConfiguredNetworks().get(index).status==0||mWifiManager.getConfiguredNetworks().get(index).status==2){
				return true;
			}else{
				try {
					i++;
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
		
	}
	public void startScan(){
		mWifiManager.startScan();
		//�õ�ɨ����
		mWifiList=mWifiManager.getScanResults();
		//�õ����úõ���������
		mWifiConfigurations=mWifiManager.getConfiguredNetworks();
	}
	//�õ������б�
	public List<ScanResult> getWifiList(){
		return mWifiList;
	}
	//�鿴ɨ����
	public StringBuffer lookUpScan(){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<mWifiList.size();i++){
			sb.append("Index_" + new Integer(i + 1).toString() + ":");
			 // ��ScanResult��Ϣת����һ���ַ�����  
            // ���аѰ�����BSSID��SSID��capabilities��frequency��level  
			sb.append((mWifiList.get(i)).toString()).append("\n");
		}
		return sb;	
	}
	public String getMacAddress(){
		return (mWifiInfo==null)?"NULL":mWifiInfo.getMacAddress();
	}
	public String getBSSID(){
		return (mWifiInfo==null)?"NULL":mWifiInfo.getBSSID();
	}
	public int getIpAddress(){
		return (mWifiInfo==null)?0:mWifiInfo.getIpAddress();
	}
	//�õ����ӵ�ID
	public int getNetWordId(){
		return (mWifiInfo==null)?-1:mWifiInfo.getNetworkId();
	}
	//�õ�wifiInfo��������Ϣ
	public String getWifiInfo(){
		return (mWifiInfo==null)?"NULL":mWifiInfo.toString();
	}
	//���һ�����粢����
	public boolean addNetWork(WifiConfiguration configuration){
		int wcgId=mWifiManager.addNetwork(configuration);
		int i = 0;
		while(mWifiManager.enableNetwork(wcgId, true)&&i<3){
			if(mWifiManager.getConfiguredNetworks().get(wcgId).status==0||mWifiManager.getConfiguredNetworks().get(wcgId).status==2){
				return true;
			}else{
				try {
					i++;
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	//�Ͽ�ָ��ID������
	public void disConnectionWifi(int netId){
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}
	/**
	 * input args
	 * name:
	 * pwd :
	 * 
	 * 
	 * output args
	 * Result:
	 * **/
	public String openWifiByNamePwd(String nameStr,String pwdStr){
		int wifiId = -1;
		int cfgdId = -1;
		int wcgId  = -1; 
		//��֤����Կ�������ܷ���
		String capabilities = "";
		StringBuffer name = new StringBuffer();
		name.append('"');
		name.append(nameStr);
		name.append('"');
		StringBuffer pwd = new StringBuffer();
		pwd.append('"');
		pwd.append(pwdStr);
		pwd.append('"');
		
		if(checkState()==3){
			closeWifi();
		}
		if(checkState()==3){
			mWifiManager.disconnect();
		}
		if(!OpenWifi()){
			
			 return "open wifi error��";
		}  
		 while(checkState() == WifiManager.WIFI_STATE_ENABLING )  
	        {  
	             try{  
	     //Ϊ�˱������һֱwhileѭ��������˯��100�����ڼ�⡭��  
	              Thread.currentThread();  
	              Thread.sleep(100);  
	            }  
	            catch(InterruptedException ie){  
	           }  
	        }  
		startScan();
		//�ж��Ƿ����name��ָ��wifi name
		for(int i=0;i<getWifiList().size();i++){
			ScanResult ScanRslt = (ScanResult)getWifiList().get(i);
			if(ScanRslt.SSID.equalsIgnoreCase(nameStr)){
				wifiId = i;
				capabilities = ScanRslt.capabilities;
			}
		}
		if(wifiId>-1){
			//�ж��Ƿ����Ѿ����õ�wifi,��������õľ�Ҫ�滻�ϵ�����
			for(int j=0;j<getConfiguration().size();j++){
				WifiConfiguration cfged = (WifiConfiguration)getConfiguration().get(j);
				
				if(cfged.SSID.equalsIgnoreCase("\"" + nameStr + "\"")){
					cfgdId = cfged.networkId;
				}
			}
			//������һ��
			WifiConfiguration wc = new WifiConfiguration();
			wc.allowedAuthAlgorithms.clear();  
			wc.allowedGroupCiphers.clear();  
			wc.allowedKeyManagement.clear();  
	         wc.allowedPairwiseCiphers.clear();  
	         wc.allowedProtocols.clear();  
	         wc.SSID = name.toString(); 
			 wc.status = WifiConfiguration.Status.ENABLED;
	         
	         
	         
	         
			if((capabilities.toUpperCase().indexOf("WPA")>0)&&(capabilities.toUpperCase().indexOf("PSK")>0)){
				//WPA-PSK/WPA2-PSK--[WPA-PSK-CCMP][WPA2-PSK-CCMP][WPS]
				//��֤����Կ�������ܷ���
				 //1.WPA
				  // ----------------------WPA-PSK���ӷ�ʽ------------------------
				//1.1  ��֤����(��Կ����Э��)WPA-PSK�������㷨��ASE   ���  [WPA-PSK-CCMP][WPS]
				//1.2 ��֤����WPA-PSK�������㷨��TKIP   ���  [WPA-PSK-TKIP][WPS]
				//1.3  ��֤����WPA-PSK�������㷨���Զ�   ���  [WPA-PSK-TKIP+CCMP][WPS],˵�������㷨��TKIP+CCMP����ASE��
				//1.4  ��֤�����Զ��������㷨���Զ�   ��� [WPA-PSK-TKIP+CCMP] [WPA2-PSK-TKIP+CCMP][WPS]
				   
				   wc.hiddenSSID = true;
				  
				                  
				 
				  wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
				  wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
				  wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
				  wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
				                   
				 // wc.preSharedKey = "\"" +pwd+"\"";
			    //��������
				boolean WPA = false;
				boolean WPA2 = false;
				boolean PSK = true;//�����֧�Ѿ��жϹ�PSK������
				boolean AES = false;
				boolean TKIP = false;
				if(capabilities.toUpperCase().indexOf("WPA-")>0){
					WPA = true;
				}
				if(capabilities.toUpperCase().indexOf("WPA2-")>0){
					WPA2 = true;
				}
				//��ΪWPA,WPA2��Ȼ��һ����true��
				//���WPA&&WPA2��Ϊfalse��˵����WPA����WPA2���Ͳ���Ҫ�ضϣ�
				//��Ϊtrue��˵����WPA��WPA2����Ҫ�ض�
				if(!(WPA&&WPA2)){
					
				}else{
					//��Ϊѡ��WPA����WPA2�����Զ������������㷨�໥�䲻Ӱ��
				}
				if(capabilities.toUpperCase().indexOf("CCMP")>0){
					AES = true;
				}
				if(capabilities.toUpperCase().indexOf("TKIP")>0){
					TKIP = true;
				}					
				//�����������
				//wc.preSharedKey = "xujiali520";  
				wc.preSharedKey = pwd.toString() ;  
		        wc.hiddenSSID = false;    
		        //WifiConfiguration.AuthAlgorithm.OPENһ������WPA��WEP��WifiConfiguration.AuthAlgorithm.SHARED
		        wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);//�����֧��Ȼ�����ֵ                                                       
		        //CCMP--AES,TKIP,WEP104--104bit����Կ��WEP40--40bit����Կ���������͵Ĳ��� 
		        if(TKIP){
		        	wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP); 
		        }
		        if(AES){
		        	wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP); 
		        }
		        
                //WifiConfiguration.KeyMgmt.WPA_PSK�����ʾ��PSK�ģ�WPA_EAP��ʾ��EAP��֤��ʽ����Ҫradius�ַ���Կ�ģ������NONE��ʾ��û��ʹ��WPA��ʽ������WEP��ʽ
		        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);    
		        //�����㷨��CCMP=ASE ��TKIP�ǱȽ��ϵķ�ʽ��NONE˵���ǲ���Ҫ��Կ���ܵģ����������ʹ��NONE�����������WPAר�õ�
		        if(TKIP){
			        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP); 
		        }
		        if(AES){
			        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP); 
		        }
		        //WPA��Э�����ͣ�RSN--WPA2  WPA--WPA
		        if(WPA){
			        wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA); 
		        }
		        if(WPA2){				          
		        	wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN); 
		        } 
		        //����״̬��CURRENT ��ʾ����ǵ�ǰ���ӵ������磬DISABLE��ʾ���᳢�����ӣ�ENABLED��ʾ���᳢������
		        //wc.status = WifiConfiguration.Status.ENABLED;
			}else if(capabilities.toUpperCase().indexOf("WEP")>0){
				//WEP --[WPS][WEP]����ϸ�����޹�
				//2.WEP
				// ---------------------WEP���ӷ�ʽ---------------------
				   wc.hiddenSSID = false;
				 
				   wc.status = WifiConfiguration.Status.ENABLED;
				 
				   wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
				   wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
				 
				   wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
				   wc.wepTxKeyIndex = 0;
				 
				    wc.wepKeys[0] =  pwd.toString() ;
				    //wc.preSharedKey = "\""+pwd+"\"";
				    //wc.wepKeys[0] = "\"" +pwd+"\"";
		            wc.hiddenSSID = true;    
		            wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);  
		            //wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);  
		            //wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);  
		            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);  
		            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);  
		            wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);  
		            wc.wepTxKeyIndex = 0;  
		            //wc.status = WifiConfiguration.Status.ENABLED;
			}else if((capabilities.toUpperCase().indexOf("WPA")>0)&&(capabilities.toUpperCase().indexOf("EAP")>0)){
				//WPA/WPA2 --[WPA-EAP-TKIP+CCMP][WPA2-EAP-TKIP+CCMP]ע�����û��[WPS]
				return "do not support WPA-EAP wifi";
			}else {
				//������ --[WPS]
				//4.������					  
				wc.hiddenSSID = false;
	            wc.status = WifiConfiguration.Status.ENABLED;
	            
	            wc.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
	            wc.allowedKeyManagement.set(KeyMgmt.WPA_EAP);
	            
	            wc.allowedProtocols.set(Protocol.WPA);
	            wc.allowedProtocols.set(Protocol.RSN);
	            
	            wc.allowedPairwiseCiphers.set(PairwiseCipher.TKIP);
	            wc.allowedPairwiseCiphers.set(PairwiseCipher.CCMP);
	            
	            wc.allowedGroupCiphers.set(GroupCipher.WEP40);
	            wc.allowedGroupCiphers.set(GroupCipher.WEP104);
	            wc.allowedGroupCiphers.set(GroupCipher.TKIP);
	            wc.allowedGroupCiphers.set(GroupCipher.CCMP);
	            wc.preSharedKey = "";
				wc.wepKeys[0] = "";  
				wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);  
				wc.wepTxKeyIndex = 0;  
				
			}
			wc.status = WifiConfiguration.Status.ENABLED;
			
			//���Դ���
	         //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	         /*if(wc.SSID=="\"LG\""){
	        	 rslt.setText("wifi name ok");
	         }else{
	        	 wc.SSID="\"LG\"";
	         }
	         if(wc.preSharedKey=="\"xujiali520\""){
	        	 rslt.setText("wifi password ok");
	         }else{
	        	 wc.preSharedKey="\"xujiali520\"";
	         }*/
	        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
			
			if(cfgdId!=-1){
				removeConfiguration(cfgdId);
			}
			/*WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			
			
			wcgId=wifi.addNetwork(wc);
			
			if(wcgId == -1){
				rslt.setText("addNetwork Fail!!!");
				return;
			}
			int i = 0;
			if(connetionConfiguration(wcgId)){
				rslt.setText("Success!");
			}else{
				rslt.setText("wifi Connect Fail!!!");
            }*/
			if(addNetWork(wc)){
				return "Success!";
			}else{
				return "wifi Connect Fail!!!";
            }
			
			
			/*
			
			
			
			while(mWifiManager.enableNetwork(wcgId, true)&&i<3){
				if(mWifiManager.getConfiguredNetworks().get(wcgId).status==0){
					return ;
				}else{
					try {
						i++;
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(addNetWork(wc)){
            	
            	return;
            }*/
			
			
			
			
			
			
		}else{//û��ɨ�赽
			return "wifi name error!";
		}
	}
	public String closeWififoRslt(){
		int i=0;
		if(getNetWordId()==-1){
			return "wifi connect status error!";
		}
		while((checkState() != WifiManager.WIFI_STATE_DISABLED )&&i<3)  
        {  
             try{  
     //Ϊ�˱������һֱwhileѭ��������˯��100�����ڼ�⡭��  
            	  i++;
            	  disConnectionWifi(getNetWordId());
                  Thread.currentThread();  
                  Thread.sleep(100); 
                  
            }  
            catch(InterruptedException ie){  
           }  
        }
		if((checkState() == WifiManager.WIFI_STATE_DISABLED)||(checkState() == WifiManager.WIFI_STATE_DISABLING)){
			return "Disconnect wifi Success!";
		}else {
			return "Disconnect wifi Fail!";
		}
		
	}
	/**
	 * input List
	 * ������       int       0.����wifi��1.�Ͽ���id��wifi
	 * �û�����  String
	 * ���룺       String
	 * 
	 * output Map
	 * �����       ��"Result"��ֵ String
	 * */
	public HashMap nfcWifiInterface(ArrayList inputPara){
		int type = (Integer) inputPara.get(0);
		HashMap map = new HashMap();
		String rslt = "";
		switch(type){
		    case 0:
		    	rslt = openWifiByNamePwd((String) inputPara.get(1), (String) inputPara.get(2));
		    	break;
		    case 1:
		    	rslt = closeWififoRslt();
		    	break;
		    default :
		    	rslt = "Operation type no supply";
	    		break;	
		}
		map.put("Result", rslt);
		
		return map;
				
	}
}

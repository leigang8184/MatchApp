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
    //定义一个WifiManager对象
	public WifiManager mWifiManager;
	//定义一个WifiInfo对象
	private WifiInfo mWifiInfo;
	//扫描出的网络连接列表
	private List<ScanResult> mWifiList;
	//网络连接列表
	private List<WifiConfiguration> mWifiConfigurations;
	WifiLock mWifiLock;
	public WifiAdmin(Context context){
		//取得WifiManager对象
		mWifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		//取得WifiInfo对象
		mWifiInfo=mWifiManager.getConnectionInfo();
	}
	
	public WifiInfo getConnectionInfo(){
		return mWifiManager.getConnectionInfo();
	}
	//打开wifi
	public boolean OpenWifi()  
    {  
        boolean bRet = true;  
        if (!mWifiManager.isWifiEnabled())  
        {  
         bRet = mWifiManager.setWifiEnabled(true);    
        }  
        return bRet;  
    } 
	//关闭wifi
	public void closeWifi(){
		if(!mWifiManager.isWifiEnabled()){
			mWifiManager.setWifiEnabled(false);
		}
	}
	 // 检查当前wifi状态  
    public int checkState() {  
        return mWifiManager.getWifiState();  
    }  
	//锁定wifiLock
	public void acquireWifiLock(){
		mWifiLock.acquire();
	}
	//解锁wifiLock
	public void releaseWifiLock(){
		//判断是否锁定
		if(mWifiLock.isHeld()){
			mWifiLock.acquire();
		}
	}
	//创建一个wifiLock
	public void createWifiLock(){
		mWifiLock=mWifiManager.createWifiLock("test");
	}
	//得到配置好的网络
	public List<WifiConfiguration> getConfiguration(){
		return mWifiConfigurations;
	}
	public void setConfiguration(int index,WifiConfiguration cfg){
		mWifiConfigurations.set(index,cfg);
	}
	
	public void removeConfiguration(int index){
		mWifiManager.removeNetwork(index);
	}
	
	//指定配置好的网络进行连接
	public boolean connetionConfiguration(int index){
		if(index>mWifiConfigurations.size()){
			return false;
		}
		//连接配置好指定ID的网络
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
		//得到扫描结果
		mWifiList=mWifiManager.getScanResults();
		//得到配置好的网络连接
		mWifiConfigurations=mWifiManager.getConfiguredNetworks();
	}
	//得到网络列表
	public List<ScanResult> getWifiList(){
		return mWifiList;
	}
	//查看扫描结果
	public StringBuffer lookUpScan(){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<mWifiList.size();i++){
			sb.append("Index_" + new Integer(i + 1).toString() + ":");
			 // 将ScanResult信息转换成一个字符串包  
            // 其中把包括：BSSID、SSID、capabilities、frequency、level  
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
	//得到连接的ID
	public int getNetWordId(){
		return (mWifiInfo==null)?-1:mWifiInfo.getNetworkId();
	}
	//得到wifiInfo的所有信息
	public String getWifiInfo(){
		return (mWifiInfo==null)?"NULL":mWifiInfo.toString();
	}
	//添加一个网络并连接
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
	//断开指定ID的网络
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
		//认证，密钥管理，加密方案
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
			
			 return "open wifi error！";
		}  
		 while(checkState() == WifiManager.WIFI_STATE_ENABLING )  
	        {  
	             try{  
	     //为了避免程序一直while循环，让它睡个100毫秒在检测……  
	              Thread.currentThread();  
	              Thread.sleep(100);  
	            }  
	            catch(InterruptedException ie){  
	           }  
	        }  
		startScan();
		//判断是否存在name所指的wifi name
		for(int i=0;i<getWifiList().size();i++){
			ScanResult ScanRslt = (ScanResult)getWifiList().get(i);
			if(ScanRslt.SSID.equalsIgnoreCase(nameStr)){
				wifiId = i;
				capabilities = ScanRslt.capabilities;
			}
		}
		if(wifiId>-1){
			//判断是否是已经配置的wifi,如果是配置的就要替换老的数据
			for(int j=0;j<getConfiguration().size();j++){
				WifiConfiguration cfged = (WifiConfiguration)getConfiguration().get(j);
				
				if(cfged.SSID.equalsIgnoreCase("\"" + nameStr + "\"")){
					cfgdId = cfged.networkId;
				}
			}
			//新配置一个
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
				//认证，密钥管理，加密方案
				 //1.WPA
				  // ----------------------WPA-PSK连接方式------------------------
				//1.1  认证类型(密钥管理协议)WPA-PSK，加密算法：ASE   结果  [WPA-PSK-CCMP][WPS]
				//1.2 认证类型WPA-PSK，加密算法：TKIP   结果  [WPA-PSK-TKIP][WPS]
				//1.3  认证类型WPA-PSK，加密算法：自动   结果  [WPA-PSK-TKIP+CCMP][WPS],说明加密算法是TKIP+CCMP（即ASE）
				//1.4  认证类型自动，加密算法：自动   结果 [WPA-PSK-TKIP+CCMP] [WPA2-PSK-TKIP+CCMP][WPS]
				   
				   wc.hiddenSSID = true;
				  
				                  
				 
				  wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
				  wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
				  wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
				  wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
				                   
				 // wc.preSharedKey = "\"" +pwd+"\"";
			    //参数处理
				boolean WPA = false;
				boolean WPA2 = false;
				boolean PSK = true;//这个分支已经判断过PSK存在了
				boolean AES = false;
				boolean TKIP = false;
				if(capabilities.toUpperCase().indexOf("WPA-")>0){
					WPA = true;
				}
				if(capabilities.toUpperCase().indexOf("WPA2-")>0){
					WPA2 = true;
				}
				//因为WPA,WPA2必然有一个是true，
				//因此WPA&&WPA2，为false，说明是WPA或者WPA2，就不需要截断，
				//若为true，说明是WPA和WPA2，需要截断
				if(!(WPA&&WPA2)){
					
				}else{
					//因为选择WPA或者WPA2或者自动，这个与加密算法相互间不影响
				}
				if(capabilities.toUpperCase().indexOf("CCMP")>0){
					AES = true;
				}
				if(capabilities.toUpperCase().indexOf("TKIP")>0){
					TKIP = true;
				}					
				//参数处理结束
				//wc.preSharedKey = "xujiali520";  
				wc.preSharedKey = pwd.toString() ;  
		        wc.hiddenSSID = false;    
		        //WifiConfiguration.AuthAlgorithm.OPEN一般用在WPA，WEP是WifiConfiguration.AuthAlgorithm.SHARED
		        wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);//这个分支必然是这个值                                                       
		        //CCMP--AES,TKIP,WEP104--104bit的密钥，WEP40--40bit的密钥，密码类型的参数 
		        if(TKIP){
		        	wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP); 
		        }
		        if(AES){
		        	wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP); 
		        }
		        
                //WifiConfiguration.KeyMgmt.WPA_PSK这个表示是PSK的，WPA_EAP表示是EAP认证方式，需要radius分发密钥的，如果是NONE表示是没有使用WPA方式或者是WEP方式
		        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);    
		        //加密算法，CCMP=ASE ，TKIP是比较老的方式，NONE说明是不需要密钥加密的，这个不建议使用NONE，这个参数是WPA专用的
		        if(TKIP){
			        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP); 
		        }
		        if(AES){
			        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP); 
		        }
		        //WPA的协议类型，RSN--WPA2  WPA--WPA
		        if(WPA){
			        wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA); 
		        }
		        if(WPA2){				          
		        	wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN); 
		        } 
		        //网络状态：CURRENT 表示这个是当前连接到的网络，DISABLE表示不会尝试连接，ENABLED表示将会尝试连接
		        //wc.status = WifiConfiguration.Status.ENABLED;
			}else if(capabilities.toUpperCase().indexOf("WEP")>0){
				//WEP --[WPS][WEP]跟详细设置无关
				//2.WEP
				// ---------------------WEP连接方式---------------------
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
				//WPA/WPA2 --[WPA-EAP-TKIP+CCMP][WPA2-EAP-TKIP+CCMP]注意后面没有[WPS]
				return "do not support WPA-EAP wifi";
			}else {
				//无密码 --[WPS]
				//4.无密码					  
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
			
			//测试代码
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
			
			
			
			
			
			
		}else{//没有扫描到
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
     //为了避免程序一直while循环，让它睡个100毫秒在检测……  
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
	 * 操作：       int       0.连接wifi，1.断开此id的wifi
	 * 用户名：  String
	 * 密码：       String
	 * 
	 * output Map
	 * 结果：       键"Result"，值 String
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

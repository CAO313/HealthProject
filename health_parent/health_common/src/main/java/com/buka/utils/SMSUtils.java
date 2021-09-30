package com.buka.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 短信发送工具类
 */
public class SMSUtils {
	public static final String VALIDATE_CODE = "SMS_217915240";//发送短信验证码
	public static final String ORDER_NOTICE = "SMS_159771588";//体检预约成功通知

	/**
	 * 发送短信
	 * @param phoneNumbers
	 * @param param
	 * @throws ClientException
	 */
	public static void sendShortMessage(String templateCode,String phoneNumbers,String param) throws ClientException{
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI5t7M987GSN1Cmn4DcEAd", "bsevbL6imKgTMQbnLmSyaAwwgq1N36");
		IAcsClient client = new DefaultAcsClient(profile);

		CommonRequest request = new CommonRequest();
		request.setSysMethod(MethodType.POST);
		request.setSysDomain("dysmsapi.aliyuncs.com");
		request.setSysVersion("2017-05-25");
		request.setSysAction("SendSms");
		request.putQueryParameter("PhoneNumbers", phoneNumbers);
		request.putQueryParameter("SignName", "布卡教育");
		request.putQueryParameter("TemplateCode", templateCode);
		request.putQueryParameter("TemplateParam", "{\"code\":\""+param+"\"}");
		try {
			CommonResponse response = client.getCommonResponse(request);
			System.out.println(response.getData());
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		try{
		SMSUtils.sendShortMessage("SMS_217915240","15537860615","123");
	}catch (Exception e){
			e.printStackTrace();
		}
	}
}


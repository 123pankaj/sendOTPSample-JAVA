package com.sendotp;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.JSONObject;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;


/*
* This code is based on jersey-client library.
* For gradle based project use compile 'com.sun.jersey:jersey-client:1.18.4'
* You can also download the jar and add it to you project.
* */
public class SendOTPServer {

    private static String otp;

    //Base URL
    public static String baseUrl = "https://sendotp.msg91.com/api";

    // Your application key
    public static String applicationKey = "Replace Your ApplicationKey from sendOTP.com";
    /** This function is used to send OTP message on mobile number
    * */
    public static void generateOTP(String countryCode, String mobileNumber){
        try {
            Client client = Client.create();
            String Url  = baseUrl+"/generateOTP";
            WebResource webResource = client.resource(Url);

            HashMap<String, String> requestBodyMap = new HashMap();
            requestBodyMap.put("countryCode",countryCode);
            requestBodyMap.put("mobileNumber",mobileNumber);
            requestBodyMap.put("getGeneratedOTP","true");
            JSONObject requestBodyJsonObject = new JSONObject(requestBodyMap);
            String input = requestBodyJsonObject.toString();

            ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
                    .header("application-Key", applicationKey)
                    .post(ClientResponse.class, input);
            String output = response.getEntity(String.class);
            System.out.println("Request: "+output);
            //fetch your oneTimePassword and save it to session or db
            JSONObject jsonObj = new JSONObject(output);
            JSONObject responseJson = jsonObj.getJSONObject("response");
            otp = responseJson.getString("oneTimePassword");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * This function is used to send OTP message on mobile number
    * */
    public static void verifyOTP(String oneTimePassword){
        try {
            //fetch your oneTimePassword from session or db
            //and compare it with the OTP sent from clien
            if(otp.equalsIgnoreCase(oneTimePassword)){
                System.out.println("Verified");
            }
            else{
                System.out.println("Invalid code");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
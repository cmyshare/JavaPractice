package com.open.office.word;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2024/8/19 18:10
 * @desc
 */
public class shop_auth {
    public static void main(String[] args) throws ParseException, IOException {
        //shop_auth();
        String[] token_shop_level = get_token_shop_level("55597779555058746a6c4d4d7163624c", 1186353L, "4c4958584c6671475359796741724f4c70495a75664c6b52494d677654475142", 121297L);
        System.out.println("获取访问令牌"+token_shop_level[0]);
        //String[] strings = get_access_token_shop_level("4b7251706661727068504e4669764950", 1186353L, "4c4958584c6671475359796741724f4c70495a75664c6b52494d677654475142", 121297L);
        //System.out.println("刷新访问令牌"+strings[0]);
    }

    //generate auth url
    public static void shop_auth() {
        long timest = System.currentTimeMillis() / 1000L;
        String host = "https://partner.test-stable.shopeemobile.com";
        String path = "/api/v2/shop/auth_partner";
        String redirect_url = "https://www.baidu.com/";
        long partner_id = 1186353L;
        String tmp_partner_key = "4c4958584c6671475359796741724f4c70495a75664c6b52494d677654475142";
        String tmp_base_string = String.format("%s%s%s", partner_id, path, timest);
        byte[] partner_key;
        byte[] base_string;
        String sign = "";
        try {
            base_string = tmp_base_string.getBytes("UTF-8");
            partner_key = tmp_partner_key.getBytes("UTF-8");
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(partner_key, "HmacSHA256");
            mac.init(secret_key);
            sign = String.format("%064x", new BigInteger(1, mac.doFinal(base_string)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = host + path + String.format("?partner_id=%s&timestamp=%s&sign=%s&redirect=%s", partner_id, timest, sign, redirect_url);
        System.out.println(url);
    }

    //shop request for access token for the first time
    public static String[] get_token_shop_level(String code, long partner_id, String tmp_partner_key, long shop_id) throws ParseException, IOException {
        String[] res = new String[2];
        long timest = System.currentTimeMillis() / 1000L;
        String host = "https://partner.test-stable.shopeemobile.com";
        String path = "/api/v2/auth/token/get";
        String tmp_base_string = String.format("%s%s%s", partner_id, path, timest);
        byte[] partner_key;
        byte[] base_string;
        BigInteger sign = null;
        String result = "";
        try {
            base_string = tmp_base_string.getBytes("UTF-8");
            partner_key = tmp_partner_key.getBytes("UTF-8");
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(partner_key, "HmacSHA256");
            mac.init(secret_key);
            sign = new BigInteger(1, mac.doFinal(base_string));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String tmp_url = host + path + String.format("?partner_id=%s&timestamp=%s&sign=%s", partner_id, timest, String.format("%032x", sign));
        URL url = new URL(tmp_url);
        HttpURLConnection conn = null;
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Map<String, Object> map = new HashMap<>();
            map.put("code", code);
            map.put("shop_id", shop_id);
            map.put("partner_id", partner_id);
            String json = JSON.toJSONString(map);
            conn.connect();
            out = new PrintWriter(conn.getOutputStream());
            out.print(json);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            res[0] = (String) jsonObject.get("access_token");
            res[1] = (String) jsonObject.get("refresh_token");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return res;
    }

    //main account request for the access token for the first time
    public static String[] get_token_account_level(String code, long partner_id, String tmp_partner_key, long main_account_id) throws ParseException, IOException {
        String[] res = new String[2];
        long timest = System.currentTimeMillis() / 1000L;
        String host = "https://partner.test-stable.shopeemobile.com";
        String path = "/api/v2/auth/token/get";
        String tmp_base_string = String.format("%s%s%s", partner_id, path, timest);
        byte[] partner_key;
        byte[] base_string;
        BigInteger sign = null;
        String result = "";
        try {
            base_string = tmp_base_string.getBytes("UTF-8");
            partner_key = tmp_partner_key.getBytes("UTF-8");
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(partner_key, "HmacSHA256");
            mac.init(secret_key);
            sign = new BigInteger(1, mac.doFinal(base_string));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String tmp_url = host + path + String.format("?partner_id=%s&timestamp=%s&sign=%s", partner_id, timest, String.format("%032x", sign));
        URL url = new URL(tmp_url);
        HttpURLConnection conn = null;
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Map<String, Object> map = new HashMap<>();
            map.put("code", code);
            map.put("main_account_id", main_account_id);
            map.put("partner_id", partner_id);
            String json = JSON.toJSONString(map);
            conn.connect();
            out = new PrintWriter(conn.getOutputStream());
            out.print(json);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            res[0] = (String) jsonObject.get("access_token");
            res[1] = (String) jsonObject.get("refresh_token");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return res;
    }

    //shop refresh the access token
    public static String[] get_access_token_shop_level(String refresh_token,long partner_id,String tmp_partner_key,long shop_id) throws ParseException,IOException{
        String[] res = new String[2];
        long timest = System.currentTimeMillis() / 1000L;
        String host = "https://partner.test-stable.shopeemobile.com";
        String path = "/api/v2/auth/access_token/get";
        String tmp_base_string = String.format("%s%s%s", partner_id, path, timest);
        byte[] partner_key;
        byte[] base_string;
        BigInteger sign = null;
        String result = "";
        try {
            base_string = tmp_base_string.getBytes("UTF-8");
            partner_key = tmp_partner_key.getBytes("UTF-8");
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(partner_key, "HmacSHA256");
            mac.init(secret_key);
            sign = new BigInteger(1,mac.doFinal(base_string));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String tmp_url = host + path + String.format("?partner_id=%s&timestamp=%s&sign=%s", partner_id,timest, String.format("%032x",sign));
        URL url = new URL(tmp_url);
        HttpURLConnection conn = null;
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Map<String,Object> map = new HashMap<>();
            map.put("refresh_token",refresh_token);
            map.put("shop_id",shop_id);
            map.put("partner_id",partner_id);
            String json = JSON.toJSONString(map);
            conn.connect();
            out = new PrintWriter(conn.getOutputStream());
            out.print(json);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while((line=in.readLine())!=null){
                result +=line;
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            res[0] = (String) jsonObject.get("access_token");
            res[1] = (String) jsonObject.get("refresh_token");
        } catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        return res;
    }

    //merchant refresh the access token
    public static String[] get_access_token_merchant_level(String refresh_token,long partner_id,String tmp_partner_key,long merchant_id) throws ParseException,IOException{
        String[] res = new String[2];
        long timest = System.currentTimeMillis() / 1000L;
        String host = "https://partner.test-stable.shopeemobile.com";
        String path = "/api/v2/auth/access_token/get";
        String tmp_base_string = String.format("%s%s%s", partner_id, path, timest);
        byte[] partner_key;
        byte[] base_string;
        BigInteger sign = null;
        String result = "";
        try {
            base_string = tmp_base_string.getBytes("UTF-8");
            partner_key = tmp_partner_key.getBytes("UTF-8");
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(partner_key, "HmacSHA256");
            mac.init(secret_key);
            sign = new BigInteger(1,mac.doFinal(base_string));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String tmp_url = host + path + String.format("?partner_id=%s&timestamp=%s&sign=%s", partner_id,timest, String.format("%032x",sign));
        URL url = new URL(tmp_url);
        HttpURLConnection conn = null;
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Map<String,Object> map = new HashMap<>();
            map.put("refresh_token",refresh_token);
            map.put("merchant_id",merchant_id);
            map.put("partner_id",partner_id);
            String json = JSON.toJSONString(map);
            conn.connect();
            out = new PrintWriter(conn.getOutputStream());
            out.print(json);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while((line=in.readLine())!=null){
                result +=line;
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            res[0] = (String) jsonObject.get("access_token");
            res[1] = (String) jsonObject.get("refresh_token");
        } catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        return res;
    }

}
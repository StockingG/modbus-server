package com.meller.modbusserver.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author chenleijun
 * @date 2018/1/11.
 * @remark
 */
public class HttpClientUtil {
    /**
     * 每个路由最大连接数
     */
    public static final int MAX_CONN_PER_ROUTE = 500;
    /**
     * 整个连接池最大连接数
     */
    public static final int MAX_CONN_TOTAL = 5000;
    /**
     * 1秒钟 毫秒数
     */
    public static final int SECONDS = 1000;
    /**
     * 数据传输过程中数据包之间间隔的最大时间
     */
    public static final int SOCKET_TIMEOUT = 5 * SECONDS;
    /**
     * 三次握手完成时间
     */
    public static final int CONNECT_TIMEOUT = 10 * SECONDS;
    /**
     * 从连接池获取连接的超时时间
     */
    public static final int CONNECTION_REQUEST_TIMEOUT = 5 * SECONDS;


    private static HttpClient apiHttpClient = null;


    /**
     * 创建 httpclient
     *
     * @return
     */
    public static HttpClient getApiHttpClient() {
        if (apiHttpClient == null) {
            apiHttpClient = createDefaultHttpClient(createDefaultHttpClientConfig());
        }
        return apiHttpClient;
    }

    public static RequestConfig createDefaultHttpClientConfig() {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setRedirectsEnabled(false).build();
        return requestConfig;
    }

    public static CloseableHttpClient createDefaultHttpClient(RequestConfig config) {
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(config)
                .setMaxConnPerRoute(MAX_CONN_PER_ROUTE).setMaxConnTotal(MAX_CONN_TOTAL).build();
        return httpclient;
    }

    /**
     * http execute 执行http请求
     *
     * @param request
     * @param resultEncode 返回编码
     * @return
     */
    private static String httpClientExecute(HttpUriRequest request, String resultEncode) {
        HttpResponse response = null;
        String result = null;
        try {
            response = getApiHttpClient().execute(request);
            HttpEntity resEntity = response.getEntity();
            result = EntityUtils.toString(resEntity, resultEncode);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
        return null;
    }

    /**
     * http get请求
     *
     * @param requestUri 请求URL
     * @return
     */
    public static String httpClientGet(String requestUri) {
        return httpClientGet(requestUri, null, null);
    }

    /**
     * http get请求
     *
     * @param requestUri   请求URL
     * @param headerParam  请求头参数
     * @param resultEncode 返回值编码
     * @return
     */
    public static String httpClientGet(String requestUri, Map<String, String> headerParam, String resultEncode) {
        try {
            HttpGet httpGet = new HttpGet(requestUri);
            Optional.ofNullable(headerParam).orElse(new HashMap<>(16)).forEach(httpGet::setHeader);
            return httpClientExecute(httpGet, resultEncode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * http post application/x-www-form-urlencoded
     *
     * @param requestUri   请求URL
     * @param requestParam 请求参数
     * @return
     */
    public static String httpClientPost(String requestUri, Map<String, String> requestParam) {
        return httpClientPost(requestUri, null, requestParam, null, null);
    }

    /**
     * http post application/x-www-form-urlencoded
     *
     * @param requestUri    请求URL
     * @param headerParam   请求头参数
     * @param requestParam  请求参数
     * @param requestEncode 请求参数编码
     * @param resultEncode  返回参数编码
     * @return
     */
    public static String httpClientPost(String requestUri, Map<String, String> headerParam, Map<String, String> requestParam, String requestEncode, String resultEncode) {
        try {
            HttpPost httpPost = new HttpPost(requestUri);
            Optional.ofNullable(headerParam).orElse(new HashMap<>(16)).forEach(httpPost::setHeader);
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            Optional.ofNullable(requestParam).orElse(new HashMap<>(16)).forEach((k, v) -> nameValuePairList.add(new BasicNameValuePair(k, v)));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, requestEncode));
            return httpClientExecute(httpPost, resultEncode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * http post application/json
     *
     * @param requestUri 请求URL
     * @param assertion  json字符串
     * @return
     */
    public static String httpClientPostJson(String requestUri, String assertion) {
        return httpClientPostJson(requestUri, null, assertion, ContentType.APPLICATION_JSON, null);
    }

    /**
     * http post application/json
     *
     * @param requestUri   请求URL
     * @param headerParam  请求头参数
     * @param assertion    json字符串
     * @param contentType
     * @param resultEncode 返回值编码
     * @return
     */
    public static String httpClientPostJson(String requestUri, Map<String, String> headerParam, String assertion, ContentType contentType, String resultEncode) {
        try {
            HttpPost httpPost = new HttpPost(requestUri);
            Optional.ofNullable(headerParam).orElse(new HashMap<>(16)).forEach((k, v) -> httpPost.setHeader(k, v));
            HttpEntity httpEntity = new StringEntity(assertion, contentType);
            httpPost.setEntity(httpEntity);
            return httpClientExecute(httpPost, resultEncode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * http post form-data
     *
     * @param requestUri   请求url
     * @param requestParam 请求入参
     * @return
     */
    public static String httpClientPostFormData(String requestUri, Map<String, String> requestParam) {

        return httpClientPostFormData(requestUri, null, requestParam);
    }

    /**
     * http post form-data
     *
     * @param requestUri   请求url
     * @param headerParam  请求头参数
     * @param requestParam 请求入参
     * @return
     */
    public static String httpClientPostFormData(String requestUri, Map<String, String> headerParam, Map<String, String> requestParam) {

        return httpClientPostFormData(requestUri, headerParam, requestParam, null, null);
    }

    /**
     * http post form-data /multipart/form-data
     *
     * @param requestUri   请求url
     * @param headerParam  请求头参数
     * @param requestParam 请求参数
     * @param fileName     文件参数名
     * @param file         文件
     * @return
     */
    public static String httpClientPostFormData(String requestUri, Map<String, String> headerParam, Map<String, String> requestParam, String fileName, File file) {

        return httpClientPostFormData(requestUri, headerParam, requestParam, fileName, file, null, null);
    }

    /**
     * http post form-data /multipart/form-data
     *
     * @param requestUri    请求url
     * @param headerParam   请求头参数
     * @param requestParam  请求参数
     * @param fileName      文件参数名
     * @param file          文件
     * @param requestEncode 请求参数编码
     * @param resultEncode  返回参数编码
     * @return
     */
    public static String httpClientPostFormData(String requestUri, Map<String, String> headerParam, Map<String, String> requestParam, String fileName, File file, String requestEncode, String resultEncode) {
        try {

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            if (file != null && StringUtils.isNotBlank(fileName)) {
                multipartEntityBuilder.addBinaryBody(fileName, file).setMode(HttpMultipartMode.RFC6532);
            }
            Optional.ofNullable(requestParam).orElse(new HashMap<>(16)).forEach((k, v) -> multipartEntityBuilder.addPart(k, new StringBody(v, ContentType.create("text/plain", Charset.forName(requestEncode)))));
            HttpPost httpPost = new HttpPost(requestUri);
            Optional.ofNullable(headerParam).orElse(new HashMap<>(16)).forEach((k, v) -> httpPost.setHeader(k, v));
            httpPost.setEntity(multipartEntityBuilder.build());
            return httpClientExecute(httpPost, resultEncode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据url获取远程文件资源
     *
     * @param url 访问地址
     * @return byte数组
     */
    public static byte[] getByteFromUrl(String url) {
        try {
            byte[] data;
            HttpGet get = new HttpGet(url);
            HttpResponse res = getApiHttpClient().execute(get);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                data = EntityUtils.toByteArray(res.getEntity());
                return data;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}

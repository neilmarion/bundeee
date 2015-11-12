package com.salarium.bundy.api;

import com.salarium.bundy.state.MethodsStateManagerImplementation;
import com.salarium.bundy.values.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.UnsupportedEncodingException;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpConnectionParams;

/** Abstract class which manages requests to the Salarium server
 *
 * @author Neil Marion dela Cruz
 */
public abstract class SalariumAPIConnection {
    private String host = Values.API.ServerEndPoints.SERVER_DOMAIN_NAME;
    protected List<NameValuePair> requestData = new ArrayList<NameValuePair>();
    protected String actionPath = null;
    public static final int SUCCESS = 1;
    public static final int FAIL = 0;

    /** Set the action path of the API to make the request to
     *
     * @param actionPath    the path to the API end-point
     */
    protected void setActionPath(String actionPath) {
        this.actionPath = actionPath;
    }

    /** Make a request to the server
     *
     * @param requestMethodType     Values.API.ServerEndPoints.RequestMethods.POST or Values.API.ServerEndPoints.RequestMethods.GET
     * @return                      response data
     * @throws java.io.IOException
     * @throws org.json.JSONException
     */
    protected JSONObject request(int requestMethodType)
        throws IOException, JSONException {
        return triggerRequest(requestMethodType);
    }

    /** Make a POST request to the server
     *
     * @return                      response data
     * @throws java.io.IOException
     * @throws org.json.JSONException
     */
    protected JSONObject postRequest() throws IOException, JSONException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
        HttpConnectionParams.setSoTimeout(httpParameters, 5000);

        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpPost httppost = new HttpPost(host + "/" + actionPath);
        JSONObject responseJSONObject = null;
        httppost.setHeader(
            "Accept", "application/json, text/javascript, */*; q=0.01"
        );
        httppost.setEntity(new UrlEncodedFormEntity(requestData));
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        System.out.println(responseString);
        responseJSONObject = new JSONObject(responseString);

        return responseJSONObject;
    }

    /** Make a GET request to the server
     *
     * @return                      response data
     * @throws java.io.IOException
     * @throws org.json.JSONException
     */
    protected JSONObject getRequest() throws IOException, JSONException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
        HttpConnectionParams.setSoTimeout(httpParameters, 5000);

        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpGet httpget = null;
        JSONObject responseJSONObject = null;
        httpget = new HttpGet(host + "/" + actionPath +
            Util.buildRequestGetParameters(requestData)
        );
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        System.out.println(responseString);
        responseJSONObject = new JSONObject(responseString);

        return responseJSONObject;
    }

    /** Set the request parameters
     * @param   key The key of the param
     * @param   key The value of the param corresponding to the key
     * @throws java.io.IOException
     * @throws org.json.JSONException
     */
    protected void setRequestData(String key, String val) {
        requestData.add(new BasicNameValuePair(key, val));
    }

    /** Call the request to the server
     * @param   requestMethodType   Values.API.ServerEndPoints.RequestMethods.POST or Values.API.ServerEndPoints.RequestMethods.GET
     * @throws  java.io.IOException
     * @throws  org.json.JSONException
     */
    private JSONObject triggerRequest(int requestMethodType)
        throws IOException, JSONException {
        JSONObject result = null;

        switch (requestMethodType) {
            case Values.API.ServerEndPoints.RequestMethods.POST:
                result = postRequest();
                break;
            case Values.API.ServerEndPoints.RequestMethods.GET:
                result = getRequest();
                break;
        }

        return result;
    }

    /** Exception class being thrown when parameters are incomplete
     *
     * @author Neil Marion dela Cruz
     */
    public class IncompleteParametersException extends Exception {
        public IncompleteParametersException(String message) {
            super(message);
        }
    }

    /** Utility class for SalariumAPIConnection
     *
     * @author Neil Marion dela Cruz
     */
    static class Util {
        static String buildRequestGetParameters(List<NameValuePair> list) {
            String query = "?";

            try {
                for(NameValuePair pair : list) {
                    query = query + pair.getName() + "=" +
                        URLEncoder.encode(pair.getValue(), "UTF-8") + "&";
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace(System.out);
            }

            return query;
        }
    }
}

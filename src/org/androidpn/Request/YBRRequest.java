package org.androidpn.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

import org.androidpn.client.util.SysApplicationImpl;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class YBRRequest {
	String m_strRequestTag; // Tag to distinguish the request
	String m_strUrl = "http://"
			+ SysApplicationImpl.getInstance().loadProperties()
					.getProperty("xmppHost", "127.0.0.1") + ":8080/androidpn/"; // The
																				// URL
																				// of
																				// the
	// request
	AsyncHttpClient m_pHttpClient; // The async http client
	RequestParams m_pParams; // The parameters for request
	JSONObject m_pResponseJson; // The response json
	boolean m_bSilent; // If pop up error message when the request failed
	boolean m_bRequestCancelled; // YES when Cancel function is called
	boolean m_bSucceed; // YES if the request succeeds.
	YBRDataRequestHandler m_pResponseHandler; // The handler to deal with the
												// success/failure of the
												// request

	public String getM_strRequestTag() {
		return m_strRequestTag;
	}

	public void setM_strRequestTag(String m_strRequestTag) {
		this.m_strRequestTag = m_strRequestTag;
	}

	public String getM_strUrl() {
		return m_strUrl;
	}

	public void setM_strUrl(String m_strUrl) {
		this.m_strUrl = m_strUrl;
	}

	public AsyncHttpClient getM_pHttpClient() {
		return m_pHttpClient;
	}

	public void setM_pHttpClient(AsyncHttpClient m_pHttpClient) {
		this.m_pHttpClient = m_pHttpClient;
	}

	public RequestParams getM_pParams() {
		return m_pParams;
	}

	public void setM_pParams(RequestParams m_pParams) {
		this.m_pParams = m_pParams;
	}

	public JSONObject getM_pResponseJson() {
		return m_pResponseJson;
	}

	public void setM_pResponseJson(JSONObject m_pResponseJson) {
		this.m_pResponseJson = m_pResponseJson;
	}

	public boolean isM_bSilent() {
		return m_bSilent;
	}

	public void setM_bSilent(boolean m_bSilent) {
		this.m_bSilent = m_bSilent;
	}

	public boolean isM_bRequestCancelled() {
		return m_bRequestCancelled;
	}

	public void setM_bRequestCancelled(boolean m_bRequestCancelled) {
		this.m_bRequestCancelled = m_bRequestCancelled;
	}

	public boolean isM_bSucceed() {
		return m_bSucceed;
	}

	public void setM_bSucceed(boolean m_bSucceed) {
		this.m_bSucceed = m_bSucceed;
	}

	public YBRDataRequestHandler getM_pResponseHandler() {
		return m_pResponseHandler;
	}

	public void setM_pResponseHandler(YBRDataRequestHandler m_pResponseHandler) {
		this.m_pResponseHandler = m_pResponseHandler;
	}

	JsonHttpResponseHandler pResponseHandler = new JsonHttpResponseHandler() {

		@Override
		public void onStart() {
		}

		@Override
		public void onSuccess(int statusCode, org.apache.http.Header[] headers,
				JSONObject response) {
			m_bSucceed = true;
			if (m_pResponseHandler != null) {
				System.out.println(response);
				m_pResponseHandler.OnSuccess(statusCode, headers, response);
			}

		}

		@Override
		public void onFailure(int statusCode, org.apache.http.Header[] headers,
				java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
			m_bSucceed = false;

			if (m_pResponseHandler != null)
				m_pResponseHandler.OnFailure(statusCode, headers, throwable,
						errorResponse);
		}

		@Override
		public void onFailure(int statusCode, org.apache.http.Header[] headers,
				String responseString, Throwable throwable) {
			System.out.println("responseString=" + responseString);
		};

		public void onFailure(int statusCode, org.apache.http.Header[] headers,
				Throwable throwable, org.json.JSONArray errorResponse) {
			throwable.printStackTrace();
		};

	};

	// Initialization
	public YBRRequest(String argTag, YBRDataRequestHandler argHandler) {
		this.m_strRequestTag = argTag;
		m_pHttpClient = new AsyncHttpClient();
		m_pParams = new RequestParams();
		m_pHttpClient.setTimeout(20000);
		m_pResponseHandler = argHandler;
	}

	// Start the asynchronous request
	public void StartRequest() {
		String url = m_strUrl + m_strRequestTag + ".do";
		System.out.println(url + m_pParams.toString());
		m_pHttpClient.post(url, m_pParams, pResponseHandler);
	}

	// Set the parameters for the request
	public void SetParamValue(String argKey, String argValue) {
		m_pParams.put(argKey, argValue);
	}

	// Set the image data parameter for the request
	public void AddImageData(File argImage, String argKey)
			throws FileNotFoundException {
		m_pParams.put(argKey, argImage);

	}

	// Set the image data parameter for the request
	public void AddImageData(File[] argImage, String argKey)
			throws FileNotFoundException {
		m_pParams.put(argKey, argImage);

	}

	// Cancel the request
	public void Cancel() {
		m_pHttpClient.cancelAllRequests(true);
	}
}

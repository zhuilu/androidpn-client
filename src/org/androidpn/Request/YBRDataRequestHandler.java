package org.androidpn.Request;

import org.json.JSONObject;

public abstract class YBRDataRequestHandler<T> {

	public abstract void OnSuccess(int statusCode,
			org.apache.http.Header[] headers, T response);

	public abstract void OnFailure(int statusCode,
			org.apache.http.Header[] headers, java.lang.Throwable throwable,
			org.json.JSONObject errorResponse);

}

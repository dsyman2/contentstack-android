package com.builtio.volley.request;

import com.builtio.volley.NetworkResponse;
import com.builtio.volley.Request;
import com.builtio.volley.Response;
import com.builtio.volley.error.AuthFailureError;
import com.builtio.volley.error.ParseError;
import com.builtio.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;
 
/**
 * Volley adapter for JSON requests that will be parsed into Java objects by Gson.
 */
public class GsonRequest<T> extends Request<T> {
    /*private final Gson gson = new Gson();*/
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Map<String, String> params;
    private final Response.Listener<T> listener;
 
    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */
    public GsonRequest(String url, Class<T> clazz, Map<String, String> headers,
            Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.params = null;
        this.listener = listener;
    }
    
    /**
     * Make a request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */
    public GsonRequest(int type, String url, Class<T> clazz, Map<String, String> headers,
    		Map<String, String> params,
            Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(type, url, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.params = params;
        this.listener = listener;
    }
 
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }
    
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
    	return params != null ? params : super.getParams();
    }
 
    @Override
    protected void deliverResponse(T response) {
    	if(null != listener){
    		listener.onResponse(response);
    	}
    }
 
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));
            /*return Response.success(
                    gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));*/
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
		return null;
    }
}
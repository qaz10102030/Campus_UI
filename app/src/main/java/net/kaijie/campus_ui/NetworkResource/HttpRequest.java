package net.kaijie.campus_ui.NetworkResource;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rabbit on 2017/10/3.
 */
public class HttpRequest {
    private String Server = "http://140.125.33.140:3030";
    private RequestQueue mQueue;
    private StringRequest getRequest;
    private int VolleyTimeOut = 20000;
    private VolleyCallback volleycallback;

    public Response.ErrorListener errorlistener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            volleycallback.onError(volleyError.toString());
        }
    };

    public HttpRequest(Context context) {
        FakeTrustManager.allowAllSSL();
        this.mQueue  = Volley.newRequestQueue(context);
    }

    public void getCourse(final VolleyCallback callback) {
        volleycallback = callback;
        getRequest = new StringRequest(Server + "/course_info",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        volleycallback.onSuccess("course", response);
                    }
                }, errorlistener);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(VolleyTimeOut,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(getRequest);
    }

    public void getSticker(final VolleyCallback callback,String url) {
        volleycallback = callback;
        ImageRequest getImage = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                volleycallback.onImageSuccess("getimage", response);
            }
        }, 400, 400, ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565, errorlistener);
        getImage.setRetryPolicy(new DefaultRetryPolicy(VolleyTimeOut,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(getImage);
    }

    public void postCourt(final VolleyCallback callback, final String date) {
        volleycallback = callback;
        getRequest = new StringRequest(Request.Method.POST,Server + "/court_info",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        volleycallback.onSuccess("court", response);
                    }
                }, errorlistener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("time",date);
                return params;
            }
        };
        getRequest.setRetryPolicy(new DefaultRetryPolicy(VolleyTimeOut,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(getRequest);
    }
    public void modifyNote(final VolleyCallback callback, final String serial, final String org_title, final String org_content, final String new_title, final String new_content,final int mothed) {
        volleycallback = callback;
        getRequest = new StringRequest(Request.Method.POST, Server + "/note_shared",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        volleycallback.onSuccess("note", response);
                    }
                }, errorlistener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("serial", serial);
                params.put("org_title", org_title);
                params.put("org_content", org_content);
                params.put("new_title", new_title);
                params.put("new_content", new_content);
                params.put("method", mothed + "");
                return params;
            }
        };
        getRequest.setRetryPolicy(new DefaultRetryPolicy(VolleyTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(getRequest);
    }
    public void postNote(final VolleyCallback callback, final String serial, final String title, final String content, final int mothed) {
        volleycallback = callback;
        getRequest = new StringRequest(Request.Method.POST,Server + "/note_shared",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        volleycallback.onSuccess("note", response);
                    }
                }, errorlistener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("serial",serial);
                params.put("title",title);
                params.put("content",content);
                params.put("method",mothed+"");
                return params;
            }
        };
        getRequest.setRetryPolicy(new DefaultRetryPolicy(VolleyTimeOut,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(getRequest);
    }
    public interface VolleyCallback{
        void onSuccess(String label, String result);
        void onError(String error);
        void onImageSuccess(String label ,Bitmap result);
    }
}

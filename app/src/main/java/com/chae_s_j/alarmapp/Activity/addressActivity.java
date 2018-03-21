package com.chae_s_j.alarmapp.Activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.chae_s_j.alarmapp.R;
import com.chae_s_j.alarmapp.Util.constant;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;

public class addressActivity extends AppCompatActivity {

    private WebView browser;
    private Geocoder geocoder;
    List<Address> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        constant.ActivityContext = this;
        geocoder = new Geocoder(this);
        browser = findViewById(R.id.webView);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setWebChromeClient(new WebChromeClient());
        browser.addJavascriptInterface(new JavaScriptInterface(), "Android");
        browser.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                browser.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });
        browser.loadUrl("http://221.149.60.219/daum.html");
    }

    class JavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processDATA(String data) {

            JSONObject jsonObject = new JSONObject();
            try {
                list = geocoder.getFromLocationName(
                        data, // 지역 이름
                        1); // 읽을 개수
            } catch (IOException e) {
                e.printStackTrace();
            //    Log.e("Error", "입출력 오류 - 서버에서 주소변환시 에러발생");
            }

            if (list != null) {
                if (list.size() == 0) {
                    try {
                        jsonObject.put("result","1");
                        data = jsonObject.toString();
                    } catch (JSONException e) { }
                } else {

                    double Latitude = list.get(0).getLatitude();
                    double Longitude = list.get(0).getLongitude();
                    try {
                        jsonObject.put("result","0");
                        jsonObject.put("Latitude",Latitude+"");
                        jsonObject.put("Longitude",Longitude+"");
                        data = jsonObject.toString();
                    } catch (JSONException e) { }

                }
            }

            Bundle extra = new Bundle();
            Intent intent = new Intent(addressActivity.this,MapActivity.class);
            extra.putString("location", data);
            intent.putExtras(extra);
            startActivity(intent);
            finish();
        }
    }
}


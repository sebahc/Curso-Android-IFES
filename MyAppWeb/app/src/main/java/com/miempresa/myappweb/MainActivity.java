package com.miempresa.myappweb;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView myWebview = (WebView) findViewById(R.id.webView);

        // habilito javascript
        WebSettings websettings = myWebview.getSettings();
        websettings.setJavaScriptEnabled(true);
        //interfaz javascript
        myWebview.addJavascriptInterface(new WebAppInterace(this),"Android");

        myWebview.loadUrl("http://www.latamlabs.com/webview/index.html");

    }

    public class WebAppInterace{
        Context mContext;

        WebAppInterace(Context c){
            mContext=c;
        }

        @JavascriptInterface
        public void mostrarToast(String cadena){
            Toast.makeText(mContext,cadena,Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

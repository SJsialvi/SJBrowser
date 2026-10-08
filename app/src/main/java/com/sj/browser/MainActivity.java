package com.sj.browser;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
    static final int PRIMARY = Color.parseColor("#0D47A1");
    static final String HOME = "https://www.google.com";
    WebView web;
    EditText bar;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(PRIMARY);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        LinearLayout top = new LinearLayout(this);
        top.setBackgroundColor(PRIMARY);
        top.setPadding(16, 16, 16, 16);
        bar = new EditText(this);
        bar.setHint("Search ya website likho");
        bar.setSingleLine(true);
        bar.setImeOptions(EditorInfo.IME_ACTION_GO);
        bar.setBackgroundColor(Color.WHITE);
        bar.setPadding(24, 16, 24, 16);
        top.addView(bar, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        root.addView(top);

        progress = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        root.addView(progress, new LinearLayout.LayoutParams(-1, 8));

        web = new WebView(this);
        root.addView(web, new LinearLayout.LayoutParams(-1, 0, 1));

        LinearLayout nav = new LinearLayout(this);
        nav.setBackgroundColor(PRIMARY);
        nav.addView(btn("◀", v -> { if (web.canGoBack()) web.goBack(); }));
        nav.addView(btn("▶", v -> { if (web.canGoForward()) web.goForward(); }));
        nav.addView(btn("⟳", v -> web.reload()));
        nav.addView(btn("⌂", v -> web.loadUrl(HOME)));
        root.addView(nav);

        setContentView(root);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setBuiltInZoomControls(true);
        s.setDisplayZoomControls(false);

        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView v, String url, android.graphics.Bitmap f) {
                bar.setText(url);
            }
        });
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView v, int p) {
                progress.setProgress(p);
                progress.setVisibility(p == 100 ? android.view.View.GONE : android.view.View.VISIBLE);
            }
        });

        bar.setOnEditorActionListener((v, id, e) -> {
            load(bar.getText().toString());
            return true;
        });

        web.loadUrl(HOME);
    }

    Button btn(String t, android.view.View.OnClickListener c) {
        Button x = new Button(this);
        x.setText(t);
        x.setTextColor(Color.WHITE);
        x.setTextSize(20);
        x.setBackgroundColor(Color.TRANSPARENT);
        x.setOnClickListener(c);
        x.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        return x;
    }

    void load(String in) {
        in = in.trim();
        if (in.isEmpty()) return;
        if (in.contains(" ") || !in.contains(".")) {
            web.loadUrl("https://www.google.com/search?q=" + Uri.encode(in));
        } else if (!in.startsWith("http")) {
            web.loadUrl("https://" + in);
        } else {
            web.loadUrl(in);
        }
    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) web.goBack();
        else super.onBackPressed();
    }
}

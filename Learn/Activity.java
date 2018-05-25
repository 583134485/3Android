package com.ace.shell.webViewActivity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ace.shell.R;
import com.ace.shell.ShellAppInterface;
import com.ace.shell.base.BaseActivity;
import com.common.app.constants.SHLConstants;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;

public class SecureWebViewActivity extends BaseActivity<SecureWebViewPresenter> implements SecureWebViewContract.View {

    @BindView(R.id.web_view)
    WebView webView;

    @BindView(R.id.loading_spinner_container)
    View loadingSpinnerContainer;

    @BindView(R.id.my_toolbar)
    Toolbar toolbar;

    @BindString(R.string.error_snackbar_message)
    String webviewSnackbackErrorMessage;

    @BindString(R.string.error_snackbar_cta_button)
    String webviewSnackbackAction;

    @Inject
    SecureWebViewPresenter presenter;

    Snackbar snackbar;
    private String title;

    public static void launchWithPostRequest(Context context, String title, String postScript, String antiForgeryToken) {
        if (context == null || title == null || antiForgeryToken == null || postScript == null) {
            return;
        }

        Intent intent = new Intent(context, SecureWebViewActivity.class);

        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        intent.putExtra(SHLConstants.SHL_WEB_VIEW_TOOLBAR_TITLE_KEY, title);
        intent.putExtra(SHLConstants.SHL_WEB_VIEW_POST_SCRIPT_KEY, postScript);
        intent.putExtra(SHLConstants.SHL_WEB_VIEW_ANTIFORGERY_TOKEN_KEY, antiForgeryToken);
        context.startActivity(intent);
    }

    public static void launchWithTargetUrl(Context context,
                                           String title,
                                           String targetUrl) {

        if (context == null || title == null || targetUrl == null) {
            return;
        }

        Intent intent = new Intent(context, SecureWebViewActivity.class);

        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        intent.putExtra(SHLConstants.SHL_WEB_VIEW_URL_KEY, targetUrl);
        intent.putExtra(SHLConstants.SHL_WEB_VIEW_TOOLBAR_TITLE_KEY, title);
        context.startActivity(intent);
    }

    public static void launchWithEndpoint(Context context,
                                          String title,
                                          String... endpointTypes) {

        if (context == null || title == null || (endpointTypes == null || endpointTypes.length == 0)) {
            return;
        }

        Intent intent = new Intent(context, SecureWebViewActivity.class);

        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        intent.putExtra(SHLConstants.SHL_WEB_VIEW_ENDPOINT_TYPE_KEY, endpointTypes);
        intent.putExtra(SHLConstants.SHL_WEB_VIEW_TOOLBAR_TITLE_KEY, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        title = intent.getStringExtra(SHLConstants.SHL_WEB_VIEW_TOOLBAR_TITLE_KEY);
        String targetUrl = intent.getStringExtra(SHLConstants.SHL_WEB_VIEW_URL_KEY);
        String[] endpoints = intent.getStringArrayExtra(SHLConstants.SHL_WEB_VIEW_ENDPOINT_TYPE_KEY);
        String postScript = intent.getStringExtra(SHLConstants.SHL_WEB_VIEW_POST_SCRIPT_KEY);
        String antiForgeryToken = intent.getStringExtra(SHLConstants.SHL_WEB_VIEW_ANTIFORGERY_TOKEN_KEY);

        if (title == null || (targetUrl == null && endpoints == null && postScript == null && antiForgeryToken == null)) {
            finish();
            return;
        }

        super.onCreate(savedInstanceState);

        if (presenterNullCheck(presenter)) {
            return;
        }

        if (targetUrl != null) {
            presenter.createWithUrl(this, targetUrl);
        } else if (endpoints != null) {
            presenter.createWithEndpoint(this, endpoints);
        } else if (!postScript.isEmpty()) {
            presenter.createWithPostRequest(this, postScript, antiForgeryToken);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void login(@NonNull String targetUrl,
                      @NonNull String oAuthUrl,
                      @NonNull String authToken,
                      String... postScript) {

        WebSettings settings = webView.getSettings();
        settings.setAllowContentAccess(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(true);

        String loginScript = presenter.createLoginScript(oAuthUrl, authToken);
        String loginScriptUrl = presenter.createScriptUrl(loginScript);
        webView.setWebViewClient(makeSecureWebViewClient(targetUrl, oAuthUrl, loginScriptUrl, postScript));
        executePost(loginScript);
    }

    @NonNull
    SecureWebViewClient makeSecureWebViewClient(@NonNull String targetUrl, @NonNull String oAuthUrl, String loginScriptUrl, String[] postScript) {
        return new SecureWebViewClient(presenter, targetUrl, oAuthUrl, loginScriptUrl, postScript);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void executePost(@NonNull String postScript) {
        webView.loadData(postScript, SHLConstants.MIME_TYPE_HTML, null);
    }

    @Override
    public void showLoadingErrorSnackbar() {
        snackbar = Snackbar.make(webView, webviewSnackbackErrorMessage, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(webviewSnackbackAction, (View v) -> reloadUrl());
        snackbar.show();
    }

    private void reloadUrl() {
        webView.reload();
        loadingSpinnerContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadUrl(@NonNull String url) {
        webView.loadUrl(url);
    }

    @Override
    public void hideSpinner() {
        loadingSpinnerContainer.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void inject() {
        ((ShellAppInterface) getApplication()).getDaggerAppComponent().inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.secure_webview_layout;
    }

    @Override
    public SecureWebViewPresenter getActivityPresenter() {
        return presenter;
    }

    @Override
    public void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setShowHideAnimationEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    private boolean presenterNullCheck(SecureWebViewPresenter presenter) {
        if (presenter == null) {
            finish();
            return true;
        }
        return false;
    }

    static class SecureWebViewClient extends WebViewClient {

        @NonNull
        private String loginScriptUrl;

        @NonNull
        private String targetUrl;

        @NonNull
        private String oAuthUrl;


        private String[] postScript;

        @Nullable
        private SecureWebViewContract.Presenter presenter;

        SecureWebViewClient(@Nullable SecureWebViewContract.Presenter presenter,
                            @NonNull String targetUrl,
                            @NonNull String oAuthUrl,
                            @NonNull String loginScriptUrl,
                            String... postScript) {
            this.presenter = presenter;
            this.targetUrl = targetUrl;
            this.oAuthUrl = oAuthUrl;
            this.loginScriptUrl = loginScriptUrl;
            this.postScript = postScript;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (presenter != null) {
                presenter.onPageFinished(url, targetUrl, oAuthUrl, loginScriptUrl, postScript);
            }
        }


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if (presenter != null) {
                presenter.onReceivedSslError(view, handler, error);
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            if (presenter != null && this.targetUrl.equals(request.getUrl().toString())) {
                presenter.onLoadingUrlError(request.getUrl().toString());
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            if (presenter != null && this.targetUrl.equals(request.getUrl().toString())) {
                presenter.onLoadingUrlError(request.getUrl().toString());
            }
        }
    }
}

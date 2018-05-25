package com.aaa.calif.account.ui.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aaa.calif.account.R;
import com.aaa.calif.account.R2;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;

public class LoginActivity extends BaseLoginActivity {
    @Inject
    LoginPresenter presenter;

    @BindView(R2.id.account_webview)
    WebView webView;

    @BindView(R2.id.loading_spinner_container)
    FrameLayout loadingSpinner;

    @BindView(R2.id.ace_help_login_links_text)
    TextView aceOrAneHelpTextView;

    //    ANE Login Links
    @BindString(R2.string.activity_login_join_ane_help)
    String fullAneHelpText;

    @BindString(R2.string.activity_login_cta_ane_help_login)
    String helpMeLoginAneText;

    @BindString(R2.string.activity_login_cta_ane_create_username)
    String createUsernameAneText;

    @BindString(R2.string.activity_login_cta_join)
    String joinText;

    //    ACE Login Links
    @BindString(R2.string.activity_login_join_ace_help)
    String fullAceHelpText;

    @BindString(R2.string.activity_login_cta_ace_find_account)
    String aceFindAccount;

    /**
     * BaseActivity Methods
     */

    @Override
    protected void inject() throws Exception {
        getClubComponent().inject(this);
    }

    @NonNull
    @Override
    protected LoginPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.acc_activity_account_login;
    }

    @Override
    protected void configureViews() {
        setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * LoginView Methods
     */


    @Override
    public void setUsernameError(boolean isError) {
        usernameInputLayout.setError(isError ? " " : null);
    }

    @Override
    public void setPasswordError(boolean isError) {
        passwordInputLayout.setError(isError ? " " : null);
    }

    @Override
    public void showErrorMessage(String error) {
        if (TextUtils.isEmpty(error)) {
            setAnimatedErrorText(getString(R.string.unable_to_login));
        }

        setAnimatedErrorText(error);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void openWebView(String url) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(webView.getWindowToken(), 0);

        setMenuItemVisibility(false);
        webView.setVisibility(View.VISIBLE);
        loadingSpinner.setVisibility(View.VISIBLE);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(true);

        webView.setWebViewClient(new LoginWebViewClient());


        webView.setVisibility(View.VISIBLE);
        webView.loadUrl(url);
    }

    class LoginWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            loadingSpinner.setVisibility(View.GONE);
        }
    }

    private void setMenuItemVisibility(boolean isVisible) {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            Menu menu = toolbar.getMenu();
            if (menu != null) {
                MenuItem item = menu.getItem(0);
                if (item != null) {
                    item.setVisible(isVisible);
                }
            }
        }
    }

    @Override
    public void toggleAccountLookup(boolean isNativeFindMyAccountAvailable) {
        if (isNativeFindMyAccountAvailable) {
            SpannableStringBuilder spannable = new SpannableStringBuilder(fullAceHelpText);
            spannable = setTextWithSpan(spannable, fullAceHelpText, aceFindAccount, new HelpMeLoginClickable(presenter, isNativeFindMyAccountAvailable));
            spannable = setTextWithSpan(spannable, fullAceHelpText, joinText, new JoinAaaClickable(presenter));
            aceOrAneHelpTextView.setText(spannable);

        } else {
            SpannableStringBuilder spannable = new SpannableStringBuilder(fullAneHelpText);
            spannable = setTextWithSpan(spannable, fullAneHelpText, helpMeLoginAneText, new HelpMeLoginClickable(presenter, isNativeFindMyAccountAvailable));
            spannable = setTextWithSpan(spannable, fullAneHelpText, createUsernameAneText, new CreateUsernameClickable(presenter));
            spannable = setTextWithSpan(spannable, fullAneHelpText, joinText, new JoinAaaClickable(presenter));
            aceOrAneHelpTextView.setText(spannable);
        }

        aceOrAneHelpTextView.setHighlightColor(Color.TRANSPARENT);
        aceOrAneHelpTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onBackPressed() {
        if (webView.getVisibility() == View.VISIBLE) {
            setMenuItemVisibility(true);
            webView.setVisibility(View.GONE);
            loadingSpinner.setVisibility(View.GONE);
            webView.loadUrl("about:blank");
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (webView.getVisibility() == View.VISIBLE) {
            return false;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private SpannableStringBuilder setTextWithSpan(SpannableStringBuilder spannable,
                                                   String fullText,
                                                   String textToLocate,
                                                   @Nullable ClickableSpan clickableSpan) {
        int start = fullText.indexOf(textToLocate);
        int end = start + textToLocate.length();
        if (clickableSpan != null && start > -1) {
            spannable.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_white_primary)),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        return spannable;
    }

    static class HelpMeLoginClickable extends ClickableSpan {

        final boolean isAvailable;
        final LoginPresenter presenter;

        HelpMeLoginClickable(LoginPresenter presenter, boolean isAvailable) {
            this.presenter = presenter;
            this.isAvailable = isAvailable;
        }

        @Override
        public void onClick(View widget) {
            presenter.startRegistrationFlow(this.isAvailable);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }

    static class CreateUsernameClickable extends ClickableSpan {
        final LoginPresenter presenter;

        CreateUsernameClickable(LoginPresenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public void onClick(View widget) {
            presenter.onCreateUsernameClick();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }

    static class JoinAaaClickable extends ClickableSpan {
        final LoginPresenter presenter;

        JoinAaaClickable(LoginPresenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public void onClick(View widget) {
            presenter.onJoinAaaClick();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }
}

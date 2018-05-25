package com.aaa.calif.account.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.aaa.calif.account.R2;
import com.aaa.calif.account.ui.base.authenticator.AuthenticatorActivity;
import com.aaa.calif.account.ui.lookup.AccountLookupActivity;
import com.aaa.calif.account.ui.roadsideAssistance.RoadsideAssistanceActivity;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;

public abstract class BaseLoginActivity extends AuthenticatorActivity<LoginPresenter> implements LoginView {

    public static final int REQUEST_CODE_LOOKUP_ACCOUNT = 4525;
    private static final int ANIMATION_DURATION = 200;
    AlphaAnimation fadeInAnim = new AlphaAnimation(0.0f, 1.0f);
    AlphaAnimation fadeOutAnim = new AlphaAnimation(1.0f, 0.0f);

    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    @BindView(R2.id.layout_content)
    ViewGroup layoutContent;

    @BindView(R2.id.activity_login_username_text_input_layout)
    TextInputLayout usernameInputLayout;

    @BindView(R2.id.activity_login_password_text_input_layout)
    TextInputLayout passwordInputLayout;

    @BindView(R2.id.activity_login_username_editText)
    TextInputEditText usernameField;

    @BindView(R2.id.activity_login_password_editText)
    TextInputEditText passwordField;

    @BindView(R2.id.activity_login_error_message)
    TextView errorMessage;

    @Override
    protected void configureDisposables() {
        disposables.add(RxTextView.textChangeEvents(usernameField)
                .subscribe(textViewTextChangeEvent -> {
                    clearError();
                    getPresenter().updateUsernameText(textViewTextChangeEvent.text().toString());
                }));

        disposables.add(RxTextView.textChangeEvents(passwordField)
                .subscribe(textViewTextChangeEvent -> {
                    clearError();
                    getPresenter().updatePasswordText(textViewTextChangeEvent.text().toString());
                }));

        disposables.add(RxTextView.editorActionEvents(passwordField)
                .subscribe(textViewEditorActionEvent -> {
                    clearError();
                    getPresenter().onPasswordImeAction(textViewEditorActionEvent.actionId());
                }));
    }

    @NonNull
    @Override
    protected ViewGroup getContentLayout() {
        return layoutContent;
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Nullable
    @Override
    protected View getSharedView() {
        return null;
    }

    @Override
    protected boolean shouldDelayTransitionForFragment() {
        return false;
    }

    @Override
    public void onAuthenticationFinish(Intent intent) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void setUsernameError(boolean isError) {
        usernameInputLayout.setError(isError ? " " : null);
    }

    @Override
    public void setPasswordError(boolean isError) {
        passwordInputLayout.setError(isError ? " " : null);
    }

    @Override
    public void setHintLabel(String hint) {
        usernameInputLayout.setHint(hint);
    }

    @Override
    public void trackLoginFromWelcomeScreen() {
        getAnalyticsTracker().trackLoginFromWelcomeScreen();
    }

    @Override
    public void trackRegisterLinkOpened() {
        getAnalyticsTracker().trackRegisterLinkOpened();
    }

    @Override
    public void trackHelpLoginLinkOpened() {
        getAnalyticsTracker().trackHelpLoginLinkOpened();
    }

    @Override
    public void trackCreateUsernameLinkOpened() {
        getAnalyticsTracker().trackCreateUsernameLinkOpened();
    }

    @Override
    public void trackLookupMyAccountLinkOpened() {
        getAnalyticsTracker().trackLookupMyAccountLinkOpened();
    }

    @Override
    public void launchRoadsideAssistance() {
        RoadsideAssistanceActivity.launch(this);
    }

    @Override
    public void showAccountLookUp() {
        AccountLookupActivity.launchForResult(this, REQUEST_CODE_LOOKUP_ACCOUNT);
    }

    @Override
    public void openWebBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    void clearError() {
        setAnimatedErrorText("");
        usernameInputLayout.setError(null);
        passwordInputLayout.setError(null);
    }

    void setAnimatedErrorText(String text) {
        setAnimatedErrorText(new SpannableString(text));
    }

    void setAnimatedErrorText(Spannable spannable) {
        if (errorMessage.getAnimation() != null && !errorMessage.getAnimation().hasEnded()) {
            return;
        }

        if (!TextUtils.isEmpty(spannable)) {
            fadeInAnim.setAnimationListener(new MyAnimationStartListener(spannable));
            fadeInAnim.setDuration(ANIMATION_DURATION);
            errorMessage.startAnimation(fadeInAnim);
        } else if (!TextUtils.isEmpty(errorMessage.getText()) && TextUtils.isEmpty(spannable)) {
            fadeOutAnim.setAnimationListener(new MyAnimationEndListener(spannable));
            fadeOutAnim.setDuration(ANIMATION_DURATION);
            errorMessage.startAnimation(fadeOutAnim);
        }
    }

    class MyAnimationStartListener implements Animation.AnimationListener {

        private Spannable spannable;

        MyAnimationStartListener(Spannable spannable) {
            this.spannable = spannable;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            errorMessage.setText(spannable);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    class MyAnimationEndListener implements Animation.AnimationListener {

        private Spannable spannable;

        MyAnimationEndListener(Spannable spannable) {
            this.spannable = spannable;
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            errorMessage.setText(spannable);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }
}

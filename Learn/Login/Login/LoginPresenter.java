package com.aaa.calif.account.ui.login;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.inputmethod.EditorInfo;

import com.aaa.calif.account.R;
import com.aaa.calif.account.service.throwable.LoginThrowable;
import com.aaa.calif.account.service.throwable.LoginValidationThrowable;
import com.aaa.calif.account.ui.base.authenticator.AuthenticatorPresenter;
import com.common.app.models.Feature;
import com.common.app.models.navigation.ClubSettings;
import com.common.app.models.navigation.EndPoint;
import com.common.app.utils.StringUtils;

import java.util.List;

import javax.inject.Inject;

public class LoginPresenter extends AuthenticatorPresenter<LoginView> {

    public static final String ARGS_HINT_LABEL = "ARGS_HINT_LABEL";
    public static final String ARGS_ZIPCODE = "ARGS_ZIPCODE";
    private static final String JOIN_NOW_ENDPOINT = "JoinNow";
    private static final String FORGOT_PASSWORD_ENDPOINT = "Forgot Password";
    private static final String REGISTRATION_ENDPOINT = "Registration";
    private static final String ACCOUNT_LOOKUP_FEATURE = "Account Lookup";

    private String username = "";
    private String password = "";
    private String hintLabel = "";
    private ClubSettings clubSettings;
    private String zipcode = "";

    @Inject
    LoginPresenter() {
    }

    @Override
    protected void setup(@Nullable Bundle arguments) {
        super.setup(arguments);
        if (arguments != null && arguments.containsKey(ARGS_HINT_LABEL)) {
            hintLabel = arguments.getString(ARGS_HINT_LABEL);
        }

        if (arguments != null && arguments.containsKey(ARGS_ZIPCODE)) {
            zipcode = arguments.getString(ARGS_ZIPCODE);
        }

        if (clubSettings == null) {
            clubSettings = getClubSettings();
        }
        view.toggleAccountLookup(isAccountLookupAvailable());
    }

    @Override
    protected void saveArguments(@NonNull Bundle arguments) {
        super.saveArguments(arguments);
        if (hintLabel != null) {
            arguments.putString(ARGS_HINT_LABEL, hintLabel);
        }
    }

    /**
     * ActivityPresenter Methods
     */
    @Override
    protected void updateViewState() {
        view.setHintLabel(hintLabel);
        hideLoading();
    }

    @Override
    protected void onClick(@IdRes int id) {
        if (id == R.id.action_done) {
            trySignIn();
        } else if (id == R.id.action_rso) {
            view.launchRoadsideAssistance();
        }
    }

    void startRegistrationFlow(boolean isNativeFindMyAccountAvailable) {
        if (isNativeFindMyAccountAvailable) {
            view.showAccountLookUp();
            view.trackLookupMyAccountLinkOpened();
        } else {
            openWebView(FORGOT_PASSWORD_ENDPOINT);
            view.trackHelpLoginLinkOpened();
        }
    }

    void onCreateUsernameClick() {
        openWebView(REGISTRATION_ENDPOINT);
        view.trackCreateUsernameLinkOpened();
    }

    void onJoinAaaClick() {
        openWebBrowser(JOIN_NOW_ENDPOINT);
        view.trackRegisterLinkOpened();
    }

    /**
     * AuthenticatorPresenter Methods
     */

    @NonNull
    @Override
    protected String username() {
        return username;
    }

    @NonNull
    @Override
    protected String password() {
        return password;
    }

    @Override
    protected boolean isCreatingAccount() {
        return false;
    }

    @Override
    protected void onLoginValidationError(LoginValidationThrowable loginValidationThrowable) {
        super.onLoginValidationError(loginValidationThrowable);
        view.setUsernameError(loginValidationThrowable.areFlagsSet(LoginValidationThrowable.EMPTY_USERNAME));
        view.setPasswordError(loginValidationThrowable.areFlagsSet(LoginValidationThrowable.EMPTY_PASSWORD));
        view.showErrorMessage(loginValidationThrowable.getMessage());
    }

    @Override
    protected void onLoginError(LoginThrowable e) {
        super.onLoginError(e);
        view.setUsernameError(true);
        view.setPasswordError(true);
        if (view instanceof LoginActivity) {
            view.showErrorMessage(view.getString(R.string.error_wrong_password));
        } else {
            view.showErrorMessage(e.getCause().getMessage());
        }
    }

    /**
     * Class Methods
     */

    void updateUsernameText(String username) {
        this.username = username;
    }

    void updatePasswordText(String password) {
        this.password = password;
    }

    void onPasswordImeAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            trySignIn();
        }
    }

    String appendOrReplaceZipQueryParam(@NonNull String url) {
        String zipQueryParam = "&zip=";
        String queryParamToReplace = "&Zip=%@";

        if (!url.contains("?")) {
            zipQueryParam = zipQueryParam.replace("&", "?");
        }

        if (url.contains(queryParamToReplace)) {
            url = url.replace(queryParamToReplace, zipQueryParam);
        } else {
            url = url.concat(zipQueryParam);
        }

        return url.concat(zipcode);
    }


    private void openWebView(String endPointType) {
        String url = getEndpointUrl(endPointType);

        view.openWebView(StringUtils.stripHtml(url));
    }

    private void openWebBrowser(String endPointType) {
        String url = getEndpointUrl(endPointType);

        view.openWebBrowser(StringUtils.stripHtml(url));
    }

    private String getEndpointUrl(String endPointType) {
        String url = "";
        List<EndPoint> endpoints = clubSettings.getEndPoints();
        for (EndPoint endpoint : endpoints) {
            if (endpoint.getType().equals(endPointType)) {
                url = endpoint.getEndPointUrl();
                break;
            }
        }

        url = appendOrReplaceZipQueryParam(url);
        return url;
    }

    boolean isAccountLookupAvailable() {
        boolean isAvailable = false;
        List<Feature> features = clubSettings.getFeatures();
        for (Feature feature : features) {
            if (feature.getFeatureType().equals(ACCOUNT_LOOKUP_FEATURE) && feature.isActive()) {
                isAvailable = true;
                break;
            }
        }
        return isAvailable;
    }
}

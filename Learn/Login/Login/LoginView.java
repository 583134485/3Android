package com.aaa.calif.account.ui.login;

import com.aaa.calif.account.ui.base.authenticator.AuthenticatorView;

interface LoginView extends AuthenticatorView {

    void showAccountLookUp();

    void openWebView(String url);

    void openWebBrowser(String url);

    void setUsernameError(boolean isError);

    void setPasswordError(boolean isError);

    void showErrorMessage(String error);

    void setHintLabel(String hint);

    void trackLoginFromWelcomeScreen();

    void trackRegisterLinkOpened();

    void trackHelpLoginLinkOpened();

    void trackCreateUsernameLinkOpened();

    void trackLookupMyAccountLinkOpened();

    void toggleAccountLookup(boolean isAvailable);

    void launchRoadsideAssistance();
}
package com.ace.shell.webViewActivity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ace.shell.BuildConfig;
import com.ace.shell.R;
import com.ace.shell.base.BaseActivity;
import com.ace.shell.unitTestReady.UnitTestReadyApplication;
import com.common.app.constants.SHLConstants;
import com.common.app.testData.ClubSettingsTestData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.lang.reflect.Method;
import java.util.Map;

import static com.common.app.constants.SHLConstants.SHL_WEB_VIEW_ANTIFORGERY_TOKEN_KEY;
import static com.common.app.constants.SHLConstants.SHL_WEB_VIEW_POST_SCRIPT_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 24, application = UnitTestReadyApplication.class)
public class SecureWebViewActivityTest {

    private final String O_AUTH_URL = "MY AUTH URL";
    private final String AUTH_TOKEN = "AUTH TOKEN";
    private final String TARGET_URL = "https://www.google.com/";
    private final String NON_TARGET_URL = "https://www.goog.com/";
    private final String LOGIN_SCRIPT = "<html><head><base></head><body><form id='aceLoginForm' enctype='application/json' method='post' action='" + O_AUTH_URL + "'><input name='token' value='" + AUTH_TOKEN + "' /><input name='access_token' value='" + AUTH_TOKEN + "' /></form><script type='text/javascript'>document.getElementById('aceLoginForm').submit();</script></body></html>";
    private final String LOGIN_SCRIPT_AS_URL = "data:text/html," + LOGIN_SCRIPT;
    private final String SHL_WEB_VIEW_TOOLBAR_TITLE_KEY = "shl_webview_toolbar_key";
    private final String SHL_WEB_VIEW_URL_KEY = "shl_webview_url_key";
    private final String TITLE = "TITLE";
    private final String SHL_WEB_VIEW_ENDPOINT_TYPE_KEY = "shl_web_view_endpoint_type_key";
    private final String POST_SCRIPT = "my post script";
    private final String ANTI_FORGERY_TOKEN = "my token";
    private final String[] ENDPOINTS = new String[]{"MembershipPaymentURL", "MembershipAutoPayEnrollment"};

    private SecureWebViewActivity spySecureWebViewActivity;
    private Application application;

    @Mock
    WebView mockWebView;
    @Mock
    View mockLoadingSpinnerContainer;
    @Mock
    SecureWebViewPresenter mockPresenter;
    @Mock
    Context mockContext;
    @Mock
    WebResourceError mockWebResourceError;
    @Mock
    WebResourceResponse mockWebResourceResponse;

    @Mock
    Intent mockIntent;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        spySecureWebViewActivity = spy(Robolectric.buildActivity(SecureWebViewActivity.class).visible().get());
        application = RuntimeEnvironment.application;

        when(spySecureWebViewActivity.getIntent()).thenReturn(mockIntent);
        when(mockIntent.getStringExtra(SHL_WEB_VIEW_TOOLBAR_TITLE_KEY)).thenReturn(TITLE);
        when(mockIntent.getStringExtra(SHL_WEB_VIEW_URL_KEY)).thenReturn(TARGET_URL);
        when(mockIntent.getStringExtra(SHL_WEB_VIEW_POST_SCRIPT_KEY)).thenReturn(POST_SCRIPT);
        when(mockIntent.getStringExtra(SHL_WEB_VIEW_ANTIFORGERY_TOKEN_KEY)).thenReturn(ANTI_FORGERY_TOKEN);
        when(mockIntent.getStringArrayExtra(SHL_WEB_VIEW_ENDPOINT_TYPE_KEY)).thenReturn(ENDPOINTS);

        SharedPreferences sp = RuntimeEnvironment.application.getSharedPreferences(
                SHLConstants.ACE_SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putString("004", ClubSettingsTestData.getUatClubSettingsString());
        editor.putString("240", ClubSettingsTestData.getAneClubSettingsString());
        editor.apply();

        spySecureWebViewActivity.presenter = mockPresenter;
        spySecureWebViewActivity.webView = mockWebView;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Test
    public void shouldLogin() throws Exception {
        WebSettings mockWebSettings = mock(WebSettings.class);
        String[] postScriptArray = new String[]{POST_SCRIPT};

        when(mockWebView.getSettings()).thenReturn(mockWebSettings);
        when(mockPresenter.createLoginScript(anyString(), anyString())).thenReturn(LOGIN_SCRIPT);
        when(mockPresenter.createScriptUrl(LOGIN_SCRIPT)).thenReturn(LOGIN_SCRIPT_AS_URL);

        spySecureWebViewActivity.login(TARGET_URL, O_AUTH_URL, AUTH_TOKEN, postScriptArray);

        verify(mockWebSettings).setAllowContentAccess(true);
        verify(mockWebSettings).setDomStorageEnabled(true);
        verify(mockWebSettings).setJavaScriptEnabled(true);
        verify(mockWebSettings).setLoadsImagesAutomatically(true);
        verify(mockWebView).setWebViewClient(any(SecureWebViewActivity.SecureWebViewClient.class));
        verify(mockPresenter).createLoginScript(O_AUTH_URL, AUTH_TOKEN);
        verify(mockPresenter).createScriptUrl(LOGIN_SCRIPT);
        verify(spySecureWebViewActivity).executePost(LOGIN_SCRIPT);
        verify(spySecureWebViewActivity).makeSecureWebViewClient(TARGET_URL, O_AUTH_URL, LOGIN_SCRIPT_AS_URL, postScriptArray);
    }

    @Test
    public void shouldExecutePost() throws Exception {
        String expectedPostScript = "my post script";

        spySecureWebViewActivity.executePost(expectedPostScript);

        verify(mockWebView).loadData(expectedPostScript, "text/html", null);
    }

    @Test
    public void webViewShouldLoadUrlGivenToIt() throws Exception {
        spySecureWebViewActivity.loadUrl(TARGET_URL);

        verify(mockWebView).loadUrl(TARGET_URL);
    }

    @Test
    public void shouldHideLoadingSpinner() throws Exception {
        spySecureWebViewActivity.loadingSpinnerContainer = mockLoadingSpinnerContainer;

        spySecureWebViewActivity.hideSpinner();

        verify(mockLoadingSpinnerContainer).setVisibility(View.GONE);
    }

    @Test
    public void shouldTellPresenterToHandleOnPageFinishedCallback() throws Exception {
        SecureWebViewActivity.SecureWebViewClient client =
                new SecureWebViewActivity.SecureWebViewClient(mockPresenter, TARGET_URL, O_AUTH_URL, LOGIN_SCRIPT);

        client.onPageFinished(mockWebView, TARGET_URL);

        verify(mockPresenter).onPageFinished(TARGET_URL, TARGET_URL, O_AUTH_URL, LOGIN_SCRIPT);
    }

    @Test
    public void onPageFinishedShouldNotCrashWebViewClient() throws Exception {
        SecureWebViewActivity.SecureWebViewClient client =
                new SecureWebViewActivity.SecureWebViewClient(null, TARGET_URL, O_AUTH_URL, LOGIN_SCRIPT);

        client.onPageFinished(mockWebView, TARGET_URL);

        verify(mockPresenter, never()).onPageFinished(TARGET_URL, TARGET_URL, O_AUTH_URL, LOGIN_SCRIPT);
    }

    @Test
    public void shouldTellPresenterToHandleOnReceivedSslError() throws Exception {
        SecureWebViewActivity.SecureWebViewClient client =
                new SecureWebViewActivity.SecureWebViewClient(mockPresenter, TARGET_URL, O_AUTH_URL, LOGIN_SCRIPT_AS_URL);

        SslErrorHandler mockHandler = mock(SslErrorHandler.class);
        SslError mockError = mock(SslError.class);
        client.onReceivedSslError(mockWebView, mockHandler, mockError);

        verify(mockPresenter).onReceivedSslError(mockWebView, mockHandler, mockError);
    }

    @Test
    public void onReceivedSslErrorShouldNotCrashWebViewClient() throws Exception {
        SecureWebViewActivity.SecureWebViewClient client =
                new SecureWebViewActivity.SecureWebViewClient(null, TARGET_URL, O_AUTH_URL, LOGIN_SCRIPT_AS_URL);

        SslErrorHandler mockHandler = mock(SslErrorHandler.class);
        SslError mockError = mock(SslError.class);
        client.onReceivedSslError(mockWebView, mockHandler, mockError);

        verify(mockPresenter, never()).onReceivedSslError(mockWebView, mockHandler, mockError);
    }

    @Test
    public void shouldLaunchWithTargetUrl() throws Exception {
        String expectedActivityStarted = new Intent(spySecureWebViewActivity, SecureWebViewActivity.class)
                .getComponent()
                .getClassName();

        SecureWebViewActivity.launchWithTargetUrl(spySecureWebViewActivity, TITLE, TARGET_URL);

        Intent nextStartedActivity = shadowOf(spySecureWebViewActivity).getNextStartedActivity();
        String actualActivityStarted = nextStartedActivity.getComponent().getClassName();

        assertEquals(expectedActivityStarted, actualActivityStarted);
        assertEquals(TARGET_URL, nextStartedActivity.getStringExtra(SHL_WEB_VIEW_URL_KEY));
        assertEquals(TITLE, nextStartedActivity.getStringExtra(SHL_WEB_VIEW_TOOLBAR_TITLE_KEY));
    }

    @Test
    public void shouldLaunchWithTargetUrlAndIntentFlagIfContextApplication() throws Exception {
        SecureWebViewActivity.launchWithTargetUrl(application, TITLE, TARGET_URL);

        Intent nextStartedActivity = shadowOf(application).getNextStartedActivity();

        assertEquals(Intent.FLAG_ACTIVITY_NEW_TASK, nextStartedActivity.getFlags());
        assertEquals(TARGET_URL, nextStartedActivity.getStringExtra(SHL_WEB_VIEW_URL_KEY));
        assertEquals(TITLE, nextStartedActivity.getStringExtra(SHL_WEB_VIEW_TOOLBAR_TITLE_KEY));
    }

    @Test
    public void shouldLaunchWithTargetEndpointAndIntentFlagIfContextApplication() throws Exception {
        SecureWebViewActivity.launchWithEndpoint(application, TITLE, ENDPOINTS);

        Intent nextStartedActivity = shadowOf(application).getNextStartedActivity();
        String[] actualEndpoints = nextStartedActivity.getStringArrayExtra(SHL_WEB_VIEW_ENDPOINT_TYPE_KEY);

        assertEquals(Intent.FLAG_ACTIVITY_NEW_TASK, nextStartedActivity.getFlags());
        String expectedEndpoint = ENDPOINTS[0];

        assertEquals(expectedEndpoint, actualEndpoints[0]);
        assertEquals(TITLE, nextStartedActivity.getStringExtra(SHL_WEB_VIEW_TOOLBAR_TITLE_KEY));
    }

    @Test
    public void shouldLaunchWithEndpointType() throws Exception {
        String expectedActivityStarted = new Intent(spySecureWebViewActivity, SecureWebViewActivity.class)
                .getComponent()
                .getClassName();

        SecureWebViewActivity.launchWithEndpoint(spySecureWebViewActivity, TITLE, ENDPOINTS);

        Intent nextStartedActivity = shadowOf(spySecureWebViewActivity).getNextStartedActivity();
        String actualActivityStarted = nextStartedActivity.getComponent().getClassName();

        assertEquals(expectedActivityStarted, actualActivityStarted);

        String[] actualEndpoints = nextStartedActivity.getStringArrayExtra(SHL_WEB_VIEW_ENDPOINT_TYPE_KEY);
        String expectedEndpoint = ENDPOINTS[0];

        assertEquals(expectedEndpoint, actualEndpoints[0]);
        assertEquals(TITLE, nextStartedActivity.getStringExtra(SHL_WEB_VIEW_TOOLBAR_TITLE_KEY));
    }

    @Test
    public void shouldOpenSecureWebviewWithPostScript() throws Exception {
        String expectedPostScript = "my post script";
        String expectedTitle = "my title";
        String expectedToken = "my anti forgery token";

        String expectedNextStartedActivity = new Intent(spySecureWebViewActivity, SecureWebViewActivity.class)
                .getComponent()
                .getClassName();

        SecureWebViewActivity.launchWithPostRequest(spySecureWebViewActivity, expectedTitle, expectedPostScript, expectedToken);

        Intent nextStartedActivity = shadowOf(spySecureWebViewActivity).getNextStartedActivity();
        String actualActivityStarted = nextStartedActivity.getComponent().getClassName();

        assertEquals(expectedNextStartedActivity, actualActivityStarted);
        assertEquals(expectedPostScript, nextStartedActivity.getStringExtra(SHL_WEB_VIEW_POST_SCRIPT_KEY));
        assertEquals(expectedTitle, nextStartedActivity.getStringExtra(SHL_WEB_VIEW_TOOLBAR_TITLE_KEY));
        assertEquals(expectedToken, nextStartedActivity.getStringExtra(SHL_WEB_VIEW_ANTIFORGERY_TOKEN_KEY));
    }

    @Test
    public void shouldNotLaunchWebViewIfContextNull() throws Exception {
        SecureWebViewActivity.launchWithTargetUrl(null, TITLE, TARGET_URL);

        Intent nextStartedActivity = shadowOf(spySecureWebViewActivity).getNextStartedActivity();

        assertEquals(null, nextStartedActivity);

        SecureWebViewActivity.launchWithEndpoint(null, TITLE, ENDPOINTS);

        nextStartedActivity = shadowOf(spySecureWebViewActivity).getNextStartedActivity();

        assertEquals(null, nextStartedActivity);
    }

    @Test
    public void shouldNotLaunchWebViewIfTitleNull() throws Exception {
        SecureWebViewActivity.launchWithTargetUrl(spySecureWebViewActivity, null, TARGET_URL);

        Intent nextStartedActivity = shadowOf(spySecureWebViewActivity).getNextStartedActivity();

        assertEquals(null, nextStartedActivity);

        SecureWebViewActivity.launchWithEndpoint(spySecureWebViewActivity, null, ENDPOINTS);

        nextStartedActivity = shadowOf(spySecureWebViewActivity).getNextStartedActivity();

        assertEquals(null, nextStartedActivity);
    }

    @Test
    public void shouldNotLaunchWebViewIfTargetUrlNull() throws Exception {
        SecureWebViewActivity.launchWithTargetUrl(spySecureWebViewActivity, TITLE, null);

        Intent nextStartedActivity = shadowOf(spySecureWebViewActivity).getNextStartedActivity();

        assertEquals(null, nextStartedActivity);
    }

    @Test
    public void shouldNotLaunchWebViewIfEndPointNull() throws Exception {
        SecureWebViewActivity.launchWithEndpoint(spySecureWebViewActivity, TITLE);

        Intent nextStartedActivity = shadowOf(spySecureWebViewActivity).getNextStartedActivity();

        assertEquals(null, nextStartedActivity);
    }

    @Test
    public void shouldNotLaunchWebViewIfNoEndpointParam() throws Exception {
        SecureWebViewActivity.launchWithEndpoint(spySecureWebViewActivity, TITLE);

        Intent nextStartedActivity = shadowOf(spySecureWebViewActivity).getNextStartedActivity();

        assertEquals(null, nextStartedActivity);
    }

    @Test
    public void shouldHaveTheCorrectLayout() throws Exception {
        spySecureWebViewActivity.onCreate(null);

        verify(spySecureWebViewActivity).setContentView(R.layout.secure_webview_layout);
    }

    @Test
    public void shouldBindViewsWithButterKnife() throws Exception {
        spySecureWebViewActivity.onCreate(null);

        assertNotNull(spySecureWebViewActivity.webView);
        assertNotNull(spySecureWebViewActivity.loadingSpinnerContainer);
    }

    @Test
    public void shouldInjectDependencies() throws Exception {
        spySecureWebViewActivity.onCreate(null);

        assertNotNull(spySecureWebViewActivity.presenter);
    }

    @Test
    public void shouldFinishActivityWhenIntentIsNull() throws Exception {
        when(spySecureWebViewActivity.getIntent()).thenReturn(null);

        spySecureWebViewActivity.onCreate(null);

        verify(spySecureWebViewActivity).finish();
    }

    @Test
    public void shouldFinishActivityWhenIntentNotNullAndDoesNotHaveTitle() throws Exception {
        when(mockIntent.getStringExtra(SHL_WEB_VIEW_TOOLBAR_TITLE_KEY)).thenReturn(null);

        spySecureWebViewActivity.onCreate(null);

        verify(spySecureWebViewActivity).finish();
    }

    @Test
    public void shouldFinishActivityWhenAllRequiredDependenciesNull() throws Exception {
        when(mockIntent.getStringExtra(SHL_WEB_VIEW_URL_KEY)).thenReturn(null);
        when(mockIntent.getStringArrayExtra(SHL_WEB_VIEW_ENDPOINT_TYPE_KEY)).thenReturn(null);
        when(mockIntent.getStringExtra(SHL_WEB_VIEW_POST_SCRIPT_KEY)).thenReturn(null);
        when(mockIntent.getStringExtra(SHL_WEB_VIEW_ANTIFORGERY_TOKEN_KEY)).thenReturn(null);

        spySecureWebViewActivity.onCreate(null);

        verify(spySecureWebViewActivity).finish();
    }

    @Test
    public void shouldCreateWithPostRequest() throws Exception {
        when(mockIntent.getStringExtra(SHL_WEB_VIEW_URL_KEY)).thenReturn(null);
        when(mockIntent.getStringArrayExtra(SHL_WEB_VIEW_ENDPOINT_TYPE_KEY)).thenReturn(null);

        doNothing().when((BaseActivity) spySecureWebViewActivity).inject();

        spySecureWebViewActivity.onCreate(null);

        verify(mockPresenter).createWithPostRequest(spySecureWebViewActivity, POST_SCRIPT, ANTI_FORGERY_TOKEN);
    }

    @Test
    public void shouldLoginWhenIntentNotNullAndUrlNotNullAndEndpointsNull() throws Exception {
        when(mockIntent.getStringArrayExtra(SHL_WEB_VIEW_ENDPOINT_TYPE_KEY)).thenReturn(null);

        spySecureWebViewActivity.onCreate(null);

        verify(spySecureWebViewActivity).login(anyString(), anyString(), anyString());
    }

    @Test
    public void shouldLoginWhenIntentNotNullAndUrlNullAndEndpointsNotNull() throws Exception {
        when(mockIntent.getStringExtra(SHL_WEB_VIEW_URL_KEY)).thenReturn(null);

        spySecureWebViewActivity.onCreate(null);

        verify(spySecureWebViewActivity).login(anyString(), anyString(), anyString());
    }

    @Test
    public void shouldCreatePresenterAndLoginWithTargetUrl() throws Exception {
        spySecureWebViewActivity.onCreate(null);

        verify(spySecureWebViewActivity).login(anyString(), anyString(), anyString());
    }

    @Test
    public void shouldSetToolbarTitle() throws Exception {
        ActionBar mockActionBar = mock(ActionBar.class);
        when(spySecureWebViewActivity.getSupportActionBar()).thenReturn(mockActionBar);

        spySecureWebViewActivity.onCreate(null);

        verify(spySecureWebViewActivity).setSupportActionBar(any(Toolbar.class));
        verify(mockActionBar).setDisplayHomeAsUpEnabled(true);
        verify(mockActionBar).setTitle(TITLE);
    }

    @Test
    public void shouldGoBackWhenBackButtonClicked() throws Exception {
        MenuItem mockMenuItem = mock(MenuItem.class);

        when(mockMenuItem.getItemId()).thenReturn(android.R.id.home);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(spySecureWebViewActivity).onBackPressed();

        boolean result = spySecureWebViewActivity.onOptionsItemSelected(mockMenuItem);

        verify(spySecureWebViewActivity).onBackPressed();
        assertTrue(result);
    }

    @Test
    public void shouldPerformSuperOnOptionsItemSelected() throws Exception {
        MenuItem mockMenuItem = mock(MenuItem.class);

        when(mockMenuItem.getItemId()).thenReturn(android.R.id.background);

        spySecureWebViewActivity.onOptionsItemSelected(mockMenuItem);

        verify(spySecureWebViewActivity, never()).onBackPressed();
    }

    @Test
    public void displaySnackBarErrorForHttpError() throws Exception {
        SecureWebViewActivity.SecureWebViewClient client =
                new SecureWebViewActivity.SecureWebViewClient(mockPresenter, TARGET_URL, O_AUTH_URL, LOGIN_SCRIPT_AS_URL);

        client.onReceivedHttpError(mockWebView, webResourceRequest, mockWebResourceResponse);

        verify(mockPresenter).onLoadingUrlError(webResourceRequest.getUrl().toString());
    }

    @Test
    public void displaySnackBarErrorForRecievedErrorForTargetUrl() throws Exception {
        SecureWebViewActivity.SecureWebViewClient client =
                new SecureWebViewActivity.SecureWebViewClient(mockPresenter, TARGET_URL, O_AUTH_URL, LOGIN_SCRIPT_AS_URL);

        client.onReceivedError(mockWebView, webResourceRequest, mockWebResourceError);

        verify(mockPresenter).onLoadingUrlError(webResourceRequest.getUrl().toString());
    }

    @Test
    public void doNotDisplaySnackBarErrorForRecievedErrorForNonTargetUrl() throws Exception {
        SecureWebViewActivity.SecureWebViewClient client =
                new SecureWebViewActivity.SecureWebViewClient(mockPresenter, NON_TARGET_URL, O_AUTH_URL, LOGIN_SCRIPT_AS_URL);

        client.onReceivedError(mockWebView, webResourceRequest, null);

        verify(mockPresenter, never()).onLoadingUrlError(anyString());
    }

    @Test
    public void shouldFinishIfPresenterIsNull() throws Exception {
        Method presenterCheckMethod = SecureWebViewActivity.class.getDeclaredMethod("presenterNullCheck", SecureWebViewPresenter.class);
        presenterCheckMethod.setAccessible(true);

        SecureWebViewPresenter nullPresenter = null;
        boolean result = (Boolean) presenterCheckMethod.invoke(spySecureWebViewActivity, nullPresenter);

        verify(spySecureWebViewActivity).finish();
        assertTrue(result);
    }

    @Test
    public void shouldLoginIfPresenterIsNotNull() throws Exception {
        Method presenterCheckMethod = SecureWebViewActivity.class.getDeclaredMethod("presenterNullCheck", SecureWebViewPresenter.class);
        presenterCheckMethod.setAccessible(true);

        SecureWebViewPresenter mockPresenter = mock(SecureWebViewPresenter.class);
        boolean result = (Boolean) presenterCheckMethod.invoke(spySecureWebViewActivity, mockPresenter);

        verify(spySecureWebViewActivity, never()).finish();
        assertFalse(result);
    }

    private WebResourceRequest webResourceRequest = new WebResourceRequest() {
        @Override
        public Uri getUrl() {
            return Uri.parse(TARGET_URL);
        }

        @Override
        public boolean isForMainFrame() {
            return false;
        }

        @Override
        public boolean isRedirect() {
            return false;
        }

        @Override
        public boolean hasGesture() {
            return false;
        }

        @Override
        public String getMethod() {
            return null;
        }

        @Override
        public Map<String, String> getRequestHeaders() {
            return null;
        }
    };


}

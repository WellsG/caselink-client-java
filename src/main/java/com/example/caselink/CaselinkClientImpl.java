package com.example.caselink;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class CaselinkClientImpl implements CaselinkClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaselinkClientImpl.class);
    private static final String API_MANUAL_CASE = "manual";
    private static final String API_AUTO_CASE = "auto";
    private static final String API_LINKAGE = "link";

    private String serverURL;
    private HttpClient httpclient;

    public static CaselinkClient create(String url) {
        final CaselinkClientImpl impl = new CaselinkClientImpl(url);
        return impl;
    }

    public CaselinkClientImpl(String serverURL) {
        LOGGER.info("Starting caselink client for server url: {}", serverURL);
        this.serverURL = serverURL;
    }

	public List<Case> listManualCases() throws Exception {
		String cases = execute(API_MANUAL_CASE, null);
		PagedList<Case> pagedList = null;
		if (!StringUtils.isEmpty(cases)) {
		    try {
		        pagedList = new Gson().fromJson(cases, new TypeToken<PagedList<Case>>(){}.getType());
		    } catch (Exception e) {
		        LOGGER.error("", e);
		    }
		}
		return pagedList != null ? pagedList.getResults() : null;

	}

	public List<Case> listAutoCases() throws Exception {
		String cases = execute(API_AUTO_CASE, null);
		PagedList<Case> pagedList = null;
		if (!StringUtils.isEmpty(cases)) {
		    try {
		        pagedList = new Gson().fromJson(cases, new TypeToken<PagedList<Case>>(){}.getType());
		    } catch (Exception e) {
		        LOGGER.error("", e);
		    }
		}
		return pagedList != null ? pagedList.getResults() : null;
	}

	public Case createManualCase(Case manualCase) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", manualCase.getId());
		params.put("type", manualCase.getType());
		params.put("title", manualCase.getTitle());
		params.put("automation", manualCase.getAutomation());
		params.put("commit", manualCase.getCommit());
		params.put("project", manualCase.getProject());
		params.put("archs", manualCase.getArchs().toArray());
		params.put("documents", manualCase.getDocuments().toArray());
		String result = executePost(API_MANUAL_CASE + "/", params);
		LOGGER.info(result);
		Case resultCase = null;
		if (!StringUtils.isEmpty(result)) {
			if (result.contains("WorkItem with this id already exists.")) {
				throw new Exception(result);
			}
		    try {
		    	resultCase = new Gson().fromJson(result, Case.class);
		    } catch (Exception e) {
		        LOGGER.error("", e);
		    }
		}
		return resultCase;
	}

	public Case getCaseById(String id) throws Exception {
		String manualCase = execute(API_MANUAL_CASE + "/" + id, null);
		Case caseResult = null;
		if (!StringUtils.isEmpty(manualCase)) {
            try {
            	caseResult = new Gson().fromJson(manualCase, Case.class);
            } catch (Exception e) {
                LOGGER.error("", e);
            }
        }
		return caseResult;
	}

	public void updateManualCase(Case manualCase) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", manualCase.getId());
		params.put("type", manualCase.getType());
		params.put("title", manualCase.getTitle());
		params.put("automation", manualCase.getAutomation());
		params.put("commit", manualCase.getCommit());
		params.put("project", manualCase.getProject());
		params.put("archs", manualCase.getArchs().toArray());
		params.put("documents", manualCase.getDocuments().toArray());
		String result = executePut(API_MANUAL_CASE + "/" + manualCase.getId() + "/", params);
		LOGGER.info(result);
	}

	public Linkage createLinkage(Linkage linkage) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("autocase_pattern", linkage.getAutocase_pattern());
		params.put("title", linkage.getTitle());
		params.put("workitem", linkage.getWorkitem());
		String result = executePost(API_LINKAGE + "/", params);
		LOGGER.info(result);
		Linkage link = null;
		if (!StringUtils.isEmpty(result)) {
			if (result.contains("non_field_errors")) {
				throw new Exception(result);
			}
			if (result.contains("object does not exist")) {
				throw new Exception(result);
			}
		    try {
		    	link = new Gson().fromJson(result, Linkage.class);
		    } catch (Exception e) {
		        LOGGER.error("", e);
		    }
		}
		return link;
	}

    public synchronized HttpClient client() {
        if (httpclient == null) {
            System.setProperty("sun.security.krb5.debug", "true");
            System.setProperty("jsse.enableSNIExtension", "false");
            System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
            // Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            httpclient = wrapClient(new DefaultHttpClient());
        }
        return httpclient;
    }
    
    private String execute(String url, Map<String, String> params) throws Exception {
        StringBuffer urls = new StringBuffer();
        urls.append(serverURL).append(url);
        if (params != null) {
            final List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            if (params != null) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
            }
            urls.append("?").append(URLEncodedUtils.format(qparams, "utf-8"));
        }
        LOGGER.info("Excute method: {}", urls.toString());
        HttpUriRequest request = new HttpGet(urls.toString());
        request.setHeader("Content-Type", "application/json");
        try {
            HttpResponse response = client().execute(request);
            return parseResponse(response);
        } finally {
            request.abort();
        }
    }

	private String executePost(String url, Map<String, Object> params) throws Exception {
		StringBuffer urls = new StringBuffer();
		urls.append(serverURL).append(url);
		String jsonParams = new Gson().toJson(params);
		LOGGER.info("Excute method: {}", urls.toString());
		LOGGER.info("Params: {}", jsonParams);
		HttpPost request = new HttpPost(urls.toString());
		request.setHeader("Content-Type", "application/json");
		request.setHeader("Accept", "application/json");
		StringEntity entity = new StringEntity(jsonParams);
		entity.setContentType("application/json");
		request.setEntity(entity);
		try {
			HttpResponse response = client().execute(request);
			return parseResponse(response);
		} finally {
			request.abort();
		}
	}

	private String executePut(String url, Map<String, Object> params) throws Exception {
		StringBuffer urls = new StringBuffer();
		urls.append(serverURL).append(url);
		String jsonParams = new Gson().toJson(params);
		LOGGER.info("Excute method: {}", urls.toString());
		LOGGER.info("Params: {}", jsonParams);
		HttpPut request = new HttpPut(urls.toString());
		request.setHeader("Content-Type", "application/json");
		request.setHeader("Accept", "application/json");
		StringEntity entity = new StringEntity(jsonParams);
		entity.setContentType("application/json");
		request.setEntity(entity);
		try {
			HttpResponse response = client().execute(request);
			return parseResponse(response);
		} finally {
			request.abort();
		}
	}

    private String parseResponse(HttpResponse response) throws Exception {
        StringBuffer sb = new StringBuffer();
        if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() <= 300) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
        } else {
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    public static org.apache.http.client.HttpClient wrapClient(org.apache.http.client.HttpClient base) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {
                }
            };
            X509HostnameVerifier hostnameVerifier = new AllowAllHostnameVerifier();
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, hostnameVerifier);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
            registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
            ClientConnectionManager mgr = new PoolingClientConnectionManager(registry);

            DefaultHttpClient httpclient = new DefaultHttpClient(mgr, base.getParams());
            httpclient.getAuthSchemes().register(AuthPolicy.SPNEGO, new SPNegoSchemeFactory());
            Credentials credential = new Credentials() {
                public String getPassword() {
                    return null;
                }

                public Principal getUserPrincipal() {
                    return null;
                }
            };
            httpclient.getCredentialsProvider().setCredentials(new AuthScope(null, -1, null), credential);
            return httpclient;
        } catch (Exception ex) {
            LOGGER.error("Wrap httpclient error.", ex);
            return null;
        }
    }

}


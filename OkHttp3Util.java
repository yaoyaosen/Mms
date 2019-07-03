package xyz.mryyaos.blog.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Yyaosen
 * @version 1.0
 * @date 2019/6/25 23:54
 */
@Slf4j
public final class OkHttp3Util {

  private static final Logger LOGGER = LoggerFactory.getLogger(OkHttp3Util.class);

  private static OkHttpClient okHttpClient;

  static {
    OkHttpClient.Builder clientbuilder = new OkHttpClient.Builder();
    // 读取超时
    clientbuilder.readTimeout(5, TimeUnit.SECONDS);
    // 连接超时
    clientbuilder.connectTimeout(3, TimeUnit.SECONDS);
    // 写入超时
    clientbuilder.writeTimeout(5, TimeUnit.SECONDS);
    // OkHttp3拦截器
    HttpLoggingInterceptor httpLogInterceptor =
        new HttpLoggingInterceptor(message -> LOGGER.debug(":: {}", message));
    // 拦截器日志分类 4种
    httpLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    // 添加拦截器
    clientbuilder.addInterceptor(httpLogInterceptor);
    clientbuilder.connectionPool(new ConnectionPool(12, 8, TimeUnit.MINUTES));

    okHttpClient = clientbuilder.build();
  }

  private OkHttp3Util() {}

  /**
   * 异步请求
   *
   * @param request 构建的请求对象
   * @param responseCallback 异步返回的response回调
   */
  public static void enqueue(Request request, Callback responseCallback) {
    okHttpClient.newCall(request).enqueue(responseCallback);
  }

  /**
   * 同步请求
   *
   * @param request 构建的请求对象
   * @return 返回的response
   */
  public static Response execute(Request request) throws IOException {
    return okHttpClient.newCall(request).execute();
  }

  public static String httpPost(String url, String json) {
    LOGGER.info("URL={}", url);
    String result = null;
    OkHttpClient httpClient = new OkHttpClient();
    RequestBody requestBody =
        FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    Request request = new Request.Builder().url(url).post(requestBody).build();

    Response response;
    try {
      response = httpClient.newCall(request).execute();
      assert response.body() != null;
      result = response.body().string();
      LOGGER.info("originResultMsg={}", result);
    } catch (IOException e) {
      LOGGER.error("http_err", e);
    }
    return result;
  }

  /**
   * get 请求
   *
   * @param url 请求地址
   * @param item 请求参数
   * @return 请求结果
   */
  public static String httpGet(String url, Object... item) {
    Request request = new Request.Builder().url(String.format(url, item)).get().build();
    Response data;
    try {
      data = OkHttp3Util.execute(request);
      if (data.body() != null) {
        return data.body().string();
      }
    } catch (IOException e) {
      log.error(e.getLocalizedMessage());
    }
    return null;
  }

  public static String getRequestUrl(String url, LinkedHashMap<String, String> params) {
    StringBuilder builder = new StringBuilder(url);
    boolean isFirst = true;
    for (String key : params.keySet()) {
      if (key != null && params.get(key) != null) {
        if (isFirst) {
          isFirst = false;
          builder.append("?");
        } else {
          builder.append("&");
        }
        builder.append(key).append("=").append(params.get(key));
      }
    }
    return builder.toString();
  }

  public static String jointUrl(String url, Map<String, String> params) {
    if (params != null) {
      Iterator<String> it = params.keySet().iterator();
      StringBuilder urlStringBuilder = null;
      while (it.hasNext()) {
        String key = it.next();
        String value = params.get(key);
        if (urlStringBuilder == null) {
          urlStringBuilder = new StringBuilder();
          urlStringBuilder.append("?");
        } else {
          urlStringBuilder.append("&");
        }
        urlStringBuilder.append(key);
        urlStringBuilder.append("=");
        urlStringBuilder.append(value);
      }
      if (urlStringBuilder != null) {
        url += urlStringBuilder.toString();
      }
    }
    return url;
  }

  /**
   * OutputStream 转String
   *
   * @param outPut InputStream
   * @return String
   * @throws IOException io异常
   */
  public static String parsInToString(InputStream outPut) throws IOException {
    StringBuilder out = new StringBuilder();
    byte[] b = new byte[4096];
    for (int n; (n = outPut.read(b)) != -1; ) {
      out.append(new String(b, 0, n));
    }
    return out.toString();
  }
}

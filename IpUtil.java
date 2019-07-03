package xyz.mryyaos.blog.utils;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Mr.Yyaosen
 * @version 1.0
 * @date 2019/6/25 23:54
 */
public final class IpUtil {

  private IpUtil() {}

  /** 获取访问用户的客户端IP（适用于公网与局域网）. */
  public static String getIpAddr(final HttpServletRequest request) throws Exception {
    if (request == null) {
      throw (new Exception("getIpAddr method HttpServletRequest Object is null"));
    }
    String unknown = "unknown";
    String ipString = request.getHeader("x-forwarded-for");
    if (StringUtils.isBlank(ipString) || unknown.equalsIgnoreCase(ipString)) {
      ipString = request.getHeader("Proxy-Client-IP");
    }
    if (StringUtils.isBlank(ipString) || unknown.equalsIgnoreCase(ipString)) {
      ipString = request.getHeader("WL-Proxy-Client-IP");
    }
    if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
      ipString = request.getRemoteAddr();
    }

    // 多个路由时，取第一个非unknown的ip
    final String[] arr = ipString.split(",");
    for (final String str : arr) {
      if (!"unknown".equalsIgnoreCase(str)) {
        ipString = str;
        break;
      }
    }

    return ipString;
  }
}

package xyz.mryyaos.blog.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * simple jsen rest response do not add more create function such as succeed, to keep response class
 * facade simple and more readable a simple response like { "CODE": 1, "MSG": "ERROR reason" }
 *
 * <p>{ "CODE": 0, "DATA": [ {"name": "lucy"}, {"name": "echo"}, {"name": "jack"} ] } also think use
 * Map to replace JSONObject
 *
 * @author Mr.Yyaosen
 * @version 1.0
 * @date 2019/6/25 23:54
 */
public class ResponseBase extends JSONObject {

  /** support use slf4j */
  private static final Logger LOGGER = LoggerFactory.getLogger(ResponseBase.class);

  // @facade start

  /** only hatch by create */
  private ResponseBase() {}

  /**
   * simple create
   *
   * @return result object
   */
  public static ResponseBase create() {
    return new ResponseBase().code(0);
  }

  /**
   * simple create with a bean object
   *
   * @param o a map or java bean or JSONObject
   * @return result object
   */
  @SuppressWarnings("unused")
  public static ResponseBase create(Object o) {
    ResponseBase responseBase = create();
    responseBase.putAll((JSONObject) JSON.toJSON(o));
    return responseBase;
  }
  // @facade end

  /**
   * easy when chain
   *
   * @param key key
   * @param value role
   * @return self
   */
  public ResponseBase add(String key, Object value) {
    put(key, value);
    return this;
  }

  /**
   * easy to merge result
   *
   * @param map content eg JSONObject
   * @return self
   */
  @SuppressWarnings("unused")
  public ResponseBase addAll(Map<? extends String, ?> map) {
    putAll(map);
    return this;
  }

  /**
   * @param o a java bean object
   * @return self
   */
  public ResponseBase append(Object o) {
    putAll((JSONObject) JSON.toJSON(o));
    return this;
  }

  // @start some schema CODE
  // include CODE、DATA、MSG、HCODE、ECODE、ERROR

  /**
   * a SWResponseBase response object must contain CODE key to classier bad request
   *
   * @param code succeed 0 otherwise 1
   * @return self
   */
  public ResponseBase code(int code) {
    return add(SimpleKey.CODE, code);
  }

  /**
   * in most situation DATA is a complex object or a list
   *
   * @param data any primary type 、 JSONArray 、 JSONObject 、 Map 、 List etc.
   * @return self
   */
  public ResponseBase data(Object data) {
    return add(SimpleKey.DATA, data);
  }

  /**
   * in most situation when an ERROR thrown (CODE eq 1),some ERROR MSG or tips will store in here
   *
   * @param msg content
   * @return self
   */
  ResponseBase msg(String msg) {
    // debug the ERROR MSG
    // because of in common, a MSG not empty
    // only if something oops...
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(msg);
    }
    return add(SimpleKey.MSG, msg);
  }

  /**
   * spring try to return a httpCode=200 response,but the actual httpCode is not 200,so we can send
   * the real httpCode here also suggest return httpCode 401、403 when auth ERROR
   *
   * @param hcode content, eg 404
   * @return self
   */
  @SuppressWarnings("unused")
  public ResponseBase hcode(int hcode) {
    return add(SimpleKey.HCODE, hcode);
  }

  /**
   * when some ERROR happened,MSG or CODE can not easy to location the ERROR, so ECODE can easier
   * classier the logical and business ERROR
   *
   * @param ecode content
   * @return self
   */
  @SuppressWarnings("unused")
  public ResponseBase ecode(int ecode) {
    return add(SimpleKey.ECODE, ecode);
  }

  /**
   * it just like MSG,but you can store ERROR when a exception exactly be catch
   *
   * @param error content
   * @return self
   */
  public ResponseBase error(String error) {
    return add(SimpleKey.ERROR, error);
  }

  // @end some schema CODE

  /** simple response key help */
  public interface SimpleKey {

    String MSG = "msg";
    String DATA = "data";
    String HCODE = "hcode";
    String ECODE = "ecode";
    String ERROR = "error";
    String CODE = "code";
  }
}

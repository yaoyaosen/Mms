package xyz.mryyaos.blog.utils;

import java.util.ArrayList;

/**
 * @author Mr.Yyaosen
 * @version 1.0
 * @date 2019/6/25 23:54
 */
public class ResponseHelp {

  /** 只返回 CODE = 0 */
  private static final ResponseBase SUCCEED = ResponseBase.create().code(0);
  /** 只包含data的成功数据 */
  private static final ResponseBaseThreadLocal<ResponseBase> SUCCEED_DATA =
      new ResponseBaseThreadLocal<>(ResponseBase.create().code(0));
  /** 包含一个eff字段 */
  private static final ResponseBaseThreadLocal<ResponseBase> SUCCEED_EFF =
      new ResponseBaseThreadLocal<>(ResponseBase.create().code(0));
  /** 返回一个分页结构的数据 */
  private static final ResponseBaseThreadLocal<ResponseBase> SUCCEED_PAGE =
      new ResponseBaseThreadLocal<>(ResponseBase.create().code(0));
  /** 返回一个失败信息 */
  private static final ResponseBaseThreadLocal<ResponseBase> FAILED =
      new ResponseBaseThreadLocal<>(ResponseBase.create().code(1));

  /**
   * 优先使用prefab
   *
   * @return rb
   */
  public static ResponseBase prefabSimpleSucceed() {
    return SUCCEED;
  }

  /**
   * 优先使用prefab
   *
   * @return rb
   */
  public static ResponseBase prefabSimpleSucceedData(Object data) {
    return SUCCEED_DATA.get().data(data);
  }

  /**
   * 优先使用prefab
   *
   * @return rb
   */
  public static ResponseBase prefabSimpleSucceedPage(
      Object list, Integer total, Integer capacity, Integer page) {
    return SUCCEED_PAGE
        .get()
        .add("list", list)
        .add(
            "pagination",
            ResponseBase.create()
                .add("total", total)
                .add("pageSize", capacity)
                .add("current", page));
  }

  /**
   * 优先使用prefab
   *
   * @return rb
   */
  public static ResponseBase prefabSimpleEmptyPage() {
    return SUCCEED_PAGE
        .get()
        .add("list", new ArrayList<>())
        .add(
            "pagination",
            ResponseBase.create().add("total", 0).add("pageSize", 10).add("current", 1));
  }

  /**
   * 优先使用prefab
   *
   * @return rb
   */
  public static ResponseBase prefabSimpleSucceedEff(int eff) {
    return SUCCEED_EFF.get().add("eff", eff);
  }

  /**
   * 优先使用prefab
   *
   * @return rb
   */
  public static ResponseBase prefabSimpleFailed(String msg) {
    return FAILED.get().msg(msg);
  }

  /**
   * 优先使用prefab
   *
   * @return rb
   */
  @SuppressWarnings("unused")
  public static ResponseBase prefabCheckEff(int eff, String failed) {
    if (eff > 0) {
      return prefabSimpleSucceedEff(eff);
    } else {
      return prefabSimpleFailed(failed);
    }
  }

  public static ResponseBase simpleSucceed() {
    return ResponseBase.create().code(0);
  }

  public static ResponseBase simpleFailed(String msg) {
    return ResponseBase.create().code(1).msg(msg);
  }

  /**
   * thread local prefab
   *
   * @param <T> response base only
   */
  private static class ResponseBaseThreadLocal<T> extends ThreadLocal<T> {

    private T initValue;

    ResponseBaseThreadLocal(T initValue) {
      this.initValue = initValue;
    }

    @Override
    protected T initialValue() {
      return this.initValue;
    }
  }
}

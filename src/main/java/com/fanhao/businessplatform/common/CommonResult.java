package com.fanhao.businessplatform.common;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用返回结果
 * @param <T>
 */
@Data
@NoArgsConstructor
public class CommonResult<T> {
    //数据
    private T data;

    private String code;

    private String message;

    private Long count;

    public CommonResult(final T data,
                        final String code,
                        final String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }
}

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

    private String resultCode;

    private String attachMessage;

    public CommonResult(final T data,
                        final String resultCode,
                        final String attachMessage) {
        this.data = data;
        this.resultCode = resultCode;
        this.attachMessage = attachMessage;
    }
}

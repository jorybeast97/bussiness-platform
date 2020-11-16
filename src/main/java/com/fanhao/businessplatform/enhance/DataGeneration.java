package com.fanhao.businessplatform.enhance;

import com.fanhao.businessplatform.entity.Employee;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 自动生成员工相关数据，在application.yml中设计相关参数
 */
@Component
public class DataGeneration implements ApplicationRunner {

    @Value("${data-generation.enable}")
    private boolean enable;

    @Value("${data-generation.data-num}")
    private Integer dataNum;

    //TODO 一切生成操作在该区域执行
    @Override
    public void run(ApplicationArguments args) throws Exception {

    }


}

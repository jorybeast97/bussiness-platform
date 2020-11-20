package com.fanhao.businessplatform.service;

import com.fanhao.businessplatform.mapper.OperationLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("operationLogService")
public class OperationLogService {
    @Autowired
    private OperationLogMapper operationLogMapper;
}

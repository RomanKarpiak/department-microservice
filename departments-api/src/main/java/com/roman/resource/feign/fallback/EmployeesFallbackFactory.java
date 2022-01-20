package com.roman.resource.feign.fallback;

import com.roman.resource.feign.EmployeesFeignClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmployeesFallbackFactory implements FallbackFactory<EmployeesFeignClient> {

    @Override
    public EmployeesFeignClient create(Throwable throwable) {
        return new EmployeesFeignClientFallback(throwable);
    }
}

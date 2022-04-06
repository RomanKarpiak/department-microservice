package com.roman.resource.feign.fallback;

import com.roman.resource.feign.EmployeesFeignClient;
import com.roman.resource.feign.exception.FallbackException;
import dto.employee.EmployeeFullInfoDto;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EmployeesFeignClientFallback implements EmployeesFeignClient {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Throwable throwable;

    public EmployeesFeignClientFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public List<EmployeeFullInfoDto> findByDepartmentId(Long departmentId) {
        if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
            logger.error("404 page not found" + departmentId
                    + "error message: " + throwable.getLocalizedMessage());
        } else {
            logger.error("Other error took place: " + throwable.getLocalizedMessage());
        }
       throw new FallbackException("Using fallback method for findByDepartmentId method because of exception - " + throwable.getLocalizedMessage());
    }
}



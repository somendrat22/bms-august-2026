package com.acciojobs.bms_august.schedulers;

import com.acciojobs.bms_august.services.CompanyService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SystemScheduler {

    private CompanyService companyService;

    public SystemScheduler(CompanyService companyService){
        this.companyService = companyService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void executeOnce() {
        companyService.registerInternalCompany();
    }

}

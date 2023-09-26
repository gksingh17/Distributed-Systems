package service.message;

import service.core.QuotationService;

public class Init {
    private QuotationService service;

    public Init(QuotationService service) {
        this.service = service;
    }

    public QuotationService getService() {
        return service;
    }

    public void setService(QuotationService service) {
        this.service = service;
    }
}

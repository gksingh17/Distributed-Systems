package service.core;

import java.util.List;

public class ClientApplication {

    private int applicationNumber;
    private ClientInfo clientInfo;
    private List<Quotation> quotationList;

    public ClientApplication(int applicationNumber, ClientInfo clientInfo, List<Quotation> quotationList) {
        this.applicationNumber = applicationNumber;
        this.clientInfo = clientInfo;
        this.quotationList = quotationList;
    }

    public ClientApplication() {
    }

    public int getApplicationNumber() {

        return applicationNumber;
    }

    public void setApplicationNumber(int applicationNumber) {

        this.applicationNumber = applicationNumber;
    }

    public ClientInfo getClientInfo() {

        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {

        this.clientInfo = clientInfo;
    }

    public List<Quotation> getQuotationList() {

        return quotationList;
    }

    public void setQuotationList(List<Quotation> quotationList) {

        this.quotationList = quotationList;
    }
}

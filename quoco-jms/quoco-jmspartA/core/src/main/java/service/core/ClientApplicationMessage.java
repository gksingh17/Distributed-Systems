package service.core;

import com.sun.org.apache.xpath.internal.operations.Quo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClientApplicationMessage implements Serializable {
    public long id;
    public ClientInfo clientInfo;
    public List<Quotation> quotationList = new ArrayList<Quotation>();

    public ClientApplicationMessage(long id, ClientInfo clientInfo, List<Quotation> quotationList){
        this.id = id;
        this.clientInfo = clientInfo;
        this.quotationList = quotationList;
    }

}

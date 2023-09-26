package service.core;

import java.util.StringJoiner;

/**
 * Class to store the quotations returned by the quotation services
 *
 * @author Rem
 */
public class Quotation implements java.io.Serializable {
    public String company;
    public String reference;
    public double price;

    public Quotation(String company, String reference, double price) {
        this.company = company;
        this.reference = reference;
        this.price = price;

    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Quotation.class.getSimpleName() + "[", "]")
                .add("company='" + company + "'")
                .add("reference='" + reference + "'")
                .add("price=" + price)
                .toString();
    }
}

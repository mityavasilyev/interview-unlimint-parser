package io.github.mityavasilyev.interviewunlimintparser.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class Order {

    private Long id;
    private Long orderId;
    private Double amount;
    private Currency currency;
    private String comment;
    private String fileName;
    private Long line;
    private String parsingResult;

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", orderId:" + orderId +
                ", amount:" + amount +
                ", currency:" + currency.getShortName() +    // Uncomment if currency is ever needed
                ", comment:'" + comment + '\'' +
                ", filename:'" + fileName + '\'' +
                ", line:" + line +
                ", result:'" + parsingResult + '\'' +
                '}';
    }
}

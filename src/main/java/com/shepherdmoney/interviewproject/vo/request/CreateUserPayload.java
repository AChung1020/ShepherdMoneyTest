package com.shepherdmoney.interviewproject.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shepherdmoney.interviewproject.model.CreditCard;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateUserPayload {

    private String name;

    private String email;

    // added DOB as instructed on assignment
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate DOB;

    private List<CreditCard> creditCards;
}

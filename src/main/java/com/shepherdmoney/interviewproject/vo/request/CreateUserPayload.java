package com.shepherdmoney.interviewproject.vo.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateUserPayload {

    private String name;

    private String email;

    private LocalDate DOB;
}

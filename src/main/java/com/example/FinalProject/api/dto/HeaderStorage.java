package com.example.FinalProject.api.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
//@Scope("prototype")
public class HeaderStorage {

    private String header;
}

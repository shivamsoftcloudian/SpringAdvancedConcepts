package com.softclouds.SpringAdvancedConcepts.dto;

import com.softclouds.SpringAdvancedConcepts.enums.Status;
import lombok.Data;

@Data
public class PackageDTO {

    private Long id;
    private String packageName;
    private String packageVersion;
    private String packageDescription;
    private int price;
    private Status status;
}

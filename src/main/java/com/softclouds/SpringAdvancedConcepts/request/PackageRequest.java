package com.softclouds.SpringAdvancedConcepts.request;

import com.softclouds.SpringAdvancedConcepts.enums.Status;
import lombok.Data;

@Data
public class PackageRequest {

    private String packageName;
    private String packageDescription;
    private int price;
    private Status status;
}

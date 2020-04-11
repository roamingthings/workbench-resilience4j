package de.roamingthings.workbench.resilience4j.usecases.sum;

import lombok.Value;

@Value
public class SumResponse {
    Integer numberA;
    Integer numberB;
    Integer sum;
    String origin;
}

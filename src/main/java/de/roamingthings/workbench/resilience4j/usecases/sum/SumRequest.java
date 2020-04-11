package de.roamingthings.workbench.resilience4j.usecases.sum;

import lombok.Value;

@Value
public class SumRequest {
    Integer numberA;
    Integer numberB;
}

package it.unibo.ewp.api.iia.get;

import java.util.List;

public interface ConditionsHashCalculator {

    List<String> calculate(byte[] content);

}

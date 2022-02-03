package it.unibo.ewp.api.iia.get;

public class ConditionsHashCalculatorVersionsBefore6 extends ConditionsHashCalculatorRelativeXPathImpl {
    public ConditionsHashCalculatorVersionsBefore6() {
        super("(.//. | .//@* | .//namespace::*)[ancestor-or-self::*[local-name()='cooperation-conditions']]");
    }
}

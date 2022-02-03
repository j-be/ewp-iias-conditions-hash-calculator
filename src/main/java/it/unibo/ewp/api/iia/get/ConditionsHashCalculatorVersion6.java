package it.unibo.ewp.api.iia.get;

public class ConditionsHashCalculatorVersion6 extends ConditionsHashCalculatorRelativeXPathImpl {
    public ConditionsHashCalculatorVersion6() {
        super("(.//. | .//@* | .//namespace::*)[ancestor-or-self::*[local-name()='cooperation-conditions']][not(ancestor-or-self::*[local-name()='sending-contact' or local-name()='receiving-contact'])]");
    }
}

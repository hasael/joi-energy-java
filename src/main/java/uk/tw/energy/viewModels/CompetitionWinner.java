package uk.tw.energy.viewModels;

import java.math.BigDecimal;

public class CompetitionWinner {

    private final String smartReaderId;
    private final BigDecimal cost;

    public CompetitionWinner(String smartReaderId, BigDecimal cost) {
        this.smartReaderId = smartReaderId;
        this.cost = cost;
    }

    public String getSmartReaderId() {
        return smartReaderId;
    }

    public BigDecimal getCost() {
        return cost;
    }
}

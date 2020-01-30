package uk.tw.energy.domain;

import uk.tw.energy.types.MeterId;

import java.util.List;

public class MeterReadings {

    private List<ElectricityReading> electricityReadings;
    private MeterId smartMeterId;

    public MeterReadings() { }

    public MeterReadings(MeterId smartMeterId, List<ElectricityReading> electricityReadings) {
        this.smartMeterId = smartMeterId;
        this.electricityReadings = electricityReadings;
    }

    public List<ElectricityReading> getElectricityReadings() {
        return electricityReadings;
    }

    public MeterId getSmartMeterId() {
        return smartMeterId;
    }
}

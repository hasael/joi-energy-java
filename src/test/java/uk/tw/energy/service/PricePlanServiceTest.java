package uk.tw.energy.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PricePlanServiceTest {
    private static final String PRICE_PLAN_1_ID = "test-supplier";
    private static final String PRICE_PLAN_2_ID = "best-supplier";
    private static final String PRICE_PLAN_3_ID = "second-best-supplier";
    private static final String SMART_METER_ID = "smart-meter-id";

    @Mock
    MeterReadingService mockMeterReadingService;

    @Test
    public void getCostForEachPricePlan() {

        //GIVEN
        PricePlan pricePlan1 = new PricePlan(PRICE_PLAN_1_ID, null, BigDecimal.TEN, emptyList());
        PricePlan pricePlan2 = new PricePlan(PRICE_PLAN_2_ID, null, BigDecimal.ONE, emptyList());
        PricePlan pricePlan3 = new PricePlan(PRICE_PLAN_3_ID, null, BigDecimal.valueOf(2), emptyList());

        Instant thursday = ZonedDateTime.of(LocalDateTime.of(2020, 1, 30, 12, 0), ZoneId.systemDefault()).toInstant();
        Instant friday = ZonedDateTime.of(LocalDateTime.of(2020, 1, 31, 12, 0), ZoneId.systemDefault()).toInstant();

        ElectricityReading reading1 = new ElectricityReading(thursday, BigDecimal.valueOf(1.0));
        ElectricityReading reading2 = new ElectricityReading(friday, BigDecimal.valueOf(2.0));

        List<ElectricityReading> electricityReadings = Arrays.asList(reading1, reading2);

        List<PricePlan> pricePlans = Arrays.asList(pricePlan1, pricePlan2, pricePlan3);
        when(mockMeterReadingService.getReadings(anyString())).thenReturn(Optional.of(electricityReadings));

        PricePlanService sut = new PricePlanService(pricePlans, mockMeterReadingService);

        HashMap<String, BigDecimal> expectedValues = new HashMap<>();
        expectedValues.put(PRICE_PLAN_1_ID, BigDecimal.valueOf(1.0));
        expectedValues.put(PRICE_PLAN_2_ID, BigDecimal.valueOf(0.1));
        expectedValues.put(PRICE_PLAN_3_ID, BigDecimal.valueOf(0.2));

        Optional<Map<String, BigDecimal>> expected = Optional.of(expectedValues);

        //WHEN
        Optional<Map<String, BigDecimal>> actual = sut.getConsumptionCostOfElectricityReadingsForEachPricePlan(SMART_METER_ID);

        //THEN
        assertEquals(expected, actual);
    }


    @Test
    public void getCostWithMultipliers() {

        //GIVEN
        PricePlan pricePlan1 = new PricePlan(PRICE_PLAN_1_ID, null, BigDecimal.TEN, emptyList());
        PricePlan pricePlan2 = new PricePlan(PRICE_PLAN_2_ID, null, BigDecimal.ONE, Arrays.asList(new PricePlan.PeakTimeMultiplier(DayOfWeek.FRIDAY, BigDecimal.TEN)));
        PricePlan pricePlan3 = new PricePlan(PRICE_PLAN_3_ID, null, BigDecimal.valueOf(2), emptyList());

        Instant thursday = ZonedDateTime.of(LocalDateTime.of(2020, 1, 30, 12, 0), ZoneId.systemDefault()).toInstant();
        Instant friday = ZonedDateTime.of(LocalDateTime.of(2020, 1, 31, 12, 0), ZoneId.systemDefault()).toInstant();

        ElectricityReading reading1 = new ElectricityReading(thursday, BigDecimal.valueOf(1.0));
        ElectricityReading reading2 = new ElectricityReading(friday, BigDecimal.valueOf(2.0));

        List<ElectricityReading> electricityReadings = Arrays.asList(reading1, reading2);

        List<PricePlan> pricePlans = Arrays.asList(pricePlan1, pricePlan2, pricePlan3);
        when(mockMeterReadingService.getReadings(anyString())).thenReturn(Optional.of(electricityReadings));

        PricePlanService sut = new PricePlanService(pricePlans, mockMeterReadingService);

        HashMap<String, BigDecimal> expectedValues = new HashMap<>();
        expectedValues.put(PRICE_PLAN_1_ID, BigDecimal.valueOf(1.0));
        expectedValues.put(PRICE_PLAN_2_ID, BigDecimal.valueOf(0.6));
        expectedValues.put(PRICE_PLAN_3_ID, BigDecimal.valueOf(0.2));

        Optional<Map<String, BigDecimal>> expected = Optional.of(expectedValues);

        //WHEN
        Optional<Map<String, BigDecimal>> actual = sut.getConsumptionCostOfElectricityReadingsForEachPricePlan(SMART_METER_ID);

        //THEN
        assertEquals(expected, actual);
    }
}

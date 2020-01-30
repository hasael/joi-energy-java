package uk.tw.energy.service;

import org.junit.Before;
import org.junit.Test;
import uk.tw.energy.types.MeterId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MeterReadingServiceTest {

    private MeterReadingService meterReadingService;

    @Before
    public void setUp() {
        meterReadingService = new MeterReadingService(new HashMap<>());
    }

    @Test
    public void givenMeterIdThatDoesNotExistShouldReturnNull() {
        assertThat(meterReadingService.getReadings(MeterId.of("unknown-id"))).isEqualTo(Optional.empty());
    }

    @Test
    public void givenMeterReadingThatExistsShouldReturnMeterReadings() {
        meterReadingService.storeReadings(MeterId.of("random-id"), new ArrayList<>());
        assertThat(meterReadingService.getReadings(MeterId.of("random-id"))).isEqualTo(Optional.of(new ArrayList<>()));
    }
}

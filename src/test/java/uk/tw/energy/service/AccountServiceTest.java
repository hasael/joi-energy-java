package uk.tw.energy.service;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;

public class AccountServiceTest {

    private static final String PRICE_PLAN_ID = "price-plan-id";
    private static final String SMART_METER_ID = "smart-meter-id";
    private static final String PRICE_PLAN_ID_2 = "price-plan-id-2";
    private static final String SMART_METER_ID_2 = "smart-meter-id-2";

    private AccountService accountService;

    @Before
    public void setUp() {
        Map<String, String> smartMeterToPricePlanAccounts = new HashMap<>();
        smartMeterToPricePlanAccounts.put(SMART_METER_ID, PRICE_PLAN_ID);
        smartMeterToPricePlanAccounts.put(SMART_METER_ID_2, PRICE_PLAN_ID_2);
        accountService = new AccountService(smartMeterToPricePlanAccounts);
    }

    @Test
    public void givenTheSmartMeterIdReturnsThePricePlanId() throws Exception {
        assertThat(accountService.getPricePlanIdForSmartMeterId(SMART_METER_ID)).isEqualTo(PRICE_PLAN_ID);
    }

    @Test
    public void getallEnrolledSmartMeters() throws Exception {
        List<String> expected = Arrays.asList(SMART_METER_ID, SMART_METER_ID_2)
                .stream()
                .sorted()
                .collect(Collectors.toList());

        List<String> actual = accountService.getEnrolledUsers()
                .stream()
                .sorted()
                .collect(Collectors.toList());

        assertEquals(expected, actual);
    }
}

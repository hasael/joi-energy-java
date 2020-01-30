package uk.tw.energy.controller;

import javafx.util.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.tw.energy.service.AccountService;
import uk.tw.energy.service.PricePlanService;
import uk.tw.energy.types.MeterId;
import uk.tw.energy.viewModels.CompetitionWinner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompetitionControllerTest {

    @Mock
    PricePlanService mockPricePlanService;

    @Mock
    AccountService mockAccountService;

    @Test
    public void returnCorrectValue() {
        MeterId user1 = MeterId.of("user1");
        MeterId user2 = MeterId.of("user2");
        MeterId user3 = MeterId.of("user3");

        String plan1 = "plan1";
        String plan2 = "plan2";
        String plan3 = "plan3";

        BigDecimal cost1 = BigDecimal.ONE;
        BigDecimal cost2 = BigDecimal.valueOf(0.5);
        BigDecimal cost3 = BigDecimal.TEN;
        List<MeterId> users = Arrays.asList(user1, user2, user3);

        when(mockAccountService.getEnrolledUsers()).thenReturn(users);
        when(mockAccountService.getPricePlanIdForSmartMeterId(user1)).thenReturn(plan1);
        when(mockAccountService.getPricePlanIdForSmartMeterId(user2)).thenReturn(plan2);
        when(mockAccountService.getPricePlanIdForSmartMeterId(user3)).thenReturn(plan3);

        when(mockPricePlanService.getConsumptionCostPerPlan(user1, plan1))
                .thenReturn(Optional.of(cost1));
        when(mockPricePlanService.getConsumptionCostPerPlan(user2, plan2))
                .thenReturn(Optional.of(cost2));
        when(mockPricePlanService.getConsumptionCostPerPlan(user3, plan3))
                .thenReturn(Optional.of(cost3));

        CompetitionController sut = new CompetitionController(mockAccountService, mockPricePlanService);

        ResponseEntity<CompetitionWinner> actual = sut.getWinner();

        ResponseEntity<CompetitionWinner> expected = ResponseEntity.ok(new CompetitionWinner(user2.getValue(), cost2));

        assertEquals(expected, actual);
    }


}

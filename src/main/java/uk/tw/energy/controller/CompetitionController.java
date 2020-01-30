package uk.tw.energy.controller;

import javafx.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.tw.energy.service.AccountService;
import uk.tw.energy.service.PricePlanService;
import uk.tw.energy.viewModels.CompetitionWinner;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/competition")
public class CompetitionController {

    private final AccountService accountService;
    private final PricePlanService pricePlanService;

    public CompetitionController(AccountService accountService, PricePlanService pricePlanService) {
        this.accountService = accountService;
        this.pricePlanService = pricePlanService;
    }

    @GetMapping("/winner")
    public ResponseEntity<CompetitionWinner> getWinner() {

        List<String> users = accountService.getEnrolledUsers();
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(users.stream()
                    .map(user -> new Pair<>(user, pricePlanService.getConsumptionCostPerPlan(user, accountService.getPricePlanIdForSmartMeterId(user)).orElse(BigDecimal.ZERO)))
                    .min(Comparator.comparing(Pair::getValue))
                    .map(p -> new CompetitionWinner(p.getKey(), p.getValue()))
                    .get());
        }
    }
}

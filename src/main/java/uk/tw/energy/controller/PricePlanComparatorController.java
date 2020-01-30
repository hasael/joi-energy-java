package uk.tw.energy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.tw.energy.service.AccountService;
import uk.tw.energy.service.PricePlanService;
import uk.tw.energy.types.MeterId;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/price-plans")
public class PricePlanComparatorController {

    private final PricePlanService pricePlanService;
    private final AccountService accountService;

    public final static String PRICE_PLAN_ID_KEY = "pricePlanId";
    public final static String PRICE_PLAN_COMPARISONS_KEY = "pricePlanComparisons";

    public PricePlanComparatorController(PricePlanService pricePlanService, AccountService accountService) {
        this.pricePlanService = pricePlanService;
        this.accountService = accountService;
    }

    @GetMapping("/compare-all/{smartMeterId}")
    public ResponseEntity<Map<String, Object>> calculatedCostForEachPricePlan(@PathVariable String smartMeterId) {
        MeterId meterId = MeterId.of(smartMeterId);
        String pricePlanId = accountService.getPricePlanIdForSmartMeterId(meterId);
        Optional<Map<String, BigDecimal>> consumptionsForPricePlans = pricePlanService.getConsumptionCostOfElectricityReadingsForEachPricePlan(meterId);

        if (!consumptionsForPricePlans.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> pricePlanComparisons = new HashMap<>();
        pricePlanComparisons.put(PRICE_PLAN_ID_KEY, pricePlanId);
        pricePlanComparisons.put(PRICE_PLAN_COMPARISONS_KEY, consumptionsForPricePlans.get());

        return ResponseEntity.ok(pricePlanComparisons);
    }

    @GetMapping("/recommend/{smartMeterId}")
    public ResponseEntity<List<Map.Entry<String, BigDecimal>>> recommendCheapestPricePlans(@PathVariable String smartMeterId, @RequestParam(value = "limit", required = false) Integer limit) {
        MeterId meterId = MeterId.of(smartMeterId);
        Optional<Map<String, BigDecimal>> consumptionsForPricePlans = pricePlanService.getConsumptionCostOfElectricityReadingsForEachPricePlan(meterId);

        if (!consumptionsForPricePlans.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        List<Map.Entry<String, BigDecimal>> recommendations = new ArrayList<>(consumptionsForPricePlans.get().entrySet());
        recommendations.sort(Comparator.comparing(Map.Entry::getValue));

        if (limit != null && limit < recommendations.size()) {
            recommendations = recommendations.subList(0, limit);
        }

        return ResponseEntity.ok(recommendations);
    }
}

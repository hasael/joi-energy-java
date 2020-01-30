package uk.tw.energy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.tw.energy.types.MeterId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private Map<MeterId,String> smartMeterToPricePlanAccounts;

    public AccountService(Map<MeterId,String> smartMeterToPricePlanAccounts) {
        this.smartMeterToPricePlanAccounts = smartMeterToPricePlanAccounts;
    }

    public String getPricePlanIdForSmartMeterId(MeterId smartMeterId) {
        return smartMeterToPricePlanAccounts.get(smartMeterId);
    }

    public List<MeterId> getEnrolledUsers(){

        return new ArrayList<>(smartMeterToPricePlanAccounts.keySet());
    }
}

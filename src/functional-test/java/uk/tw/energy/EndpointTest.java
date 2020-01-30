package uk.tw.energy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import uk.tw.energy.builders.MeterReadingsBuilder;
import uk.tw.energy.domain.MeterReadings;
import uk.tw.energy.types.MeterId;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
public class EndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldStoreReadings() throws JsonProcessingException {
        MeterReadings meterReadings = new MeterReadingsBuilder().generateElectricityReadings().build();
        HttpEntity<String> entity = getStringHttpEntity(meterReadings);

        ResponseEntity<String> response = restTemplate.postForEntity("/readings/store", entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void givenMeterIdShouldReturnAMeterReadingAssociatedWithMeterId() throws JsonProcessingException {
        String smartMeterId = "bob";
        populateMeterReadingsForMeter(smartMeterId);

        ResponseEntity<String> response = restTemplate.getForEntity("/readings/read/" + smartMeterId, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldCalculateAllPrices() throws JsonProcessingException {
        String smartMeterId = "bob";
        populateMeterReadingsForMeter(smartMeterId);

        ResponseEntity<String> response = restTemplate.getForEntity("/price-plans/compare-all/" + smartMeterId, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void givenMeterIdAndLimitShouldReturnRecommendedCheapestPricePlans() throws JsonProcessingException {
        String smartMeterId = "bob";
        populateMeterReadingsForMeter(smartMeterId);

        ResponseEntity<String> response = restTemplate.getForEntity("/price-plans/recommend/" + smartMeterId + "?limit=2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void returnCompetitionWinner() {

        ResponseEntity<String> response = restTemplate.getForEntity("/competition/winner", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private HttpEntity<String> getStringHttpEntity(Object object) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonMeterData = mapper.writeValueAsString(object);
        return (HttpEntity<String>) new HttpEntity(jsonMeterData, headers);
    }

    private void populateMeterReadingsForMeter(String smartMeterId) throws JsonProcessingException {
        MeterId meterId = MeterId.of(smartMeterId);
        MeterReadings readings = new MeterReadingsBuilder().setSmartMeterId(meterId)
                .generateElectricityReadings(20)
                .build();

        HttpEntity<String> entity = getStringHttpEntity(readings);
        restTemplate.postForEntity("/readings/store", entity, String.class);
    }
}
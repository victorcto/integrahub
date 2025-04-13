package br.com.integrahub.services.impls;

import br.com.integrahub.dtos.HubspotErrorResponse;
import br.com.integrahub.dtos.crm.contact.ContactRequest;
import br.com.integrahub.dtos.crm.contact.ContactResponse;
import br.com.integrahub.exceptions.DataValidationException;
import br.com.integrahub.exceptions.HubSpotException;
import br.com.integrahub.exceptions.IntegraHubException;
import br.com.integrahub.services.ContactService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class HubSpotContactServiceImpl implements ContactService {
    private static final String BASE_URL = "https://api.hubapi.com/crm/v3/objects/contacts";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public ContactResponse create(ContactRequest contact) {
        validateContactRequest(contact);

        Map<String, String> properties = new HashMap<>();
        properties.put("email", contact.email());
        properties.put("firstname", contact.firstname());
        properties.put("lastname", contact.lastname());
        Map<String, Object> requestBody = Map.of("properties", properties);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody);
        try {
            ResponseEntity<ContactResponse> response = restTemplate.postForEntity(BASE_URL, request, ContactResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw handleHubspotException(e);
        }
    }

    private void validateContactRequest(ContactRequest contact) {
        if (contact.email() == null || contact.email().isEmpty()) {
            throw new DataValidationException("Email is required");
        }
    }

    @Override
    public ContactResponse get(String id) {
        try {
            ResponseEntity<ContactResponse> response = restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.GET,
                    null, ContactResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw handleHubspotException(e);
        }
    }

    private HubSpotException handleHubspotException(HttpStatusCodeException e) {
        try {
            String responseBody = e.getResponseBodyAsString();
            if (!responseBody.isBlank()) {
                HubspotErrorResponse hubspotError = objectMapper.readValue(responseBody, HubspotErrorResponse.class);
                throw new HubSpotException(hubspotError, e.getStatusCode());
            }

            throw new HubSpotException(e.getMessage(), e.getStatusCode());
        } catch (JsonProcessingException ex) {
            throw new IntegraHubException("Erro ao processar resposta de erro do HubSpot");
        }
    }
}

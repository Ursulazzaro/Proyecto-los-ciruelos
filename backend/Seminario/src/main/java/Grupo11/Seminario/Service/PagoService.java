package Grupo11.Seminario.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import Grupo11.Seminario.DTO.PagoMercadoPagoDTO;
import Grupo11.Seminario.Entities.Enum.EstadoPago;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PagoService {

    @Autowired
    RestTemplate rest_template;

    public PagoMercadoPagoDTO pagar(ResponseEntity<String> response) throws JsonMappingException, JsonProcessingException {
        LocalDate fecha = null;
        LocalTime hora = null;
        
        // Obtenemos el body de la consulta
        ObjectMapper mapper = new ObjectMapper();
        JsonNode response_body = mapper.readTree(response.getBody());
    
        // Se obtiene el método de pago
        JsonNode metodo_pago_node = response_body.get("payment_type_id");
        String metodo_pago = metodo_pago_node.asText();

        switch (metodo_pago) {
            case "debit_card":
                metodo_pago = "TarjetaDebito";
                break;
            case "credit_card":
                metodo_pago = "TarjetaCredito";
                break;
            default:
                metodo_pago = "Transferencia";
                break;
        }

        // Se obtiene el motivo del pago
        JsonNode motivo_node = response_body.get("description");
        String motivo = motivo_node.asText();

        // Se obtiene el email, nombre, apellido, numero y tipo de identificacion del comprador
        JsonNode payer_node = response_body.get("payer");
        String email_pagador = payer_node.get("email").asText();
        String nombre_pagador = payer_node.get("first_name").asText();
        String apellido_pagador = payer_node.get("last_name").asText();

        JsonNode identificacion_node = payer_node.get("identification");
        String tipo_identificacion = identificacion_node.get("type").asText();
        String numero_identificacion = identificacion_node.get("number").asText();
    
        JsonNode estado_cobro_node = response_body.get("status_detail");
        String estado_cobro = estado_cobro_node.asText();
    
        // Se establece el estado del pago
        EstadoPago estado_pago = EstadoPago.Pendiente;
        // Se busca la fecha de pago
        if (estado_cobro.equals("accredited")) {

            // Se obtiene la fecha y hora del pago
            JsonNode fecha_hora_pago = response_body.get("money_release_date");
            String fecha_hora_pago_str = fecha_hora_pago.asText();
            
            // Definir el formato que incluye milisegundos y zona horaria
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            
            // Parsear la fecha y hora con el formato personalizado
            ZonedDateTime fecha_hora_zoned = ZonedDateTime.parse(fecha_hora_pago_str, formatter);
        
            // Extraer la fecha
            fecha = fecha_hora_zoned.toLocalDate();
        
            // Extraer la hora y truncarla a segundos
            hora = fecha_hora_zoned.toLocalTime().truncatedTo(ChronoUnit.SECONDS);

            // Se establece el estado del pago
            estado_pago = EstadoPago.Acreditado;
        }
        // Construir el PagoMercadoPagoDTO
        PagoMercadoPagoDTO pago_mpDTO = new PagoMercadoPagoDTO
        (email_pagador, nombre_pagador, apellido_pagador, 
        tipo_identificacion, numero_identificacion, 
        motivo, metodo_pago, fecha, hora, estado_pago);
            
        return pago_mpDTO;
    } 

    public Boolean validar_pago(ResponseEntity<String> response){
        return !response.getStatusCode().equals(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> buscar_pago(Long id_pago) throws JsonMappingException, JsonProcessingException{
        // URL para obtener un pago
        String url = "https://api.mercadopago.com/v1/payments/"+id_pago;
        
        // Crear el headers del tipo Authorization Bearer Token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.obtener_token_mercado_pago());

        // Crear la entidad HTTP que contiene los headers
        HttpEntity<Object> request = new HttpEntity<>(headers);

        try {
            return rest_template.exchange(url, HttpMethod.GET, request, String.class);
        } catch (HttpClientErrorException e) {
            // Si ocurre un error HTTP como 404, puedes retornar una respuesta con el código adecuado
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
     }
    }

    public String obtener_token_mercado_pago() throws JsonMappingException, JsonProcessingException {
        String url = "https://api.mercadopago.com/oauth/token";  // URL de la API

        // Crear el headers del tipo URLENCODED
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Crear los parámetros de la solicitud
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        // Client_id de la cuenta a depositar
        body.add("client_id", "6872871786610767");
        // Client_secret de la cueta a depositar
        body.add("client_secret", "UKIcaPM5E9Omg3gyYW0tNqCJMeZAaM3G");

        // Crear la entidad HTTP que contiene el cuerpo de la solicitud y los headers
        HttpEntity<Object> request = new HttpEntity<>(body,headers);

        try {
            // Hacer la solicitud POST
            ResponseEntity<String> response = rest_template.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
            );

            ObjectMapper mapper = new ObjectMapper();
            JsonNode response_body = mapper.readTree(response.getBody());

            JsonNode access_token = response_body.get("access_token");
            return access_token.asText();
        } catch (Exception e) {
            return "No se obtuvo el Token";
        }

    }

}

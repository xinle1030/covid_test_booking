package fit3077.lab02team14.assignment2.model.actor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.ui.Model;

import java.io.IOException;

public class UserDeserializer extends JsonDeserializer<Model> {
    @Override
    public User deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonMappingException {

        ObjectMapper mapper = (ObjectMapper) jp.getCodec();

        ObjectNode node = mapper.readTree(jp);

        Boolean isCustomer = node.get("isCustomer").asBoolean();
        Boolean isReceptionist = node.get("isReceptionist").asBoolean();
        Boolean isHealthcareWorker = node.get("isHealthcareWorker").asBoolean();

        // Check the "type" property and map "object" to the suitable class
        if (isReceptionist){
            return mapper.treeToValue(node, Receptionist.class);
        }
        else if (isHealthcareWorker){
            return mapper.treeToValue(node, HealthcareWorker.class);
        }
        else if (isCustomer){
            return mapper.treeToValue(node, Customer.class);
        }
        else{
            throw new JsonMappingException(jp,
            "Invalid value for the \"type\" property");
        }

        // switch (userRole) {

        //     case CUSTOMER:
                

        //     case RECEPTIONIST:
        //         return mapper.treeToValue(node, Receptionist.class);

        //     case HEALTHCAREWORKER:
        //         return mapper.treeToValue(node, HealthcareWorker.class);

        //     case PATIENT:
        //         return mapper.treeToValue(node, Patient.class);

        //     default:
        //         throw new JsonMappingException(jp,
        //                 "Invalid value for the \"type\" property");
        // }
    }
}

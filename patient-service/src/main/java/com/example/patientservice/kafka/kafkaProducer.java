package com.example.patientservice.kafka;

import com.example.patientservice.mapper.PatientMapper;
import com.example.patientservice.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class kafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(kafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public kafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Patient patient) {
        PatientEvent event = PatientEvent.newBuilder().setPatientId(patient.getId().toString()).setName(patient.getName()).setEmail(patient.getEmail()).setEventType("PATIENT_CREATED").build();
        log.info("Sending Patient Event to Kafka");
        try{
            kafkaTemplate.send("patient",event.toByteArray()).get();
        } catch (Exception e) {
            log.error("Error while PatientCreate event: {}",event);
        }
    }
}

package com.redhat.qcon.kafka.services;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.RecordMetadata;
import io.vertx.reactivex.kafka.client.producer.KafkaProducer;
import io.vertx.reactivex.kafka.client.producer.KafkaProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

public class KafkaServiceImpl implements KafkaService {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaServiceImpl.class);

    KafkaProducer<String, String> producer;

    public KafkaServiceImpl(Vertx vertx, JsonObject config) {

        // Convert JSON config to Map<String, String>
        Map<String, String> cfg = config.stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
        producer = KafkaProducer.create(io.vertx.reactivex.core.Vertx.newInstance(vertx), cfg);
    }

    @Override
    public void publish(JsonObject insult, Handler<AsyncResult<Void>> handler) {
        LOG.info("Received Favorite Message: {}", insult.encodePrettily());

        Future<Void> fut = Future.future();
        fut.setHandler(handler);

        insult.put("uuid", UUID.randomUUID().toString());
        KafkaProducerRecord<String, String> favorite = KafkaProducerRecord.create("favorites", insult.encode());
        producer.rxWrite(favorite).subscribe(r -> {
            LOG.info("Message sent to Kafka");
            fut.complete();
        }, e -> {
            LOG.error("Failed to publish message to Kafka");
            fut.fail(e);
        });
    }
}

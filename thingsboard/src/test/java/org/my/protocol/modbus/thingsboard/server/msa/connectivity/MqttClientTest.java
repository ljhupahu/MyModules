package org.my.protocol.modbus.thingsboard.server.msa.connectivity;

import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.thingsboard.mqtt.MqttClient;
import org.thingsboard.mqtt.MqttClientConfig;
import org.thingsboard.mqtt.MqttHandler;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.security.DeviceCredentials;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

@Slf4j
public class MqttClientTest extends AbstractContainerTest {

    /**
     * 单个间隔1秒循环发送数据
     * @throws Exception
     */
    @Test
    public void singleDevice() throws Exception {
        restClient.login("tenant@thingsboard.org", "tenant");
        DeviceCredentials deviceCredentials = getDeviceCredentials("d4113a60-d32d-11eb-be04-69f17b5ad1dc");

        MqttClient mqttClient = getMqttClient(deviceCredentials, null);
        for (; ; ) {
            mqttClient.publish("v1/devices/me/telemetry", Unpooled.wrappedBuffer(devicePayload().toString().getBytes())).get();
            Thread.sleep(1000);
        }
    }

    @NotNull
    private DeviceCredentials getDeviceCredentials(String uuid) {
        Device device = new Device();
        device.setId(new DeviceId(toUUID(uuid)));

        return restClient.getDeviceCredentialsByDeviceId(device.getId()).get();
    }

    protected JsonObject devicePayload() {
        JsonObject values = new JsonObject();
        values.addProperty("temperature", 20 + new Random().nextInt(20));
        return values;
    }

    private UUID toUUID(String id) {
        return UUID.fromString(id);
    }

    private MqttClient getMqttClient(DeviceCredentials deviceCredentials, MqttMessageListener listener) throws InterruptedException, ExecutionException {
        MqttClientConfig clientConfig = new MqttClientConfig();
        clientConfig.setClientId("MQTT client from test");
        clientConfig.setUsername(deviceCredentials.getCredentialsId());
        MqttClient mqttClient = MqttClient.create(clientConfig, listener);
        mqttClient.connect("localhost", 1883).get();
        return mqttClient;
    }


    @Data
    private class MqttMessageListener implements MqttHandler {
        private final BlockingQueue<MqttEvent> events;

        private MqttMessageListener() {
            events = new ArrayBlockingQueue<>(100);
        }

        @Override
        public void onMessage(String topic, ByteBuf message) {
            log.info("MQTT message [{}], topic [{}]", message.toString(StandardCharsets.UTF_8), topic);
            events.add(new MqttEvent(topic, message.toString(StandardCharsets.UTF_8)));
        }
    }

    @Data
    private class MqttEvent {
        private final String topic;
        private final String message;
    }
}

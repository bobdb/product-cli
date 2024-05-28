package net.bobdb;

import java.util.List;

public record GPTRequest(String model, List<GPTMessage> messages, double temperature) {
}

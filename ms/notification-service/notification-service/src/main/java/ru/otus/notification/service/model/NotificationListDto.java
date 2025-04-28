package ru.otus.notification.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationListDto {

    private List<Item> notifications;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private UUID id;
        private String sendTo;
        private String subject;
        private String message;
    }
}

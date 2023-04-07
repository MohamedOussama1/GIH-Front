package com.example.frontend;

import javafx.event.Event;
import javafx.event.EventType;

public class WindowChangeEvent extends Event {

    public static final EventType WINDOW_CHANGE_EVENT =
            new EventType<>(EventType.ROOT, "WINDOW_CHANGE_EVENT");

    public WindowChangeEvent() {
        super(WINDOW_CHANGE_EVENT);
    }

}

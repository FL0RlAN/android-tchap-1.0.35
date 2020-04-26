package org.matrix.androidsdk.rest.model;

import java.util.List;

public class EventContext {
    public String end;
    public Event event;
    public List<Event> eventsAfter;
    public List<Event> eventsBefore;
    public String start;
    public List<Event> state;
}

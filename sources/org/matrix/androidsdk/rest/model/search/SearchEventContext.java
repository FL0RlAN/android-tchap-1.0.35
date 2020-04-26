package org.matrix.androidsdk.rest.model.search;

import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.rest.model.Event;

public class SearchEventContext {
    public String end;
    public List<Event> eventsAfter;
    public List<Event> eventsBefore;
    public Map<String, SearchUserProfile> profileInfo;
    public String start;
}

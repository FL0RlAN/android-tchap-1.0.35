package org.matrix.androidsdk.rest.model.search;

import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.rest.model.Event;

public class SearchRoomEventResults {
    public Integer count;
    public Map<String, SearchGroup> groups;
    public String nextBatch;
    public List<SearchResult> results;
    public Map<String, List<Event>> state;
}

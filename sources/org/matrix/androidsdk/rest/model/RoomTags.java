package org.matrix.androidsdk.rest.model;

import java.io.Serializable;
import java.util.Map;

public class RoomTags implements Serializable {
    public Map<String, Map<String, Double>> tags;
}

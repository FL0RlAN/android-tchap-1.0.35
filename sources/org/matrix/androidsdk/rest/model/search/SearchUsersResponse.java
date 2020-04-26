package org.matrix.androidsdk.rest.model.search;

import java.util.List;
import org.matrix.androidsdk.rest.model.User;

public class SearchUsersResponse {
    public Boolean limited;
    public List<User> results;
}

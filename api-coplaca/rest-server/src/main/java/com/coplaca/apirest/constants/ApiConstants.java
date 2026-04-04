package com.coplaca.apirest.constants;

/**
 * Constants for API paths and prefixes to ensure consistency across controllers
 */
public final class ApiConstants {

    private ApiConstants() {
        // Utility class
    }

    // Base API paths
    public static final String API_V1 = "/api/v1";

    // Authentication & Authorization
    public static final String AUTH_BASE = "/auth";
    public static final String AUTH_LOGIN = "/login";
    public static final String AUTH_SIGNUP = "/signup";

    // Resources
    public static final String PRODUCTS = "/products";
    public static final String ORDERS = "/orders";
    public static final String USERS = "/users";
    public static final String CATEGORIES = "/categories";
    public static final String OFFERS = "/offers";
    public static final String WAREHOUSES = "/warehouses";
    public static final String ADMIN = "/admin";
    public static final String LANDING = "/landing";

    // Nested paths
    public static final String CURRENT_USER = "/me";
    public static final String STATS = "/stats";
    public static final String SEARCH = "/search";
    public static final String ACTIVE = "/active";
    public static final String DISABLED = "/disabled";
    public static final String INTERNAL = "/internal";

    // Query parameters
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_SIZE = "size";
    public static final String PARAM_SORT = "sort";
    public static final String PARAM_QUERY = "query";

    // Response messages
    public static final String MSG_SUCCESS = "Operation completed successfully";
    public static final String MSG_CREATED = "Resource created successfully";
    public static final String MSG_UPDATED = "Resource updated successfully";
    public static final String MSG_DELETED = "Resource deleted successfully";
    public static final String MSG_NOT_FOUND = "Resource not found";
    public static final String MSG_UNAUTHORIZED = "Unauthorized access";
    public static final String MSG_FORBIDDEN = "Access forbidden";
    public static final String MSG_BAD_REQUEST = "Invalid request";
}

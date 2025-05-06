package ue.poznan.spring_jwt_auth.auth.config;

public class JwtConstraintsUtils {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_PATH = "/api/v1/auth/refresh-token";

    public enum Token {
        REFRESH,
        AUTHORIZATION,
    }

    public static class Claims {
        public static final String JWT_TOKEN_TYPE = "tokenType";
        public static final String ID = "id";
        public static final String ROLES = "roles";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
    }
}

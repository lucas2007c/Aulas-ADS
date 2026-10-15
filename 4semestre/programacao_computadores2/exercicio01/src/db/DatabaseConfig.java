package db;

public class DatabaseConfig {
    public static final String URL = getRequiredEnvironmentVariable("DB_URL");
    public static final String USER = getRequiredEnvironmentVariable("DB_USER");
    public static final String PASSWORD = getRequiredEnvironmentVariable("DB_PASSWORD");
    
    private static String getRequiredEnvironmentVariable(String variableName) {
        String value = System.getenv(variableName);
        
        if(value == null || value.isBlank()){
            throw new IllegalStateException(
            "A variavel de ambiente" + variableName + " não foi configurada.");
        }
        
        return value;
    }
}

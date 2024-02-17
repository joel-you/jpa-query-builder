package persistence.sql.ddl;

import jakarta.persistence.Id;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryBuilder {
    public String createTableSql(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE ")
                .append(convertCamelCaseToSnakeCase(clazz.getSimpleName()))
                .append(" (");

        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> {
                    field.setAccessible(true);

                    sb.append(convertCamelCaseToSnakeCase(field.getName()))
                            .append(" ")
                            .append(getDbColumnTypeByType(field.getType()))
                            .append(", ");
                });

        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .forEach(field ->
                        sb.append("PRIMARY KEY (")
                                .append(convertCamelCaseToSnakeCase(field.getName()))
                                .append(")")
                );

        sb.append(")");

        return sb.toString();
    }

    private String getDbColumnTypeByType(Class<?> type) {
        if (type == Long.class) {
            return "BIGINT";
        }
        if (type == String.class) {
            return "VARCHAR";
        }
        if (type == Integer.class) {
            return "INTEGER";
        }

        throw new IllegalArgumentException("This type is not supported.");
    }

    public static String convertCamelCaseToSnakeCase(String input) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        String result = matcher.replaceAll(replacement);

        return result.toLowerCase();
    }
}

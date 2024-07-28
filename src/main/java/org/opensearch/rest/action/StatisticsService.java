package org.opensearch.rest.action;
import org.opensearch.rest.model.Ups;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatisticsService {

    public double calculateAverage(List<Ups> statuses, String fieldName) {
        double sum = 0;
        int count = 0;

        for (Ups status : statuses) {
            try {
                Field field = Ups.class.getField(fieldName);
                Object value = field.get(status);

                if (value instanceof Number) {
                    sum += ((Number) value).doubleValue();
                    count++;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
            }
        }

        return count > 0 ? sum / count : 0.0;
    }


    public double findMaxValue(List<Ups> statuses, String fieldName) {
        double maxValue = Double.MIN_VALUE;

        for (Ups status : statuses) {
            try {
                Field field = Ups.class.getField(fieldName);
                Object value = field.get(status);

                if (value instanceof Number) {
                    maxValue = Math.max(maxValue, ((Number) value).doubleValue());
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
            }
        }

        return maxValue == Double.MIN_VALUE ? 0 : maxValue;
    }

    public Set<String> extractUniqueValues(List<Ups> statuses, String fieldName) {
        Set<String> uniqueValues = new HashSet<>();

        for (Ups status : statuses) {
            try {
                Field field = Ups.class.getField(fieldName);
                Object value = field.get(status);

                if (value instanceof String) {
                    uniqueValues.add((String) value);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
            }
        }

        return uniqueValues;
    }
}

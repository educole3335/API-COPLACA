package com.coplaca.apirest.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

/**
 * Servicio de geolocalización y cálculo de distancias
 * Usa la fórmula de Haversine para calcular distancia entre dos puntos (lat, lon)
 */
@Service
public class GeolocationService {

    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double AVERAGE_DELIVERY_SPEED_KMH = 40.0; // Velocidad promedio en reparto urbano
    private static final int MINUTES_PER_STOP = 5; // Minutos por parada adicional

    /**
     * Calcula la distancia entre dos coordenadas usando la fórmula de Haversine
     *
     * @param lat1 Latitud del punto 1
     * @param lon1 Longitud del punto 1
     * @param lat2 Latitud del punto 2
     * @param lon2 Longitud del punto 2
     * @return Distancia en kilómetros
     */
    public BigDecimal calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
            return BigDecimal.ZERO;
        }

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_KM * c;

        return new BigDecimal(String.format("%.2f", distance));
    }

    /**
     * Calcula el tiempo estimado de entrega en minutos
     *
     * @param distanceKm Distancia en kilómetros
     * @param numberOfStops Número de paradas adicionales en la ruta
     * @return Tiempo estimado en minutos
     */
    public Integer calculateEstimatedMinutes(BigDecimal distanceKm, Integer numberOfStops) {
        if (distanceKm == null || distanceKm.compareTo(BigDecimal.ZERO) < 0) {
            return 30;
        }

        double timeInMinutes = (distanceKm.doubleValue() / AVERAGE_DELIVERY_SPEED_KMH) * 60;
        
        if (numberOfStops != null && numberOfStops > 0) {
            timeInMinutes += (numberOfStops * MINUTES_PER_STOP);
        }

        // Añadir 5 minutos de buffer para preparación
        timeInMinutes += 5;

        return (int) Math.ceil(timeInMinutes);
    }

    /**
     * Calcula la coordenada promedio entre múltiples puntos
     * Útil para optimizar rutas
     */
    public double[] calculateCentroid(java.util.List<double[]> coordinates) {
        if (coordinates == null || coordinates.isEmpty()) {
            return new double[]{0, 0};
        }

        double sumLat = 0;
        double sumLon = 0;
        for (double[] coord : coordinates) {
            sumLat += coord[0];
            sumLon += coord[1];
        }

        return new double[]{
                sumLat / coordinates.size(),
                sumLon / coordinates.size()
        };
    }

    /**
     * Valida si las coordenadas son válidas
     */
    public boolean areCoordinatesValid(Double latitude, Double longitude) {
        return latitude != null && longitude != null &&
                latitude >= -90 && latitude <= 90 &&
                longitude >= -180 && longitude <= 180;
    }
}

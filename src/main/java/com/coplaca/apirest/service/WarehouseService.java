package com.coplaca.apirest.service;

import com.coplaca.apirest.exception.ResourceNotFoundException;
import com.coplaca.apirest.entity.Warehouse;
import com.coplaca.apirest.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class WarehouseService {
    
    private final WarehouseRepository warehouseRepository;
    
    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }
    
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findByIsActiveTrue();
    }
    
    public Warehouse getWarehouseById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
    }
    
    public Warehouse createWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }
    
    public Warehouse updateWarehouse(Long id, Warehouse warehouseDetails) {
        Warehouse w = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
        w.setName(warehouseDetails.getName());
        w.setAddress(warehouseDetails.getAddress());
        w.setLatitude(warehouseDetails.getLatitude());
        w.setLongitude(warehouseDetails.getLongitude());
        w.setCapacity(warehouseDetails.getCapacity());
        w.setPhoneNumber(warehouseDetails.getPhoneNumber());
        w.setManagerName(warehouseDetails.getManagerName());
        return warehouseRepository.save(w);
    }
    
    public Warehouse findNearestWarehouse(double latitude, double longitude) {
        List<Warehouse> warehouses = warehouseRepository.findByIsActiveTrue();
        
        if (warehouses.isEmpty()) {
            return warehouseRepository.findFirstByOrderByIdAsc();
        }
        
        return warehouses.stream()
                .min((w1, w2) -> {
                    double distance1 = calculateDistance(latitude, longitude, w1.getLatitude(), w1.getLongitude());
                    double distance2 = calculateDistance(latitude, longitude, w2.getLatitude(), w2.getLongitude());
                    return Double.compare(distance1, distance2);
                })
                .orElse(null);
    }
    
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}

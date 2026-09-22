/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.brisvegastech.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author rudra
 */
@Service
public class NativeQueryService {
    @PersistenceContext
    private EntityManager entityManager; // Framework injects this automatically

    public List<Object[]> fetchServices() {
        // Execute a raw SQL query returning a list of Object arrays or maps
        String sql = "SELECT sm.id, sm.name, sm.description FROM service_master sm WHERE sm.status = 'paid'";
        List<Object[]> services = entityManager.createNativeQuery(sql).getResultList();
        // Your logic here
        for (Object[] row : services) {
            Object serviceId = row[0];
            Object serviceName = row[1];
            Object serviceDescription = row[2];
            // Process your data here
            System.out.println("serviceId "+serviceId+" serviceName "+serviceName+" serviceDescription "+serviceDescription);
        }
        return services;
    }
}

package com.laundry.service;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.laundry.entity.LaundryShop;
import com.laundry.entity.Order;
import com.laundry.entity.User;
import com.laundry.exception.ResourceNotFoundException;
import com.laundry.model.UserRole;
import com.laundry.repository.LaundryShopRepository;
import com.laundry.repository.OrderRepository;
import com.laundry.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final LaundryShopRepository laundryShopRepository;
    private final UserRepository userRepository;
    
    public List<Order> getOrdersByDeliveryBoy(User deliveryBoy) {
        return orderRepository.findByDeliveryBoy(deliveryBoy);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }
    public List<Order> getOrdersByShop(LaundryShop shop) {
        return orderRepository.findByShop(shop);
    }
    
    //Distance Calculate Method
    
    public double calculateDistance(double lat1, double lon1,
            double lat2, double lon2) {

final int R = 6371;

double latDistance = Math.toRadians(lat2 - lat1);
double lonDistance = Math.toRadians(lon2 - lon1);

double a =
Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
+ Math.cos(Math.toRadians(lat1))
* Math.cos(Math.toRadians(lat2))
* Math.sin(lonDistance / 2)
* Math.sin(lonDistance / 2);

double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

return R * c;
}
    public LaundryShop findNearestShop(double userLat, double userLon) {

        List<LaundryShop> shops = laundryShopRepository.findByActiveTrue();

        LaundryShop nearestShop = null;
        double minDistance = Double.MAX_VALUE;

        for(LaundryShop shop : shops){

            double distance = calculateDistance(
                    userLat,
                    userLon,
                    shop.getLatitude(),
                    shop.getLongitude()
            );

            if(distance < minDistance){

                minDistance = distance;
                nearestShop = shop;

            }
        }

        return nearestShop;
    }
    
    //NearestDeliveryBoy
    
    public User findNearestDeliveryBoy(double userLat, double userLon) {

        List<User> deliveryBoys = userRepository.findByRole(UserRole.DELIVERY);

        User nearest = null;
        double minDistance = Double.MAX_VALUE;

        for(User boy : deliveryBoys){

            double distance = calculateDistance(
                    userLat,
                    userLon,
                    boy.getLatitude(),
                    boy.getLongitude()
            );

            if(distance < minDistance){

                minDistance = distance;
                nearest = boy;

            }

        }

        return nearest;
    }
    
    public User findAvailableDeliveryBoy(){

        List<User> deliveryBoys =
                userRepository.findByRoleAndAvailableTrue("DELIVERY");

        if(deliveryBoys.isEmpty()){
            return null;
        }

        return deliveryBoys.get(0);

    }
}
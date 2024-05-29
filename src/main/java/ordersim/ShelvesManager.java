package ordersim;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ShelvesManager {
    private Shelf hotShelf;
    private Shelf coldShelf;
    private Shelf frozenShelf;
    private Shelf overflowShelf;

    private Lock hotShelfLock;
    private Lock coldShelfLock;
    private Lock frozenShelfLock;
    private Lock overflowShelfLock;

    Map<String, Shelf> map;

    public ShelvesManager(int hotCapacity, int coldCapacity, int frozenCapacity, int overflowCapacity) {
        this.hotShelf = Shelf.createShelf(hotCapacity, Config.HOT_DECAY_MODIFIER);
        this.coldShelf = Shelf.createShelf(coldCapacity, Config.COLD_DECAY_MODIFIER);
        this.frozenShelf = Shelf.createShelf(frozenCapacity, Config.FROZEN_DECAY_MODIFIER);
        this.overflowShelf = Shelf.createShelf(overflowCapacity, Config.OVERFLOW_DECAY_MODIFIER);

        this.hotShelfLock = new ReentrantLock();
        this.coldShelfLock = new ReentrantLock();
        this.frozenShelfLock = new ReentrantLock();
        this.overflowShelfLock = new ReentrantLock();

        this.map = new ConcurrentHashMap<>();
    }

    public void addOrderToShelf(Order order) {
        boolean success = false;
        if (order.getTemperature() == Temperature.HOT) {
            hotShelfLock.lock();
            try {
                List<Order> discardedOrders = hotShelf.cleanup();
                if (!discardedOrders.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String currentTime = LocalDateTime.now().format(formatter);
                    for (Order discardedOrder : discardedOrders) {
                        System.out.println(currentTime + "[ShelvesManager] Cleanup order: " + discardedOrder.getId() + " from hot shelf");
                    }

                    for (Order discardedOrder : discardedOrders) {
                        map.remove(discardedOrder.getId());
                    }
                    printShelves();
                }
                success = hotShelf.addOrder(order);
                if (success) {
                    map.put(order.getId(), hotShelf);
                }
            } finally {
                hotShelfLock.unlock();
            }
        } else if (order.getTemperature() == Temperature.COLD) {
            coldShelfLock.lock();
            try {
                List<Order> discardedOrders = coldShelf.cleanup();
                if (!discardedOrders.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String currentTime = LocalDateTime.now().format(formatter);
                    for (Order discardedOrder : discardedOrders) {
                        System.out.println(currentTime + "[ShelvesManager] Cleanup order: " + discardedOrder.getId() + " from hot shelf");
                    }

                    for (Order discardedOrder : discardedOrders) {
                        map.remove(discardedOrder.getId());
                    }
                    printShelves();
                }
                success = coldShelf.addOrder(order);
                if (success) {
                    map.put(order.getId(), coldShelf);
                }
            } finally {
                coldShelfLock.unlock();
            }
        } else if (order.getTemperature() == Temperature.FROZEN) {
            frozenShelfLock.lock();
            try {
                List<Order> discardedOrders = frozenShelf.cleanup();
                if (!discardedOrders.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String currentTime = LocalDateTime.now().format(formatter);
                    for (Order discardedOrder : discardedOrders) {
                        System.out.println(currentTime + "[ShelvesManager] Cleanup order: " + discardedOrder.getId() + " from hot shelf");
                    }

                    for (Order discardedOrder : discardedOrders) {
                        map.remove(discardedOrder.getId());
                    }
                    printShelves();
                }
                success = frozenShelf.addOrder(order);
                if (success) {
                    map.put(order.getId(), frozenShelf);
                }
            } finally {
                frozenShelfLock.unlock();
            }
        }

        if (success) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String currentTime = LocalDateTime.now().format(formatter);

            System.out.println(currentTime + "[ShelvesManager] Added order: " + order.getId() + " to shelf");
            printShelves();
        } else {
            overflowShelfLock.lock();
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String currentTime = LocalDateTime.now().format(formatter);

                List<Order> discardedOrders = overflowShelf.cleanup();
                if (!discardedOrders.isEmpty()) {
                    for (Order discardedOrder : discardedOrders) {
                        System.out.println(currentTime + "[ShelvesManager] Cleanup order: " + discardedOrder.getId() + " from hot shelf");
                    }

                    for (Order discardedOrder : discardedOrders) {
                        map.remove(discardedOrder.getId());
                    }
                    printShelves();
                }

                success = overflowShelf.addOrder(order);
                if (success) {
                    map.put(order.getId(), overflowShelf);
                    System.out.println(currentTime + "[ShelvesManager] Added order: " + order.getId() + " to overflow shelf");
                    printShelves();
                } else {
                    Order removedOrder = overflowShelf.discardOrder();
                    System.out.println(currentTime + "[ShelvesManager] Discarded order: " + removedOrder.getId() + " from overflow shelf");
                    printShelves();
                    success = overflowShelf.addOrder(order);
                    if (success) {
                        map.put(order.getId(), overflowShelf);
                        System.out.println(currentTime + "[ShelvesManager] Added order: " + order.getId() + " to overflow shelf");
                        printShelves();
                    } else {
                        throw new RuntimeException("This should not happen");
                    }
                }
            } finally {
                overflowShelfLock.unlock();
            }
        }
    }

    public Order fetchOrderFromShelf(String orderId) {
        Shelf shelf = map.get(orderId);
        if (shelf == null) {
            return null;
        }

        Order order = null;
        if (shelf == hotShelf) {
            hotShelfLock.lock();
            try {
                List<Order> discardedOrders = hotShelf.cleanup();
                if (!discardedOrders.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String currentTime = LocalDateTime.now().format(formatter);
                    for (Order discardedOrder : discardedOrders) {
                        System.out.println(currentTime + "[ShelvesManager] Cleanup order: " + discardedOrder.getId() + " from hot shelf");
                    }

                    for (Order discardedOrder : discardedOrders) {
                        map.remove(discardedOrder.getId());
                    }
                    printShelves();
                }
                order = hotShelf.fetchOrder(orderId);
            } finally {
                hotShelfLock.unlock();
            }
        } else if (shelf == coldShelf) {
            coldShelfLock.lock();
            try {
                List<Order> discardedOrders = coldShelf.cleanup();
                if (!discardedOrders.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String currentTime = LocalDateTime.now().format(formatter);
                    for (Order discardedOrder : discardedOrders) {
                        System.out.println(currentTime + "[ShelvesManager] Cleanup order: " + discardedOrder.getId() + " from hot shelf");
                    }

                    for (Order discardedOrder : discardedOrders) {
                        map.remove(discardedOrder.getId());
                    }
                    printShelves();
                }
                order = coldShelf.fetchOrder(orderId);
            } finally {
                coldShelfLock.unlock();
            }
        } else if (shelf == frozenShelf) {
            List<Order> discardedOrders = frozenShelf.cleanup();
            if (!discardedOrders.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String currentTime = LocalDateTime.now().format(formatter);
                for (Order discardedOrder : discardedOrders) {
                    System.out.println(currentTime + "[ShelvesManager] Cleanup order: " + discardedOrder.getId() + " from hot shelf");
                }

                for (Order discardedOrder : discardedOrders) {
                    map.remove(discardedOrder.getId());
                }
                printShelves();
            }
            frozenShelfLock.lock();
            try {
                order = frozenShelf.fetchOrder(orderId);
            } finally {
                frozenShelfLock.unlock();
            }
        } else if (shelf == overflowShelf) {
            overflowShelfLock.lock();
            try {
                List<Order> discardedOrders = overflowShelf.cleanup();
                if (!discardedOrders.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String currentTime = LocalDateTime.now().format(formatter);
                    for (Order discardedOrder : discardedOrders) {
                        System.out.println(currentTime + "[ShelvesManager] Cleanup order: " + discardedOrder.getId() + " from hot shelf");
                    }

                    for (Order discardedOrder : discardedOrders) {
                        map.remove(discardedOrder.getId());
                    }
                    printShelves();
                }
                order = overflowShelf.fetchOrder(orderId);
            } finally {
                overflowShelfLock.unlock();
            }
        }

        if (order != null) {
            map.remove(orderId);
            printShelves();
        }

        return order;
    }

    public void printShelves() {
        hotShelfLock.lock();
        coldShelfLock.lock();
        frozenShelfLock.lock();
        overflowShelfLock.lock();
        try {
            System.out.println("--------------------------------------------------");
            System.out.println("Hot shelf:");
            hotShelf.printOrders();
            System.out.println("--------------------------------------------------");
            System.out.println("Cold shelf:");
            coldShelf.printOrders();
            System.out.println("--------------------------------------------------");
            System.out.println("Frozen shelf:");
            frozenShelf.printOrders();
            System.out.println("--------------------------------------------------");
            System.out.println("Overflow shelf:");
            overflowShelf.printOrders();
            System.out.println("--------------------------------------------------");
        } finally {
            hotShelfLock.unlock();
            coldShelfLock.unlock();
            frozenShelfLock.unlock();
            overflowShelfLock.unlock();
        }
    }
}

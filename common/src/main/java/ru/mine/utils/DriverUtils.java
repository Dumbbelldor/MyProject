package ru.mine.utils;

import org.springframework.stereotype.Service;
import ru.mine.domain.Driver;
import ru.mine.repository.DriverRepository;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DriverUtils {

    static DriverRepository repository;

    private DriverUtils() {}

    public static boolean isAnyoneReady() {
        return !repository.findByAvailableTrue().isEmpty();
    }

    public static Integer assignAndGetId(boolean bool) {
        if (bool) {
            List<Driver> readyList = repository.findByAvailableTrue();
            Driver driver = readyList.get(
                    ThreadLocalRandom.current().nextInt(readyList.size()));
            driver.setAvailable(false);
            repository.save(driver);
            return driver.getId();
        } else {
            List<Driver> unreadyList = repository.findAll();
            return unreadyList.get(
                    ThreadLocalRandom.current().nextInt(
                            unreadyList.size())).getId();
        }
    }
}

package com.pos.inventorysystem.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class GenericUtils {
    private GenericUtils() {
        // This constructor is intentionally left empty
    }

    public static String GenerateBarcode() {
        Random random = new Random();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyddss");
        String originalString = LocalDateTime.now().format(formatter);
        long randomNumber = generateRandomNumber(random);
        int splitIndex = 4;
        String prefix = originalString.substring(0, splitIndex);
        String suffix = originalString.substring(splitIndex);

        return prefix + randomNumber + suffix;
    }

    public static String GenerateCustomerNo() {
        Random random = new Random();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyss");
        String originalString = LocalDateTime.now().format(formatter);
        long randomNumber = generateRandomNumber(random);
        int splitIndex = 2;
        String prefix = originalString.substring(0, splitIndex);
        String suffix = originalString.substring(splitIndex);

        return "10" + prefix + randomNumber + suffix;
    }

    public static String GenerateEmployeeNo() {
        Random random = new Random();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyss");
        String originalString = LocalDateTime.now().format(formatter);
        long randomNUmber = generateRandomNumber(random);
        int splitIndex = 2;
        String prefix = originalString.substring(0, splitIndex);
        String suffix = originalString.substring(splitIndex);

        return "15" + prefix + randomNUmber + suffix;
    }

    public static String GenerateInvoiceNo() {
        Random random = new Random();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyss");
        String originalString = LocalDateTime.now().format(formatter);
        long randomNUmber = generateRandomNumber(random);
        int splitIndex = 2;
        String prefix = originalString.substring(0, splitIndex);
        String suffix = originalString.substring(splitIndex);

        return "20" + prefix + randomNUmber + suffix;
    }

    private static long generateRandomNumber(Random random) {
        long lowerBound = 100000L;
        long upperBound = 999999L;

        return lowerBound + (long) (random.nextDouble() * (upperBound - lowerBound));
    }
}

package com.wallet.client;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GrpcClient {
    private static int countOfUserSessions;
    private static int countOfRequestsPerUser;
    private static int countOfRoundsPerUser;

    public static void main(String[] args) {
        if (args == null || args.length < 3) {
            countOfUserSessions = 1;
            countOfRequestsPerUser = 3;
            countOfRoundsPerUser = 3;
        } else {
            try {
                countOfUserSessions = new Integer(args[0]);
                countOfRequestsPerUser = new Integer(args[1]);
                countOfRoundsPerUser = new Integer(args[2]);
            } catch (Exception e){
                System.out.println("error illigal arguments");
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter number of concurrent users emulated");
                countOfUserSessions = scanner.nextInt();
                System.out.println("Enter number of concurrent requests a user will make");
                countOfRequestsPerUser = scanner.nextInt();
                System.out.println("Enter number of rounds each thread is executing");
                countOfRoundsPerUser = scanner.nextInt();
            }
        }


        ExecutorService poolUsersRequests = Executors.newFixedThreadPool(countOfUserSessions);
        for (int i = 0; i < countOfUserSessions; i++) {
            poolUsersRequests.submit(new UserSession(1, countOfRequestsPerUser, countOfRoundsPerUser));
        }
        poolUsersRequests.shutdown();
    }
}

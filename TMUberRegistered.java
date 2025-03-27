/***
 * Name: Silvia Das
 * Student ID: 501239223
 ***/

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.Map;

public class TMUberRegistered
{
    // These variables are used to generate user account and driver ids
    private static int firstUserAccountID = 900;
    private static int firstDriverId = 700;

    // Generate a new user account id
    public static String generateUserAccountId(Map<String, User> current)
    {
        return "" + firstUserAccountID + current.size();
    }

    // Generate a new driver id
    public static String generateDriverId(ArrayList<Driver> current)
    {
        return "" + firstDriverId + current.size();
    }

    // Database of Preregistered users
    // In Assignment 2 these will be loaded from a file
    // The test scripts and test outputs included with the skeleton code use these
    // users and drivers below. You may want to work with these to test your code (i.e. check your output with the
    // sample output provided). 

    public static void loadPreregisteredUsers (Map<String, User> users) // throws IOException
    {
        String accountId = new String(generateUserAccountId(users));
        users.put(accountId, (new User(accountId, "McInerney, T.", "34 5th Street", 25)));
        accountId = generateUserAccountId(users);
        users.put(accountId, (new User(accountId, "Valenzano, R.", "71 1st Street", 55)));
        accountId = generateUserAccountId(users);
        users.put(accountId, (new User(accountId, "Lugez, E.", "55 9th Avenue", 125)));
        accountId = generateUserAccountId(users);
        users.put(accountId, (new User(accountId, "Miranskyy, A.", "15 2nd Avenue", 15)));
        accountId = generateUserAccountId(users);
        users.put(accountId, (new User(accountId, "Raman, P.", "32 3rd Street", 13)));
        accountId = generateUserAccountId(users);
        users.put(accountId, (new User(accountId, "Woungang, I.", "64 6th Avenue", 27)));
        accountId = generateUserAccountId(users);
        users.put(accountId, (new User(accountId, "Soutchanski, M.", "28 7th Avenue", 22)));
        accountId = generateUserAccountId(users);
        users.put(accountId, (new User(accountId, "Harley, E.", "10 7th Avenue", 34)));
        accountId = generateUserAccountId(users);
        users.put(accountId, (new User(accountId, "Mason, D.", "48 8th Street", 11)));
        accountId = generateUserAccountId(users);
        users.put(accountId, (new User(accountId, "Santos, M.", "83 4th Street", 41)));
    }

    /*
    public static Map<String, User> loadPreregisteredUsers (String filename) // throws IOException
    {   
        // Initialize arraylist of users
        Map<String, User> users = new HashMap<>();
        File file = new File(filename);
        Scanner sc = new Scanner(file);

        // Read through each line of file
        while (sc.hasNextLine()){
            String name = sc.nextLine();
            String address = sc.nextLine();
            double wallet=Double.parseDouble(sc.nextLine());
            // Create new user object with given info and add to users array list
            String accountId = new String(generateUserAccountId(users));
            User user = new User(accountId, name, address, wallet);
            users.put(accountId, user);
        }
        sc.close();
        return users;
    }
    */

    // Database of Preregistered drivers
    // In Assignment 2 these will be loaded from a file

    public static void loadPreregisteredDrivers(ArrayList<Driver> drivers) // throws IOException
    {
        drivers.add(new Driver(generateDriverId(drivers), "Tom Cruise", "Toyota Corolla", "MAVERICK", "34 4th Street"));
        drivers.add(new Driver(generateDriverId(drivers), "Brad Pitt",  "Audi S4", "FGDR 983", "85 8th Street"));
        drivers.add(new Driver(generateDriverId(drivers), "Millie Brown",  "Tesla", "STRNGRTHGS", "67 9th Avenue"));
        drivers.add(new Driver(generateDriverId(drivers), "Tim Chalamet",  "Thopter", "DUNE", "21 8th Avenue"));
        drivers.add(new Driver(generateDriverId(drivers), "John Boyega",  "X-Wing", "REBEL", "32 7th Avenue"));
    }

    /*
    public static ArrayList<Driver> loadPreregisteredDrivers(String filename) // throws IOException
    {
        // Initialize arraylist of drivers
        ArrayList<Driver> drivers = new ArrayList<>();
        File file = new File(filename);
        Scanner sc = new Scanner(file);

        // Read through each line of file
        while (sc.hasNextLine()){
            String name = sc.nextLine();
            String carModel = sc.nextLine();
            String licensePlate=sc.nextLine();
            String address=sc.nextLine();
            // Create new driver object with given info and add to drivers array list
            drivers.add(new Driver(generateDriverId(drivers), name, carModel, licensePlate, address));
        }
        sc.close();
        return drivers;

    }
    */

}


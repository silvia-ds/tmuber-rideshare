/***
 * Name: Silvia Das
 * Student ID: 501239223
 ***/

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.LinkedHashMap;
 
 /*
  * Class contains the main logic of the system
  * Tracks all users, drivers and service requests (RIDE or DELIVERY)
  */
 public class TMUberSystemManager
 {
   //private ArrayList<User>   users;
   private static ArrayList<Driver> drivers;
 
   //private ArrayList<TMUberService> serviceRequests;

   private Queue<TMUberService> zone0Queue;
   private Queue<TMUberService> zone1Queue;
   private Queue<TMUberService> zone2Queue;
   private Queue<TMUberService> zone3Queue;
 
   private static Map<String, User> userMap;

   public double totalRevenue; // Total revenues accumulated via rides and deliveries
   
   // Rates per city block
   private static final double DELIVERYRATE = 1.2;
   private static final double RIDERATE = 1.5;
   // Portion of a ride/delivery cost paid to the driver
   private static final double PAYRATE = 0.1;
 
   //These variables are used to generate user account and driver ids
   int userAccountId = 900;
   int driverId = 700;
 
   public TMUberSystemManager()
   {
     userMap = new LinkedHashMap<>();
     drivers = new ArrayList<Driver>();
     //serviceRequests = new ArrayList<TMUberService>(); 

     zone0Queue = new LinkedList<>();
     zone1Queue = new LinkedList<>();
     zone2Queue = new LinkedList<>();
     zone3Queue = new LinkedList<>();

     TMUberRegistered.loadPreregisteredUsers(userMap);
     TMUberRegistered.loadPreregisteredDrivers(drivers);

 
     totalRevenue = 0;
   }
 

   // Sets users and drivers array lists
   public void setUsers(Map<String, User> userMap){
    TMUberRegistered.loadPreregisteredUsers(userMap);
   }

   public static void setDrivers(ArrayList<Driver> driverList){
    TMUberRegistered.loadPreregisteredDrivers(driverList);
   }

   // Gets user from list of users (null if not found)
   public User getUser(String accountId) throws Exception{
    User user = userMap.get(accountId);
    if (user == null)
      throw new Exception("User not found");
    return user;   
  }

   // Gets driver from list of drivers (null if not found)
   public Driver getDriver(String driverId){
    for (Driver driver: drivers){
      if (driver.getId().equals(driverId))
        return driver;
    }
    return null;
  }
   
  // Check for duplicate driver
  private boolean driverExists(Driver driver){
     for (Driver driver2: drivers){
       if (driver.equals(driver2))
         return true;
     }
     return false;
  }
   
   // Given a user, check if user ride/delivery request already exists in service requests
private boolean existingRequest(TMUberService req) {
  if (existingReqInQueue(req, zone0Queue) || existingReqInQueue(req, zone1Queue) || existingReqInQueue(req, zone2Queue) || existingReqInQueue(req, zone3Queue))
      return true;
  return false;
}

private boolean existingReqInQueue(TMUberService req, Queue<TMUberService> queue) {
  for (TMUberService request : queue) {
    if (req.equals(request))
      return true;
  }
  return false;
}

 
   // Calculate the cost of a ride / delivery based on distance 
   private double getDeliveryCost(int distance){
     return distance * DELIVERYRATE;
   }
 
   private double getRideCost(int distance){
     return distance * RIDERATE;
   }
 
   // Print Information (printInfo()) about all registered users in the system
   public void listAllUsers() {
    System.out.println();
    for (User user: userMap.values()) {
        user.printInfo();
        System.out.println();
    }
}
 
   // Print Information (printInfo()) about all registered drivers in the system
   public void listAllDrivers(){
     System.out.println();
     for (Driver driver: drivers){
       driver.printInfo();
       System.out.println();
     }
   }
 
   // Print Information (printInfo()) about all current service requests
   public void listAllServiceRequests(){
    System.out.println("ZONE 0\n======");
    zoneSpecificPrint(zone0Queue);
    System.out.println("ZONE 1\n======");
    zoneSpecificPrint(zone1Queue);    
    System.out.println("ZONE 2\n======");
    zoneSpecificPrint(zone2Queue);    
    System.out.println("ZONE 3\n======");
    zoneSpecificPrint(zone3Queue);
   }

   // Method to print specific request info
   public void zoneSpecificPrint(Queue<TMUberService> requests){
    int i = 1;
    // Iterates through all requests in a queue
    for (TMUberService request : requests) {
      System.out.print(i+". -------------------------");
      request.printInfo();
      System.out.println("\n");
      i++; // Increases count (request #)
    }
   }
 
   // Add a new user to the system
   public void registerNewUser(String name, String address, double wallet) throws Exception{
     // checks validity of new user
     // 1. checks if name is valid (not an empty string)
     if (name.isBlank())
       throw new Exception("Invalid User Name");
     // 2. checks if address is valid (valid address, not empty)
     if (!CityMap.validAddress(address) || address.isBlank())
      throw new Exception("Invalid User Address");
     // 3. checks if wallet is valid
     if (wallet<0)
      throw new Exception("Invalid Money in Wallet");
 
     // creates object of new user & checks if user already exists in the system
     String accountId = TMUberRegistered.generateUserAccountId(userMap);
     User newUser = new User(accountId, name, address, wallet);
     if (userMap.containsKey(accountId))
         throw new Exception("User Already Exists in System");
 
     // adds new user to list of users if all previous conditions are met
     userMap.put(accountId, newUser);
     System.out.println("New user has been successfully added.");
   }
 
   // Add a new driver to the system
   public void registerNewDriver(String name, String carModel, String carLicencePlate, String address) throws Exception{
     // checks validity of driver
     // 1. checks if name is valid (not an empty string)
     if (name.isBlank())
      throw new Exception("Invalid User Name");
     // 2. checks if car model is valid
     if (carModel.isBlank())
      throw new Exception("Invalid Car Model");
     // 3. checks if license plate is valid
     if (carLicencePlate.isBlank())
      throw new Exception("Invalid Car License Plate");

     // 4. checks if address is valid
     if (!(CityMap.validAddress(address)))
      throw new Exception("Invalid address");
 
     // creates new driver & checks if driver already exists in system
     Driver newDriver = new Driver(TMUberRegistered.generateDriverId(drivers), name, carModel, carLicencePlate, address);
     if (driverExists(newDriver))
       throw new Exception("Driver Already Exists in System");
 
     // adds new driver to list of drivers if all previous conditions are met
     drivers.add(newDriver);
     System.out.println("New driver has been successfully added.");
   }
 
   // Request a ride. User wallet will be reduced when drop off happens
   public void requestRide(String accountId, String from, String to) throws Exception{
    // checking for valid parameters
    // 1. check if accountId exists
    User userForThisRide = userMap.get(accountId);
    if (userForThisRide == null)
        throw new Exception("User Account Not Found");

    // 2. checks if from and to are valid addresses
    if (!CityMap.validAddress(from) || !CityMap.validAddress(to))
        throw new Exception("Invalid Address");

    // checks for valid ride conditions
    // checks ride distance
    int distance = CityMap.getDistance(from, to);
    if (distance <= 1)
        throw new Exception("Insufficient Travel Distance");

    // checks if user has sufficient funds
    if (getRideCost(distance) > userForThisRide.getWallet())
        throw new Exception("Insufficient Funds");

    // creates new Uber Ride object
    TMUberRide thisRide = new TMUberRide(from, to, userForThisRide, distance, getRideCost(distance));

    // checks if request already exists
    if (existingRequest(thisRide))
        throw new Exception("User Already Has Ride Request");

    // since all conditions are met,
    // 1. adds to zone-specific arraylist
     if (CityMap.getCityZone(from)==0)
      zone0Queue.add(thisRide);
     else if (CityMap.getCityZone(from)==1)
      zone1Queue.add(thisRide);
     else if (CityMap.getCityZone(from)==2)
      zone2Queue.add(thisRide);     
     else if (CityMap.getCityZone(from)==3)
      zone3Queue.add(thisRide);
     // 4. increments number of rides for this user
     userForThisRide.addRide();
   }
 
   // Request a food delivery. User wallet will be reduced when drop off happens
   public void requestDelivery(String accountId, String from, String to, String restaurant, String foodOrderId) throws Exception{
    // checking for valid parameters
    // 1. check if user accountID exists
    User userForThisDelivery = userMap.get(accountId);
    if (userForThisDelivery == null)
        throw new Exception("User Account Not Found");

    // 2. checks if from and to are valid addresses
    if (!CityMap.validAddress(from) || !CityMap.validAddress(to))
        throw new Exception("Invalid Address");

     // 3. checks if restaurant is valid (not empty)
     if (restaurant.isBlank())
       throw new Exception("Invalid Restaurant");
 
     // checks for valid ride conditions
     // checks ride distance
     int distance = CityMap.getDistance(from, to);
     if (distance<=1)
       throw new Exception("Insufficient Travel Distance");
     // checks if user has sufficient funds
     if (getRideCost(distance) > userForThisDelivery.getWallet())
      throw new Exception("Insufficient Funds");
 
     // creates new Uber Delivery object
     TMUberDelivery thisDelivery = new TMUberDelivery(from, to, userForThisDelivery, distance, getDeliveryCost(distance), restaurant, foodOrderId);
     
     // Check if existing delivery request for this user for this restaurant and food order #
     if (existingRequest(thisDelivery))
       throw new Exception("User Already Has Delivery Request at Restaurant with this Food Order");
 
     // since all conditions are met,
     // 1. adds to zone-specific arraylist
     if (CityMap.getCityZone(from)==0)
      zone0Queue.add(thisDelivery);
     else if (CityMap.getCityZone(from)==1)
      zone1Queue.add(thisDelivery);
     else if (CityMap.getCityZone(from)==2)
      zone2Queue.add(thisDelivery);     
     else if (CityMap.getCityZone(from)==3)
      zone3Queue.add(thisDelivery);
     // 2. increments number of rides for this user
     userForThisDelivery.addDelivery();
    }
 
   // Cancel an existing service request
   public void cancelServiceRequest(int zoneNum, int reqNum) throws Exception{
    if (zoneNum==0)
      cancelReq(zone0Queue, reqNum);
    if (zoneNum==1)
      cancelReq(zone1Queue, reqNum);
    if (zoneNum==2)
      cancelReq(zone2Queue, reqNum);
    if (zoneNum==3)
      cancelReq(zone3Queue, reqNum);
    }

  public void cancelReq(Queue<TMUberService> request, int reqNum) throws Exception{
    if (reqNum < 1 || reqNum > request.size())
      throw new Exception("Invalid Request #: "+reqNum);
    request.remove();
  }
   
   // Drop off a ride or a delivery. This completes a service.
   public void dropOff(String driverId) throws Exception{
    
    // 1. Use id to find driver
    Driver driver = getDriver(driverId);

    // 2. Check driver status
    if ((driver.getStatus()==(Driver.Status.AVAILABLE)))
      throw new Exception("Driver is not currently driving");

    // 3. create instance of this request
    TMUberService thisReq = driver.getRequest();

    // 4. complete necessary actions
    totalRevenue+=thisReq.getCost();            // add service cost to revenues
    driver.pay(thisReq.getCost()*PAYRATE);      // pay the driver
    totalRevenue -= thisReq.getCost()*PAYRATE;  // deduct driver fee from total revenues
    driver.setStatus(Driver.Status.AVAILABLE);  // driver is now available again
    User user = thisReq.getUser();
    user.payForService(thisReq.getCost());      // user pays for ride / delivery
    driver.setAddress(thisReq.getTo());           // sets user address to address delivered to

   }
 
   // Sort methods & their helper classes (by implementing comparator)
   public void sortByUserName(){
    // Convert map to arraylist to use collections.sort() as usual
    ArrayList<User> userList = new ArrayList<>(userMap.values());
    Collections.sort(userList, new nameComparator());
    System.out.println();
    // Iterates through all users & prints as it goes
    for (User user: userList) {
        user.printInfo();
        System.out.println();
    }
   }
 
   private class nameComparator implements Comparator<User>{
     public int compare(User user1, User user2){
       return user1.getName().compareTo(user2.getName());
     }
   }
   
   public void sortByWallet(){
    // Convert map to arraylist to use collections.sort() as usual
    ArrayList<User> userList = new ArrayList<>(userMap.values());
     Collections.sort(userList, new walletComparator());
     // Iterates through all users & prints as it goes
     for (User user: userList) {
      user.printInfo();
      System.out.println();
    }
  }
 
   private class walletComparator implements Comparator<User>{
     public int compare(User user1, User user2){
       return Double.compare(user1.getWallet(), user2.getWallet());
     }
   }

   // Converts queue to arraylist
   public static ArrayList<TMUberService> convertQueue(Queue<TMUberService> queue) {
    return new ArrayList<>(queue);
}

   public void sortByDistance(){
    // Converts queue to arraylist using above method
    ArrayList<TMUberService> list0 = convertQueue(zone0Queue);
    ArrayList<TMUberService> list1 = convertQueue(zone1Queue);
    ArrayList<TMUberService> list2 = convertQueue(zone2Queue);
    ArrayList<TMUberService> list3 = convertQueue(zone3Queue);

    // Uses collections.sort() as usual
     Collections.sort(list0, new distanceComparator());
     Collections.sort(list1, new distanceComparator());
     Collections.sort(list2, new distanceComparator());
     Collections.sort(list3, new distanceComparator());

     // Prints using below method
     System.out.println("ZONE 0\n======");
     listPrint(list0);
     System.out.println("ZONE 1\n======");
     listPrint(list1);
     System.out.println("ZONE 2\n======");
     listPrint(list2);
     System.out.println("ZONE 3\n======");
     listPrint(list3);

    }

  // Prints list items
    public void listPrint(ArrayList<TMUberService> requests){
      // Iterates through all requests in a queue
      for (int i=1; i<requests.size()+1; i++) {
        System.out.print(i+". -------------------------");
        requests.get(i-1).printInfo();
        System.out.println("\n");
      }
    }
   
   private class distanceComparator implements Comparator<TMUberService>{
     public DistanceComparator(CityMap cityMap) {
         this.cityMap = cityMap;
     }
     public int compare(TMUberService req1, TMUberService req2) {
       int dist1 = CityMap.getDistance(req1.getFrom(), req1.getTo());
       int dist2 = CityMap.getDistance(req2.getFrom(), req2.getTo());
       return Integer.compare(dist1, dist2);
     }
   }


  // Assignment 2 extension

  // Method to pickup a request (assign driver to a request)
  public void pickup(String driverId) throws Exception {

    // 1. Find the driver object based on driverId
    Driver driver = getDriver(driverId);
    if (driver==null)
      throw new Exception("Invalid Driver Id");

    // 2. Get the driver's current zone
    int driverZone = CityMap.getCityZone(driver.getAddress());

    // 3. Get the queue for the driver's zone
    Queue<TMUberService> reqQueue = new LinkedList<TMUberService>();
    if (driverZone==0)
      reqQueue=zone0Queue;
    if (driverZone==1)
      reqQueue=zone1Queue;
    if (driverZone==2)
      reqQueue=zone2Queue;
    if (driverZone==3)
      reqQueue=zone3Queue;

    // 4. Check if there's a ride request in the queue
    if (reqQueue.isEmpty())
      throw new Exception("No Service Requests in Zone " + driverZone);

    // 5. Assign the first ride request (remove it from the queue)
    TMUberService rideRequest = reqQueue.poll();

    // 6. Update driver status and location
    driver.setRequest(rideRequest);
    driver.setStatus(Driver.Status.DRIVING);
    driver.setAddress(rideRequest.getFrom());
  }

  // Method to relocate driver
  public void driveTo(String driverId, String address) throws Exception{
    // checks for valid conditions (driver id, valid address, available driver)
    if (!(CityMap.validAddress(address)) || address.isEmpty())
      throw new Exception("Invalid address");
    Driver driver = getDriver(driverId);
    if (driver==null)
      throw new Exception("Invalid Driver Id");
    if (driver.getStatus()==Driver.Status.DRIVING)
      throw new Exception("Driver is already driving");

    // since all conditions are met and no exceptions are caught, change address
    driver.setAddress(address);
  }
  



} 
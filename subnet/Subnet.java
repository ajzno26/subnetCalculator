package subnet;

/** 
 * Class to represent a subnet.
 *
 * @version 1.0
 */

import java.util.*;
import java.math.BigInteger;

public class Subnet {
    private static String input; 

    // Constructor Subnet
    public Subnet(String input) {
        this.input = input;
    }

    // Declare private variables 
    private static long IPAddress = 0L;
    private static String IP = " ";
    private static int prefix = 0; 
    private static int count = 0;

    // Check for errors with CIDR notation and throw exceptions if there are any. 
    private static void errorHandling() throws Exception {
        String[] segments = IP.split("\\.");
        int[] seg = new int[4];
        int check = 1;
        for (int i = 0; i < segments.length; i++) {
            seg[i] = Integer.parseInt(segments[i]);
            if (seg[i] > 255 | seg[i] < 0) {
                check = 0; 
            }
        }
      
        // Throw Exceptions
        if (prefix > 32 || prefix < 0 || prefix == 0) {
            throw new Exception();
        } else if (!(segments.length ==4)) {
            throw new Exception();
        } else if (check == 0) {
            throw new Exception();
        }
    }

    // Convert String to long 
    private static long stringToLong(String str) {
        String[] strSplit = str.split("\\.");
        long first = Long.parseLong(strSplit[0]);
        long second = Long.parseLong(strSplit[1]);
        long third = Long.parseLong(strSplit[2]);
        long fourth = Long.parseLong(strSplit[3]);
        long address = (first << 24 ) + (second << 16) + (third << 8) + fourth;
        return address; 
    }

    // Convert long to String
    private static String longToString(long val) {
        String[] s = new String[4]; 
        for (int i = 3; i >= 0; i--) {
            s[i] = Long.toString(val & 0xff);
            val = val >> 8;
        }
        String sJoined = String.join(".", s);
        return sJoined;
    }
    
    // Returns Address (String) depending on whether getFirstUsableAddress or getLastUsableAddress was called 
    private static String useableAddress(long addressLong) {
        long firstOctet = (addressLong >> 24) & 0xFF;
        long secondOctet = (addressLong >> 16) & 0xFF; 
        long thirdOctet = (addressLong >> 8) & 0xFF;
        long fourthOctet = 0;
        if (count == 1) { 
            fourthOctet = ((addressLong) + 1) & 0xFF; 
        } else if (count == 2) {
            fourthOctet = ((addressLong) - 1) & 0xFF; 
        }
        String useableAddress = firstOctet + "." + secondOctet + "." + thirdOctet + "." + fourthOctet;
        return useableAddress; 
    }

    // Store user input values. Return IP Address (String)
    public static String getIP() {
        String[] str = input.split("/");
        IP = str[0];
        IPAddress = stringToLong(IP);
        prefix = Integer.parseInt(str[1]);
        
        try {
            errorHandling(); 
        } catch (Exception e) {
            System.out.println("Invalid CIDR notation!");
            System.out.println("Usage: java subnet.SubnetCalculator IP/PREFIX");
            System.exit(0);
        }
        return IP; 
    }

    // Get Netmask by using bitwise operations
    public static String getNetmask() {
        int shift = 0xffffffff << (32 - prefix); 
        int firstOct = (shift >> 24) & 0xFF; 
        int secondOct = (shift >> 16) & 0xFF;
        int thirdOct = (shift >> 8) & 0xFF; 
        int fourthOct = shift & 0xFF; 
        String netmask = firstOct + "." + secondOct + "." + thirdOct + "." + fourthOct; 
        if (prefix == 0) {
            return "0.0.0.0";
        } else {
            return netmask;
        }
    }

    // Get number of useable IP Addresses
    public static String getNumUsableIPs() {
        int numUsableIPs = (int) Math.pow(2, (32-prefix)) -2;
        if (numUsableIPs < 0) {
            numUsableIPs = 0;
        }
        if (prefix == 0) {
            return "4294967296";
        } else {
            return String.valueOf(numUsableIPs);
        }
    }

    // Calculate network address by using bitwise operation
    public static String getNetworkAddress() {
        long maskLong = stringToLong(getNetmask());

        // bitwise AND: address (long) & netmask (long) 
        long networkAddressLong = IPAddress & maskLong;
        String networkAddress = longToString(networkAddressLong);
        return networkAddress;
    }

    // Get first useable IP address using network address
    public static String getFirstUsableAddress() {
        count = 1; 
        long addressLong = stringToLong(getNetworkAddress());
        String firstUsableAddress = useableAddress(addressLong); 
        if (prefix == 31 | prefix == 32) {
            return "N/A";
        } else {
            return firstUsableAddress;
        }
    }

    // Get last useable IP address using broadcast address 
    public static String getLastUsableAddress() {
        count = 2; 
        long addressLong = stringToLong(getBroadcastAddress());
        String lastUsableAddress = useableAddress(addressLong); 
        if (prefix == 31 | prefix == 32) {
            return "N/A";
        } else {
            return lastUsableAddress;
        }
    }

    // Get broadcast address by using bitwise operation
    public static String getBroadcastAddress() {
        // Invert netmask 
        long mask = stringToLong(getNetmask()); 
        long inverted = ~mask; 

        // bitwise OR --> inverted netmask | IP address
        long broadcastLong = inverted | IPAddress;
        String broadcastAddress = longToString(broadcastLong);
        return broadcastAddress;
    }
}

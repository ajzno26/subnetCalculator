# subnetCalculator

## Program Design

The program computes the netmask, network address, broadcast address, first and last usable addresses, and the number of usable IPs given the CIDR notation subnet. 

It converts the IP Address (string) to a long value, and performs bitwise operations to calculate netmask, network address, and broadcast address. It converts the long value back to a string value to print statements.

Netmask: shift each octect and bitwise AND operation 

        int shift = 0xffffffff << (32 - prefix); 
        int firstOct = (shift >> 24) & 0xFF; 
        int secondOct = (shift >> 16) & 0xFF;
        int thirdOct = (shift >> 8) & 0xFF; 
        int fourthOct = shift & 0xFF; 
        String netmask = firstOct + "." + secondOct + "." + thirdOct + "." + fourthOct; 
        
Network Address: bitwise AND operation --> IP Address (long) & netmask (long) 

Broadcast address: bitwise OR operation --> IP Address (long) | inverted netmask (long)

## Instructions
Clone the repository in your local terminal

    $ git clone git@github.com:ajzno26/subnetCalculator.git
    
cd into the subnetCalculator directory

    $ cd subnetCalculator

Makefile to build the application

    $ make 

Provide a CIDR notation subnet when running the java application in the terminal. 

For example, 

    $ java subnet.Calculator 5.6.7.8/22 

Sample output for the previous input: 

    IP: 5.6.7.8
    Netmask: 255.255.252.0
    Number Usable IPs: 1022
    Network Address: 5.6.4.0
    First Usable Address: 5.6.4.1
    Last Usable Address: 5.6.7.254
    Broadcast Address: 5.6.7.255

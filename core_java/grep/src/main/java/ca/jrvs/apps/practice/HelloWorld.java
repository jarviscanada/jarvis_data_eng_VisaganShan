package main.java.ca.jrvs.apps.practice;

class HelloWorld implements RegexExc{

    // Your program begins with a call to main().
    // Prints "Hello, World" to the terminal window.
    public static void main(String[] args) {
        HelloWorld helloWorld = new HelloWorld();
        System.out.println("Hello, World");
        System.out.println(helloWorld.matchJpeg("Andy.jpeg"));  // Expected output: true
        System.out.println(helloWorld.matchJpeg("Andy.jpg"));   // Expected output: true
        System.out.println(helloWorld.matchJpeg("Andy.png"));   // Expected output: false

        // Testing matchIp method
        System.out.println("Testing matchIp method:");
        System.out.println(helloWorld.matchIp("192.168.0.1"));      // Expected output: true
        System.out.println(helloWorld.matchIp("999.999.999.999"));  // Expected output: true
        System.out.println(helloWorld.matchIp("256.256.256.256"));  // Expected output: true
        System.out.println(helloWorld.matchIp("1000.0.0.1"));       // Expected output: false
        System.out.println(helloWorld.matchIp("192.168.01.01"));    // Expected output: true
        System.out.println(helloWorld.matchIp("192.168.0"));        // Expected output: false
        System.out.println(helloWorld.matchIp("..."));              // Expected output: false
        System.out.println(helloWorld.matchIp("1234.123.123.123")); // Expected output: false

        // Testing isEmptyLine method
        System.out.println(helloWorld.isEmptyLine(""));  // Expected output: True
        System.out.println(helloWorld.isEmptyLine(" ")); // Expected output: False
    }

    @Override
    public boolean matchJpeg(String filename) {
        return filename.matches(".*\\.(jpeg|jpeg)");
    }

    @Override
    public boolean matchIp(String ip) {
        return ip.matches("^([0-9]{1,3}\\.){3}[0-9]{1,3}$");
    }

    @Override
    public boolean isEmptyLine(String line) {
        return line.matches("^$");
    }
}
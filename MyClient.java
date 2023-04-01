import java.io.*;
import java.net.*;

public class MyClient {
    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost", 50000);
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            BufferedReader dis = new BufferedReader(new InputStreamReader(s.getInputStream()));

            dout.write(("HELO\n").getBytes());
            String str = (String) dis.readLine();
            System.out.println("Server= " + str);

            String username = System.getProperty("user.name");
            dout.write(("AUTH" + username + "\n").getBytes());
            String str2 = (String) dis.readLine();
            System.out.println("Server= " + str2);

            dout.write(("REDY\n").getBytes());
            String str3 = (String) dis.readLine();

            //while(!str3.equals("NONE")) {
                System.out.println("Server= " + str3);
                String[] splitStr = str3.split(" ");
                dout.write(("GETS Capable " + splitStr[4] + " " + splitStr[5] + " " + splitStr[6] +"\n").getBytes());
                String str4=(String)dis.readLine();
                System.out.println("message= "+str4);
                String[] splitStr2 = str4.split(" ");

                int numberOfServers = Integer.parseInt(splitStr2[1]);
	    
	            String str5;
                String[] splitStr3;
                int[] serverSize = new int[numberOfServers];
                for(int i = 0; i < numberOfServers; i++) {
	   	            dout.write(("OK\n").getBytes());
	    	        str5=(String)dis.readLine();
                    splitStr3 = str5.split(" ");
                    serverSize[i] = Integer.parseInt(splitStr3[4]);
                    	//System.out.println("message= "+str5);
	    	        System.out.println("message= "+serverSize[i]);
	            }
                int largestServer = serverSize[0];
                int numberOfLargeServers = 0;
                for(int i = 1; i < serverSize.length; i++) {
                    if(serverSize[i] > serverSize[i-1]) {
                        largestServer = serverSize[i];
                        numberOfLargeServers = 1;
                    } else {
                        numberOfLargeServers++;
                    }
                }
                System.out.println("message= "+ numberOfLargeServers);
          //  }
            dout.flush();
            dout.close();
            s.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

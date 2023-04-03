import java.io.*;
import java.net.*;

import javax.xml.crypto.Data;

public class MyClient {
    public static void main(String[] args) {
        int[] serverID = null;
        String[] serverNames = null;
        boolean serverInformation = false;
        try {
            Socket s = new Socket("localhost", 50000);
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            BufferedReader dis = new BufferedReader(new InputStreamReader(s.getInputStream()));

            dout.write(("HELO\n").getBytes());
            String str = (String) dis.readLine();

            String username = System.getProperty("user.name");
            dout.write(("AUTH " + username + "\n").getBytes());
            String lastMessage = (String) dis.readLine();

            int LRRIndex = 0;
            int numberOfLargeServers = 1;
            ServerDetails[] servers;

            dout.write(("REDY\n").getBytes());
            lastMessage = (String) dis.readLine();
            String[] lastMessageSplit = lastMessage.split(" ");
            if (!serverInformation) {
                dout.write(("GETS Capable " + lastMessageSplit[4] + " " + lastMessageSplit[5] + " "
                        + lastMessageSplit[6] + "\n").getBytes());
                String data = (String) dis.readLine();
                String[] dataSplit = data.split(" ");
                int totalServers = Integer.parseInt(dataSplit[1]);

                dout.write(("OK\n").getBytes());

                String serverRecord;
                String[] splitServerRecord;
                servers = new ServerDetails[totalServers];
                ServerDetails largestServer = null;
                for (int i = 0; i < totalServers; i++) {
                    serverRecord = (String) dis.readLine();
                    splitServerRecord = serverRecord.split(" ");
                    servers[i] = new ServerDetails(splitServerRecord[0], Integer.parseInt(splitServerRecord[1]),
                            Integer.parseInt(splitServerRecord[4]));
                    if (i >= 1) {
                        if (servers[i].serverCores > servers[i - 1].serverCores) {
                            largestServer = servers[i];
                            numberOfLargeServers = 1;
                        } else if (servers[i].serverName.equals(largestServer.serverName)
                                && servers[i].serverCores <= servers[i - 1].serverCores) {
                            numberOfLargeServers++;
                        }
                    } else {
                        largestServer = servers[0];
                        numberOfLargeServers = 1;
                    }
                }
                int index = 0;
                serverNames = new String[numberOfLargeServers];
                serverID = new int[numberOfLargeServers];
                for (int i = 0; i < totalServers; i++) {
                    if (index >= 1) {
                        if (servers[i].serverCores == largestServer.serverCores
                                && servers[i].serverName.equals(serverNames[index - 1])) {
                            serverID[index] = servers[i].serverID;
                            serverNames[index] = servers[i].serverName;
                            index++;
                        }
                    } else if (servers[i].serverCores == largestServer.serverCores) {
                        serverID[index] = servers[i].serverID;
                        serverNames[index] = servers[i].serverName;
                        index++;
                    }
                }
                serverInformation = true;
            }
            dout.write(("OK\n").getBytes());
            String newMessage = (String) dis.readLine();
            while (!lastMessage.equals("NONE")) {
                if (lastMessageSplit[0].equals("JOBN")) {
                    dout.write(("SCHD " + lastMessageSplit[2] + " " + serverNames[LRRIndex] + " " + serverID[LRRIndex]
                            + "\n").getBytes());
                    newMessage = (String) dis.readLine();

                    dout.write(("REDY\n").getBytes());
                    lastMessage = (String) dis.readLine();
                    lastMessageSplit = lastMessage.split(" ");

                    LRRIndex++;
                    if (LRRIndex >= numberOfLargeServers) {
                        LRRIndex = 0;
                    }
                }
                if (lastMessageSplit[0].equals("JCPL")) {
                    dout.write(("REDY\n").getBytes());
                    lastMessage = (String) dis.readLine();
                    lastMessageSplit = lastMessage.split(" ");
                }

            }

            dout.write(("QUIT\n").getBytes());
            lastMessage = (String) dis.readLine();
            dout.flush();
            dout.close();
            s.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

class ServerDetails {
    public String serverName;
    public int serverID;
    public int serverCores;

    public ServerDetails(String serverName, int serverID, int serverCores) {
        this.serverName = serverName;
        this.serverID = serverID;
        this.serverCores = serverCores;
    }
}

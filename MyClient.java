import java.io.*;  
import java.net.*;  
public class MyClient {  
public static void main(String[] args) {  
try{      
Socket s=new Socket("localhost",50000);  
DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
BufferedReader dis=new BufferedReader(new InputStreamReader(s.getInputStream()));  
dout.write(("HELO\n").getBytes());
String  str=(String)dis.readLine();  
System.out.println("message= "+str);
dout.write(("AUTH test\n").getBytes());
String  str2=(String)dis.readLine();
System.out.println("message= "+str2);
dout.write(("REDY\n").getBytes());
String str3=(String)dis.readLine();
System.out.println("message= "+str3);
dout.write(("GETS Capable 2 300 500\n").getBytes());
String str4=(String)dis.readLine();
System.out.println("message= "+str4);
dout.write(("OK\n").getBytes());
String str5=(String)dis.readLine();
System.out.println("message= "+str5);
dout.write(("OK\n").getBytes());
String str6=(String)dis.readLine();
System.out.println("message= "+str6);
dout.write(("OK\n").getBytes());
String str7=(String)dis.readLine();
System.out.println("message= "+str7);
dout.write(("SCHD 0 small 0\n").getBytes());
String str8=(String)dis.readLine();
System.out.println("message= "+str8);
dout.write(("QUIT\n").getBytes());
String str9=(String)dis.readLine();
System.out.println("message= "+str9);
dout.flush();  
dout.close(); 
s.close();  
}catch(Exception e){System.out.println(e);}  
}  
}  

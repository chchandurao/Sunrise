package com.selenium.keywordfw.standard;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class IPAddressMacAddress {
	public static boolean IPAddressMacAddress(){	
		String Ipresult="fail";
		String MAcresult="fail";
		InetAddress ip;
		try {	 
			ip = InetAddress.getLocalHost();
			System.out.println("Current IP address : " + ip.getHostAddress());
			if(ip.getHostAddress().equals("192.168.1.54")){
				System.out.println("IP Address Match");	
				Ipresult="pass";
			}
	 
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);	 
			byte[] mac = network.getHardwareAddress();	 
			System.out.print("Current MAC address : ");	 
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}			
			System.out.println(sb.toString());
			if(sb.toString().equals("6C-3B-E5-23-48-40")){
				
				System.out.println("Mac Address Match");
				MAcresult = "pass";
			}	 
		} catch (UnknownHostException e) {	 
			e.printStackTrace();	 
		} catch (SocketException e){	 
			e.printStackTrace();	 
		}
		if(Ipresult.equals("pass")&& (MAcresult.equals("pass"))){return true;}else{return false;}
	}
}

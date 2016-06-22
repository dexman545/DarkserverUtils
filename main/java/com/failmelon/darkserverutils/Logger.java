package com.failmelon.darkserverutils;

// Really basic logging class
public class Logger 
{
	public static void WriteLog(String str)
	{
		System.out.println(new StringBuilder().append("[DSU] ").append(str).toString());
	}
}

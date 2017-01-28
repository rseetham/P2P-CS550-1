package hello;

import java.rmi.Remote;

public class FileDetails implements Remote{
	String fileName;
	long size;
}


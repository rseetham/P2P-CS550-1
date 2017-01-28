package hello;

import java.util.ArrayList;
import java.util.Iterator;

public class Peer {
	
	int peerId;
	String ip;
	ArrayList<String> files;
	
	public Peer (int peerId, String ip){
		this.peerId = peerId;
		this.ip = ip;
		this.files = new ArrayList();
	}
	
	int getPeerId (){
		return peerId;
	}
	
	String getIp () {
		return ip;
	}
	
	void addFile (String file) {
		Iterator<String> it = files.iterator();
		while(it.hasNext()){
			if (it.next().equals(file))
				return;
		}
		files.add(file);
	}
	
	
	Boolean removeFile (String file) {
		Iterator<String> it = files.iterator();
		while(it.hasNext()){
			if (it.next().equals(file)){
				files.remove(file);
				return true;
			}
		}
		return false;
	}

}

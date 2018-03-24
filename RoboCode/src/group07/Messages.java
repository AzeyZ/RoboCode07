package group07;

import robocode.MessageEvent;
// https://stackoverflow.com/questions/3429921/what-does-serializable-mean
public class Messages {
	private String msg;

	public Messages(MessageEvent event) {
		msg = event.getMessage();
	}
}

package me.desht.dhutils.responsehandler;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

	private final Map<String, ExpectBase> exp = new HashMap<String, ExpectBase>();

	public void expect(String playerName, ExpectBase data) {
		expect(playerName, data, null);
	}

	private void expect(String playerName, ExpectBase data, String expectee) {
		data.setPlayerName(playerName);
		data.setResp(this);
		if (expectee != null) {
			exp.put(genKey(expectee, data.getClass()), data);
		} else {
			exp.put(genKey(playerName, data.getClass()), data);
		}
	}

	private String genKey(String playerName, Class<? extends ExpectBase> c) {
		return playerName + ":" + c.getName();
	}

	public boolean isExpecting(String playerName, Class<? extends ExpectBase> action) {
		return exp.containsKey(genKey(playerName, action));
	}

	public void handleAction(String playerName, Class<? extends ExpectBase> action) {
		ExpectBase e = exp.get(genKey(playerName, action));
		cancelAction(playerName, action);
		e.doResponse(playerName);
	}

	public void cancelAction(String playerName, Class<? extends ExpectBase> action) {
		exp.remove(genKey(playerName, action));
	}

	public <T extends ExpectBase> T getAction(String playerName, Class<T> action) {
		return action.cast(exp.get(genKey(playerName, action)));
	}

}

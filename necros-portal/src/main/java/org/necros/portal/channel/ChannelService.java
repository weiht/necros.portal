package org.necros.portal.channel;

import java.io.IOException;
import java.util.List;

public interface ChannelService {
	List<Channel> channelsOwnedBy(String ownerId);
	Channel findById(String id);
	void save(Channel channel);
	void saveTemplate(Channel channel);
	void remove(String id);
	String exportAll() throws IOException;
	void importAll(String fn) throws IOException;
}

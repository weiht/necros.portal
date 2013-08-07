package org.necros.portal.channel;

import java.io.IOException;
import java.util.List;

import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;

public interface ChannelService {
	List<Channel> channelsOwnedBy(String ownerId);
	Channel findById(String id);
	void save(Channel channel);
	void saveTemplate(Channel channel);
	List<Channel> all();
	int countAll();
	PageQueryResult<Channel> pageAll(Pager pager);
	List<Channel> filter(String filterText);
	int countFiltered(String filterText);
	PageQueryResult<Channel> pageFiltered(Pager pager, String filterText);
	void remove(String id);
	String exportAll() throws IOException;
	void importAll(String fn) throws IOException;
}

package org.necros.portal.channel.h4;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXB;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;
import org.necros.portal.channel.Channel;
import org.necros.portal.channel.ChannelService;
import org.necros.portal.channel.xsd.ChannelType;
import org.necros.portal.channel.xsd.ChannelsType;
import org.necros.portal.channel.xsd.ObjectFactory;
import org.necros.portal.util.FileUtils;
import org.necros.portal.util.SessionFactoryHelper;
import org.necros.portal.util.ZipExportCallback;
import org.necros.portal.util.ZipExporter;
import org.necros.portal.util.ZipImportCallback;
import org.necros.portal.util.ZipImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class ChannelServiceH4 implements ChannelService {
	private static final String CHANNELS_XML_NAME = "channels.xml";
	private static final Logger logger = LoggerFactory.getLogger(ChannelServiceH4.class);
	
	private ZipExporter zipExporter;
	private ZipImporter zipImporter;
	private String channelFileExtension;
	
	private SessionFactoryHelper sessionFactoryHelper;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Channel> channelsOwnedBy(String ownerId) {
		if (logger.isDebugEnabled()) {
			logger.debug("Finding channels owned by: [" + ownerId + "]");
		}
		Criteria c = createCriteria();
		if (StringUtils.hasText(ownerId)) {
			c.add(Restrictions.eq("ownerId", ownerId));
		} else {
			c.add(Restrictions.eqOrIsNull("ownerId", ""));
		}
		return c.list();
	}

	private Criteria createCriteria() {
		return sessionFactoryHelper.createCriteria(Channel.class);
	}

	@Override
	public Channel findById(String id) {
		return (Channel) sessionFactoryHelper.getSession().get(Channel.class, id);
	}

	@Override
	public void save(Channel ch) {
		logger.trace("Saving channel: {}", ch);
		Channel channel = findById(ch.getId());
		if (channel == null) {
			sessionFactoryHelper.getSession().save(ch);
		} else {
			sessionFactoryHelper.getSession().update(ch);
		}
	}

	@Override
	public void saveTemplate(Channel ch) {
		logger.trace("Saving template for channel: {}", ch);
		Channel channel = findById(ch.getId());
		channel.setTemplate(ch.getTemplate());
		sessionFactoryHelper.getSession().update(channel);
	}

	@Override
	public void remove(String id) {
		logger.debug("Removing channel: {}", id);
		Channel ch = findById(id);
		if (ch != null)
			sessionFactoryHelper.getSession().delete(ch);
	}
	
	@Override
	public String exportAll() throws IOException {
		File f = FileUtils.createTempFile();
		String fn = zipExporter.exportZip(f.getAbsolutePath(), new ZipExportCallback() {
			@Override
			public void doZip(ZipOutputStream zos) throws IOException {
				List<Channel> channels = all();
				if (channels == null || channels.isEmpty()) throw new IOException("未找到任何页面。");
				zipChannelXml(zos, channels);
				for (Channel ch: channels) {
					zipChannel(zos, ch);
				}
			}
		});
		return fn;
	}

	private void zipChannel(ZipOutputStream zos, Channel ch) throws IOException {
		ZipEntry ze = new ZipEntry(ch.getId() + channelFileExtension);
		zos.putNextEntry(ze);
		String t = ch.getTemplate();
		if (t == null) t = "";
		zos.write(t.getBytes());
	}

	private void zipChannelXml(ZipOutputStream zos,
			List<Channel> channels) throws IOException {
		ObjectFactory of = new ObjectFactory();
		ChannelsType ct = of.createChannelsType();
		List<ChannelType> cts = ct.getChannel();
		for (Channel ch: channels) {
			ChannelType c = of.createChannelType();
			c.setId(ch.getId());
			c.setDisplayName(ch.getDisplayName());
			cts.add(c);
		}
		ZipEntry ze = new ZipEntry(CHANNELS_XML_NAME);
		zos.putNextEntry(ze);
		JAXB.marshal(of.createChannels(ct), zos);
	}

	@Override
	public void importAll(String fn) throws IOException {
		logger.debug("fn:" + fn);
		zipImporter.importZip(fn, new ZipImportCallback() {
			@Override
			public boolean doUnzip(String name, ZipInputStream zis) throws IOException {
				BufferedReader r = new BufferedReader(new InputStreamReader(zis));
				if (CHANNELS_XML_NAME.equals(name)) {
					unzipChannelsXml(r);
				} else {
					if (name.endsWith(channelFileExtension)) {
						name = name.substring(0, name.length() - channelFileExtension.length());
					}
					logger.debug(name);
					StringBuilder buff = readToEnd(r);
					Channel ch = findById(name);
					if (ch == null) {
						ch = new Channel();
						ch.setId(name);
						ch.setDisplayName(name);
					}
					ch.setTemplate(buff.toString());
					save(ch);
				}
				return true;
			}
		});
	}

	private void unzipChannelsXml(BufferedReader r) throws IOException {
		StringBuilder buff = readToEnd(r);
		StringReader sr = new StringReader(buff.toString());
		ChannelsType cst = JAXB.unmarshal(sr, ChannelsType.class);
		List<ChannelType> chtypes = cst.getChannel();
		for (ChannelType t: chtypes) {
			String id = t.getId();
			if (StringUtils.hasText(id)) {
				Channel ch = findById(id);
				String n = t.getDisplayName();
				if (ch == null) {
					ch = new Channel();
					ch.setId(id);
					ch.setDisplayName(StringUtils.hasText(n) ? n : id);
					save(ch);
				} else {
					if (StringUtils.hasText(n)) {
						ch.setDisplayName(n);
						save(ch);
					}
				}
			}
		}
	}

	private StringBuilder readToEnd(BufferedReader r)
			throws IOException {
		String l;
		StringBuilder buff = new StringBuilder();
		while ((l = r.readLine()) != null) {
			if (buff.length() > 0) buff.append("\n");
			buff.append(l);
		}
		return buff;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Channel> all() {
		return createCriteria()
				.list();
	}

	@Override
	public int countAll() {
		return sessionFactoryHelper.count(createCriteria());
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageQueryResult<Channel> pageAll(Pager pager) {
		pager.setRecordCount(countAll());
		return sessionFactoryHelper.pageResult(createCriteria(), pager);
	}
	
	private Criteria filterCriteria(String filterText) {
		return createCriteria().add(Restrictions.or(
				Restrictions.like("id", filterText, MatchMode.ANYWHERE),
				Restrictions.like("displayName", filterText, MatchMode.ANYWHERE)
				));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Channel> filter(String filterText) {
		return filterCriteria(filterText).list();
	}

	@Override
	public int countFiltered(String filterText) {
		return sessionFactoryHelper.count(filterCriteria(filterText));
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageQueryResult<Channel> pageFiltered(Pager pager, String filterText) {
		pager.setRecordCount(countFiltered(filterText));
		return sessionFactoryHelper.pageResult(filterCriteria(filterText), pager);
	}

	public String getChannelFileExtension() {
		return channelFileExtension;
	}

	public void setChannelFileExtension(String channelFileExtension) {
		this.channelFileExtension = channelFileExtension;
	}

	public ZipExporter getZipExporter() {
		return zipExporter;
	}

	public void setZipExporter(ZipExporter zipExporter) {
		this.zipExporter = zipExporter;
	}

	public ZipImporter getZipImporter() {
		return zipImporter;
	}

	public void setZipImporter(ZipImporter zipImporter) {
		this.zipImporter = zipImporter;
	}

	public void setSessionFactoryHelper(SessionFactoryHelper sessionFactoryHelper) {
		this.sessionFactoryHelper = sessionFactoryHelper;
	}
}

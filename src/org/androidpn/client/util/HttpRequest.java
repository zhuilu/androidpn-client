/**
 * Date:2015年6月26日上午10:31:01
 * Copyright (c) 2015, daishulin@163.com All Rights Reserved.
 *
 */
package org.androidpn.client.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.androidpn.Request.YBRDataRequestHandler;
import org.androidpn.Request.YBRRequest;
import org.androidpn.client.PersistentConnectionListener;
import org.androidpn.client.XmppManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard.Row;
import android.os.Handler;
import android.util.Log;

/**
 * XmppConnection 工具类
 * 
 * @author 肖赛SoAi
 * 
 */
public class HttpRequest {
	private int SERVER_PORT = 5222;
	private String SERVER_HOST = "127.0.0.1";
	private XMPPConnection connection = null;
	private String SERVER_NAME = "androidpn";
	private static HttpRequest xmppConnection = new HttpRequest();
	private ConnectionListener connectionListener;

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	synchronized public static HttpRequest getInstance() {
		return xmppConnection;
	}

	/**
	 * 创建连接
	 */
	public XMPPConnection getConnection() {
		if (connection == null) {
			openConnection();
		}
		return connection;
	}

	/**
	 * 打开连接
	 */
	public boolean openConnection() {
		connection = SysApplicationImpl.getInstance().getXmppManager()
				.getConnection();
		// try {
		// if (null == connection || !connection.isAuthenticated()) {
		// XMPPConnection.DEBUG_ENABLED = true;// 开启DEBUG模式
		// // 配置连接
		// ConnectionConfiguration config = new ConnectionConfiguration(
		// SERVER_HOST, SERVER_PORT, SERVER_NAME);
		// config.setReconnectionAllowed(true);
		// config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
		// config.setSendPresence(true); // 状态设为离线，目的为了取离线消息
		// config.setSASLAuthenticationEnabled(false); // 是否启用安全验证
		// config.setTruststorePath("/system/etc/security/cacerts.bks");
		// config.setTruststorePassword("changeit");
		// config.setTruststoreType("bks");
		// connection = new XMPPConnection(config);
		// connection.connect();// 连接到服务器
		// // 配置各种Provider，如果不配置，则会无法解析数据
		// configureConnection(ProviderManager.getInstance());
		// return true;
		// }
		// } catch (XMPPException xe) {
		// xe.printStackTrace();
		// connection = null;
		// }
		return true;
	}

	/**
	 * 关闭连接
	 */
	public void closeConnection() {
		if (connection != null) {
			// 移除連接監聽
			// connection.removeConnectionListener(connectionListener);
			if (connection.isConnected())
				connection.disconnect();
			connection = null;
		}
		Log.i("XmppConnection", "關閉連接");
	}

	/**
	 * 登录
	 * 
	 * @param account
	 *            登录帐号
	 * @param password
	 *            登录密码
	 * @return
	 */
	public boolean login(String account, String password) {
		try {
			if (getConnection() == null)
				return false;
			getConnection().login(account, password);
			// 更改在綫狀態
			Presence presence = new Presence(Presence.Type.available);
			getConnection().sendPacket(presence);
			// 添加連接監聽
			connectionListener = new PersistentConnectionListener(connection);
			getConnection().addConnectionListener(connectionListener);
			return true;
		} catch (XMPPException xe) {
			xe.printStackTrace();
		}
		return false;
	}

	// public void startReconnectionThread() {
	// synchronized (reconnection) {
	// if (!reconnection.isAlive()) {
	// reconnection.setName("Xmpp Reconnection Thread");
	// reconnection.start();
	// }
	// }
	// }

	/**
	 * 注册
	 * 
	 * @param account
	 *            注册帐号
	 * @param password
	 *            注册密码
	 * @return 1、注册成功 0、服务器没有返回结果2、这个账号已经存在3、注册失败
	 */
	public String regist(String account, String password) {
		if (getConnection() == null)
			return "0";
		Registration reg = new Registration();
		reg.setType(IQ.Type.SET);
		reg.setTo(getConnection().getServiceName());
		// 注意这里createAccount注册时，参数是UserName，不是jid，是"@"前面的部分。
		reg.setUsername(account);
		reg.setPassword(password);
		// 这边addAttribute不能为空，否则出错。所以做个标志是android手机创建的吧！！！！！
		reg.addAttribute("android", "geolo_createUser_android");
		PacketFilter filter = new AndFilter(new PacketIDFilter(
				reg.getPacketID()), new PacketTypeFilter(IQ.class));
		PacketCollector collector = getConnection().createPacketCollector(
				filter);
		getConnection().sendPacket(reg);
		IQ result = (IQ) collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		// Stop queuing results停止请求results（是否成功的结果）
		collector.cancel();
		if (result == null) {
			Log.e("regist", "No response from server.");
			return "0";
		} else if (result.getType() == IQ.Type.RESULT) {
			Log.v("regist", "regist success.");
			return "1";
		} else { // if (result.getType() == IQ.Type.ERROR)
			if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
				Log.e("regist", "IQ.Type.ERROR: "
						+ result.getError().toString());
				return "2";
			} else {
				Log.e("regist", "IQ.Type.ERROR: "
						+ result.getError().toString());
				return "3";
			}
		}
	}

	/**
	 * 更改用户状态
	 */
	public void setPresence(int code) {
		XMPPConnection con = getConnection();
		if (con == null)
			return;
		Presence presence;
		switch (code) {
		case 0:
			presence = new Presence(Presence.Type.available);
			con.sendPacket(presence);
			Log.v("state", "设置在线");
			break;
		case 1:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.chat);
			con.sendPacket(presence);
			Log.v("state", "设置Q我吧");
			break;
		case 2:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.dnd);
			con.sendPacket(presence);
			Log.v("state", "设置忙碌");
			break;
		case 3:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.away);
			con.sendPacket(presence);
			Log.v("state", "设置离开");
			break;
		case 4:
			Roster roster = con.getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			for (RosterEntry entry : entries) {
				presence = new Presence(Presence.Type.unavailable);
				presence.setPacketID(Packet.ID_NOT_AVAILABLE);
				presence.setFrom(con.getUser());
				presence.setTo(entry.getUser());
				con.sendPacket(presence);
				Log.v("state", presence.toXML());
			}
			// 向同一用户的其他客户端发送隐身状态
			presence = new Presence(Presence.Type.unavailable);
			presence.setPacketID(Packet.ID_NOT_AVAILABLE);
			presence.setFrom(con.getUser());
			presence.setTo(StringUtils.parseBareAddress(con.getUser()));
			con.sendPacket(presence);
			Log.v("state", "设置隐身");
			break;
		case 5:
			presence = new Presence(Presence.Type.unavailable);
			con.sendPacket(presence);
			Log.v("state", "设置离线");
			break;
		default:
			break;
		}
	}

	/**
	 * 获取所有组
	 * 
	 * @return 所有组集合
	 */
	public List<RosterGroup> getGroups() {
		if (getConnection() == null)
			return null;
		List<RosterGroup> grouplist = new ArrayList<RosterGroup>();
		Collection<RosterGroup> rosterGroup = getConnection().getRoster()
				.getGroups();
		Iterator<RosterGroup> i = rosterGroup.iterator();
		while (i.hasNext()) {
			grouplist.add(i.next());
		}
		return grouplist;
	}

	/**
	 * 获取某个组里面的所有好友
	 * 
	 * @param roster
	 * @param groupName
	 *            组名
	 * @return
	 */
	public List<RosterEntry> getEntriesByGroup(String groupName) {
		if (getConnection() == null)
			return null;
		List<RosterEntry> Entrieslist = new ArrayList<RosterEntry>();
		RosterGroup rosterGroup = getConnection().getRoster().getGroup(
				groupName);
		Collection<RosterEntry> rosterEntry = rosterGroup.getEntries();
		Iterator<RosterEntry> i = rosterEntry.iterator();
		while (i.hasNext()) {
			Entrieslist.add(i.next());
		}
		return Entrieslist;
	}

	/**
	 * 获取所有好友信息
	 * 
	 * @return
	 */
	public List<RosterEntry> getAllEntries() {
		if (getConnection() == null)
			return null;
		List<RosterEntry> Entrieslist = new ArrayList<RosterEntry>();
		Collection<RosterEntry> rosterEntry = getConnection().getRoster()
				.getEntries();
		Iterator<RosterEntry> i = rosterEntry.iterator();
		while (i.hasNext()) {
			Entrieslist.add(i.next());
		}
		return Entrieslist;
	}

	/**
	 * 添加一个分组
	 * 
	 * @param groupName
	 * @return
	 */
	public boolean addGroup(String groupName) {
		if (getConnection() == null)
			return false;
		try {
			getConnection().getRoster().createGroup(groupName);
			Log.v("addGroup", groupName + "創建成功");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除分组
	 * 
	 * @param groupName
	 * @return
	 */
	public boolean removeGroup(String groupName) {
		return true;
	}

	/**
	 * 添加好友 无分组
	 * 
	 * @param userName
	 * @param name
	 * @return
	 */
	public boolean addUser(String userName, String name) {
		if (getConnection() == null)
			return false;
		try {
			getConnection().getRoster().createEntry(userName, name, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 添加好友 有分组
	 * 
	 * @param userName
	 * @param name
	 * @param groupName
	 * @return
	 */
	public boolean addUser(String userName, String name, String groupName) {
		if (getConnection() == null)
			return false;
		try {
			Presence subscription = new Presence(Presence.Type.subscribed);
			subscription.setTo(userName);
			userName += "@" + getConnection().getServiceName();
			getConnection().sendPacket(subscription);
			getConnection().getRoster().createEntry(userName, name,
					new String[] { groupName });
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除好友
	 * 
	 * @param userName
	 * @return
	 */
	public boolean removeUser(String userName) {
		if (getConnection() == null)
			return false;
		try {
			RosterEntry entry = null;
			if (userName.contains("@"))
				entry = getConnection().getRoster().getEntry(userName);
			else
				entry = getConnection().getRoster().getEntry(
						userName + "@" + getConnection().getServiceName());
			if (entry == null)
				entry = getConnection().getRoster().getEntry(userName);
			getConnection().getRoster().removeEntry(entry);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 修改心情
	 * 
	 * @param connection
	 * @param status
	 */
	public void changeStateMessage(String status) {
		if (getConnection() == null)
			return;
		Presence presence = new Presence(Presence.Type.available);
		presence.setStatus(status);
		getConnection().sendPacket(presence);
	}

	/**
	 * 文件转字节
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private byte[] getFileBytes(File file) throws IOException {
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			int bytes = (int) file.length();
			byte[] buffer = new byte[bytes];
			int readBytes = bis.read(buffer);
			if (readBytes != buffer.length) {
				throw new IOException("Entire file not read");
			}
			return buffer;
		} finally {
			if (bis != null) {
				bis.close();
			}
		}
	}

	/**
	 * 删除当前用户
	 * 
	 * @return
	 */
	public boolean deleteAccount() {
		if (getConnection() == null)
			return false;
		try {
			getConnection().getAccountManager().deleteAccount();
			return true;
		} catch (XMPPException e) {
			return false;
		}
	}

	/**
	 * 修改密码
	 * 
	 * @return
	 */
	public boolean changePassword(String pwd) {
		if (getConnection() == null)
			return false;
		try {
			getConnection().getAccountManager().changePassword(pwd);
			return true;
		} catch (XMPPException e) {
			return false;
		}
	}

	/**
	 * 判断OpenFire用户的状态 strUrl : url格式 -
	 * http://my.openfire.com:9090/plugins/presence
	 * /status?jid=user1@SERVER_NAME&type=xml 返回值 : 0 - 用户不存在; 1 - 用户在线; 2 -
	 * 用户离线 说明 ：必须要求 OpenFire加载 presence 插件，同时设置任何人都可以访问
	 */
	public int IsUserOnLine(String user) {
		String url = "http://" + SERVER_HOST + ":9090/plugins/presence/status?"
				+ "jid=" + user + "@" + SERVER_NAME + "&type=xml";
		int shOnLineState = 0; // 不存在
		try {
			URL oUrl = new URL(url);
			URLConnection oConn = oUrl.openConnection();
			if (oConn != null) {
				BufferedReader oIn = new BufferedReader(new InputStreamReader(
						oConn.getInputStream()));
				if (null != oIn) {
					String strFlag = oIn.readLine();
					oIn.close();
					System.out.println("strFlag" + strFlag);
					if (strFlag.indexOf("type=\"unavailable\"") >= 0) {
						shOnLineState = 2;
					}
					if (strFlag.indexOf("type=\"error\"") >= 0) {
						shOnLineState = 0;
					} else if (strFlag.indexOf("priority") >= 0
							|| strFlag.indexOf("id=\"") >= 0) {
						shOnLineState = 1;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return shOnLineState;
	}

	public void configureConnection(ProviderManager pm) {
	}

	public static void RequestToPostMessage(
			YBRDataRequestHandler<Integer> argHandler) {

		// img_url init
		// Create a request
		YBRRequest pRequest = new YBRRequest("user_api", argHandler);
		pRequest.SetParamValue("token", "login_token");
		pRequest.SetParamValue("Android", "platform");

		// Start the request
		pRequest.setM_bSilent(true);
		pRequest.StartRequest();
	}

	public static void RequestPostMessageTo(String userid,
			YBRDataRequestHandler<Integer> argHandler) {

		// img_url init
		// Create a request
		YBRRequest pRequest = new YBRRequest("notification_api", argHandler);
		pRequest.SetParamValue("broadcast", "broadcast");
		pRequest.SetParamValue("username", userid);
		pRequest.SetParamValue("title", "title");
		pRequest.SetParamValue("message", "message");
		pRequest.SetParamValue("uri", "uri");
		// Start the request
		pRequest.setM_bSilent(true);
		pRequest.StartRequest();
	}

	/**
	 * 登录
	 * 
	 * @param handler
	 * @param string
	 * @param string2
	 * @param ybrDataRequestHandler
	 */
	public void loginTo(Handler handler, String photo, String password,
			YBRDataRequestHandler<JSONObject> ybrDataRequestHandler) {
		System.out.println("loginTo");
		SysApplicationImpl.getInstance().getXmppManager()
				.connect(handler, photo, password, ybrDataRequestHandler);
	}
}
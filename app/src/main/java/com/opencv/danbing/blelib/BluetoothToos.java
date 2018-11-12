package com.opencv.danbing.blelib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Creation time: 2018/10/24
 * Editor: $USER_NAME
 * Functional description:
 */
public class BluetoothToos implements IBluetoothToos {
	private static final String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	private List<String> mainlist = new ArrayList();
	private String msg_temporary = "";
	private IBluetoothListener listener;
	private String msg;
	private boolean isfirst = true;
	private boolean iscoon = false;

	public BluetoothToos() {
	}

	public void setIDeviceListListener(IBluetoothListener listener) {
		this.listener = listener;
	}

	public BluetoothSocket getBluetoothSocket(String lanya_address) {
		BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(lanya_address);
		BluetoothSocket socket = null;

		try {
			socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
		} catch (Exception var5) {
			System.out.println(var5.toString() + "错误");
		}

		return socket;
	}

	public boolean connect_bluetooth(BluetoothSocket socket) {
		try {
			socket.connect();
			this.iscoon = true;
			return true;
		} catch (Exception var3) {
			return false;
		}
	}

	public boolean close_bluetooth(BluetoothSocket socket) {
		try {
			this.iscoon = false;
			this.notifyObserversList();
			socket.close();
			return true;
		} catch (Exception var3) {
			return false;
		}
	}

	public void getSockeMsg(BluetoothSocket _socket, IBluetoothListener listener) {
		this.setIDeviceListListener(listener);
		this.mainlist.clear();
		this.msg_temporary = "";
		this.isfirst = true;
		byte[] buffer = new byte[64];

		while (this.iscoon) {
			boolean var4 = false;
			try {
				if (_socket == null) {
//					Common.setUserParam(VApplication.getInstances(), Common.COMMON_USER_WIFI_STATUS, "无法配对");
					break;
				}

				int length = _socket.getInputStream().read(buffer);
				if (length > 0) {
					try {
						String recv = this.toHexString(buffer, length);
						this.parseData(recv);
					} catch (Exception var6) {
						var6.printStackTrace();
					}
				}
			} catch (Exception var7) {
				var7.printStackTrace();
				close_bluetooth(_socket);
//				Common.setUserParam(VApplication.getInstances(), Common.COMMON_USER_WIFI_STATUS, "无法配对");
			}
		}

	}

	private void notifyObservers() {
		this.listener.getMsg(this.msg);
	}

	private void notifyObserversList() {
		this.listener.getMsgList(this.mainlist);
	}

	private String toHexString(byte[] arg, int length) {
		String result = "";
		if (arg == null) {
			return "";
		} else {
			result = new String(arg);
//			for (int i = 0; i < length; ++i) {
//				byte bytestr = arg[i];
//				if (bytestr >= 65 && bytestr <= 90 || bytestr >= 48 && bytestr <= 57 || bytestr == 58 || bytestr == 35 || bytestr == 36 || bytestr == 62) {
//					result = result + (char) bytestr;
//				}
//			}
			return result;
		}
	}

	private void parseData(String msgstr) {
		msgstr.trim();
		if (!msgstr.equals("")) {
			String[] str = msgstr.split(">");

			for (int i = 0; i < str.length; ++i) {
				String macnamestr = str[i];
				if (!macnamestr.equals("")) {
					if (this.isfirst) {
						this.isfirst = false;
						if (macnamestr.indexOf("#") != -1) {
							macnamestr = macnamestr.substring(macnamestr.indexOf("#") + 1, macnamestr.length());
						} else if (macnamestr.indexOf("$") != -1) {
							macnamestr = macnamestr.substring(macnamestr.indexOf("$") + 1, macnamestr.length());
						}
					}

					String newmsgtemporary;
					String tag;
					if (macnamestr.length() == 18 || macnamestr.length() == 27) {
						newmsgtemporary = macnamestr.substring(0, 1);
						if (newmsgtemporary.equals("#") || newmsgtemporary.equals("$")) {
							tag = macnamestr.substring(1, macnamestr.length());
							this.adddata(tag, newmsgtemporary);
						}

						this.msg_temporary = "";
					} else {
						newmsgtemporary = "";
						if (this.msg_temporary.equals("")) {
							this.msg_temporary = macnamestr;
						} else {
							if (this.msg_temporary.indexOf("#") == -1 && this.msg_temporary.indexOf("$") == -1) {
								newmsgtemporary = macnamestr + this.msg_temporary;
							} else {
								newmsgtemporary = this.msg_temporary + macnamestr;
							}

							newmsgtemporary.trim();
							if (newmsgtemporary.length() == 18 || newmsgtemporary.length() == 27) {
								tag = newmsgtemporary.substring(0, 1);
								String mac = newmsgtemporary.substring(1, newmsgtemporary.length());
								this.adddata(mac, tag);
								this.msg_temporary = "";
							} else {
								this.msg_temporary = newmsgtemporary;
							}
						}
					}
				}
			}
		}

	}

	private void adddata(String mac, String tag) {
		if (tag.equals("$")) {
			this.mainlist.add(mac);
			this.msg = mac;
			this.notifyObservers();
		}

	}
}

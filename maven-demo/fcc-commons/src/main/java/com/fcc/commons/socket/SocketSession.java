package com.fcc.commons.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fcc.commons.socket.io.IoHandler;
import com.fcc.commons.socket.io.IoSessionAbstract;
import com.fcc.commons.socket.io.codec.AbstractProtocolDecoderOutput;
import com.fcc.commons.socket.io.codec.AbstractProtocolEncoderOutput;
import com.fcc.commons.socket.io.codec.IoBuffer;
import com.fcc.commons.socket.io.codec.ProtocolDecoder;
import com.fcc.commons.socket.io.codec.ProtocolEncoder;

public class SocketSession extends IoSessionAbstract {
	
	private static Logger logger = Logger.getLogger(SocketSession.class.getName());
	
	private SocketSession session;
	/** socket */
	private Socket socket;
	/** 事件处理 */
	private IoHandler handler;
	/** 协议编码 */
	private ProtocolEncoder encoder;
	/** 协议解码 */
	private ProtocolDecoder decoder;

	private DataInputStream dis;
	private DataOutputStream dos;
	
	private String clientIp;
	private int clientPort;
	private String socketId;
	
	private SocketAddress remoteAddress;
	private SocketAddress localAddress;
	/** 读取数据的缓冲字节 */
	private ByteBuffer readByteBuffer;
	/** 记录编码数据 */
	private ProtocolEncoderOutputImpl encoderOutput;
	/** 记录解码数据 */
	private ProtocolDecoderOutputImpl decoderOutput;
	/** 读取数据时的通道 */
	private IoBuffer ioBuffer;
	
	private WriteData writeData;
	private ReadData readData;

	private Boolean destory = false;
	
	public SocketSession(Socket socket, IoHandler handler, ProtocolEncoder encoder, ProtocolDecoder decoder) {
		this.handler = handler;
		this.encoder = encoder;
		this.decoder = decoder;
		this.session = this;
		this.socket = socket;
		this.localAddress = socket.getLocalSocketAddress();
		this.remoteAddress = socket.getRemoteSocketAddress();
		init();
	}
	
	public void init() {
		String serverAddress = remoteAddress.toString();
		String[] params = serverAddress.split("/")[1].split(":");
		clientIp = params[0];// 客户端IP
		clientPort = Integer.valueOf(params[1]);// 客户端连接端口
		socketId = clientIp + ":" + clientPort;
		// 通知建立连接
		try {
			handler.sessionCreated(this);
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.WARNING, "connection fail:" + socketId, e);
		}
		try {
			readByteBuffer = ByteBuffer.allocate(getReadBufferSize());
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			
			encoderOutput = new ProtocolEncoderOutputImpl();
			decoderOutput = new ProtocolDecoderOutputImpl();
			ioBuffer = new IoBufferImpl(); 
			
			writeData = new WriteData();
			readData = new ReadData();
			writeData.start();
			readData.start();
			// 通知建立数据流
			handler.sessionOpened(this);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public IoHandler getHandler() {
		return this.handler;
	}

	public SocketAddress getLocalAddress() {
		return localAddress;
	}

	public SocketAddress getRemoteAddress() {
		return remoteAddress;
	}

	public boolean isClosing() {
		if (writeData.flag == true && readData.flag == true) {
			return false;
		}
		return true;
	}

	public Object read() {
		return null;
	}

	public boolean write(Object message) {
		if (readData.flag == false || writeData.flag == false) return false;
		try {
			encoder.encode(this, message, encoderOutput);
			return true;
		} catch (Exception e) {
			logger.log(Level.WARNING, "encode error", e);
			e.printStackTrace();
		}
		return false;
	}
	
	private class ProtocolEncoderOutputImpl extends AbstractProtocolEncoderOutput {
	}
	
	private class ProtocolDecoderOutputImpl extends AbstractProtocolDecoderOutput {
	}
	
	private class IoBufferImpl implements IoBuffer {

		public ByteBuffer getByteBuffer() {
			return readByteBuffer;
		}

		public InputStream getInputStream() {
			return dis;
		}

		public OutputStream getOutputStream() {
			return dos;
		}
		
	}
	
	private synchronized void destorySession() {
		if (destory == true) return;
		logger.info("SocketSession.destorySession() start");
		destory = true;
		readData.flag = false;
		writeData.flag = false;
		Object message = null;
		while ((message = decoderOutput.getMessageQueue().poll()) != null) {
			try {
				// 通知读数据成功
				handler.messageReceived(session, message);
			} catch (Exception e) {
				logger.log(Level.WARNING, "messageReceived error", e);
				e.printStackTrace();
			}
		}
		while ((message = encoderOutput.getMessageQueue().poll()) != null) {
			try {
				handler.messageSentFail(session, message);
			} catch (Exception e) {
				logger.log(Level.WARNING, "messageSentFail error", e);
				e.printStackTrace();
			}
		}
		Thread t = new Thread(new Runnable(){
			public void run() {
				try {
					logger.info("SocketSession.notifySessionClosed() start");
					handler.sessionClosed(session);
					logger.info("SocketSession.notifySessionClosed() end");
				} catch (Exception e) {
					logger.log(Level.WARNING, "sessionClosed error", e);
				}
			}
		});
		t.start();
		logger.info("SocketSession.destorySession() end");
	}
	
	/** 监听写数据 */
	private class WriteData extends Thread {
		/** 休眠时间单位：毫秒 */
		int period = 1000;
		boolean flag = true;
		boolean writeFlag = false;
		public void run() {
			while (flag) {
				Object message = null;
				try {
					message = encoderOutput.getMessageQueue().poll();
					if (message == null) {
						try {
							Thread.sleep(period);
						} catch (Exception e) {
							e.printStackTrace();
						}
						continue;
					}
					writeFlag = false;
					dos.write((byte[]) message);
					writeFlag = true;
				} catch (SocketException e) {
					logger.log(Level.WARNING, "写数据时，断开了Socket，bye!", e);
					e.printStackTrace();
				} catch (EOFException e) {
					logger.log(Level.WARNING, "写数据时，断开了EOF，bye!", e);
					e.printStackTrace();
				} catch (IOException e) {
					logger.log(Level.WARNING, "写数据时，断开连接，bye!", e);
					e.printStackTrace();
				} catch (Exception e) {
					logger.log(Level.WARNING, "写数据时，出现异常，bye!", e);
					e.printStackTrace();
				}
				if (writeFlag == false) {
					// 结束本线程
					destorySession();
				} else {
					// 通知写数据成功
					try {
						handler.messageSent(session, message);
					} catch (Exception e) {
						logger.log(Level.WARNING, "messageSent error", e);
						e.printStackTrace();
					}
				}
			}
		}
	}
	// 监听读数据
	private class ReadData extends Thread {
		/** 休眠时间单位：毫秒 */
		int period = 1000;
		boolean flag = true;
		boolean readFlag = false;
		public void run() {
			while (flag) {
				readFlag = false;
				Object message = null;
				try {
					readByteBuffer.clear();
					// 阻塞式的读取数据
					int count = dis.read(readByteBuffer.array());
					if (count > 0) {// 读取到数据
						byte[] data = new byte[count];
						System.arraycopy(readByteBuffer.array(), 0, data, 0, count);
						try {
							decoder.decode(session, ioBuffer, decoderOutput);
						} catch (RuntimeException e) {
							logger.log(Level.WARNING, "decode error", e);
							e.printStackTrace();
						}
						
						message = decoderOutput.getMessageQueue().poll();
						if (message == null) {
							try {
								Thread.sleep(period);
							} catch (Exception e) {
								e.printStackTrace();
							}
							continue;
						}
						// 通知读数据成功
						handler.messageReceived(session, message);
						readFlag = true;
					} else if (count == -1) {// 已断开连接
						logger.info("读数据时，已经到达流的末尾而没有更多的数据");
					} else {
						logger.info("读数据时，读到空数据");
					}
				} catch (SocketException e) {
					logger.log(Level.WARNING, "读数据时，断开了Socket，bye!", e);
					e.printStackTrace();
				} catch (EOFException e) {
					logger.log(Level.WARNING, "读数据时，断开了EOF，bye!", e);
					e.printStackTrace();
				} catch (IOException e) {
					logger.log(Level.WARNING, "读数据时，断开了，bye!", e);
					e.printStackTrace();
				} catch (Exception e) {
					logger.log(Level.WARNING, "读数据时，出现异常，bye!", e);
					e.printStackTrace();
				}
				if (readFlag == false) {
					// 结束本线程
					destorySession();
				}
			}
		}
	}
	
	public boolean isDestory() {
		return destory;
	}

	public String getSocketId() {
		return socketId;
	}
}

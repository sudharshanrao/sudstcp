package com.cleartcp.stream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cleartcp.domain.message.MessageType;

public abstract class StreamReaderWriter {
	
    private static final Logger logger = LoggerFactory.getLogger(StreamReaderWriter.class);
    protected DataOutputStream  outputStreamToTCPServer;
    protected DataInputStream   inputStreamFromTCPServer;
	private static final byte STX = 0x02;
	private static final byte ETX = 0x03;
    private void writeMessage(byte[] msg, int msgLen, int messageType) throws IOException {
        synchronized (outputStreamToTCPServer) {
        	outputStreamToTCPServer.writeByte(STX);
            outputStreamToTCPServer.writeInt(msgLen);
            outputStreamToTCPServer.writeInt(messageType);
            outputStreamToTCPServer.write(msg, 0, msgLen);
            outputStreamToTCPServer.writeInt(calculateCheckSum(msg, 0, msgLen));
            outputStreamToTCPServer.write(ETX);
            outputStreamToTCPServer.flush();
        }
	}

    public void writeObject(Object obj, MessageType messageType) throws IOException {
		byte[] buf = SerializationUtils.serialize((Serializable) obj);
        writeMessage(buf, buf.length, messageType.getType());
	}
	
	private Object readMessage(int msgLen, DataInputStream inputStreamFromTCPServer) throws IOException, ClassNotFoundException{
		byte[] buf = new byte[msgLen];
		inputStreamFromTCPServer.readFully(buf); 
		int expectedChecksum = calculateCheckSum(buf);
		int checksum = inputStreamFromTCPServer.readInt();
		if(checksum != expectedChecksum){
			throw new IOException("Checksum error: expecting "+expectedChecksum+" but got "+checksum);
		}
		if(inputStreamFromTCPServer.readByte() == ETX){
			return SerializationUtils.deserialize(buf);
		}
		return null;
	}
	
    public Object readObject() throws ClassNotFoundException, IOException {
        synchronized (inputStreamFromTCPServer) {
        	if(inputStreamFromTCPServer.readByte() == STX){
        		logger.trace("Start of message...");
        	}
            int msgLen = inputStreamFromTCPServer.readInt();
            int streamMessageType = inputStreamFromTCPServer.readInt();
            MessageType messageType = MessageType.lookup(streamMessageType);
			switch (messageType) {
			case APPLICATION:
				logger.trace("application message received");
				break;
			case HEARTBEAT:
				logger.trace("heartbeat message received");
				break;
			case IDENTIFICATION:
				logger.trace("Identification message received");
				break;
			default:
				logger.trace("Unknown message. will skip..");
				return null;
			}
            return readMessage(msgLen, inputStreamFromTCPServer);
        }
	}
    
    @SuppressWarnings("incomplete-switch")
    public byte[] readBytes() throws IOException{
    	boolean isHeartbeat = false;
    	if(inputStreamFromTCPServer.readByte() == STX){
    		logger.trace("Start of message...");
    	}
        int msgLen = inputStreamFromTCPServer.readInt();
        int streamMessageType = inputStreamFromTCPServer.readInt();
        MessageType messageType = MessageType.lookup(streamMessageType);
		switch (messageType) {
		case HEARTBEAT:
			isHeartbeat = true;
			break;
		}
    	byte[] buf = new byte[msgLen];
		inputStreamFromTCPServer.readFully(buf); 
		int expectedChecksum = calculateCheckSum(buf);
		int checksum = inputStreamFromTCPServer.readInt();
		if(checksum != expectedChecksum){
			throw new IOException("Checksum error: expecting "+expectedChecksum+" but got "+checksum);
		}
		if(inputStreamFromTCPServer.readByte() == ETX){
			if(isHeartbeat){
				return new byte[0]; // return empty byte[] for heartbeat.
			}
			return buf;
		}
		return null;
    }
    
    public void writeBytes(byte[] buf) throws IOException {
    	writeMessage(buf, buf.length, MessageType.APPLICATION.ordinal());
    }
    
	/**
	 * Calculates the check sum of the given bytes.
	 *
	 * @param bytes
	 *            the array from which a check sum is calculated
	 * @return the check sum
	 */
	private int calculateCheckSum(byte[] bytes) {
		return calculateCheckSum(bytes, 0, bytes.length);
	}

	/**
	 * Calculates the check sum of the given range of bytes.
	 *
	 * @param bytes
	 *            the array from which a check sum is calculated
	 * @param from
	 *            the initial index of the range to be copied, inclusive
	 * @param to
	 *            the final index of the range to be copied, exclusive. (This
	 *            index may lie outside the array.)
	 * @return the check sum
	 */
	private int calculateCheckSum(byte[] bytes, int from, int to) {
		int sum = 0;
		for (int i = from; i < to; i++) {
			sum += bytes[i];
			sum &= 0xFF;
		}
		return sum;
	}
}
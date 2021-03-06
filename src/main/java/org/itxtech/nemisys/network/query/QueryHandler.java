package org.itxtech.nemisys.network.query;

import org.itxtech.nemisys.Server;
import org.itxtech.nemisys.event.server.QueryRegenerateEvent;
import org.itxtech.nemisys.utils.Binary;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class QueryHandler {

    public static final byte HANDSHAKE = 0x09;
    public static final byte STATISTICS = 0x00;

    private Server server;
    private byte[] lastToken;
    private byte[] token;
    private byte[] longData;
    private byte[] shortData;
    private long timeout;

    public QueryHandler() {
        this.server = Server.getInstance();
        String ip = this.server.getIp();
        String addr = (!"".equals(ip)) ? ip : "0.0.0.0";
        int port = this.server.getPort();

        this.regenerateToken();
        this.lastToken = this.token;
        this.regenerateInfo();
    }

    public static String getTokenString(byte[] token, String salt) {
        return getTokenString(new String(token), salt);
    }

    public static String getTokenString(String token, String salt) {
        try {
            return String.valueOf(Binary.readInt(Binary.subBytes(MessageDigest.getInstance("SHA-512").digest((salt + ":" + token).getBytes()), 7, 4)));
        } catch (NoSuchAlgorithmException e) {
            return String.valueOf(new Random().nextInt());
        }
    }

    public void regenerateInfo() {
        QueryRegenerateEvent ev = this.server.getQueryInformation();
        this.longData = ev.getLongQuery();
        this.shortData = ev.getShortQuery();
        this.timeout = System.currentTimeMillis() + ev.getTimeout();
    }

    public void regenerateToken() {
        this.lastToken = this.token;
        byte[] token = new byte[16];
        for (int i = 0; i < 16; i++) {
            token[i] = (byte) new Random().nextInt(255);
        }
        this.token = token;
    }

    public void handle(String address, int port, byte[] packet) {
        int offset = 2;
        byte packetType = packet[offset++];
        int sessionID = Binary.readInt(Binary.subBytes(packet, offset, 4));
        offset += 4;
        byte[] payload = Binary.subBytes(packet, offset);

        switch (packetType) {
            case HANDSHAKE:
                byte[] reply = Binary.appendBytes(
                        HANDSHAKE,
                        Binary.writeInt(sessionID),
                        getTokenString(this.token, address).getBytes(),
                        new byte[]{0x00}
                );

                this.server.getNetwork().sendPacket(address, port, reply);
                break;
            case STATISTICS:
                String token = String.valueOf(Binary.readInt(Binary.subBytes(payload, 0, 4)));
                if (!token.equals(getTokenString(this.token, address)) && !token.equals(getTokenString(this.lastToken, address))) {
                    break;
                }

                if (this.timeout < System.currentTimeMillis()) {
                    this.regenerateInfo();
                }
                reply = Binary.appendBytes(
                        STATISTICS,
                        Binary.writeInt(sessionID),
                        payload.length == 8 ? this.longData : this.shortData
                );

                this.server.getNetwork().sendPacket(address, port, reply);
                break;
        }
    }
}

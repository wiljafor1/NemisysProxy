package org.itxtech.nemisys.network.protocol.mcpe;

public class SubClientLoginPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.SUB_CLIENT_LOGIN_PACKET;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
    }
}

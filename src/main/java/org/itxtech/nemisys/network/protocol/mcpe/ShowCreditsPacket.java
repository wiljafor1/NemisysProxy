package org.itxtech.nemisys.network.protocol.mcpe;

public class ShowCreditsPacket extends DataPacket {

    public static final int STATUS_START_CREDITS = 0;
    public static final int STATUS_END_CREDITS = 1;

    public long eid;
    public int status;

    @Override
    public byte pid() {
        return ProtocolInfo.SHOW_CREDITS_PACKET;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        this.putVarInt(this.status);
    }
}

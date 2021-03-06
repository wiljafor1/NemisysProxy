package org.itxtech.nemisys.network.protocol.mcpe;

public class SetEntityLinkPacket extends DataPacket {

    public static final byte TYPE_REMOVE = 0;
    public static final byte TYPE_RIDE = 1;
    public static final byte TYPE_PASSENGER = 2;

    public long rider;
    public long riding;
    public byte type;
    public byte unknownByte;

    @Override
    public void decode() {
        this.rider = this.getEntityUniqueId();
        this.riding = this.getEntityUniqueId();
        this.type = (byte) this.getByte();
        this.unknownByte = (byte) this.getByte();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(this.rider);
        this.putEntityUniqueId(this.riding);
        this.putByte(this.type);
        this.putByte(this.unknownByte);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.SET_ENTITY_LINK_PACKET;
    }
}

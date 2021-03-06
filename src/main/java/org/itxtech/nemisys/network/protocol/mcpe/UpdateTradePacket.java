package org.itxtech.nemisys.network.protocol.mcpe;

public class UpdateTradePacket extends DataPacket {

    public byte windowId;
    public byte windowType = 15;
    public int unknownVarInt1;
    public int unknownVarInt2;
    public boolean isWilling;
    public long trader;
    public long player;
    public String displayName;
    public byte[] offers;

    @Override
    public byte pid() {
        return ProtocolInfo.UPDATE_TRADE_PACKET;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(windowId);
        this.putByte(windowType);
        this.putVarInt(unknownVarInt1);
        this.putVarInt(unknownVarInt2);
        this.putBoolean(isWilling);
        this.putEntityUniqueId(player);
        this.putEntityUniqueId(trader);
        this.putString(displayName);
        this.put(this.offers);
    }
}

package org.itxtech.nemisys.network.protocol.mcpe;

/**
 * @author Nukkit Project Team
 */
public class PlayerInputPacket extends DataPacket {

    public float motionX;
    public float motionY;

    public boolean unknownBool1;
    public boolean unknownBool2;

    @Override
    public void decode() {
        this.motionX = this.getLFloat();
        this.motionY = this.getLFloat();
        this.unknownBool1 = this.getBoolean();
        this.unknownBool2 = this.getBoolean();
    }

    @Override
    public void encode() {
    }

    @Override
    public byte pid() {
        return ProtocolInfo.PLAYER_INPUT_PACKET;
    }
}

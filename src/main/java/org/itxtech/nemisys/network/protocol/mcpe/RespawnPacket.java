package org.itxtech.nemisys.network.protocol.mcpe;

import org.itxtech.nemisys.math.Vector3f;

/**
 * @author Nukkit Project Team
 */
public class RespawnPacket extends DataPacket {

    public float x;
    public float y;
    public float z;

    @Override
    public void decode() {
        Vector3f v = this.getVector3f();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    @Override
    public void encode() {
        this.reset();
        this.putVector3f(this.x, this.y, this.z);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.RESPAWN_PACKET;
    }
}

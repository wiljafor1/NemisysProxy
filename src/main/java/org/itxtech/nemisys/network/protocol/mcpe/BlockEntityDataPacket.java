package org.itxtech.nemisys.network.protocol.mcpe;

import org.itxtech.nemisys.math.BlockVector3;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BlockEntityDataPacket extends DataPacket {

    public int x;
    public int y;
    public int z;
    public byte[] namedTag;

    @Override
    public byte pid() {
        return ProtocolInfo.BLOCK_ENTITY_DATA_PACKET;
    }

    @Override
    public void decode() {
        BlockVector3 v = this.getBlockVector3();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.namedTag = this.get();
    }

    @Override
    public void encode() {
        this.reset();
        this.putBlockVector3(this.x, this.y, this.z);
        this.put(this.namedTag);
    }
}
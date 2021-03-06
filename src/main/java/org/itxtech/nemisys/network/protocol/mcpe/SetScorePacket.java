package org.itxtech.nemisys.network.protocol.mcpe;

import org.itxtech.nemisys.network.protocol.mcpe.types.ScoreInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CreeperFace
 */
public class SetScorePacket extends DataPacket {

    public Action action;
    public List<ScoreInfo> infos;

    public void encode() {
        reset();
        putByte((byte) action.ordinal());

        putUnsignedVarInt(infos.size());
        infos.forEach(it -> {
            putVarLong(it.scoreId);
            putString(it.objective);
            putLInt(it.score);
            putByte((byte) Type.FAKE.ordinal());
            putString(it.name);
        });
    }

    @Override
    public void decode() {
        action = Action.values()[getByte()];

        int length = (int) getUnsignedVarInt();
        List<ScoreInfo> infos = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            long id = getVarLong();
            String obj = getString();
            int score = getLInt();

            getByte();

            String name = getString();

            ScoreInfo info = new ScoreInfo(id, obj, score, name);
            infos.add(info);
        }

        this.infos = infos;
    }

    @Override
    public byte pid() {
        return ProtocolInfo.SET_SCORE_PACKET;
    }

    public enum Action {
        SET,
        REMOVE
    }

    public enum Type {
        INVALID,
        PLAYER,
        ENTITY,
        FAKE
    }
}

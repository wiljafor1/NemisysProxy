package org.itxtech.nemisys.network.protocol.mcpe;

import org.itxtech.nemisys.utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by CreeperFace on 5.3.2017.
 */
public class ClientboundMapItemDataPacket extends DataPacket {

    public int[] eids = new int[0];

    public long mapId;
    public int update;
    public byte scale;
    public int width;
    public int height;
    public int offsetX;
    public int offsetZ;

    public byte dimensionId;

    public MapDecorator[] decorators = new MapDecorator[0];

    public long[] decoratorEntities = new long[0];

    public int[] colors = new int[0];
    public BufferedImage image = null;

    public static final int TEXTURE_UPDATE = 2;
    public static final int DECORATIONS_UPDATE = 4;
    public static final int ENTITIES_UPDATE = 8;

    @Override
    public byte pid() {
        return ProtocolInfo.CLIENTBOUND_MAP_ITEM_DATA_PACKET;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(mapId);

        int update = 0;
        if (eids.length > 0) {
            update |= 0x08;
        }
        if (decorators.length > 0) {
            update |= DECORATIONS_UPDATE;
        }

        if (image != null || colors.length > 0) {
            update |= TEXTURE_UPDATE;
        }

        this.putUnsignedVarInt(update);
        this.putByte(this.dimensionId);

        if ((update & 0x08) != 0) {
            this.putUnsignedVarInt(eids.length);
            for (int eid : eids) {
                this.putEntityUniqueId(eid);
            }
        }
        if ((update & (TEXTURE_UPDATE | DECORATIONS_UPDATE)) != 0) {
            this.putByte(this.scale);
        }

        if ((update & DECORATIONS_UPDATE) != 0) {
            this.putUnsignedVarInt(decoratorEntities.length);
            for (long id : this.decoratorEntities) {
                this.putEntityUniqueId(id);
            }

            this.putUnsignedVarInt(decorators.length);

            for (MapDecorator decorator : decorators) {
                this.putByte(decorator.rotation);
                this.putByte(decorator.icon);
                this.putByte(decorator.offsetX);
                this.putByte(decorator.offsetZ);
                this.putString(decorator.label);
                this.putUnsignedVarInt(decorator.color.getRGB());
            }
        }

        if ((update & TEXTURE_UPDATE) != 0) {
            this.putVarInt(width);
            this.putVarInt(height);
            this.putVarInt(offsetX);
            this.putVarInt(offsetZ);

            this.putUnsignedVarInt(width * height);

            if (image != null) {
                for (int y = 0; y < width; y++) {
                    for (int x = 0; x < height; x++) {
                        Color color = new Color(image.getRGB(x, y), true);
                        byte red = (byte) color.getRed();
                        byte green = (byte) color.getGreen();
                        byte blue = (byte) color.getBlue();

                        putUnsignedVarInt(Utils.toRGB(red, green, blue, (byte) 0xff));
                    }
                }

                image.flush();
            } else if (colors.length > 0) {
                for (int color : colors) {
                    putUnsignedVarInt(color);
                }
            }
        }
    }

    public class MapDecorator {
        public byte rotation;
        public byte icon;
        public byte offsetX;
        public byte offsetZ;
        public String label;
        public Color color;
    }
}

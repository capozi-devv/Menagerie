package net.capozi.menagerie.client.render;

import net.capozi.menagerie.Menagerie;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormats;

public final class TrickRoomShaders {
    public static ShaderProgram TRICK_ROOM;

    private TrickRoomShaders() {
    }

    public static void register() {
        CoreShaderRegistrationCallback.EVENT.register(context ->
                context.register(Menagerie.identifier("trick_room"), VertexFormats.POSITION_TEXTURE, shader -> TRICK_ROOM = shader)
        );
    }
}

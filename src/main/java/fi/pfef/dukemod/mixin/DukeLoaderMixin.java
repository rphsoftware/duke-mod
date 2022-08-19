package fi.pfef.dukemod.mixin;

import fi.pfef.dukemod.DukeMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jcodec.api.FrameGrab;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.image.BufferedImage;
import java.io.File;

@Mixin(net.minecraft.client.MinecraftClient.class)
public class DukeLoaderMixin {
    private static final Logger LOGGER = LogManager.getLogger();

    @Inject(method = "loadLogo(Lnet/minecraft/client/texture/TextureManager;)V", at = @At("TAIL"))
    public void loadLogo(TextureManager textureManager, CallbackInfo ci) throws Throwable {
        FrameGrab grab = FrameGrab.createFrameGrab(
                NIOUtils.readableChannel(new File(
                        DukeMod.class.getResource("/3billion_sm.mp4").toURI()
                ))
        );

        Picture picture;
        long before = System.nanoTime();
        while (null != (picture = grab.getNativeFrame())) {
            BufferedImage image = AWTUtil.toBufferedImage(picture);
            NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
            Identifier identifier = new Identifier(String.format("textures/duke/%d.png", DukeMod.list.size()));
            MinecraftClient.getInstance().getTextureManager().loadTexture(
                    identifier,
                    texture
            );
            DukeMod.list.add(identifier);

            System.out.println(identifier);
        }
        long after = System.nanoTime();

        System.out.printf("Load time: %sms%n", (after - before) / 1_000_000);
        LOGGER.info("And make it double");
        Display.setTitle("3 Billion Devices Run Java");
    }

    /*@Inject(at = @At("RETURN"), method="getMaxFramerate", cancellable = true)
    private void getMaxFramerate(CallbackInfoReturnable<Integer> cir) {
        if(cir.getReturnValue() == 30) {
            cir.setReturnValue(240);
            cir.cancel();
        }
    }*/


}

package fi.pfef.dukemod.mixin;

import fi.pfef.dukemod.DukeMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.gui.screen.TitleScreen.class)
public class MainMenuMixin extends Screen {
    @Shadow
    private String splashText;

    private long startNanotime;

    private static long MS_PER_FRAME = 40;

    @Inject(at = @At("HEAD"), cancellable = true, method = "transformPanorama(F)V")
    private void transformPanorama(float tickDelta, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "renderPanorama")
    private void renderPanorama(int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(at = @At("TAIL"), method = "renderBackground")
    private void renderBg(int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
        long timeDiff = (System.nanoTime() - this.startNanotime) / 1_000_000;
        long frameId = (timeDiff / MS_PER_FRAME) % DukeMod.list.size();

        this.client.getTextureManager().bindTexture(DukeMod.list.get((int) frameId));
        int w = this.width;
        int h = this.height;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(0, 0, 1.0).texture(0.0, 0.0).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(w, 0, 1.0).texture(1.0, 0.0).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(w, h, 1.0).texture(1.0, 1.0).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(0, h, 1.0).texture(0.0, 1.0).color(255, 255, 255, 255).next();
        tessellator.draw();
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void init(CallbackInfo ci) {
        this.splashText = "3 Billion Devices Run Java";
        this.startNanotime = System.nanoTime();
    }

    @ModifyVariable(at = @At("STORE"), method = "render", name = "string2", ordinal = 0)
    private String string1(String value) {
        return "Minecraft 1.8.3 Billion";
    }

    @ModifyVariable(at = @At("STORE"), method = "render", name = "string2", ordinal = 1)
    private String string2(String value) {
        return "3 Billion Devices Run Java";
    }
}

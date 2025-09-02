package de.shadowdara.betterbackups.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.screen.Screen;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        this.addDrawableChild(ButtonWidget.builder(
                        Text.translatable("button.betterbackups.backup"),
                        button -> {
                            // Dein Code hier – z. B. neues Screen öffnen oder Logik ausführen
                            System.out.println("Backup-Button wurde gedrückt!");
                        })
                //.dimensions(this.width / 2 - 100, this.height / 4 + 120, 200, 20)
                .dimensions(10, 10, 100, 20)
                .build()
        );
    }
}

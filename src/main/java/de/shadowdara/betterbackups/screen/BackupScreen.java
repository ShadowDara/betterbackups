package de.shadowdara.betterbackups.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class BackupScreen extends Screen {

    public BackupScreen() {
        super(Text.translatable("screen.betterbackups.title"));
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.back"),
                button -> this.close()
        ).dimensions(this.width / 2 - 100, this.height / 2 + 40, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context); // Standard-Hintergrund
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}

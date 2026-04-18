package de.shadowdara.betterbackups.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;

import java.io.File;
import java.util.List;

import static de.shadowdara.betterbackups.screen.BackupScreen.getFileSize;
import static de.shadowdara.betterbackups.screen.BackupScreen.humanReadableByteCount;

public class BackupEntry extends ElementListWidget.Entry<BackupEntry> {

    private final File file;
    private final ButtonWidget deleteButton;

    public BackupEntry(File file) {
        this.file = file;

        this.deleteButton = ButtonWidget.builder(
                Text.literal("X"),
                button -> {
                    MinecraftClient.getInstance().setScreen(
                            new ConfirmScreen(confirmed -> {
                                if (confirmed) {
                                    deleteFile(file);
                                    MinecraftClient.getInstance().setScreen(new BackupScreen());
                                } else {
                                    MinecraftClient.getInstance().setScreen(new BackupScreen());
                                }
                            },
                                    Text.translatable("button.betterbackups.delete"),
                                    Text.literal(file.getName()))
                    );
                }
        ).dimensions(0, 0, 20, 20).build();
    }

    // Function to delete a File
    private void deleteFile(File file) {
        if (file.isDirectory()) {
            // DO NOTHING
        } else {
            file.delete();
        }
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight,
                       int mouseX, int mouseY, boolean hovered, float tickDelta) {

        // Hover
        if (hovered) {
            context.fill(x - 28, y, x + entryWidth, y + entryHeight, 0x33FFFFFF);
        }

        // File name (left side)
        context.drawTextWithShadow(
                MinecraftClient.getInstance().textRenderer,
                Text.literal(file.getName() + " " +
                        humanReadableByteCount(getFileSize(file))),
                x - 20,
                y + 6,
                0xFFFFFF
        );

        // Delete button (right side, aligned!)
        deleteButton.setX(x + entryWidth - 10);
        deleteButton.setY(y);

        deleteButton.render(context, mouseX, mouseY, tickDelta);
    }

    @Override
    public List<? extends Element> children() {
        return List.of(deleteButton);
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return List.of(deleteButton);
    }
}

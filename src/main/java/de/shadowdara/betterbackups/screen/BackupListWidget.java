package de.shadowdara.betterbackups.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;

import java.io.File;

public class BackupListWidget extends ElementListWidget<BackupEntry> {

    public BackupListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
    }

    public void addBackup(File file) {
        this.addEntry(new BackupEntry(file));
    }

    private void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteFile(child);
                }
            }
        }
        file.delete();
    }
}

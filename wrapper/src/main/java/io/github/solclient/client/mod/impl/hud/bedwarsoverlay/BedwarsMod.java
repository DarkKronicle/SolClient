package io.github.solclient.client.mod.impl.hud.bedwarsoverlay;

import io.github.solclient.client.event.EventHandler;
import io.github.solclient.client.event.impl.PreTickEvent;
import io.github.solclient.client.event.impl.ReceiveChatMessageEvent;
import io.github.solclient.client.event.impl.ScoreboardRenderEvent;
import io.github.solclient.client.mod.ModCategory;
import io.github.solclient.client.mod.hud.HudElement;
import io.github.solclient.client.mod.impl.SolClientMod;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;


import java.util.*;
import java.util.regex.Pattern;

public class BedwarsMod extends SolClientMod {

    private final static Pattern[] GAME_START = {
            Pattern.compile("^\\s*?Protect your bed and destroy the enemy beds\\.\\s*?$"),
            Pattern.compile("^\\s*?Bed Wars Lucky Blocks\\s*?$"),
            Pattern.compile("^\\s*?Bed Wars Swappage\\s*?$")
    };

    private final static BedwarsMod INSTANCE = new BedwarsMod();

    public static BedwarsMod getInstance() {
        return INSTANCE;
    }

    protected BedwarsGame currentGame = null;
    protected final TeamUpgradesOverlay upgradesOverlay;
    private int targetTick = -1;

    private BedwarsMod() {
        upgradesOverlay = new TeamUpgradesOverlay(this);
    }

    @Override
    public String getDetail() {
        return I18n.translate("sol_client.mod.screen.by", "DarkKronicle") + " " + I18n.translate("sol_client.mod.screen.textues_by", "Sybillian");
    }

    @EventHandler
    public void onMessage(ReceiveChatMessageEvent event) {
        // Remove formatting
        String rawMessage = event.originalMessage.replaceAll("§.", "");
        if (currentGame != null) {
            currentGame.onChatMessage(rawMessage, event);
            String time = "§7" + currentGame.getFormattedTime() + " ";
            if (!event.cancelled) {
                // Add time to every message received in game
                if (event.newMessage != null) {
                    event.newMessage = new LiteralText(time).append(event.newMessage);
                } else {
                    event.newMessage = new LiteralText(time).append(event.formattedMessage);
                }
            }
        } else if (targetTick < 0 && BedwarsMessages.matched(GAME_START, rawMessage).isPresent()) {
            // Give time for Hypixel to sync
            targetTick = mc.inGameHud.getTicks() + 10;
        }
    }

    public Optional<BedwarsGame> getGame() {
        return currentGame == null ? Optional.empty() : Optional.of(currentGame);
    }

    @EventHandler
    public void onTick(PreTickEvent event) {
        if (currentGame != null) {
            if (currentGame.isStarted()) {
                // Trigger setting the header
                mc.inGameHud.getPlayerListWidget().setHeader(null);
                currentGame.tick();
            } else {
                if (checkReady()) {
                    currentGame.onStart();
                }
            }
        } else {
            if (targetTick > 0 && mc.inGameHud.getTicks() > targetTick) {
                currentGame = new BedwarsGame(this);
                targetTick = -1;
            }
        }
    }

    private boolean checkReady() {
        for (PlayerListEntry player : mc.player.networkHandler.getPlayerList()) {
            String name = mc.inGameHud.getPlayerListWidget().getPlayerName(player).replaceAll("§.", "");
            if (name.charAt(1) == ' ') {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<HudElement> getHudElements() {
        return Arrays.asList(upgradesOverlay);
    }

    @Override
    public String getId() {
        return "bedwars";
    }

    @Override
    public ModCategory getCategory() {
        return ModCategory.HUD;
    }

    public boolean inGame() {
        return currentGame != null && currentGame.isStarted();
    }

    @EventHandler
    public void onScoreboardRender(ScoreboardRenderEvent event) {
        if (inGame()) {
            currentGame.onScoreboardRender(event);
        }
    }

    public void gameEnd() {
        upgradesOverlay.onEnd();
        currentGame = null;
    }

}

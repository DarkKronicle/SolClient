package io.github.solclient.client.mod.impl.hud.crosshair;

import org.lwjgl.opengl.GL11;

import com.google.gson.annotations.Expose;
import com.mojang.blaze3d.platform.GlStateManager;

import io.github.solclient.client.event.EventHandler;
import io.github.solclient.client.event.impl.*;
import io.github.solclient.client.mod.impl.SolClientHudMod;
import io.github.solclient.client.mod.option.annotation.Option;
import io.github.solclient.client.util.*;
import io.github.solclient.client.util.data.*;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.Window;
import net.minecraft.util.hit.BlockHitResult.Type;
import net.minecraft.world.level.LevelInfo.GameMode;

public class CrosshairMod extends SolClientHudMod {

	private static final String DEFAULT_CROSSHAIR = "LCCH-9-ECBAgPAfAgQIEAA";

	@Expose
	@Option
	private boolean customCrosshair = false;
	@Expose
	@Option
	private final PixelMatrix crosshairPixels = new PixelMatrix(15, 15);
	@Expose
	@Option
	private boolean thirdPerson = true;
	@Expose
	@Option
	private boolean spectatorAlways = false;
	@Expose
	@Option
	private boolean debug = false;
	@Expose
	@Option
	private boolean blending = true;
	@Expose
	@Option
	private Colour crosshairColour = Colour.WHITE;
	@Expose
	@Option
	private boolean highlightEntities = false;
	@Expose
	@Option
	private Colour entityColour = Colour.PURE_RED;

	public CrosshairMod() {
		LCCH.parse(DEFAULT_CROSSHAIR, crosshairPixels);
	}

	@Override
	public String getId() {
		return "crosshair";
	}

	private void bind() {
		if (!customCrosshair) {
			mc.getTextureManager().bindTexture(DrawableHelper.GUI_ICONS_TEXTURE);
			return;
		}

		crosshairPixels.bind(-1, 0);
	}

	@EventHandler
	public void onCrosshairRender(PreGameOverlayRenderEvent event) {
		if (event.type == GameOverlayElement.CROSSHAIRS) {
			event.cancelled = true;
			if ((!debug && mc.options.debugEnabled)
					|| (!spectatorAlways && (mc.interactionManager.getCurrentGameMode() == GameMode.SPECTATOR
							&& mc.result.type != Type.ENTITY))
					|| (!thirdPerson && mc.options.perspective != 0)) {
				return;
			}

			crosshairColour.bind();

			GlStateManager.enableBlend();
			GlStateManager.enableAlphaTest();
			GlStateManager.blendFuncSeparate(770, 771, 1, 0);

			if (highlightEntities && mc.result != null && mc.result.entity != null
					&& !(mc.result.entity.isInvisible() || mc.result.entity.isInvisibleTo(mc.player)))
				entityColour.bind();
			else if (blending) {
				GlStateManager.blendFuncSeparate(775, 769, 1, 0);
				GlStateManager.enableAlphaTest();
			}

			Window window = new Window(mc);

			float half = customCrosshair ? crosshairPixels.getWidth() / 2 : 8;
			GlStateManager.pushMatrix();
			GlStateManager.scale(getScale(), getScale(), getScale());
			GlStateManager.translate((int) (window.getScaledWidth() / getScale() / 2 - half),
					(int) (window.getScaledHeight() / getScale() / 2 - half), 0);

			bind();

			int scale = customCrosshair ? crosshairPixels.getWidth() : 16;

			if (customCrosshair)
				DrawableHelper.drawTexture(0, 0, 0, 0, scale, scale, scale, scale, scale, scale);
			else
				MinecraftUtils.drawTexture(0, 0, 0, 0, 16, 16, 0);

			GlStateManager.popMatrix();
			GlStateManager.color(1, 1, 1);
		}
	}

}

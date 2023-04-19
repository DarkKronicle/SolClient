/*
 * Sol Client - an open source Minecraft client
 * Copyright (C) 2021-2023  TheKodeToad and Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.solclient.client.mod.impl.tweaks.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;

import io.github.solclient.client.mod.impl.tweaks.TweaksMod;
import net.minecraft.client.render.item.HeldItemRenderer;

@Mixin(HeldItemRenderer.class)
public class ItemRendererMixin {

	@Inject(method = "renderFireOverlay", at = @At("HEAD"))
	public void transformFire(float tickDelta, CallbackInfo callback) {
		if (TweaksMod.enabled && TweaksMod.instance.lowerFireBy != 0) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(0, -TweaksMod.instance.lowerFireBy, 0);
		}
	}

	@Inject(method = "renderFireOverlay", at = @At("RETURN"))
	public void popFire(float tickDelta, CallbackInfo callback) {
		if (TweaksMod.enabled && TweaksMod.instance.lowerFireBy != 0)
			GlStateManager.popMatrix();
	}

}

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

package io.github.solclient.client.mod.impl.hud.chat.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import io.github.solclient.client.mod.impl.hud.chat.ChatMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerEntity.ChatVisibilityType;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;chatVisibilityType:Lnet/minecraft/entity/player/PlayerEntity$ChatVisibilityType;"))
	public ChatVisibilityType overrideChatVisibility(GameOptions instance) {
		if (ChatMod.enabled)
			return ChatVisibilityType.FULL; /* Always allow chat to be opened */

		return instance.chatVisibilityType;
	}

}

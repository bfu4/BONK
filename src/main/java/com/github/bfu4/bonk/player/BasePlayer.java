/**
 * MIT License
 * <p>
 * Copyright (c) 2021 bfu4
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.bfu4.bonk.player;

import com.github.bfu4.bonk.PluginBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;

import java.util.UUID;

/**
 * BasePlayer - Base Class for creating BukkitIntegrated player objects
 *
 * @author bfu4
 * @since 11/02/2021 @ 12.34
 */
public class BasePlayer implements ServerOperator {

   private final CommandSender sender;

   public BasePlayer(CommandSender sender) {
      this.sender = sender;
   }

   public void sendMessage(String message) {
      sender.sendMessage(translateMessage(message));
   }

   public void sendFormattedMessage(String message) {
      sendMessage(PluginBase.COLORED_PREFIX + " " + message);
   }

   public boolean isOperator() {
      return sender.isOp();
   }

   public boolean hasPermission(String permission) {
      return sender.hasPermission(permission);
   }

   public String getName() {
      return sender.getName();
   }

   public UUID getUUID() {
      Player player = Bukkit.getPlayer(sender.getName());
      return player != null ? player.getUniqueId() : null;
   }

   public Location getLocation() { return sender instanceof Player ? ((Player) sender).getLocation() : null; }

   private String translateMessage(String message) {
      return ChatColor.translateAlternateColorCodes('&', message);
   }

   @Override
   public boolean isOp() {
      return sender.isOp();
   }

   public CommandSender getSender() {
      return sender;
   }

   @Override
   public void setOp(boolean b) {
      sender.setOp(b);
   }

}

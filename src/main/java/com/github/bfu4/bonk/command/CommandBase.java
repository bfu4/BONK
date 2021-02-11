/**
 * MIT License
 * <p>
 * Copyright (c) 2021 bfu4
 * <p>
 *
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


package com.github.bfu4.bonk.command;

import com.github.bfu4.bonk.player.BasePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import com.github.bfu4.bonk.PluginBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class CommandBase implements CommandExecutor, TabExecutor {

   private final PluginBase plugin;
   private final String identifier;

   private final ArrayList<String> tabArgs;
   private final HashMap<String, CommandBase> subcommands;

   /**
    * Create a new command
    *
    * @param commandIdentifier name of the command
    * @param plugin plugin the command is registered to
    */
   public CommandBase(String commandIdentifier, PluginBase plugin) {
      this.plugin = plugin;
      this.identifier = commandIdentifier;
      this.tabArgs = new ArrayList<>();
      this.subcommands = new HashMap<>();
   }

   /**
    * Create a new command
    *
    * @param commandIdentifier name of the command
    * @param plugin plugin the command is registered to
    * @param validArgs arguments that are valid and able to be tabbed
    */
   public CommandBase(String commandIdentifier, PluginBase plugin, String ... validArgs) {
      this(commandIdentifier, plugin);
      tabArgs.addAll(Arrays.asList(validArgs));
   }

   /**
    * Get the name of the command
    *
    * @return name
    */
   public String getName() { return identifier; }

   /**
    * Add valid arguments to the command
    *
    * @param args arguments that can be tabbed
    */
   public void addValidArgs(String ... args) { tabArgs.addAll(Arrays.asList(args)); }

   /**
    * Add a subcommand
    *
    * @param subcommandName name of the subcommand
    * @param subcommand instance of the subcommand
    */
   public void addSubcommand(String subcommandName, CommandBase subcommand) {
      subcommands.put(subcommandName, subcommand);
      tabArgs.add(subcommandName);
   }

   /**
    * Get the valid arguments
    *
    * @return tabbable arguments
    */
   public ArrayList<String> getTabArgs() { return tabArgs; }

   public PluginBase getPlugin() { return plugin; }

   public String getPermission() {
      return getClass().getAnnotation(Permission.class).value();
   }

   public boolean isSubcommand() { return getClass().getAnnotation(Subcommand.class) != null; }

   /**
    * Execute the command
    *
    * @param user user invoking the command
    * @param args arguments passed
    */
   public abstract void execute(BasePlayer user, String[] args);

   @Override
   public boolean onCommand(CommandSender sender, Command command, String identifier, String[] args) {
      BasePlayer user = new BasePlayer(sender);
      CommandBase next = null;
      if (args.length >= 1) {
         next = getSubcommands().get(args[0].toLowerCase());
      }
      boolean nextIsSubcommand = next != null && next.isSubcommand();
      String[] newArgs = new String[nextIsSubcommand ? args.length - 1 : args.length];
      if (nextIsSubcommand)  {
         System.arraycopy(args, 1, newArgs, 0, args.length - 1);
      } else {
         newArgs = args;
      }
      if (user.hasPermission(getPermission())) {
         if (nextIsSubcommand) {
            if (user.hasPermission(next.getPermission())) {
               next.onCommand(sender, command, identifier, newArgs);
            } else {
               user.sendFormattedMessage("&cInsufficient permission!");
            }
         } else {
            execute(user, newArgs);
         }
      } else {
         user.sendFormattedMessage("&cInsufficient permission!");
      }
      return true;
   }

   @Override
   public List<String> onTabComplete(CommandSender sender, Command command, String identifier, String[] args) {
      List<String> tabbableArgs = new ArrayList<>();
      CommandBase cmd = null;
      if (args.length > 0) {
         cmd =  getSubcommands().get(args[0]);
      }

      // Cycle through the subcommands to get the tab args.
      if (args.length > 1) {
         for (int i = 1; i < args.length; i ++) {
            if (args[i].equals("\\s") || cmd == null) break;
            CommandBase next = cmd.getSubcommands().get(args[i]);
            if (next == null) break;
            cmd = next;
         }
      }
      return cmd != null
         ? StringUtil.copyPartialMatches(args[args.length - 1], cmd.getTabArgs(), tabbableArgs)
         : StringUtil.copyPartialMatches(args[args.length - 1], tabArgs, tabbableArgs);
   }

   /**
    * Get the command usage
    *
    * @return usage
    */
   public String getUsage() {
      Usage usageAnnotation = getClass().getAnnotation(Usage.class);
      if (usageAnnotation != null) return usageAnnotation.value();
      return "&cIncorrect command usage.";
   }

   /**
    * Get the subcommands
    *
    * @return subcommands
    */
   public HashMap<String, CommandBase> getSubcommands() { return this.subcommands; }
}
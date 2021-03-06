/**
 * MIT License
 * <p>
 * Copyright (c) 2021 Copystrike
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

package com.github.bfu4.bonk.file;

import com.github.bfu4.bonk.PluginBase;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Config -
 *
 * @author Copystrike
 * @since 06/02/2021 @ 21:57
 */
public class YamlFile extends BaseFile {

   protected YamlConfiguration configuration;

   /**
    * Create new yaml file
    *
    * @param plugin    plugin that the file belongs to
    * @param name      name of the file
    */
   public YamlFile(PluginBase plugin, String name) {
      super(plugin, name, ".yml");
   }

   /**
    * Reload the file
    */
   @Override
   public void reload() {
      configuration = YamlConfiguration.loadConfiguration(getFile());
   }

   /**
    * Create the file
    */

   @Override
   public void createFile() {

      if (!getFile().getParentFile().mkdir() && !getFile().getParentFile().exists()) {
         getPlugin().getLogger().warning("Could not find parent directory!");
         return;
      }

      if (!getFile().exists()) {
         try {
            if (!getFile().createNewFile()) {
               if (!getFile().isFile()) {
                  getPlugin().getLogger().warning("Failed to create " + getName() + getExtension() + ". Error: The file is not a file. Maybe a directory?");
                  return;
               }
               getPlugin().getLogger().warning("Failed to create " + getName() + getExtension() + ". Error: File already exist.");
               return;
            } else {
               // Gets the file from the plugin itself
               InputStream pluginFile = getPlugin().getResource(getName() + getExtension());
               BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pluginFile));
               // Loads the config as a YAML
               this.configuration = YamlConfiguration.loadConfiguration(bufferedReader);
               // Set the setting copy defaults to true so we can save changes to the target file
               configuration.options().copyDefaults(true);
               // And saving so it gets saved to the target file
               configuration.save(getFile());
               getPlugin().getLogger().info("Successfully created " + getName() + getExtension() + "!");
            }
         } catch (Exception e) {
            getPlugin().getLogger().warning("Failed to create " + getName() + getExtension() + "! Error: " + e.getLocalizedMessage() + ".");
            return;
         }
      }

      if (configuration == null) configuration = YamlConfiguration.loadConfiguration(getFile());

      try {
         configuration.load(getFile());
      } catch (IOException | InvalidConfigurationException e) {
         getPlugin().getLogger().warning("Failed to load " + getName() + getExtension() + "! Error: " + e.getLocalizedMessage() + ".");
      }
   }

   /**
    * Get the parent configuration
    *
    * @return configuration
    */
   public YamlConfiguration getConfiguration() {
      if (configuration == null) {
         configuration = YamlConfiguration.loadConfiguration(getFile());
      }
      return configuration;
   }

   public void save() {
      try {
         getConfiguration().save(getFile());
      } catch (IOException e) {
         getPlugin().getLogger().warning("Failed to save file! Reason: " + e.getMessage());
      }
   }

}
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

import java.io.File;

/**
 * BaseFile - Base for file objects
 *
 * @author Copystrike
 * @since 06/02/2021 @ 21:58
 */
public abstract class BaseFile {

   private final PluginBase plugin;
   private final File dataFolder;
   private final String name;
   private final String extension;
   protected File file;

   /**
    * Create new file
    *
    * @param plugin plugin that the file belongs to
    * @param name name of the file
    * @param extension file extension
    */
   public BaseFile(PluginBase plugin, String name, String extension) {
      this.plugin = plugin;
      this.dataFolder = plugin.getDataFolder();
      this.name = name;
      this.extension = extension;

      this.file = new File(dataFolder,name + extension);
   }

   /**
    * Get the plugin this file belongs to
    *
    * @return plugin
    */
   public PluginBase getPlugin() { return this.plugin; }

   /**
    * Get the file
    *
    * @return file
    */
   public File getFile() { return file; }

   /**
    * Get the folder which contains the file
    *
    * @return folder
    */
   public File getDataFolder() { return dataFolder; }

   /**
    * Get the name of the file
    *
    * @return name
    */
   public String getName() { return name; }

   /**
    * Get the extension of the file
    *
    * @return file extension
    */
   public String getExtension() { return extension; }

   /**
    * Get the path of the file
    *
    * @return path
    */
   public String getPath() { return file.getPath(); }

   /**
    * Reload the file
    */
   public abstract void reload();

   /**
    * Create the physical file on the filesystem
    */
   public abstract void createFile();

}

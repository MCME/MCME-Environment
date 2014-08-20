/*
 * ResourceRegions, a regions based texture-switcher
 * Copyright (c) 2014 dags <http://dags.me>
 *
 *   This program is free software: you can redistribute it and/or modify it
 *   under the terms of the GNU General Public License as published by the
 *   Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful, but WITHOUT
 *   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *   FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *   for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.dags.resourceregions.util;

import me.dags.resourceregions.ResourceRegions;

import java.io.File;
import java.io.IOException;

/**
 * @author dags_ <dags@dags.me>
 */

public class FileUtil
{

    public static File getFile(File folder, String fileName)
    {
        File f = new File(folder, fileName);
        if (!f.exists())
        {
            if (fileName.contains(".") || f.isFile())
            {
                try
                {
                    f.createNewFile();
                }
                catch (IOException e)
                {
                    return null;
                }
            }
            else if (fileName.endsWith("/") || f.isDirectory())
            {
                f.mkdirs();
            }
        }
        return f;
    }

    public static File getRegionToLoad(String fileName)
    {
        File[] contents = getRegionFolder().listFiles();
        if (contents != null)
        {
            for (File f : contents)
            {
                String name = f.getName();
                if (!name.endsWith(".json"))
                {
                    continue;
                }
                name = name.replace(".json", "");
                if (name.equalsIgnoreCase(fileName))
                {
                    return f;
                }
            }
        }
        return null;
    }

    public static String getMatches(String fileName)
    {
        boolean found = false;
        StringBuilder sb = new StringBuilder();
        sb.append("No matches could be found!").trimToSize();
        File[] contents = getRegionFolder().listFiles();
        if (contents != null)
        {
            fileName = fileName.toLowerCase();
            for (File f : contents)
            {
                if (f.getName().contains(fileName))
                {
                    if (!found)
                    {
                        sb.setLength(0);
                        found = true;
                    }
                    sb.append(f.getName().replace(".json", ""));
                    sb.append(", ");
                }
            }
            if (found)
            {
                sb.deleteCharAt(sb.length() - 2);
            }
        }
        return sb.toString();
    }


    public static File getRegionFolder()
    {
        return new File(ResourceRegions.getPlugin().getDataFolder(), "regions");
    }

}

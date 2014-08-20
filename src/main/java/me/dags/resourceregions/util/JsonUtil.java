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
import me.dags.resourceregions.region.Region;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.File;
import java.io.IOException;

/**
 * @author dags_ <dags@dags.me>
 */

public class JsonUtil
{

    private static final ObjectMapper om = new ObjectMapper().enable(SerializationConfig.Feature.INDENT_OUTPUT);

    public static Region loadRegion(File f)
    {
        try
        {
            synchronized (om)
            {
                return om.readValue(f, Region.class);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean saveRegion(Region r)
    {
        File folder = FileUtil.getFile(ResourceRegions.getPlugin().getDataFolder(), "regions/");
        File save = FileUtil.getFile(folder, r.getFileName());
        try
        {
            synchronized (om)
            {
                om.writeValue(save, r);
            }
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }
}

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

package me.dags.resourceregions.region;

import me.dags.resourceregions.util.JsonUtil;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

import java.awt.*;

/**
 * @author dags_ <dags@dags.me>
 */

@JsonAutoDetect(JsonMethod.NONE)
public class Region
{

    @JsonProperty
    private String name;
    @JsonProperty
    private String packUrl;
    @JsonProperty
    private String worldName;
    @JsonProperty
    private int weight;
    @JsonProperty
    private int[] xpoints;
    @JsonProperty
    private int[] zpoints;

    private Polygon bounds;

    public Region()
    {}

    public Region(boolean dummy)
    {
        if (dummy)
        {
            name = "";
            weight = -1;
        }
    }

    public void init()
    {
        bounds = new Polygon(xpoints, zpoints, xpoints.length);
        xpoints = null;
        zpoints = null;
    }

    public void setName(String s)
    {
        name = s;
    }

    public void setPackUrl(String s)
    {
        packUrl = s;
    }

    public void setWorldName(String s)
    {
        worldName = s;
    }

    public void resetBounds()
    {
        if (bounds == null)
        {
            bounds = new Polygon();
        }
        bounds.reset();
    }

    public void addPoint(int x, int z)
    {
        if (bounds == null)
        {
            bounds = new Polygon();
        }
        bounds.addPoint(x, z);
    }

    public void setWeight(int i)
    {
        weight = i;
    }

    public String getName()
    {
        return name;
    }

    public String getPackUrl()
    {
        return packUrl;
    }

    public String getWorldName()
    {
        return worldName;
    }

    public boolean contains(int x, int z)
    {
        return bounds.contains(x, z);
    }

    public int getWeight()
    {
        return weight;
    }

    public boolean validate()
    {
        return name != null && packUrl != null && worldName != null && bounds != null && bounds.npoints > 1 && weight > -1;
    }

    public boolean save()
    {
        xpoints = new int[bounds.npoints];
        zpoints = new int[bounds.npoints];
        for (int i = 0; i < bounds.npoints; i++)
        {
            xpoints[i] = bounds.xpoints[i];
            zpoints[i] = bounds.ypoints[i];
        }
        return JsonUtil.saveRegion(this);
    }

    public String getFileName()
    {
        return (worldName + "-" + name).toLowerCase() + ".json";
    }

}

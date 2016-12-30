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
    private int n;
    @JsonProperty
    private int[] xpoints;
    @JsonProperty
    private int[] zpoints;

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

    public void setName(String s)
    {
        name = s;
    }

    public void setPackUrl(String s)
    {
        packUrl = s;
    }
    
    public String getPackUrl() {
        return packUrl;
    }

    public void setWorldName(String s)
    {
        worldName = s;
    }

    public void addPoint(int x, int z)
    {
        int length = n;
        int[] xp = new int[length + 1];
        int[] zp = new int[length + 1];
        for (int i = 0; i < length; i++)
        {
            xp[i] = xpoints[i];
            zp[i] = zpoints[i];
        }
        xp[length] = x;
        zp[length] = z;
        xpoints = xp;
        zpoints = zp;
        n = xpoints.length;
    }

    public void resetBounds()
    {
        xpoints = new int[0];
        zpoints = new int[0];
        n = 0;
    }

    public void setWeight(int i)
    {
        weight = i;
    }

    public String getName()
    {
        return name;
    }

    public String getWorldName()
    {
        return worldName;
    }
    
    public int[] getXPoints() {
        return xpoints;
    }
    
    public int[] getZPoints() {
        return zpoints;
    }

    public boolean contains(int x, int z)
    {
        boolean inside = false;
        for (int i = 0, j = n - 1; i < n; j = i++)
        {
            if ((zpoints[i] > z) != (zpoints[j] > z) && (x < (xpoints[j] - xpoints[i]) * (z - zpoints[i]) / (zpoints[j] - zpoints[i]) + xpoints[i]))
            {
                inside = !inside;
            }
        }
        return inside;
    }

    public int getWeight()
    {
        return weight;
    }

    public boolean isValid()
    {
        return name != null && packUrl != null && worldName != null && n > 1 && weight > -1;
    }

    public boolean save()
    {
        return JsonUtil.saveRegion(this);
    }

    public String getFileName()
    {
        return (worldName + "-" + name).toLowerCase() + ".json";
    }

}

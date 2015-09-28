/*
 *
 * SHARON - Human Activities Simulator
 * Author: ATG Group (http://atg.deib.polimi.it/)
 *
 * Copyright (C) 2015, Politecnico di Milano
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

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package it.polimi.deib.atg.sharon.data;


import static it.polimi.deib.atg.sharon.utils.Methods.geoDist;
import static java.util.Arrays.sort;

public class Sensor {

    private int value, x, y, range;
    private int[] areax, areay;
    private double prob;
    private String name;

    public Sensor(String name, int value, int x, int y) {
        this(name, value, x, y, 10, 1);
    }

    public Sensor(String name, int value, int x, int y, int range, double prob) {
        this.name = name;
        this.value = value;
        this.x = x;
        this.y = y;
        this.range = range;
        this.prob = prob;
    }

    public Sensor(String name, int value, int x, int y, int[] areax, int[] areay, double prob) {
        this.name = name;
        this.value = value;
        this.x = x;
        this.y = y;
        this.areax = new int[2];
        System.arraycopy(areax, 0, this.areax, 0, areax.length);
        this.areay = new int[2];
        System.arraycopy(areay, 0, this.areay, 0, areax.length);
        sort(this.areax);
        sort(this.areay);
        this.prob = prob;
    }

    public boolean isActivatedBy(int tgt_x, int tgt_y) {
        if (areax == null) {
            return (geoDist(x, y, tgt_x, tgt_y) <= range);
        } else {
            return (areax[0] < tgt_x && tgt_x <= areax[1] && areay[0] < tgt_y && tgt_y <= areay[1]);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public double getProb() {
        return prob;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }
}

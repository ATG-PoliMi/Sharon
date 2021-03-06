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

import java.util.ArrayList;

public class Targets {
	ArrayList <Integer> LLADLTicks;
	
	public Targets() {
		
	}
	
	public Targets (ArrayList<Integer> LLADLTicks) {
		this.LLADLTicks = LLADLTicks;
	}

	public ArrayList<Integer> getLLADLTicks() {
		return LLADLTicks;
	}

	public void setLLADLTicks(ArrayList<Integer> lLADLTicks) {
		LLADLTicks = lLADLTicks;
	} 

}

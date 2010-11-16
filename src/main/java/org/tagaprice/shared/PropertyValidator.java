/*
 * Copyright 2010 TagAPrice.org
 * 
 * Licensed under the Creative Commons License. You may not
 * use this file except in compliance with the License. 
 *
 * http://creativecommons.org/licenses/by-nc/3.0/
*/

/**
 * Project: TagAPriceUI
 * Filename: PropertyValidator.java
 * Date: 05.07.2010
*/
package org.tagaprice.shared;

import java.util.ArrayList;
import java.util.HashMap;

import org.tagaprice.shared.data.Property;
import org.tagaprice.shared.data.PropertyTypeDefinition;
import org.tagaprice.shared.data.PropertyGroup;
import org.tagaprice.shared.data.Category;
import org.tagaprice.shared.data.PropertyTypeDefinition.Datatype;

public class PropertyValidator {

	
	public static boolean isValid(Category type, SearchResult<Property> properties){
		
		HashMap<String, ArrayList<Property>> pl = propertyListToHash(properties);
		PropertyGroup pg = extractPropertyGroupFromType(type);
		
		//TestCount
		if(!testCount(pg,pl))
			return false;
		
		//TestInt
		if(!testInt(pg, pl))
			return false;
		
		//TestDouble
		if(!testDouble(pg, pl))
			return false;
		
		return true;
	}
	
	private static boolean testCount(PropertyGroup pg, HashMap<String, ArrayList<Property>> pl){
		
		for(PropertyTypeDefinition pd:pg.getGroupElements()){
			if(pl.get(pd.getName())!=null 
					&& pd.isUnique()
					&& pl.get(pd.getName()).size()>1){
				return false;
			}
			
		}
		
		return true;
	}
	
	
	private static boolean testInt(PropertyGroup pg,HashMap<String, ArrayList<Property>> pl){
		
		for(PropertyTypeDefinition pd:pg.getGroupElements()){
			if(pd.getType().equals(Datatype.INT) && pl.get(pd.getName())!=null){
				for(Property pDa:pl.get(pd.getName())){		
					try{
						Integer.parseInt(pDa.getValue());
					} 
					catch(NumberFormatException nfe) {	
						return false;
					}
				}
			}
		}
		
		
		return true;
	}
	
	private static boolean testDouble(PropertyGroup pg,HashMap<String, ArrayList<Property>> pl){
		
		for(PropertyTypeDefinition pd:pg.getGroupElements()){
			if(pd.getType().equals(Datatype.DOUBLE) && pl.get(pd.getName())!=null){
				for(Property pDa:pl.get(pd.getName())){		
					try{
						Double.parseDouble(pDa.getValue());
					} 
					catch(NumberFormatException nfe) {	
						return false;
					}
				}
			}
		}
		
		
		return true;
	}
	
	private static HashMap<String, ArrayList<Property>> propertyListToHash(SearchResult<Property> properties){
		HashMap<String, ArrayList<Property>> hashProperties = new HashMap<String, ArrayList<Property>>();
		for(Property pd:properties){
			if(hashProperties.get(pd.getName())==null){
				hashProperties.put(pd.getName(), new ArrayList<Property>());
			}			
			hashProperties.get(pd.getName()).add(pd);
		}
		
		return hashProperties;
	}
	
	private static PropertyGroup extractPropertyGroupFromType(Category type){
		PropertyGroup returnVal = new PropertyGroup("NutritionFacts", PropertyGroup.GroupType.LIST);
		
		for(PropertyGroup pg:type.getPropertyGroups()){
			for(PropertyTypeDefinition pd:pg.getGroupElements()){
				returnVal.addGroupElement(pd);
			}
		}
		
		
		return returnVal;
	}
}

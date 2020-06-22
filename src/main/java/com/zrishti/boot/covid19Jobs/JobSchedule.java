package com.zrishti.boot.covid19Jobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.net.*;
import java.nio.*;
import java.nio.file.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class JobSchedule {

	@Scheduled (initialDelay = 2000L,fixedDelay=200000L)
	public void getDataFromWHO() {
		
		try {
			

		DBAccess cdetails=new DBAccess();
		cdetails.getConnection();
		
		
		System.out.println("Getting data from --- REST API" + new Date());
		ObjectMapper objectMapper = new ObjectMapper();
		
		//read json file data to String
				byte[] jsonData = Files.readAllBytes(Paths.get("../../owid-covid-data.json"));
				JsonNode rootNode = objectMapper.readTree(jsonData);
				

//				URL url = new URL("https://covid.ourworldindata.org/data/owid-covid-data.json");	
//						System.out.println("Getting data from ---URL - > " + url);
//				JsonNode rootNode = objectMapper.readTree(url);
				Iterator<Map.Entry<String, JsonNode>> iter = rootNode.fields();
			
				
				int dummyCtr = 0;
				while (iter.hasNext()) {
					Map.Entry<String, JsonNode> entry = iter.next();
					String key = entry.getKey();
					JsonNode valEntryNode = entry.getValue();
					//System.out.println("\n\n\n");
					//System.out.print(" valEntryNode.isObject()" + key +  " - > " + valEntryNode.isObject());
					//System.out.print("valEntryNode.isArray()" +  valEntryNode.isArray());
					
					if (valEntryNode.isArray()) {
					        ArrayNode arrayNode = (ArrayNode) valEntryNode;
							System.out.println( " arrayNode.size() " + arrayNode.size());
					        for (int i = 0; i < arrayNode.size(); i++) {
								JsonNode dateEntry = arrayNode.get(i);
					            //System.out.println(dateEntry.get("location") + " " + dateEntry.get("date") +  "  " + dateEntry.get("total_cases") +  "  " + dateEntry.get("new_cases"));
								cdetails.insertIntoDB("insert into cases values (" + 
										"'" + dateEntry.get("date").textValue() + "'," +
										"'" + dateEntry.get("location").textValue() + "'," +
											  checkAndReturnInt(dateEntry,"total_cases") + "," +
											  checkAndReturnInt(dateEntry,"new_cases") + "," +
											  checkAndReturnInt(dateEntry,"total_deaths")  + "," +
											  checkAndReturnInt(dateEntry,"new_deaths") + "," +
										"0" + ");");		
								//if(i>2) break;
					        }

					   }
					
					dummyCtr++;						
   					//if(dummyCtr>10) break;
					
//					map.entrySet()
//					     .forEach(System.out::println);
				}
				
			
			} catch(Exception e){
				
				e.printStackTrace();
				//send message to fix this exception
			}
					

	}
	
	// so rror as API has some missing data so need to check.
	private int checkAndReturnInt(JsonNode dateEntry, String key) {
		
		if(dateEntry==null || dateEntry.get(key)==null)
		{
			return -1;
		}else {
			
			return dateEntry.get(key).asInt();
		}
		
		
	}

}

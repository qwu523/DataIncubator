import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
public class main {

	public static void main(String[] args) throws FileNotFoundException {
		int month;
		String[] f=new String[21];
//		String[] f=new String[12];
		int col=0,row=0;
		int sameStartDest=0;
		int startId=0, endId=0, bikeId=0;
		int preciseTripDuration=0;
		long tripCount=0;
		float st_lat=0, st_long=0, ed_lat=0, ed_long=0;
		double totalTripDist=0;
		float earthRadius=6371;
		String stTime="",edTime="";
		Date d1=null,d2 = null;
		SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy HH:mm");  
		ArrayList<Long> timeAl;
		long sumTimeAl=0;
		Long[] avgDuration=new Long[21];
		int userType=0;
		int exceedCount=0;
		int numOfBike=0;
		long sumBikeMoveTime=0;
		HashMap<Integer,Integer> bikeStEdStation=new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> bikeMoveTime=new HashMap<Integer,Integer>();
		for(month=0;month<12;month++){
			if(month<9) f[month]="20150"+Integer.toString(month+1)+"-citibike-tripdata.csv";
			else f[month]="2015"+Integer.toString(month+1)+"-citibike-tripdata.csv";
		}
		for(month=0;month<9;month++){
			f[month+12]="20160"+Integer.toString(month+1)+"-citibike-tripdata.csv"; 
		}
		String pattern = "[,\n]";
	    Pattern r = Pattern.compile(pattern);
	    ArrayList<Integer> al=new ArrayList<Integer>();
	    ArrayList<Integer> startAl=new ArrayList<Integer>(); 
	    ArrayList<Integer> destAl=new ArrayList<Integer>();
	    
	    HashMap<Integer,HashSet> hm=new HashMap<Integer,HashSet>();
	    ArrayList<Integer> station=new ArrayList<Integer>();
	          
	    Scanner scanner;
	    for(int i=0;i<f.length;i++){
		    scanner = new Scanner(new File(f[i]));
	        scanner.useDelimiter(r);
	        col=0;row=0;
	        timeAl=new ArrayList<Long>();
	        sumTimeAl=0;
	        System.out.println("Processing file: "+f[i]);
	        while(scanner.hasNext()){       	
	            //System.out.print(scanner.next()+"|");
	        	String test=scanner.next().toString();
	        	test=test.replaceAll("[\"]","");
	        	if(col==0 && row>0){
	        		try{
	        			preciseTripDuration=Integer.valueOf(test);
	        		}catch(NumberFormatException e){
	        			e.printStackTrace();
	        		}
	        		al.add(preciseTripDuration);	        		
	        	}
	        	if(col==3 && row>0) startAl.add(Integer.valueOf(test));
	        	if(col==7 && row>0) destAl.add(Integer.valueOf(test));
	        	///bikeid hashmap
	        	if(col==3 && row>0) startId=Integer.valueOf(test);
	        	if(col==7 && row>0) endId=Integer.valueOf(test);
	        	if(col==11 && row>0) {
	        		bikeId=Integer.valueOf(test);
	        		if(!hm.containsKey(bikeId)){   		
		        	//	HaseSet<Integer>=new HaseSet<I>();
		        		hm.put(bikeId,new HashSet<Integer>()); 		
		        	}
	        		hm.get(bikeId).add(startId);
	        		hm.get(bikeId).add(endId);
	        		if(!bikeStEdStation.containsKey(bikeId)){
	        			bikeStEdStation.put(bikeId,endId);
	        			numOfBike++;
	        		}else{
	        			if(bikeStEdStation.get(bikeId)!=startId){
	        				if(!bikeMoveTime.containsKey(bikeId)){
	        					bikeMoveTime.put(bikeId,0);
	        				}else{
	        					bikeMoveTime.put(bikeId,bikeMoveTime.get(bikeId)+1);
	        				}
	        			}
	        		}
	        		bikeStEdStation.put(bikeId,endId);
	        	}
	        	
	        	if(col==5 && row>0) st_lat=Float.valueOf(test);
	        	if(col==6 && row>0) st_long=Float.valueOf(test);
	        	if(col==9 && row>0) st_lat=Float.valueOf(test);
	        	if(col==10 && row>0) st_long=Float.valueOf(test);
	        	if(row>0){
		        	double tripDist=Great_circle_distance(earthRadius,st_lat,st_long,ed_lat,ed_long);
		        	totalTripDist+=tripDist;
	        	}
	        	
	        	if(col==1 && row>0) stTime=test;
	        	if(col==2 && row>0) edTime=test;
	        	if(row>0 && col==2){
	        		/*
	        		try {
	        		    d1 = format.parse(stTime);
	        		    d2 = format.parse(edTime);
	        		} catch (ParseException e) {
	        		    //e.printStackTrace();
	        			try {
	        				format = new SimpleDateFormat("M/dd/yyyy HH:mm");
	        				d1 = format.parse(stTime);
		        		    d2 = format.parse(edTime);
		        		} catch (ParseException ee) {
		        			try {
		        				format = new SimpleDateFormat("MM/d/yyyy HH:mm");
		        				d1 = format.parse(stTime);
			        		    d2 = format.parse(edTime);
			        		} catch (ParseException eee) {
			        		//	eee.printStackTrace();
			        			try {
			        				format = new SimpleDateFormat("M/d/yyyy HH:mm");
			        				d1 = format.parse(stTime);
				        		    d2 = format.parse(edTime);
				        		} catch (ParseException eeee) {
				        			eeee.printStackTrace();
				        		}
			        		}
		        		}
	        		}  
	        		long diff =d2.getTime() - d1.getTime();
	        		long timeDurationInSeconds = TimeUnit.MILLISECONDS.toSeconds(diff);	     */    		
	        	//	timeAl.add(timeDurationInSeconds);//Use start time and end time to compute trip duration.
	        		timeAl.add((long)preciseTripDuration);//Use tripduration given in the .csv file.
	        	}
	        	
	        	if(col==12 && row>0){
	        		userType=test.equals("Subscriber")?1:2;
	        		if(userType==1){//subscriber
	        			exceedCount+=(preciseTripDuration>45*60?1:0);
	        		}else{//customer
	        			exceedCount+=(preciseTripDuration>30*60?1:0);
	        		}
	        	}
	        	
	     
	        	
	        	col=col==14?0:col+1;
	        	row=col==0?row+1:row;
	        	if(col==13 && row>0) tripCount++;
	        	
	        }
	        for(Long t: timeAl){
	        	sumTimeAl+=t;
	        }
	        avgDuration[i]=sumTimeAl/timeAl.size();
	//      System.out.println(timeAl.size()+"|"+sumTimeAl);
	        scanner.close();
	    }
        Integer[] ia=al.toArray(new Integer[al.size()]);
        Integer[] startIa=startAl.toArray(new Integer[startAl.size()]);
        Integer[] destIa=destAl.toArray(new Integer[destAl.size()]);
///Q2_1        
        Arrays.sort(ia);
        float medianIa=ia.length/2==0?(float) (0.5*(ia[ia.length/2-1]+ia[ia.length/2])):(float) ia[ia.length/2];//q2_1
        System.out.println("Q2_1: "+Float.valueOf(medianIa));
///Q2_2
       for(int i=0;i<startIa.length;i++){
    	   if(startIa[i]==destIa[i]) sameStartDest++;
       }
       System.out.println("Q2_2: "+(float)sameStartDest/tripCount*100+"%");
///Q2_3  
       ArrayList<Integer> bikeAl=new ArrayList<Integer>();
       for ( Integer key : hm.keySet() ) {
    	   bikeAl.add(hm.get(key).size());    	   
    	}
       Integer[] bikeArray=bikeAl.toArray(new Integer[bikeAl.size()]);
       double EX2=0,EX=0;
       for(Integer i:bikeArray) {
    	   EX2+=i*i;
    	   EX+=i;
       }
       double sd=Math.sqrt(EX2+EX);
       System.out.println("Q2_3: "+sd);
///Q2_4
       double avgDist=totalTripDist/tripCount;
       System.out.println("Q2_4: "+avgDist);
///Q2_5
       Long min=Long.MAX_VALUE;
       Long max=Long.MIN_VALUE;
       for(Long i : avgDuration) { //if you need to know the index, use int (i=0;i<array.length;i++) instead
    	   if(i > max) max = i;
    	   if(i < min) min = i; 
       }
       System.out.println("Q2_5: MAX-"+max+". MIN-"+min);
       for(int i=0;i<avgDuration.length;i++) System.out.println("Q2_5: Month "+i+" average duration in seconds: "+avgDuration[i]);
///Q2_6
       System.out.println("Ride duration exceed percentage: "+(float)exceedCount/tripCount*100+"%");
///Q2_8
       for(Map.Entry<Integer, Integer> entry : bikeMoveTime.entrySet()) {
    	//    int key = entry.getKey();
    	    int value = entry.getValue();
    	    sumBikeMoveTime+=value;
       }
       System.out.println("Average move time for each bike: "+(float)sumBikeMoveTime/numOfBike);
	}

	
	
	public static double Great_circle_distance(float earthRadius, float st_lat, float st_long, float ed_lat, float ed_long){
		double gcd=2*Math.asin(Math.sqrt(Math.pow(Math.sin(Math.toRadians((st_lat-ed_lat)/2)),2)+
				Math.cos(Math.toRadians(st_lat))*Math.cos(Math.toRadians(ed_lat))*Math.pow(Math.sin(Math.toRadians((st_long-ed_long)/2)),2)));
		return earthRadius*gcd;
	}

}


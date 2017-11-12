import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class MainApplication 
{
	private static final int  MaxAmountOfPhotos = 10000;
	private static final String FileNamePattern = "IMG_%04d";
	private static ArrayList<Photo> _photos;
	
	private static FileWriter _fileWriter;
	private static PrintWriter _printWriter;
	
	public static void main(String[] args) throws IOException
	{
		//all output results will be in this file!
		_fileWriter = new FileWriter("out.txt");
		_printWriter = new PrintWriter(_fileWriter);		
		
		_photos = new ArrayList<Photo>(MaxAmountOfPhotos);
		
		//set dates range to find between
		Calendar startDate = Calendar.getInstance();
		startDate.clear();
		startDate.set(2016, 0, 1); //January 1, 2016
		long startDateInMiliseconds = startDate.getTimeInMillis();
        
		Calendar endDate = Calendar.getInstance();
		endDate.clear();
		endDate.set(2017, 11, 31); //December 31, 2017
		long endDateInMiliseconds = endDate.getTimeInMillis();

		//generate test Photo objects with test data
		for(int i = 0; i < MaxAmountOfPhotos; i++)
		{
			String fileName = String.format(FileNamePattern, i);
			long randomDateInMiliseconds = GetRandomDateFromRange(startDateInMiliseconds, endDateInMiliseconds);
			int randomDateInSeconds = TrimmMiliseconds(randomDateInMiliseconds);
			
			Photo photo = new Photo(fileName, randomDateInSeconds);
			_photos.add(photo);
		}
		
		PrintPhotos(_photos.toArray(new Photo[_photos.size()]));
		
		System.out.println("Please enter date in format: yyyy-MM-dd");
		_printWriter.println("Please enter date in format: yyyy-MM-dd");
		
		//Enter date for searching across photos
		Scanner scanner= new Scanner(System.in);
		String input= scanner.nextLine();
		_printWriter.println(input);
		scanner.close();
		
        Photo[] foundPhotos = findPhotos(input);
        System.out.println("-----------SEARCH RESULTS-----------");
        _printWriter.println("-----------SEARCH RESULTS-----------");
        
        //results handling
        if(foundPhotos == null)
        {
        	System.out.println("Error! Wrong date format! Can't parse string.");
        	_printWriter.println("Error! Wrong date format! Can't parse string.");
        }
        else if(foundPhotos.length == 0)
        {
        	System.out.println("Nothing found.");
        	_printWriter.println("Nothing found.");
        }
        else
        {       	
        	PrintPhotos(foundPhotos);
        	System.out.println(String.format("Found %d item(s).", foundPhotos.length));
        	_printWriter.println(String.format("Found %d item(s).", foundPhotos.length));
        }
        
        _printWriter.flush();
		_printWriter.close();
		_fileWriter.close();
	}

	
	private static void PrintPhotos(Photo[] photos) 
	{
		for(int i = 0; i < photos.length; i++)
		{
			System.out.println(photos[i].toString());
			_printWriter.println(photos[i].toString());
		}
	}
	
	public static Photo[] findPhotos(String date)
	{
		SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
        Date inputDate = dateParser.parse(date, new ParsePosition(0));
        
        if(inputDate == null)
        {
        	return null;
        }
        
        long inputDateInMiliseconds = inputDate.getTime();
        
        long inputDateTrimmed = CutOffTimeInformation(inputDateInMiliseconds);
        ArrayList<Photo> foundPhotos = new ArrayList<Photo>();
        
        for(int i = 0; i < _photos.size(); i++)
        {
        	Photo obj = _photos.get(i);
        	long photoDateTrimmed = CutOffTimeInformation(obj.taken_at * 1000L);
        	if(inputDateTrimmed == photoDateTrimmed)
        	{
        		foundPhotos.add(obj);
        	}
        }

        return foundPhotos.toArray(new Photo[foundPhotos.size()]);
	}
	
	private static long CutOffTimeInformation(long timeInMiliseconds)
	{
		Calendar c = Calendar.getInstance();
	    c.setTime(new Date(timeInMiliseconds));
	    c.set(Calendar.HOUR_OF_DAY, c.getActualMinimum(Calendar.HOUR_OF_DAY));
	    c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
	    c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
	    c.set(Calendar.MILLISECOND, c.getActualMinimum(Calendar.MILLISECOND));
	    return c.getTime().getTime();
	}
	
	private static long GetRandomDateFromRange(long minDate, long maxDate)
	{
		return ThreadLocalRandom.current().nextLong(minDate, maxDate);
	}
	
	private static int TrimmMiliseconds(long dateInMiliseconds)
	{
		return (int) (dateInMiliseconds / 1000L);
	}
}
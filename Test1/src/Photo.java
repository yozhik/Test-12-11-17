import java.sql.Date;
import java.text.SimpleDateFormat;

public class Photo
{
	private static int count = 0;
	
	public int id;
	public String filename;
	public int taken_at;
	
	public Photo(String fileName, int takenAt)
	{
		id = count;
		filename = fileName;
		taken_at = takenAt;
		
		count++;
	}
	
	public String toString()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = formatter.format(new Date(((long)taken_at) * 1000L));
        
		StringBuilder sb = new StringBuilder();
		sb.append(Integer.toString(id));
		sb.append(" --> ");
		sb.append(filename);
		sb.append(" --> ");
		sb.append(formattedDate);
		
		return sb.toString();
	}  
}
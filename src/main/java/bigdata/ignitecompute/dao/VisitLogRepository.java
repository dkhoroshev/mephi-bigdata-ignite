package bigdata.ignitecompute.dao;

import bigdata.ignitecompute.model.VisitLog;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class VisitLogRepository {

    private Ignite ignite;
    private IgniteCache<Integer, VisitLog> visitLogCacheConfiguration;


    public VisitLogRepository(Ignite ignite, String visitLogCacheName) {
        this.visitLogCacheConfiguration = ignite.getOrCreateCache(visitLogCacheName);
    }

    public String add(String visitLogFile){
        Integer i = 1;
        try {
            File file1 = new File(visitLogFile);
            FileReader fr1 = new FileReader(file1);
            BufferedReader reader1 = new BufferedReader(fr1);
            String line1 = reader1.readLine();
            while (line1 !=null){
                String[] col = line1.split(",");
                this.visitLogCacheConfiguration.put(i, new VisitLog(Integer.parseInt(col[0]),Integer.parseInt(col[1]),new SimpleDateFormat("YYYY-MM-dd H:m:s").parse(col[2]),Boolean.parseBoolean(col[3])));
                line1 = reader1.readLine();
//                System.out.println(col[2]);
                i++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException|ParseException e) {
            e.printStackTrace();
        }
        finally {
            return i.toString();
        }
    }

    public void destroy() {
        this.visitLogCacheConfiguration.destroy();
    }

}

package bigdata.ignitecompute.dao;

import bigdata.ignitecompute.config.IgniteConf;
import bigdata.ignitecompute.model.Publication;
import bigdata.ignitecompute.model.VisitLog;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.resources.IgniteInstanceResource;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class PublicationRepository {
    @IgniteInstanceResource
    private Ignite ignite;
    private IgniteCache<Integer, Publication> publicationCacheConfiguration;
    private IgniteCache<Integer, VisitLog> visitLogCacheConfiguration;


    public PublicationRepository(Ignite ignite, String publicationCacheName) {
        this.publicationCacheConfiguration = ignite.getOrCreateCache(publicationCacheName);
    }

    public String add(String publicationFile){
        Integer i = 1;
        try {

            File file = new File(publicationFile);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line !=null){
                String[] col = line.split(",",0);
                this.publicationCacheConfiguration.put(i, new Publication(Integer.parseInt(col[0]),Integer.parseInt(col[1]), new SimpleDateFormat("YYYY-MM-dd").parse(col[2]),col[3]));
                line = reader.readLine();
//                System.out.println(col[2]);
                i++;
            }

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            return i.toString();
        }
    }



}

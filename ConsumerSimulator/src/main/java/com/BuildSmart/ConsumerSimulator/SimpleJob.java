package com.BuildSmart.ConsumerSimulator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class SimpleJob implements Job
{
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        Logger logger = Logger.getLogger(SimpleJob.class.getName());
        Random random = new Random(System.currentTimeMillis());

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String target = dataMap.get("target").toString();

        int value = random.nextInt();
        logger.info("Value: " + value);

        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(target);

            // Request parameters and other properties.
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("value", value + ""));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    // do something useful
                    logger.info(instream.toString());
                }
            }
        }
        catch (Exception e) {
            logger.severe("Exception occurred while executing job: " + e.getMessage());
        }


    }
}

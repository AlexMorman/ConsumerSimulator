package com.BuildSmart.ConsumerSimulator;



import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.InputStream;
import java.nio.charset.Charset;
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
            httppost.setEntity(new UrlEncodedFormEntity(params, Charset.defaultCharset()));

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);

            logger.info(String.valueOf(response.getCode()));
        }
        catch (Exception e) {
            logger.severe("Exception occurred while executing job: " + e.getMessage());
        }


    }
}

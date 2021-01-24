package com.findthebusiness.backend.controller;

import com.findthebusiness.backend.service.service_implementation.SitemapServiceImpl;
import com.findthebusiness.backend.utils.ScheduleDelayTimes;
import com.findthebusiness.backend.utils.enums.DirectoryPathsEnum;
import com.findthebusiness.backend.utils.enums.UpdateFrequencyType;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;

@RestController
@RequestMapping("api/sitemap")
public class SitemapController {

    private final SitemapServiceImpl sitemapService;
    private String sitemapsDirectoryPath;

    public SitemapController(SitemapServiceImpl sitemapService) {
        this.sitemapService = sitemapService;
        sitemapsDirectoryPath = new FileSystemResource("").getFile().getAbsolutePath() + "\\sitemaps";
    }

    @Scheduled(fixedDelay = ScheduleDelayTimes.GENERATE_SITEMAPS)
    private void generateSitemaps() throws MalformedURLException {
        sitemapService.createSitemapForShopsWithFrequency(UpdateFrequencyType.DAILY, sitemapsDirectoryPath);
        sitemapService.createSitemapIndexFile(sitemapsDirectoryPath);
    }

    @GetMapping(path="/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    private FileSystemResource getSitemapIndex() {
        File file = new File(sitemapsDirectoryPath+ "\\sitemap_index.xml");
        return new FileSystemResource(file);
    }

}

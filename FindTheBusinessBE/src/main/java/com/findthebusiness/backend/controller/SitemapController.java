package com.findthebusiness.backend.controller;

import com.findthebusiness.backend.service.service_implementation.SitemapServiceImpl;
import com.findthebusiness.backend.utils.ScheduleDelayTimes;
import com.findthebusiness.backend.utils.enums.DirectoryPathsEnum;
import com.findthebusiness.backend.utils.enums.UpdateFrequencyType;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("api/sitemap")
public class SitemapController {

    private final SitemapServiceImpl sitemapService;
    private final Path sitemapDirectory;

    public SitemapController(SitemapServiceImpl sitemapService) throws IOException {
        this.sitemapService = sitemapService;
        sitemapDirectory = Files.createTempDirectory("sitemaps");
    }

    @Scheduled(fixedDelay = ScheduleDelayTimes.GENERATE_SITEMAPS)
    private void generateSitemaps() throws MalformedURLException {
        sitemapService.createSitemapForShopsWithFrequency(UpdateFrequencyType.DAILY, sitemapDirectory.toAbsolutePath().toString());
        sitemapService.createSitemapIndexFile(sitemapDirectory.toAbsolutePath().toString());
    }

    @GetMapping(path="/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    private FileSystemResource getSitemapIndex() {
        File file = new File(sitemapDirectory.toAbsolutePath().toString() + "\\sitemap_index.xml");
        return new FileSystemResource(file);
    }

    @GetMapping(path="/{sitemap}", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    private FileSystemResource getSitemapIndex(@PathVariable String sitemap) {
        File file = new File(sitemapDirectory.toAbsolutePath().toString() + "\\" + sitemap);
        return new FileSystemResource(file);
    }

}

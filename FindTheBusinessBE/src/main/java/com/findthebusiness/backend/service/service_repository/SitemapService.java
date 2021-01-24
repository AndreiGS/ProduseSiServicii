package com.findthebusiness.backend.service.service_repository;

import java.net.MalformedURLException;

import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.utils.enums.UpdateFrequencyType;
import java.util.List;

public interface SitemapService {
	void createSitemapForShopsWithFrequency(UpdateFrequencyType frequency, String sitemapsDirectoryPath) throws MalformedURLException;
	void createSitemapIndexFile(String sitemapsDirectoryPath) throws MalformedURLException;
	List<Shops> findAllPublishedShops();
}

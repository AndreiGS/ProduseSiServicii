package com.findthebusiness.backend.service.service_implementation;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import com.findthebusiness.backend.repository.ShopRepository;
import com.findthebusiness.backend.service.service_repository.SitemapService;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.utils.enums.UpdateFrequencyType;
import com.redfin.sitemapgenerator.*;
import org.springframework.stereotype.Service;

@Service
public class SitemapServiceImpl implements SitemapService {

	private final ShopRepository shopRepository;

	public SitemapServiceImpl(ShopRepository shopRepository) {
		this.shopRepository = shopRepository;
	}

	public void createSitemapForShopsWithFrequency(UpdateFrequencyType updateFrequency, String sitemapsDirectoryPath)  throws MalformedURLException {

		File targetDirectory = new File(sitemapsDirectoryPath);		 
		WebSitemapGenerator wsg = WebSitemapGenerator.builder("https://produsesiservicii.ro", targetDirectory)
									.fileNamePrefix("sitemap_" + updateFrequency.toString())
									.gzip(true)
									.build();

		List<Shops> shops = findAllPublishedShops();
		for(Shops shop : shops) {
			String url = "https://produsesiservicii.ro/store" + shop.getId();

			WebSitemapUrl wsmUrl = new WebSitemapUrl.Options(url)
					.lastMod(shop.getBoughtAt())
					.priority(0.9)
					.changeFreq(changeFrequencyFromUpdateFrequency(updateFrequency))
					.build();
			wsg.addUrl(wsmUrl);
		}

		wsg.write();
		if (shops.size() > 50000) {
			wsg.writeSitemapsWithIndex();
		}	

	}

	public void createSitemapIndexFile(String sitemapsDirectoryPath) throws MalformedURLException {
		
		File targetDirectory = new File(sitemapsDirectoryPath);
		File outFile = new File(sitemapsDirectoryPath + "\\sitemap_index.xml");
		SitemapIndexGenerator sig = new SitemapIndexGenerator("https://produsesiservicii.ro", outFile);

		File[] files = targetDirectory.listFiles();

		if(files == null)
			return;

		for (File file : files) {
			boolean isNotSitemapIndexFile = !file.getName().startsWith("sitemap_index");
			if (isNotSitemapIndexFile) {
				SitemapIndexUrl sitemapIndexUrl = new SitemapIndexUrl("https://produsesiservicii.ro/api/sitemap/" + file.getName(), new Date(file.lastModified()));
				sig.addUrl(sitemapIndexUrl);
			}

		}
		sig.write();	
	}

	private ChangeFreq changeFrequencyFromUpdateFrequency(UpdateFrequencyType updateFrequency) {
		
		ChangeFreq response = null;
		
		if(updateFrequency == UpdateFrequencyType.DAILY)  response = ChangeFreq.DAILY;
	    else if(updateFrequency == UpdateFrequencyType.WEEKLY) response = ChangeFreq.WEEKLY;
	    else if(updateFrequency == UpdateFrequencyType.MONTHLY) response = ChangeFreq.MONTHLY;
	    else if(updateFrequency == UpdateFrequencyType.YEARLY) response = ChangeFreq.YEARLY;
	    else if(updateFrequency == UpdateFrequencyType.TERMINATED) response = ChangeFreq.NEVER;
	    else if(updateFrequency == UpdateFrequencyType.UNKNOWN) response = ChangeFreq.MONTHLY;
	    else response = ChangeFreq.MONTHLY;
		
		return response;
			
	}

	@Override
	public List<Shops> findAllPublishedShops() {
		return shopRepository.findAllByIsPublishedAndActualSizeGreaterThan(true, 0).orElseGet(ArrayList::new);
	}
}

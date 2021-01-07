package com.findthebusiness.backend.service.service_implementation;

import com.findthebusiness.backend.dto.search.*;
import com.findthebusiness.backend.entity.Categories;
import com.findthebusiness.backend.entity.Items;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.entity.Subcategories;
import com.findthebusiness.backend.mapper.mapper_implementation.CategoriesMapperImpl;
import com.findthebusiness.backend.mapper.mapper_implementation.ItemsMapperImpl;
import com.findthebusiness.backend.mapper.mapper_implementation.ShopMapperImpl;
import com.findthebusiness.backend.mapper.mapper_implementation.SubcategoriesMapperImpl;
import com.findthebusiness.backend.repository.CategoryRepository;
import com.findthebusiness.backend.repository.ItemsRepository;
import com.findthebusiness.backend.repository.ShopRepository;
import com.findthebusiness.backend.repository.SubcategoriesRepository;
import com.findthebusiness.backend.service.service_repository.SearchService;

import com.findthebusiness.backend.utils.enums.ShopPromotionTypesEnum;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchServiceImpl implements SearchService {

    private final ItemsRepository itemsRepository;
    private final SubcategoriesRepository subcategoriesRepository;
    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;
    private final ItemsMapperImpl itemsMapper;
    private final ShopMapperImpl shopsMapper;
    private final CategoriesMapperImpl categoriesMapper;
    private final SubcategoriesMapperImpl subcategoriesMapper;

    public SearchServiceImpl(ItemsRepository itemsRepository, SubcategoriesRepository subcategoriesRepository, ShopRepository shopRepository, CategoryRepository categoryRepository, ItemsMapperImpl itemsMapper, ShopMapperImpl shopsMapper, CategoriesMapperImpl categoriesMapper, SubcategoriesMapperImpl subcategoriesMapper) {
        this.itemsRepository = itemsRepository;
        this.subcategoriesRepository = subcategoriesRepository;
        this.shopRepository = shopRepository;
        this.categoryRepository = categoryRepository;
        this.itemsMapper = itemsMapper;
        this.shopsMapper = shopsMapper;
        this.categoriesMapper = categoriesMapper;
        this.subcategoriesMapper = subcategoriesMapper;
    }

    //CONTROLLER METHODS
    @Override
    public List<SearchShopResponseDto> search(String searchBarText, String category, String subcategory, Double rating, String county) throws Exception {
        if(rating != null)
            if(rating == -1)
                rating = null;

        if(county != null)
            if(county.equals("none"))
                county = null;

        if(searchBarText != null)
            if(searchBarText.equals("none"))
                searchBarText = null;

        if(category != null)
            if(category.equals("none"))
                category = null;

        if(subcategory != null)
            if(subcategory.equals("none"))
                subcategory = null;

        List<SearchShopResponseDto> foundItems = generateFromItemList(searchBarText, subcategory, category, rating, county);
        if(foundItems != null)
            return foundItems;

        List<SearchShopResponseDto> foundShops = generateFromShopTitleOrDescription(searchBarText, subcategory, category, rating, county);
        if(foundShops != null)
            return foundShops;

        throw new Exception();
    }

    @Override
    public CategoriesAndSubcategoriesDto getCategoriesAndSubcategories() {
        List<GetCategoriesResponseDto> categories = convertCategoriesToGetCategoriesResponseDto(findAllCategories());
        List<GetSubcategoriesResponseDto> subcategories = convertSubcategoriesToGetSubcategoriesResponseDto(findAllSubcategories());

        return new CategoriesAndSubcategoriesDto(categories, subcategories);
    }

    @Override
    public List<GetSubcategoriesByCategoryRequestDto> getSubcategoriesByCategory(String categoryId) {
        Categories categories = findCategoryById(categoryId);

        if(categories == null) {
            return new ArrayList<>();
        }

        List<Subcategories> subcategories = findAllSubcategoriesByCategory(categories);
        return convertSubcategoriesToGetSubcategoriesByCategoryRequestDto(subcategories);
    }

    //CUSTOM METHODS
    @Override
    public List<SearchShopResponseDto> generateFromItemList(String searchBarText, String subcategory, String category, Double rating, String county) {
        List<Items> foundItems;
        if(subcategory == null && category == null && rating == null && county == null) {
            //Get all items
            foundItems = findAllItemsAndShopPublished();
        } else {
            //Find items by category/subcategory
            foundItems = findItems(category, subcategory, rating, county);
        }

        if(foundItems != null) {
            //Order them using lcs with binary search
            List<Items> items = orderItems(foundItems, searchBarText);

            if(items != null) {
                //Get shops and their list of items found
                HashMap<String, List<SearchItemsResponseDto>> shopsAndItems = findShopsByItems(items);
                return convertHashMapToList(convertShopIdToShopDto(shopsAndItems));
            }
        }

        return null;
    }

    @Override
    public List<SearchShopResponseDto> generateFromShopTitleOrDescription(String searchBarText, String subcategory, String category, Double rating, String county) {
        List<Shops> foundShops;
        if(subcategory == null && category == null && rating == null && county == null) {
            //Get all shops
            foundShops = findAllPublishedShops();
        } else {
            //Find shops by category/subcategory
            foundShops = findShops(category, subcategory, rating, county);
        }

        if(foundShops != null) {
            List<Shops> shops = orderShopsByTitle(foundShops, searchBarText);

            if(shops != null) {
                List<ShopDto> shopDtoList = convertShopsToShopDtoList(shops);

                return convertShopDtoListToSearchShopResponseDtoWithNoItems(shopDtoList);
            } else {
                return generateFromShopDescription(foundShops, searchBarText);
            }
        }

        return null;
    }

    @Override
    public List<SearchShopResponseDto> generateFromShopDescription(List<Shops> foundShops, String searchBarText) {
        List<Shops> shops = orderShopsByDescription(foundShops, searchBarText);
        if(shops != null) {
            List<ShopDto> shopDtoList = convertShopsToShopDtoList(shops);

            return convertShopDtoListToSearchShopResponseDtoWithNoItems(shopDtoList);
        }
        return null;
    }

    @Override
    public List<Items> orderItems(List<Items> previousList, String input) {
        SortedSet<ItemsWithPointsDto> sortedItemSet = new TreeSet<>();
        for(Items item : previousList) {
            int itemPoints = getPoints(item, input, null);

            if (itemPoints == -1)
                continue;

            ItemsWithPointsDto newItem = new ItemsWithPointsDto(itemPoints, item);
            sortedItemSet.add(newItem);
        }

        int size = sortedItemSet.size();
        if(size <= 0)
            return null;

        return convertItemsWithPointsDtoSortedSetToItemsList(sortedItemSet);
    }

    @Override
    public List<Shops> orderShopsByTitle(List<Shops> previousList, String input) {
        SortedSet<ShopsByTitleWithPointsDto> sortedShopSet = new TreeSet<>();

        for(Shops shop : previousList) {
            int itemPoints = getPoints(shop, input, "TITLE");

            if (itemPoints == -1)
                continue;

            ShopsByTitleWithPointsDto newShop = new ShopsByTitleWithPointsDto(itemPoints, shop);
            sortedShopSet.add(newShop);
        }

        int size = sortedShopSet.size();
        if(size <= 0)
            return null;

        return convertShopsByTitleWithPointsDtoSortedSetToShopsList(sortedShopSet);
    }

    @Override
    public List<Shops> orderShopsByDescription(List<Shops> previousList, String input) {
        SortedSet<ShopsByDescriptionWithPointsDto> sortedShopSet = new TreeSet<>();

        for(Shops shop : previousList) {
            int itemPoints = getPoints(shop, input, "DESCRIPTION");

            if (itemPoints == -1)
                continue;

            ShopsByDescriptionWithPointsDto newShop = new ShopsByDescriptionWithPointsDto(itemPoints, shop);
            sortedShopSet.add(newShop);
        }

        int size = sortedShopSet.size();
        if(size <= 0)
            return null;

        return convertShopsByDescriptionWithPointsDtoSortedSetToShopsList(sortedShopSet);
    }

    @Override
    public List<Items> findItems(String category, String subcategory, Double rating, String county) {
        List<Items> foundItems = null;

        if (county != null && rating != null) {
            foundItems = findItemsByCountyAndRating(rating, county);
        } else if (county != null) {
            foundItems = findItemsByCounty(county);
        } else if (rating != null) {
            foundItems = findItemsByRating(rating);
        }

        if (subcategory != null) {
            Subcategories subcategories = findSubcategoryById(subcategory);
            if (subcategories != null && foundItems == null)
                foundItems = findItemsBySubcategory(subcategories);
            else if (subcategories != null)
                foundItems = findItemsBySubcategoryInList(subcategories, foundItems);

            return foundItems;
        }
        if (category != null) {
            Categories categories = findCategoryById(category);
            if (categories != null && foundItems == null)
                foundItems = findItemsByCategory(categories);
            else if (categories != null)
                foundItems = findItemsByCategoryInList(categories, foundItems);
        }
        return foundItems;
    }

    @Override
    public List<Shops> findShops(String category, String subcategory, Double rating, String county) {
        List<Shops> foundShops = null;

        if(county != null && rating != null) {
            foundShops = findShopsByCountyAndRating(county, rating);
        } else if(county != null) {
            foundShops = findShopsByCounty(county);
        } else if(rating != null) {
            foundShops = findShopsByRating(rating);
        }

        if(subcategory != null) {
            Subcategories subcategories = findSubcategoryById(subcategory);
            if(subcategories != null && foundShops == null)
                foundShops = findAllShopsBySubcategory(subcategories);
            else if(subcategories != null)
                foundShops = findAllShopsBySubcategoryInList(subcategories, foundShops);

            return foundShops;
        }

        if(category != null) {
            Categories categories = findCategoryById(category);
            if(categories != null && foundShops == null)
                foundShops = findAllShopsByCategory(categories);
            else if(categories != null)
                foundShops = findAllShopsByCategoryInList(categories, foundShops);
        }

        return foundShops;
    }

    @Override
    public HashMap<ShopDto, List<SearchItemsResponseDto>> convertShopIdToShopDto(HashMap<String, List<SearchItemsResponseDto>> shopsAndItems) {
        HashMap<ShopDto, List<SearchItemsResponseDto>> newShopsAndItems = new HashMap<>();

        Iterator it = shopsAndItems.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            String shopId = (String) pair.getKey();
            Shops shops = findShopById(shopId);

            newShopsAndItems.put(convertShopToShopDto(shops), (List<SearchItemsResponseDto>) pair.getValue());
        }

        return newShopsAndItems;
    }

    @Override
    public List<SearchShopResponseDto> convertHashMapToList(HashMap<ShopDto, List<SearchItemsResponseDto>> shopsAndItems) {
        List<SearchShopResponseDto> searchShopResponseDtoList= new ArrayList<>();

        Iterator it = shopsAndItems.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            Object value = pair.getValue();
            List<SearchItemsResponseDto> itemsIds = null;

            if(value instanceof List) {
                itemsIds = (List<SearchItemsResponseDto>) pair.getValue();
            }

            searchShopResponseDtoList.add(new SearchShopResponseDto(((ShopDto) pair.getKey()), itemsIds));
            it.remove();
        }

        return searchShopResponseDtoList;
    }

    @Override
    public HashMap<String, List<SearchItemsResponseDto>> findShopsByItems(List<Items> items) {
        HashMap<String, List<SearchItemsResponseDto>> shopListItemsPair = new HashMap<>();

        for(Items item : items) {
            String shopId = item.getShop().getId();
            List<SearchItemsResponseDto> itemsByShop = shopListItemsPair.get(shopId);

            if(itemsByShop == null) {
                itemsByShop = new ArrayList<>();
            }

            itemsByShop.add(new SearchItemsResponseDto(item.getId()));
            shopListItemsPair.put(shopId, itemsByShop);
        }

        return shopListItemsPair;
    }

    @Override
    public ShopDto convertShopToShopDto(Shops shops) {
        return shopsMapper.convertShopsToShopDto(shops);
    }

    @Override
    public List<ShopDto> convertShopsToShopDtoList(List<Shops> shops) {
        List<ShopDto> shopDtoList = new ArrayList<>();
        for(Shops shop : shops) {
            shopDtoList.add(convertShopToShopDto(shop));
        }
        return shopDtoList;
    }

    @Override
    public List<SearchShopResponseDto> convertShopDtoListToSearchShopResponseDtoWithNoItems(List<ShopDto> shopDtoList) {
        List<SearchShopResponseDto> searchShopResponseDtoList = new ArrayList<>();

        for(ShopDto shop : shopDtoList) {
            SearchShopResponseDto entityToAdd = new SearchShopResponseDto(shop, null);
            searchShopResponseDtoList.add(entityToAdd);
        }

        return searchShopResponseDtoList;
    }

    @Override
    public int getPoints(Object object, String input, String whatStringToCheck) {
        if(object instanceof Items) {
            return getPointsForItems((Items) object, input);
        }
        if(object instanceof Shops) {
            return getPointsForShop((Shops) object, input, whatStringToCheck);
        }

        return -1;
    }

    @Override
    public int getPointsForShop(Shops shops, String input, String whatStringToCheck) {
        if(shops.getPublished() == false)
            return -1;

        if(input == null) {
            if(shops.getPromotedInSearches() == true)
                return 1000;
            return 0;
        }

        int points;

        if(whatStringToCheck.equals("TITLE")) {
            points = getPoints(input, shops.getName());
        }
        else {
            points = getPoints(input, shops.getDescription());
        }

        if(points == -1)
            return points;

        if(shops.getPromotedInSearches() == true)
            points += 1000;

        return points;
    }

    @Override
    public int getPointsForItems(Items items, String input) {
        Shops shop = items.getShop();

        if(shop.getPublished() == false)
            return -1;

        if(input == null) {
            if(shop.getPromotedInSearches() == true)
                return 1000;
            return 0;
        }

        int points = 0;

        points = getPoints(input, items.getTitle());

        if(points == -1)
            return -1;

        if(shop.getPromotedInSearches() == true)
            points += 1000;

        return points;
    }

    @Override
    public int getPoints(String input, String title) {
        int points = -1;
        String[] words = title.toLowerCase().trim().split("\\s+");
        for(String word : words) {
            int localPoints = compareStrings(input.toLowerCase(), word);
            if(localPoints > points) {
                points=localPoints;
            }
        }
        return points;
    }

    @Override
    public int compareStrings(String input, String title) {
        int n=input.length();
        int m=title.length();
        int min=Math.min(n, m);
        for (int i = 0; i < min; i++) {
            if (input.charAt(i) != title.charAt(i)) {
                return (i < n / 2) ? -1 : i;
            }
        }
        return min;
    }

    @Override
    public List<Items> convertItemsWithPointsDtoSortedSetToItemsList(SortedSet<ItemsWithPointsDto> itemsWithPointsDtos) {
        Iterator i = itemsWithPointsDtos.iterator();
        List<Items> items = new ArrayList<>();
        while(i.hasNext()) {
            ItemsWithPointsDto itemsWithPointsDto = (ItemsWithPointsDto) i.next();
            items.add(itemsWithPointsDto.getItems());
        }

        return items;
    }

    @Override
    public List<Shops> convertShopsByTitleWithPointsDtoSortedSetToShopsList(SortedSet<ShopsByTitleWithPointsDto> shopsByTitleWithPointsDtoSortedSet) {
        Iterator i = shopsByTitleWithPointsDtoSortedSet.iterator();
        List<Shops> shops = new ArrayList<>();
        while(i.hasNext()) {
            ShopsByTitleWithPointsDto shopsWithPointsDto = (ShopsByTitleWithPointsDto) i.next();
            shops.add(shopsWithPointsDto.getShops());
        }

        return shops;
    }

    @Override
    public List<Shops> convertShopsByDescriptionWithPointsDtoSortedSetToShopsList(SortedSet<ShopsByDescriptionWithPointsDto> shopsByDescriptionWithPointsDtoSortedSet) {
        Iterator i = shopsByDescriptionWithPointsDtoSortedSet.iterator();
        List<Shops> shops = new ArrayList<>();
        while(i.hasNext()) {
            ShopsByDescriptionWithPointsDto shopsWithPointsDto = (ShopsByDescriptionWithPointsDto) i.next();
            shops.add(shopsWithPointsDto.getShops());
        }

        return shops;
    }

    /*@Override
    public int kmp(String input, String title) {
        int M = input.length();
        int N = title.length();

        int lps[] = new int[M];
        int j = 0;
        int maxFound=-1;

        computeLPSArray(input, M, lps);

        int i = 0;
        while (i < N) {
            if (input.charAt(j) == title.charAt(i)) {
                j++;
                i++;
            }
            if (j == M) {
                return M;
            } else if (i < N && input.charAt(j) != title.charAt(i)) {
                if (j != 0)
                    j = lps[j - 1];
                else
                    i = i + 1;
            }
        }
        return maxFound;
    }

    @Override
    public void computeLPSArray(String input, int M, int lps[]) {
        int len = 0;
        int i = 1;
        lps[0] = 0;

        while (i < M) {
            if (input.charAt(i) == input.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = len;
                    i++;
                }
            }
        }
    }

    @Override
    public int lcs(char[] title, char[] input) {
        int maxLength = 0;

        int titleLength = title.length;
        int inputLength = input.length;

        int[][] dp = new int[titleLength+1][inputLength+1];


        for(int i=0; i<=titleLength; i++) {
            for(int j=0; j<=inputLength; j++) {
                if (i == 0 || j == 0){
                    dp[i][j] = 0;
                    continue;
                }

                if(title[i-1] == ' ' || input[j-1] == ' ') {
                    dp[i][j] = 0;
                    continue;
                }

                if(title[i-1] == input[j-1]) {
                    dp[i][j] = dp[i-1][j-1] + 1;
                    if(dp[i][j]>maxLength)
                        maxLength = dp[i][j];
                    continue;
                }

                dp[i][j] = 0;
            }
        }

        return maxLength;
    }*/

    @Override
    public List<Items> findItemsBySubcategoryInList(Subcategories subcategories, List<Items> foundItems) {
        List<Items> newItems = new ArrayList<>();
        for(Items item : foundItems) {
            if(!item.getShop().getSubcategories().contains(subcategories))
                continue;

            newItems.add(item);
        }

        return newItems;
    }

    @Override
    public List<Items> findItemsByCategoryInList(Categories categories, List<Items> foundItems) {
        List<Subcategories> subcategories = findAllSubcategoriesByCategory(categories);

        List<Items> newItems = new ArrayList<>();

        for(Items item : foundItems) {
            Shops shop = item.getShop();
            for(Subcategories subcategory : subcategories) {
                if(shop.getSubcategories().contains(subcategory)) {
                    newItems.add(item);
                    break;
                }
            }
        }

        return newItems;
    }

    @Override
    public List<Shops> findAllShopsBySubcategoryInList(Subcategories subcategories, List<Shops> shopsFound) {
        List<Shops> newShops = new ArrayList<>();
        for(Shops shop : shopsFound) {
            if(!shop.getSubcategories().contains(subcategories))
                continue;
            newShops.add(shop);
        }

        return newShops;
    }

    @Override
    public List<Shops> findAllShopsByCategoryInList(Categories categories, List<Shops> shopsFound) {
        List<Subcategories> subcategories = findAllSubcategoriesByCategory(categories);
        List<Shops> newShops = new ArrayList<>();

        for(Shops shop : shopsFound) {
            for(Subcategories subcategory : subcategories) {
                if(shop.getSubcategories().contains(subcategory)) {
                    newShops.add(shop);
                    break;
                }

            }
        }

        return newShops;
    }

    @Override
    public List<GetCategoriesResponseDto> convertCategoriesToGetCategoriesResponseDto(List<Categories> categories) {
        return categoriesMapper.convertCategoriesToGetCategoriesResponseDto(categories);
    }

    @Override
    public List<GetSubcategoriesResponseDto> convertSubcategoriesToGetSubcategoriesResponseDto(List<Subcategories> subcategories) {
        return subcategoriesMapper.convertSubcategoriesToGetSubcategoriesResponseDto(subcategories);
    }

    @Override
    public List<GetSubcategoriesByCategoryRequestDto> convertSubcategoriesToGetSubcategoriesByCategoryRequestDto(List<Subcategories> subcategories) {
        return subcategoriesMapper.convertSubcategoriesToGetSubcategoriesByCategoryRequestDto(subcategories);
    }

    //JPA METHODS
    @Override
    public Categories findCategoryByName(String category) {
        return categoryRepository.findByName(category).orElse(null);
    }

    @Override
    public Subcategories findSubcategoryByName(String subcategory) {
        return subcategoriesRepository.findByName(subcategory).orElse(null);
    }

    @Override
    public List<Items> findAllItemsAndShopPublished() {
        return itemsRepository.findAllItemsAndShopPublished(true).orElse(null);
    }

    @Override
    public List<Items> findItemsByCategory(Categories categories) {
        List<Shops> shopsByCategory = findAllShopsByCategory(categories);
        List<Items> items = new ArrayList<>();

        for(Shops shop : shopsByCategory) {
            items.addAll(shop.getItems());
        }

        return items;

        /*//Get all subcategories for category
        List<Subcategories> subcategoriesList = findAllSubcategoriesByCategory(categories);
        //Find all items for each subcategory found
        List<Items> items = new ArrayList<>();
        for(Subcategories subcategory : subcategoriesList) {
            List<Items> foundItemsBySubcategory = findItemsBySubcategory(subcategory);

            if(foundItemsBySubcategory == null)
                continue;

            items.addAll(foundItemsBySubcategory);
        }

        return items;*/
    }

    @Override
    public List<Items> findItemsBySubcategory(Subcategories subcategories) {
        List<Items> items = new ArrayList<>();
        List<Shops> shopsList = findAllShopsBySubcategory(subcategories);

        for(Shops shop : shopsList) {
            List<Items> foundItemsBySubcategory = shop.getItems();
            if(foundItemsBySubcategory == null)
                continue;
            items.addAll(foundItemsBySubcategory);
        }

        return items.size() > 0 ? items : null;
    }

    @Override
    public List<Shops> findAllShopsBySubcategory(Subcategories subcategories) {
        return shopRepository.findAllBySubcategoriesAndActualSizeGreaterThan(subcategories, 0).orElseGet(ArrayList::new);
    }

    @Override
    public List<Subcategories> findAllSubcategoriesByCategory(Categories categories) {
        return subcategoriesRepository.findAllByCategory(categories).orElseGet(ArrayList::new);
    }

    @Override
    public Shops findShopById(String id) {
        return shopRepository.findById(id).orElse(null);
    }

    @Override
    public List<Shops> findAllShopsByCategory(Categories categories) {
        List<Shops> shops = new ArrayList<>();
        shops = shopRepository.findAllByCategories(categories).orElseGet(ArrayList::new);
        /*List<Subcategories> allSubcategoriesForCategory = findAllSubcategoriesByCategory(categories);

        for(Subcategories subcategories : allSubcategoriesForCategory) {
            shops.addAll(findAllShopsBySubcategory(subcategories));
        }*/

        return shops.size() > 0 ? shops : null;
    }

    @Override
    public List<Shops> findAllPublishedShops() {
        return shopRepository.findAllByIsPublishedAndActualSizeGreaterThan(true, 0).orElseGet(ArrayList::new);
    }

    @Override
    public List<Categories> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Subcategories> findAllSubcategories() {
        return subcategoriesRepository.findAll();
    }

    @Override
    public Categories findCategoryById(String categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    @Override
    public Subcategories findSubcategoryById(String subcategoryId) {
        return subcategoriesRepository.findById(subcategoryId).orElse(null);
    }

    @Override
    public List<Items> findItemsByCountyAndRating(Double rating, String county) {
        return itemsRepository.findItemsByShopCountyAndRatingAndPublished(true, county, rating).orElse(null);
    }

    @Override
    public List<Items> findItemsByCounty(String county) {
        return itemsRepository.findItemsByShopCountyAndPublished(true, county).orElse(null);
    }

    @Override
    public List<Items> findItemsByRating(Double rating) {
        return itemsRepository.findItemsByShopRatingAndPublished(true, rating).orElse(null);
    }

    @Override
    public List<Shops> findShopsByCountyAndRating(String county, Double rating) {
        return shopRepository.findAllByCountyEqualsAndRatingGreaterThanEqual(county, rating).orElse(null);
    }

    @Override
    public List<Shops> findShopsByRating(Double rating) {
        return shopRepository.findAllByRatingGreaterThanEqual(rating).orElse(null);
    }

    @Override
    public List<Shops> findShopsByCounty(String county) {
        return shopRepository.findAllByCountyEquals(county).orElse(null);
    }
}

import os
import django

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'api.settings')
django.setup()
import json
from deals.models import Deals

def categorySorter(category):
    categories = []
    foodKeywords = ["Fast Food, Restaurants, Dining, Groceries", "Wines"]
    entKeywords = ["Places of Interest, Entertainment, Tourist Attractions, Movies", "Apple, iPhone, MacBook, iPad, iTouch", "Computers, Tablets, Accessories, Software", "Body Fitness Gym", "Digital Cameras, DSLRs", "Games, Consoles, Xbox, Playstation, Nintendo, PC", "Mobile Phones Smartphones, Apple, Samsung, Sony, LG, HTC, Xiaomi etc", "iPhone", "Music, CDs, DVDs, Blu-Ray, Movies, Music CDs, Video Rental", "Singtel, Starhub & M1 Phones, Broadband & Pay TV Offers", "TVs, Speakers, Blu-Ray, Home Theatre, Headphones, Earphones", "Video Camcorders", "Web Hosting (Shared, VPS, Cloud, Dedicated, etc)"]
    retailKeywords = ["Beauty", "Cosmetics, Make-up, Brushes", "Decoratives, Collectibles", "Department Stores, Online Stores, Online Major Sales Events", "Eye Care & Optical Lens", "Fashion, Branded Apparel, Wallets, Accessories", "Footwear, Shoes, Slippers, Sneakers, Sandals", "Fragrances & Perfumes", "Handbags", "Furniture, Chairs, Sofa Sets, Dining Tables, Interior Designing, etc" "Health Products, Massagers", "Home Appliances, Washers, Fridges, Air Conditioners, Fans, Vacuum Cleaners", "Jewellery, Gold, Diamonds, Silver, Rings, Pendants, Chains, Bangles, Earrings", "Kitchenware, Cutlery, Pans, Cookers, Blenders, Cookware", "Personal Care, Creams, Soap, Shampoo, Toiletries", "Shopping Malls", "Sports, Golf, Accessories", "Warehouse, Clearance, Big Sales", "Watches"]

    for string1 in foodKeywords:
        if string1 in category:
            categories.append("Food")
            break

    for string2 in entKeywords:
        if string2 in category:
            categories.append("Entertainment")
            break

    for string3 in retailKeywords:
        if string3 in category:
            categories.append("Retail")
            break
    
    if len(categories) == 0:
        categories.append("Others")

    count = 0
    categoriesString = ""
    for string in categories:
        if count != 0:
            categoriesString += ","
        categoriesString += string
        count += 1
    
    return categoriesString


def deEmojify(inputString):
    return inputString.encode('ascii', 'ignore').decode('ascii')

with open('./onepair_crawler/onepair_crawler/spiders/data.json') as f:
    data = json.load(f)

for item in data:
    try:
        name = item['name']
    except KeyError:
        name = ""
    try:
        start = item['start']
    except KeyError:
        start = ""
    try:
        end = item['end']
    except KeyError:
        end = ""
    try:
        image = item['image']
    except KeyError:
        image = ""
    try:
        vendor = item['vendor']
    except KeyError:
        vendor = ""
    try:
        terms = item['terms']
        terms = deEmojify(terms)
    except KeyError:
        terms = ""
    try:
        category = item['category']
        category = categorySorter(category)
    except KeyError:
        category = ""

    '''
    print("1. Name: " + name)
    print("2. Start: " + start)
    print("3. End: " + end)
    print("4. Image: " + image)
    print(terms.encode())
    print("6. Category: " + category)
    print("===================================")'''
    if(not Deals.objects.all().filter(name=name)):
        print("Adding")
        deal = Deals(name=name, start=start, end=end, image=image, vendors=vendor, terms=terms, category=category)
        deal.save()
    
print("Success")

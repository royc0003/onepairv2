import scrapy
from .item import DealItem
import re
from scrapy.loader import ItemLoader
#scrapy crawl deals -o data.json

class DealsSpider(scrapy.Spider):
    name='deals'
    allowed_domains=['singpromos.com']
    start_urls = [
        'https://singpromos.com/tag/1-for-1/'
    ]

    def parse(self, response):
        count = 0
        for deal in response.xpath("//article[@class='mh-loop-item clearfix']"):
            
            i = ItemLoader(item=DealItem(), selector=deal)
            i.add_xpath('name', ".//div[@class='mh-loop-content clearfix']/header[@class='mh-loop-header']/h3/a/text()")
            i.add_xpath('start', ".//div[@class='mh-loop-content clearfix']/header[@class='mh-loop-header']/div[@class='mh-meta mh-loop-meta']/span[@class='mh-meta-date updated']/span[@class='hidden dtstart']/span[@class='value-title']/@title")
            i.add_xpath('end', ".//div[@class='mh-loop-content clearfix']/header[@class='mh-loop-header']/div[@class='mh-meta mh-loop-meta']/span[@class='mh-meta-date updated']/span[@class='hidden dtend']/span[@class='value-title']/@title")
            i.add_xpath('image', ".//div[@class='mh-loop-thumb']/a/img/@src"),
            i.add_xpath('vendor', ".//div[@class='mh-loop-content clearfix']/header[@class='mh-loop-header']/div[@class='mh-meta mh-loop-meta']/span[@class='mh-meta-date updated']/span[@class='hidden location']/text()")
            
            inner_page = response.xpath(".//div[@class='mh-loop-content clearfix']/header[@class='mh-loop-header']/h3/a/@href").extract()[count]
            count += 1
            yield scrapy.Request(url=inner_page, callback=self.parse2, dont_filter=True, meta={'deal_item': i}, priority=50)
            

        next_page = response.xpath("//div[@class='mh-loop-pagination clearfix']/a[@class='next page-numbers']/@href").extract_first()

        if next_page is not None:
            next_page_link = response.urljoin(next_page)
            print("next page")
            yield scrapy.Request(url=next_page, callback=self.parse)

    def parse2(self, response):
        print("EHEH")
        print(response)
        i = response.meta.get('deal_item')
        value = ""
        cat = ""
        for tag in response.xpath("//p[@class='mh-meta entry-meta']/span[@class='entry-meta-categories']/a/text()").extract():
            cat += tag + " "
        
        for paragraph in response.xpath("//div[@class='entry-content clearfix']/p | //div[@class='entry-content clearfix']/ul/li").extract():
            value += cleanhtml(paragraph) + " "
        i.add_value('terms', value)
        i.add_value('category', cat)
        yield i.load_item()

def cleanhtml(raw_html):
        cleanr = re.compile('<.*?>')
        cleantext = re.sub(cleanr, '', raw_html)
        return cleantext
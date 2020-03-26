from django.contrib import admin
from .models import Deals, ClientUsers, BlackLists, Choices, Requests

# Register your models here.
admin.site.register(Deals)
admin.site.register(ClientUsers)
admin.site.register(BlackLists)
admin.site.register(Choices)
admin.site.register(Requests)
from rest_framework import serializers
from .models import Deals, ClientUsers, BlackLists, Choices, Requests

class DealsSerializer(serializers.ModelSerializer):
    class Meta:
        model = Deals
        fields = '__all__'

class ClientUsersSerializer(serializers.ModelSerializer):
    class Meta:
        model = ClientUsers
        fields = '__all__'

class BlackListsSerializer(serializers.ModelSerializer):
    class Meta:
        model = BlackLists
        fields = '__all__'

class ChoicesSerializer(serializers.ModelSerializer):
    class Meta:
        model = Choices
        fields = '__all__'

class RequestsSerializer(serializers.ModelSerializer):
    class Meta:
        model = Requests
        fields = '__all__'
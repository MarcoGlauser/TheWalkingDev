from rest_framework import serializers
from user.models import AppUser


class AppUserSerializer(serializers.ModelSerializer):

    class Meta:
        model = AppUser

from rest_framework import serializers
from user.models import AppUser


class AppUserSerializer(serializers.ModelSerializer):
    total_steps = serializers.IntegerField(read_only=True)
    class Meta:
        model = AppUser

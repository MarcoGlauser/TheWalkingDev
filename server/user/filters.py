import django_filters
from user.models import AppUser


class UserFilter(django_filters.FilterSet):
    class Meta:
        model = AppUser
        fields = ['username', ]

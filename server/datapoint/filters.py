import django_filters
from datapoint.models import StepDiff
from rest_framework import filters
from django.utils.datetime_safe import datetime
from pytz import utc

class TimestampFilter(django_filters.NumberFilter):

    def convert_timestamp(self,timestamp):
        return datetime.fromtimestamp(int(timestamp),tz=utc)

    def filter(self, qs, value):
        return super(TimestampFilter,self).filter(qs,self.convert_timestamp(value))

class StepDiffFilter(django_filters.FilterSet):

    created_at_gte = TimestampFilter(name='created_at',lookup_type='gte')
    created_at_lte = TimestampFilter(name='created_at', lookup_type='lte')

    class Meta:
        model = StepDiff
        fields = ['user__id','created_at_gte','created_at_lte']


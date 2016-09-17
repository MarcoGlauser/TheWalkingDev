from datapoint.models import StepDiff
from rest_framework import serializers


class StepDiffSerializer(serializers.ModelSerializer):
    class Meta:
        model = StepDiff
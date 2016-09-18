from datapoint.filters import StepDiffFilter
from datapoint.models import StepDiff
from datapoint.serializers import StepDiffSerializer
from rest_framework import viewsets


class StepDiffViewSet(viewsets.ModelViewSet):
    """
    This viewset automatically provides `list` and `detail` actions.
    """
    queryset = StepDiff.objects.all()
    serializer_class = StepDiffSerializer
    filter_class = StepDiffFilter
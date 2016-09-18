from rest_framework import viewsets
from user.models import AppUser
from user.serializers import AppUserSerializer


class UserViewSet(viewsets.ModelViewSet):
    """
    This viewset automatically provides `list` and `detail` actions.
    """
    queryset = AppUser.objects.all()
    serializer_class = AppUserSerializer
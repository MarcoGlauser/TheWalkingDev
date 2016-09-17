from django.conf.urls import url, include
from rest_framework.routers import DefaultRouter
from datapoint import views

router = DefaultRouter()
router.register(r'step_diffs', views.StepDiffViewSet)

urlpatterns = [
    url(r'^', include(router.urls)),
]
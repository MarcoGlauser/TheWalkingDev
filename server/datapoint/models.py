from django.db import models

class StepDiff(models.Model):
    created_at = models.DateTimeField(auto_now_add=True)
    number_of_steps = models.IntegerField()
    user = models.ForeignKey('user.AppUser',related_name='step_diffs')
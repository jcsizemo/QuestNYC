from django.db import models

class nylandmark(models.Model):
	name = models.CharField(max_length = 150)
	latitude = models.FloatField()
	longitude = models.FloatField()
	address = models.CharField(max_length = 150)
	
	def __unicode__(self):
		return self.name
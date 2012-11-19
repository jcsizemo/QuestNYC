from django.db import models
from django.contrib.auth.models import User

# Create your models here.
class userPoints(models.Model):
	point = models.IntegerField()
	user = models.OneToOneField(User)
	
	def __unicode__(self):
		return str(self.point)
	
	def addPoint(self, number):
		self.point += number
		self.save()
	
	def getPoint(self):
		return self.point
		
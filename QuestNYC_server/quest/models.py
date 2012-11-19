from django.db import models
from django.contrib.auth.models import User
from landmarks.models import *
import math
from django import template
from quest.models import *
register = template.Library()

@register.filter
def multiply(number, multiplier):
	return number*multiplier


class quest(models.Model):
	owner = models.ForeignKey(User, null = True, related_name = "owner")
	name = models.CharField(max_length = 50)
	latitude = models.FloatField()
	longitude = models.FloatField()
	b1_lat = models.FloatField()
	b1_lon = models.FloatField()
	b2_lat = models.FloatField()
	b2_lon = models.FloatField()
	b3_lat = models.FloatField()
	b3_lon = models.FloatField()
	b4_lat = models.FloatField()
	b4_lon = models.FloatField()
	description = models.CharField(max_length = 150)
	userRating = models.ManyToManyField(User, null = True, blank = True, through = "rating")
	
	
	def __unicode__(self):
		return self.name
	#def getBoundary(self):
		
		#boundarySet = [(question.landmark.latitude, question.landmark.longitude, math.atan2(question.landmark.latitude-self.latitude, question.landmark.longitude-self.longitude)) for question in self.question_set.all()]
		#boundarySet.sort(key = lambda x: math.atan2(x[0] - self.latitude, x[1] - self.longitude))
		#return boundarySet
	
class question(models.Model):
	quest = models.ForeignKey(quest, null = True, blank = True)
	#landmark = models.ForeignKey(nylandmark)
	latitude = models.FloatField()
	longitude = models.FloatField()	
	sentence = models.CharField(max_length = 200)
	type = models.CharField(max_length = 30)
	difficulty = models.IntegerField()
	solved = models.ManyToManyField(User, null = True, blank = True, through = "solvedByUser")
	def __unicode__(self):
		return self.sentence
	
	def isSolved(self, user):
		return solvedByUser.objects.get(user = user, question = self).type
		
	def createIsSolved(self, user):
		sbu = solvedByUser(user = user, question = self)
		sbu.save()
		return sbu.type
		
	def updateIsSolved(self, user, value):
		sbu = solvedByUser.objects.get(user = user, question = self)
		sbu.type = value
		sbu.save()
	
class openQuestion(models.Model):
	answer = models.CharField(max_length = 200)
	question = models.OneToOneField(question)

class multipleChoice(models.Model):
	choice = models.CharField(max_length = 100)
	def __unicode__(self):
		return self.choice
	
class closedQuestion(models.Model):
	choices = models.ManyToManyField(multipleChoice, related_name='c+')
	answer = models.OneToOneField(multipleChoice, null = True, blank = True)
	question = models.OneToOneField(question)		

class locationQuestion(models.Model):
	longitude = models.FloatField()
	latitude = models.FloatField()
	question = models.OneToOneField(question)
	
class solvedByUser(models.Model):
	# 0 : not yet, 1 : incorrect, 2 : correct 
	type = models.SmallIntegerField(default = 0)
	user = models.ForeignKey(User)
	question = models.ForeignKey(question)
	
	def __unicode__(self):
		return "user : " + str(self.user) + ", question : " + str(self.question.id) + " , type : " + str(self.type)

class rating(models.Model):
	rate = models.SmallIntegerField(default = 3)
	user = models.ForeignKey(User)
	quest = models.ForeignKey(quest)	

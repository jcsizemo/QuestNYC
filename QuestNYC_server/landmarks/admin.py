from django.contrib import admin
from quest.models import *

class questionInline(admin.TabularInline):
	model = question

class questAdmin(admin.ModelAdmin):
    model = quest
    list_display = ['owner', 'name', 'longitude', 'latitude', 'description', 'b1_lon', 'b1_lat','b2_lon', 'b2_lat','b3_lon', 'b3_lat','b4_lon', 'b4_lat']
    inlines = [questionInline]

class questionAdmin(admin.ModelAdmin):
    model = question
    list_display = ['quest', 'sentence', 'type', 'difficulty', 'latitude', 'longitude'] 

class openquestionAdmin(admin.ModelAdmin):
    model = openQuestion
    list_display = ['answer', 'question'] 

class multiplechoiceAdmin(admin.ModelAdmin):
    model = multipleChoice
    list_display = ['choice'] 

class closedquestionAdmin(admin.ModelAdmin):
    model = closedQuestion
    list_display = ['answer', 'question'] 

class locationquestionAdmin(admin.ModelAdmin):
    model = locationQuestion
    list_display = ['longitude', 'latitude', 'question'] 


admin.site.register(quest, questAdmin)
admin.site.register(question, questionAdmin)
admin.site.register(openQuestion, openquestionAdmin)
admin.site.register(multipleChoice, multiplechoiceAdmin)
admin.site.register(closedQuestion, closedquestionAdmin)
admin.site.register(locationQuestion, locationquestionAdmin)

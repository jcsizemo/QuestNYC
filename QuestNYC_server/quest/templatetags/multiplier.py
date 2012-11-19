from django import template
from quest.models import *
register = template.Library()

@register.filter
def multiply(number, multiplier):
	return number*multiplier
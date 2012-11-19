from django.shortcuts import render_to_response, RequestContext
from django.contrib.auth.models import User

def index(request):
	
	# Check user logged in
	if not request.user.is_authenticated():
		return render_to_response('common/login.html', RequestContext(request))
	
	# Or, if logged in
	try:
		category = request.GET['category']
	except:
		category = 'art'
	
	if category == 'art':
		pictureList = ['andywarhol.jpg','keithharing.jpg', 'lichtenstein.png', 'monalisa.jpg', 'mondrian.jpg', 'vangogh.jpg']
	elif category == 'rti':
		pictureList = []	
	elif category == 'sp':
		pictureList = []	
		
		
	return render_to_response('art/index.html', {'category' : category, 'pictureList' : pictureList}, RequestContext(request))
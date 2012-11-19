from django.shortcuts import render_to_response, HttpResponse, redirect, RequestContext
from django.contrib.auth.models import User
from django.utils import simplejson
from django.contrib.auth import authenticate, login, logout
from users.models import *
from django.views.decorators.csrf import csrf_exempt

def index(request):
	# Check user logged in
	if not request.user.is_authenticated():
		return render_to_response('login.html', RequestContext(request))
	
	# Or, if logged in
	return render_to_response('index.html', RequestContext(request))

@csrf_exempt	
def signin(request):

	# Check validation of id and pw
	try:
		id = request.POST['username']
		pw = request.POST['password']
		
	except:
		return HttpResponse(simplejson.dumps({"success":"false","reason":"ID or PW insufficient!"}))
	
	user = authenticate(username = id, password = pw)

	if user is not None:
		if user.is_active:
			login(request, user)
			request.COOKIES['sessionid'] = "fce8026e61b94e9a6b68c5f27c5ad3ae"
			return HttpResponse(simplejson.dumps({"success":"true", "admin" : request.user.is_superuser, "sessionid" : request.COOKIES["sessionid"]}))
		else:
			return HttpResponse(simplejson.dumps({"success":"false","reason":"Please check your ID/PW one more!"}))			
	else:
		return HttpResponse(simplejson.dumps({"success":"false","reason":"Please check your ID/PW one more!"}))
		
@csrf_exempt		
def registration(request):
	try:
		nn = request.POST['nn']
		id = request.POST['username']
		pw = request.POST['password']
	except:
		return HttpResponse(simplejson.dumps({"success":"false","reason":"Data parsing Error"}))		

	try:	

		user = User.objects.create_user(username = id, password = pw)
		up = userPoints(point = 0, user = user)
		up.save()
		user.first_name = nn
		user.save()
		return HttpResponse(simplejson.dumps({"success":"true"}))		
	except:
		return HttpResponse(simplejson.dumps({"success":"false","reason":"Create user Error"}))		


def signout(request):

	logout(request)
	return redirect('/')
		
def signup(request):

	return render_to_response('signup.html')




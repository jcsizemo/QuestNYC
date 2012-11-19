from django.shortcuts import render_to_response, RequestContext
from django.contrib.auth.models import User
from django.contrib.auth import authenticate
from quest.models import *
from math import fabs
from django.views.decorators.csrf import csrf_exempt

def returnUser(id, pw):
	user = authenticate(username=id, password=pw)
	return user
	
def loadQuest(request):
	"""
	Return available list of quests according to current coordinate 
	"""
	try:
		longitude = float(request.GET['longitude'])
		latitude = float(request.GET['latitude'])
		id = request.GET['username']
		pw = request.GET['password']
	except:
		return render_to_response('quest.xml', {'result' : False})
	
	user = returnUser(id,pw)
	# Retrieve quest with margin
	margin = 0.5
	if user.is_superuser:
		quests = quest.objects.all()
	else:
		quests = quest.objects.filter(longitude__gte = longitude - margin, longitude__lte = longitude + margin, latitude__gte = latitude - margin, latitude__lte = latitude + margin).all()
	
	
	for qt in quests:
		average = 0.0
		for userRate in qt.rating_set.all():
			average += userRate.rate
		try:
			average /= qt.rating_set.count()
		except:
			average = 0.0
		
		qt.grade = 5*((average/5 + (4*margin - (math.fabs(longitude - qt.longitude) + math.fabs(latitude - qt.latitude)))/(4*margin))/2)
		if qt.grade < 0: qt.grade = 0
		
		# Check the percent how much user already solved (correct, incorrect including)
		try:
			qt.solvedRate = solvedByUser.objects.filter(user = user, question__in = qt.question_set.all(), type__in = [1,2] ).count()/float(qt.question_set.count())	
		except:
			qt.solvedRate = 0
				
	questList = list(quests)
	questList.sort(key= lambda x:x.grade, reverse=True)
	return render_to_response('quest.xml', {'result' : True, 'quests' : questList}, RequestContext(request))
	
	
def loadQuestion(request):
	"""
	Return list of questions contains in selected quest
	"""
	try:
		questId = request.GET['questid']
		id = request.GET['username']
		pw = request.GET['password']
	except:
		return render_to_response('question.xml', {'result' : False})
	
	# Retrieve available questions 
	questions = quest.objects.get(id=questId).question_set.all()
	user = returnUser(id,pw)
	
	for question in questions:
		try:
			question.isSolved = question.isSolved(user)
		except:	
			question.isSolved = question.createIsSolved(user)
			
	return render_to_response('question.xml', {'result' : True, 'questions' : questions}, RequestContext(request))
	
	
def selectQuestion(request):
	"""
	Return the sentence of question
	"""
	try:
		questionId = request.GET['questionid']
	except:
		return render_to_response('selectquestion.xml', {'result' : False})
	 
	try:
		q = question.objects.get(id=questionId)
	except:
		return render_to_response('selectquestion.xml', {'result' : False, 'reason':"no data"})
	
	#if q.type == "OpenQuestion":
	#	pass
	if q.type == "ClosedQuestion":
		q.choices = q.closedquestion.choices.all()
	#elif q.type == "LocationQuestion":
	#	q.longitude = q.locationquestion.longitude
	#	q.latitude = q.locationquestion.latitude
	
	return render_to_response('selectquestion.xml', {'result' : True, 'question' : q}, RequestContext(request))	
	
def answerQuestion(request):
	"""
	Return the answer's correctness
	"""
	try:
		questionId = request.GET['questionid']
		id = request.GET['username']
		pw = request.GET['password']
	except:
		return render_to_response('answerquestion.xml', {'result' : False, 'reason' : 'no question Id'})
		
	q = question.objects.get(id=questionId)	
	user = returnUser(id,pw)
	correct = False
	
	if q.type == "OpenQuestion":
		try:
			# Answer should be string
			userAnswer = request.GET['answer']
		except:
			return render_to_response('answerquestion.xml', {'result' : False, 'reason' : 'no open question answer'})	
		correct = (userAnswer.lower() == q.openquestion.answer.lower())
		
	elif q.type == "ClosedQuestion":
		try:
			# Answer should be ID, one of the multiple choices
			userAnswer = int(request.GET['answer'])
		except:
			return render_to_response('answerquestion.xml', {'result' : False, 'reason' : 'no closed question answer'})	
		correct = (userAnswer == q.closedquestion.answer.id)
		
	elif q.type == "LocationQuestion":
		try:
			userLon = float(request.GET['longitude'])
			userLat = float(request.GET['latitude'])
		except:
			return render_to_response('answerquestion.xml', {'result' : False, 'reason' : 'no location question answer'})	
		margin = 0.01	
		correct = ((userLon < q.locationquestion.longitude + margin) and (userLon > q.locationquestion.longitude - margin) and (userLat < q.locationquestion.latitude + margin) and (userLat > q.locationquestion.latitude - margin))

	if correct:
		q.updateIsSolved(user, 2)
		user.userpoints.addPoint(100)
	else:
		q.updateIsSolved(user, 1)
		user.userpoints.addPoint(-20)
		
	return render_to_response('answerquestion.xml', {'result' : True, 'point' : user.userpoints.point,'correct' : correct}, RequestContext(request))

@csrf_exempt
def deleteQuest(request):
	"""
	delete a quest
	"""
	try:
		quest_id = request.GET['questid']
		id = request.GET['username']
		pw = request.GET['password']
	except:
		return render_to_response('deletequest.xml', {'result' : False, 'reason' : 'parsing error'})
	user = returnUser(id,pw)
	q=quest.objects.get(id=quest_id)
	if user == q.owner or user.is_superuser:
		q.delete()
	else:
		return render_to_response('deletequest.xml', {'result' : False, 'reason' : 'permission insufficient'})
		
	return render_to_response('deletequest.xml', {'result' : True})

@csrf_exempt
def addQuest(request):
	"""
	create a default quest for the user
	"""
	try:
		name = request.GET['name']
		latitude = float(request.GET['latitude'])
		longitude = float(request.GET['longitude'])
		description = request.GET['description']
		b1_lon = request.GET['b1_lon']
		b1_lat = request.GET['b1_lat']
		b2_lon = request.GET['b2_lon']
		b2_lat = request.GET['b2_lat']
		b3_lon = request.GET['b3_lon']
		b3_lat = request.GET['b3_lat']
		b4_lon = request.GET['b4_lon']
		b4_lat = request.GET['b4_lat']
		id = request.GET['username']
		pw = request.GET['password']

	except:
		return render_to_response('addquest.xml', {'result' : False,'reason': "parsing error"})
	try:
		user = returnUser(id,pw)
		q=quest(owner=user,name=name,latitude=latitude,longitude=longitude,description=description,b1_lon=b1_lon,b1_lat=b1_lat, b2_lon=b2_lon,b2_lat=b2_lat, b3_lon=b3_lon,b3_lat=b3_lat,b4_lon=b4_lon,b4_lat=b4_lat)
		q.save()
	except:
		return render_to_response('addquest.xml', {'result' : False, 'reason': "transaction error"})
	
	return render_to_response('addquest.xml', {'result' : True, 'questid' : q.id})
	

@csrf_exempt
def addQuestion(request):
	"""
	to add a question
	"""
	try:
		quest_id = request.GET["questid"]
		type = request.GET["type"]
		sentence = request.GET["sentence"]
		longitude = request.GET["longitude"]
		latitude = request.GET["latitude"]
		difficulty = request.GET["difficulty"]
	except:
		return render_to_response('addquestion.xml', {'result' : False, 'reason' : 'parsing error'})
		
	try:	
		qt = quest.objects.get(id=quest_id)

		qtn = question(quest = qt, sentence = sentence, type = type, difficulty = difficulty, longitude = longitude, latitude = latitude)
		qtn.save()
	except:
		return render_to_response('addquestion.xml', {'result' : False, 'reason' : 'getting quest, adding question error'})		
	
	if type == "OpenQuestion":	
		try:
			answer = request.GET["answer"]
		except:
			return render_to_response('addquestion.xml', {'result' : False, 'reason' : 'parsing open question answer error'})			
		try:
			oqtn = openQuestion(question = qtn, answer = answer)
			oqtn.save()		
		except:
			return render_to_response('addquestion.xml', {'result' : False, 'reason' : 'adding open question error'})					
		
	elif type == "ClosedQuestion":
		try:
			answer_index = int(request.GET["answer_index"])
			mc = request.GET["choices"]
		except:
			return render_to_response('addquestion.xml', {'result' : False, 'reason' : 'parsing closed question answer error'})						

		try:
			cqtn = closedQuestion(question = qtn)
			cqtn.save()		
			cnt = 0		
			for choice in mc.split("|"):
				cnt += 1
				mc = multipleChoice(choice = choice)
				mc.save()
				if cnt == answer_index: cqtn.answer = mc
				cqtn.choices.add(mc)
				cqtn.save()
		except:
			return render_to_response('addquestion.xml', {'result' : False, 'reason' : 'adding closed question error'})						

		
	elif type == "LocationQuestion":
		try:
			longitude = float(request.GET['longitude'])
			latitude = float(request.GET['latitude'])
		except:
			return render_to_response('answerquestion.xml', {'result' : False, 'reason' : 'parsing location question error'})	

		try:
			lqtn = locationQuestion(question = qtn, longitude = longitude, latitude = latitude)
			lqtn.save()	
		except:
			return render_to_response('addquestion.xml', {'result' : False, 'reason' : 'adding location question error'})	
	
	return render_to_response('addquestion.xml', {'result' : True})													


def rateQuest(request):
	"""
	Saving user's rating on quest after finishing it
	"""
	try:
		questId = request.GET['questid']
		rate = int(request.GET['rate'])
		id = request.GET['username']
		pw = request.GET['password']
	except:
		# Reusing the template of answerquestion
		return render_to_response('ratingquest.xml', {'result' : False, 'reason':'parsing error'})
		
	try:
		user = returnUser(id,pw)
		q = quest.objects.get(id=questId)	
		r = rating(user = user, quest = q, rate = rate)
		r.save()
	except:
		# Reusing the template of answerquestion
		return render_to_response('ratingquest.xml', {'result' : False, 'reason' : 'saving error'})
				
	return render_to_response('ratingquest.xml', {'result' : True}, RequestContext(request))
"""
def upload_file(request):
    if request.method == 'POST':
		try:
			questionid = request.POST('questionid')
		except:
			return render_to_response('upload.xml', {'result' : False})
        form = UploadFileForm(request.POST, request.FILES)
        if form.is_valid():
            handle_uploaded_file(request.FILES['file'])
            return render_to_response('upload.xml', {'result' : False})	
    else:
        form = UploadFileForm()
    return render_to_response('upload.xml', {'result' : True})	

def handle_uploaded_file(file)
"""
"""
To-Do
1. Add quest and question
2. Delete quest and question by owner or admin
3. Photo question?
"""
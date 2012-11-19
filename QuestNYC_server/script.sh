#!/bin/bash
exec python manage.py run_gunicorn -b 0.0.0.0:80

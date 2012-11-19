# -*- coding: utf-8 -*-
import datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models


class Migration(SchemaMigration):

    def forwards(self, orm):
        # Adding model 'quest'
        db.create_table('quest_quest', (
            ('id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('owner', self.gf('django.db.models.fields.related.ForeignKey')(related_name='owner', null=True, to=orm['auth.User'])),
            ('name', self.gf('django.db.models.fields.CharField')(max_length=50)),
            ('latitude', self.gf('django.db.models.fields.FloatField')()),
            ('longitude', self.gf('django.db.models.fields.FloatField')()),
            ('b1_lat', self.gf('django.db.models.fields.FloatField')()),
            ('b1_lon', self.gf('django.db.models.fields.FloatField')()),
            ('b2_lat', self.gf('django.db.models.fields.FloatField')()),
            ('b2_lon', self.gf('django.db.models.fields.FloatField')()),
            ('b3_lat', self.gf('django.db.models.fields.FloatField')()),
            ('b3_lon', self.gf('django.db.models.fields.FloatField')()),
            ('b4_lat', self.gf('django.db.models.fields.FloatField')()),
            ('b4_lon', self.gf('django.db.models.fields.FloatField')()),
            ('description', self.gf('django.db.models.fields.CharField')(max_length=150)),
        ))
        db.send_create_signal('quest', ['quest'])

        # Adding model 'question'
        db.create_table('quest_question', (
            ('id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('quest', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['quest.quest'], null=True, blank=True)),
            ('latitude', self.gf('django.db.models.fields.FloatField')()),
            ('longitude', self.gf('django.db.models.fields.FloatField')()),
            ('sentence', self.gf('django.db.models.fields.CharField')(max_length=200)),
            ('type', self.gf('django.db.models.fields.CharField')(max_length=30)),
            ('difficulty', self.gf('django.db.models.fields.IntegerField')()),
        ))
        db.send_create_signal('quest', ['question'])

        # Adding model 'openQuestion'
        db.create_table('quest_openquestion', (
            ('id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('answer', self.gf('django.db.models.fields.CharField')(max_length=200)),
            ('question', self.gf('django.db.models.fields.related.OneToOneField')(to=orm['quest.question'], unique=True)),
        ))
        db.send_create_signal('quest', ['openQuestion'])

        # Adding model 'multipleChoice'
        db.create_table('quest_multiplechoice', (
            ('id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('choice', self.gf('django.db.models.fields.CharField')(max_length=100)),
        ))
        db.send_create_signal('quest', ['multipleChoice'])

        # Adding model 'closedQuestion'
        db.create_table('quest_closedquestion', (
            ('id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('answer', self.gf('django.db.models.fields.related.OneToOneField')(to=orm['quest.multipleChoice'], unique=True, null=True, blank=True)),
            ('question', self.gf('django.db.models.fields.related.OneToOneField')(to=orm['quest.question'], unique=True)),
        ))
        db.send_create_signal('quest', ['closedQuestion'])

        # Adding M2M table for field choices on 'closedQuestion'
        db.create_table('quest_closedquestion_choices', (
            ('id', models.AutoField(verbose_name='ID', primary_key=True, auto_created=True)),
            ('closedquestion', models.ForeignKey(orm['quest.closedquestion'], null=False)),
            ('multiplechoice', models.ForeignKey(orm['quest.multiplechoice'], null=False))
        ))
        db.create_unique('quest_closedquestion_choices', ['closedquestion_id', 'multiplechoice_id'])

        # Adding model 'locationQuestion'
        db.create_table('quest_locationquestion', (
            ('id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('longitude', self.gf('django.db.models.fields.FloatField')()),
            ('latitude', self.gf('django.db.models.fields.FloatField')()),
            ('question', self.gf('django.db.models.fields.related.OneToOneField')(to=orm['quest.question'], unique=True)),
        ))
        db.send_create_signal('quest', ['locationQuestion'])

        # Adding model 'solvedByUser'
        db.create_table('quest_solvedbyuser', (
            ('id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('type', self.gf('django.db.models.fields.SmallIntegerField')(default=0)),
            ('user', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['auth.User'])),
            ('question', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['quest.question'])),
        ))
        db.send_create_signal('quest', ['solvedByUser'])

        # Adding model 'rating'
        db.create_table('quest_rating', (
            ('id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('rate', self.gf('django.db.models.fields.SmallIntegerField')(default=3)),
            ('user', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['auth.User'])),
            ('quest', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['quest.quest'])),
        ))
        db.send_create_signal('quest', ['rating'])


    def backwards(self, orm):
        # Deleting model 'quest'
        db.delete_table('quest_quest')

        # Deleting model 'question'
        db.delete_table('quest_question')

        # Deleting model 'openQuestion'
        db.delete_table('quest_openquestion')

        # Deleting model 'multipleChoice'
        db.delete_table('quest_multiplechoice')

        # Deleting model 'closedQuestion'
        db.delete_table('quest_closedquestion')

        # Removing M2M table for field choices on 'closedQuestion'
        db.delete_table('quest_closedquestion_choices')

        # Deleting model 'locationQuestion'
        db.delete_table('quest_locationquestion')

        # Deleting model 'solvedByUser'
        db.delete_table('quest_solvedbyuser')

        # Deleting model 'rating'
        db.delete_table('quest_rating')


    models = {
        'auth.group': {
            'Meta': {'object_name': 'Group'},
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '80'}),
            'permissions': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['auth.Permission']", 'symmetrical': 'False', 'blank': 'True'})
        },
        'auth.permission': {
            'Meta': {'ordering': "('content_type__app_label', 'content_type__model', 'codename')", 'unique_together': "(('content_type', 'codename'),)", 'object_name': 'Permission'},
            'codename': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'content_type': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['contenttypes.ContentType']"}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '50'})
        },
        'auth.user': {
            'Meta': {'object_name': 'User'},
            'date_joined': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now'}),
            'email': ('django.db.models.fields.EmailField', [], {'max_length': '75', 'blank': 'True'}),
            'first_name': ('django.db.models.fields.CharField', [], {'max_length': '30', 'blank': 'True'}),
            'groups': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['auth.Group']", 'symmetrical': 'False', 'blank': 'True'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'is_active': ('django.db.models.fields.BooleanField', [], {'default': 'True'}),
            'is_staff': ('django.db.models.fields.BooleanField', [], {'default': 'False'}),
            'is_superuser': ('django.db.models.fields.BooleanField', [], {'default': 'False'}),
            'last_login': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now'}),
            'last_name': ('django.db.models.fields.CharField', [], {'max_length': '30', 'blank': 'True'}),
            'password': ('django.db.models.fields.CharField', [], {'max_length': '128'}),
            'user_permissions': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['auth.Permission']", 'symmetrical': 'False', 'blank': 'True'}),
            'username': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '30'})
        },
        'contenttypes.contenttype': {
            'Meta': {'ordering': "('name',)", 'unique_together': "(('app_label', 'model'),)", 'object_name': 'ContentType', 'db_table': "'django_content_type'"},
            'app_label': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'model': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '100'})
        },
        'quest.closedquestion': {
            'Meta': {'object_name': 'closedQuestion'},
            'answer': ('django.db.models.fields.related.OneToOneField', [], {'to': "orm['quest.multipleChoice']", 'unique': 'True', 'null': 'True', 'blank': 'True'}),
            'choices': ('django.db.models.fields.related.ManyToManyField', [], {'related_name': "'c+'", 'symmetrical': 'False', 'to': "orm['quest.multipleChoice']"}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'question': ('django.db.models.fields.related.OneToOneField', [], {'to': "orm['quest.question']", 'unique': 'True'})
        },
        'quest.locationquestion': {
            'Meta': {'object_name': 'locationQuestion'},
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'latitude': ('django.db.models.fields.FloatField', [], {}),
            'longitude': ('django.db.models.fields.FloatField', [], {}),
            'question': ('django.db.models.fields.related.OneToOneField', [], {'to': "orm['quest.question']", 'unique': 'True'})
        },
        'quest.multiplechoice': {
            'Meta': {'object_name': 'multipleChoice'},
            'choice': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'})
        },
        'quest.openquestion': {
            'Meta': {'object_name': 'openQuestion'},
            'answer': ('django.db.models.fields.CharField', [], {'max_length': '200'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'question': ('django.db.models.fields.related.OneToOneField', [], {'to': "orm['quest.question']", 'unique': 'True'})
        },
        'quest.quest': {
            'Meta': {'object_name': 'quest'},
            'b1_lat': ('django.db.models.fields.FloatField', [], {}),
            'b1_lon': ('django.db.models.fields.FloatField', [], {}),
            'b2_lat': ('django.db.models.fields.FloatField', [], {}),
            'b2_lon': ('django.db.models.fields.FloatField', [], {}),
            'b3_lat': ('django.db.models.fields.FloatField', [], {}),
            'b3_lon': ('django.db.models.fields.FloatField', [], {}),
            'b4_lat': ('django.db.models.fields.FloatField', [], {}),
            'b4_lon': ('django.db.models.fields.FloatField', [], {}),
            'description': ('django.db.models.fields.CharField', [], {'max_length': '150'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'latitude': ('django.db.models.fields.FloatField', [], {}),
            'longitude': ('django.db.models.fields.FloatField', [], {}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '50'}),
            'owner': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'owner'", 'null': 'True', 'to': "orm['auth.User']"}),
            'userRating': ('django.db.models.fields.related.ManyToManyField', [], {'symmetrical': 'False', 'to': "orm['auth.User']", 'null': 'True', 'through': "orm['quest.rating']", 'blank': 'True'})
        },
        'quest.question': {
            'Meta': {'object_name': 'question'},
            'difficulty': ('django.db.models.fields.IntegerField', [], {}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'latitude': ('django.db.models.fields.FloatField', [], {}),
            'longitude': ('django.db.models.fields.FloatField', [], {}),
            'quest': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['quest.quest']", 'null': 'True', 'blank': 'True'}),
            'sentence': ('django.db.models.fields.CharField', [], {'max_length': '200'}),
            'solved': ('django.db.models.fields.related.ManyToManyField', [], {'symmetrical': 'False', 'to': "orm['auth.User']", 'null': 'True', 'through': "orm['quest.solvedByUser']", 'blank': 'True'}),
            'type': ('django.db.models.fields.CharField', [], {'max_length': '30'})
        },
        'quest.rating': {
            'Meta': {'object_name': 'rating'},
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'quest': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['quest.quest']"}),
            'rate': ('django.db.models.fields.SmallIntegerField', [], {'default': '3'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['auth.User']"})
        },
        'quest.solvedbyuser': {
            'Meta': {'object_name': 'solvedByUser'},
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'question': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['quest.question']"}),
            'type': ('django.db.models.fields.SmallIntegerField', [], {'default': '0'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['auth.User']"})
        }
    }

    complete_apps = ['quest']